package main.kotlin

import io.javalin.Javalin
import org.jetbrains.exposed.sql.transactions.transaction
import com.natpryce.konfig.*
import model.*
import org.jetbrains.exposed.sql.*

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


    val flightData = FlightDAO()

    transaction {
        logger.addLogger(StdOutSqlLogger)


        (Flight innerJoin Site innerJoin Vehicle)
                .selectAll().forEach {
            println("${it[Flight.name]} - ${it[Site.name]} - ${it[Vehicle.name]} - ${it[Flight.time]}")
        }

    }

    val app = Javalin.start(getHerokuPort())
    app.get("/") { ctx -> ctx.result("Hello World") }

    app.get("/flights") { ctx ->
        ctx.json(flightData.flights)
    }

    app.get("/flights/:id") { ctx ->
        ctx.json(flightData.findById(ctx.param("id")!!.toInt())!!)
    }
}

fun getHerokuPort(): Int {
    val processBuilder = ProcessBuilder()
    if (processBuilder.environment().get("PORT") != null) {
        return Integer.parseInt(processBuilder.environment().get("PORT"))
    }

    return 8080
}