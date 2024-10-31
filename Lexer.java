import java.util.HashMap;
import java.util.LinkedList;

/*
 * A Lexer class which implements a Lexer method
 * which goes through the input string and creates
 * tokens depending on if they match the requirement
 * for each tokenType.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class Lexer {
    private StringHandler stringHandler;
    private int lineNumber;
    private int charPosition;
    private HashMap<String, Token.TokenType> keywords;
    private HashMap<String, Token.TokenType> oneCharacter;
    private HashMap<String, Token.TokenType> twoCharacter;

    public Lexer(String str) {
        this.stringHandler = new StringHandler(str);
        this.lineNumber = 0;
        this.charPosition = 0;
        keywords = new HashMap<>();
        oneCharacter = new HashMap<>();
        twoCharacter = new HashMap<>();
        Populate_HashMaps();
    }

    LinkedList<Token> Lex() throws Exception {
        LinkedList<Token> tokens = new LinkedList<Token>();

        while (!stringHandler.IsDone()) {
            Token symbolToken = ProcessSymbol();
            if (symbolToken != null) {
                tokens.add(symbolToken);
            } else if (SkipCharacterCheck()) {
                stringHandler.Swallow(1);
                charPosition++;
            } else if (Character.isLetter(stringHandler.Peek(0))) {
                tokens.add(ProcessWord());
            } else if (Character.isDigit(stringHandler.Peek(0)) || stringHandler.Peek(0) == '.') {
                tokens.add(ProcessNumber());
            } else if (stringHandler.Peek(0) == '#') {
                char findNewLine = stringHandler.GetChar();
                while (!stringHandler.IsDone() && findNewLine != '\n') { findNewLine = stringHandler.GetChar(); }
                tokens.add(new Token(Token.TokenType.SEPERATOR, lineNumber+1, charPosition+1));
                lineNumber++;
                charPosition = 0;
            } else if (stringHandler.Peek(0) == '"') {
                tokens.add(HandleStringLiteral());
            } else if (stringHandler.Peek(0) == '`')  {
                tokens.add(HandlePattern());
            } else { throw new Exception("ERROR: Character not recognized: " + stringHandler.Peek(0)); }
        }
        return tokens;
    }

    private Token ProcessWord() {
        String str = "";
        while (!stringHandler.IsDone() && !SkipCharacterCheck()) {
            if (Character.isLetter(stringHandler.Peek(0)) || Character.isDigit(stringHandler.Peek(0)) || stringHandler.Peek(0) == '_') {
                str += stringHandler.GetChar();
                charPosition++;
            } else { break; }
        }
        if (keywords.containsKey(str)) { return new Token(keywords.get(str), lineNumber+1, charPosition+1); }
        return new Token(Token.TokenType.WORD, lineNumber+1, charPosition+1, str);
    }

    private Token ProcessNumber() throws Exception {
        String str = "";
        boolean decimalCheck = false;
        while (!stringHandler.IsDone() && !SkipCharacterCheck()) {
            if (!decimalCheck && Character.isDigit(stringHandler.Peek(0)) || stringHandler.Peek(0) == '.') {
                if (stringHandler.Peek(0) == '.') { decimalCheck = true; }
                str += stringHandler.GetChar();
                charPosition++;
            } else {
                if (decimalCheck && stringHandler.Peek(0) == '.') {
                    throw new Exception("ERROR: Decimal already exists in the number.");
                } else if (Character.isDigit(stringHandler.Peek(0))) {
                    str += stringHandler.GetChar();
                    charPosition++;
                } else { break; }
            }
        }
        return new Token(Token.TokenType.NUMBER, lineNumber+1, charPosition+1, str);
    }

    private Token HandleStringLiteral() {
        String str = "";
        boolean gotStringLiteralChar = false;
        while (!stringHandler.IsDone()) {
            if (stringHandler.Peek(0) == '\\' && stringHandler.Peek(1) == '"') {
                stringHandler.Swallow(1);
                str += stringHandler.GetChar();
                charPosition++;
            } else if (stringHandler.Peek(0) == '"') {
                stringHandler.Swallow(1);
                charPosition++;
                if (!gotStringLiteralChar) {
                    gotStringLiteralChar = true;
                } else {
                    break;
                }
            } else {
                str += stringHandler.GetChar();
                charPosition++;
            }
        }
        return new Token(Token.TokenType.STRINGLITERAL, lineNumber+1, charPosition+1, str);
    }

    private Token HandlePattern() {
        String str = "";
        boolean gotPatternChar = false;
        while (!stringHandler.IsDone()) {
            if (stringHandler.Peek(0) == '\\' && stringHandler.Peek(1) == '`') {
                stringHandler.Swallow(1);
                str += stringHandler.GetChar();
                charPosition++;
            } else if (stringHandler.Peek(0) == '`') {
                stringHandler.Swallow(1);
                charPosition++;
                if (!gotPatternChar) {
                    gotPatternChar = true;
                } else {
                    break;
                }
            } else {
                str += stringHandler.GetChar();
                charPosition++;
            }
        }
        return new Token(Token.TokenType.REGEX, lineNumber+1, charPosition+1, str);
    }

    private Token ProcessSymbol() {
        String tempStr = "";
        Token returnToken = null;
        if (!stringHandler.IsDone() && !stringHandler.IsPeekDone(1) && twoCharacter.containsKey(stringHandler.PeekString(2))) {
            tempStr += stringHandler.GetChar();
            tempStr += stringHandler.GetChar();
            returnToken = new Token(twoCharacter.get(tempStr), lineNumber, charPosition);
            charPosition += 2;
        }

        if (!stringHandler.IsDone() && returnToken == null && oneCharacter.containsKey(stringHandler.PeekString(1))) {
            char tempChar = stringHandler.GetChar();
            returnToken = new Token(oneCharacter.get(String.valueOf(tempChar)), lineNumber, charPosition);
            if (tempChar == '\n') { lineNumber++; }
            charPosition++;
        }
        return returnToken;
    }

    private boolean SkipCharacterCheck() { return Character.isWhitespace(stringHandler.Peek(0)) || stringHandler.Peek(0) == '\t' || stringHandler.Peek(0) == '\r'; }

    private void Populate_HashMaps() {
        keywords.put("while", Token.TokenType.WHILE);
        keywords.put("if", Token.TokenType.IF);
        keywords.put("do", Token.TokenType.DO);
        keywords.put("for", Token.TokenType.FOR);
        keywords.put("break", Token.TokenType.BREAK);
        keywords.put("continue", Token.TokenType.CONTINUE);
        keywords.put("else", Token.TokenType.ELSE);
        keywords.put("return", Token.TokenType.RETURN);
        keywords.put("BEGIN", Token.TokenType.BEGIN);
        keywords.put("END", Token.TokenType.END);
        keywords.put("print", Token.TokenType.PRINT);
        keywords.put("printf", Token.TokenType.PRINTF);
        keywords.put("next", Token.TokenType.NEXT);
        keywords.put("in", Token.TokenType.IN);
        keywords.put("delete", Token.TokenType.DELETE);
        keywords.put("getline", Token.TokenType.GETLINE);
        keywords.put("exit", Token.TokenType.EXIT);
        keywords.put("nextfile", Token.TokenType.NEXTFILE);
        keywords.put("function", Token.TokenType.FUNCTION);

        twoCharacter.put(">=", Token.TokenType.GREATERTHANEQUAL);
        twoCharacter.put("++", Token.TokenType.INCREMENTONE);
        twoCharacter.put("--", Token.TokenType.DECREMENTONE);
        twoCharacter.put("<=", Token.TokenType.LESSTHANEQUAL);
        twoCharacter.put("==", Token.TokenType.EQUALTO);
        twoCharacter.put("!=", Token.TokenType.NOTEQUALTO);
        twoCharacter.put("^=", Token.TokenType.POWEREQUAL);
        twoCharacter.put("%=", Token.TokenType.MODEQUAL);
        twoCharacter.put("*=", Token.TokenType.MULTIPLYEQUAL);
        twoCharacter.put("/=", Token.TokenType.DIVIDEEQUAL);
        twoCharacter.put("+=", Token.TokenType.PLUSEQUAL);
        twoCharacter.put("-=", Token.TokenType.MINUSEQUAL);
        twoCharacter.put("!~", Token.TokenType.NOTMATCH);
        twoCharacter.put("&&", Token.TokenType.AND);
        twoCharacter.put(">>", Token.TokenType.APPEND);
        twoCharacter.put("||", Token.TokenType.OR);

        oneCharacter.put("{", Token.TokenType.OPENCURLYBRACKET);
        oneCharacter.put("}", Token.TokenType.CLOSECURLYBRACKET);
        oneCharacter.put("[", Token.TokenType.OPENSQUAREBRACKET);
        oneCharacter.put("]", Token.TokenType.CLOSESQUAREBRACKET);
        oneCharacter.put("(", Token.TokenType.OPENROUNDBRACKET);
        oneCharacter.put(")", Token.TokenType.CLOSEROUNDBRACKET);
        oneCharacter.put("$", Token.TokenType.DOLLAR);
        oneCharacter.put("~", Token.TokenType.MATCH);
        oneCharacter.put("=", Token.TokenType.ASSIGNEQUAL);
        oneCharacter.put("<", Token.TokenType.LESSTHAN);
        oneCharacter.put(">", Token.TokenType.GREATERTHAN);
        oneCharacter.put("!", Token.TokenType.NOT);
        oneCharacter.put("+", Token.TokenType.PLUS);
        oneCharacter.put("^", Token.TokenType.POWER);
        oneCharacter.put("-", Token.TokenType.MINUS);
        oneCharacter.put("?", Token.TokenType.QUESTIONMARK);
        oneCharacter.put(":", Token.TokenType.COLON);
        oneCharacter.put("*", Token.TokenType.MULTIPLY);
        oneCharacter.put("/", Token.TokenType.DIVIDE);
        oneCharacter.put("%", Token.TokenType.MOD);
        oneCharacter.put(";", Token.TokenType.SEPERATOR);
        oneCharacter.put("\n", Token.TokenType.SEPERATOR);
        oneCharacter.put("|", Token.TokenType.PIPE);
        oneCharacter.put(",", Token.TokenType.COMMA);
    }
}
