package cn.goour

import cn.goour.model.IndexData
import cn.goour.model.MySession
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.PartialContent
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.sessions.*
import java.text.SimpleDateFormat

/**
 * Created by 侯坤林(houkunlin@ibona.cn) on 2018-12-24.
 *
 * @author 侯坤林
 * @date 2018-12-24 16:48
 */
/**
 * 运行入口文件.
 * 在 build.gradle 添加了 mainClassName ,因此在运行的时候,编辑运行main为该mainClassName即可.
 * 在 application.conf 添加了 modules 为当前main扩展 , 由此关联起来,运行时可以直接调用该扩展
 */
fun Application.main() {
    /**
     * 安装 FreeMarker 特性,可以直接调用它来渲染模板文件
     */
    install(FreeMarker) {
        // 这个 this::class.java.classLoader 官方是这样写的,我尝试直接用javaClass运行报错
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    /**
     * 可以启用多个routing, 把不同的route放在不同的代码块
     */
    routing {
        // 启用静态文件访问,静态文件地址/static
        static("/static") {
            // 这是静态文件路径static,这个就是resources/static
            resources("static")
        }
    }

    /**
     * 开启大文件特性的支持, 官方说可以支持观看视频拖动进度, 还可以支持续传
     */
    install(PartialContent)

    /**
     * 加入Jackson进行JSON对象响应
     */
    install(ContentNegotiation){
        jackson {
            this.dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        }
    }

    /**
     * 使用表单验证
     */
    install(Authentication) {
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

    /**
     * 使用session会话功能
     */
    install(Sessions) {
        cookie<MySession>("SESSION")
    }

    /**
     * 正式的路径映射内容
     */
    routing {
        get("/") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(0, 1, 3, 5, 4, 8)))))
        }

        post {
            val indexData = call.receive<IndexData>()
            log.debug("POST 接收的数据: {}", indexData)
            indexData.items += 200
            indexData.items += 400
            call.respond(indexData)
        }

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

}