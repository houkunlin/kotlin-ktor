package cn.goour.model

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*

object SimpleJWT {
    private val algorithm = Algorithm.HMAC256("JWT-password")
    val verifier = JWT.require(algorithm).build()
    fun sign(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)

    fun sign(block: JWTCreator.Builder.() -> JWTCreator.Builder): String {
        return block(JWT.create()).sign(algorithm)
    }
}