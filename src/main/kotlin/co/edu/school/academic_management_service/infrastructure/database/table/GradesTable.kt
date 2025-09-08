package co.edu.school.academic_management_service.infrastructure.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

/**
 * Exposed table definition for grades.
 * Maps to the grades table in the database.
 */
object GradesTable : LongIdTable("grades") {
    val name = varchar("name", 100).uniqueIndex()
    val description = varchar("description", 500).nullable()
    val level = integer("level").uniqueIndex()
    val isActive = bool("is_active").default(true)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}
