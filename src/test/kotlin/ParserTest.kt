import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import parser.LexicalAnalyzer
import parser.ParseException
import parser.Parser
import parser.Token
import java.io.InputStream
import kotlin.test.assertEquals

class ParserTest {

    @Test
    fun testVar() {
        successParse("a")
    }

    @Test
    fun testVarWithBrackets() {
        successParse("((a))")
    }

    @Test
    fun testConcat() {
        successParse("ab")
    }

    @Test
    fun testConcat2() {
        successParse("abc")
    }

    @Test
    fun testOr() {
        successParse("a|c")
    }

    @Test
    fun testStar() {
        successParse("a*")
    }

    @Test
    fun testOrWithConcat() {
        successParse("ab|c")
    }

    @Test
    fun testOrWithConcat2() {
        successParse("a(b|c)")
    }

    @Test
    fun testOrWithConcat3() {
        successParse("(ab)|c")
    }

    @Test
    fun testOrWithConcat4() {
        successParse("ab|cd")
    }

    @Test
    fun testOrWithStar() {
        successParse("a*|b")
    }

    @Test
    fun testOrWithStar2() {
        successParse("a|b*")
    }

    @Test
    fun testOrWithStar3() {
        successParse("(a|b)*")
    }

    @Test
    fun testConcatWithStar() {
        successParse("ab*")
    }

    @Test
    fun testConcatWithStar2() {
        successParse("(ab)*")
    }

    @Test
    fun testConcatWithStar3() {
        successParse("ab*c")
    }

    @Test
    fun testConcatWithRepeats() {
        successParse("ab{1}")
    }

    @Test
    fun allTest() {
        successParse("ab|c*d")
    }

    @Test
    fun allTest2() {
        successParse("(ab)*")
    }

    @Test
    fun allTest3() {
        successParse("((abc*b|a)*ab(aa|b*)b)*")
    }

    @Test
    fun invalidManyStar() {
        assertError("a**")
    }

    @Test
    fun invalidPrefixStar() {
        assertError("*a")
    }

    @Test
    fun invalidManyOR() {
        assertError("a||b")
    }

    @Test
    fun invalidOrWithRepeats() {
        assertError("a|{1}")
    }

    @Test
    fun invalidCliniWithRepeats() {
        assertError("a*{1}")
        assertError("a{1}*")
    }

    private fun successParse(expression: String) {
        val lex = LexicalAnalyzer(getIS(expression))
        val parser = Parser(lex)
        parser.parse()
        assertEquals(Token.END, lex.curToken())
    }

    private fun assertError(expression: String) {
        val lex = LexicalAnalyzer(getIS(expression))
        val parser = Parser(lex)
        assertThrows<ParseException> { parser.parse() }
    }

    private fun getIS(source: String): InputStream {
        return source.byteInputStream()
    }
}