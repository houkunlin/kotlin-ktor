package cn.goour.route

import cn.goour.model.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Routing.jwtLogin() {
    route("auth/jwt") {
        post("register") {
            val user = call.receive<User>()
            log.debug("register user : {}", user)
            val jwtToken = JwtToken(username = user.username)

            val token = SimpleJWT.sign {
                // 设置jwt token过期时间
                withExpiresAt(Date(System.currentTimeMillis() + 120_000))
                // 设置用户信息
                withSubject(jwtToken.toString())
            }
            call.respond(mapOf("token" to token))
        }
        authenticate {
            get("info") {
                // 访问需要验证的代码时，这里可以直接获取到用户信息
                val principal = call.principal<JwtToken>()
                call.respond(Msg.success(principal))
            }
        }
    }
}
