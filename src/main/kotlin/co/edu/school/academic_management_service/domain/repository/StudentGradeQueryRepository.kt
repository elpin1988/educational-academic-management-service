package co.edu.school.academic_management_service.domain.repository

import co.edu.school.academic_management_service.domain.entity.StudentGrade
import java.time.LocalDateTime

/**
 * Query repository interface for StudentGrade entity.
 * Follows Command Query Responsibility Segregation (CQRS) pattern.
 * 
 * This interface defines read operations for StudentGrade entities.
 */
interface StudentGradeQueryRepository {
    /**
     * Finds a student grade enrollment by its ID.
     * @param id The ID of the student grade enrollment
     * @return The student grade enrollment if found, null otherwise
     */
    fun findById(id: Long): StudentGrade?
    
    /**
     * Finds all grade enrollments for a specific student.
     * @param studentId The ID of the student
     * @return List of all grade enrollments for the student
     */
    fun findByStudentId(studentId: Long): List<StudentGrade>
    
    /**
     * Finds all student enrollments for a specific grade.
     * @param gradeId The ID of the grade
     * @return List of all student enrollments for the grade
     */
    fun findByGradeId(gradeId: Long): List<StudentGrade>
    
    /**
     * Finds the current active enrollment for a student.
     * @param studentId The ID of the student
     * @return The current active enrollment if found, null otherwise
     */
    fun findCurrentEnrollmentByStudentId(studentId: Long): StudentGrade?
    
    /**
     * Finds all active enrollments for a specific grade.
     * @param gradeId The ID of the grade
     * @return List of all active enrollments for the grade
     */
    fun findActiveEnrollmentsByGradeId(gradeId: Long): List<StudentGrade>
    
    /**
     * Finds all active enrollments.
     * @return List of all active enrollments
     */
    fun findAllActiveEnrollments(): List<StudentGrade>
    
    /**
     * Finds all enrollments that were active on a specific date.
     * @param date The date to check
     * @return List of all enrollments active on the date
     */
    fun findEnrollmentsActiveOnDate(date: LocalDateTime): List<StudentGrade>
    
    /**
     * Finds all enrollments for a student within a date range.
     * @param studentId The ID of the student
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of all enrollments within the date range
     */
    fun findEnrollmentsByStudentIdAndDateRange(
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
     * Counts the number of active enrollments for a specific grade.
     * @param gradeId The ID of the grade
     * @return The count of active enrollments
     */
    fun countActiveEnrollmentsByGradeId(gradeId: Long): Long
    
    /**
     * Counts the total number of enrollments for a student.
     * @param studentId The ID of the student
     * @return The total count of enrollments
     */
    fun countEnrollmentsByStudentId(studentId: Long): Long
}
