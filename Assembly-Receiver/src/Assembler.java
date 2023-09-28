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
}