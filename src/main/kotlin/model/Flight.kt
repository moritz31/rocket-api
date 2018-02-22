package model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Flight : IntIdTable() {

    val name = varchar("name", 50)

    val time = datetime("time")

    val location = reference("location",Site.id)

    val vehicle = reference("vehicle",Vehicle.id)

    val success = bool("success")
}

data class flight(val name: String, val time: String, val location: Int, val vehicle: Int, val success: Boolean)

class FlightDAO {

    val flights = hashMapOf<Int,flight>()

    init {
        transaction {
            Flight.selectAll().forEach {
                flights.put(it[Flight.id].value,
                        flight(it[Flight.name].toString(),
                                it[Flight.time].toString(),
                                it[Flight.location].value,
                                it[Flight.vehicle].value,
                                it[Flight.success]))
            }
        }
    }

    fun findById(id: Int): flight? {
        return flights[id]
    }
}