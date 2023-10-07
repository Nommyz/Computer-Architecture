import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assembler {

    private static boolean DEBUG = true;

    private static final Map<String, String> TYPE_MAP = Map.of(
            "add", "R",
            "nand", "R",
            "lw", "I",
            "sw", "I",
            "beq", "I",
            "jalr", "J",
            "halt", "O",
            "noop", "O",
            ".fill", "F");

    private static final Map<String, String> OPCODE_MAP = Map.of(
            "add", "000",
            "nand", "001",
            "lw", "010",
            "sw", "011",
            "beq", "100",
            "jalr", "101",
            "halt", "110",
            "noop", "111",
            ".fill", "FIL");
    private static final Map<String, Integer> NUMERIC_FIELD_COUNT_MAP = Map.of(
            "add", 3,
            "nand", 3,
            "lw", 2,
            "sw", 2,
            "beq", 2,
            "jalr", 2,
            "halt", 0,
            "noop", 0,
            ".fill", 0);
    private static final String MC_STARTER = "0000000"; // bits[31-25] (7 bits) always be 0
    private Tokenizer tok;
    private Map<String, Integer> labelMap = new HashMap<>();
    private int currentLine = 0;
    private List<String> lineData = new ArrayList<>();
    private List<String> machineCodes = new ArrayList<>();
    private String machineCode = MC_STARTER;

    Assembler(String assembly) {
        tok = new Tokenizer(assembly);
    }

    //////// Main Zone (Convert Assembly to Machine code)/////////
    /**
     * Loads a line of assembly instruction into lineData for compilation.
     */
    private void loadLine() {
        lineData = new ArrayList<>();
        while (tok.hasNext()) {
            String token = tok.next();
            System.out.print(token);
            if (isLineBreak(token)) {
                break;
            }

            lineData.add(token);
        }
    }
    /**
     * Converts the binary machine codes into decimal format.
     * @return The decimal machine codes.
     */
    public List<String> getDecimalMachineCodes() {
        List<String> dmc = new ArrayList<>();
        for (String mc : machineCodes) {
            dmc.add(toDecimal(mc) + "");
        }

        return dmc;
    }

    /**
     * Compile the assembly into binary machine codes.
     * @return The binary machine codes.
     */
    public List<String> assemblyIntoMachineCode() {
        // Iterate through the file once to fill labelMap
        loadLine();
        while (tok.hasNext()) { // If there is still a token, the file isn't fully read yet
            if (lineData.isEmpty()) {
                loadLine();
                continue;
            }

            if (DEBUG) {
                System.out.println("Filling labelMap, lineData: " + lineData);
            }

            // Check if the line starts with a label or an instruction, or otherwise
            if (!isInstruction(lineData.get(0))) {
                if (isValidLabel(lineData.get(0))) {
                    labelMap.put(lineData.get(0), currentLine);
                } else {
                    exit(1, "A: First token of the line is not a label or an instruction.");
                }
            }

            // Prepare for next loop
            currentLine++;
            loadLine();
        }


        if (DEBUG) {
            System.out.println(labelMap);
        }

        tok.resetIterator();
        currentLine = 0;

        // Iterate through the file again to compile each line into a machine code
        loadLine();
        while (tok.hasNext()) { // If there is still a token, the file isn't fully read yet
            int instIndex = 0;
            if (lineData.isEmpty()) {
                loadLine();
                continue;
            }

            if (DEBUG) {
                System.out.println("Compiling, lineData: " + lineData);
            }

            // Check if the line starts with a label or an instruction, or otherwise
            if (!isInstruction(lineData.get(instIndex))) {
                if (!isLabel(lineData.get(instIndex))) {
                    exit(1, "A: First token of the line is not a label or an instruction.");
                } else {
                    instIndex++;
                    if (!isInstruction(lineData.get(instIndex))) {
                        exit(1, "B: Second token of the line is not an instruction.");
                    }
                }
            }

            // Prepare for compilation
            String inst = lineData.get(instIndex);
            String opcode = OPCODE_MAP.get(inst);
            String type = TYPE_MAP.get(inst);

            int fieldCount = NUMERIC_FIELD_COUNT_MAP.get(inst);
            String[] fields = { "", "", "" };

            // Sets the machine code to start with 0000000 since those bits should always be zero
            machineCode = MC_STARTER;
            machineCode += opcode;

            // Convert numeric fields to number (numeric fields are fields that cannot be)
            for (int j = 0; j < fieldCount; ++j) {
                fields[j] = lineData.get(instIndex + 1 + j);
                // Check for anomaly
                if (!isInteger(fields[j]))
                    exit(1, "C1: fields[" + j + "] is not an integer.");
                int fieldInteger = toInteger(fields[j]);
                if (fieldInteger < 0 || fieldInteger > 7)
                    exit(1, "C2: fields[" + j + "] is out of range[0,7].");

                // Convert
                fields[j] = toBinaryString(fieldInteger);
                fields[j] = fillBits("0", fields[j], 3);
            }

            // Continue building the machine code string
            if (type.equals("R")) { // R-TYPE: add, nand
                machineCode += fields[0];
                machineCode += fields[1];
                machineCode += "0000000000000";
                machineCode += fields[2];

            } else if (type.equals("I")) { // I-TYPE: lw, sw, beq
                machineCode += fields[0];
                machineCode += fields[1];

                // Check if fields[2] is an integer or a symbolic address, or otherwise
                fields[2] = lineData.get(instIndex + 3);
                int offsetField = 0;
                if (DEBUG) System.out.println("field[2] = " + fields[2]);


                if (isInteger(fields[2])) { // Case: fields[2] is an integer

                    offsetField = toInteger(fields[2]);
                    if (DEBUG) System.out.println("field[2] isInteger = " + offsetField);

                } else if (isLabel(fields[2])) { // Case: fields[2] is a symbolic address

                    // Calculate offsetField
                    // Check if instruction is beq because beq has different offsetField calculation
                    if (inst.equals("beq")) {
                        // Address = PC + 1 + offsetField
                        // offsetField = Address - PC - 1
                        offsetField = labelMap.get(fields[2]) - currentLine - 1;
                    } else {
                        offsetField = labelMap.get(fields[2]);
                    }
                    if (DEBUG) System.out.println("field[2] isLabel = " + offsetField);

                } else {
                    exit(1, "D: Invalid fields[2] for I-Type instruction.");
                }

                // Check for anomaly
                if (offsetField < -32768 || offsetField > 32767)
                    exit(1, "E: beq offsetField out of range[-32768,32767]");

                // Convert offsetFields into binary string
                String offsetFieldBin;
                if (offsetField >= 0) {
                    offsetFieldBin = toBinaryString(offsetField);
                    offsetFieldBin = fillBits("0", offsetFieldBin, 16);

                } else {
                    offsetField = -offsetField;
                    offsetFieldBin = toBinaryString(offsetField);
                    offsetFieldBin = fillBits("0", offsetFieldBin, 16);
                    offsetFieldBin = twosCompliment(offsetFieldBin);
                }
                machineCode += offsetFieldBin;

            } else if (type.equals("J")) { // J-TYPE: jalr
                machineCode += fields[0];
                machineCode += fields[1];
                machineCode += "0000000000000000";

            } else if (type.equals("O")) { // O-TYPE: halt, noop
                machineCode += "0000000000000000000000";

            } else if (inst.equals(".fill")) { // .fill
                machineCode = "";
                String field = lineData.get(instIndex + 1); // .fill only one field
                if(DEBUG) System.out.println("instIndex: " + instIndex + " field: " + field);

                // Check if the field is an integer or a symbolic address, or otherwise
                if (isInteger(field)) { // Case: field is an integer

                    int dec = toInteger(field);
                    String bin;
                    if (dec < 0) {
                        dec = -dec;
                        bin = toBinaryString(dec);
                        bin = fillBits("0", bin, 32);
                        machineCode = twosCompliment(bin);
                    } else {
                        bin = toBinaryString(dec);
                        machineCode = fillBits("0", bin, 32);
                    }

                } else if (isLabel(field)) { // Case: field is a symbolic address

                    int dec = labelMap.get(field);
                    String bin = toBinaryString(dec);
                    machineCode = bin;
                    machineCode = fillBits("0", machineCode, 32);

                } else {
                    exit(1, "F: Invalid field for .fill instruction.");
                }
            }
            if (DEBUG) System.out.println("machineCode: " + machineCode);

            machineCodes.add(machineCode);
            currentLine++;
            loadLine();
        }

        return new ArrayList<String>(machineCodes);
    }


    /////// Binary Operation Zone//////
    /**
     * Converts a decimal integer into a binary string.
     * @param dec The decimal integer.
     * @return The binary string.
     */
    public static String toBinaryString(int dec) {
        if (DEBUG) System.out.print("toBinaryString(" + dec + "): ");

        StringBuilder bin = new StringBuilder("");

        if (dec == 0 || dec == 1) {
            bin.append(dec);
            if (DEBUG) System.out.println(bin);
            return bin.toString();
        }

        while (dec > 0) {
            bin.append(dec % 2);
            dec /= 2;
        }

        bin.reverse();

        if (DEBUG) System.out.println(bin);

        return bin.toString();
    }
    /**
     * Converts a token into an integer.
     * @param token The token to be converted.
     * @return The integer.
     */
    public static int toInteger(String token) {
        return Integer.parseInt(token);
    }
    /**
     * Converts a binary string into a decimal integer.
     * @param bin The binary string.
     * @return decimal integer.
     */
    public static int toDecimal(String bin) {
        if (DEBUG) System.out.print("toDecimal(" + bin + "): ");

        int dec = 0;
        int power = 0;

        if (bin.charAt(0) == '0') {
            for (int i = bin.length() - 1; i >= 0; i--) {
                dec += toInteger(bin.charAt(i) + "") * Math.pow(2, power);
                power++;
            }
        } else {
            bin = twosCompliment(bin);
            for (int i = bin.length() - 1; i >= 0; i--) {
                dec += toInteger(bin.charAt(i) + "") * Math.pow(2, power);
                power++;
            }
            dec = -dec;
        }

        if (DEBUG) System.out.println(dec);

        return dec;
    }
    /**
     * Perform 2's compliment on the binary string.
     * @param bin The binary string to be converted.
     * @return result of binary string in 2's compliment form.
     */
    public static String twosCompliment(String bin) {
        String twos = "", ones = "";

        for (int i = 0; i < bin.length(); i++) {
            ones += flip(bin.charAt(i));
        }
        StringBuilder builder = new StringBuilder(ones);

     //Plus one
        for (int i = ones.length() - 1; i > 0; i--) {
            if (ones.charAt(i) == '1') {
                builder.setCharAt(i, '0');
            } else {
                builder.setCharAt(i, '1');
                break;
            }
        }
        twos = builder.toString(); // completely twos compliment(filpbit + 1)

        if (DEBUG) System.out.println("twosCompliment(" + bin + "): " + twos);
        return twos;
    }

    /**
     * Flips a character bit from '0' to '1'
     */
    public static char flip(char c) {
        if (c == '0') {
            return '1';
        }
        return '0';
    }
    /**
     * Fill the {@code base} binary string with {@code filler} until its length is {@code length}.
     * @param filler The character that will be used to fill.
     * @param base The base binary string.
     * @param length The length of the resulting binary string.
     * @return A binary string of length {@code length}.
     */
    public static String fillBits(String filler, String base, int length) {
        StringBuilder sb = new StringBuilder("");

        while (base.length() + sb.length() < length) {
            sb = sb.append(filler);
        }
        sb = sb.append(base);

        return sb.toString();
    }


    /////// Checking Zone///////
    public static boolean isInteger(String token) {
        try{
            int number = Integer.parseInt(token);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    /**
     * Checks whether the {@code token} is a line break character.
     * @param token The token to be checked.
     * @return {@code True} if {@code token} is a line break character.
     */
    private static boolean isLineBreak(String token) {
        return token.equals("\n");
    }

    /**
     * Checks whether the {@code token} is an instruction or a .fill.
     * @param token The token to be checked.
     * @return {@code True} if {@code token} is an instruction or a .fill.
     */
    private static boolean isInstruction(String token) {
        return TYPE_MAP.containsKey(token);
    }

    /**
     * Checks whether the {@code token} is a label.
     * @param token The token to be checked.
     * @return {@code True} if {@code token} is a label.
     */
    private boolean isLabel(String token) {
        System.out.println("isLabel: " + token + " " + labelMap.containsKey(token));
        return labelMap.containsKey(token);
    }

    /**
     * Checks whether the {@code token} is passes the constraints of being a label.
     * Used in adding new label to the {@code labelMap}.
     * @param token The token to be checked.
     * @return {@code True} if {@code token} is a label.
     */
    private boolean isValidLabel(String token) {
        return !(token.length() > 6
                || labelMap.containsKey(token)
                || isInteger(token.charAt(0) + ""));
    }


    /////////System methods Zone////////
    /**
     * Call System.exit() with {@code exitCode} and announce the exit with {@code message}.
     * @param exitCode The exit code.
     * @param message The exit message.
     */
    public void exit(int exitCode, String message) {
        System.out.println("Exited with status " + exitCode);
        System.out.println("Exit message: " + message);
        System.exit(exitCode);
    }

}