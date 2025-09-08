package co.edu.school.academic_management_service.infrastructure.repository

import co.edu.school.academic_management_service.domain.entity.StudentGrade
import co.edu.school.academic_management_service.domain.repository.StudentGradeCommandRepository
import co.edu.school.academic_management_service.infrastructure.database.mapper.StudentGradeMapper
import co.edu.school.academic_management_service.infrastructure.database.table.StudentGradeEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * Real implementation of StudentGradeCommandRepository using Exposed ORM DAO API.
 * This implementation uses PostgreSQL database with Exposed Entity-based approach.
 */
@Repository
class StudentGradeCommandRepositoryImpl : StudentGradeCommandRepository {
    
    override fun save(studentGrade: StudentGrade): StudentGrade {
        return transaction {
            val entity = if (studentGrade.id != null) {
                StudentGradeEntity.findById(studentGrade.id)
                    ?: throw IllegalArgumentException("StudentGrade with id ${studentGrade.id} not found")
            } else {
                StudentGradeEntity.new {
                    this.studentId = studentGrade.studentId
                    this.grade = co.edu.school.academic_management_service.infrastructure.database.table.GradeEntity.findById(studentGrade.gradeId)
                        ?: throw IllegalArgumentException("Grade with id ${studentGrade.gradeId} not found")
                    this.startDate = studentGrade.startDate
                    this.endDate = studentGrade.endDate
                    this.isActive = studentGrade.isActive
                    this.createdAt = studentGrade.createdAt
                    this.updatedAt = studentGrade.updatedAt
                }
            }
            StudentGradeMapper.toDomain(entity)
        }
    }
    
    override fun update(studentGrade: StudentGrade): StudentGrade {
        val id = studentGrade.id ?: throw IllegalArgumentException("StudentGrade ID cannot be null for update")
        
        return transaction {
            val entity = StudentGradeEntity.findById(id)
                ?: throw IllegalArgumentException("StudentGrade with id $id not found")
            
            entity.studentId = studentGrade.studentId
            entity.grade = co.edu.school.academic_management_service.infrastructure.database.table.GradeEntity.findById(studentGrade.gradeId)
                ?: throw IllegalArgumentException("Grade with id ${studentGrade.gradeId} not found")
            entity.startDate = studentGrade.startDate
            entity.endDate = studentGrade.endDate
            entity.isActive = studentGrade.isActive
            entity.updatedAt = LocalDateTime.now()
            
            StudentGradeMapper.toDomain(entity)
        }
    }
    
    override fun deleteById(id: Long): Boolean {
        return transaction {
            val entity = StudentGradeEntity.findById(id)
            entity?.delete()
            entity != null
        }
    }
    
    override fun delete(studentGrade: StudentGrade): Boolean {
        val id = studentGrade.id ?: return false
        return deleteById(id)
    }
    
    override fun endEnrollment(studentId: Long, gradeId: Long, endDate: LocalDateTime): StudentGrade? {
        // TODO: Implement complex query logic
        // For now, return null to maintain compatibility
        return null
    }
    
    override fun transferStudent(
        studentId: Long, 
        fromGradeId: Long, 
        toGradeId: Long, 
        transferDate: LocalDateTime
    ): StudentGrade? {
        // TODO: Implement complex query logic
        // For now, return null to maintain compatibility
        return null
    }
}