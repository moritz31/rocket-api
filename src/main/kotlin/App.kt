package main.kotlin

import com.beust.klaxon.Klaxon
import io.javalin.Javalin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.transactions.transaction
import com.natpryce.konfig.*

object server : PropertyGroup() {
    val port by intType
    val url by stringType
    val databaseName by stringType
    val username by stringType
    val password by stringType
}

fun main(args: Array<String>) {

    val config = ConfigurationProperties.fromResource("default.conf")

    Database.connect(
            url = "jdbc:sqlserver://${config[server.url]}:${config[server.port]};databaseName=${config[server.databaseName]}",
            user = config[server.username],
            password = config[server.password],
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver")

    transaction {
        logger.addLogger(StdOutSqlLogger)
        val flights = Flights.all()
        for(f in flights) {
            System.out.println(f.name)
            System.out.println(f.time)
        }

    }

    val app = Javalin.start(7000)
    app.get("/") { ctx -> ctx.result("Hello World") }

    app.get("/flights") {
        ctx -> ctx.result("Flight data")
        transaction {
            logger.addLogger(StdOutSqlLogger)
            val flights = Flights.all()

        }
    }
}