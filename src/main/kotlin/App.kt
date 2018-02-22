package main.kotlin

import io.javalin.Javalin
import org.jetbrains.exposed.sql.transactions.transaction
import com.natpryce.konfig.*
import controller.FlightController
import controller.SiteController
import controller.VehicleController
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

    // Launch the app
    val app = Javalin.start(getHerokuPort())

    // Start the flightController
    FlightController(app)
    SiteController(app)
    VehicleController(app)
}


/**
 * If running on heroku we need to get the port deployed by heroku instead of the default one
 */
fun getHerokuPort(): Int {
    val processBuilder = ProcessBuilder()
    if (processBuilder.environment().get("PORT") != null) {
        return Integer.parseInt(processBuilder.environment().get("PORT"))
    }

    return 8080
}