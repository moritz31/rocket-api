package model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Site : IntIdTable() {

    val name = varchar("name",50)

}

data class site(val name: String)

class SiteDAO {

    val sites = hashMapOf<Int,site>()

    init {
        transaction {
            Site.selectAll().forEach {
                sites.put(it[Site.id].value,site(it[Site.name]))
            }
        }
    }

    fun findById(id: Int) : site? {
        return sites[id]
    }
}