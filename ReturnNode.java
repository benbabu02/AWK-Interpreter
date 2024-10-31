/*
 * The Return Node class which extends a Statement Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class ReturnNode extends StatementNode {
    Node statement;
    public ReturnNode(){}
    public ReturnNode(Node statement) {
        this.statement = statement;
    }

    Node getStatement() {
        return statement;
    }

    @Override
    public String toString() {
        return "return " + statement;
    }
}
