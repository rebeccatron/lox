import me.bink.klox.Token
import me.bink.klox.TokenType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

import org.junit.Assert.*

class TokenTest {

    @Test
    fun testToString() {
        val subject = Token(
            TokenType.DOT,
            ".",
            null,
            45
        )

        assertThat(
            subject.toString(),
            equalTo("Token(type=DOT, lexeme=., literal=null, line=45)")
        )
    }
}