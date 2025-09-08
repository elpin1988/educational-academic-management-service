package co.edu.school.academic_management_service.application.usecase

import co.edu.school.academic_management_service.application.port.output.GradeRepositoryPort
import co.edu.school.academic_management_service.application.port.output.StudentGradeRepositoryPort
import co.edu.school.academic_management_service.domain.entity.Grade
import co.edu.school.academic_management_service.domain.entity.StudentGrade
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

/**
 * Unit tests for StudentGradeManagementUseCaseImpl.
 * Tests business logic and validation rules.
 */
class StudentGradeManagementUseCaseImplTest {
    
    private lateinit var studentGradeRepositoryPort: StudentGradeRepositoryPort
    private lateinit var gradeRepositoryPort: GradeRepositoryPort
    private lateinit var studentGradeManagementUseCase: StudentGradeManagementUseCaseImpl
    
    @BeforeEach
    fun setUp() {
        studentGradeRepositoryPort = mockk()
        gradeRepositoryPort = mockk()
        studentGradeManagementUseCase = StudentGradeManagementUseCaseImpl(
            studentGradeRepositoryPort, gradeRepositoryPort
        )
    }
    
    @Test
    fun `enrollStudentInGrade should enroll student when data is valid`() {
        // Given
        val studentId = 1L
        val gradeId = 1L
        val startDate = LocalDateTime.now()
        val grade = Grade(id = gradeId, name = "First Grade", level = 1)
        val expectedStudentGrade = StudentGrade(
            id = 1L,
            studentId = studentId,
            gradeId = gradeId,
            startDate = startDate,
            endDate = null,
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        every { gradeRepositoryPort.findById(gradeId) } returns grade
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) } returns false
        every { studentGradeRepositoryPort.hasActiveEnrollment(studentId) } returns false
        every { studentGradeRepositoryPort.save(any()) } returns expectedStudentGrade
        
        // When
        val result = studentGradeManagementUseCase.enrollStudentInGrade(studentId, gradeId, startDate)
        
        // Then
        assertThat(result).isEqualTo(expectedStudentGrade)
        verify { gradeRepositoryPort.findById(gradeId) }
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) }
        verify { studentGradeRepositoryPort.hasActiveEnrollment(studentId) }
        verify { studentGradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `enrollStudentInGrade should throw exception when student is already enrolled in grade`() {
        // Given
        val studentId = 1L
        val gradeId = 1L
        val startDate = LocalDateTime.now()
        val grade = Grade(id = gradeId, name = "First Grade", level = 1)
        
        every { gradeRepositoryPort.findById(gradeId) } returns grade
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) } returns true
        
        // When & Then
        assertThatThrownBy { 
            studentGradeManagementUseCase.enrollStudentInGrade(studentId, gradeId, startDate) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student is already enrolled in this grade")
        
        verify { gradeRepositoryPort.findById(gradeId) }
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) }
        verify(exactly = 0) { studentGradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `enrollStudentInGrade should throw exception when student has active enrollment`() {
        // Given
        val studentId = 1L
        val gradeId = 1L
        val startDate = LocalDateTime.now()
        val grade = Grade(id = gradeId, name = "First Grade", level = 1)
        
        every { gradeRepositoryPort.findById(gradeId) } returns grade
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) } returns false
        every { studentGradeRepositoryPort.hasActiveEnrollment(studentId) } returns true
        
        // When & Then
        assertThatThrownBy { 
            studentGradeManagementUseCase.enrollStudentInGrade(studentId, gradeId, startDate) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student is already enrolled in another grade")
        
        verify { gradeRepositoryPort.findById(gradeId) }
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) }
        verify { studentGradeRepositoryPort.hasActiveEnrollment(studentId) }
        verify(exactly = 0) { studentGradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `enrollStudentInGrade should throw exception when grade not found`() {
        // Given
        val studentId = 1L
        val gradeId = 1L
        val startDate = LocalDateTime.now()
        
        every { gradeRepositoryPort.findById(gradeId) } returns null
        
        // When & Then
        assertThatThrownBy { 
            studentGradeManagementUseCase.enrollStudentInGrade(studentId, gradeId, startDate) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade not found")
        
        verify { gradeRepositoryPort.findById(gradeId) }
        verify(exactly = 0) { studentGradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `enrollStudentInGrade should throw exception when grade is inactive`() {
        // Given
        val studentId = 1L
        val gradeId = 1L
        val startDate = LocalDateTime.now()
        val inactiveGrade = Grade(id = gradeId, name = "First Grade", level = 1, isActive = false)
        
        every { gradeRepositoryPort.findById(gradeId) } returns inactiveGrade
        
        // When & Then
        assertThatThrownBy { 
            studentGradeManagementUseCase.enrollStudentInGrade(studentId, gradeId, startDate) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade is not active for enrollment")
        
        verify { gradeRepositoryPort.findById(gradeId) }
        verify(exactly = 0) { studentGradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `transferStudentToGrade should transfer student when data is valid`() {
        // Given
        val studentId = 1L
        val fromGradeId = 1L
        val toGradeId = 2L
        val transferDate = LocalDateTime.now()
        val fromGrade = Grade(id = fromGradeId, name = "First Grade", level = 1)
        val toGrade = Grade(id = toGradeId, name = "Second Grade", level = 2)
        val expectedStudentGrade = StudentGrade(
            id = 1L,
            studentId = studentId,
            gradeId = toGradeId,
            startDate = transferDate,
            endDate = null,
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        every { gradeRepositoryPort.findById(fromGradeId) } returns fromGrade
        every { gradeRepositoryPort.findById(toGradeId) } returns toGrade
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, fromGradeId) } returns true
        every { studentGradeRepositoryPort.endEnrollment(studentId, fromGradeId, transferDate) } returns StudentGrade(
            id = 1L, studentId = studentId, gradeId = fromGradeId, startDate = LocalDateTime.now().minusDays(30),
            endDate = transferDate, isActive = false, createdAt = LocalDateTime.now().minusDays(30),
            updatedAt = LocalDateTime.now()
        )
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, toGradeId) } returns false
        every { studentGradeRepositoryPort.hasActiveEnrollment(studentId) } returns false
        every { studentGradeRepositoryPort.save(any()) } returns expectedStudentGrade
        
        // When
        val result = studentGradeManagementUseCase.transferStudentToGrade(
            studentId, fromGradeId, toGradeId, transferDate
        )
        
        // Then
        assertThat(result).isEqualTo(expectedStudentGrade)
        verify { gradeRepositoryPort.findById(fromGradeId) }
        verify { gradeRepositoryPort.findById(toGradeId) }
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, fromGradeId) }
        verify { studentGradeRepositoryPort.endEnrollment(studentId, fromGradeId, transferDate) }
        verify { studentGradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `transferStudentToGrade should throw exception when student not enrolled in from grade`() {
        // Given
        val studentId = 1L
        val fromGradeId = 1L
        val toGradeId = 2L
        val transferDate = LocalDateTime.now()
        val fromGrade = Grade(id = fromGradeId, name = "First Grade", level = 1)
        val toGrade = Grade(id = toGradeId, name = "Second Grade", level = 2)
        
        every { gradeRepositoryPort.findById(fromGradeId) } returns fromGrade
        every { gradeRepositoryPort.findById(toGradeId) } returns toGrade
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, fromGradeId) } returns false
        
        // When & Then
        assertThatThrownBy { 
            studentGradeManagementUseCase.transferStudentToGrade(
                studentId, fromGradeId, toGradeId, transferDate
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student is not enrolled in the specified grade")
        
        verify { gradeRepositoryPort.findById(fromGradeId) }
        verify { gradeRepositoryPort.findById(toGradeId) }
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, fromGradeId) }
        verify(exactly = 0) { studentGradeRepositoryPort.endEnrollment(any(), any(), any()) }
        verify(exactly = 0) { studentGradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `endStudentEnrollment should end enrollment when student is enrolled`() {
        // Given
        val studentId = 1L
        val gradeId = 1L
        val endDate = LocalDateTime.now()
        val endedEnrollment = StudentGrade(
            id = 1L,
            studentId = studentId,
            gradeId = gradeId,
            startDate = LocalDateTime.now().minusDays(30),
            endDate = endDate,
            isActive = false,
            createdAt = LocalDateTime.now().minusDays(30),
            updatedAt = LocalDateTime.now()
        )
        
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) } returns true
        every { studentGradeRepositoryPort.endEnrollment(studentId, gradeId, endDate) } returns endedEnrollment
        
        // When
        val result = studentGradeManagementUseCase.endStudentEnrollment(studentId, gradeId, endDate)
        
        // Then
        assertThat(result).isEqualTo(endedEnrollment)
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) }
        verify { studentGradeRepositoryPort.endEnrollment(studentId, gradeId, endDate) }
    }
    
    @Test
    fun `endStudentEnrollment should throw exception when student not enrolled`() {
        // Given
        val studentId = 1L
        val gradeId = 1L
        val endDate = LocalDateTime.now()
        
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) } returns false
        
        // When & Then
        assertThatThrownBy { 
            studentGradeManagementUseCase.endStudentEnrollment(studentId, gradeId, endDate) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student is not enrolled in the specified grade")
        
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) }
        verify(exactly = 0) { studentGradeRepositoryPort.endEnrollment(any(), any(), any()) }
    }
    
    @Test
    fun `graduateStudent should graduate student when has active enrollment`() {
        // Given
        val studentId = 1L
        val graduationDate = LocalDateTime.now()
        val currentEnrollment = StudentGrade(
            id = 1L,
            studentId = studentId,
            gradeId = 1L,
            startDate = LocalDateTime.now().minusDays(30),
            endDate = null,
            isActive = true,
            createdAt = LocalDateTime.now().minusDays(30),
            updatedAt = LocalDateTime.now()
        )
        val graduatedEnrollment = currentEnrollment.copy(
            endDate = graduationDate,
            isActive = false
        )
        
        every { studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId) } returns currentEnrollment
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, currentEnrollment.gradeId) } returns true
        every { studentGradeRepositoryPort.endEnrollment(studentId, currentEnrollment.gradeId, graduationDate) } returns graduatedEnrollment
        
        // When
        val result = studentGradeManagementUseCase.graduateStudent(studentId, graduationDate)
        
        // Then
        assertThat(result).isEqualTo(graduatedEnrollment)
        verify { studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId) }
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, currentEnrollment.gradeId) }
        verify { studentGradeRepositoryPort.endEnrollment(studentId, currentEnrollment.gradeId, graduationDate) }
    }
    
    @Test
    fun `graduateStudent should throw exception when student not enrolled`() {
        // Given
        val studentId = 1L
        val graduationDate = LocalDateTime.now()
        
        every { studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId) } returns null
        
        // When & Then
        assertThatThrownBy { 
            studentGradeManagementUseCase.graduateStudent(studentId, graduationDate) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student is not enrolled in any grade")
        
        verify { studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId) }
        verify(exactly = 0) { studentGradeRepositoryPort.endEnrollment(any(), any(), any()) }
    }
    
    @Test
    fun `removeEnrollment should remove enrollment when not active`() {
        // Given
        val enrollmentId = 1L
        val inactiveEnrollment = StudentGrade(
            id = enrollmentId,
            studentId = 1L,
            gradeId = 1L,
            startDate = LocalDateTime.now().minusDays(30),
            endDate = LocalDateTime.now().minusDays(1),
            isActive = false,
            createdAt = LocalDateTime.now().minusDays(30),
            updatedAt = LocalDateTime.now()
        )
        
        every { studentGradeRepositoryPort.findById(enrollmentId) } returns inactiveEnrollment
        every { studentGradeRepositoryPort.deleteById(enrollmentId) } returns true
        
        // When
        val result = studentGradeManagementUseCase.removeEnrollment(enrollmentId)
        
        // Then
        assertThat(result).isTrue()
        verify { studentGradeRepositoryPort.findById(enrollmentId) }
        verify { studentGradeRepositoryPort.deleteById(enrollmentId) }
    }
    
    @Test
    fun `removeEnrollment should throw exception when enrollment is active`() {
        // Given
        val enrollmentId = 1L
        val activeEnrollment = StudentGrade(
            id = enrollmentId,
            studentId = 1L,
            gradeId = 1L,
            startDate = LocalDateTime.now().minusDays(30),
            endDate = null,
            isActive = true,
            createdAt = LocalDateTime.now().minusDays(30),
            updatedAt = LocalDateTime.now()
        )
        
        every { studentGradeRepositoryPort.findById(enrollmentId) } returns activeEnrollment
        
        // When & Then
        assertThatThrownBy { 
            studentGradeManagementUseCase.removeEnrollment(enrollmentId) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot remove active enrollment. End it first.")
        
        verify { studentGradeRepositoryPort.findById(enrollmentId) }
        verify(exactly = 0) { studentGradeRepositoryPort.deleteById(any()) }
    }
}
