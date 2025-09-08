package co.edu.school.academic_management_service.infrastructure.database.mapper

import co.edu.school.academic_management_service.domain.entity.Grade
import co.edu.school.academic_management_service.infrastructure.database.table.GradeEntity

object GradeMapper {
    
    fun toDomain(entity: GradeEntity): Grade {
        return Grade(
            id = entity.id.value,
            name = entity.name,
            description = entity.description,
            level = entity.level,
            isActive = entity.isActive,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(domain: Grade): GradeEntity {
        return if (domain.id != null) {
            GradeEntity.findById(domain.id) ?: throw IllegalArgumentException("Grade with id ${domain.id} not found")
        } else {
            GradeEntity.new {
                this.name = domain.name
                this.description = domain.description
                this.level = domain.level
                this.isActive = domain.isActive
                this.createdAt = domain.createdAt
                this.updatedAt = domain.updatedAt
            }
        }
    }
}
