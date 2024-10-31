/*
 * The For each Node class which extends a Statement Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class ForEachNode extends StatementNode {
    Node condition1;
    BlockNode blockNode;

    public ForEachNode(Node condition1, BlockNode blockNode) {
        this.condition1 = condition1;
        this.blockNode = blockNode;
    }

    Node getCondition1() {
        return condition1;
    }

    BlockNode getBlockNode() {
        return blockNode;
    }

    @Override
    public String toString() {
        return "for (" + condition1 + ") {" + blockNode + "}";
    }
}
