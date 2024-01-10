package parser

class Parser(private val lex: LexicalAnalyzer) {
    private fun assertToken(token: Token) {
        if (lex.curToken() != token) {
            throw ParseException("Expected $token token, but was ${lex.curToken()}")
        }
    }

    private fun consume(token: Token) {
        assertToken(token)
        lex.nextToken()
    }

    fun parse(): Tree {
        lex.nextToken()
        val tree = parseOr()
        assertToken(Token.END)
        return tree
    }

    private fun parseOr(): Tree {
        val concat = parseConcat()
        val orContinue = parseOrContinue()
        return nonTerminal("or", concat, orContinue)
    }

    private fun parseOrContinue(): Tree {
        return when (lex.curToken()) {
            Token.OR -> {
                consume(Token.OR)
                val concat = parseConcat()
                val or = parseOrContinue()
                nonTerminal("orContinue", OR, concat, or)
            }

            else -> nonTerminal("orContinue", EMPTY)
        }
    }

    private fun parseConcat(): Tree {
        val clini = parseClini()
        val concatContinue = parseConcatContinue()
        return nonTerminal("concat", clini, concatContinue)
    }

    private fun parseConcatContinue(): Tree {
        return when (lex.curToken()) {
            Token.VAR, Token.LPAREN, Token.STAR -> {
                val clini = parseClini()
                val concatContinue = parseConcatContinue()
                nonTerminal("concatContinue", clini, concatContinue)
            }

            else -> {
                nonTerminal("concatContinue", EMPTY)
            }
        }
    }

    private fun parseClini(): Tree {
        val base = parseBase()
        val cliniContinue = parseCliniContinue()
        return nonTerminal("clini", base, cliniContinue)
    }

    private fun parseCliniContinue(): Tree {
        return when (lex.curToken()) {
            Token.STAR -> {
                consume(Token.STAR)
                nonTerminal("cliniContinue", CLINI)
            }

            else -> nonTerminal("cliniContinue", EMPTY)
        }
    }


    private fun parseBase(): Tree {
        return when (lex.curToken()) {
            Token.LPAREN -> {
                consume(Token.LPAREN)
                val or = parseOr()
                consume(Token.RPAREN)
                nonTerminal("base", LPAREN, or, RPAREN)
            }

            Token.VAR -> {
                val variable = terminal(Char(lex.curChar()).toString())
                consume(Token.VAR)
                nonTerminal("base", variable)
            }

            else -> throw ParseException("Expected VAR/LPAREN, got ${lex.curToken()}")
        }
    }


//
//    private fun parseExpression(): TreeNode {
//        val term = parseTerm()
//
//        return when (lex.curToken()) {
//            Token.OR -> {
//                consume(Token.OR)
//                val expression = parseExpression()
//                return OrNode(term, expression)
//            }
//            else -> return term
//        }
//    }
//
//    private fun parseTerm(): TreeNode {
//        val factor = parseFactor()
//
//        return when (lex.curToken()) {
//            Token.VAR -> {
//                val term = parseTerm()
//                return ConcatNode(factor, term)
//            }
//            Token.LPAREN -> {
//                consume(Token.LPAREN)
//                val term = parseExpression()
//                consume(Token.RPAREN)
//                return ConcatNode(factor, term)
//            }
//            else -> return factor
//        }
//    }
//
//    private fun parseFactor(): TreeNode {
//        val base = parseBase()
//        val factorCnt = parseFactorCnt()
//        var result = base
//
//        return when (factorCnt) {
//            0 -> base
//            else -> StarNode(result)
//        }
//    }
//
//    private fun parseFactorCnt(): Int {
//        return when (lex.curToken()) {
//            Token.STAR -> {
//                consume(Token.STAR)
//                1
//            }
//            else -> 0
//        }
//    }
//
//    private fun parseBase(): TreeNode {
//        return when (lex.curToken()) {
//            Token.LPAREN -> {
//                consume(Token.LPAREN)
//                val exception = parseExpression()
//                consume(Token.RPAREN)
//                exception
//            }
//            else -> {
//                val node = LiteralNode(Char(lex.curChar()))
//                consume(Token.VAR)
//                node
//            }
//        }
//    }
}