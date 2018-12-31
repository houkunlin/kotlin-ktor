package cn.goour

import cn.goour.model.*
import cn.goour.route.*
import freemarker.cache.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import java.text.*

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
        // 表单验证
        loginConfig()
        // JWT验证
        jwt {
            verifier(SimpleJWT.verifier)

            validate {
                // 如果我们设置了过期时间，在运行到这里之前，它们会帮我们检测过期时间
                // 如果已过期，是不会运行到这里的，运行到这里意味着它一定是有效的token
                val jsonString = it.payload.subject
                // 返回一个用户信息，当然也可以返回其他对象信息，只要他实现Principal接口即可
                JwtToken.build(jsonString)
            }
        }
    }

    /**
     * 使用session会话功能
     */
    install(Sessions) {
        // 通过cookie保存会话
        cookie<MySession>("SESSION")
    }

    install(DefaultHeaders) {
        header("AuthorName", "HouKunLin")
        header("AuthorEmail", "houkunlin@aliyun.com")
    }

    /**
     * 正式的路径映射内容
     */
    routing {
        configStaticPath()
        login()
        jwtLogin()

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