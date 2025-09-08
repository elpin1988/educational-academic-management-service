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
import java.time.LocalDateTime

/**
 * Unit tests for GradeManagementUseCaseImpl.
 * Tests business logic and validation rules.
 */
class GradeManagementUseCaseImplTest {
    
    private lateinit var gradeRepositoryPort: GradeRepositoryPort
    private lateinit var gradeManagementUseCase: GradeManagementUseCaseImpl
    
    @BeforeEach
    fun setUp() {
        gradeRepositoryPort = mockk()
        gradeManagementUseCase = GradeManagementUseCaseImpl(gradeRepositoryPort)
    }
    
    @Test
    fun `createGrade should create grade when data is valid`() {
        // Given
        val name = "First Grade"
        val description = "First grade of elementary school"
        val level = 1
        val expectedGrade = Grade(
            id = 1L,
            name = name,
            description = description,
            level = level
        )
        
        every { gradeRepositoryPort.existsByName(name) } returns false
        every { gradeRepositoryPort.existsByLevel(level) } returns false
        every { gradeRepositoryPort.save(any()) } returns expectedGrade
        
        // When
        val result = gradeManagementUseCase.createGrade(name, description, level)
        
        // Then
        assertThat(result).isEqualTo(expectedGrade)
        verify { gradeRepositoryPort.existsByName(name) }
        verify { gradeRepositoryPort.existsByLevel(level) }
        verify { gradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `createGrade should throw exception when grade name already exists`() {
        // Given
        val name = "First Grade"
        val description = "First grade of elementary school"
        val level = 1
        
        every { gradeRepositoryPort.existsByName(name) } returns true
        
        // When & Then
        assertThatThrownBy { 
            gradeManagementUseCase.createGrade(name, description, level) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade with name '$name' already exists")
        
        verify { gradeRepositoryPort.existsByName(name) }
        verify(exactly = 0) { gradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `createGrade should throw exception when grade level already exists`() {
        // Given
        val name = "First Grade"
        val description = "First grade of elementary school"
        val level = 1
        
        every { gradeRepositoryPort.existsByName(name) } returns false
        every { gradeRepositoryPort.existsByLevel(level) } returns true
        
        // When & Then
        assertThatThrownBy { 
            gradeManagementUseCase.createGrade(name, description, level) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade with level $level already exists")
        
        verify { gradeRepositoryPort.existsByName(name) }
        verify { gradeRepositoryPort.existsByLevel(level) }
        verify(exactly = 0) { gradeRepositoryPort.save(any()) }
    }
    
    @Test
    fun `createGrade should throw exception when name is blank`() {
        // When & Then
        assertThatThrownBy { 
            gradeManagementUseCase.createGrade("", "description", 1) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade name cannot be blank")
    }
    
    @Test
    fun `createGrade should throw exception when level is invalid`() {
        // When & Then
        assertThatThrownBy { 
            gradeManagementUseCase.createGrade("Grade", "description", 0) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade level must be between 1 and 12")
    }
    
    @Test
    fun `updateGrade should update grade when data is valid`() {
        // Given
        val id = 1L
        val name = "Updated Grade"
        val description = "Updated description"
        val level = 2
        val existingGrade = Grade(
            id = id,
            name = "Old Grade",
            description = "Old description",
            level = 1
        )
        val updatedGrade = existingGrade.copy(
            name = name,
            description = description,
            level = level,
            updatedAt = LocalDateTime.now()
        )
        
        every { gradeRepositoryPort.findById(id) } returns existingGrade
        every { gradeRepositoryPort.existsByName(name) } returns false
        every { gradeRepositoryPort.existsByLevel(level) } returns false
        every { gradeRepositoryPort.update(any()) } returns updatedGrade
        
        // When
        val result = gradeManagementUseCase.updateGrade(id, name, description, level)
        
        // Then
        assertThat(result).isEqualTo(updatedGrade)
        verify { gradeRepositoryPort.findById(id) }
        verify { gradeRepositoryPort.existsByName(name) }
        verify { gradeRepositoryPort.existsByLevel(level) }
        verify { gradeRepositoryPort.update(any()) }
    }
    
    @Test
    fun `updateGrade should throw exception when grade not found`() {
        // Given
        val id = 1L
        val name = "Updated Grade"
        val description = "Updated description"
        val level = 2
        
        every { gradeRepositoryPort.findById(id) } returns null
        
        // When & Then
        assertThatThrownBy { 
            gradeManagementUseCase.updateGrade(id, name, description, level) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Grade not found")
        
        verify { gradeRepositoryPort.findById(id) }
        verify(exactly = 0) { gradeRepositoryPort.update(any()) }
    }
    
    @Test
    fun `deactivateGrade should deactivate grade when grade exists and is active`() {
        // Given
        val id = 1L
        val activeGrade = Grade(
            id = id,
            name = "Test Grade",
            description = "Test description",
            level = 1,
            isActive = true
        )
        val deactivatedGrade = activeGrade.copy(
            isActive = false,
            updatedAt = LocalDateTime.now()
        )
        
        every { gradeRepositoryPort.findById(id) } returns activeGrade
        every { gradeRepositoryPort.update(any()) } returns deactivatedGrade
        
        // When
        val result = gradeManagementUseCase.deactivateGrade(id)
        
        // Then
        assertThat(result).isTrue()
        verify { gradeRepositoryPort.findById(id) }
        verify { gradeRepositoryPort.update(any()) }
    }
    
    @Test
    fun `deactivateGrade should return false when grade is already inactive`() {
        // Given
        val id = 1L
        val inactiveGrade = Grade(
            id = id,
            name = "Test Grade",
            description = "Test description",
            level = 1,
            isActive = false
        )
        
        every { gradeRepositoryPort.findById(id) } returns inactiveGrade
        
        // When
        val result = gradeManagementUseCase.deactivateGrade(id)
        
        // Then
        assertThat(result).isFalse()
        verify { gradeRepositoryPort.findById(id) }
        verify(exactly = 0) { gradeRepositoryPort.update(any()) }
    }
    
    @Test
    fun `deleteGrade should delete grade when grade exists and is inactive`() {
        // Given
        val id = 1L
        val inactiveGrade = Grade(
            id = id,
            name = "Test Grade",
            description = "Test description",
            level = 1,
            isActive = false
        )
        
        every { gradeRepositoryPort.findById(id) } returns inactiveGrade
        every { gradeRepositoryPort.deleteById(id) } returns true
        
        // When
        val result = gradeManagementUseCase.deleteGrade(id)
        
        // Then
        assertThat(result).isTrue()
        verify { gradeRepositoryPort.findById(id) }
        verify { gradeRepositoryPort.deleteById(id) }
    }
    
    @Test
    fun `deleteGrade should throw exception when trying to delete active grade`() {
        // Given
        val id = 1L
        val activeGrade = Grade(
            id = id,
            name = "Test Grade",
            description = "Test description",
            level = 1,
            isActive = true
        )
        
        every { gradeRepositoryPort.findById(id) } returns activeGrade
        
        // When & Then
        assertThatThrownBy { 
            gradeManagementUseCase.deleteGrade(id) 
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot delete active grade. Deactivate it first.")
        
        verify { gradeRepositoryPort.findById(id) }
        verify(exactly = 0) { gradeRepositoryPort.deleteById(id) }
    }
}
