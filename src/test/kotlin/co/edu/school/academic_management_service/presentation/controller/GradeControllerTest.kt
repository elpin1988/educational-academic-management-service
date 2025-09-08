package co.edu.school.academic_management_service.presentation.controller

import co.edu.school.academic_management_service.application.port.input.GradeManagementUseCase
import co.edu.school.academic_management_service.application.port.input.GradeQueryUseCase
import co.edu.school.academic_management_service.domain.entity.Grade
import co.edu.school.academic_management_service.presentation.dto.CreateGradeDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * Integration tests for GradeController.
 * Tests HTTP endpoints and JSON serialization/deserialization.
 */
@WebMvcTest(GradeController::class)
@org.springframework.context.annotation.Import(co.edu.school.academic_management_service.infrastructure.config.TestSecurityConfig::class)
class GradeControllerTest {
    
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    
    @Autowired
    private lateinit var gradeManagementUseCase: GradeManagementUseCase
    
    @Autowired
    private lateinit var gradeQueryUseCase: GradeQueryUseCase
    
    @Test
    fun `createGrade should return 201 when grade is created successfully`() {
        // Given
        val createGradeDto = CreateGradeDto(
            name = "First Grade",
            description = "First grade of elementary school",
            level = 1
        )
        val grade = Grade(
            id = 1L,
            name = "First Grade",
            description = "First grade of elementary school",
            level = 1
        )
        
        every { gradeManagementUseCase.createGrade(any(), any(), any()) } returns grade
        
        // When & Then
        mockMvc.perform(
            post("/api/v1/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGradeDto))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("First Grade"))
            .andExpect(jsonPath("$.description").value("First grade of elementary school"))
            .andExpect(jsonPath("$.level").value(1))
            .andExpect(jsonPath("$.isActive").value(true))
    }
    
    @Test
    fun `getGradeById should return 200 when grade exists`() {
        // Given
        val gradeId = 1L
        val grade = Grade(
            id = gradeId,
            name = "First Grade",
            description = "First grade of elementary school",
            level = 1
        )
        
        every { gradeQueryUseCase.getGradeById(gradeId) } returns grade
        
        // When & Then
        mockMvc.perform(get("/api/v1/grades/$gradeId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("First Grade"))
            .andExpect(jsonPath("$.description").value("First grade of elementary school"))
            .andExpect(jsonPath("$.level").value(1))
            .andExpect(jsonPath("$.isActive").value(true))
    }
    
    @Test
    fun `getGradeById should return 404 when grade does not exist`() {
        // Given
        val gradeId = 1L
        
        every { gradeQueryUseCase.getGradeById(gradeId) } returns null
        
        // When & Then
        mockMvc.perform(get("/api/v1/grades/$gradeId"))
            .andExpect(status().isNotFound)
    }
    
    @Test
    fun `getAllGrades should return 200 with list of grades`() {
        // Given
        val grades = listOf(
            Grade(id = 1L, name = "First Grade", description = "First grade", level = 1),
            Grade(id = 2L, name = "Second Grade", description = "Second grade", level = 2)
        )
        
        every { gradeQueryUseCase.getAllGrades() } returns grades
        
        // When & Then
        mockMvc.perform(get("/api/v1/grades"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("First Grade"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Second Grade"))
    }
    
    @Test
    fun `getActiveGrades should return 200 with list of active grades`() {
        // Given
        val activeGrades = listOf(
            Grade(id = 1L, name = "First Grade", description = "First grade", level = 1, isActive = true),
            Grade(id = 2L, name = "Second Grade", description = "Second grade", level = 2, isActive = true)
        )
        
        every { gradeQueryUseCase.getActiveGrades() } returns activeGrades
        
        // When & Then
        mockMvc.perform(get("/api/v1/grades/active"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].isActive").value(true))
            .andExpect(jsonPath("$[1].isActive").value(true))
    }
    
    @Test
    fun `gradeExists should return 200 with exists true when grade exists`() {
        // Given
        val gradeId = 1L
        
        every { gradeQueryUseCase.gradeExists(gradeId) } returns true
        
        // When & Then
        mockMvc.perform(get("/api/v1/grades/exists/$gradeId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.exists").value(true))
    }
    
    @Test
    fun `gradeExists should return 200 with exists false when grade does not exist`() {
        // Given
        val gradeId = 1L
        
        every { gradeQueryUseCase.gradeExists(gradeId) } returns false
        
        // When & Then
        mockMvc.perform(get("/api/v1/grades/exists/$gradeId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.exists").value(false))
    }
    
    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun gradeManagementUseCase(): GradeManagementUseCase = mockk()
        
        @Bean
        @Primary
        fun gradeQueryUseCase(): GradeQueryUseCase = mockk()
    }
}
