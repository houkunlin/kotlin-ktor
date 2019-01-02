package cn.goour.entity

import org.jetbrains.exposed.sql.*
import java.util.Date

object UserTable : Table() {
    val id = long("id").primaryKey().autoIncrement()
    val nickname = varchar("nickname", length = 32)
    val avatar = varchar("avatar", length = 255)
//    val gmtCreate = datetime("gmtCreate")
//    val gmtModified = datetime("gmtModified")
}

object UserAuthTable : Table() {
    val id = long("id").primaryKey().autoIncrement()
    val userId = long("userId")
    val type = varchar("type", length = 32)
    val account = varchar("account", length = 128)
    val certification = varchar("certification", length = 256)
    val expired = datetime("gmtCreate").nullable()

//    val user = reference("user", Column<Long>(UserTable, "id", LongColumnType()))

//    val gmtCreate = datetime("gmtCreate")
//    val gmtModified = datetime("gmtModified")
}

data class User(
    var id: Long = 0,
    var nickname: String = "",
    var avatar: String = "",

    var gmtCreate: Date = Date(),
    var gmtModified: Date = Date()
)

data class UserAuth(
    var id: Long = 0,
    var userId: Long = 0,
    var type: String = "",
    var account: String = "",
    var certification: String = "",
    var expired: Date? = null,
//    var user: User? = null,
    var gmtCreate: Date = Date(),
    var gmtModified: Date = Date()
)