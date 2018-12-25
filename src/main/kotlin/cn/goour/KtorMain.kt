package cn.goour

import cn.goour.model.IndexData
import cn.goour.model.MySession
import cn.goour.route.configStaticPath
import cn.goour.route.loginConfig
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.Authentication
import io.ktor.features.ContentNegotiation
import io.ktor.features.PartialContent
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
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

    install(Routing) {
        configStaticPath()
    }

    /**
     * 开启大文件特性的支持, 官方说可以支持观看视频拖动进度, 还可以支持续传
     */
    install(PartialContent)

    /**
     * 加入Jackson进行JSON对象响应
     */
    install(ContentNegotiation) {
        jackson {
            this.dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        }
    }

    /**
     * 使用表单验证
     */
    install(Authentication) {
        loginConfig()
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
    }

}