package co.edu.school.academic_management_service.infrastructure.repository

import co.edu.school.academic_management_service.domain.entity.Grade
import co.edu.school.academic_management_service.domain.repository.GradeQueryRepository
import co.edu.school.academic_management_service.infrastructure.database.mapper.GradeMapper
import co.edu.school.academic_management_service.infrastructure.database.table.GradeEntity
import co.edu.school.academic_management_service.infrastructure.database.table.GradeTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Repository

/**
 * Real implementation of GradeQueryRepository using Exposed ORM DAO API.
 * This implementation uses PostgreSQL database with Exposed Entity-based approach.
 */
@Repository
class GradeQueryRepositoryImpl : GradeQueryRepository {
    
    override fun findById(id: Long): Grade? {
        return transaction {
            GradeEntity.findById(id)?.let { GradeMapper.toDomain(it) }
        }
    }
    
    override fun findByName(name: String): Grade? {
        return transaction {
            GradeEntity.find { GradeTable.name eq name }
                .firstOrNull()?.let { GradeMapper.toDomain(it) }
        }
    }
    
    override fun findByLevel(level: Int): Grade? {
        return transaction {
            GradeEntity.find { GradeTable.level eq level }
                .firstOrNull()?.let { GradeMapper.toDomain(it) }
        }
    }
    
    override fun findAll(): List<Grade> {
        return transaction {
            GradeEntity.all().map { GradeMapper.toDomain(it) }
        }
    }
    
    override fun findAllActive(): List<Grade> {
        return transaction {
            GradeEntity.find { GradeTable.isActive eq true }
                .map { GradeMapper.toDomain(it) }
        }
    }
    
    override fun findAllInactive(): List<Grade> {
        return transaction {
            GradeEntity.find { GradeTable.isActive eq false }
                .map { GradeMapper.toDomain(it) }
        }
    }
    
    override fun existsById(id: Long): Boolean {
        return transaction {
            GradeEntity.findById(id) != null
        }
    }
    
    override fun existsByName(name: String): Boolean {
        return transaction {
            GradeEntity.find { GradeTable.name eq name }
                .count() > 0
        }
    }
    
    override fun existsByLevel(level: Int): Boolean {
        return transaction {
            GradeEntity.find { GradeTable.level eq level }
                .count() > 0
        }
    }
    
    override fun count(): Long {
        return transaction {
            GradeEntity.all().count().toLong()
        }
    }
    
    override fun countActive(): Long {
        return transaction {
            GradeEntity.find { GradeTable.isActive eq true }
                .count().toLong()
        }
    }
}