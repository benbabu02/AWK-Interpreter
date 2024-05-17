package csi404.assignment7;

import java.util.HashMap;
import java.util.LinkedList;

public class Lexer {
    private StringHandler stringHandler;
    private int lineNumber;
    private int charPosition;
    private HashMap<String, Token.TokenType> keywords;
    private HashMap<String, Token.TokenType> oneCharacter;

    public Lexer(String str) {
        this.stringHandler = new StringHandler(str);
        this.lineNumber = 0;
        this.charPosition = 0;
        keywords = new HashMap<>();
        oneCharacter = new HashMap<>();
        Populate_HashMaps();
    }

    LinkedList<Token> Lex() throws Exception {
        LinkedList<Token> tokens = new LinkedList<>();
        while (!stringHandler.IsDone()) {
            Token symbolToken = ProcessSymbol();
            if (symbolToken != null) {
                tokens.add(symbolToken);
            } else if (SkipCharacterCheck()) {
                stringHandler.Swallow(1);
                charPosition++;
            } else if (Character.isLetter(stringHandler.Peek(0))) { // found a word
                tokens.add(ProcessWord());
            } else if (Character.isDigit(stringHandler.Peek(0))) {
                tokens.add(ProcessNumber());
            } else {
                throw new Exception("ERROR: Character not recognized: " + stringHandler.Peek(0));
            }
        }
        return tokens;
    }

    private Token ProcessWord() throws Exception {
        String str = "";
        while (!stringHandler.IsDone() && !SkipCharacterCheck()) {
            if (Character.isLetter(stringHandler.Peek(0))) {
                str += stringHandler.GetChar();
                charPosition++;
            } else {
                break;
            }
        }
        if (keywords.containsKey(str)) {
            if (str.equals("R")) {
                Token number = ProcessNumber();
                return new Token(keywords.get(str), lineNumber + 1, charPosition + 1, number.getTokenValue());
            } else {
                return new Token(keywords.get(str), lineNumber + 1, charPosition + 1);
            }
        } else {
            throw new Exception("ERROR: ProcessWord word not recognized: " + str);
        }
    }

    private Token ProcessNumber() throws Exception {
        String str = "";
        while (!stringHandler.IsDone() && !SkipCharacterCheck()) {
            if (Character.isDigit(stringHandler.Peek(0))) {
                str += stringHandler.GetChar();
                charPosition++;
            } else {
                throw new Exception("Invalid Character for number: " + stringHandler.Peek(0));
            }
        }
        return new Token(Token.TokenType.NUMBER, lineNumber+1, charPosition+1, str);
    }

    private Token ProcessSymbol() {
        Token returnToken = null;
        if (!stringHandler.IsDone() && returnToken == null && oneCharacter.containsKey(stringHandler.PeekString(1))) {
            char tempChar = stringHandler.GetChar();
            returnToken = new Token(oneCharacter.get(String.valueOf(tempChar)), lineNumber, charPosition);
            if (tempChar == '\n') { lineNumber++; }
            charPosition++;
        }
        return returnToken;
    }

    private boolean SkipCharacterCheck() {
        return Character.isWhitespace(stringHandler.Peek(0)) || stringHandler.Peek(0) == '\t' || stringHandler.Peek(0) == '\r';
    }

    private void Populate_HashMaps() {
        keywords.put("math", Token.TokenType.MATH);
        keywords.put("add", Token.TokenType.ADD);
        keywords.put("subtract", Token.TokenType.SUBTRACT);
        keywords.put("multiply", Token.TokenType.MULTIPLY);
        keywords.put("and", Token.TokenType.AND);
        keywords.put("or", Token.TokenType.OR);
        keywords.put("not", Token.TokenType.NOT);
        keywords.put("xor", Token.TokenType.XOR);
        keywords.put("copy", Token.TokenType.COPY);
        keywords.put("halt", Token.TokenType.HALT);
        keywords.put("branch", Token.TokenType.BRANCH);
        keywords.put("jump", Token.TokenType.JUMP);
        keywords.put("call", Token.TokenType.CALL);
        keywords.put("push", Token.TokenType.PUSH);
        keywords.put("load", Token.TokenType.LOAD);
        keywords.put("return", Token.TokenType.RETURN);
        keywords.put("store", Token.TokenType.STORE);
        keywords.put("peek", Token.TokenType.PEEK);
        keywords.put("pop", Token.TokenType.POP);
        keywords.put("interrupt", Token.TokenType.INTERRUPT);
        keywords.put("equal", Token.TokenType.EQUAL);
        keywords.put("unequal", Token.TokenType.UNEQUAL);
        keywords.put("greater", Token.TokenType.GREATER);
        keywords.put("less", Token.TokenType.LESS);
        keywords.put("greaterOrEqual", Token.TokenType.GREATEROREQUAL);
        keywords.put("lessOrEqual", Token.TokenType.LESSOREQUAL);
        keywords.put("shift", Token.TokenType.SHIFT);
        keywords.put("left", Token.TokenType.LEFT);
        keywords.put("right", Token.TokenType.RIGHT);
        keywords.put("R", Token.TokenType.REGISTER);

        oneCharacter.put("\r", Token.TokenType.SLASHR);
        oneCharacter.put("\n", Token.TokenType.NEWLINE);
    }
}


