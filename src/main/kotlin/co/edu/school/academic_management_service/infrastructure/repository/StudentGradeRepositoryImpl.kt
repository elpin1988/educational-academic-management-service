package co.edu.school.academic_management_service.infrastructure.repository

import co.edu.school.academic_management_service.application.port.output.StudentGradeRepositoryPort
import co.edu.school.academic_management_service.domain.entity.StudentGrade
import co.edu.school.academic_management_service.domain.repository.StudentGradeCommandRepository
import co.edu.school.academic_management_service.domain.repository.StudentGradeQueryRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * Implementation of StudentGradeRepositoryPort.
 * This is an adapter that implements the output port interface.
 * 
 * For now, this is a stub implementation. In a real scenario,
 * this would delegate to the actual repository implementations.
 */
@Repository
class StudentGradeRepositoryImpl(
    private val studentGradeCommandRepository: StudentGradeCommandRepository,
    private val studentGradeQueryRepository: StudentGradeQueryRepository
) : StudentGradeRepositoryPort {
    
    override fun save(studentGrade: StudentGrade): StudentGrade = 
        studentGradeCommandRepository.save(studentGrade)
    
    override fun update(studentGrade: StudentGrade): StudentGrade = 
        studentGradeCommandRepository.update(studentGrade)
    
    override fun deleteById(id: Long): Boolean = 
        studentGradeCommandRepository.deleteById(id)
    
    override fun findById(id: Long): StudentGrade? = 
        studentGradeQueryRepository.findById(id)
    
    override fun findByStudentId(studentId: Long): List<StudentGrade> = 
        studentGradeQueryRepository.findByStudentId(studentId)
    
    override fun findByGradeId(gradeId: Long): List<StudentGrade> = 
        studentGradeQueryRepository.findByGradeId(gradeId)
    
    override fun findCurrentEnrollmentByStudentId(studentId: Long): StudentGrade? = 
        studentGradeQueryRepository.findCurrentEnrollmentByStudentId(studentId)
    
    override fun findActiveEnrollmentsByGradeId(gradeId: Long): List<StudentGrade> = 
        studentGradeQueryRepository.findActiveEnrollmentsByGradeId(gradeId)
    
    override fun findAllActiveEnrollments(): List<StudentGrade> = 
        studentGradeQueryRepository.findAllActiveEnrollments()
    
    override fun findEnrollmentsActiveOnDate(date: LocalDateTime): List<StudentGrade> = 
        studentGradeQueryRepository.findEnrollmentsActiveOnDate(date)
    
    override fun findEnrollmentsByStudentIdAndDateRange(
        studentId: Long, 
        startDate: LocalDateTime, 
        endDate: LocalDateTime
    ): List<StudentGrade> = 
        studentGradeQueryRepository.findEnrollmentsByStudentIdAndDateRange(
            studentId, startDate, endDate
        )
    
    override fun isStudentEnrolledInGrade(studentId: Long, gradeId: Long): Boolean = 
        studentGradeQueryRepository.isStudentEnrolledInGrade(studentId, gradeId)
    
    override fun hasActiveEnrollment(studentId: Long): Boolean = 
        studentGradeQueryRepository.hasActiveEnrollment(studentId)
    
    override fun endEnrollment(studentId: Long, gradeId: Long, endDate: LocalDateTime): StudentGrade? = 
        studentGradeCommandRepository.endEnrollment(studentId, gradeId, endDate)
    
    override fun transferStudent(
        studentId: Long, 
        fromGradeId: Long, 
        toGradeId: Long, 
        transferDate: LocalDateTime
    ): StudentGrade? = 
        studentGradeCommandRepository.transferStudent(studentId, fromGradeId, toGradeId, transferDate)
    
    override fun countActiveEnrollmentsByGradeId(gradeId: Long): Long = 
        studentGradeQueryRepository.countActiveEnrollmentsByGradeId(gradeId)
    
    override fun countEnrollmentsByStudentId(studentId: Long): Long = 
        studentGradeQueryRepository.countEnrollmentsByStudentId(studentId)
}
