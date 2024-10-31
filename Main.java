import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * The Main class which implements the main method
 * in order to run this Interpreter.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 *
 * Run with these command in terminal:
 * cd out\production\cscode
 *
 * java csi311.interpreter4.Main csi311\interpreter4\test1.awk
 * java csi311.interpreter4.Main csi311\interpreter4\test2.awk
 * java csi311.interpreter4.Main csi311\interpreter4\test3.awk
 * java csi311.interpreter4.Main csi311\interpreter4\test4.awk
 * java csi311.interpreter4.Main csi311\interpreter4\test5.awk
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            Path myPath = Paths.get(args[0]);
            String content = new String(Files.readAllBytes(myPath.toAbsolutePath()));
            Lexer lexer = new Lexer(content);
            Parser parser = new Parser(lexer.Lex());
            Parser.ProgramNode pNode = parser.Parse();
            Interpreter interpreter = new Interpreter(pNode, myPath.toAbsolutePath());
            interpreter.InterpretProgram(pNode);
        }
    }
}
