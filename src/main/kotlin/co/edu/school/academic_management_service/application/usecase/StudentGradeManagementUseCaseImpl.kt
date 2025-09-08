package co.edu.school.academic_management_service.application.usecase

import co.edu.school.academic_management_service.application.port.input.StudentGradeManagementUseCase
import co.edu.school.academic_management_service.application.port.output.GradeRepositoryPort
import co.edu.school.academic_management_service.application.port.output.StudentGradeRepositoryPort
import co.edu.school.academic_management_service.domain.constant.AcademicConstants
import co.edu.school.academic_management_service.domain.entity.StudentGrade
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Implementation of student grade management use cases.
 * Follows Single Responsibility Principle and contains business logic.
 */
@Service
class StudentGradeManagementUseCaseImpl(
    private val studentGradeRepositoryPort: StudentGradeRepositoryPort,
    private val gradeRepositoryPort: GradeRepositoryPort
) : StudentGradeManagementUseCase {
    
    override fun enrollStudentInGrade(
        studentId: Long, 
        gradeId: Long, 
        startDate: LocalDateTime
    ): StudentGrade {
        // Validate business rules
        validateEnrollment(studentId, gradeId, startDate)
        
        // Check if student is already enrolled in this grade
        if (studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId)) {
            throw IllegalArgumentException(AcademicConstants.ERROR_STUDENT_ALREADY_ENROLLED)
        }
        
        // Check if student has any active enrollment
        if (studentGradeRepositoryPort.hasActiveEnrollment(studentId)) {
            throw IllegalArgumentException("Student is already enrolled in another grade")
        }
        
        val studentGrade = StudentGrade(
            id = null,
            studentId = studentId,
            gradeId = gradeId,
            startDate = startDate,
            endDate = null,
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        return studentGradeRepositoryPort.save(studentGrade)
    }
    
    override fun transferStudentToGrade(
        studentId: Long, 
        fromGradeId: Long, 
        toGradeId: Long, 
        transferDate: LocalDateTime
    ): StudentGrade {
        // Validate business rules
        validateTransfer(studentId, fromGradeId, toGradeId, transferDate)
        
        // Check if student is currently enrolled in the from grade
        if (!studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, fromGradeId)) {
            throw IllegalArgumentException("Student is not enrolled in the specified grade")
        }
        
        // End current enrollment
        studentGradeRepositoryPort.endEnrollment(studentId, fromGradeId, transferDate)
        
        // Create new enrollment
        return enrollStudentInGrade(studentId, toGradeId, transferDate)
    }
    
    override fun endStudentEnrollment(
        studentId: Long, 
        gradeId: Long, 
        endDate: LocalDateTime
    ): StudentGrade? {
        // Validate business rules
        validateEndEnrollment(studentId, gradeId, endDate)
        
        // Check if student is enrolled in the grade
        if (!studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId)) {
            throw IllegalArgumentException("Student is not enrolled in the specified grade")
        }
        
        return studentGradeRepositoryPort.endEnrollment(studentId, gradeId, endDate)
    }
    
    override fun graduateStudent(studentId: Long, graduationDate: LocalDateTime): StudentGrade? {
        // Get current enrollment
        val currentEnrollment = studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId)
            ?: throw IllegalArgumentException(AcademicConstants.ERROR_STUDENT_NOT_ENROLLED)
        
        // End current enrollment
        return endStudentEnrollment(studentId, currentEnrollment.gradeId, graduationDate)
    }
    
    override fun removeEnrollment(enrollmentId: Long): Boolean {
        val enrollment = studentGradeRepositoryPort.findById(enrollmentId)
            ?: throw IllegalArgumentException(AcademicConstants.ERROR_STUDENT_GRADE_NOT_FOUND)
        
        // Business rule: Cannot remove active enrollments
        if (enrollment.isCurrentlyActive()) {
            throw IllegalArgumentException("Cannot remove active enrollment. End it first.")
        }
        
        return studentGradeRepositoryPort.deleteById(enrollmentId)
    }
    
    private fun validateEnrollment(studentId: Long, gradeId: Long, startDate: LocalDateTime) {
        require(studentId > 0) { "Student ID must be positive" }
        require(gradeId > 0) { "Grade ID must be positive" }
        require(startDate.isBefore(LocalDateTime.now().plusDays(1))) { 
            "Start date cannot be in the future" 
        }
        
        // Check if grade exists and is active
        val grade = gradeRepositoryPort.findById(gradeId)
            ?: throw IllegalArgumentException(AcademicConstants.ERROR_GRADE_NOT_FOUND)
        
        if (!grade.isValidForEnrollment()) {
            throw IllegalArgumentException(AcademicConstants.ERROR_GRADE_INACTIVE)
        }
    }
    
    private fun validateTransfer(studentId: Long, fromGradeId: Long, toGradeId: Long, transferDate: LocalDateTime) {
        require(studentId > 0) { "Student ID must be positive" }
        require(fromGradeId > 0) { "From grade ID must be positive" }
        require(toGradeId > 0) { "To grade ID must be positive" }
        require(fromGradeId != toGradeId) { "Cannot transfer to the same grade" }
        require(transferDate.isBefore(LocalDateTime.now().plusDays(1))) { 
            "Transfer date cannot be in the future" 
        }
        
        // Check if both grades exist and are active
        gradeRepositoryPort.findById(fromGradeId)
            ?: throw IllegalArgumentException("From grade not found")
        
        val toGrade = gradeRepositoryPort.findById(toGradeId)
            ?: throw IllegalArgumentException("To grade not found")
        
        if (!toGrade.isValidForEnrollment()) {
            throw IllegalArgumentException("To grade is not active for enrollment")
        }
    }
    
    private fun validateEndEnrollment(studentId: Long, gradeId: Long, endDate: LocalDateTime) {
        require(studentId > 0) { "Student ID must be positive" }
        require(gradeId > 0) { "Grade ID must be positive" }
        require(endDate.isBefore(LocalDateTime.now().plusDays(1))) { 
            "End date cannot be in the future" 
        }
    }
}
