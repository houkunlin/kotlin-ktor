package cn.goour

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

/**
 * Created by 侯坤林(houkunlin@ibona.cn) on 2018-12-24.
 *
 * @author 侯坤林
 * @date 2018-12-24 16:48
 */

fun Application.main() {
    routing {
        get("/") {
            call.respond("Hello world!")
        }
    }
}