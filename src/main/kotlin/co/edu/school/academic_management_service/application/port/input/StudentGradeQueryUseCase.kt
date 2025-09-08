package co.edu.school.academic_management_service.application.port.input

import co.edu.school.academic_management_service.domain.entity.StudentGrade
import java.time.LocalDateTime

/**
 * Input port for student grade query use cases.
 * Follows the Dependency Inversion Principle.
 * 
 * This interface defines the query operations for student grade enrollments.
 */
interface StudentGradeQueryUseCase {
    /**
     * Gets a student grade enrollment by its ID.
     * @param id The ID of the enrollment
     * @return The enrollment if found, null otherwise
     */
    fun getEnrollmentById(id: Long): StudentGrade?
    
    /**
     * Gets all grade enrollments for a specific student.
     * @param studentId The ID of the student
     * @return List of all enrollments for the student
     */
    fun getEnrollmentsByStudentId(studentId: Long): List<StudentGrade>
    
    /**
     * Gets all student enrollments for a specific grade.
     * @param gradeId The ID of the grade
     * @return List of all enrollments for the grade
     */
    fun getEnrollmentsByGradeId(gradeId: Long): List<StudentGrade>
    
    /**
     * Gets the current active enrollment for a student.
     * @param studentId The ID of the student
     * @return The current active enrollment if found, null otherwise
     */
    fun getCurrentEnrollmentByStudentId(studentId: Long): StudentGrade?
    
    /**
     * Gets all active enrollments for a specific grade.
     * @param gradeId The ID of the grade
     * @return List of all active enrollments for the grade
     */
    fun getActiveEnrollmentsByGradeId(gradeId: Long): List<StudentGrade>
    
    /**
     * Gets all active enrollments.
     * @return List of all active enrollments
     */
    fun getAllActiveEnrollments(): List<StudentGrade>
    
    /**
     * Gets all enrollments that were active on a specific date.
     * @param date The date to check
     * @return List of all enrollments active on the date
     */
    fun getEnrollmentsActiveOnDate(date: LocalDateTime): List<StudentGrade>
    
    /**
     * Gets all enrollments for a student within a date range.
     * @param studentId The ID of the student
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of all enrollments within the date range
     */
    fun getEnrollmentsByStudentIdAndDateRange(
        studentId: Long, 
        startDate: LocalDateTime, 
        endDate: LocalDateTime
    ): List<StudentGrade>
    
    /**
     * Checks if a student is currently enrolled in a specific grade.
     * @param studentId The ID of the student
     * @param gradeId The ID of the grade
     * @return true if the student is enrolled, false otherwise
     */
    fun isStudentEnrolledInGrade(studentId: Long, gradeId: Long): Boolean
    
    /**
     * Checks if a student has any active enrollment.
     * @param studentId The ID of the student
     * @return true if the student has an active enrollment, false otherwise
     */
    fun hasActiveEnrollment(studentId: Long): Boolean
    
    /**
     * Gets the count of active enrollments for a specific grade.
     * @param gradeId The ID of the grade
     * @return The count of active enrollments
     */
    fun getActiveEnrollmentCountByGradeId(gradeId: Long): Long
    
    /**
     * Gets the total count of enrollments for a student.
     * @param studentId The ID of the student
     * @return The total count of enrollments
     */
    fun getEnrollmentCountByStudentId(studentId: Long): Long
}
