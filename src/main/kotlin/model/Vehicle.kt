package model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Vehicle : IntIdTable() {

    val name = varchar("name",50)

}

data class vehicle(val name: String)

class VehicleDAO {

    val vehicles = hashMapOf<Int,vehicle>()

    init {
        transaction {
            Vehicle.selectAll().forEach {
                vehicles.put(it[Vehicle.id].value,vehicle(it[Vehicle.name]))
            }
        }
    }

    fun findById(id: Int) : vehicle? {
        return vehicles[id]
    }

}