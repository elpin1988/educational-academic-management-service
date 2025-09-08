package co.edu.school.academic_management_service.application.port.input

import co.edu.school.academic_management_service.domain.entity.StudentGrade
import java.time.LocalDateTime

/**
 * Input port for student grade management use cases.
 * Follows the Dependency Inversion Principle.
 * 
 * This interface defines the business operations for managing student grade enrollments.
 */
interface StudentGradeManagementUseCase {
    /**
     * Enrolls a student in a grade.
     * @param studentId The ID of the student
     * @param gradeId The ID of the grade
     * @param startDate The start date of the enrollment
     * @return The created student grade enrollment
     */
    fun enrollStudentInGrade(
        studentId: Long, 
        gradeId: Long, 
        startDate: LocalDateTime = LocalDateTime.now()
    ): StudentGrade
    
    /**
     * Transfers a student from one grade to another.
     * @param studentId The ID of the student
     * @param fromGradeId The ID of the current grade
     * @param toGradeId The ID of the new grade
     * @param transferDate The date of the transfer
     * @return The new student grade enrollment
     */
    fun transferStudentToGrade(
        studentId: Long, 
        fromGradeId: Long, 
        toGradeId: Long, 
        transferDate: LocalDateTime = LocalDateTime.now()
    ): StudentGrade
    
    /**
     * Ends a student's enrollment in a grade.
     * @param studentId The ID of the student
     * @param gradeId The ID of the grade
     * @param endDate The end date of the enrollment
     * @return The updated student grade enrollment
     */
    fun endStudentEnrollment(
        studentId: Long, 
        gradeId: Long, 
        endDate: LocalDateTime = LocalDateTime.now()
    ): StudentGrade?
    
    /**
     * Graduates a student from their current grade.
     * @param studentId The ID of the student
     * @param graduationDate The graduation date
     * @return The updated student grade enrollment
     */
    fun graduateStudent(studentId: Long, graduationDate: LocalDateTime = LocalDateTime.now()): StudentGrade?
    
    /**
     * Removes a student grade enrollment.
     * @param enrollmentId The ID of the enrollment to remove
     * @return true if the enrollment was removed, false otherwise
     */
    fun removeEnrollment(enrollmentId: Long): Boolean
}
