package me.bink.klox

class Scanner constructor(private val source: String) {

    private val tokens = ArrayList<Token>()
    private val sourceChars = source.toCharArray()

    // offset into the string pointing to the first character of the lexeme
    private var start = 0

    // offset into the string pointing to the character of the lexeme
    // currently under consideration
    private var current = 0

    private var line = 1

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        // not strictly necessary to include an EOF token, but nice + tidy
        tokens.add(Token(TokenType.EOF, "", null, line))

        return tokens
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun scanToken() {
        when (advance()) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)
        }
    }

    private fun advance(): Char {
        current++
        // TODO: check bounds?
        return sourceChars[current - 1]
    }

    private fun addToken(tokenType: TokenType, literal: Any? = null) {
        // start inclusive, end exclusive
        val text = source.substring(start, current)

        tokens.add(Token(tokenType, text, literal, line))
    }
}