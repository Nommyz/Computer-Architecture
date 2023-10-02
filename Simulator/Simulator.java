import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Simulator {
    private static final int NUMMEMORY = 65536; // maximum number of words in memory
    private static final int NUMREGS = 8; // number of machine registers
    private static final int MAXLINELENGTH = 1000;


    public static class StateStruct{
        int pc;
        int[] mem = new int[NUMMEMORY];
        int[] reg = new int[NUMREGS];
        int numMemory;
    }

    public static void printState(StateStruct state){
    int i;
    System.out.println("\n@@@\nstate: ");
    System.out.println("\tpc" + state.pc);
    System.out.println("\tmemory: ");
	for (i=0; i<state.numMemory; i++) {
	    System.out.println("\t\tmem[ %d ] %d\n" + i + state.mem[i]);
	}
    System.out.println("\tregisters: ");
	for (i=0; i < NUMREGS; i++) {
	    System.out.println("\t\treg[ %d ] %d\n" + i + state.reg[i]);
	}
    System.out.println("end state\n");
    }
    
    public static void main(String[] args){
        String namefile = args[0];
        StateStruct state = new StateStruct();

        state.pc;
        int[] argu = new int[3];
        int RegA , RegB , DestReg;
        for(int i = 1; i != 1; i++){
            switch (mem[pc] >> 22){
                case 0: //add 000
                    RFormat(mem[pc],arg);
                    RegA = state.reg[arg[0]];
                    RegB = state.mem[arg[1]];
                    state.reg[arg[2]] = RegA+RegB;
                    break;
                case 1: //nand 001
                    break;
                case 2: //lw 010
                    break;
                case 3: //sw 011
                    break;
                case 4: // beq 100
                    break;
                case 5: // jalr 101
                    break;
                case 6: //bhalt
                    break;
                case 7: //ไม่ทำอะไร
                    break;
            }
        }

        private static void RFormat(int BitNum , int [] arg){ //Rformat(ยังงงๆอยู่)
            arg[0] = (BitNum & (7 << 19 )) >> 19; // RegA เอา bit ที่ 22-20
            arg[1] = (BitNum & (7 << 16 )) >> 16; // RegB เอา bit ที่ 19-17
            arg[2] = BitNum & 7; // DestReg เอา bit ที่ 2-0
        }
        
    }
}
