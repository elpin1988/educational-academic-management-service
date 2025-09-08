package co.edu.school.academic_management_service.application.usecase

import co.edu.school.academic_management_service.application.port.input.StudentGradeQueryUseCase
import co.edu.school.academic_management_service.application.port.output.StudentGradeRepositoryPort
import co.edu.school.academic_management_service.domain.entity.StudentGrade
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Implementation of student grade query use cases.
 * Follows Single Responsibility Principle and contains query logic.
 */
@Service
class StudentGradeQueryUseCaseImpl(
    private val studentGradeRepositoryPort: StudentGradeRepositoryPort
) : StudentGradeQueryUseCase {
    
    override fun getEnrollmentById(id: Long): StudentGrade? {
        return studentGradeRepositoryPort.findById(id)
    }
    
    override fun getEnrollmentsByStudentId(studentId: Long): List<StudentGrade> {
        require(studentId > 0) { "Student ID must be positive" }
        return studentGradeRepositoryPort.findByStudentId(studentId)
    }
    
    override fun getEnrollmentsByGradeId(gradeId: Long): List<StudentGrade> {
        require(gradeId > 0) { "Grade ID must be positive" }
        return studentGradeRepositoryPort.findByGradeId(gradeId)
    }
    
    override fun getCurrentEnrollmentByStudentId(studentId: Long): StudentGrade? {
        require(studentId > 0) { "Student ID must be positive" }
        return studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId)
    }
    
    override fun getActiveEnrollmentsByGradeId(gradeId: Long): List<StudentGrade> {
        require(gradeId > 0) { "Grade ID must be positive" }
        return studentGradeRepositoryPort.findActiveEnrollmentsByGradeId(gradeId)
    }
    
    override fun getAllActiveEnrollments(): List<StudentGrade> {
        return studentGradeRepositoryPort.findAllActiveEnrollments()
    }
    
    override fun getEnrollmentsActiveOnDate(date: LocalDateTime): List<StudentGrade> {
        require(!date.isAfter(LocalDateTime.now())) { 
            "Date cannot be in the future" 
        }
        return studentGradeRepositoryPort.findEnrollmentsActiveOnDate(date)
    }
    
    override fun getEnrollmentsByStudentIdAndDateRange(
        studentId: Long, 
        startDate: LocalDateTime, 
        endDate: LocalDateTime
    ): List<StudentGrade> {
        require(studentId > 0) { "Student ID must be positive" }
        require(!startDate.isAfter(endDate)) { "Start date cannot be after end date" }
        require(startDate.isBefore(LocalDateTime.now().plusDays(1))) { 
            "Start date cannot be in the future" 
        }
        
        return studentGradeRepositoryPort.findEnrollmentsByStudentIdAndDateRange(
            studentId, startDate, endDate
        )
    }
    
    override fun isStudentEnrolledInGrade(studentId: Long, gradeId: Long): Boolean {
        require(studentId > 0) { "Student ID must be positive" }
        require(gradeId > 0) { "Grade ID must be positive" }
        return studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId)
    }
    
    override fun hasActiveEnrollment(studentId: Long): Boolean {
        require(studentId > 0) { "Student ID must be positive" }
        return studentGradeRepositoryPort.hasActiveEnrollment(studentId)
    }
    
    override fun getActiveEnrollmentCountByGradeId(gradeId: Long): Long {
        require(gradeId > 0) { "Grade ID must be positive" }
        return studentGradeRepositoryPort.countActiveEnrollmentsByGradeId(gradeId)
    }
    
    override fun getEnrollmentCountByStudentId(studentId: Long): Long {
        require(studentId > 0) { "Student ID must be positive" }
        return studentGradeRepositoryPort.countEnrollmentsByStudentId(studentId)
    }
}
