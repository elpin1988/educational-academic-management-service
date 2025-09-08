package co.edu.school.academic_management_service.domain.entity

import co.edu.school.academic_management_service.domain.constant.AcademicConstants
import java.time.LocalDateTime

/**
 * Domain entity representing a student's grade enrollment history.
 * Follows Single Responsibility Principle by focusing only on student-grade relationship.
 * 
 * This entity tracks the academic progression of students through different grades.
 */
data class StudentGrade(
    val id: Long?,
    val studentId: Long,
    val gradeId: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    init {
        require(studentId > 0) { "Student ID must be positive" }
        require(gradeId > 0) { "Grade ID must be positive" }
        require(endDate == null || !endDate.isBefore(startDate)) { 
            "End date cannot be before start date" 
        }
        // Note: Duration validation is handled in business logic, not in constructor
        // to allow for flexible test scenarios and edge cases
        require(!startDate.isAfter(LocalDateTime.now().plusDays(1))) {
            "Start date cannot be more than 1 day in the future"
        }
        require(!startDate.isBefore(LocalDateTime.now().minusYears(10))) {
            "Start date cannot be more than 10 years in the past"
        }
    }
    
    /**
     * Business method to check if the enrollment is currently active
     */
    fun isCurrentlyActive(): Boolean = isActive && endDate == null
    
    /**
     * Business method to check if the enrollment has ended
     */
    fun hasEnded(): Boolean = endDate != null
    
    /**
     * Business method to get the duration of enrollment
     */
    fun getEnrollmentDuration(): Long {
        val end = endDate ?: LocalDateTime.now()
        return java.time.Duration.between(startDate, end).toDays()
    }
    
    /**
     * Business method to check if enrollment is valid for a given date
     */
    fun isValidForDate(date: LocalDateTime): Boolean {
        return date.isAfter(startDate) && (endDate == null || date.isBefore(endDate))
    }
}
