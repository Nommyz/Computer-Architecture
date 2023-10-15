import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Simulator {
    private static final int NUMMEMORY = 65536; // maximum number of words in memory
    private static final int NUMREGS = 8; // number of machine registers
    private static final int MAXLINELENGTH = 50;


    public static class StateStruct{
        int pc = 0;
        int[] mem = new int[NUMMEMORY];
        int[] reg = new int[NUMREGS];
        int numMemory ;
    }

    public static void printState(StateStruct state){
        int i;
        System.out.println("\n@@@\nstate: ");
        System.out.println("\tpc " + state.pc);
        System.out.println("\tmemory: ");
        for (i=0; i<state.numMemory; i++) {
            System.out.println("\t\tmem[ " + i + " ] " + state.mem[i]);
        }
        System.out.println("\tregisters: ");
        for (i=0; i < NUMREGS; i++) {
            System.out.println("\t\treg[ " + i + " ] " + state.reg[i]);
        }
        System.out.println("end state");
    }

    public static void main(String[] args) throws FileNotFoundException {

        StateStruct state = new StateStruct();

        try {
            File myText = new File("Simulator/machine-code.txt");
            Scanner s = new Scanner(myText);
            while (s.hasNextLine()) {
                String data = s.nextLine();
                state.mem[state.numMemory] = Integer.parseInt(data);
//                System.out.println(state.mem[state.numMemory]+ "\n");
                state.numMemory++;

            }
        }catch (FileNotFoundException E){
            System.out.println("Error Files not found");
            E.printStackTrace();
            System.exit(1);
        }

        boolean isHalt = false;
        int[] arg = new int[3];
        int RegA , RegB , DestReg , Offset;
        for(int i = 1; i <= state.numMemory ; i++){
            if(isHalt == false){
                printState(state);
                switch (state.mem[state.pc] >> 22){
                    case 0: //add 000
                        RFormat(state.mem[state.pc],arg);
                        RegA = state.reg[arg[0]];
                        RegB = state.reg[arg[1]];
                        DestReg = RegA + RegB;
                        state.reg[arg[2]] = DestReg;

                        state.pc = state.pc + 1;
                        break;
                    case 1: //nand 001
                        RFormat(state.mem[state.pc],arg);
                        RegA = state.reg[arg[0]];
                        RegB = state.reg[arg[1]];
                        //ขั้นตอนทำ NAND
                        String binA = Integer.toBinaryString(RegA); //แปลง RegA เป็น Binary
                        String binB = Integer.toBinaryString(RegB); //แปลง RegB เป็น Binary
                        //ทำให้จำนวนหลักเท่ากันก่อนเทียบ
                        while(binA.length() != binB.length()){
                            if(binA.length() > binB.length()){
                                binB = "0" + binB;
                            }else{
                                binA = "0" + binA;
                            }
                        }
                        String nand_str = "";
                        // NAND
                        for(int a = 0 ; a < binA.length() ; a++){
                            if(binA.charAt(a) == '1' && binB.charAt(a) == '1'){
                                nand_str += "0";
                            }else{
                                nand_str += "1";
                            }
                        }
                        DestReg = Integer.parseInt(nand_str, 2);
                        state.reg[arg[2]] = DestReg;

                        state.pc = state.pc + 1;
                        break;
                    case 2: //lw 010
                        IFormat(state.mem[state.pc],arg);
                        RegA = state.reg[arg[0]];
                        Offset = arg[2];
                        RegB = state.mem[RegA + Offset];
                        state.reg[arg[1]] = RegB;

                        state.pc = state.pc + 1;
                        break;
                    case 3: //sw 011
                        IFormat(state.mem[state.pc],arg);
                        RegA = state.reg[arg[0]];
                        RegB = state.reg[arg[1]];
                        Offset = arg[2];
                        state.mem[RegA + Offset] = RegB;

                        state.pc = state.pc + 1;
                        break;
                    case 4: // beq 100
                        IFormat(state.mem[state.pc],arg);
                        RegA = state.reg[arg[0]];
                        RegB = state.reg[arg[1]];
                        Offset = arg[2];
                        if(RegA == RegB){
                            state.pc = state.pc + 1 + Offset;
                            //                        state.pc = state.pc + Offset;
                        }else{
                            state.pc = state.pc + 1;
                        }
                        break;
                    case 5: // jalr 101
                        JFormat(state.mem[state.pc],arg);
                        RegA = state.reg[arg[0]];
                        RegB = state.reg[arg[1]];
                        if(arg[0] == arg[1]){
                            RegB = state.pc + 1;
                            state.reg[arg[1]] = RegB;
                            state.pc = state.pc + 1;
                        }else{
                            RegB = state.pc + 1;
                            state.reg[arg[1]] = RegB;
                            state.pc = RegA;
                        }
                        break;
                    case 6: // halt 110
                        OFormat(state.mem[state.pc],arg);
                        isHalt = true;
                        System.out.println("machine halted");
                        System.out.println("total of " + i + " instructions executed");
                        System.out.println("final state of machine:");

                        state.pc = state.pc + 1;
                        break;
                    case 7: //ไม่ทำอะไร
                        OFormat(state.mem[state.pc],arg);

                        state.pc = state.pc + 1;
                        break;
                }
            }else{
                break;
            }
        }
        printState(state);
    }

    /**
     * using this method to assign Bits into arg for RFormat
     * @param BitNum using BitNum to calculate logic and assign to arg
     * @param arg using arg for waiting BitNum to calculate logic
     */
    private static void RFormat(int BitNum, int[] arg){ //r-format
        arg[0] = (BitNum & (7 << 19 )) >> 19; // regA เอา bit ที่ 21-19
        arg[1] = (BitNum & (7 << 16 )) >> 16; // regB เอา bit ที่ 18-16
        arg[2] = BitNum & 7; // destReg เอา bit ที่ 2-0
    }

    /**
     * using this method to assign Bits into arg for IFormat
     * @param BitNum using BitNum to calculate logic and assign to arg
     * @param arg using arg for waiting BitNum to calculate logic
     */
    private static void IFormat(int BitNum, int[] arg){
        arg[0] = (BitNum & (7 << 19)) >> 19; // regA เอา bit ที่ 21-19
        arg[1] = (BitNum & (7 << 16)) >> 16; // regB เอา bit ที่ 18-16

        if(((BitNum >> 15) & 1) == 1){ // OffsetField เอา bit ที่ 15-0 โดยเป็น 2s' complement
            arg[2] = (BitNum & 32767) - (BitNum & (1 << 15)); // กรณี 1 OffsetField เป็น -
        }else{
            arg[2] = BitNum & 32767; // กรณี 0 OffsetField เป็น +
        }
    }

    /**
     * using this method to assign Bits into arg for JFormat
     * @param BitNum using BitNum to calculate logic and assign to arg
     * @param arg using arg for waiting BitNum to calculate logic
     */
    private static void JFormat(int BitNum, int[] arg){
        arg[0] = (BitNum & (7 << 19)) >> 19; // regA เอา bit ที่ 21-19
        arg[1] = (BitNum & (7 << 16)) >> 16; // regB เอา bit ที่ 18-16
    }

    /**
     * using this method to assign Bits into arg for OFormat
     * @param BitNum using BitNum to calculate logic and assign to arg
     * @param arg using arg for waiting BitNum to calculate logic
     */
    private static void OFormat(int BitNum, int[] arg){

    }
}