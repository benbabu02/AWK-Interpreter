import java.util.HashMap;

/*
 * The Interpreter Array Data Type class which extends Interpreter Data Type.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class InterpreterArrayDataType extends InterpreterDataType {
    HashMap<String, InterpreterDataType> iadt;

    public InterpreterArrayDataType() {
        this.iadt = new HashMap<>();
    }

    HashMap<String, InterpreterDataType> getIADT() {
        return iadt;
    }

    @Override
    public String toString() {
        return "InterpreterArrayDataType: " + iadt.toString();
    }
}
