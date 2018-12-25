package cn.goour

import cn.goour.model.IndexData
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.Authentication
import io.ktor.auth.FormAuthChallenge
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.form
import io.ktor.features.PartialContent
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

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
     * 正式的路径映射内容
     */
    routing {
        get("/") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(0, 1, 3, 5, 4, 8)))))
        }

        /**
         * 添加登录映射
         */
        route("/login") {
            get {
                call.respond(FreeMarkerContent("login.ftl", null))
            }
            post {
                val params = call.receiveParameters()
                log.debug("请求参数:{}", params)
                if (params["username"] == null || params["password"] == null) {
                    call.respond(FreeMarkerContent("login.ftl", mapOf("error" to "失败,请输入参数")))
                } else {
                    call.respond(FreeMarkerContent("login.ftl", mapOf("ok" to "成功")))
                }
            }
        }
    }

}