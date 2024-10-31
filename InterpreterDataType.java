/*
 * The Interpreter Data Type class.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class InterpreterDataType {
    String str;

    public InterpreterDataType() {
        this.str = "";
    }

    public InterpreterDataType(String inputStr) {
        this.str = inputStr;
    }

    String getStr() {
        return str;
    }

    @Override
    public String toString() {
        return "InterpreterDataType: " + str;
    }
}
