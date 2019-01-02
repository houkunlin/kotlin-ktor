package cn.goour.config

import cn.goour.entity.*
import com.typesafe.config.*
import com.zaxxer.hikari.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.createStatements
import org.jetbrains.exposed.sql.transactions.*
import java.sql.*
import java.util.*
import javax.sql.*

@KtorExperimentalAPI
object DatabaseFactory {
    val database: Database

    init {
        database = Database.connect(datasource())
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction {
            // create(UserAuthTable)
            createStatements(UserTable, UserAuthTable)
            val user = UserTable.insert {
                it[nickname] = "nick"
                it[avatar] = "http://"
            } get UserTable.id
            UserAuthTable.insert {
                it[userId] = user!!
                it[type] = "username"
                it[account] = "houkunlin"
                it[certification] = "houkunlin"
            }
        }
    }

    private fun datasource(): DataSource {
        val datasourceConfig = ConfigFactory.load().getConfig("datasource")
        val properties = datasourceConfig.toProperties()
        val config = HikariConfig(properties)
        config.validate()
        return HikariDataSource(config)
    }
}

fun Config.toProperties(): Properties {
    val properties = Properties()
    this.entrySet().forEach {
        val value = it.value
        val a = it.value.toString()
        val type = it.value.valueType()
        properties[it.key] = it.value.render()
    }
    return properties
}