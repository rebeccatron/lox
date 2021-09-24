package me.bink.klox

import me.bink.klox.TokenType.*

// range is inclusive
internal val NUMBER_RANGE: CharRange = '0'.rangeTo('9')


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
        tokens.add(Token(EOF, "", null, line))

        return tokens
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun scanToken() {
        when (val c = advance()) {

            // straightforward tokens
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)

            // one or two-character tokens
            '!' -> addToken(if (match('=')) BANG_EQUAL else BANG_EQUAL)
            '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
            '<' -> addToken(if (match('=')) LESS_EQUAL else LESS)
            '>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)

            // could be single character or whole comment!
            '/' -> handleSlash()

            // disregard whitespace
            ' ', '\r', '\t' -> Unit // Unit is like void
            '\n' -> line++

            // strings
            '"' -> handleString()

            else -> handleOther(c)
        }
    }

    private fun handleOther(c: Char) {
        when {
            isDigit(c) -> handleNumber()
            else -> Lox.error(line, "Unexpected character: $c")
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

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false

        if (sourceChars[current] != expected) return false;

        // advance current, since this lexeme has more than one character
        current++
        return true

    }

    private fun handleSlash() {
        if (match('/')) {
            // handle comments to end of line
            // Comments *are* lexemes, but we don't care about them so we skip retaining them as tokens
            while (peek() != '\n' && !isAtEnd()) advance()
        } else {
            addToken(SLASH)
        }
    }

    // like advance, but only a 'lookahead' so it doesn't advance the current pointer
    private fun peek(): Char {
        return if (isAtEnd()) {
            '\n'
        } else {
            sourceChars[current]
        }
    }

    private fun peekNext(): Char {
        val nextChar = current + 1
        return if (nextChar >= source.length) {
            '\n'
        } else {
            sourceChars[nextChar]
        }
    }

    private fun handleString() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string!")
        }

        advance() // Closing " finishing the string

        val stringLiteral = source.substring(start + 1, current - 1) // trim the "'s
        addToken(STRING, stringLiteral)
    }

    private fun isDigit(character: Char): Boolean {
        return character in NUMBER_RANGE
    }

    private fun handleNumber() {
        while (isDigit((peek()))) advance()

        // is there a '.'?
        if (peek() == '.' && isDigit(peekNext())) {
            advance() // consume the decimal point...

            while (isDigit((peek()))) advance() // ...and all the following digits
        }

        val numberString = source.substring(start, current)
        addToken(NUMBER, numberString.toDouble())
    }
}