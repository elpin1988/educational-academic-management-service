package co.edu.school.academic_management_service.application.usecase

import co.edu.school.academic_management_service.application.port.input.GradeManagementUseCase
import co.edu.school.academic_management_service.application.port.output.GradeRepositoryPort
import co.edu.school.academic_management_service.domain.constant.AcademicConstants
import co.edu.school.academic_management_service.domain.entity.Grade
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Implementation of grade management use cases.
 * Follows Single Responsibility Principle and contains business logic.
 */
@Service
class GradeManagementUseCaseImpl(
    private val gradeRepositoryPort: GradeRepositoryPort
) : GradeManagementUseCase {
    
    override fun createGrade(name: String, description: String?, level: Int): Grade {
        // Validate business rules
        validateGradeCreation(name, description, level)
        
        // Check if grade already exists
        if (gradeRepositoryPort.existsByName(name)) {
            throw IllegalArgumentException("Grade with name '$name' already exists")
        }
        
        if (gradeRepositoryPort.existsByLevel(level)) {
            throw IllegalArgumentException("Grade with level $level already exists")
        }
        
        val grade = Grade(
            name = name.trim(),
            description = description?.trim(),
            level = level
        )
        
        return gradeRepositoryPort.save(grade)
    }
    
    override fun updateGrade(id: Long, name: String, description: String?, level: Int): Grade {
        // Validate business rules
        validateGradeCreation(name, description, level)
        
        val existingGrade = gradeRepositoryPort.findById(id)
            ?: throw IllegalArgumentException(AcademicConstants.ERROR_GRADE_NOT_FOUND)
        
        // Check if name is being changed and if new name already exists
        if (existingGrade.name != name.trim() && gradeRepositoryPort.existsByName(name.trim())) {
            throw IllegalArgumentException("Grade with name '$name' already exists")
        }
        
        // Check if level is being changed and if new level already exists
        if (existingGrade.level != level && gradeRepositoryPort.existsByLevel(level)) {
            throw IllegalArgumentException("Grade with level $level already exists")
        }
        
        val updatedGrade = existingGrade.copy(
            name = name.trim(),
            description = description?.trim(),
            level = level,
            updatedAt = LocalDateTime.now()
        )
        
        return gradeRepositoryPort.update(updatedGrade)
    }
    
    override fun deactivateGrade(id: Long): Boolean {
        val grade = gradeRepositoryPort.findById(id)
            ?: throw IllegalArgumentException(AcademicConstants.ERROR_GRADE_NOT_FOUND)
        
        if (!grade.isActive) {
            return false // Already inactive
        }
        
        val deactivatedGrade = grade.copy(
            isActive = false,
            updatedAt = LocalDateTime.now()
        )
        
        gradeRepositoryPort.update(deactivatedGrade)
        return true
    }
    
    override fun activateGrade(id: Long): Boolean {
        val grade = gradeRepositoryPort.findById(id)
            ?: throw IllegalArgumentException(AcademicConstants.ERROR_GRADE_NOT_FOUND)
        
        if (grade.isActive) {
            return false // Already active
        }
        
        val activatedGrade = grade.copy(
            isActive = true,
            updatedAt = LocalDateTime.now()
        )
        
        gradeRepositoryPort.update(activatedGrade)
        return true
    }
    
    override fun deleteGrade(id: Long): Boolean {
        val grade = gradeRepositoryPort.findById(id)
            ?: throw IllegalArgumentException(AcademicConstants.ERROR_GRADE_NOT_FOUND)
        
        // Business rule: Cannot delete active grades
        if (grade.isActive) {
            throw IllegalArgumentException("Cannot delete active grade. Deactivate it first.")
        }
        
        return gradeRepositoryPort.deleteById(id)
    }
    
    private fun validateGradeCreation(name: String, description: String?, level: Int) {
        require(name.isNotBlank()) { "Grade name cannot be blank" }
        require(name.length in AcademicConstants.MIN_NAME_LENGTH..AcademicConstants.MAX_NAME_LENGTH) {
            "Grade name must be between ${AcademicConstants.MIN_NAME_LENGTH} and ${AcademicConstants.MAX_NAME_LENGTH} characters"
        }
        
        require(level in AcademicConstants.MIN_GRADE_LEVEL..AcademicConstants.MAX_GRADE_LEVEL) {
            "Grade level must be between ${AcademicConstants.MIN_GRADE_LEVEL} and ${AcademicConstants.MAX_GRADE_LEVEL}"
        }
        
        description?.let {
            require(it.length in AcademicConstants.MIN_DESCRIPTION_LENGTH..AcademicConstants.MAX_DESCRIPTION_LENGTH) {
                "Description must be between ${AcademicConstants.MIN_DESCRIPTION_LENGTH} and ${AcademicConstants.MAX_DESCRIPTION_LENGTH} characters"
            }
        }
    }
}
