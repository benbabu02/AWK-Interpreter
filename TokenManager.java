package csi404.assignment7;

import java.util.LinkedList;
import java.util.Optional;

public class TokenManager {
    private LinkedList<Token> tokens;

    public TokenManager(LinkedList<Token> inputTokens) { this.tokens = inputTokens; }

    Optional<Token> Peek(int j) {
        if (MoreTokens() && j < tokens.size()) { return Optional.ofNullable(tokens.get(j)); }
        return Optional.empty();
    }

    LinkedList<Token> getTokens() {
        return tokens;
    }

    boolean MoreTokens() { return !tokens.isEmpty(); }

    Optional<Token> MatchAndRemove(Token.TokenType t) {
        if (MoreTokens() && tokens.element().getTokenType() == t) { return Optional.ofNullable(tokens.pollFirst()); }
        return Optional.empty();
    }
}
