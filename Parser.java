import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/*
 * The Parser class which implements a Parse method
 * which goes through the list of Token and calls
 * ParseAction and ParseFunction. ParseAction checks
 * for the keywords BEGIN and END and works with them.
 * ParseFunction check if the format of a new function
 * is written correctly.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class Parser {
    private TokenManager tokenManager;

    public static class ProgramNode {
        private List<BlockNode> beginBlockNodes;
        private List<BlockNode> endBlockNodes;
        private List<BlockNode> otherBlockNodes;
        private List<FunctionDefinitionNode> functionNodes;

        public ProgramNode () {
            this.beginBlockNodes = new ArrayList<>();
            this.endBlockNodes = new ArrayList<>();
            this.otherBlockNodes = new ArrayList<>();
            this.functionNodes = new ArrayList<>();
        }

        List<BlockNode> getBeginBlockNodes() { return beginBlockNodes; }

        List<BlockNode> getEndBlockNodes() { return endBlockNodes; }

        List<BlockNode> getOtherBlockNodes() { return otherBlockNodes; }

        List<FunctionDefinitionNode> getFunctionNodes() { return functionNodes; }

        void addFunctionNode(FunctionDefinitionNode functionDefinitionNode) {
            functionNodes.add(functionDefinitionNode);
        }


        public String toString() {
            return "Begin Nodes: " + beginBlockNodes + "\nEnd Nodes: " + endBlockNodes + "\nOther Nodes: " +
                    otherBlockNodes + "\nFunction Nodes: " + functionNodes;
        }
    }

    public Parser(LinkedList<Token> inputTokens) { this.tokenManager = new TokenManager(inputTokens); }

    // loops and deletes all seperator tokens from the beginning
    boolean AcceptSeperators() {
        int seperatorCount = 0;
        while (tokenManager.MoreTokens()) {
            if (tokenManager.MatchAndRemove(Token.TokenType.SEPERATOR).isEmpty()) { return seperatorCount > 0; }
            seperatorCount++;
        }
        return seperatorCount > 0;
    }

    ProgramNode Parse() throws Exception {
        ProgramNode programNode = new ProgramNode();
        while (tokenManager.MoreTokens()) {
            if (!ParseFunction(programNode) && !ParseAction(programNode)) { throw new Exception("Syntax Error: Parser<Parse>"); }
        }
        return programNode;
    }

    boolean ParseFunction(ProgramNode programNode) throws Exception {
        Optional<Token> tokenTemp;
        LinkedList<String> parameters = new LinkedList<>();
        String functionName;
        if (!tokenManager.MoreTokens()) return false;
        if (tokenManager.MatchAndRemove(Token.TokenType.FUNCTION).isPresent()) {
            tokenTemp = tokenManager.MatchAndRemove(Token.TokenType.WORD);
            if (tokenTemp.isPresent()) {
                functionName = tokenTemp.get().getTokenValue();
                if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
                    while (tokenManager.MoreTokens()) {
                        if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) { break; }
                        tokenTemp = tokenManager.MatchAndRemove(Token.TokenType.WORD);
                        if (tokenTemp.isPresent()) {
                            parameters.add(tokenTemp.get().getTokenValue());
                            if (tokenManager.MatchAndRemove(Token.TokenType.COMMA).isPresent()) {
                                AcceptSeperators();
                            } else {
                                if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                                    break;
                                } else { return false; }
                            }
                        } else {
                            if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isEmpty()) { return false; }
                            break;
                        }
                    }
                    AcceptSeperators();

                    if (tokenManager.MatchAndRemove(Token.TokenType.OPENCURLYBRACKET).isPresent()) {
                        AcceptSeperators();
                        if (tokenManager.MatchAndRemove(Token.TokenType.CLOSECURLYBRACKET).isPresent()) {
                            FunctionDefinitionNode newFunctionDefinitionNode = new FunctionDefinitionNode(functionName, parameters);
                            BlockNode blockNode = ParseBlock();
                            newFunctionDefinitionNode.statementNodesMutator(blockNode.getStatement());
                            programNode.functionNodes.add(newFunctionDefinitionNode);
                            AcceptSeperators();
                            return true;
                        } else { return false; }
                    }
                }
            }
        }
        return false;
    }

    boolean ParseAction(ProgramNode programNode) throws Exception {
        if (tokenManager.MoreTokens()) {
            AcceptSeperators();
            if (tokenManager.MatchAndRemove(Token.TokenType.BEGIN).isPresent()) {
                BlockNode beginBlock = ParseBlock();
                programNode.beginBlockNodes.add(beginBlock);
                AcceptSeperators();
                return true;
            } else if (tokenManager.MatchAndRemove(Token.TokenType.END).isPresent()) {
                BlockNode endBlock = ParseBlock();
                programNode.endBlockNodes.add(endBlock);
                AcceptSeperators();
                return true;
            } else {
                Optional<Node> parseOperation = ParseOperation();
                BlockNode otherBlock = ParseBlock();
                if (parseOperation.isPresent()) { otherBlock.setCondition(parseOperation); }
                programNode.otherBlockNodes.add(otherBlock);
                return false;
            }
        }
        return false;
    }

    // removes the open curly bracket, and loops and parses each statement until a close curly bracket is found
    BlockNode ParseBlock() throws Exception {
        BlockNode newBlockNode = new BlockNode();
        AcceptSeperators();
        if (tokenManager.MatchAndRemove(Token.TokenType.OPENCURLYBRACKET).isPresent()) {
            AcceptSeperators();
            while (tokenManager.MatchAndRemove(Token.TokenType.CLOSECURLYBRACKET).isEmpty()) {
                Optional<StatementNode> parseStatement = ParseStatement();
                if (parseStatement.isEmpty()) { throw new Exception("parseStatement is empty: ParseBlock"); }
                newBlockNode.addStatement(parseStatement.get());
                AcceptSeperators();
            }
        }
        return newBlockNode;
    }

    // parses the different statements possible
    Optional<StatementNode> ParseStatement() throws Exception {
        if (tokenManager.MatchAndRemove(Token.TokenType.CONTINUE).isPresent()) { // parses continue
            Optional<StatementNode> parseContinue = ParseContinue();
            if (parseContinue.isEmpty()) { throw new Exception("parseContinue is empty: ParseStatement"); }
            return parseContinue;
        } else if (tokenManager.MatchAndRemove(Token.TokenType.BREAK).isPresent()) { // parses break
            Optional<StatementNode> parseBreak = ParseBreak();
            if (parseBreak.isEmpty()) { throw new Exception("parseContinue is empty: ParseStatement"); }
            return parseBreak;
        } else if (tokenManager.MatchAndRemove(Token.TokenType.IF).isPresent()) { // parses if
            return ParseIf();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.FOR).isPresent()) { // parses for
            Optional<StatementNode> parseFor = ParseFor();
            if (parseFor.isEmpty()) { throw new Exception("parseFor is empty: ParseStatement"); }
            return parseFor;
        } else if (tokenManager.MatchAndRemove(Token.TokenType.DELETE).isPresent()) { // parses delete
            Optional<StatementNode> parseDelete = ParseDelete();
            if (parseDelete.isEmpty()) { throw new Exception("parseDelete is empty: ParseStatement"); }
            return parseDelete;
        } else if (tokenManager.MatchAndRemove(Token.TokenType.WHILE).isPresent()) { // parses while
            Optional<StatementNode> parseWhile = ParseWhile();
            if (parseWhile.isEmpty()) { throw new Exception("parseWhile is empty: ParseStatement"); }
            return parseWhile;
        } else if (tokenManager.MatchAndRemove(Token.TokenType.DO).isPresent()) { // parses do
            Optional<StatementNode> parseDoWhile = ParseDoWhile();
            if (parseDoWhile.isEmpty()) { throw new Exception("parseDoWhile is empty: ParseStatement"); }
            return parseDoWhile;
        } else if (tokenManager.MatchAndRemove(Token.TokenType.RETURN).isPresent()) { // parses return
            Optional<StatementNode> parseReturn = ParseReturn();
            if (parseReturn.isEmpty()) { throw new Exception("parseReturn is empty: ParseStatement"); }
            return parseReturn;
        } else {  // parses ParseOperation, if it is an instance of AssignmentNode or FunctionCallNode, return it
            Optional<Node> parseOP = ParseOperation();
            if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseStatement"); }
            if (parseOP.get() instanceof AssignmentNode) {
                return Optional.of((AssignmentNode)parseOP.get());
            } else if (parseOP.get() instanceof FunctionCallNode) {
                return Optional.of((FunctionCallNode)parseOP.get());
            }
        }
        throw new Exception("invalid parseOP: ParseStatement");
    }

    // checks if the upcoming tokens create a function call. if it does, the create a FunctionCallNode to handle it
    Optional<Node> ParseFunctionCall() throws Exception {
        Optional<Token> curr = tokenManager.Peek(0);
        LinkedList<Node> parameters = new LinkedList<>();
        if (curr.isPresent() && curr.get().getTokenType() == Token.TokenType.GETLINE) { // parses getline call
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.GETLINE);
            if (token.isEmpty()) { throw new Exception("token is Empty: parseFunctionCall<GETLINE>"); }
            AcceptSeperators();
            return Optional.of(new FunctionCallNode(token.get(), new LinkedList<>()));
        } else if (curr.isPresent() && curr.get().getTokenType() == Token.TokenType.PRINT) { // parses print call
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.PRINT);
            if (token.isEmpty()) { throw new Exception("token is empty: parseFunctionCall<PRINT>"); }
            if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
                Optional<Node> parseOP = ParseOperation();
                if (parseOP.isEmpty()) {
                    throw new Exception("parseOP is empty: ParseFunctionCall<PRINT>");
                }
                parameters.add(parseOP.get());
                if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                    AcceptSeperators();
                    return Optional.of(new FunctionCallNode(token.get(), parameters));
                }
            } else {
                Optional<Node> parseOP = ParseOperation();
                if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseFunctionCall<PRINT>"); }
                parameters.add(parseOP.get());
                AcceptSeperators();
                return Optional.of(new FunctionCallNode(token.get(), parameters));
            }
        } else if (curr.isPresent() && curr.get().getTokenType() == Token.TokenType.PRINTF) { // parses printf call
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.PRINTF);
            if (token.isEmpty()) { throw new Exception("token is empty: parseFunctionCall<PRINTF>"); }
            if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
                Optional<Node> parseOP = ParseOperation();
                if (parseOP.isEmpty()) {
                    throw new Exception("parseOP is empty: ParseFunctionCall<PRINTF>");
                }
                parameters.add(parseOP.get());
                if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                    AcceptSeperators();
                    return Optional.of(new FunctionCallNode(token.get(), parameters));
                }
            } else {
                Optional<Node> parseOP = ParseOperation();
                if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseFunctionCall<PRINTF>"); }
                parameters.add(parseOP.get());
                AcceptSeperators();
                return Optional.of(new FunctionCallNode(token.get(), parameters));
            }
        } else if (curr.isPresent() && curr.get().getTokenType() == Token.TokenType.EXIT) { // parses exit call
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.EXIT);
            if (token.isEmpty()) { throw new Exception("token is empty: parseFunctionCall<EXIT>"); }
            if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
                Optional<Node> parseOP = ParseOperation();
                if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseFunctionCall<EXIT>"); }
                parameters.add(parseOP.get());
                if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                    AcceptSeperators();
                    return Optional.of(new FunctionCallNode(token.get(), parameters));
                }
            } else {
                Optional<Node> parseOP = ParseOperation();
                if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseFunctionCall<EXIT>"); }
                parameters.add(parseOP.get());
                AcceptSeperators();
                return Optional.of(new FunctionCallNode(token.get(), parameters));
            }
        } else if (curr.isPresent() && curr.get().getTokenType() == Token.TokenType.NEXTFILE) { // parses nextfile call
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.NEXTFILE);
            if (token.isEmpty()) { throw new Exception("token is Empty: parseFunctionCall<NEXTFILE>"); }
            AcceptSeperators();
            return Optional.of(new FunctionCallNode(token.get(), new LinkedList<>()));
        } else if (curr.isPresent() && curr.get().getTokenType() == Token.TokenType.NEXT) { // parses next call
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.NEXT);
            if (token.isEmpty()) { throw new Exception("token is Empty: parseFunctionCall<NEXT>"); }
            AcceptSeperators();
            return Optional.of(new FunctionCallNode(token.get(), new LinkedList<>()));
        } else { // parses user function calls
            if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().tokenType == Token.TokenType.WORD && tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().tokenType == Token.TokenType.OPENROUNDBRACKET) {
                Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.WORD);
                if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
                    while (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isEmpty()) {
                        tokenManager.MatchAndRemove(Token.TokenType.COMMA);
                        Optional<Node> parseOP = ParseOperation();
                        if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseFunctionCall<WORD>"); }
                        parameters.add(parseOP.get());
                        if (tokenManager.MatchAndRemove(Token.TokenType.COMMA).isEmpty()) { break; }
                    }
                    tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET);
                    AcceptSeperators();
                    if (token.isEmpty()) { throw new Exception("Token is empty: Parser<ParseFunctionCall(else)>"); };
                    return Optional.of(new FunctionCallNode(token.get(), parameters));
                }
            }
        }
        return Optional.empty();
    }

    // if it is continue statement, return a new ContinueNode
    Optional<StatementNode> ParseContinue() {
        return Optional.of(new ContinueNode());
    }

    // if it is break statement, return a new BreakNode
    Optional<StatementNode> ParseBreak() {
        return Optional.of(new BreakNode());
    }

    // if it is an if statement, gets the parameters, calls parseStatement to get the statements, and returns a new IfNode
    Optional<StatementNode> ParseIf() throws Exception {
        if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
            Optional<Node> parseOP = ParseOperation();
            if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseIf"); }
            if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                BlockNode blockNode = ParseBlock();
                if (blockNode.getStatement().isEmpty()) { throw new Exception("parseBlock is empty: ParseIf"); }
                if (tokenManager.MatchAndRemove(Token.TokenType.ELSE).isPresent()) {
                    if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.IF) {
                        Optional<StatementNode> parseStatement = ParseStatement();
                        if (parseStatement.isEmpty()) { throw new Exception("parseStatement is empty in ParseIf"); }
                        return Optional.of(new IfNode(parseOP.get(), blockNode, parseStatement.get()));
                    } else {
                        BlockNode elseBlockNode = ParseBlock();
                        return Optional.of(new IfNode (parseOP.get(), blockNode, new IfNode(null, elseBlockNode, null)));
                    }
                }
                return Optional.of(new IfNode(parseOP.get(), blockNode, null));
            }
        }
        return Optional.empty();
    }

    // if it is a for statement, gets the parameters, calls parseStatement to get the statements, and returns a new ForNode
    Optional<StatementNode> ParseFor() throws Exception {
        if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
            Optional<Node> condition1 = ParseOperation();
            if (condition1.isEmpty()) { throw new Exception("condition1 is empty: ParseFor"); }
            AcceptSeperators();
            if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                BlockNode blockNode = ParseBlock();
                if (blockNode.getStatement().isEmpty()) { throw new Exception("blockNode is empty: ParseFor<Each>"); }
                return Optional.of(new ForEachNode(condition1.get(), blockNode));
            } else {
                Optional<Node> condition2 = ParseOperation();
                if (condition2.isEmpty()) {
                    throw new Exception("condition2 is empty: ParseFor");
                }
                AcceptSeperators();
                Optional<Node> condition3 = ParseOperation();
                if (condition3.isEmpty()) { throw new Exception("condition3 is empty: ParseFor"); }
                if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                    BlockNode blockNode = ParseBlock();
                    if (blockNode.getStatement().isEmpty()) { throw new Exception("blockNode is empty: ParseFor"); }
                    return Optional.of(new ForNode(condition1.get(), condition2.get(), condition3.get(), blockNode));
                }
            }
        }
        return Optional.empty();
    }

    // if it is delete statement, get the parameter, and return a new DeleteNode
    Optional<StatementNode> ParseDelete() throws Exception {
        if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
            Optional<Node> parseLValue = ParseLValue();
            if (parseLValue.isEmpty()) { throw new Exception("parseLValue is empty: ParseDelete"); }
            if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                return Optional.of(new DeleteNode(parseLValue.get()));
            }
        }
        return Optional.empty();
    }

    // if it is a while statement, gets the parameter, calls parseStatement to get the statements, and returns a new WhileNode
    Optional<StatementNode> ParseWhile() throws Exception {
        if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
            Optional<Node> parseOP = ParseOperation();
            if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseWhile"); }
            if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                return Optional.of(new WhileNode(parseOP.get(), ParseBlock()));
            }
        }
        return Optional.empty();
    }

    // if it is a do while statement, calls parseStatement to get the statements, gets the parameters, and returns a new DoWhileNode
    Optional<StatementNode> ParseDoWhile() throws Exception {
        BlockNode parseBlock = ParseBlock();
        if (parseBlock.getStatement().isEmpty()) { throw new Exception("parseBlock is empty: ParseDoWhile"); }
        if (tokenManager.MatchAndRemove(Token.TokenType.WHILE).isPresent()) {
            if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
                Optional<Node> parseOP = ParseOperation();
                if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseDoWhile"); }
                if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                    return Optional.of(new DoWhileNode(parseBlock, parseOP.get()));
                }
            }
        }
        return Optional.empty();
    }

    // if it is return statement, get the parameter, and return a new ReturnNode
    Optional<StatementNode> ParseReturn() throws Exception {
        Optional<Node> parseOP = ParseOperation();
        if (parseOP.isEmpty()) { throw new Exception("parseOP is empty: ParseReturn"); }
        return Optional.of(new ReturnNode(parseOP.get()));
    }

    Optional<Node> ParseOperation() throws Exception {
        return ParseAssignmentOperations();
    }

    // parses all assignment statement operations
    Optional<Node> ParseAssignmentOperations() throws Exception {
        Optional<Node> lvalue = ParseConditionalOperations();
        if (lvalue.isEmpty()) { throw new Exception("lvalue is empty: ParseAssignmentOperations"); }
        if (tokenManager.MatchAndRemove(Token.TokenType.ASSIGNEQUAL).isPresent()) {
            return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), ParseOperation(), OperationNode.OperationList.ASSIGN)));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.MINUSEQUAL).isPresent()) {
            return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), ParseOperation(), OperationNode.OperationList.SUBTRACT)));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.PLUSEQUAL).isPresent()) {
            return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), ParseOperation(), OperationNode.OperationList.ADD)));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.DIVIDEEQUAL).isPresent()) {
            return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), ParseOperation(), OperationNode.OperationList.DIVIDE)));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.MULTIPLYEQUAL).isPresent()) {
            return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), ParseOperation(), OperationNode.OperationList.MULTIPLY)));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.MODEQUAL).isPresent()) {
            return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), ParseOperation(), OperationNode.OperationList.MODULO)));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.POWEREQUAL).isPresent()) {
            return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), ParseOperation(), OperationNode.OperationList.EXPONENT)));
        }
        return lvalue;
    }

    // parses all conditional statement operations
    Optional<Node> ParseConditionalOperations() throws Exception {
        Optional<Node> parseLogicalOP = ParseLogicalOperations();
        if (parseLogicalOP.isEmpty()) { throw new Exception("ParseLogicalOP is empty: ParseConditionalOperations"); }
        if (tokenManager.MatchAndRemove(Token.TokenType.QUESTIONMARK).isPresent()) {
            Optional<Node> parse2 = ParseOperation();
            if (tokenManager.MatchAndRemove(Token.TokenType.COLON).isPresent()) {
                return Optional.of(new TernaryNode(parseLogicalOP.get(), parse2, ParseOperation()));
            }
        }
        return parseLogicalOP;
    }

    // parses all logical statement operations
    Optional<Node> ParseLogicalOperations() throws Exception {
        Optional<Node> parseArrayOP = ParseArrayOperations();
        if (parseArrayOP.isEmpty()) { throw new Exception("parseArrayOP is empty: ParseLogicalOperations"); }
        if (tokenManager.MatchAndRemove(Token.TokenType.OR).isPresent()) {
            return Optional.of(new OperationNode(parseArrayOP.get(), ParseOperation(), OperationNode.OperationList.OR));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.AND).isPresent()) {
            return Optional.of(new OperationNode(parseArrayOP.get(), ParseOperation(), OperationNode.OperationList.AND));
        }
        return parseArrayOP;
    }

    // parses all array statement operations
    Optional<Node> ParseArrayOperations() throws Exception {
        Optional<Node> parseMatchOP = ParseMatchOperations();
        if (parseMatchOP.isEmpty()) { throw new Exception("parseMatchOP is empty: ParseArrayOperations"); }
        if (tokenManager.MatchAndRemove(Token.TokenType.IN).isPresent()) {
            return Optional.of(new OperationNode(parseMatchOP.get(), ParseLValue(), OperationNode.OperationList.IN));
        }
        return parseMatchOP;
    }

    // parses all match statement operations
    Optional<Node> ParseMatchOperations() throws Exception {
        Optional<Node> parseComparisonOP = ParseComparisonOperations();
        if (parseComparisonOP.isEmpty()) { throw new Exception("parseComparisonOP is empty: ParseAssignmentOperations"); }
        if (tokenManager.MatchAndRemove(Token.TokenType.NOTMATCH).isPresent()) {
            return Optional.of(new OperationNode(parseComparisonOP.get(), ParseComparisonOperations(), OperationNode.OperationList.NOTMATCH));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.MATCH).isPresent()) {
            return Optional.of(new OperationNode(parseComparisonOP.get(), ParseComparisonOperations(), OperationNode.OperationList.MATCH));
        }
        return parseComparisonOP;
    }

    // parses all comparison statement operations
    Optional<Node> ParseComparisonOperations() throws Exception {
        Optional<Node> parseStringCon = ParseStringConcatenation();
        if (parseStringCon.isEmpty()) { throw new Exception("parseStringCon is empty: ParseComparisonOperations"); }
        if (tokenManager.MatchAndRemove(Token.TokenType.LESSTHAN).isPresent()) {
            return Optional.of(new OperationNode(parseStringCon.get(), ParseStringConcatenation(), OperationNode.OperationList.LT));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.LESSTHANEQUAL).isPresent()) {
            return Optional.of(new OperationNode(parseStringCon.get(), ParseStringConcatenation(), OperationNode.OperationList.LE));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.NOTEQUALTO).isPresent()) {
            return Optional.of(new OperationNode(parseStringCon.get(), ParseStringConcatenation(), OperationNode.OperationList.NE));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.EQUALTO).isPresent()) {
            return Optional.of(new OperationNode(parseStringCon.get(), ParseStringConcatenation(), OperationNode.OperationList.EQ));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.GREATERTHAN).isPresent()) {
            return Optional.of(new OperationNode(parseStringCon.get(), ParseStringConcatenation(), OperationNode.OperationList.GT));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.GREATERTHANEQUAL).isPresent()) {
            return Optional.of(new OperationNode(parseStringCon.get(), ParseStringConcatenation(), OperationNode.OperationList.GE));
        }
        return parseStringCon;
    }

    /*
     * Parses all string concatenation statements.
     * Checks if there are two strings which are beside each other. If this exits, then it creates a new OperationNode
     * which stores the left node, right node, and the operation type concatenation.
     */
    Optional<Node> ParseStringConcatenation() throws Exception {
        if (tokenManager.MoreTokens() && tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.WORD) {
            if (tokenManager.MoreTokens() && tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.WORD) {
                Optional<Token> word1 = tokenManager.MatchAndRemove(Token.TokenType.WORD);
                Optional<Token> word2 = tokenManager.MatchAndRemove(Token.TokenType.WORD);
                if (word1.isEmpty()) { throw new Exception("First word is empty: Parser<ParseStringConcatenation>"); }
                if (word2.isEmpty()) { throw new Exception("First word is empty: Parser<ParseStringConcatenation>"); }
                return Optional.of(new OperationNode(new VariableReferenceNode(word1.get().getTokenValue()), Optional.of(new VariableReferenceNode(word2.get().getTokenValue())), OperationNode.OperationList.CONCATENATION));
            }
        }
//        Optional<Node> parseLeft = ParseMathOperations();
//        Optional<Node> parseRight = ParseMathOperations();
//        if (parseLeft.isPresent() && parseRight.isPresent() ) {
//            if (parseLeft.get() instanceof VariableReferenceNode && parseRight.get() instanceof VariableReferenceNode) {
//                parseLeft =  Optional.of(new OperationNode(parseLeft.get(), parseRight, OperationNode.OperationList.CONCATENATION));
//            }
//        }
        return ParseMathOperations();
//        return parseLeft;
    }


    /*
     * Parses all math statement operations.
     * Gets the left and right expression of the statement and creates a new OperationNode which stores the left node,
     * right node, and the math operation which is being done.
     */
    Optional<Node> ParseMathOperations() throws Exception {
        Optional<Node> parseExpOP = ParseExponentOperations();
        if (parseExpOP.isEmpty()) {  return Expression(); }
        if (tokenManager.MatchAndRemove(Token.TokenType.MINUS).isPresent()) {
            return Optional.of(new OperationNode(parseExpOP.get(), Expression(), OperationNode.OperationList.SUBTRACT));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.PLUS).isPresent()) {
            return Optional.of(new OperationNode(parseExpOP.get(), Expression(), OperationNode.OperationList.ADD));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.MOD).isPresent()) {
            return Optional.of(new OperationNode(parseExpOP.get(), Expression(), OperationNode.OperationList.MODULO));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.DIVIDE).isPresent()) {
            return Optional.of(new OperationNode(parseExpOP.get(), Expression(), OperationNode.OperationList.DIVIDE));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.MULTIPLY).isPresent()) {
            return Optional.of(new OperationNode(parseExpOP.get(), Expression(), OperationNode.OperationList.MULTIPLY));
        }
        return parseExpOP;
    }

    Optional<Node> Factor() throws Exception {
        Optional<Token> num = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
        if (num.isPresent()) { return Optional.of(new ConstantNode(num.get().getTokenValue())); }
        if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) {
            Optional<Node> exp = Expression();
            if (exp.isEmpty()) { throw new Exception("exp is null: Factor"); }
            if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isEmpty()) {
                throw new Exception("Close Round Bracket is missing: Factor");
            }
            return exp;
        }
        return Optional.empty();

        // call exponent if its there then return that else return bottom level
    }

    Optional<Node> Term() throws Exception {
        Optional<Node> left = Factor();
        if (left.isEmpty()) { throw new Exception("left is empty: Term"); }
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.MULTIPLY);
            if (op.isEmpty()) { op = tokenManager.MatchAndRemove(Token.TokenType.DIVIDE); }
            if (op.isEmpty()) { op = tokenManager.MatchAndRemove(Token.TokenType.MOD); }
            if (op.isEmpty()) { return left; }
            Optional<Node> right = Factor();
            left = Optional.of(new MathOpNode(left.get(), op, right));
        } while (true);
    }

    Optional<Node> Expression() throws Exception {
        Optional<Node> left = Term();
        if (left.isEmpty()) { throw new Exception("left is empty: Expression"); }
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.PLUS);
            if (op.isEmpty()) { op = tokenManager.MatchAndRemove(Token.TokenType.MINUS); }
            if (op.isEmpty()) { return left; }
            Optional<Node> right = Term();
            left = Optional.of(new MathOpNode(left.get(), op, right));
        } while (true);
    }

    // parses all exponent statement operations
    Optional<Node> ParseExponentOperations() throws Exception {
        Optional<Node> lvalue = ParseBottomLevel();
        if (lvalue.isEmpty()) { throw new Exception("lvalue is empty: ParseExponentOperations"); }
        if (tokenManager.MatchAndRemove(Token.TokenType.POWER).isPresent()) {
            Optional<Node> p = ParseOperation();
            return Optional.of(new OperationNode(lvalue.get(), p, OperationNode.OperationList.EXPONENT));
        }
        return lvalue;
    }

    // parses post increment and decrement statement operations
    Optional<Node> ParsePostOperations() throws Exception {
        AcceptSeperators();
        Optional<Node> lvalue = ParseLValue();
        if (lvalue.isEmpty()) { throw new Exception("lvalue is empty: ParsePostOperations"); }
        if (tokenManager.MatchAndRemove(Token.TokenType.INCREMENTONE).isPresent()) {
            return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), OperationNode.OperationList.POSTINC)));
        } else if (tokenManager.MatchAndRemove(Token.TokenType.DECREMENTONE).isPresent()) {
            return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), OperationNode.OperationList.POSTDEC)));
        }
        return lvalue;
    }

    // parses all basic bottom level statement operations
    Optional<Node> ParseBottomLevel() throws Exception {
        Optional<Token> curr = tokenManager.Peek(0);
        Optional<Token> tempToken;
        if (!tokenManager.MoreTokens()) { throw new Exception("No More Tokens: ParseBottomLevel."); }
        if (curr.isEmpty()) { throw new Exception("No More Tokens: ParseBottomLevel."); }
        if (curr.get().getTokenType() == Token.TokenType.STRINGLITERAL) { // STRINGLITERAL -> ConstantNode(value)
            tempToken = tokenManager.MatchAndRemove(Token.TokenType.STRINGLITERAL);
            if (tempToken.isEmpty()) { throw new Exception("tempToken is empty: ParseBottomLevel"); }
            ConstantNode cNode = new ConstantNode(tempToken.get().getTokenValue());
            return Optional.of(cNode);
        } else if (curr.get().getTokenType() == Token.TokenType.NUMBER) { // NUMBER -> ConstantNode(value)
            tempToken = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (tempToken.isEmpty()) { throw new Exception("tempToken is empty: ParseBottomLevel"); }
            ConstantNode cNode = new ConstantNode(tempToken.get().getTokenValue());
            return Optional.of(cNode);
        } else if (curr.get().getTokenType() == Token.TokenType.REGEX) { // PATTERN -> PatterNode(value)
            tempToken = tokenManager.MatchAndRemove(Token.TokenType.REGEX);
            if (tempToken.isEmpty()) { throw new Exception("tempToken is empty: ParseBottomLevel"); }
            PatternNode pNode = new PatternNode(tempToken.get().getTokenValue());
            return Optional.of(pNode);
        } else if (tokenManager.MatchAndRemove(Token.TokenType.OPENROUNDBRACKET).isPresent()) { // LPAREN ParseOperation() RPAREN -> result of ParseOperation
            Optional<Node> parseOP = ParseOperation();
            if (parseOP.isEmpty()) { throw new Exception("Brackets: Parse Operation is Empty"); }
            if (tokenManager.MatchAndRemove(Token.TokenType.CLOSEROUNDBRACKET).isPresent()) {
                return parseOP;
            }
        } else if (tokenManager.MatchAndRemove(Token.TokenType.NOT).isPresent()) { // NOT ParseOperation() -> Operation(result of ParseOperation, NOT)
            Optional<Node> parseOP = ParseOperation();
            if (parseOP.isEmpty()) { throw new Exception("Not: Parse Operation is Empty"); }
            OperationNode opNode = new OperationNode(parseOP.get(), OperationNode.OperationList.NOT);
            return Optional.of(opNode);
        } else if (tokenManager.MatchAndRemove(Token.TokenType.MINUS).isPresent()) { // MINUS ParseOperation() -> Operation(result of ParseOperation, UNARYNEG)
            Optional<Node> parseOP = ParseOperation();
            if (parseOP.isEmpty()) { throw new Exception("Minus: Parse Operation is Empty"); }
            OperationNode opNode = new OperationNode(parseOP.get(), OperationNode.OperationList.UNARYNEG);
            return Optional.of(opNode);
        } else if (tokenManager.MatchAndRemove(Token.TokenType.PLUS).isPresent()) { // PLUS ParseOperation() -> Operation(result of ParseOperation, UNARYPOS)
            Optional<Node> parseOP = ParseOperation();
            if (parseOP.isEmpty()) { throw new Exception("Plus: Parse Operation is Empty"); }
            OperationNode opNode = new OperationNode(parseOP.get(), OperationNode.OperationList.UNARYPOS);
            return Optional.of(opNode);
        } else if (curr.get().getTokenType() == Token.TokenType.INCREMENTONE) { // INCREMENT ParseOperation() -> Operation(result of ParseOperation, PREINC)
            tokenManager.MatchAndRemove(Token.TokenType.INCREMENTONE);
            Optional<Node> parseOP = ParseOperation();
            if (parseOP.isEmpty()) { throw new Exception("IncrementOne: Parse Operation is Empty"); }
            AssignmentNode opNode = new AssignmentNode(parseOP.get(), new OperationNode(parseOP.get(), OperationNode.OperationList.PREINC));
            return Optional.of(opNode);
        } else if (curr.get().getTokenType() == Token.TokenType.DECREMENTONE) { // DECREMENT ParseOperation() -> Operation(result of ParseOperation, PREDEC)
            tokenManager.MatchAndRemove(Token.TokenType.DECREMENTONE);
            Optional<Node> parseOP = ParseOperation();
            if (parseOP.isEmpty()) { throw new Exception("DecrementOne: Parse Operation is Empty"); }
            AssignmentNode opNode = new AssignmentNode(parseOP.get(), new OperationNode(parseOP.get(), OperationNode.OperationList.PREDEC));
            return Optional.of(opNode);
        }

        Optional<Node> parseFunctionCall = ParseFunctionCall();
        if (parseFunctionCall.isPresent()) {
            return parseFunctionCall;
        }
        Optional<Node> postOP = ParsePostOperations();
        if (postOP.isPresent()) {
            return postOP;
        }
        return ParseLValue();
    }

    // parses all low level statement operations
    Optional<Node> ParseLValue() throws Exception {
        Optional<Token> curr = tokenManager.Peek(0);
        if (!tokenManager.MoreTokens()) { throw new Exception("No More Tokens: ParseLValue."); }
        if (curr.isEmpty()) { throw new Exception("No More Tokens: ParseLValue."); }
        if (curr.get().getTokenType() == Token.TokenType.DOLLAR) { // pattern check for DOLLAR + ParseBottomLevel()
            tokenManager.MatchAndRemove(Token.TokenType.DOLLAR);
            Optional<Node> parseBottomLvl = ParseBottomLevel();
            if (parseBottomLvl.isEmpty()) { throw new Exception("Bottom Level is Empty."); }
            OperationNode newOpNode = new OperationNode(parseBottomLvl.get(), OperationNode.OperationList.DOLLAR);
            return Optional.of(newOpNode);
        } else if (curr.get().getTokenType() == Token.TokenType.WORD) {
            Optional<Token> tempToken = tokenManager.MatchAndRemove(Token.TokenType.WORD);
            if (tempToken.isEmpty()) { throw new Exception("tempToken is empty: ParseLValue<WORD>"); }
            if (tokenManager.MatchAndRemove(Token.TokenType.OPENSQUAREBRACKET).isPresent()) { // pattern check for WORD + OPENARRAY + ParseOperation() + CLOSEARRAY
                Optional<Node> parseOP = ParseOperation();
                if (parseOP.isEmpty()) { throw new Exception("Empty Parse Operation"); }
                if (tokenManager.MatchAndRemove(Token.TokenType.CLOSESQUAREBRACKET).isPresent()) {
                    VariableReferenceNode newVRN = new VariableReferenceNode(tempToken.get().getTokenValue(), parseOP);
                    return Optional.of(newVRN);
                }
            } else { // pattern check for WORD (and no OPENARRAY)
                VariableReferenceNode newVRN = new VariableReferenceNode(tempToken.get().getTokenValue());
                return Optional.of(newVRN);
            }
        }
        return Optional.empty();
    }
}
