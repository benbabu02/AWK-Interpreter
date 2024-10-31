/*
 * The If Node class which extends a Statement Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class IfNode extends StatementNode {
    Node condition;
    BlockNode blockNode;
    StatementNode next;

    public IfNode(Node condition, BlockNode blockNode, StatementNode next) {
        this.condition = condition;
        this.blockNode = blockNode;
        this.next = next;
    }

    Node getCondition() {
        return condition;
    }

    BlockNode getBlockNode() {
        return blockNode;
    }

    StatementNode getNext() {
        return next;
    }

    @Override
    public String toString() {
//        String returnStr = "if (" + condition + ") {" + blockNode + "}";
//        Optional<StatementNode> curr = next;
//        while (curr.isPresent()) {
//            if (curr.next == null) {
//                returnStr += "else {" + curr.blockNode + "}";
//            } else {
//                returnStr += "else if (" + curr.condition + ") {" + curr.blockNode + "}";
//            }
//            curr = curr.next;
//        }
//        return returnStr;

//        String returnStr = "if (";  //+ condition + ") {" + blockNode + "} " + next;
//        if (condition != null) {
//            returnStr += condition + ") {" + blockNode + "}";
//            if (next != null) {
//                returnStr += next;
//                return returnStr;
//            }
//        } else if (next != null) {
//            returnStr += next;
//            return returnStr;
//        } else {
//            return returnStr + blockNode;
//        }
//        return returnStr;

        return "if (" + condition + ") {" + blockNode + "} -> " + next;
    }
}
