import org.testng.Assert;
import org.testng.annotations.Test;

public class AssemblerTest {

    @Test
    public void testToBinaryString(){
        Assert.assertEquals("0", Assembler.toBinaryString(0));
        Assert.assertEquals("1", Assembler.toBinaryString(1));
        Assert.assertEquals("10", Assembler.toBinaryString(2));
        Assert.assertEquals("110", Assembler.toBinaryString(6));
        Assert.assertEquals("111", Assembler.toBinaryString(7));
        Assert.assertEquals("1010", Assembler.toBinaryString(10));
        Assert.assertEquals("1111111", Assembler.toBinaryString(127));
        Assert.assertEquals("10000000", Assembler.toBinaryString(128));
    }
}