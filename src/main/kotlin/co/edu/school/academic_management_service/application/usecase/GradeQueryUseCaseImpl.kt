package co.edu.school.academic_management_service.application.usecase

import co.edu.school.academic_management_service.application.port.input.GradeQueryUseCase
import co.edu.school.academic_management_service.application.port.output.GradeRepositoryPort
import co.edu.school.academic_management_service.domain.entity.Grade
import org.springframework.stereotype.Service

/**
 * Implementation of grade query use cases.
 * Follows Single Responsibility Principle and contains query logic.
 */
@Service
class GradeQueryUseCaseImpl(
    private val gradeRepositoryPort: GradeRepositoryPort
) : GradeQueryUseCase {
    
    override fun getGradeById(id: Long): Grade? {
        return gradeRepositoryPort.findById(id)
    }
    
    override fun getGradeByName(name: String): Grade? {
        require(name.isNotBlank()) { "Grade name cannot be blank" }
        return gradeRepositoryPort.findByName(name.trim())
    }
    
    override fun getGradeByLevel(level: Int): Grade? {
        require(level > 0) { "Grade level must be positive" }
        return gradeRepositoryPort.findByLevel(level)
    }
    
    override fun getAllGrades(): List<Grade> {
        return gradeRepositoryPort.findAll()
    }
    
    override fun getActiveGrades(): List<Grade> {
        return gradeRepositoryPort.findAllActive()
    }
    
    override fun getInactiveGrades(): List<Grade> {
        return gradeRepositoryPort.findAllInactive()
    }
    
    override fun gradeExists(id: Long): Boolean {
        return gradeRepositoryPort.existsById(id)
    }
    
    override fun gradeExistsByName(name: String): Boolean {
        require(name.isNotBlank()) { "Grade name cannot be blank" }
        return gradeRepositoryPort.existsByName(name.trim())
    }
    
    override fun gradeExistsByLevel(level: Int): Boolean {
        require(level > 0) { "Grade level must be positive" }
        return gradeRepositoryPort.existsByLevel(level)
    }
}
