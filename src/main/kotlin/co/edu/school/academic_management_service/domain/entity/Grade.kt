package co.edu.school.academic_management_service.domain.entity

import co.edu.school.academic_management_service.domain.constant.AcademicConstants
import java.time.LocalDateTime

/**
 * Entity representing a grade level in the school system.
 * Follows Single Responsibility Principle by focusing only on grade information.
 * 
 * This entity is immutable and contains business logic validations.
 */
data class Grade(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val level: Int,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(name.isNotBlank()) { "Grade name cannot be blank" }
        require(name.length in AcademicConstants.MIN_NAME_LENGTH..AcademicConstants.MAX_NAME_LENGTH) {
            "Grade name must be between ${AcademicConstants.MIN_NAME_LENGTH} and ${AcademicConstants.MAX_NAME_LENGTH} characters"
        }
        require(level in AcademicConstants.MIN_GRADE_LEVEL..AcademicConstants.MAX_GRADE_LEVEL) {
            "Grade level must be between ${AcademicConstants.MIN_GRADE_LEVEL} and ${AcademicConstants.MAX_GRADE_LEVEL}"
        }
        require(description?.isNotBlank() != false) { "Description cannot be blank if provided" }
        description?.let {
            require(it.length in AcademicConstants.MIN_DESCRIPTION_LENGTH..AcademicConstants.MAX_DESCRIPTION_LENGTH) {
                "Description must be between ${AcademicConstants.MIN_DESCRIPTION_LENGTH} and ${AcademicConstants.MAX_DESCRIPTION_LENGTH} characters"
            }
        }
    }
    
    /**
     * Business method to check if grade is valid for enrollment
     */
    fun isValidForEnrollment(): Boolean = isActive
    
    /**
     * Business method to get display name
     */
    fun getDisplayName(): String = name.trim()
    
    /**
     * Business method to get full description
     */
    fun getFullDescription(): String = description?.let { "$name - $it" } ?: name
}
