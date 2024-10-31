/*
 * The Token class which implements a Token.
 *
 * @author Benjamin Babu (bbabu@albany.edu)
 */
public class Token {
    enum TokenType {
        WORD, NUMBER, SEPERATOR, WHILE, IF, DO, FOR, BREAK, CONTINUE, ELSE, RETURN, BEGIN, END, PRINT, PRINTF, NEXT, IN,
        DELETE, GETLINE, EXIT, NEXTFILE, FUNCTION, STRINGLITERAL, REGEX, GREATERTHANEQUAL, INCREMENTONE, DECREMENTONE,
        LESSTHANEQUAL, EQUALTO, NOTEQUALTO, POWEREQUAL, MODEQUAL, MULTIPLYEQUAL, DIVIDEEQUAL, PLUSEQUAL, MINUSEQUAL,
        NOTMATCH, AND, APPEND, OR, OPENCURLYBRACKET, CLOSECURLYBRACKET, OPENSQUAREBRACKET, CLOSESQUAREBRACKET,
        OPENROUNDBRACKET, CLOSEROUNDBRACKET, DOLLAR, MATCH, ASSIGNEQUAL, LESSTHAN, GREATERTHAN, NOT, PLUS, POWER, MINUS,
        QUESTIONMARK, COLON, MULTIPLY, DIVIDE, MOD, PIPE, COMMA
    }

    public TokenType tokenType;
    private int lineNumber;
    private int charPosition;
    private String value;

    public Token(TokenType inputTokenType, int lineNumber, int charPosition) {
        this.tokenType = inputTokenType;
        this.lineNumber = lineNumber;
        this.charPosition = charPosition;
    }

    public Token(TokenType inputTokenType, int lineNumber, int charPosition, String inputValue) {
        this.tokenType = inputTokenType;
        this.lineNumber = lineNumber;
        this.charPosition = charPosition;
        this.value = inputValue;
    }

    /*
     * Accessor method to get token type for a token.
     *
     * @return TokenType
     */
    public TokenType getTokenType() { return tokenType; }

    public String getTokenValue() { return value; }

    /*
     * This method is used to modify the format of how
     * the token is printed.
     *
     * @return String
     */
    public String toString() {
        if (value == null) { return tokenType + " "; }
        return tokenType + "(" + value + ") ";
    }
}
