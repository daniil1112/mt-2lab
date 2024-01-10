package parser

import java.io.IOException
import java.io.InputStream
import java.text.ParseException

// Порядок операций
// max - *
// middle - concotenate
// low - |

enum class Token {
    VAR, OR, STAR, LPAREN, RPAREN, START, END
}

class LexicalAnalyzer(private val stream: InputStream) {

    constructor(expression: String) : this(expression.byteInputStream())
    private var curChar: Int = -2
    private var curPos: Int = 0
    private var curToken: Token = Token.START

    private fun isBlank(char: Int): Boolean {
        if (char < 0) {
            return false
        }
        return Char(char).isWhitespace()
    }

    private fun isVar(char: Int): Boolean {
        return (char >= 'a'.code && char <= 'z'.code) || (char >= 'A'.code && char <= 'Z'.code)
    }

    private fun nextChar() {
        curPos++;
        try {
            curChar = stream.read();
        } catch (e: IOException) {
            throw ParseException(e.message, curPos);
        }
    }

    private fun updateAndGetCurrentToken(token: Token): Token {
        curToken = token
        return curToken
    }

    fun nextToken(): Token {
        nextChar()
        while (isBlank(curChar)) {
            nextChar()
        }

        if (curChar == -1) {
            return updateAndGetCurrentToken(Token.END)
        }

        if (isVar(curChar)) {
            return updateAndGetCurrentToken(Token.VAR)
        }

        if ('('.code == curChar) {
            return updateAndGetCurrentToken(Token.LPAREN)
        }
        if (')'.code == curChar) {
            return updateAndGetCurrentToken(Token.RPAREN)
        }
        if ('|'.code == curChar) {
            return updateAndGetCurrentToken(Token.OR)
        }
        if ('*'.code == curChar) {
            return updateAndGetCurrentToken(Token.STAR)
        }

        throw ParseException("Illegal character ${Char(curChar)}", curPos);
    }

    fun curToken(): Token {
        return curToken
    }

    fun curChar(): Int {
        return curChar
    }
}