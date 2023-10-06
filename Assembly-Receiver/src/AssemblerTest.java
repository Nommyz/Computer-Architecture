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
    @Test
    public void testToDecimal(){
        Assert.assertEquals(0, Assembler.toDecimal("0"));
        Assert.assertEquals(1, Assembler.toDecimal("01"));
        Assert.assertEquals(2, Assembler.toDecimal("010"));
        Assert.assertEquals(7, Assembler.toDecimal("0111"));
        Assert.assertEquals(7, Assembler.toDecimal("0000111"));
        Assert.assertEquals(64+16+4+1, Assembler.toDecimal("01010101"));

        Assert.assertEquals(-1, Assembler.toDecimal("11"));
        Assert.assertEquals(-2, Assembler.toDecimal("110"));
        Assert.assertEquals(-16, Assembler.toDecimal("110000"));
        Assert.assertEquals(-11, Assembler.toDecimal("110101"));
    }

    @Test
    public void testTwoCompliment(){
        Assert.assertEquals("1111111111111111", Assembler.twosCompliment("0000000000000001"));//+1 -> -1
        Assert.assertEquals("1111111111111110", Assembler.twosCompliment("0000000000000010"));//+2 -> -2
        Assert.assertEquals("1111111111111101", Assembler.twosCompliment("0000000000000011"));//+3 -> -3
        Assert.assertEquals("1111111111111100", Assembler.twosCompliment("0000000000000100"));//+4 -> -4
        Assert.assertEquals("1111111111111011", Assembler.twosCompliment("0000000000000101"));//+5 -> -5
        Assert.assertEquals("1111111111111010", Assembler.twosCompliment("0000000000000110"));//+6 -> -6
        Assert.assertEquals("1111111111111001", Assembler.twosCompliment("0000000000000111"));//+7 -> -7
        Assert.assertEquals("1111111111111000", Assembler.twosCompliment("0000000000001000"));//+8 -> -8
        Assert.assertEquals("1111111111110111", Assembler.twosCompliment("0000000000001001"));//+9 -> -9
        Assert.assertEquals("1111111111110110", Assembler.twosCompliment("0000000000001010"));//+10 -> -10
        Assert.assertEquals("0000000000000010", Assembler.twosCompliment("1111111111111110"));//-2 -> +2

        //8-bit case
        Assert.assertEquals("11011110", Assembler.twosCompliment("00100010")); //+34 -> -34
        // n-bit case
        Assert.assertEquals("01001", Assembler.twosCompliment("10111"));  //-9 -> 9
    }
    @Test
    public void testIsInteger(){
        Assert.assertEquals(true, Assembler.isInteger("1"));
        Assert.assertEquals(false, Assembler.isInteger("1.1"));
        Assert.assertEquals(true, Assembler.isInteger("-1"));
        Assert.assertEquals(false, Assembler.isInteger("a"));
        Assert.assertEquals(false, Assembler.isInteger("1a"));
        Assert.assertEquals(false, Assembler.isInteger("John"));
        Assert.assertEquals(false, Assembler.isInteger("Jason1"));
    }

    @Test
    public void testFillBit(){
        Assert.assertEquals("0000000000000001", Assembler.fillBits("0","1",16));
        Assert.assertEquals("00000001", Assembler.fillBits("0","1",8));
        Assert.assertEquals("00000000000000000000000111101001", Assembler.fillBits("0","111101001",32));
        Assert.assertEquals("11111111111000", Assembler.fillBits("1","000",14));
        Assert.assertEquals("1111111111111111111111111111111111111111111111111111111111111110", Assembler.fillBits("1","0",64));
        Assert.assertEquals("44441", Assembler.fillBits("4","1",5));
    }
}