package co.edu.school.academic_management_service.infrastructure.repository

import co.edu.school.academic_management_service.domain.entity.Grade
import co.edu.school.academic_management_service.domain.repository.GradeCommandRepository
import co.edu.school.academic_management_service.infrastructure.database.mapper.GradeMapper
import co.edu.school.academic_management_service.infrastructure.database.table.GradeEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * Real implementation of GradeCommandRepository using Exposed ORM DAO API.
 * This implementation uses PostgreSQL database with Exposed Entity-based approach.
 */
@Repository
class GradeCommandRepositoryImpl : GradeCommandRepository {
    
    override fun save(grade: Grade): Grade {
        return transaction {
            val entity = if (grade.id != null) {
                GradeEntity.findById(grade.id)
                    ?: throw IllegalArgumentException("Grade with id ${grade.id} not found")
            } else {
                GradeEntity.new {
                    this.name = grade.name
                    this.description = grade.description
                    this.level = grade.level
                    this.isActive = grade.isActive
                    this.createdAt = grade.createdAt
                    this.updatedAt = grade.updatedAt
                }
            }
            GradeMapper.toDomain(entity)
        }
    }
    
    override fun update(grade: Grade): Grade {
        val id = grade.id ?: throw IllegalArgumentException("Grade ID cannot be null for update")
        
        return transaction {
            val entity = GradeEntity.findById(id)
                ?: throw IllegalArgumentException("Grade with id $id not found")
            
            entity.name = grade.name
            entity.description = grade.description
            entity.level = grade.level
            entity.isActive = grade.isActive
            entity.updatedAt = LocalDateTime.now()
            
            GradeMapper.toDomain(entity)
        }
    }
    
    override fun deleteById(id: Long): Boolean {
        return transaction {
            val entity = GradeEntity.findById(id)
            entity?.delete()
            entity != null
        }
    }
    
    override fun delete(grade: Grade): Boolean {
        val id = grade.id ?: return false
        return deleteById(id)
    }
}