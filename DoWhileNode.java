/*
 * The Do While Node class which extends a Statement Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class DoWhileNode extends StatementNode {
    BlockNode blockNode;
    Node condition;

    public DoWhileNode(BlockNode blockNode, Node condition) {
        this.blockNode = blockNode;
        this.condition = condition;
    }

    Node getCondition() {
        return condition;
    }

    BlockNode getBlockNode() { return blockNode; }

    @Override
    public String toString() {
        return "do {" + blockNode + "} while (" + condition +  "}";
    }
}
