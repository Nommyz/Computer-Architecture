import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class FileReaderWriter {

    /**
     * reads a file and turns it into a single long String
     * 
     * @param path the path to the source file
     * @return a single String of every line of data in the source file
     */
    public static String readFileToString(String path) {
        String data = "";
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                data += sc.nextLine() + " \n ";
            }
            data += " \n ";
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            // e.printStackTrace();
        }
        return data;
    }

    /**
    * writes the data in {@code strings} 
     * 
     * @param path the path to the output file
     * @param strings the List<String> that will be written
     */
    public static void writeStringToFile(String path, List<String> strings) {
        try {
            System.out.println("Writing to " + path + " .....");
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for (String str : strings) {
                writer.write(str);
                writer.write("\n");
            }
            writer.close();
            System.out.println("Finished writing.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            System.out.println("path: " + path);
            e.printStackTrace();
        }
        
    }
}