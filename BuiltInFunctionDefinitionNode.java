import java.util.HashMap;
import java.util.function.Function;

/*
 * The Builtin Function Definition Node class which extends a Function Definition Node.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode {

    Function<HashMap<String, InterpreterDataType>, String> execute;
    boolean variadic;

    public BuiltInFunctionDefinitionNode(Function<HashMap<String, InterpreterDataType>, String> execute, boolean variadic) {
        this.execute = execute;
        this.variadic = variadic;
    }

    String Execute(HashMap<String, InterpreterDataType> parameters) {
        return execute.apply(parameters);
    }
}
