package controller

import io.javalin.ApiBuilder.get
import io.javalin.Javalin
import model.VehicleDAO

class VehicleController(app: Javalin) {

    val _app = app

    val vehicleData = VehicleDAO()

    init {
        this.registerRoutes()
    }

    private fun registerRoutes() {

        this._app.routes {

            get("/vehicle/") { ctx ->
                ctx.json(this.vehicleData.vehicles)
            }

            get("/vehicle/:id") { ctx ->
                ctx.json(this.vehicleData.findById(ctx.param("id")!!.toInt())!!)
            }

        }

    }

}