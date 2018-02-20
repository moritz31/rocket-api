package main.kotlin

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

    var config: Configuration

    try {
        config = EnvironmentVariables() overriding
                ConfigurationProperties.fromResource("default.conf")
    } catch (e: Misconfiguration) {
        e.printStackTrace()
        config = EnvironmentVariables()
    }

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

    val app = Javalin.start(getHerokuPort())
    app.get("/") { ctx -> ctx.result("Hello World") }

    app.get("/flights") {
        ctx -> ctx.result("Flight data")
        transaction {
            logger.addLogger(StdOutSqlLogger)
            val flights = Flights.all()
        }
    }
}

fun getHerokuPort(): Int {
    val processBuilder = ProcessBuilder()
    if (processBuilder.environment().get("PORT") != null) {
        return Integer.parseInt(processBuilder.environment().get("PORT"))
    }

    return 8080
}