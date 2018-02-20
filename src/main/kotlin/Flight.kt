import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.booleanParam

object Flight : IntIdTable() {

    val name = varchar("name", 50)

    val time = datetime("time")

    val location = integer("location")

    val vehicle = integer("vehicle")

    val success = bool("success")
}

class Flights(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Flights>(Flight)

    var name     by Flight.name
    var time     by Flight.time
    var location by Flight.location
    var vehicle  by Flight.vehicle
    var success  by Flight.success
}