package co.edu.school.academic_management_service.application.usecase

import co.edu.school.academic_management_service.application.port.output.StudentGradeRepositoryPort
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
 * Unit tests for StudentGradeQueryUseCaseImpl.
 * Tests query logic and validation rules.
 */
class StudentGradeQueryUseCaseImplTest {
    
    private lateinit var studentGradeRepositoryPort: StudentGradeRepositoryPort
    private lateinit var studentGradeQueryUseCase: StudentGradeQueryUseCaseImpl
    
    @BeforeEach
    fun setUp() {
        studentGradeRepositoryPort = mockk()
        studentGradeQueryUseCase = StudentGradeQueryUseCaseImpl(studentGradeRepositoryPort)
    }
    
    @Test
    fun `getEnrollmentById should return enrollment when found`() {
        // Given
        val enrollmentId = 1L
        val enrollment = StudentGrade(
            id = enrollmentId,
            studentId = 1L,
            gradeId = 1L,
            startDate = LocalDateTime.now().minusDays(30),
            endDate = null,
            isActive = true,
            createdAt = LocalDateTime.now().minusDays(30),
            updatedAt = LocalDateTime.now()
        )
        
        every { studentGradeRepositoryPort.findById(enrollmentId) } returns enrollment
        
        // When
        val result = studentGradeQueryUseCase.getEnrollmentById(enrollmentId)
        
        // Then
        assertThat(result).isEqualTo(enrollment)
        verify { studentGradeRepositoryPort.findById(enrollmentId) }
    }
    
    @Test
    fun `getEnrollmentById should return null when not found`() {
        // Given
        val enrollmentId = 1L
        
        every { studentGradeRepositoryPort.findById(enrollmentId) } returns null
        
        // When
        val result = studentGradeQueryUseCase.getEnrollmentById(enrollmentId)
        
        // Then
        assertThat(result).isNull()
        verify { studentGradeRepositoryPort.findById(enrollmentId) }
    }
    
    @Test
    fun `getEnrollmentsByStudentId should return enrollments when found`() {
        // Given
        val studentId = 1L
        val enrollments = listOf(
            StudentGrade(
                id = 1L,
                studentId = studentId,
                gradeId = 1L,
                startDate = LocalDateTime.now().minusDays(60),
                endDate = LocalDateTime.now().minusDays(30),
                isActive = false,
                createdAt = LocalDateTime.now().minusDays(60),
                updatedAt = LocalDateTime.now().minusDays(30)
            ),
            StudentGrade(
                id = 2L,
                studentId = studentId,
                gradeId = 2L,
                startDate = LocalDateTime.now().minusDays(30),
                endDate = null,
                isActive = true,
                createdAt = LocalDateTime.now().minusDays(30),
                updatedAt = LocalDateTime.now()
            )
        )
        
        every { studentGradeRepositoryPort.findByStudentId(studentId) } returns enrollments
        
        // When
        val result = studentGradeQueryUseCase.getEnrollmentsByStudentId(studentId)
        
        // Then
        assertThat(result).isEqualTo(enrollments)
        verify { studentGradeRepositoryPort.findByStudentId(studentId) }
    }
    
    @Test
    fun `getEnrollmentsByStudentId should throw exception when studentId is invalid`() {
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.getEnrollmentsByStudentId(0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student ID must be positive")
        
        verify(exactly = 0) { studentGradeRepositoryPort.findByStudentId(any()) }
    }
    
    @Test
    fun `getEnrollmentsByGradeId should return enrollments when found`() {
        // Given
        val gradeId = 1L
        val enrollments = listOf(
            StudentGrade(
                id = 1L,
                studentId = 1L,
                gradeId = gradeId,
                startDate = LocalDateTime.now().minusDays(30),
                endDate = null,
                isActive = true,
                createdAt = LocalDateTime.now().minusDays(30),
                updatedAt = LocalDateTime.now()
            )
        )
        
        every { studentGradeRepositoryPort.findByGradeId(gradeId) } returns enrollments
        
        // When
        val result = studentGradeQueryUseCase.getEnrollmentsByGradeId(gradeId)
        
        // Then
        assertThat(result).isEqualTo(enrollments)
        verify { studentGradeRepositoryPort.findByGradeId(gradeId) }
    }
    
    @Test
    fun `getEnrollmentsByGradeId should throw exception when gradeId is invalid`() {
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.getEnrollmentsByGradeId(0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade ID must be positive")
        
        verify(exactly = 0) { studentGradeRepositoryPort.findByGradeId(any()) }
    }
    
    @Test
    fun `getCurrentEnrollmentByStudentId should return current enrollment when found`() {
        // Given
        val studentId = 1L
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
        
        every { studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId) } returns currentEnrollment
        
        // When
        val result = studentGradeQueryUseCase.getCurrentEnrollmentByStudentId(studentId)
        
        // Then
        assertThat(result).isEqualTo(currentEnrollment)
        verify { studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId) }
    }
    
    @Test
    fun `getCurrentEnrollmentByStudentId should return null when not found`() {
        // Given
        val studentId = 1L
        
        every { studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId) } returns null
        
        // When
        val result = studentGradeQueryUseCase.getCurrentEnrollmentByStudentId(studentId)
        
        // Then
        assertThat(result).isNull()
        verify { studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(studentId) }
    }
    
    @Test
    fun `getCurrentEnrollmentByStudentId should throw exception when studentId is invalid`() {
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.getCurrentEnrollmentByStudentId(0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student ID must be positive")
        
        verify(exactly = 0) { studentGradeRepositoryPort.findCurrentEnrollmentByStudentId(any()) }
    }
    
    @Test
    fun `getActiveEnrollmentsByGradeId should return active enrollments when found`() {
        // Given
        val gradeId = 1L
        val activeEnrollments = listOf(
            StudentGrade(
                id = 1L,
                studentId = 1L,
                gradeId = gradeId,
                startDate = LocalDateTime.now().minusDays(30),
                endDate = null,
                isActive = true,
                createdAt = LocalDateTime.now().minusDays(30),
                updatedAt = LocalDateTime.now()
            )
        )
        
        every { studentGradeRepositoryPort.findActiveEnrollmentsByGradeId(gradeId) } returns activeEnrollments
        
        // When
        val result = studentGradeQueryUseCase.getActiveEnrollmentsByGradeId(gradeId)
        
        // Then
        assertThat(result).isEqualTo(activeEnrollments)
        verify { studentGradeRepositoryPort.findActiveEnrollmentsByGradeId(gradeId) }
    }
    
    @Test
    fun `getActiveEnrollmentsByGradeId should throw exception when gradeId is invalid`() {
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.getActiveEnrollmentsByGradeId(0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade ID must be positive")
        
        verify(exactly = 0) { studentGradeRepositoryPort.findActiveEnrollmentsByGradeId(any()) }
    }
    
    @Test
    fun `getAllActiveEnrollments should return all active enrollments`() {
        // Given
        val activeEnrollments = listOf(
            StudentGrade(
                id = 1L,
                studentId = 1L,
                gradeId = 1L,
                startDate = LocalDateTime.now().minusDays(30),
                endDate = null,
                isActive = true,
                createdAt = LocalDateTime.now().minusDays(30),
                updatedAt = LocalDateTime.now()
            )
        )
        
        every { studentGradeRepositoryPort.findAllActiveEnrollments() } returns activeEnrollments
        
        // When
        val result = studentGradeQueryUseCase.getAllActiveEnrollments()
        
        // Then
        assertThat(result).isEqualTo(activeEnrollments)
        verify { studentGradeRepositoryPort.findAllActiveEnrollments() }
    }
    
    @Test
    fun `getEnrollmentsActiveOnDate should return enrollments active on date`() {
        // Given
        val date = LocalDateTime.now().minusDays(15)
        val enrollments = listOf(
            StudentGrade(
                id = 1L,
                studentId = 1L,
                gradeId = 1L,
                startDate = LocalDateTime.now().minusDays(30),
                endDate = null,
                isActive = true,
                createdAt = LocalDateTime.now().minusDays(30),
                updatedAt = LocalDateTime.now()
            )
        )
        
        every { studentGradeRepositoryPort.findEnrollmentsActiveOnDate(date) } returns enrollments
        
        // When
        val result = studentGradeQueryUseCase.getEnrollmentsActiveOnDate(date)
        
        // Then
        assertThat(result).isEqualTo(enrollments)
        verify { studentGradeRepositoryPort.findEnrollmentsActiveOnDate(date) }
    }
    
    @Test
    fun `getEnrollmentsActiveOnDate should throw exception when date is in future`() {
        // Given
        val futureDate = LocalDateTime.now().plusDays(1)
        
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.getEnrollmentsActiveOnDate(futureDate) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Date cannot be in the future")
        
        // Note: The validation happens before reaching the repository, so no verification needed
        verify(exactly = 0) { studentGradeRepositoryPort.findEnrollmentsActiveOnDate(any()) }
    }
    
    @Test
    fun `getEnrollmentsByStudentIdAndDateRange should return enrollments in date range`() {
        // Given
        val studentId = 1L
        val startDate = LocalDateTime.now().minusDays(60)
        val endDate = LocalDateTime.now().minusDays(30)
        val enrollments = listOf(
            StudentGrade(
                id = 1L,
                studentId = studentId,
                gradeId = 1L,
                startDate = LocalDateTime.now().minusDays(50),
                endDate = LocalDateTime.now().minusDays(40),
                isActive = false,
                createdAt = LocalDateTime.now().minusDays(50),
                updatedAt = LocalDateTime.now().minusDays(40)
            )
        )
        
        every { 
            studentGradeRepositoryPort.findEnrollmentsByStudentIdAndDateRange(
                studentId, startDate, endDate
            ) 
        } returns enrollments
        
        // When
        val result = studentGradeQueryUseCase.getEnrollmentsByStudentIdAndDateRange(
            studentId, startDate, endDate
        )
        
        // Then
        assertThat(result).isEqualTo(enrollments)
        verify { 
            studentGradeRepositoryPort.findEnrollmentsByStudentIdAndDateRange(
                studentId, startDate, endDate
            ) 
        }
    }
    
    @Test
    fun `getEnrollmentsByStudentIdAndDateRange should throw exception when studentId is invalid`() {
        // Given
        val studentId = 0L
        val startDate = LocalDateTime.now().minusDays(60)
        val endDate = LocalDateTime.now().minusDays(30)
        
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.getEnrollmentsByStudentIdAndDateRange(
                studentId, startDate, endDate
            ) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student ID must be positive")
        
        verify(exactly = 0) { 
            studentGradeRepositoryPort.findEnrollmentsByStudentIdAndDateRange(any(), any(), any()) 
        }
    }
    
    @Test
    fun `getEnrollmentsByStudentIdAndDateRange should throw exception when startDate is after endDate`() {
        // Given
        val studentId = 1L
        val startDate = LocalDateTime.now().minusDays(30)
        val endDate = LocalDateTime.now().minusDays(60)
        
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.getEnrollmentsByStudentIdAndDateRange(
                studentId, startDate, endDate
            ) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Start date cannot be after end date")
        
        verify(exactly = 0) { 
            studentGradeRepositoryPort.findEnrollmentsByStudentIdAndDateRange(any(), any(), any()) 
        }
    }
    
    @Test
    fun `isStudentEnrolledInGrade should return true when student is enrolled`() {
        // Given
        val studentId = 1L
        val gradeId = 1L
        
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) } returns true
        
        // When
        val result = studentGradeQueryUseCase.isStudentEnrolledInGrade(studentId, gradeId)
        
        // Then
        assertThat(result).isTrue()
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) }
    }
    
    @Test
    fun `isStudentEnrolledInGrade should return false when student is not enrolled`() {
        // Given
        val studentId = 1L
        val gradeId = 1L
        
        every { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) } returns false
        
        // When
        val result = studentGradeQueryUseCase.isStudentEnrolledInGrade(studentId, gradeId)
        
        // Then
        assertThat(result).isFalse()
        verify { studentGradeRepositoryPort.isStudentEnrolledInGrade(studentId, gradeId) }
    }
    
    @Test
    fun `isStudentEnrolledInGrade should throw exception when studentId is invalid`() {
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.isStudentEnrolledInGrade(0, 1L) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student ID must be positive")
        
        verify(exactly = 0) { studentGradeRepositoryPort.isStudentEnrolledInGrade(any(), any()) }
    }
    
    @Test
    fun `isStudentEnrolledInGrade should throw exception when gradeId is invalid`() {
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.isStudentEnrolledInGrade(1L, 0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade ID must be positive")
        
        verify(exactly = 0) { studentGradeRepositoryPort.isStudentEnrolledInGrade(any(), any()) }
    }
    
    @Test
    fun `hasActiveEnrollment should return true when student has active enrollment`() {
        // Given
        val studentId = 1L
        
        every { studentGradeRepositoryPort.hasActiveEnrollment(studentId) } returns true
        
        // When
        val result = studentGradeQueryUseCase.hasActiveEnrollment(studentId)
        
        // Then
        assertThat(result).isTrue()
        verify { studentGradeRepositoryPort.hasActiveEnrollment(studentId) }
    }
    
    @Test
    fun `hasActiveEnrollment should return false when student has no active enrollment`() {
        // Given
        val studentId = 1L
        
        every { studentGradeRepositoryPort.hasActiveEnrollment(studentId) } returns false
        
        // When
        val result = studentGradeQueryUseCase.hasActiveEnrollment(studentId)
        
        // Then
        assertThat(result).isFalse()
        verify { studentGradeRepositoryPort.hasActiveEnrollment(studentId) }
    }
    
    @Test
    fun `hasActiveEnrollment should throw exception when studentId is invalid`() {
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.hasActiveEnrollment(0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student ID must be positive")
        
        verify(exactly = 0) { studentGradeRepositoryPort.hasActiveEnrollment(any()) }
    }
    
    @Test
    fun `getActiveEnrollmentCountByGradeId should return count of active enrollments`() {
        // Given
        val gradeId = 1L
        val count = 5L
        
        every { studentGradeRepositoryPort.countActiveEnrollmentsByGradeId(gradeId) } returns count
        
        // When
        val result = studentGradeQueryUseCase.getActiveEnrollmentCountByGradeId(gradeId)
        
        // Then
        assertThat(result).isEqualTo(count)
        verify { studentGradeRepositoryPort.countActiveEnrollmentsByGradeId(gradeId) }
    }
    
    @Test
    fun `getActiveEnrollmentCountByGradeId should throw exception when gradeId is invalid`() {
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.getActiveEnrollmentCountByGradeId(0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade ID must be positive")
        
        verify(exactly = 0) { studentGradeRepositoryPort.countActiveEnrollmentsByGradeId(any()) }
    }
    
    @Test
    fun `getEnrollmentCountByStudentId should return count of enrollments`() {
        // Given
        val studentId = 1L
        val count = 3L
        
        every { studentGradeRepositoryPort.countEnrollmentsByStudentId(studentId) } returns count
        
        // When
        val result = studentGradeQueryUseCase.getEnrollmentCountByStudentId(studentId)
        
        // Then
        assertThat(result).isEqualTo(count)
        verify { studentGradeRepositoryPort.countEnrollmentsByStudentId(studentId) }
    }
    
    @Test
    fun `getEnrollmentCountByStudentId should throw exception when studentId is invalid`() {
        // When & Then
        assertThatThrownBy { 
            studentGradeQueryUseCase.getEnrollmentCountByStudentId(0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Student ID must be positive")
        
        verify(exactly = 0) { studentGradeRepositoryPort.countEnrollmentsByStudentId(any()) }
    }
}
