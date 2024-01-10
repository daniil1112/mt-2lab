import org.junit.jupiter.api.Test
import parser.LexicalAnalyzer
import parser.Token
import java.io.InputStream
import kotlin.test.assertEquals

class LexicalAnalyzerTest {
    @Test
    fun testEmpty() {
        check("")
    }

    @Test
    fun testVar() {
        check("abc", Token.VAR, Token.VAR, Token.VAR)
    }

    @Test
    fun testVarParsing() {
        val lex = LexicalAnalyzer(getIS("abc"))
        assertEquals(Token.START, lex.curToken())
        for (ch in "abc") {
            lex.nextToken()
            assertEquals(Token.VAR, lex.curToken())
            assertEquals(lex.curChar(), ch.code)
        }
        lex.nextToken()
        assertEquals(Token.END, lex.curToken())
    }

    @Test
    fun testOrParsing() {
        check("|", Token.OR)
    }

    @Test
    fun testOrParsing2() {
        check("a|", Token.VAR, Token.OR)
    }

    @Test
    fun testOrParsing3() {
        check("a|b", Token.VAR, Token.OR, Token.VAR)
    }

    @Test
    fun testStarParsing() {
        check("*", Token.STAR)
    }

    @Test
    fun testStarParsing2() {
        check("a*", Token.VAR, Token.STAR)
    }

    @Test
    fun testStarParsing3() {
        check("a*b", Token.VAR, Token.STAR, Token.VAR)
    }

    @Test
    fun testBrackets1() {
        check("()", Token.LPAREN, Token.RPAREN)
    }

    @Test
    fun testBrackets2() {
        check("(())()", Token.LPAREN, Token.LPAREN, Token.RPAREN, Token.RPAREN, Token.LPAREN, Token.RPAREN)
    }

    @Test
    fun invalidBrackets1() {
        check("(", Token.LPAREN)
    }

    @Test
    fun invalidBrackets2() {
        check(")", Token.RPAREN)
    }

    @Test
    fun invalidBrackets3() {
        check("(()", Token.LPAREN, Token.LPAREN, Token.RPAREN)
    }

    private fun check(expression: String, vararg tokens: Token) {
        val lex = LexicalAnalyzer(getIS(expression))
        assertEquals(Token.START, lex.curToken())
        for (token in tokens) {
            lex.nextToken()
            assertEquals(token, lex.curToken())
        }
        lex.nextToken()
        assertEquals(Token.END, lex.curToken())
    }

    private fun getIS(source: String): InputStream {
        return source.byteInputStream()
    }
}