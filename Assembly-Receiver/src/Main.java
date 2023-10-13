import java.util.List;

public class Main {
    private static String SRC_DIR = "assemblyFiles/";
    private static String SRC_EXT = ".s";
    private static String OUTPUT_DIR = "machineCodes/";

    public static void main(String[] args) throws Exception {

         compile("test1");
         compile("test2");
    }

    /**
     * Compile a single assembly file
     * @param fileName the name of the assembly file to be compiled
     */
    public static void compile(String fileName){
        Assembler as = new Assembler(FileReaderWriter.readFileToString(SRC_DIR+fileName+SRC_EXT));

        List<String> machineCodes = as.assemblyIntoMachineCode(); // binary
        List<String> decimalMachineCodes = as.getDecimalMachineCodes(); //decimal
        System.out.println(machineCodes);
        FileReaderWriter.writeStringToFile(OUTPUT_DIR + fileName+".bin", machineCodes);
        FileReaderWriter.writeStringToFile(OUTPUT_DIR + fileName+".txt", decimalMachineCodes);
    }
}
