import java.util.*;

public class Assembler {
    private static boolean DEBUG = true;

    // Map of assembly instruction type (R, I, J, O, F)
    private static final Map<String, String> TYPE_MAP = Map.of(
            "add", "R",
            "nand", "R",
            "lw", "I",
            "sw", "I",
            "beq", "I",
            "jalr", "J",
            "halt", "O",
            "noop", "O",
            ".fill", "F"
    );

    // Map of assembly instruction opcodes
    private static final Map<String, String> OPCODE_MAP = Map.of(
            "add", "000",
            "nand", "001",
            "lw", "010",
            "sw", "011",
            "beq", "100",
            "jalr", "101",
            "halt", "110",
            "noop", "111",
            ".fill", "FIL"
    );

    // Map of the number of numeric fields for each instruction
    private static final Map<String, Integer> NUMERIC_FIELD_COUNT_MAP = Map.of(
            "add", 3,
            "nand", 3,
            "lw", 2,
            "sw", 2,
            "beq", 2,
            "jalr", 2,
            "halt", 0,
            "noop", 0,
            ".fill", 0
    );

    // Initial part of machine code for all instructions
    private static final String MC_STARTER = "0000000";

    private Tokenizer tok;
    private Map<String, Integer> labelMap = new HashMap<>();
    private int currentLine = 0;
    private List<String> lineData = new ArrayList<>();
    private List<String> machineCodes = new ArrayList<>();
    private String machineCode = MC_STARTER;

    /**
     * Constructor for the Assembler class.
     *
     * @param assembly The assembly code to be processed.
     */
    public Assembler(String assembly) {
        tok = new Tokenizer(assembly);
    }

    /**
     * Get the machine codes in decimal format.
     *
     * @return A list of decimal machine codes.
     */
    public List<String> getDecimalMachineCodes() {
        List<String> dmc = new ArrayList<>();
        for (String mc : machineCodes) {
            dmc.add(toDecimal(mc) + "");
        }
        return dmc;
    }
    ////////// Main Zone ////////////
    /**
     * Assemble the given assembly code into machine code.
     *
     * @return A list of machine codes in binary format.
     */
    public List<String> assemblyIntoMachineCode() {
        fillLabelMap();
        tok.resetIterator();
        currentLine = 0;
        loadLine();

        while (tok.hasNext()) {
            compileLine();
            machineCodes.add(machineCode);
            currentLine++;
            loadLine();
        }
        System.out.println(machineCode + "dsddddd");
        return machineCodes;
    }

    // Load a line of assembly instruction into lineData for compilation
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

    // Fill the labelMap with labels and their corresponding line numbers
    private void fillLabelMap() {
        loadLine();
        while (tok.hasNext()) {
            if (lineData.isEmpty()) {
                loadLine();
                continue;
            }

            if (isInstruction(lineData.get(0)) || (isValidLabel(lineData.get(0)) && !labelMap.containsKey(lineData.get(0)))) {
                labelMap.put(lineData.get(0), currentLine);
            } else {
                exit(1, "A: First token of the line is not a label or an instruction or duplicate label.");
            }

            currentLine++;
            loadLine();
        }
    }

    // Compile a single line of assembly code into machine code
    private void compileLine() {
        int instIndex = 0;
        while (lineData.isEmpty()) {
            loadLine();
        }

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

        String inst = lineData.get(instIndex);
        String opcode = OPCODE_MAP.get(inst);
        String type = TYPE_MAP.get(inst);
        int fieldCount = NUMERIC_FIELD_COUNT_MAP.get(inst);
        String[] fields = new String[fieldCount];

        machineCode = MC_STARTER + opcode;

        for (int j = 0; j < fieldCount; ++j) {
            fields[j] = lineData.get(instIndex + 1 + j);
            if (!isInteger(fields[j]))
                exit(1, "C1: fields[" + j + "] is not an integer.");
            int fieldInteger = toInteger(fields[j]);
            if (fieldInteger < 0 || fieldInteger > 7)
                exit(1, "C2: fields[" + j + "] is out of range[0,7].");
            fields[j] = toBinaryString(fieldInteger);
            fields[j] = fillBits("0", fields[j], 3);
        }

        if (type.equals("R")) {
            machineCode += fields[0] + fields[1] + "0000000000000" + fields[2];
        } else if (type.equals("I")) {
            machineCode += fields[0] + fields[1];
            String offsetField = calculateOffsetField(lineData.get(instIndex + 3), inst);
            machineCode += offsetField;
        } else if (type.equals("J")) {
            machineCode += fields[0] + fields[1] + "0000000000000000";
        } else if (type.equals("O")) {
            machineCode += "0000000000000000000000";
        } else if (inst.equals(".fill")) {
            machineCode = processFillInstruction(lineData.get(instIndex + 1));
        }

        if (DEBUG) System.out.println("machineCode: " + machineCode);
    }

    // Calculate the offset field for I-type instructions
    private String calculateOffsetField(String field, String inst) {
        int offsetField = 0;
        if (isInteger(field)) {
            offsetField = toInteger(field);
        } else if (isLabel(field)) {
            if (inst.equals("beq")) {
                offsetField = labelMap.get(field) - currentLine - 1;
            } else {
                offsetField = labelMap.get(field);
            }
        } else {
            exit(1, "D: Invalid field for I-Type instruction.");
        }

        if (offsetField < -32768 || offsetField > 32767)
            exit(1, "E: beq offsetField out of range[-32768,32767]");

        String offsetFieldBin = (offsetField >= 0) ? toBinaryString(offsetField) : twosCompliment(toBinaryString(-offsetField));
        return fillBits("0", offsetFieldBin, 16);
    }

    // Process .fill instructions and convert them to machine code
    private String processFillInstruction(String field) {
        String machineCode = "";
        if (isInteger(field)) {
            int dec = toInteger(field);
            String bin;
            if (dec < 0) {
                dec = -dec;
                bin = toBinaryString(dec);
                bin = (bin.length() == 32) ? bin : twosCompliment(fillBits("0", bin, 32));
                machineCode = bin;
            } else {
                bin = toBinaryString(dec);
                machineCode = (bin.length() == 32) ? bin : fillBits("0", bin, 32);
            }
        } else if (isLabel(field)) {
            int dec = labelMap.get(field);
            String bin = fillBits("0", toBinaryString(dec), 32);
            machineCode = bin;
        } else {
            exit(1, "F: Invalid field for .fill instruction.");
        }
        return machineCode;
    }

    ////////// Binary Operation Zone ////////////

    // Convert an integer to a binary string
    public static String toBinaryString(int dec) {
        StringBuilder bin = new StringBuilder("");
        if (dec == 0 || dec == 1) {
            bin.append(dec);
            return bin.toString();
        }
        while (dec > 0) {
            bin.append(dec % 2);
            dec /= 2;
        }
        bin.reverse();
        return bin.toString();
    }

    // Convert a binary string to an integer
    public static int toInteger(String token) {
        return Integer.parseInt(token);
    }

    // Convert a binary string to a decimal integer
    public static int toDecimal(String bin) {
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
        return dec;
    }

    // Calculate the two's complement of a binary string
    public static String twosCompliment(String bin) {
        String twos = "", ones = "";

        for (int i = 0; i < bin.length(); i++) {
            ones += flip(bin.charAt(i));
        }
        StringBuilder builder = new StringBuilder(ones);

        for (int i = ones.length() - 1; i > 0; i--) {
            if (ones.charAt(i) == '1') {
                builder.setCharAt(i, '0');
            } else {
                builder.setCharAt(i, '1');
                break;
            }
        }
        twos = builder.toString();
        return twos;
    }

    // Flip a binary digit (0 to 1, and 1 to 0)
    public static char flip(char c) {
        return (c == '0') ? '1' : '0';
    }

    // Fill bits of a binary string to a specified length
    public static String fillBits(String filler, String base, int length) {
        StringBuilder sb = new StringBuilder("");
        while (base.length() + sb.length() < length) {
            sb = sb.append(filler);
        }
        sb = sb.append(base);
        return sb.toString();
    }

    ////////// Checking Zone ////////////

    // Check if a token is an integer
    public static boolean isInteger(String token) {
        try {
            Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // Check if a token represents a line break (newline character)
    private static boolean isLineBreak(String token) {
        return token.equals("\n");
    }

    // Check if a token is a valid assembly instruction
    private static boolean isInstruction(String token) {
        return TYPE_MAP.containsKey(token);
    }

    // Check if a token is a label that exists in the labelMap
    private boolean isLabel(String token) {
        return labelMap.containsKey(token);
    }

    // Check if a token is a valid label (alphanumeric, no longer than 6 characters)
    private boolean isValidLabel(String token) {
        return !(token.length() > 6 || labelMap.containsKey(token) || !Character.isLetter(token.charAt(0)));
    }

    ///////// System Zone //////////

    // Exit the program with an exit code and message
    public void exit(int exitCode, String message) {
        System.out.println("Exited with status " + exitCode);
        System.out.println("Exit message: " + message);
        System.exit(exitCode);
    }
}

