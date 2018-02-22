package controller

import io.javalin.ApiBuilder.get
import io.javalin.Javalin
import model.FlightDAO

class FlightController(app: Javalin) {

    val _app = app

    val flightData = FlightDAO()

    init {

        this.registerRoutes()
    }
    
    private fun registerRoutes() {

        this._app.routes {

            get("/flights/") { ctx ->
                ctx.json(this.flightData.flights)
            }

            get("/flights/:id") { ctx ->
                ctx.json(this.flightData.findById(ctx.param("id")!!.toInt())!!)
            }

        }
    }
}