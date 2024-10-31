# AWK-Interpreter

## Overview
This project is a Java-based interpreter inspired by the AWK programming language, designed to parse and execute scripts using Abstract Syntax Trees (ASTs) and dynamic typing. The interpreter handles lexing, parsing, and interpreting code, supporting various nodes and functionalities such as conditionals, loops, function definitions, and built-in functions.

## Project Structure

The interpreter follows a multi-stage approach, broken down into the following components:

### 1. **Lexer**
   - **Purpose**: Tokenizes input AWK-like code into words, numbers, symbols, and other lexemes.
   - **Main Files**:
     - `Lexer.java`: Processes raw input into a stream of tokens using a linked list.
     - `Token.java`: Defines token types, such as `WORD`, `NUMBER`, `SEPARATOR`, and includes built-in keywords.
     - `StringHandler.java`: Manages string inputs, allowing peeking, getting, and handling characters.

### 2. **Parser**
   - **Purpose**: Transforms a list of tokens into an AST, structuring code into a hierarchy of nodes for easier interpretation.
   - **Main Files**:
     - `Parser.java`: Manages token streams and handles parsing with precedence levels.
     - `TokenManager.java`: Facilitates token management with methods like `MatchAndRemove`.
     - `Node.java` and various derived classes (e.g., `FunctionDefinitionNode`, `BlockNode`, `IfNode`) represent elements of the AST.

### 3. **Interpreter**
   - **Purpose**: Executes the AST, managing variables and function calls.
   - **Main Files**:
     - `Interpreter.java`: Core interpreter, managing global and local variables and reading input files.
     - `InterpreterDataType` and `InterpreterArrayDataType`: Data types for storing variables, supporting both scalars and associative arrays.
     - `BuiltInFunctionDefinitionNode.java`: Defines built-in functions with support for variadic arguments.

### Features

- **Dynamic Typing and Operations**: Variables can store any data type (primarily strings and floats), converting as needed during operations.
- **Lexing and Parsing**: Handles keywords, symbols, and various AWK-specific syntax to create a structured AST.
- **Complex Expressions**: Supports operations with correct precedence using nodes like `MathOpNode` and `OperationNode`.
- **Built-in Functions**: AWK functions such as `print`, `getline`, `substr`, and `tolower` are implemented with Java’s native functions.

### Key Classes

- **Token Types**: Tokens are classified as keywords, operators, literals, etc., for efficient parsing.
- **Node Types**: Different nodes (e.g., `ForNode`, `IfNode`, `WhileNode`) represent various constructs, allowing modular execution.
- **ReturnType**: Manages control flow, enabling `break`, `continue`, and `return` statements within loops and functions.

