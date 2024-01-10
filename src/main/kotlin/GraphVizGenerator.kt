import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.Label
import guru.nidi.graphviz.attribute.Shape
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Factory.node
import guru.nidi.graphviz.model.Node
import parser.EMPTY
import parser.Tree
import java.io.File

class GraphVizGenerator (private val showEmpty: Boolean, private val showEmptyNodes: Boolean) {
    private var id: Long = 0

    fun writeGraph(tree: Tree, filename: String) {
        val graph = graph().directed().with(convertTree(tree))
        Graphviz.fromGraph(graph).render(Format.PNG).toFile(File(filename))
    }

    private fun convertTree(tree: Tree): Node? {
        return when (tree.isTerminal) {
            false -> convertNonTerminal(tree)
            true -> convertTerminal(tree)
        }
    }

    private fun convertTerminal(tree: Tree): Node {
        return toNodeOp(tree.node)
    }

    private fun convertNonTerminal(tree: Tree): Node? {
        if (!showEmptyNodes && ( tree.children.isEmpty() || tree.children == listOf(EMPTY))) {
            return null
        }
        var result = toNode(tree.node)
        for (child in tree.children) {
            if (!showEmpty && child == EMPTY) {
                continue
            }
            convertTree(child)?.let { result = result.link(it) }
        }
        return result
    }

    private fun toNode(value: String): Node {
        return node(nextId()).with(Label.of(value))
    }

    private fun toNodeOp(value: String): Node {
        return node(nextId()).with(Label.of(value), Shape.BOX, Color.GREEN)
    }

    private fun nextId(): String {
        id += 1
        return id.toString()
    }
}
