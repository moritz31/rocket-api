package model

import org.jetbrains.exposed.dao.IntIdTable

object Site : IntIdTable() {

    val name = varchar("name",50)

}