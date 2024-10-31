public class WhileNode extends StatementNode {

    /*
     * The While Node class which extends a Statement Node.
     *
     * @author Benjamin Babu (bbabu@albany.edu)
     */
    Node condition;
    BlockNode blockNode;
    public WhileNode(Node condition, BlockNode blockNode) {
        this.condition = condition;
        this.blockNode = blockNode;
    }

    Node getCondition() {
        return condition;
    }

    BlockNode getBlockNode() {
        return blockNode;
    }

    @Override
    public String toString() {
        return "while (" + condition + ") {" + blockNode + "}";
    }
}
