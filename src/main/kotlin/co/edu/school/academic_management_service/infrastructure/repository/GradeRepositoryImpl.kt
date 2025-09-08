package co.edu.school.academic_management_service.infrastructure.repository

import co.edu.school.academic_management_service.application.port.output.GradeRepositoryPort
import co.edu.school.academic_management_service.domain.entity.Grade
import co.edu.school.academic_management_service.domain.repository.GradeCommandRepository
import co.edu.school.academic_management_service.domain.repository.GradeQueryRepository
import org.springframework.stereotype.Repository

/**
 * Implementation of GradeRepositoryPort.
 * This is an adapter that implements the output port interface.
 * 
 * For now, this is a stub implementation. In a real scenario,
 * this would delegate to the actual repository implementations.
 */
@Repository
class GradeRepositoryImpl(
    private val gradeCommandRepository: GradeCommandRepository,
    private val gradeQueryRepository: GradeQueryRepository
) : GradeRepositoryPort {
    
    override fun save(grade: Grade): Grade = gradeCommandRepository.save(grade)
    
    override fun update(grade: Grade): Grade = gradeCommandRepository.update(grade)
    
    override fun deleteById(id: Long): Boolean = gradeCommandRepository.deleteById(id)
    
    override fun findById(id: Long): Grade? = gradeQueryRepository.findById(id)
    
    override fun findByName(name: String): Grade? = gradeQueryRepository.findByName(name)
    
    override fun findByLevel(level: Int): Grade? = gradeQueryRepository.findByLevel(level)
    
    override fun findAll(): List<Grade> = gradeQueryRepository.findAll()
    
    override fun findAllActive(): List<Grade> = gradeQueryRepository.findAllActive()
    
    override fun findAllInactive(): List<Grade> = gradeQueryRepository.findAllInactive()
    
    override fun existsById(id: Long): Boolean = gradeQueryRepository.existsById(id)
    
    override fun existsByName(name: String): Boolean = gradeQueryRepository.existsByName(name)
    
    override fun existsByLevel(level: Int): Boolean = gradeQueryRepository.existsByLevel(level)
    
    override fun count(): Long = gradeQueryRepository.count()
    
    override fun countActive(): Long = gradeQueryRepository.countActive()
}
