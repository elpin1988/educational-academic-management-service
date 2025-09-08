package co.edu.school.academic_management_service.domain.entity

import co.edu.school.academic_management_service.domain.constant.AcademicConstants
import java.time.LocalDateTime

/**
 * Domain entity representing a student in the academic management system.
 * Follows Single Responsibility Principle by focusing only on student information.
 * 
 * This entity is immutable and contains business logic validations.
 */
data class Student(
    val id: Long? = null,
    val documentType: String,
    val documentNumber: String,
    val firstName: String,
    val lastName: String,
    val currentGradeId: Long? = null,
    val guardianId: Long,
    val userId: Long? = null, // Reference to User entity for authentication
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(documentType.isNotBlank()) { "Document type cannot be blank" }
        require(documentType in setOf(
            AcademicConstants.DOCUMENT_TYPE_CC,
            AcademicConstants.DOCUMENT_TYPE_TI,
            AcademicConstants.DOCUMENT_TYPE_CE,
            AcademicConstants.DOCUMENT_TYPE_PASSPORT
        )) { "Invalid document type" }
        
        require(documentNumber.isNotBlank()) { "Document number cannot be blank" }
        require(documentNumber.length in AcademicConstants.MIN_DOCUMENT_NUMBER_LENGTH..AcademicConstants.MAX_DOCUMENT_NUMBER_LENGTH) {
            "Document number must be between ${AcademicConstants.MIN_DOCUMENT_NUMBER_LENGTH} and ${AcademicConstants.MAX_DOCUMENT_NUMBER_LENGTH} characters"
        }
        
        require(firstName.isNotBlank()) { "First name cannot be blank" }
        require(firstName.length in AcademicConstants.MIN_NAME_LENGTH..AcademicConstants.MAX_NAME_LENGTH) {
            "First name must be between ${AcademicConstants.MIN_NAME_LENGTH} and ${AcademicConstants.MAX_NAME_LENGTH} characters"
        }
        
        require(lastName.isNotBlank()) { "Last name cannot be blank" }
        require(lastName.length in AcademicConstants.MIN_NAME_LENGTH..AcademicConstants.MAX_NAME_LENGTH) {
            "Last name must be between ${AcademicConstants.MIN_NAME_LENGTH} and ${AcademicConstants.MAX_NAME_LENGTH} characters"
        }
        
        require(guardianId > 0) { "Guardian ID must be positive" }
        require(currentGradeId == null || currentGradeId > 0) { 
            "Current grade ID must be positive if provided" 
        }
    }
    
    /**
     * Business method to get full name
     */
    val fullName: String
        get() = "$firstName $lastName"
    
    /**
     * Business method to get document information
     */
    val documentInfo: String
        get() = "$documentType $documentNumber"
    
    /**
     * Business method to check if student is currently enrolled
     */
    fun isCurrentlyEnrolled(): Boolean = isActive && currentGradeId != null
    
    /**
     * Business method to check if student can be enrolled in a grade
     */
    fun canBeEnrolledInGrade(gradeId: Long): Boolean = isActive && currentGradeId != gradeId
    
    /**
     * Business method to get display name for reports
     */
    fun getDisplayName(): String = fullName.trim()
}
