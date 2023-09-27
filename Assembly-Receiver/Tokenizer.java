
import java.util.StringTokenizer;

public class Tokenizer {
    StringTokenizer con;
    StringTokenizer reset;

    Tokenizer(String assembly){
        con = new StringTokenizer(assembly," 	");
        reset = new StringTokenizer(assembly," 	");
    }

    public boolean hasNext(){
        return con.hasMoreTokens();
    }

    public String next(){
        return con.nextToken();
    }

    /**
     * resets the iterator to the first token
     */
    public void resetIterator(){
       con = reset;
       
    }

}
