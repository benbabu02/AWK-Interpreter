package csi404.assignment7;

public class Token {
    enum TokenType { // 35 tokens
        MATH, ADD, SUBTRACT, MULTIPLY, AND, OR, NOT, XOR, COPY, HALT, BRANCH, JUMP, CALL, PUSH, LOAD, RETURN, STORE,
        PEEK, POP, INTERRUPT, EQUAL, UNEQUAL, GREATER, LESS, GREATEROREQUAL, LESSOREQUAL, SHIFT, LEFT, RIGHT, NUMBER,
        REGISTER, OPENROUNDBRACKET, CLOSEROUNDBRACKET, NEWLINE, SLASHR, TWO_R, THREE_R
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
