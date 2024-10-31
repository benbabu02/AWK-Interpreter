import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/*
 * A Interpreter class which implements a Interpreter method
 * which takes a file as an input along with a ProgramNode. It then
 * goes interprets the file and fills each element of the program node
 * (as in the begin blocks, end blocks, other blocks, and function blocks).
 * Then it "executes" each block depending on what type of block it is
 * in order to achieve a final output.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class Interpreter {

    HashMap<String, InterpreterDataType> globalVariables = new HashMap<>();
    HashMap<String, FunctionDefinitionNode> functionSource = new HashMap<>();
    LineManager lineManager;
    HashMap<String, InterpreterDataType> parameters = new HashMap<>();

    public class LineManager {
        List<String> strList;
        private int NF = 0;
        private int NR = 0;
        private int FNR = 0;

        public LineManager(List<String> inputStrList) {
            this.strList = inputStrList;
        }

        boolean SplitAndAssign() {
            if (strList.isEmpty()) { return false; }    // there are no more lines from the file
            String str = strList.remove(0);
            FNR = 0;
            globalVariables.put("$0", new InterpreterDataType(str));  // adds the whole line to $0
            NF++;
            String[] strSplit = str.split(globalVariables.get("FS").getStr());
            for (int i = 1; i <= strSplit.length; i++) {    // adds each word in the line to $i
                globalVariables.put("$"+NF, new InterpreterDataType(strSplit[i-1]));
                NF++;
            }
            FNR++;
            NR++;
            return true;
        }

        @Override
        public String toString() {
            String returnStr = "";
            for (String s : strList) {
                returnStr += s;
            }
            return returnStr;
        }
    }

    public Interpreter(Parser.ProgramNode pNode, Path filePath) throws IOException {
        if (filePath == null) { // file path provided is null
            lineManager = new LineManager(new LinkedList<>());
        }
        else if (Files.exists(filePath)) { // file path exists
            List<String> fileLines = Files.readAllLines(filePath);
            lineManager = new LineManager(fileLines);
            globalVariables.put("FILENAME", new InterpreterDataType(filePath.toString()));
        } else { // file path does not exist
            lineManager = new LineManager(new LinkedList<>());
        }
        globalVariables.put("FS", new InterpreterDataType(" ")); // field seperator is set as a space
        globalVariables.put("OFMT", new InterpreterDataType("%.6g"));
        globalVariables.put("OFS", new InterpreterDataType(" "));
        globalVariables.put("ORS", new InterpreterDataType("\n"));

        // handles the print builtin function
        Function<HashMap<String, InterpreterDataType>, String> PrintFunction = ((parameters) -> {
            String returnStr = "";
            InterpreterArrayDataType iadt = (InterpreterArrayDataType) parameters.get("0"); // since print is a variadic the parameter is an iadt
            for (int i = 0; i < iadt.iadt.size(); i++) { // looping through iadt to get the parameters
                returnStr += iadt.iadt.get(String.valueOf(i)).getStr() + globalVariables.get("FS").getStr();
            }
            System.out.println(returnStr);
            return returnStr;
        });
        BuiltInFunctionDefinitionNode printFunction = new BuiltInFunctionDefinitionNode(PrintFunction, true);
        functionSource.put("print", printFunction);
        pNode.addFunctionNode(printFunction);

        // handles the printf builtin function
        Function<HashMap<String, InterpreterDataType>, String> PrintfFunction = ((parameters) -> {
            InterpreterArrayDataType iadt = (InterpreterArrayDataType) parameters.get("0");  // since printf is a variadic the parameter is an iadt
            String[] argStr = new String[iadt.iadt.size()];
            for (int i = 1; i < iadt.iadt.size(); i++) { // populates the list of strings
                argStr[i-1] = iadt.iadt.get(String.valueOf(i)).getStr() + globalVariables.get("FS").getStr();
            }
            String returnStr = String.format(iadt.iadt.get("0").getStr(), argStr); // builds the return string
            System.out.println(returnStr);
            return returnStr;
        });
        BuiltInFunctionDefinitionNode printfFunction = new BuiltInFunctionDefinitionNode(PrintfFunction, true);
        functionSource.put("printf", printfFunction);
        pNode.addFunctionNode(printfFunction);

        // handles the getline builtin function
        Function<HashMap<String, InterpreterDataType>, String> GetlineFunction = ((parameters) -> {
            boolean check = lineManager.SplitAndAssign(); // gets the return of SplitAndAssign
            if (!check) { // was not able to split and assign
                return "1";
            }
            return "0"; // was able to split and assign
        });
        BuiltInFunctionDefinitionNode getlineFunction = new BuiltInFunctionDefinitionNode(GetlineFunction, false);
        functionSource.put("getline", getlineFunction);
        pNode.addFunctionNode(getlineFunction);

        // handles the next builtin function
        Function<HashMap<String, InterpreterDataType>, String> NextFunction = ((parameters) -> {
            boolean check = lineManager.SplitAndAssign(); // gets the return of SplitAndAssign
            if (!check) { return "1"; } // was not able to split and assign
            return "0"; // was able to split and assign
        });
        BuiltInFunctionDefinitionNode nextFunction = new BuiltInFunctionDefinitionNode(NextFunction, false);
        functionSource.put("next", nextFunction);
        pNode.addFunctionNode(nextFunction);

        // handles the gsub builtin function
        Function<HashMap<String, InterpreterDataType>, String> GsubFunction = ((parameters) -> {
            String regex = parameters.get("0").getStr(); // gets the string to look for
            String replacement = parameters.get("1").getStr(); // gets the string to replace it with
            String targetArr;
            if (!parameters.isEmpty()) {
                targetArr = parameters.get("2").getStr(); // gets the target string from parameters
            } else {
                targetArr = globalVariables.get("$0").getStr(); // gets the target string from global variables
            }
            targetArr = targetArr.replaceAll(regex, replacement);
            return targetArr;
        });
        BuiltInFunctionDefinitionNode gsubFunction = new BuiltInFunctionDefinitionNode(GsubFunction, false);
        functionSource.put("gsub", gsubFunction);
        pNode.addFunctionNode(gsubFunction);

        // handles the match builtin function
        Function<HashMap<String, InterpreterDataType>, String> MatchFunction = ((parameters) -> {
            String target = parameters.get("0").getStr(); // gets the target string
            String regex = parameters.get("1").getStr(); // gets the string to match it with
            return String.valueOf(target.contains(regex));
        });
        BuiltInFunctionDefinitionNode matchFunction = new BuiltInFunctionDefinitionNode(MatchFunction, false);
        functionSource.put("match", matchFunction);
        pNode.addFunctionNode(matchFunction);

        // handles the sub builtin function
        Function<HashMap<String, InterpreterDataType>, String> SubFunction = ((parameters) -> {
            String regex = parameters.get("0").getStr(); // gets the string to look for
            String replacement = parameters.get("1").getStr(); // gets the string to replace it with
            String target = parameters.get("2").getStr(); // gets the target string
            return target.replaceFirst(regex, replacement);
        });
        BuiltInFunctionDefinitionNode subFunction = new BuiltInFunctionDefinitionNode(SubFunction, false);
        functionSource.put("sub", subFunction);
        pNode.addFunctionNode(subFunction);

        // handles the index builtin function
        Function<HashMap<String, InterpreterDataType>, String> IndexFunction = ((parameters) -> {
            String target = parameters.get("0").getStr(); // gets the target string
            String regex = parameters.get("1").getStr(); // gets the string to look for
            return String.valueOf(target.indexOf(regex));
        });
        BuiltInFunctionDefinitionNode indexFunction = new BuiltInFunctionDefinitionNode(IndexFunction, false);
        functionSource.put("index", indexFunction);
        pNode.addFunctionNode(indexFunction);

        // handles the length builtin function
        Function<HashMap<String, InterpreterDataType>, String> LengthFunction = ((parameters) -> {
            String target;
            if (parameters.isEmpty()) {
                target = globalVariables.get("$0").getStr(); // gets the target string from global variables
            } else {
                target = parameters.get("0").getStr(); // gets the target string from parameters
            }
            return String.valueOf(target.length());
        });
        BuiltInFunctionDefinitionNode lengthFunction = new BuiltInFunctionDefinitionNode(LengthFunction, false);
        functionSource.put("length", lengthFunction);
        pNode.addFunctionNode(lengthFunction);

        // handles the split builtin function
        Function<HashMap<String, InterpreterDataType>, String> SplitFunction = ((parameters) -> {
            String target = parameters.get("0").getStr(); // gets the target string
            InterpreterArrayDataType arr = (InterpreterArrayDataType) parameters.get("1"); // gets the array to store it to
            String fs = globalVariables.get("FS").getStr(); // gets the field separator to separate by
            InterpreterArrayDataType sep = null;
            if (parameters.size() > 2) {
                fs = parameters.get("2").getStr();
                sep = (InterpreterArrayDataType) parameters.get("3");
            }
            String[] splitArr = target.split(fs);
            for (int i = 0; i < splitArr.length; i++) {
                arr.iadt.put(String.valueOf(i), new InterpreterDataType(splitArr[i]));
            }
            if (sep != null) {
                for (int j = 0; j < splitArr.length-1; j++) {
                    sep.iadt.put(String.valueOf(j), new InterpreterDataType(fs));
                }
            }
            return String.valueOf(splitArr.length);
        });
        BuiltInFunctionDefinitionNode splitFunction = new BuiltInFunctionDefinitionNode(SplitFunction, false);
        functionSource.put("split", splitFunction);
        pNode.addFunctionNode(splitFunction);

        // handles the substr builtin function
        Function<HashMap<String, InterpreterDataType>, String> SubstrFunction = ((parameters) -> {
            String target = parameters.get("0").getStr(); // gets the target string
            int startIndex = Integer.parseInt(parameters.get("1").getStr()); // gets the start index
            int endIndex = target.length()+1; // gets the default end index to the length of the string
            if (parameters.size() > 2) { // if a second parameters was given it is used as the end index
                endIndex = Integer.parseInt(parameters.get("2").getStr());
            }
            return target.substring(startIndex-1, endIndex-1);
        });
        BuiltInFunctionDefinitionNode substrFunction = new BuiltInFunctionDefinitionNode(SubstrFunction, false);
        functionSource.put("substr", substrFunction);
        pNode.addFunctionNode(substrFunction);

        // handles the tolower builtin function
        Function<HashMap<String, InterpreterDataType>, String> TolowerFunction = ((parameters) -> {
            String returnStr = parameters.get("0").getStr().toLowerCase(); // gets the target string and returns a lowercase version
            return returnStr;
        });
        BuiltInFunctionDefinitionNode tolowerFunction = new BuiltInFunctionDefinitionNode(TolowerFunction, false);
        functionSource.put("tolower", tolowerFunction);
        pNode.addFunctionNode(tolowerFunction);

        // handles the toupper builtin function
        Function<HashMap<String, InterpreterDataType>, String> ToupperFunction = ((parameters) -> {
            String returnStr = parameters.get("0").getStr().toUpperCase(); // gets the target string and returns a uppercase version
            return returnStr;
        });
        BuiltInFunctionDefinitionNode toUpperFunction = new BuiltInFunctionDefinitionNode(ToupperFunction, false);
        functionSource.put("toupper", toUpperFunction);
        pNode.addFunctionNode(toUpperFunction);
    }

    void InterpretProgram(Parser.ProgramNode programNode) throws Exception {
        // run begin blocks
        for (BlockNode beginBlock : programNode.getBeginBlockNodes()) { // loops through the begin block nodes and "executes" them
            InterpretBlock(beginBlock); 
        }
        // run other blocks
        while (lineManager.SplitAndAssign()) {
            for (BlockNode otherBlock: programNode.getOtherBlockNodes()) {  // loops through the other block nodes and "executes" them
                InterpretBlock(otherBlock);
            }
        }
        // run end blocks
        for (BlockNode endBlock : programNode.getEndBlockNodes()) { // loops through the end block nodes and "executes" them
            InterpretBlock(endBlock);
        }
    }

    void InterpretBlock(BlockNode blockNode) throws Exception {
        HashMap<String, InterpreterDataType> locals = new HashMap<>();
        if (blockNode.getCondition() == null || blockNode.getCondition().isEmpty()) { // block node does not have a condition
            for (StatementNode statement : blockNode.getStatement()) {
                ProcessStatement(locals, statement);
            }
        } else { // block node has a condition so only call process statements if that condition is true
            InterpreterDataType condition = GetIDT(blockNode.getCondition().get(), locals);
            if (condition.getStr() == "true") {
                for (StatementNode statement : blockNode.getStatement()) {
                    ProcessStatement(locals, statement);
                }
            }
        }
    }

    String RunFunctionCall(FunctionCallNode functionCallNode, HashMap<String, InterpreterDataType> localVariables) throws Exception {
        String functionName;
        // gets the name which should be used to search for the function call in the hashmap
        if (functionCallNode.getName() != null) {
            functionName = functionCallNode.getName();
        } else if (functionCallNode.functionName.getTokenType() == Token.TokenType.PRINT) {
            functionName = "print";
        } else if (functionCallNode.functionName.getTokenType() == Token.TokenType.PRINTF) {
            functionName = "printf";
        } else if (functionCallNode.functionName.getTokenType() == Token.TokenType.GETLINE) {
            functionName = "getline";
        } else if (functionCallNode.functionName.getTokenType() == Token.TokenType.NEXT) {
            functionName = "next";
        } else if (functionCallNode.functionName.getTokenType() == Token.TokenType.MATCH) {
            functionName = "match";
        } else {
            functionName = functionCallNode.getName();
        }

        // for print
        if (functionSource.containsKey(functionName)) {
            FunctionDefinitionNode func = functionSource.get(functionName);
            if (func instanceof BuiltInFunctionDefinitionNode) { // function is a builtin function
                if (((BuiltInFunctionDefinitionNode) func).variadic) { // function is a variadic so the hashmap is <String, IADT>
                    HashMap<String, InterpreterDataType> map = new HashMap<>();
                    InterpreterArrayDataType iadt = new InterpreterArrayDataType();
                    for (int j = 0; j < functionCallNode.parameters.size(); j++) { // populates the hashmap contained within the iadt object
                        InterpreterDataType ii = GetIDT(functionCallNode.parameters.get(j), localVariables);
                        iadt.iadt.put(Integer.toString(j), ii);
                    }
                    map.put("0", iadt); // sets the iadt in the map hashmap
                    return ((BuiltInFunctionDefinitionNode) func).Execute(map);
                } else { // function is not a variadic so the hashmap is just <String, IDT>
                    HashMap<String, InterpreterDataType> map = new HashMap<>();
                    for (int j = 0; j < functionCallNode.parameters.size(); j++) { // populates the map hashmap
                        map.put(Integer.toString(j), GetIDT(functionCallNode.parameters.get(j), localVariables));
                    }
                    return ((BuiltInFunctionDefinitionNode) func).Execute(map);
                }
            } else { // not a builtin function (user defined function)
                HashMap<String, InterpreterDataType> map = new HashMap<>();
                for (int j = 0; j < functionCallNode.parameters.size(); j++) { // populates the map hashmap
                    map.put(Integer.toString(j), GetIDT(functionCallNode.parameters.get(j), localVariables));
                }
                InterpretListOfStatements(func.getStatementNodes(), map);
            }
        }
        return "";
    }

    ReturnType ProcessStatement(HashMap<String, InterpreterDataType> locals, StatementNode stmt) throws Exception {
        if (stmt instanceof BreakNode) { // checks if statement node is a BreakNode
            return new ReturnType(ReturnType.ReturnEnums.Break);
        } else if (stmt instanceof ContinueNode) { // checks if statement node is a ContinueNode
            return new ReturnType(ReturnType.ReturnEnums.Continue);
        } else if (stmt instanceof DeleteNode) { // checks if statement node is a DeleteNode
            DeleteNode deleteNode = (DeleteNode) stmt;
            if (deleteNode.getCondition() instanceof VariableReferenceNode) { // checks if the condition is a  VariableReferenceNode
                VariableReferenceNode vNode = (VariableReferenceNode) deleteNode.getCondition(); // type casts the delete node to a VariableReferenceNode
                if (locals.containsKey(vNode.getName())) { // local contains the array
                    InterpreterArrayDataType arr = (InterpreterArrayDataType) locals.get(vNode.getName()); // gets the array from locals
                    if (vNode.getExpression() == null || vNode.getExpression().isEmpty()) { // indices not set so delete it all
                        locals.remove(vNode.getName()); // removes the array from locals
                    } else { // indices are set so only delete that
                        arr.getIADT().remove(GetIDT(vNode.expression.get(), locals).getStr()); // calls GetIDT on the expression and removes it from the array hashmap which is stored in locals
                    }
                } else if (globalVariables.containsKey(vNode.getName())) { // globals contains the array
                    InterpreterArrayDataType arr = (InterpreterArrayDataType) globalVariables.get(vNode.getName());
                    if (vNode.getExpression() == null || vNode.getExpression().isEmpty()) { // indices not set so delete it all
                        globalVariables.remove(vNode.getName()); // removes the array from global
                    } else { // indices are set so only delete
                        arr.getIADT().remove(GetIDT(vNode.expression.get(), locals).getStr()); // calls GetIDT on the expression and removes it from the array hashmap which is stored in locals
                    }
                }
                return new ReturnType(ReturnType.ReturnEnums.Normal, "true");
            } else {
                throw new Exception("Incorrect delete condition: Interpreter<ProcessStatement(DeleteNode)>");
            }
        } else if (stmt instanceof DoWhileNode) { // checks if statement node is a DoWhileNode
            DoWhileNode doWhileNode = (DoWhileNode) stmt;
            InterpreterDataType condition = GetIDT(doWhileNode.getCondition(), locals);
            do {
                ReturnType interStmt = InterpretListOfStatements(doWhileNode.getBlockNode().getStatement(), locals); // calls interpret list of statements on the block node for DoWhileNode
                if (interStmt.getReturnEnum() == ReturnType.ReturnEnums.Break) { // checks if the return enum is break and returns break
                    return new ReturnType(ReturnType.ReturnEnums.Break);
                } else if (interStmt.getReturnEnum() == ReturnType.ReturnEnums.Return) { // checks if the return enum is return and returns the result
                    return interStmt;
                }
                condition = GetIDT(doWhileNode.getCondition(), locals);
            } while (condition.getStr() == "true");
        } else if (stmt instanceof ForNode) { // checks if statement node is a ForNode
            ForNode forNode = (ForNode) stmt;
            if (forNode.getCondition1() != null) {
                GetIDT(forNode.getCondition1(), locals); // calls GetIDT on the first condition of the for loop
            }
            InterpreterDataType condition = GetIDT(forNode.condition2, locals);  // calls GetIDT on the second condition of the for loop
            while (condition.getStr() == "true") {
                ReturnType interStmt = InterpretListOfStatements(forNode.getBlockNode().getStatement(), locals);
                if (interStmt.getReturnEnum() == ReturnType.ReturnEnums.Break) { // checks if the return enum is break and returns break
                    break;
                } else if (interStmt.getReturnEnum() == ReturnType.ReturnEnums.Return) { // checks if the return enum is return and returns the result
                    return interStmt;
                }
                GetIDT(forNode.getCondition3(), locals); // calls GetIDT on the third condition of the for loop
                condition = GetIDT(forNode.condition2, locals); // evaluates the condition again to check if it is true
            }
        } else if (stmt instanceof ForEachNode) { // checks if statement node is a ForEachNode
            ForEachNode forEachNode = (ForEachNode) stmt;
            if (forEachNode.getCondition1() instanceof OperationNode) { // checks if condition 1 is an operation node
                OperationNode forEachOpNode = (OperationNode) forEachNode.getCondition1();
                if (forEachOpNode.getLeft() instanceof VariableReferenceNode && forEachOpNode.getRight().isPresent()) { // checks if the left side is a variable reference node
                    VariableReferenceNode opNodeLeft = (VariableReferenceNode) forEachOpNode.getLeft();
                    if (forEachOpNode.getRight().get() instanceof VariableReferenceNode) { // checks if the right side is a variable reference node
                        VariableReferenceNode opNodeRight = (VariableReferenceNode) forEachOpNode.getRight().get();
                        InterpreterArrayDataType arr = (InterpreterArrayDataType) locals.get(opNodeRight.getName()); // gets the hashmap from locals
                        for (String currKey : arr.getIADT().keySet()) {
                            locals.put(opNodeLeft.getName(), new InterpreterDataType(currKey)); // sets the variable in locals to the variable name in the hashmap
                            ReturnType interStmt = InterpretListOfStatements(forEachNode.getBlockNode().getStatement(), locals);
                            if (interStmt.getReturnEnum() == ReturnType.ReturnEnums.Break) { // checks if the return enum is break and returns break
                                return new ReturnType(ReturnType.ReturnEnums.Break);
                            } else if (interStmt.getReturnEnum() == ReturnType.ReturnEnums.Return) { // checks if the return enum is return and returns the result
                                return interStmt;
                            }
                        }
                    }
                }
            }
        } else if (stmt instanceof IfNode) { // checks if statement node is a IfNode
            IfNode ifNode = (IfNode) stmt;
            IfNode curr = ifNode;
            InterpreterDataType condition = GetIDT(curr.getCondition(), locals); // checks the condition of the if statement using GetIDT
            while (condition == null || condition.getStr() == "true") {
                ReturnType interStmt = InterpretListOfStatements(curr.getBlockNode().getStatement(), locals);
                if (interStmt.getReturnEnum() != ReturnType.ReturnEnums.Normal) { // returns only if the return enums is not normal
                    return interStmt;
                }
                curr = (IfNode) ifNode.getNext(); // moves to the next if node
                condition = GetIDT(curr.getCondition(), locals); // evaluates the condition again
            }
        } else if (stmt instanceof ReturnNode) { // checks if statement node is a ReturnNode
            ReturnNode returnNode = (ReturnNode) stmt;
            if (returnNode.getStatement() == null) { // return without an expression
                return new ReturnType(ReturnType.ReturnEnums.Return);
            } else { // return with an expression
                return new ReturnType(ReturnType.ReturnEnums.Return, GetIDT(returnNode.getStatement(), locals).getStr());
            }
        } else if (stmt instanceof WhileNode) { // checks if statement node is a WhileNode
            WhileNode whileNode = (WhileNode) stmt;
            InterpreterDataType condition = GetIDT(whileNode.getCondition(), locals); // evaluates the condition
            while (condition.getStr() == "true") {
                ReturnType interStmt = InterpretListOfStatements(whileNode.getBlockNode().getStatement(), locals);
                if (interStmt.getReturnEnum() == ReturnType.ReturnEnums.Break) { // checks if the return enum is break and returns break
                    return new ReturnType(ReturnType.ReturnEnums.Break);
                } else if (interStmt.getReturnEnum() == ReturnType.ReturnEnums.Return) { // checks if the return enum is return and returns the result
                    return interStmt;
                }
                condition = GetIDT(whileNode.getCondition(), locals); // evaluates the condition again
            }
        }
        InterpreterDataType gotIDT = GetIDT(stmt, locals);
        if (gotIDT == null) {
            throw new Exception("Node is not found: Interpreter<ProcessStatement>");
        } else {
            return new ReturnType(ReturnType.ReturnEnums.Normal, gotIDT.getStr());
        }
    }

    // yippeee :DDDDDDDDDDDDDD
    ReturnType InterpretListOfStatements(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> locals) throws Exception {
        for (StatementNode stNode : statements) { // loops through given linked list of statement nodes
            ReturnType rType = ProcessStatement(locals, stNode); // calls process statement and checks the return enum
            if (rType.getReturnEnum() != ReturnType.ReturnEnums.Normal) { return rType; } // return the result only if the return enum is normal
        }
        return new ReturnType(ReturnType.ReturnEnums.Normal);
    }

    // GetIDT method takes a node and a hashmap of local variables. It evaluates the node and returns and IDT.
    InterpreterDataType GetIDT(Node node, HashMap<String, InterpreterDataType> localVariables) throws Exception {
        if (node instanceof AssignmentNode) { // If instance of AssignmentNode it check if it is an instance of VariableReferenceNode or OperationNode, gets the required information, adds it to the local variables and returns it
            Node leftNode = ((AssignmentNode) node).target;
            if (leftNode instanceof VariableReferenceNode) { // if it is an instance of VariableReferenceNode
                InterpreterDataType rightSide = GetIDT(((AssignmentNode) node).expression, localVariables);
                localVariables.put(leftNode.toString(), rightSide);
                return rightSide;
            } else if (leftNode instanceof OperationNode) { // if it is an instance of OperationNode check for a dollar sign
                if (((OperationNode) leftNode).thisEnum == OperationNode.OperationList.DOLLAR) {
                    InterpreterDataType rightSide = GetIDT(((AssignmentNode) node).expression, localVariables);
                    localVariables.put(leftNode.toString(), rightSide);
                    return rightSide;
                }
            }
        } else if (node instanceof ConstantNode) { // If instance of ConstantNode it returns a new InterpreterDataType with the string value
            return new InterpreterDataType(((ConstantNode) node).getStr());
        } else if (node instanceof FunctionCallNode) { // If instance of FunctionCallNode it returns an empty string for now
            return new InterpreterDataType(RunFunctionCall((FunctionCallNode) node, localVariables));
        } else if (node instanceof PatternNode) { // If instance of PatternNode it returns an exception
            throw new Exception("Passing Pattern to Function: Interpreter<GetIDT(PatternNode)>");
        } else if (node instanceof TernaryNode tNode) { // If instance of TernaryNode, first check if the condition is true or false, depending on that run the true or false case
            InterpreterDataType booleanCondition = GetIDT(tNode.checkCase, localVariables); // condition checking for TernaryNode
            if (booleanCondition.getStr().equals("false")) { // false case for TernaryNode
                if (tNode.falseCase.isEmpty()) {
                    throw new Exception("falseCase is Empty: Interpreter<GetIDT(TernaryNode)>");
                }
                return GetIDT(tNode.falseCase.get(), localVariables);
            } else { // true case for TernaryNode
                if (tNode.trueCase.isEmpty()) {
                    throw new Exception("trueCase is Empty: Interpreter<GetIDT(TernaryNode)>");
                }
                return GetIDT(tNode.trueCase.get(), localVariables);
            }
        } else if (node instanceof VariableReferenceNode) { // If instance of VariableReferenceNode, check if expression is empty. If it is then we look through the local and global variables hashmaps. If it is not, it is an array reference.
            VariableReferenceNode vNode = (VariableReferenceNode) node;
            if (vNode.expression == null || vNode.expression.isEmpty()) { // expression is empty/null
                if (localVariables.containsKey(vNode.getName())) {
                    return localVariables.get(vNode.getName());
                } else if (globalVariables.containsKey(vNode.getName())) {
                    return globalVariables.get(vNode.getName());
                }
            } else { // expression exists so it is an array reference. We use GetIDT to get the index and then look for it in local variables. If it is there and is of type InterpreterArrayData type, return it.
                InterpreterDataType resolvedIndex = GetIDT(vNode.expression.get(), localVariables);
                InterpreterDataType indexReturn = localVariables.get(vNode.getName());
                if (indexReturn instanceof InterpreterArrayDataType) { // only returns if the value is of type InterpreterArrayDataType
                    return ((InterpreterArrayDataType) indexReturn).iadt.get(resolvedIndex.getStr());
                } else {
                    throw new Exception("Error indexReturn is not of type InterpreterArrayDataType: Interpreter<GetIDT(VariableReferenceNode)>");
                }
            }
        } else if (node instanceof OperationNode opNode) { // If instance of OperationNode, check which operation type it is and handle each case seperately.
            InterpreterDataType left = GetIDT(opNode.left, localVariables);
            if (opNode.thisEnum == OperationNode.OperationList.EXPONENT) { // operation type is exponents so you try to convert to float and return the correct answer
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(EXPONENT)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(Math.pow(leftFloat, rightFloat)));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(EXPONENT)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.ADD) { // operation type is add, so you try to convert to float and return answer after adding
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(ADD)>");
                    }
                    InterpreterDataType test = GetIDT(opNode.right.get(), localVariables);
                    float rightFloat = Float.parseFloat(test.getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat + rightFloat));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(ADD)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.SUBTRACT) { // operation type is multiply, so you try to convert to float and return answer after subtracting
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(SUBTRACT)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat - rightFloat));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(SUBTRACT)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.MULTIPLY) { // operation type is multipy, so you try to convert to float and return answer after multiplying
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(MULTIPLY)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat * rightFloat));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(MULTIPLY)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.DIVIDE) { // operation type is divide, so you try to convert to float and return answer after dividing
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(DIVIDE)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat / rightFloat));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(Divide)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.MODULO) { // operation type is modulo, so you try to convert to float and return answer after modulo
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(MODULO)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat % rightFloat));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(MODULO)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.ASSIGN) { // operation type is assign, so you check if it a VariableReferenceNode, if it is then put it into the localVariables hashmap and return the value.
                if (opNode.left instanceof VariableReferenceNode) {
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(ASSIGN)>");
                    }
                    localVariables.put(((VariableReferenceNode) opNode.left).getName(), GetIDT(opNode.right.get(), localVariables));
                    return localVariables.get(((VariableReferenceNode) opNode.left).getName());
                } else {
                    throw new Exception("Assign Error: Interpreter<OperationNode(ASSIGN)");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.EQ) { // operation type is equal, so you try to convert to float, if it converts successfully, compare the floats, or else you compare strings
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(EQ)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat == rightFloat));
                } catch (NumberFormatException e) {
                    String leftString = left.getStr();
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(EQ)>");
                    }
                    String rightString = GetIDT(opNode.right.get(), localVariables).getStr();
                    return new InterpreterDataType(String.valueOf(leftString.equals(rightString)));
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.NE) { // operation type is not equal to, so you try to convert to float, if it converts successfully, compare the floats, or else you compare strings
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(NE)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat != rightFloat));
                } catch (NumberFormatException e) {
                    String leftString = left.getStr();
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(NE)>");
                    }
                    String rightString = GetIDT(opNode.right.get(), localVariables).getStr();
                    return new InterpreterDataType(String.valueOf(!Objects.equals(leftString, rightString)));
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.LT) { // operation type is less than, so you try to convert to float, if it converts successfully, compare the floats, or else you compare strings
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(LT)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat < rightFloat));
                } catch (NumberFormatException e) {
                    String leftString = left.getStr();
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(LT)>");
                    }
                    String rightString = GetIDT(opNode.right.get(), localVariables).getStr();
                    return new InterpreterDataType(String.valueOf(leftString.compareTo(rightString) < 0));
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.LE) { // operation type is less than equal, so you try to convert to float, if it converts successfully, compare the floats, or else you compare strings
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(LE)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat <= rightFloat));
                } catch (NumberFormatException e) {
                    String leftString = left.getStr();
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(LE)>");
                    }
                    String rightString = GetIDT(opNode.right.get(), localVariables).getStr();
                    return new InterpreterDataType(String.valueOf(leftString.compareTo(rightString) <= 0));
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.GT) { // operation type is greater than, so you try to convert to float, if it converts successfully, compare the floats, or else you compare strings
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(GT)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat > rightFloat));
                } catch (NumberFormatException e) {
                    String leftString = left.getStr();
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(GT)>");
                    }
                    String rightString = GetIDT(opNode.right.get(), localVariables).getStr();
                    return new InterpreterDataType(String.valueOf(leftString.compareTo(rightString) > 0));
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.GE) { // operation type is greater than equal, so you try to convert to float, if it converts successfully, compare the floats, or else you compare strings
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(GE)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat >= rightFloat));
                } catch (NumberFormatException e) {
                    String leftString = left.getStr();
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(GE)>");
                    }
                    String rightString = GetIDT(opNode.right.get(), localVariables).getStr();
                    return new InterpreterDataType(String.valueOf(leftString.compareTo(rightString) >= 0));
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.AND) { // operation type is and, so you try to convert to float and return boolean the value of and applied on the left expression and right
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(AND)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    boolean leftCheckZero = leftFloat == 0;
                    boolean rightCheckZero = rightFloat == 0;
                    return new InterpreterDataType(String.valueOf(leftCheckZero && rightCheckZero));
                } catch (NumberFormatException e) {
                    return new InterpreterDataType("false");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.OR) { // operation type is or, so you try to convert to float and return the boolean value of or applied on the left expression and right
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(OR)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    boolean leftCheckZero = leftFloat == 0;
                    boolean rightCheckZero = rightFloat == 0;
                    return new InterpreterDataType(String.valueOf(leftCheckZero || rightCheckZero));
                } catch (NumberFormatException e) {
                    return new InterpreterDataType("false");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.NOT) { // operation type is not, so you try to convert the right expression to float and return the not value of it
                try {
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(NOT)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    boolean rightCheckZero = rightFloat == 0;
                    return new InterpreterDataType(String.valueOf(!rightCheckZero));
                } catch (NumberFormatException e) {
                    return new InterpreterDataType("false");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.MATCH) { // operation type is match, so you try to find a match of the right expression in the left expression and return a boolean
                try {
                    String leftString = left.getStr();
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<GetIDT(MATCH)>");
                    }
                    PatternNode rightPattern = (PatternNode) opNode.right.get();
                    Pattern pattern = Pattern.compile(rightPattern.getPattern());
                    Matcher matcher = pattern.matcher(leftString);
                    return new InterpreterDataType(String.valueOf(matcher.find()));
                } catch (PatternSyntaxException e) {
                    throw new Exception("Error in Regex Matching: Interpreter<GetIDT(MATCH)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.NOTMATCH) { // operation type is not match, so you try to find a match of the right expression in the left expression and return the opposite boolean
                try {
                    String leftString = left.getStr();
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<GetIDT(NOTMATCH)>");
                    }
                    PatternNode rightPattern = (PatternNode) opNode.right.get();
                    Pattern pattern = Pattern.compile(rightPattern.getPattern());
                    Matcher matcher = pattern.matcher(leftString);
                    return new InterpreterDataType(String.valueOf(!matcher.find()));
                } catch (PatternSyntaxException e) {
                    throw new Exception("Error in Regex Matching: Interpreter<GetIDT(NOTMATCH)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.DOLLAR) { // operation type is dollar, so you get the left expression and return it with a dollar sign in front of it
                return new InterpreterDataType("$" + GetIDT(opNode.left, localVariables).getStr());
            } else if (opNode.thisEnum == OperationNode.OperationList.PREINC) { // operation type is preinc, so you get the right expression, add one to it, and return it
                try {
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(PREINC)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(rightFloat + 1));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(PREINC)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.POSTINC) { // operation type is preinc, so you get the left expression, add one to it, and return it
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat + 1));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(POSTINC)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.PREDEC) { // operation type is predec, so you get the right expression, subtract one from it, and return it
                try {
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(PREDEC)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(rightFloat - 1.0));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(PREDEC)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.POSTDEC) { // operation type is predec, so you get the left expression, subtract one from it, and return it
                try {
                    float leftFloat = Float.parseFloat(left.getStr());
                    return new InterpreterDataType(String.valueOf(leftFloat - 1.0));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(POSTDEC)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.UNARYPOS) { // operation type is unarypos, so you get the right expression, and multipy 1.0 to it
                try {
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(UNARYPOS)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(rightFloat * 1.0));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(UNARYPOS)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.UNARYNEG) { // operation type is unarypos, so you get the right expression, and multipy -1.0 to it
                try {
                    if (opNode.right.isEmpty()) {
                        throw new Exception("Right is Empty: Interpreter<OperationNode(UNARYNEG)>");
                    }
                    float rightFloat = Float.parseFloat(GetIDT(opNode.right.get(), localVariables).getStr());
                    return new InterpreterDataType(String.valueOf(rightFloat * (-1.0)));
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid Numbers: Interpreter<OperationNode(UNARYNEG)>");
                }
            } else if (opNode.thisEnum == OperationNode.OperationList.CONCATENATION) { // operation type is concatenation, so you get the left expression and right expression, combine them into one string and return it
                if (opNode.right.isEmpty()) {
                    throw new Exception("Right is Empty: Interpreter<OperationNode(CONCATENATION)>");
                }
                return new InterpreterDataType(opNode.left.toString() + opNode.right.toString());
            } else if (opNode.thisEnum == OperationNode.OperationList.IN) { // operation type is in, so you get the right expression and check if the left exists in the local or global variables, if it does then return the value
                if (opNode.right.isEmpty()) {
                    throw new Exception("Right is Empty: Interpreter<OperationNode(IN)>");
                }
                if (opNode.right.get() instanceof VariableReferenceNode vNode) {
                    if (vNode.expression.isPresent()) {
                        if (localVariables.containsKey(opNode.left.toString())) {
                            return localVariables.get(opNode.left.toString());
                        } else if (globalVariables.containsKey(opNode.left.toString())) {
                            return globalVariables.get(opNode.left.toString());
                        }
                    } else {
                        throw new Exception("Not an Array: Interpreter<OperationNode(IN)>");
                    }
                }
            }
        }
        return null;
    }
}
