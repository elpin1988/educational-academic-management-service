package co.edu.school.academic_management_service.domain.repository

import co.edu.school.academic_management_service.domain.entity.StudentGrade
import java.time.LocalDateTime

/**
 * Command repository interface for StudentGrade entity.
 * Follows Command Query Responsibility Segregation (CQRS) pattern.
 * 
 * This interface defines write operations for StudentGrade entities.
 */
interface StudentGradeCommandRepository {
    /**
     * Saves a new student grade enrollment or updates an existing one.
     * @param studentGrade The student grade enrollment to save
     * @return The saved student grade enrollment with generated ID
     */
    fun save(studentGrade: StudentGrade): StudentGrade
    
    /**
     * Updates an existing student grade enrollment.
     * @param studentGrade The student grade enrollment to update
     * @return The updated student grade enrollment
     */
    fun update(studentGrade: StudentGrade): StudentGrade
    
    /**
     * Deletes a student grade enrollment by its ID.
     * @param id The ID of the student grade enrollment to delete
     * @return true if the enrollment was deleted, false otherwise
     */
    fun deleteById(id: Long): Boolean
    
    /**
     * Deletes a student grade enrollment entity.
     * @param studentGrade The student grade enrollment to delete
     * @return true if the enrollment was deleted, false otherwise
     */
    fun delete(studentGrade: StudentGrade): Boolean
    
    /**
     * Ends a student's enrollment in a grade.
     * @param studentId The ID of the student
     * @param gradeId The ID of the grade
     * @param endDate The end date of the enrollment
     * @return The updated student grade enrollment
     */
    fun endEnrollment(studentId: Long, gradeId: Long, endDate: LocalDateTime): StudentGrade?
    
    /**
     * Transfers a student from one grade to another.
     * @param studentId The ID of the student
     * @param fromGradeId The ID of the current grade
     * @param toGradeId The ID of the new grade
     * @param transferDate The date of the transfer
     * @return The new student grade enrollment
     */
    fun transferStudent(studentId: Long, fromGradeId: Long, toGradeId: Long, transferDate: LocalDateTime): StudentGrade?
}
