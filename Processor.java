package csi404.assignment7;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Processor {
    Word PC = new Word(); // program counter
    Word SP = new Word(); // stack pointer
    Bit halted = new Bit(false); // boolean for this is halted

    Word currentInstruction = new Word(); // the current instruction

    Map<String, Word> instructions = new HashMap<String, Word>();

    Word[] registers = new Word[32];

    int currentClockCycle = 0;

    Processor() {
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Word();
        }
        InstructionCache cache = new InstructionCache();
        currentClockCycle += 350;
        L2Cache Cache = new L2Cache();
        currentClockCycle += 20;
        currentClockCycle += 350;
        instructions.put("result" , new Word());
        SP.set(1024);
    }

    public void run() throws Exception { // runs the processor
        while (!halted.getValue()) {
            fetch();
            decode();
            execute();
            store();
        }
    }

    public int getCurrentClockCycle() { return currentClockCycle; }

    public Word getPC() {
        return PC;
    }

    public Word getSP() {
        return SP;
    }

    String convertFromBoolean(String input) {
        String resultStr = "";
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == 'f') {
                resultStr += "0";
            } else {
                resultStr += "1";
            }
        }
        return resultStr;
    }

    Bit[] createMask(int start, int end) {
        Bit[] resultMask = new Bit[32];
        for (int i = 0; i < 32; i++) {
            if (i >= start && i <= end) {
                resultMask[i] = new Bit(true);
            } else {
                resultMask[i] = new Bit(false);
            }
        }
        return resultMask;
    }

    public void decode() { // gets the data for the instruction
        // look at the last 2 bits from currentInstruction
        // follow rules in introduction to determine which registers and immediate values to get, using shift and masking (R0 is always 0)
        String twoBits = "";
        twoBits += currentInstruction.getBit(30).toString() + currentInstruction.getBit(31).toString();

        Word immediate;
        Word function;
        Word rd;
        Word opcode = new Word(createMask(27, 31));
        opcode = opcode.and(currentInstruction);
        instructions.put("opcode", opcode);

        switch(convertFromBoolean(twoBits)) {
            case "11": // 3 Register (3R)
                // Immediate (8) -> Rs1 (5) -> Rs2 (5) -> Function (4) -> Rd (5) -> Opcode (5)

                // getting immediate and shifting right
                immediate = new Word(createMask(0, 7));
                immediate = immediate.and(currentInstruction);
                immediate = immediate.rightShift(24);
                instructions.put("immediate", immediate);

                // getting rs1 and shifting right
                Word rs1 = new Word(createMask(8, 12));
                rs1 = rs1.and(currentInstruction);
                rs1 = rs1.rightShift(19);
                instructions.put("rs1", rs1);

                // getting rs2 and shifting right
                Word rs2 = new Word(createMask(13, 17));
                rs2 = rs2.and(currentInstruction);
                rs2 = rs2.rightShift(14);
                instructions.put("rs2", rs2);

                // getting function and shifting right
                function = new Word(createMask(18, 21));
                function = function.and(currentInstruction);
                function = function.rightShift(10);
                instructions.put("function", function);

                // getting rd and shifting right
                rd = new Word(createMask(22, 26));
                rd = rd.and(currentInstruction);
                rd = rd.rightShift(5);
                instructions.put("rd", rd);

                break;

            case "10": // 2 Register (2R)
                // Immediate (13) -> Rs (5) -> Function (4) -> Rd (5) -> Opcode (5)

                // getting immediate and shifting right
                immediate = new Word(createMask(0, 12));
                immediate = immediate.and(currentInstruction);
                immediate = immediate.rightShift(19);
                instructions.put("immediate", immediate);

                // getting rs and shifting right
                Word rs = new Word(createMask(13, 17));
                rs = rs.and(currentInstruction);
                rs = rs.rightShift(14);
                instructions.put("rs", rs);

                // getting function and shifting right
                function = new Word(createMask(18, 21));
                function = function.and(currentInstruction);
                function = function.rightShift(10);
                instructions.put("function", function);

                // getting rd and shifting right
                rd = new Word(createMask(22, 26));
                rd = rd.and(currentInstruction);
                rd = rd.rightShift(5);
                instructions.put("rd", rd);

                break;

            case "01": // Dest Only (1R)
                // Immediate (18) -> Function (4) -> Rd (5) -> Opcode (5)

                // getting immediate and shifting right
                immediate = new Word(createMask(0, 17));
                immediate = immediate.and(currentInstruction);
                immediate = immediate.rightShift(14);
                instructions.put("immediate", immediate);

                // getting function and shifting right
                function = new Word(createMask(18, 21));
                function = function.and(currentInstruction);
                function = function.rightShift(10);
                instructions.put("function", function);

                // getting rd and shifting right
                rd = new Word(createMask(22, 26));
                rd = rd.and(currentInstruction);
                rd = rd.rightShift(5);
                instructions.put("rd", rd);

                break;

            case "00": // no register (0R)
                // Immediate (27) -> Opcode (5)

                // getting immediate and shifting right
                immediate = new Word(createMask(0, 26));
                immediate = immediate.and(currentInstruction);
                immediate = immediate.rightShift(5);
                instructions.put("immediate", immediate);

                break;
        }

        // when you get the values from the register, store them in Words (without using set, getSigned, getUnsigned)
    }

    public Boolean BOPExecution(Word op1, Word op2, Word function) throws Exception {
        // setting up a Bit array to use in alu.doOperation for subtraction
        Bit[] subtractionFunctionBits = new Bit[4];
        for (int i = 0; i < 4; i ++) {
            subtractionFunctionBits[i] = new Bit();
            subtractionFunctionBits[i].set(true);
        }

        // subtract the two ops
        ALU alu = new ALU(op1, op2);
        alu.doOperation(subtractionFunctionBits);
        currentClockCycle += 10;

        if (!function.getBit(28).getValue()) { // bit[0] == 0
            if (!function.getBit(29).getValue()) { // bit[1] == 0
                if (!function.getBit(30).getValue()) { // bit[2] == 0
                    if (!function.getBit(31).getValue()) { // bit[3] == 0
                        // 0000 - equals
                        if (alu.getResult().checkIfZero()) { // checks if zero, if it is its equal
                            return true;
                        } else { // not equal
                            return false;
                        }
                    } else { // bit[3] == 1
                        // 0001 - not equal
                        currentClockCycle += 300;
                        if (alu.getResult().checkIfZero()) { // checks if zero, if it is its equal
                            return false;
                        } else { // negative result
                            return true;
                        }
                    }
                } else { //bit[2] == 1
                    if (!function.getBit(31).getValue()) { // bit[3] == 0
                        // 0010 - less than
                        if (alu.getResult().getBit(0).getValue()) { // value is true which means it is a negative number => op1 < op2
                            return true;
                        } else { // value is false which means it is a positive number => op1 >= op2
                            return false;
                        }
                    } else { // bit[3] == 1
                        currentClockCycle += 300;
                        if (!alu.getResult().getBit(0).getValue() || alu.getResult().checkIfZero()) { // value is zero or positive => op1 >= op2
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            } else { // bit[1] == 1
                if (!function.getBit(30).getValue()) { // bit[2] == 0
                    if (!function.getBit(31).getValue()) { // bit[3] == 0
                        // 0100 - greater than
                        // 5 > 4
                        if (!alu.getResult().getBit(0).getValue() && !alu.getResult().checkIfZero()) { // value is positive => op1 > op2
                            return true;
                        } else { // value is negative => op1 <= op2
                            return false;
                        }
                    } else { // bit[3] == 1
                        // 0101 - less than or equal
                        currentClockCycle += 300;
                        if (alu.getResult().getBit(0).getValue() || alu.getResult().checkIfZero()) { // value is zero or negative => op1 <= op2
                            return true;
                        } else { // value is positive => op 1 > op2
                            return false;
                        }
                    }
                }
            }
        } else {
            throw new Exception("inside BOP exec wrong number");
        }
        return false;
    }

    public void execute() throws Exception { // executes the instruction
        if (instructions.get("opcode").checkIfZero()) { // opcode = 00000
            halted.set(true);
        } else {
            currentClockCycle += 150;
            // gets the first three bits from opcode and turns it into a string
            String firstThreeOpCode = "";
            firstThreeOpCode += instructions.get("opcode").getBit(27).toString() + instructions.get("opcode").getBit(28).toString() + instructions.get("opcode").getBit(29).toString();
            // gets the last two bits from opcode and turns into a string
            String twoBits = "";
            twoBits += instructions.get("opcode").getBit(30).toString() + instructions.get("opcode").getBit(31).toString();
            // checks which case it is in the instruction definition matrix - this one is Math(000)
            if (convertFromBoolean(firstThreeOpCode).equalsIgnoreCase("000")) { // opcode - 000 - math
                // checks which case it is in the instruction definition matrix
                if (convertFromBoolean(twoBits).equalsIgnoreCase("00")) {  // last two bits are 00 00000
                    halted.set(true);
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("01")) {  // last two bits are 01 00001
                    Word result = new Word();
                    result.copy(instructions.get("immediate"));
                    instructions.put("result", result);
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("10")) {  // last two bits are 10 00010
                    ALU alu = new ALU(registers[getIndex(instructions.get("rd").getBitArray())], registers[getIndex(instructions.get("rs").getBitArray())]);
                    alu.doOperation(getFunctionBits(instructions.get("function").getBitArray()));
                    if (instructions.get("function").getBinaryRepresentation().equals("00000000000000000000000000000111")) {
                        currentClockCycle += 10;
                    } else {
                        currentClockCycle += 2;
                    }
                    Word result = new Word();
                    result.copy(alu.getResult());
                    currentClockCycle += 300;
                    instructions.put("result", result);
                } else { // last two bits are 11 00011
                    ALU alu = new ALU(registers[getIndex(instructions.get("rs1").getBitArray())], registers[getIndex(instructions.get("rs2").getBitArray())]);
                    alu.doOperation(getFunctionBits(instructions.get("function").getBitArray()));
                    if (instructions.get("function").getBinaryRepresentation().equals("00000000000000000000000000000111")) {
                        currentClockCycle += 10;
                    } else {
                        currentClockCycle += 2;
                    }
                    Word result = new Word();
                    result.copy(alu.getResult());
                    currentClockCycle += 300;
                    instructions.put("result", result);
                }
            } else if (convertFromBoolean(firstThreeOpCode).equalsIgnoreCase("001")) { // Branch (001) - Instruction Definition Matrix
                if (convertFromBoolean(twoBits).equalsIgnoreCase("00")) {   // 00100
                    // pc <- imm
                    PC.copy(instructions.get("immediate"));
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("01")) {    // 00101
                    // pc <- pc + imm
                    ALU alu = new ALU(PC, instructions.get("immediate"));
                    PC.copy(alu.add2(PC, instructions.get("immediate")));
                    currentClockCycle += 2;
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("10")) {    // 00110
                    // pc <- Rs BOP Rd? pc + imm : pc
                    if (BOPExecution(registers[getIndex(instructions.get("rs").getBitArray())], registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("function"))) { // true so pc = pc + imm
                        ALU alu = new ALU(PC, instructions.get("immediate"));
                        PC.copy(alu.add2(PC, instructions.get("immediate")));
                        currentClockCycle += 2;
                    }
                } else {    // 00111
                    // pc <- Rs1 BOP Rs2 ? pc + imm : pc
                    if (BOPExecution(registers[getIndex(instructions.get("rs1").getBitArray())], registers[getIndex(instructions.get("rs2").getBitArray())], instructions.get("function"))) { // true so pc = pc + imm
                        ALU alu = new ALU(PC, instructions.get("immediate"));
                        PC.copy(alu.add2(PC, instructions.get("immediate")));
                        currentClockCycle += 2;
                    }
                }
            } else if (convertFromBoolean(firstThreeOpCode).equalsIgnoreCase("010")) { // Call (010) - Instruction Definition Matrix
                if (convertFromBoolean(twoBits).equalsIgnoreCase("00")) {   // 01000
                    SP.copy(SP.decrement());
                    MainMemory.write(SP, PC);
                    PC.copy(instructions.get("immediate"));
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("01")) {    // 01001
                    SP.copy(SP.decrement());
                    MainMemory.write(SP, PC);
                    ALU alu = new ALU(registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("immediate"));
                    PC.copy(alu.add2(registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("immediate")));
                    currentClockCycle += 2;
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("10")) {    // 01010
                    if (BOPExecution(registers[getIndex(instructions.get("rs").getBitArray())], registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("function"))) {
                        SP.copy(SP.decrement());
                        MainMemory.write(SP, PC);
                        ALU alu = new ALU(registers[getIndex(instructions.get("rs").getBitArray())], registers[getIndex(instructions.get("rd").getBitArray())]);
                        PC.copy(alu.add2(PC, instructions.get("immediate")));
                        currentClockCycle += 2;
                    }
                } else {    // 01011
                    if (BOPExecution(registers[getIndex(instructions.get("rs1").getBitArray())], registers[getIndex(instructions.get("rs2").getBitArray())], instructions.get("function"))) {
                        SP.copy(SP.decrement());
                        MainMemory.write(SP, PC);
                        ALU alu = new ALU(registers[getIndex(instructions.get("rs1").getBitArray())], registers[getIndex(instructions.get("rs2").getBitArray())]);
                        PC.copy(alu.add2(registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("immediate")));
                        currentClockCycle += 2;
                    }
                }
            } else if (convertFromBoolean(firstThreeOpCode).equalsIgnoreCase("011")) { // Push (011) - Instruction Definition Matrix
                if (convertFromBoolean(twoBits).equalsIgnoreCase("00")) {   // 01100
                    // unused
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("01")) {    // 01101
                    // mem[--sp] <- rd MOP imm
                    ALU alu = new ALU(registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("immediate"));
                    alu.doOperation(getFunctionBits(instructions.get("function").getBitArray()));
                    if (instructions.get("function").getBinaryRepresentation().equals("00000000000000000000000000000111")) {
                        currentClockCycle += 10;
                    } else {
                        currentClockCycle += 2;
                    }
                    SP.copy(SP.decrement());
                    MainMemory.write(SP, alu.getResult());
                    currentClockCycle += 300;
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("10")) {    // 01110
                    // mem[--sp] <- rd MOP rs
                    ALU alu = new ALU(registers[getIndex(instructions.get("rd").getBitArray())], registers[getIndex(instructions.get("rs").getBitArray())]);
                    alu.doOperation(getFunctionBits(instructions.get("function").getBitArray()));
                    if (instructions.get("function").getBinaryRepresentation().equals("00000000000000000000000000000111")) {
                        currentClockCycle += 10;
                    } else {
                        currentClockCycle += 2;
                    }
                    SP.copy(SP.decrement());
                    MainMemory.write(SP, alu.getResult());
                    currentClockCycle += 300;
                } else {    // 01111
                    // mem[--sp] <- rs1 MOP rs2
                    ALU alu = new ALU(registers[getIndex(instructions.get("rs1").getBitArray())], registers[getIndex(instructions.get("rs2").getBitArray())]);
                    alu.doOperation(getFunctionBits(instructions.get("function").getBitArray()));
                    if (instructions.get("function").getBinaryRepresentation().equals("00000000000000000000000000000111")) {
                        currentClockCycle += 10;
                    } else {
                        currentClockCycle += 2;
                    }
                    SP.copy(SP.decrement());
                    MainMemory.write(SP, alu.getResult());
                }
            } else if (convertFromBoolean(firstThreeOpCode).equalsIgnoreCase("100")) { // Load (100) - Instruction Definition Matrix
                if (convertFromBoolean(twoBits).equalsIgnoreCase("00")) {   // 10000
                    // RETURN (pc <- pop)
                    PC.copy(MainMemory.read(SP));
                    SP.copy(SP.increment());
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("01")) {    // 10001
                    // rd <- mem[rd + imm]
                    ALU alu = new ALU(registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("immediate"));
                    instructions.put("rd", MainMemory.read(alu.add2(registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("immediate"))));
                    currentClockCycle += 2;
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("10")) {    // 10010
                    // rd <- mem[rs + imm]
                    ALU alu = new ALU(registers[getIndex(instructions.get("rs").getBitArray())], instructions.get("immediate"));
                    instructions.put("rd", MainMemory.read(alu.add2(registers[getIndex(instructions.get("rs").getBitArray())], instructions.get("immediate"))));
                    currentClockCycle += 2;
                } else {    // 10011
                    // rd <- mem[rs1 + rs2]
                    ALU alu = new ALU(registers[getIndex(instructions.get("rs1").getBitArray())], registers[getIndex(instructions.get("rs2").getBitArray())]);
                    instructions.put("rd", MainMemory.read(alu.add2(registers[getIndex(instructions.get("rs1").getBitArray())], registers[getIndex(instructions.get("rs2").getBitArray())])));
                    currentClockCycle += 2;
                }
            } else if (convertFromBoolean(firstThreeOpCode).equalsIgnoreCase("101")) { // Store (101) - Instruction Definition Matrix
                if (convertFromBoolean(twoBits).equalsIgnoreCase("00")) {   // 10100
                    // unused
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("01")) {    // 10101
                    // mem[rd] <- imm
                    MainMemory.write(registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("immediate"));
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("10")) {    // 10110
                    // mem[rd + imm] <- rs
                    ALU alu = new ALU(registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("immediate"));
                    MainMemory.write(alu.add2(registers[getIndex(instructions.get("rd").getBitArray())], instructions.get("immediate")), registers[getIndex(instructions.get("rs").getBitArray())]);
                    currentClockCycle += 2;
                } else {    // 10111
                    // mem[rd + rs1] <- rs2
                    ALU alu = new ALU(registers[getIndex(instructions.get("rd").getBitArray())], registers[getIndex(instructions.get("rs1").getBitArray())]);
                    MainMemory.write(alu.add2(registers[getIndex(instructions.get("rd").getBitArray())], registers[getIndex(instructions.get("rs1").getBitArray())]), registers[getIndex(instructions.get("rs2").getBitArray())]);
                    currentClockCycle += 2;
                }
            } else if (convertFromBoolean(firstThreeOpCode).equalsIgnoreCase("110")) { // Pop/interrupt (110) - Instruction Definition Matrix
                if (convertFromBoolean(twoBits).equalsIgnoreCase("00")) {   // 11000
                    // push pc; pc <- intvec[imm]
                    // not required to implement this
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("01")) {    // 11001
                    // rd <- mem[sp++]
                    instructions.put("rd", MainMemory.read(SP));
                    SP.copy(SP.increment());
                } else if (convertFromBoolean(twoBits).equalsIgnoreCase("10")) {    // 11010
                    // rd <- mem[sp - (rs + imm)]
                    ALU alu1 = new ALU();
                    ALU alu2 = new ALU(SP, alu1.add2(registers[getIndex(instructions.get("rs").getBitArray())], instructions.get("immediate")));
                    currentClockCycle += 2;
                    Bit[] subtractionOP = new Bit[4];
                    for (int i = 0; i < 4; i ++) {
                        subtractionOP[i] = new Bit();
                        subtractionOP[i].set(true);
                    }
                    alu2.doOperation(subtractionOP);
                    currentClockCycle += 2;
                    instructions.put("rd", alu2.getResult());
                } else {    // 11011
                    // rd <- mem[sp - (rs1 + rs2)]
                    ALU alu1 = new ALU();
                    ALU alu2 = new ALU(SP, alu1.add2(registers[getIndex(instructions.get("rs1").getBitArray())], registers[getIndex(instructions.get("rs2").getBitArray())]));
                    currentClockCycle += 2;
                    Bit[] subtractionOP = new Bit[4];
                    for (int i = 0; i < 4; i ++) {
                        subtractionOP[i] = new Bit();
                        subtractionOP[i].set(true);
                    }
                    alu2.doOperation(subtractionOP);
                    currentClockCycle += 2;
                    instructions.put("rd", alu2.getResult());
                }
            }
        }
    }

    // gets last 4 bits from a word, stores it in an array, and returns it for use in ALU.doOperation
    Bit[] getFunctionBits(Bit[] wordBits) {
        Bit[] resultBits = new Bit[4];
        resultBits[0] = wordBits[28];
        resultBits[1] = wordBits[29];
        resultBits[2] = wordBits[30];
        resultBits[3] = wordBits[31];
        return resultBits;
    }

    public void store() { // stores the results
        if (instructions.containsKey("rd")) {
            int index = getIndex(instructions.get("rd").getBitArray()); // gets index of rd
            if (index != 0) {
                registers[index%32].copy(instructions.get("result")); // stores info into register at index
            }
        }
    }

    // gets index when given a bit array and returns it
    public int getIndex(Bit[] bitArray) {
        int returnNum = 0;
        int power = 32;
        for (int i = 0; i < 32; i++) {
            if (bitArray[i].getValue()) {
                if (power == 0) {
                    returnNum++;
                } else {
                    returnNum += (int) Math.pow(2, power - 1);
                }
            }
            power--;
        }
        return returnNum;
    }

    public void fetch() { // gets next instruction and increments PC
        currentInstruction.copy(MainMemory.read(PC));
        PC.copy(PC.increment());
    }
}
