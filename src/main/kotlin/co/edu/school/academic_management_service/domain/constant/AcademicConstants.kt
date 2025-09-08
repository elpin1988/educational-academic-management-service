package co.edu.school.academic_management_service.domain.constant

/**
 * Constants for the academic management domain.
 * Contains business rules and validation constants.
 */
object AcademicConstants {
    
    // Grade level constraints
    const val MIN_GRADE_LEVEL = 1
    const val MAX_GRADE_LEVEL = 12
    
    // Name length constraints
    const val MIN_NAME_LENGTH = 2
    const val MAX_NAME_LENGTH = 100
    const val MIN_DESCRIPTION_LENGTH = 10
    const val MAX_DESCRIPTION_LENGTH = 500
    
    // Document constraints
    const val MIN_DOCUMENT_NUMBER_LENGTH = 5
    const val MAX_DOCUMENT_NUMBER_LENGTH = 20
    
    // Academic year constraints
    const val ACADEMIC_YEAR_MONTHS = 10 // Typical academic year duration
    const val MIN_ENROLLMENT_DURATION_DAYS = 30 // Minimum enrollment duration
    
    // Status values
    const val STATUS_ACTIVE = "ACTIVE"
    const val STATUS_INACTIVE = "INACTIVE"
    const val STATUS_GRADUATED = "GRADUATED"
    const val STATUS_TRANSFERRED = "TRANSFERRED"
    
    // Document types
    const val DOCUMENT_TYPE_CC = "CC" // Cédula de Ciudadanía
    const val DOCUMENT_TYPE_TI = "TI" // Tarjeta de Identidad
    const val DOCUMENT_TYPE_CE = "CE" // Cédula de Extranjería
    const val DOCUMENT_TYPE_PASSPORT = "PASSPORT"
    
    // Grade names
    const val GRADE_PRESCHOOL = "PRESCHOOL"
    const val GRADE_KINDERGARTEN = "KINDERGARTEN"
    const val GRADE_FIRST = "FIRST"
    const val GRADE_SECOND = "SECOND"
    const val GRADE_THIRD = "THIRD"
    const val GRADE_FOURTH = "FOURTH"
    const val GRADE_FIFTH = "FIFTH"
    const val GRADE_SIXTH = "SIXTH"
    const val GRADE_SEVENTH = "SEVENTH"
    const val GRADE_EIGHTH = "EIGHTH"
    const val GRADE_NINTH = "NINTH"
    const val GRADE_TENTH = "TENTH"
    const val GRADE_ELEVENTH = "ELEVENTH"
    const val GRADE_TWELFTH = "TWELFTH"
    
    // Error messages
    const val ERROR_GRADE_NOT_FOUND = "Grade not found"
    const val ERROR_STUDENT_NOT_FOUND = "Student not found"
    const val ERROR_STUDENT_GRADE_NOT_FOUND = "Student grade enrollment not found"
    const val ERROR_INVALID_GRADE_LEVEL = "Invalid grade level"
    const val ERROR_STUDENT_ALREADY_ENROLLED = "Student is already enrolled in this grade"
    const val ERROR_STUDENT_NOT_ENROLLED = "Student is not enrolled in any grade"
    const val ERROR_INVALID_ENROLLMENT_DATE = "Invalid enrollment date"
    const val ERROR_GRADE_INACTIVE = "Grade is not active for enrollment"
}
