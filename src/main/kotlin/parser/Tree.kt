package parser

data class Tree(val node: String, val isTerminal: Boolean, val children: List<Tree>)

fun terminal(node: String): Tree {
    return Tree(node, true, emptyList())
}

fun nonTerminal(node: String, vararg children: Tree): Tree {
    return Tree(node, false, children.asList())
}

val EMPTY = terminal("ε")
val OR = terminal("|")
val CLINI = terminal("*")
val LPAREN = terminal("(")
val RPAREN = terminal(")")