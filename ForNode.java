/*
 * The For Node class which extends a Statement Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class ForNode extends StatementNode {
    Node condition1;
    Node condition2;
    Node condition3;
    BlockNode blockNode;

    public ForNode(Node condition1, Node condition2, Node condition3, BlockNode blockNode) {
        this.condition1 = condition1;
        this.condition2 = condition2;
        this.condition3 = condition3;
        this.blockNode = blockNode;
    }

    Node getCondition1() {
        return condition1;
    }

    Node getCondition2() {
        return condition2;
    }

    Node getCondition3() {
        return condition3;
    }

    BlockNode getBlockNode() {
        return blockNode;
    }

    @Override
    public String toString() {
        return "for(" + condition1 + "; " + condition2 + "; " + condition3 + ") {" + blockNode + "}";
    }
}
