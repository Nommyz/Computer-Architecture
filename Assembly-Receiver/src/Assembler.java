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
        twos = builder.toString(); // completely (filpbit + 1)

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
}