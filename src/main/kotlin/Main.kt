import com.xenomachina.argparser.ArgParser
import parser.LexicalAnalyzer
import parser.Parser

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::MyArgs).run {
        val tree = Parser(LexicalAnalyzer(expression)).parse()
        val graphVizGenerator = GraphVizGenerator(showEmpty = withEmptyTerminals, showEmptyNodes = withEmptyNodes)
        graphVizGenerator.writeGraph(tree, filename)
    }
}

class MyArgs(parser: ArgParser) {
    val expression by parser.positional("Launch: program <expression> <fileToResult> [flags]")
    val filename by parser.positional("Launch: program <expression> <fileToResult> [flags]")
    val withEmptyTerminals by parser.flagging("--et", "--withEmptyTerminals", help = "show empty terminals")
    val withEmptyNodes by parser.flagging("--en", "--withEmptyNodes", help = "show empty nodes")
}