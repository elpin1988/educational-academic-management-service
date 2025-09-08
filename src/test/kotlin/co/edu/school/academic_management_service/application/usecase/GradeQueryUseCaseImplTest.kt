package co.edu.school.academic_management_service.application.usecase

import co.edu.school.academic_management_service.application.port.output.GradeRepositoryPort
import co.edu.school.academic_management_service.domain.entity.Grade
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Unit tests for GradeQueryUseCaseImpl.
 * Tests query logic and validation rules.
 */
class GradeQueryUseCaseImplTest {
    
    private lateinit var gradeRepositoryPort: GradeRepositoryPort
    private lateinit var gradeQueryUseCase: GradeQueryUseCaseImpl
    
    @BeforeEach
    fun setUp() {
        gradeRepositoryPort = mockk()
        gradeQueryUseCase = GradeQueryUseCaseImpl(gradeRepositoryPort)
    }
    
    @Test
    fun `getGradeById should return grade when found`() {
        // Given
        val gradeId = 1L
        val grade = Grade(
            id = gradeId,
            name = "First Grade",
            description = "First grade of elementary school",
            level = 1
        )
        
        every { gradeRepositoryPort.findById(gradeId) } returns grade
        
        // When
        val result = gradeQueryUseCase.getGradeById(gradeId)
        
        // Then
        assertThat(result).isEqualTo(grade)
        verify { gradeRepositoryPort.findById(gradeId) }
    }
    
    @Test
    fun `getGradeById should return null when not found`() {
        // Given
        val gradeId = 1L
        
        every { gradeRepositoryPort.findById(gradeId) } returns null
        
        // When
        val result = gradeQueryUseCase.getGradeById(gradeId)
        
        // Then
        assertThat(result).isNull()
        verify { gradeRepositoryPort.findById(gradeId) }
    }
    
    @Test
    fun `getGradeByName should return grade when found`() {
        // Given
        val gradeName = "First Grade"
        val grade = Grade(
            id = 1L,
            name = gradeName,
            description = "First grade of elementary school",
            level = 1
        )
        
        every { gradeRepositoryPort.findByName(gradeName) } returns grade
        
        // When
        val result = gradeQueryUseCase.getGradeByName(gradeName)
        
        // Then
        assertThat(result).isEqualTo(grade)
        verify { gradeRepositoryPort.findByName(gradeName) }
    }
    
    @Test
    fun `getGradeByName should return null when not found`() {
        // Given
        val gradeName = "Non-existent Grade"
        
        every { gradeRepositoryPort.findByName(gradeName) } returns null
        
        // When
        val result = gradeQueryUseCase.getGradeByName(gradeName)
        
        // Then
        assertThat(result).isNull()
        verify { gradeRepositoryPort.findByName(gradeName) }
    }
    
    @Test
    fun `getGradeByName should throw exception when name is blank`() {
        // When & Then
        assertThatThrownBy { 
            gradeQueryUseCase.getGradeByName("") 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade name cannot be blank")
        
        verify(exactly = 0) { gradeRepositoryPort.findByName(any()) }
    }
    
    @Test
    fun `getGradeByLevel should return grade when found`() {
        // Given
        val level = 1
        val grade = Grade(
            id = 1L,
            name = "First Grade",
            description = "First grade of elementary school",
            level = level
        )
        
        every { gradeRepositoryPort.findByLevel(level) } returns grade
        
        // When
        val result = gradeQueryUseCase.getGradeByLevel(level)
        
        // Then
        assertThat(result).isEqualTo(grade)
        verify { gradeRepositoryPort.findByLevel(level) }
    }
    
    @Test
    fun `getGradeByLevel should return null when not found`() {
        // Given
        val level = 1
        
        every { gradeRepositoryPort.findByLevel(level) } returns null
        
        // When
        val result = gradeQueryUseCase.getGradeByLevel(level)
        
        // Then
        assertThat(result).isNull()
        verify { gradeRepositoryPort.findByLevel(level) }
    }
    
    @Test
    fun `getGradeByLevel should throw exception when level is invalid`() {
        // When & Then
        assertThatThrownBy { 
            gradeQueryUseCase.getGradeByLevel(0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade level must be positive")
        
        verify(exactly = 0) { gradeRepositoryPort.findByLevel(any()) }
    }
    
    @Test
    fun `getAllGrades should return all grades`() {
        // Given
        val grades = listOf(
            Grade(id = 1L, name = "First Grade", level = 1),
            Grade(id = 2L, name = "Second Grade", level = 2)
        )
        
        every { gradeRepositoryPort.findAll() } returns grades
        
        // When
        val result = gradeQueryUseCase.getAllGrades()
        
        // Then
        assertThat(result).isEqualTo(grades)
        verify { gradeRepositoryPort.findAll() }
    }
    
    @Test
    fun `getActiveGrades should return active grades`() {
        // Given
        val activeGrades = listOf(
            Grade(id = 1L, name = "First Grade", level = 1, isActive = true),
            Grade(id = 2L, name = "Second Grade", level = 2, isActive = true)
        )
        
        every { gradeRepositoryPort.findAllActive() } returns activeGrades
        
        // When
        val result = gradeQueryUseCase.getActiveGrades()
        
        // Then
        assertThat(result).isEqualTo(activeGrades)
        verify { gradeRepositoryPort.findAllActive() }
    }
    
    @Test
    fun `getInactiveGrades should return inactive grades`() {
        // Given
        val inactiveGrades = listOf(
            Grade(id = 1L, name = "Old Grade", level = 1, isActive = false)
        )
        
        every { gradeRepositoryPort.findAllInactive() } returns inactiveGrades
        
        // When
        val result = gradeQueryUseCase.getInactiveGrades()
        
        // Then
        assertThat(result).isEqualTo(inactiveGrades)
        verify { gradeRepositoryPort.findAllInactive() }
    }
    
    @Test
    fun `gradeExists should return true when grade exists`() {
        // Given
        val gradeId = 1L
        
        every { gradeRepositoryPort.existsById(gradeId) } returns true
        
        // When
        val result = gradeQueryUseCase.gradeExists(gradeId)
        
        // Then
        assertThat(result).isTrue()
        verify { gradeRepositoryPort.existsById(gradeId) }
    }
    
    @Test
    fun `gradeExists should return false when grade does not exist`() {
        // Given
        val gradeId = 1L
        
        every { gradeRepositoryPort.existsById(gradeId) } returns false
        
        // When
        val result = gradeQueryUseCase.gradeExists(gradeId)
        
        // Then
        assertThat(result).isFalse()
        verify { gradeRepositoryPort.existsById(gradeId) }
    }
    
    @Test
    fun `gradeExistsByName should return true when grade exists`() {
        // Given
        val gradeName = "First Grade"
        
        every { gradeRepositoryPort.existsByName(gradeName) } returns true
        
        // When
        val result = gradeQueryUseCase.gradeExistsByName(gradeName)
        
        // Then
        assertThat(result).isTrue()
        verify { gradeRepositoryPort.existsByName(gradeName) }
    }
    
    @Test
    fun `gradeExistsByName should return false when grade does not exist`() {
        // Given
        val gradeName = "Non-existent Grade"
        
        every { gradeRepositoryPort.existsByName(gradeName) } returns false
        
        // When
        val result = gradeQueryUseCase.gradeExistsByName(gradeName)
        
        // Then
        assertThat(result).isFalse()
        verify { gradeRepositoryPort.existsByName(gradeName) }
    }
    
    @Test
    fun `gradeExistsByName should throw exception when name is blank`() {
        // When & Then
        assertThatThrownBy { 
            gradeQueryUseCase.gradeExistsByName("") 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade name cannot be blank")
        
        verify(exactly = 0) { gradeRepositoryPort.existsByName(any()) }
    }
    
    @Test
    fun `gradeExistsByLevel should return true when grade exists`() {
        // Given
        val level = 1
        
        every { gradeRepositoryPort.existsByLevel(level) } returns true
        
        // When
        val result = gradeQueryUseCase.gradeExistsByLevel(level)
        
        // Then
        assertThat(result).isTrue()
        verify { gradeRepositoryPort.existsByLevel(level) }
    }
    
    @Test
    fun `gradeExistsByLevel should return false when grade does not exist`() {
        // Given
        val level = 1
        
        every { gradeRepositoryPort.existsByLevel(level) } returns false
        
        // When
        val result = gradeQueryUseCase.gradeExistsByLevel(level)
        
        // Then
        assertThat(result).isFalse()
        verify { gradeRepositoryPort.existsByLevel(level) }
    }
    
    @Test
    fun `gradeExistsByLevel should throw exception when level is invalid`() {
        // When & Then
        assertThatThrownBy { 
            gradeQueryUseCase.gradeExistsByLevel(0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade level must be positive")
        
        verify(exactly = 0) { gradeRepositoryPort.existsByLevel(any()) }
    }
}
