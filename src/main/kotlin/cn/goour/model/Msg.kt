package cn.goour.model

data class Msg(
    var code: Int,
    var message: String,
    var data: Any? = null
) {
    companion object {
        fun success(): Msg {
            return success("success")
        }

        fun success(message: String): Msg {
            return Msg(0, message)
        }

        fun success(data: Any?): Msg {
            return Msg(0, "success", data)
        }

        fun error(): Msg {
            return error("error")
        }

        fun error(message: String): Msg {
            return Msg(1, message)
        }
    }
}