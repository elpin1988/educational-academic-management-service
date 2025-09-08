package co.edu.school.academic_management_service.infrastructure.database.table

import co.edu.school.academic_management_service.domain.entity.Grade
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object GradeTable : LongIdTable("grades") {
    val name = varchar("name", 100).uniqueIndex()
    val description = text("description").nullable()
    val level = integer("level").uniqueIndex()
    val isActive = bool("is_active").default(true)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class GradeEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<GradeEntity>(GradeTable)
    
    var name by GradeTable.name
    var description by GradeTable.description
    var level by GradeTable.level
    var isActive by GradeTable.isActive
    var createdAt by GradeTable.createdAt
    var updatedAt by GradeTable.updatedAt
    
    fun toDomain(): Grade {
        return Grade(
            id = id.value,
            name = name,
            description = description,
            level = level,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

fun GradeEntity.Companion.fromDomain(grade: Grade): GradeEntity {
    val entity = if (grade.id != null) {
        GradeEntity.findById(grade.id) ?: GradeEntity.new { }
    } else {
        GradeEntity.new { }
    }
    
    entity.name = grade.name
    entity.description = grade.description
    entity.level = grade.level
    entity.isActive = grade.isActive
    entity.createdAt = grade.createdAt
    entity.updatedAt = grade.updatedAt
    
    return entity
}
