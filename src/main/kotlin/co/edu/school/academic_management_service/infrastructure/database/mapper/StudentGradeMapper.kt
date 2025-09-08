package co.edu.school.academic_management_service.infrastructure.database.mapper

import co.edu.school.academic_management_service.domain.entity.StudentGrade
import co.edu.school.academic_management_service.infrastructure.database.table.StudentGradeEntity

object StudentGradeMapper {
    
    fun toDomain(entity: StudentGradeEntity): StudentGrade {
        return StudentGrade(
            id = entity.id.value,
            studentId = entity.studentId,
            gradeId = entity.grade.id.value,
            startDate = entity.startDate,
            endDate = entity.endDate,
            isActive = entity.isActive,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(domain: StudentGrade): StudentGradeEntity {
        return if (domain.id != null) {
            StudentGradeEntity.findById(domain.id) ?: throw IllegalArgumentException("StudentGrade with id ${domain.id} not found")
        } else {
            StudentGradeEntity.new {
                this.studentId = domain.studentId
                this.grade = co.edu.school.academic_management_service.infrastructure.database.table.GradeEntity.findById(domain.gradeId)
                    ?: throw IllegalArgumentException("Grade with id ${domain.gradeId} not found")
                this.startDate = domain.startDate
                this.endDate = domain.endDate
                this.isActive = domain.isActive
                this.createdAt = domain.createdAt
                this.updatedAt = domain.updatedAt
            }
        }
    }
}
