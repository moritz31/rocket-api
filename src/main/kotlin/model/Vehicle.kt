package model

import org.jetbrains.exposed.dao.IntIdTable

object Vehicle : IntIdTable() {

    val name = varchar("name",50)

}