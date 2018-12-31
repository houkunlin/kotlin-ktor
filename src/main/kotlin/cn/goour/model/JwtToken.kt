package cn.goour.model

import com.fasterxml.jackson.module.kotlin.*
import io.ktor.auth.*
import java.util.concurrent.atomic.*

data class JwtToken(
    var userId: Long = num.getAndIncrement(),
    var username: String
) : Principal {
    override fun toString(): String {
        return json.writeValueAsString(this)
    }

    companion object {
        val num = AtomicLong(0)
        private val json = jacksonObjectMapper()

        fun build(jsonString: String): JwtToken? {
            return json.readValue(jsonString)
        }
    }
}