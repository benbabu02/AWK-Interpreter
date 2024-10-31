import java.util.LinkedList;

/*
 * The Function Definition Node class which implements a Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class FunctionDefinitionNode extends Node {
    private String functionName;
    private LinkedList<String> parameters;
    private LinkedList<StatementNode> statementNodes;

    public FunctionDefinitionNode() {}

    public FunctionDefinitionNode(String FunctionName, LinkedList<String> parameters) {
        this.functionName = FunctionName;
        this.parameters = parameters;
        this.statementNodes = new LinkedList<>();
    }

    String getFunctionName() {
        return functionName;
    }

    LinkedList<String> getParameters() {
        return parameters;
    }

    LinkedList<StatementNode> getStatementNodes() {
        return statementNodes;
    }

    void statementNodesMutator(LinkedList<StatementNode> inputStatementNodes) {
        this.statementNodes.addAll(inputStatementNodes);
    }

    @Override
    public String toString() {
//        return "name: " + functionName + ", parameters: " + parameters.toString() + ", statementNodes: " + statementNodes.toString();
        String returnStr = "FunctionDefinitionNode: " + functionName;
        if (parameters != null) {
            returnStr += " parameters: " + parameters.toString();
        }
        if (statementNodes != null) {
            returnStr += " statementNodes: " + statementNodes.toString();
        }
        return returnStr;

    }
}
