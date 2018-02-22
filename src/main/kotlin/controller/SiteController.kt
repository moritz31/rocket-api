package controller

import io.javalin.ApiBuilder.get
import io.javalin.Javalin
import model.SiteDAO

class SiteController(app: Javalin) {

    val _app = app

    val siteData = SiteDAO()

    init {
        this.registerRoutes()
    }

    private fun registerRoutes() {

        this._app.routes {

            get("/site/") {ctx ->
                ctx.json(this.siteData.sites)
            }

            get("/site/:id") {ctx ->
                ctx.json(this.siteData.findById(ctx.param("id")!!.toInt())!!)
            }

        }

    }

}