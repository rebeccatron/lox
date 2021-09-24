package me.bink.klox

class Scanner constructor(private val source: String) {

    private val tokens = ArrayList<Token>()

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

    private fun scanToken() {
        TODO("Not yet implemented")
    }

    private fun isAtEnd(): Boolean {
        return false
    }
}