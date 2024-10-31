import java.util.LinkedList;
import java.util.Optional;

/*
 * The BlockNode class which implements a BlockNode.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class BlockNode extends Node {
    LinkedList<StatementNode> statementNodes;
    Optional<Node> condition;

    public BlockNode() {
        this.statementNodes = new LinkedList<>();
    }

    LinkedList<StatementNode> getStatement() {
        return statementNodes;
    }

    Optional<Node> getCondition() { return condition; }

    void addStatement(StatementNode statement) { this.statementNodes.add(statement); }

    void setCondition(Optional<Node> condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return getStatement().toString();
    }
}
