package co.edu.school.academic_management_service.domain.service

import co.edu.school.academic_management_service.domain.constant.AcademicConstants
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.regex.Pattern

/**
 * Service for domain validations.
 * Contains business rule validations that can be reused across the domain.
 */
@Service
class ValidationService {
    
    /**
     * Validates grade name format and length.
     */
    fun validateGradeName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult.error("Grade name cannot be blank")
        }
        
        if (name.length < AcademicConstants.MIN_NAME_LENGTH) {
            return ValidationResult.error("Grade name must be at least ${AcademicConstants.MIN_NAME_LENGTH} characters")
        }
        
        if (name.length > AcademicConstants.MAX_NAME_LENGTH) {
            return ValidationResult.error("Grade name must be at most ${AcademicConstants.MAX_NAME_LENGTH} characters")
        }
        
        if (!isValidGradeNameFormat(name)) {
            return ValidationResult.error("Grade name contains invalid characters")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates grade level.
     */
    fun validateGradeLevel(level: Int): ValidationResult {
        if (level < AcademicConstants.MIN_GRADE_LEVEL) {
            return ValidationResult.error("Grade level must be at least ${AcademicConstants.MIN_GRADE_LEVEL}")
        }
        
        if (level > AcademicConstants.MAX_GRADE_LEVEL) {
            return ValidationResult.error("Grade level must be at most ${AcademicConstants.MAX_GRADE_LEVEL}")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates grade description.
     */
    fun validateGradeDescription(description: String?): ValidationResult {
        if (description == null) {
            return ValidationResult.success()
        }
        
        if (description.isBlank()) {
            return ValidationResult.error("Description cannot be blank if provided")
        }
        
        if (description.length < AcademicConstants.MIN_DESCRIPTION_LENGTH) {
            return ValidationResult.error("Description must be at least ${AcademicConstants.MIN_DESCRIPTION_LENGTH} characters")
        }
        
        if (description.length > AcademicConstants.MAX_DESCRIPTION_LENGTH) {
            return ValidationResult.error("Description must be at most ${AcademicConstants.MAX_DESCRIPTION_LENGTH} characters")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates student ID.
     */
    fun validateStudentId(studentId: Long): ValidationResult {
        if (studentId <= 0) {
            return ValidationResult.error("Student ID must be positive")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates grade ID.
     */
    fun validateGradeId(gradeId: Long): ValidationResult {
        if (gradeId <= 0) {
            return ValidationResult.error("Grade ID must be positive")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates enrollment date.
     */
    fun validateEnrollmentDate(date: LocalDateTime): ValidationResult {
        val now = LocalDateTime.now()
        
        if (date.isAfter(now.plusDays(1))) {
            return ValidationResult.error("Enrollment date cannot be more than 1 day in the future")
        }
        
        if (date.isBefore(now.minusYears(10))) {
            return ValidationResult.error("Enrollment date cannot be more than 10 years in the past")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates date range.
     */
    fun validateDateRange(startDate: LocalDateTime, endDate: LocalDateTime): ValidationResult {
        if (startDate.isAfter(endDate)) {
            return ValidationResult.error("Start date cannot be after end date")
        }
        
        val duration = java.time.Duration.between(startDate, endDate)
        if (duration.toDays() > 365 * 10) { // 10 years
            return ValidationResult.error("Date range cannot exceed 10 years")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates document type.
     */
    fun validateDocumentType(documentType: String): ValidationResult {
        if (documentType.isBlank()) {
            return ValidationResult.error("Document type cannot be blank")
        }
        
        val validTypes = setOf(
            AcademicConstants.DOCUMENT_TYPE_CC,
            AcademicConstants.DOCUMENT_TYPE_TI,
            AcademicConstants.DOCUMENT_TYPE_CE,
            AcademicConstants.DOCUMENT_TYPE_PASSPORT
        )
        
        if (!validTypes.contains(documentType.uppercase())) {
            return ValidationResult.error("Invalid document type. Must be one of: ${validTypes.joinToString(", ")}")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates document number.
     */
    fun validateDocumentNumber(documentNumber: String): ValidationResult {
        if (documentNumber.isBlank()) {
            return ValidationResult.error("Document number cannot be blank")
        }
        
        if (documentNumber.length < AcademicConstants.MIN_DOCUMENT_NUMBER_LENGTH) {
            return ValidationResult.error("Document number must be at least ${AcademicConstants.MIN_DOCUMENT_NUMBER_LENGTH} characters")
        }
        
        if (documentNumber.length > AcademicConstants.MAX_DOCUMENT_NUMBER_LENGTH) {
            return ValidationResult.error("Document number must be at most ${AcademicConstants.MAX_DOCUMENT_NUMBER_LENGTH} characters")
        }
        
        if (!isValidDocumentNumberFormat(documentNumber)) {
            return ValidationResult.error("Document number contains invalid characters")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates student name.
     */
    fun validateStudentName(name: String, fieldName: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult.error("$fieldName cannot be blank")
        }
        
        if (name.length < AcademicConstants.MIN_NAME_LENGTH) {
            return ValidationResult.error("$fieldName must be at least ${AcademicConstants.MIN_NAME_LENGTH} characters")
        }
        
        if (name.length > AcademicConstants.MAX_NAME_LENGTH) {
            return ValidationResult.error("$fieldName must be at most ${AcademicConstants.MAX_NAME_LENGTH} characters")
        }
        
        if (!isValidNameFormat(name)) {
            return ValidationResult.error("$fieldName contains invalid characters")
        }
        
        return ValidationResult.success()
    }
    
    /**
     * Validates guardian ID.
     */
    fun validateGuardianId(guardianId: Long): ValidationResult {
        if (guardianId <= 0) {
            return ValidationResult.error("Guardian ID must be positive")
        }
        
        return ValidationResult.success()
    }
    
    private fun isValidGradeNameFormat(name: String): Boolean {
        // Grade names should contain only letters, numbers, spaces, and hyphens
        val pattern = Pattern.compile("^[a-zA-Z0-9\\s\\-]+$")
        return pattern.matcher(name).matches()
    }
    
    private fun isValidDocumentNumberFormat(documentNumber: String): Boolean {
        // Document numbers should contain only alphanumeric characters
        val pattern = Pattern.compile("^[a-zA-Z0-9]+$")
        return pattern.matcher(documentNumber).matches()
    }
    
    private fun isValidNameFormat(name: String): Boolean {
        // Names should contain only letters, spaces, hyphens, and apostrophes
        val pattern = Pattern.compile("^[a-zA-Z\\s\\-']+$")
        return pattern.matcher(name).matches()
    }
}

/**
 * Result of a validation operation.
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
) {
    companion object {
        fun success(): ValidationResult = ValidationResult(true)
        fun error(message: String): ValidationResult = ValidationResult(false, message)
    }
}
