package cn.goour.route

import cn.goour.model.MySession
import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.slf4j.LoggerFactory

/**
 * 表单验证页面和请求配置.
 * Created by 侯坤林(houkunlin@ibona.cn) on 2018-12-25.
 *
 * @author 侯坤林
 * @date 2018-12-25 16:29
 */
val log = LoggerFactory.getLogger("Login")!!

fun Routing.login() {
    /**
     * 添加登录映射
     */
    route("/login") {
        get {
            val session = call.sessions.get<MySession>()
            if (session == null) {
                log.debug("该用户确实未登录")
            } else {
                log.debug("这是一个已经登录成功的用户")
            }
            call.respond(FreeMarkerContent("login.ftl", null))
        }
        // 使用表单验证
        authenticate("login") {
            // 当表单验证成功时,才会进入到这里,验证失败在form验证代码里面会直接跳转
            post {
                // 理论上运行到这里不可能为Null,但是不明白为什么为什么会返回Null
                // 这个UserIdPrincipal是在form validate代码里返回的用户对象,它是一个实现Principal接口的对象
                val principal = call.principal<UserIdPrincipal>() ?: error("登录失败,没有用户")
                // 这个东西看起来有点怪怪的,它会在浏览器产生一个SESSION = name%3D%2523{{name}}的值
                call.sessions.set(MySession(principal.name))
                // 登录成功跳转
                call.respondRedirect("/", permanent = false)
            }
        }
    }
}

/**
 * 表单认证配置
 */
fun Authentication.Configuration.loginConfig() {
    form("login") {
        userParamName = "username"
        passwordParamName = "password"
        // 使用该方法会返回一个代码401验证错误
        challenge = FormAuthChallenge.Unauthorized
        // 自定义处理验证失败时跳转链接
        challenge = FormAuthChallenge.Redirect(url = { credentials ->
            // 验证失败时会执行这里,这个credentials是一个UserPasswordCredential对象
            log.debug("challenge user = {}", credentials)
            "/"
        })
        log.debug(
            "userParamName = {}, passwordParamName = {}, challenge = {}",
            userParamName,
            passwordParamName,
            challenge
        )
        // 进行账号验证
        validate { credentials ->
            log.debug("credentials = {}", credentials)
            if (credentials.name == credentials.password) {
                UserIdPrincipal(credentials.name)
            } else {
                // 验证失败
                null
            }
        }
    }
}