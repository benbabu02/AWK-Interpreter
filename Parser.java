package csi404.assignment7;

import java.util.LinkedList;
import java.util.Optional;

public class Parser {
    private TokenManager tokenManager;

    public Parser(LinkedList<Token> inputTokens) {
        this.tokenManager = new TokenManager(inputTokens);
    }

    boolean AcceptSeperators() {
        int seperatorCount = 0;
        while (tokenManager.MoreTokens()) {
            if (tokenManager.MatchAndRemove(Token.TokenType.NEWLINE).isEmpty()) {
                return seperatorCount > 0;
            }
            seperatorCount++;
        }
        return seperatorCount > 0;
    }

    String[] Parse() throws Exception {
        String[] result = new String[1024];
        int count = 0;
        while (count < 1024 && tokenManager.MoreTokens()) {
            result[count] = ParseStatement();
            count++;
        }
        return result;
    }

    String ParseStatement() throws Exception {
        AcceptSeperators();
        if (tokenManager.MatchAndRemove(Token.TokenType.MATH).isPresent()) {
            return ParseMath();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.BRANCH).isPresent()) {
            return ParseBranch();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.HALT).isPresent()) {
            return ParseStatementHalt();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.COPY).isPresent()) {
            return ParseStatementCopy();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.JUMP).isPresent()) {
            return ParseJump();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.CALL).isPresent()) {
            return ParseCall();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.PUSH).isPresent()) {
            return ParsePush();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.POP).isPresent()) {
            return ParsePop();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.LOAD).isPresent()) {
            return ParseLoad();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.STORE).isPresent()) {
            return ParseStore();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.RETURN).isPresent()) {
            return ParseReturn();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.PEEK).isPresent()) {
            return ParsePeek();
        } else if (tokenManager.MatchAndRemove(Token.TokenType.INTERRUPT).isPresent()) {
            return ParseInterrupt();
        }
        return "";
    }

    String insertZeroInFront(String input, boolean flag, int num) {
        String result = "";
        for (int i = input.length(); i < num; i++) {
            result += "0";
        }
        if (flag) {
            return result + input;
        } else {
            return input + result;
        }
    }

    String ParseMath() throws Exception {
        AcceptSeperators();
        // math  MATH ADD twoR | MATH ADD threeR  (( subtract, … are all the same)
        String BinaryMOP = "";
        BinaryMOP = ParseMOP(); // gets MOP binary representation
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
            tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
            tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(2).isEmpty()) { // checks if its 2R
            String[] Binary2R = ParseTwoR();
            return insertZeroInFront(Binary2R[0] + BinaryMOP + Binary2R[1] + "00010", true, 32);
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                   tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                   tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(3).isEmpty()) { // checks if its 3R
            String[] Binary3R = ParseThreeR();
            return insertZeroInFront(Binary3R[0] + Binary3R[1] + BinaryMOP + Binary3R[2] + "00011", true, 32);
        } else {
            throw new Exception("ERROR: Invalid Math Operator in ParseMath");
        }
    }

    String ParseMOP() throws Exception {
        AcceptSeperators();
        if (tokenManager.MatchAndRemove(Token.TokenType.AND).isPresent()) { // and
            return "1000";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.OR).isPresent()) { // or
            return "1001";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.XOR).isPresent()) { // xor
            return "1010";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.NOT).isPresent()) { // not
            return "1011";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.LEFT).isPresent()) { // left
            return "1100";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.RIGHT).isPresent()) { // right
            return "1101";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.ADD).isPresent()) { // add
            return "1110";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.SUBTRACT).isPresent()) { // subtract
            return "1111";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.MULTIPLY).isPresent()) { // multiply
            return "0111";
        } else { // invalid MOP
            throw new Exception("ERROR: Invalid Mathematical Expression in ParseMOP");
        }
    }

    String ParseBranch() throws Exception {
        AcceptSeperators();
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(1).isEmpty()) {
            return ParseJump();
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(1).isEmpty()) {
            return ParseJump();
        } else if (tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(4).isPresent() && tokenManager.Peek(4).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(4).isEmpty()) {
            String BinaryBOP = "";
            BinaryBOP = ParseBOP(); // gets MOP binary representation
            String[] Binary2R = ParseTwoR();
            Optional<Token> number = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (number.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(number.get().getTokenValue()))), true, 13)
                        + Binary2R[0] + BinaryBOP + Binary2R[1] + "00110";
            } else {
                throw new Exception("ERROR: Missing Number in ParseBranch 2R");
            }
        } else if (tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(4).isPresent() && tokenManager.Peek(4).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(4).isEmpty()) { // Branch - 3R (00111)
            String BinaryBOP = "";
            BinaryBOP = ParseBOP(); // gets MOP binary representation
            String[] Binary3R = ParseTwoR();
            Optional<Token> number = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (number.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(number.get().getTokenValue()))), true, 8)
                        + Binary3R[0] + Binary3R[1] + BinaryBOP + "00000" + "00111";
            } else {
                throw new Exception("ERROR: Missing Number in ParseBranch 2R");
            }
        }
        return "";
    }

    String ParseBOP() throws Exception {
        if (tokenManager.MatchAndRemove(Token.TokenType.EQUAL).isPresent()) { // Equals (eq)
            return "0000";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.UNEQUAL).isPresent()) { // Not Equal (neq)
            return "0001";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.LESS).isPresent()) { // Less than (lt)
            return "0010";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.GREATEROREQUAL).isPresent()) { // Greater than or equal (ge)
            return "0011";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.GREATER).isPresent()) { // Greater than (gt)
            return "0100";
        } else if (tokenManager.MatchAndRemove(Token.TokenType.LESSOREQUAL).isPresent()) { // Less than or equal (le)
            return "0101";
        } else { // invalid BOP
            throw new Exception("ERROR: Invalid Boolean Expression in ParseBOP");
        }
    }

    String ParseStatementHalt() throws Exception {
        AcceptSeperators();
        return insertZeroInFront(ParseHalt(), true, 32);
    }

    String ParseStatementCopy() throws Exception {
        AcceptSeperators();
        String[] copy = ParseCopy();
        return copy[1] + "0000" + copy[0] + "00001";
    }

    String ParseJump() throws Exception {
        AcceptSeperators();
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(1).isEmpty()) {
            Optional<Token> number = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (number.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(number.get().getTokenValue()))) + "00100", true, 32);
            } else {
                throw new Exception("ERROR: Missing Number in ParseJump");
            }
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(1).isEmpty()) {
            Optional<Token> number = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (number.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(number.get().getTokenValue()))) + "00100", true, 32);
            } else {
                throw new Exception("ERROR: Missing Number in ParseJump");
            }
        } else {
            throw new Exception("ERROR: Invalid Expression in ParseJump");
        }
    }

    String ParseCall() throws Exception {
        AcceptSeperators();
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(1).isEmpty()) {
            Optional<Token> number = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (number.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(number.get().getTokenValue()))) + "01000", true, 32);
            } else {
                throw new Exception("ERROR: Missing Number in ParseCall");
            }
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(2).isEmpty()) {
            String[] BinaryDestOnly = ParseDestOnly();
            return insertZeroInFront(BinaryDestOnly[1]+ "0000" + BinaryDestOnly[0] + "01001", true, 32);
        } else if (tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(4).isPresent() && tokenManager.Peek(4).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(4).isEmpty()) {
            String BinaryBOP = "";
            BinaryBOP = ParseBOP(); // gets MOP binary representation
            String[] Binary2R = ParseTwoR();
            Optional<Token> number = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (number.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(number.get().getTokenValue()))), true, 13)
                        + Binary2R[0] + BinaryBOP + Binary2R[1] + "01010";
            } else {
                throw new Exception("ERROR: Missing Number in ParseCall 2R");
            }
        } else if (tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(4).isPresent() && tokenManager.Peek(4).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(5).isPresent() && tokenManager.Peek(5).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(5).isEmpty()) {
            String BinaryBOP = "";
            BinaryBOP = ParseBOP(); // gets MOP binary representation
            String[] Binary3R = ParseThreeR();
            Optional<Token> number = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (number.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(number.get().getTokenValue()))), true, 8)
                        + Binary3R[0] + Binary3R[1] + BinaryBOP + Binary3R[2] + "01011";
            } else {
                throw new Exception("ERROR: Missing Number in ParseBranch 2R");
            }
        } else {
            throw new Exception("ERROR: Invalid Expression in ParseCall");
        }
    }

    String ParsePush() throws Exception {
        AcceptSeperators();
        String BinaryMOP = "";
        BinaryMOP = ParseMOP(); // gets MOP binary representation
        // Push - Dest Only (01101)
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(2).isEmpty()) {
            Optional<Token> register = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
            if (register.isPresent()) {
                Optional<Token> number = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                if (number.isPresent()) {
                    return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(number.get().getTokenValue())))
                            + BinaryMOP +convertNumToFiveBitBinary(Integer.parseInt(register.get().getTokenValue()))
                            + "01101", true, 32);
                } else {
                    throw new Exception("ERROR: Invalid Number in ParsePush");
                }
            } else {
                throw new Exception("Error: Invalid Register in ParsePush");
            }
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(2).isEmpty()) { // Push - 2R (01110)
            String[] Binary2R = ParseTwoR();
            return insertZeroInFront(Binary2R[0] + BinaryMOP + Binary2R[1] + "01110", true, 32);
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(2).isEmpty()) { // Push - 3R (01111)
            String[] Binary3R = ParseTwoR();
            return insertZeroInFront(Binary3R[0] + BinaryMOP + Binary3R[1] + "01110", true, 32);
        } else {
            throw new Exception("ERROR: Invalid Expression in ParsePush");
        }
    }

    String ParsePop() throws Exception {
        AcceptSeperators();
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(1).isEmpty()) { // checks if only one register exists
            Optional<Token> register = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
            if (register.isPresent()) {
                return insertZeroInFront("0000" + Integer.toBinaryString((Integer.parseInt(register.get().getTokenValue()))) + "11001", true, 32);
            }
        } throw new Exception("ERROR: Invalid Expression in ParsePop");
    }

    String ParseLoad() throws Exception {
        AcceptSeperators();
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(1).isEmpty()) {
            // noR (10000)
            return ParseReturn().substring(0, 27) + "10000";
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(2).isEmpty()) {
            // Dest Only (10001)
            Optional<Token> register = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
            if (register.isPresent()) {
                Optional<Token> number = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                if (number.isPresent()) {
                    return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(number.get().getTokenValue())))
                            + "0000" +convertNumToFiveBitBinary(Integer.parseInt(register.get().getTokenValue()))
                            + "10001", true, 32);
                } else {
                    throw new Exception("ERROR: Invalid Expression in ParseLoad <Missing Number>");
                }
            } else {
                throw new Exception("ERROR: Invalid Expression in ParseLoad <Missing Register>");
            }
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(3).isEmpty()) {
            // 2R (10010)
            String[] Binary2R = ParseTwoR();
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (token.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(token.get().getTokenValue()))), true, 13)
                        + Binary2R[0] + "0000" + Binary2R[1] + "10010";
            } else {
                throw new Exception("ERROR: Invalid Expression in ParseLoad <Missing Number>");
            }
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(3).isEmpty()) {
            // 3R (10011)
            String[] Binary3R = ParseThreeR();
            return insertZeroInFront(Binary3R[0] + Binary3R[1] + "0000" + Binary3R[2] + "10011", true, 32);
        } else {
            throw new Exception("ERROR: Invalid Expression in ParseLoad");
        }
    }

    String ParseStore() throws Exception {
        AcceptSeperators();
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(2).isEmpty()) { // checks if its Dest Only (10101)
            // Mem[Rd]  imm
            String[] BinaryDestOnly = ParseDestOnly();
            return BinaryDestOnly[1] + "0000" + BinaryDestOnly[0] + "10101";
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(3).isEmpty()) { // checks if its 2R (10110)
            // mem[Rd + imm]  Rs
            String[] Binary2R = ParseTwoR();
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (token.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(token.get().getTokenValue()))), true, 13)
                        + Binary2R[0] + "0000" + Binary2R[1] + "10110";
            } else {
                throw new Exception("ERROR: Invalid Expression in ParseStore");
            }
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(3).isEmpty()) { // checks if its 3R (10111)
            // Mem[Rd + Rs1]  Rs2
            String[] Binary3R = ParseThreeR();
            return insertZeroInFront(Binary3R[0] + Binary3R[1] + "0000" + Binary3R[2] + "10111", true, 32);
        } else {
            throw new Exception("ERROR: Invalid Expression in ParseStore");
        }
    }

    String ParseReturn() throws Exception {
        AcceptSeperators();
        return ParsePop();
    }

    String ParsePeek() throws Exception {
        AcceptSeperators();
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(3).isEmpty()) { // checks if its 2R (11010)
            String[] Binary2R = ParseTwoR();
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (token.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(token.get().getTokenValue())))
                        + Binary2R[0] + "0000" + Binary2R[1] + "11010", true, 32);
            } else {
                throw new Exception("ERROR: Invalid Expression in ParsePeek");
            }
        } else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(2).isPresent() && tokenManager.Peek(2).get().getTokenType() == Token.TokenType.REGISTER &&
                tokenManager.Peek(3).isPresent() && tokenManager.Peek(3).get().getTokenType() == Token.TokenType.NUMBER &&
                tokenManager.Peek(4).isPresent() && tokenManager.Peek(4).get().getTokenType() == Token.TokenType.NEWLINE ||
                tokenManager.Peek(4).isEmpty()) { // checks if its 3R (11011)
            String[] Binary3R = ParseThreeR();
            Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (token.isPresent()) {
                return insertZeroInFront(Integer.toBinaryString((Integer.parseInt(token.get().getTokenValue())))
                        + Binary3R[0] + Binary3R[1] + "0000" + Binary3R[2] + "11011", true, 32);
            } else {
                throw new Exception("ERROR: Invalid Expression in ParsePeek");
            }
        } else {
            throw new Exception("ERROR: Invalid Expression in ParsePeek");
        }
    }

    String ParseInterrupt() throws Exception {
        AcceptSeperators();
        throw new Exception("ParseInterrupt: Interrupt is not a valid token");
    }

    String[] ParseCopy() {
        AcceptSeperators();
        return ParseDestOnly();
    }

    String ParseHalt() throws Exception {
        AcceptSeperators();
        return "00000";
    }

    // parses two registers
    // returns [R1, R2]
    String[] ParseTwoR() throws Exception {
        AcceptSeperators();
        Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
        String[] result = new String[2];
        if (token.isPresent()) {
            result[0] = convertNumToFiveBitBinary(Integer.parseInt(token.get().getTokenValue()));
            token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
            if (token.isPresent()) {
                result[1] = convertNumToFiveBitBinary(Integer.parseInt(token.get().getTokenValue()));
            }
        } else {
            throw new Exception("ERROR: Invalid Statement in ParseTwoR");
        }
        return result;
    }

    // converts an integer to its 5-bit binary representation
    String convertNumToFiveBitBinary(int num) {
        String resultString = "";
        String binaryNum = Integer.toBinaryString((num));
        for (int i = binaryNum.length(); i < 5; i++) {
            resultString += "0";
        }
        return resultString + binaryNum;
    }

    String[] ParseThreeR() throws Exception {
        AcceptSeperators();
        Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
        String[] result = new String[3];
        if (token.isPresent()) {
            result[0] = convertNumToFiveBitBinary(Integer.parseInt(token.get().getTokenValue()));
            token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
            if (token.isPresent()) {
                result[1] = convertNumToFiveBitBinary(Integer.parseInt(token.get().getTokenValue()));
                token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (token.isPresent()) {
                    result[2] = convertNumToFiveBitBinary(Integer.parseInt(token.get().getTokenValue()));
                }
            }
        } else {
            throw new Exception("ERROR: Invalid Statement in ParseThreeR");
        }
        return result;
    }

    // returns [register, immediate]
    String[] ParseDestOnly() {
        AcceptSeperators();
        Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
        String[] result = new String[2];
        if (token.isPresent()) {
            result[0] = convertNumToFiveBitBinary(Integer.parseInt(token.get().getTokenValue()));
            token = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (token.isPresent()) {
                result[1] = insertZeroInFront(Integer.toBinaryString((Integer.parseInt(token.get().getTokenValue()))), true, 18);
            }
        }
        return result;
    }

    String ParseNoR() throws Exception {
        AcceptSeperators();
        Optional<Token> token = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
        if (token.isPresent()) {
            String withZerosInFront = insertZeroInFront(Integer.toBinaryString((Integer.parseInt(token.get().getTokenValue()))), true, 27);
            return insertZeroInFront(withZerosInFront, false, 32);
        } else {
            throw new Exception("ParseNoR: Invalid expression");
        }
    }
}
