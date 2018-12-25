package cn.goour.route

import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.Route

/**
 * 静态路径路由配置.
 * Created by 侯坤林(houkunlin@ibona.cn) on 2018-12-25.
 *
 * @author 侯坤林
 * @date 2018-12-25 16:02
 */
fun Route.configStaticPath() {
    // 启用静态文件访问,静态文件地址/static
    static("/static") {
        // 这是静态文件路径static,这个就是resources/static
        resources("static")
    }
}