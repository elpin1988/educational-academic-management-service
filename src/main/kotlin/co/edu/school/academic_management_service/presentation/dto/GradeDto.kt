package co.edu.school.academic_management_service.presentation.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.*
import java.time.LocalDateTime

/**
 * Data Transfer Object for Grade entity.
 * Used for API communication.
 */
data class GradeDto(
    val id: Long? = null,
    
    @field:NotBlank(message = "Grade name cannot be blank")
    @field:Size(min = 2, max = 100, message = "Grade name must be between 2 and 100 characters")
    val name: String,
    
    @field:Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    val description: String? = null,
    
    @field:Min(value = 1, message = "Grade level must be at least 1")
    @field:Max(value = 12, message = "Grade level must be at most 12")
    val level: Int,
    
    val isActive: Boolean = true,
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime? = null,
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime? = null
)

/**
 * Data Transfer Object for creating a new Grade.
 */
data class CreateGradeDto(
    @field:NotBlank(message = "Grade name cannot be blank")
    @field:Size(min = 2, max = 100, message = "Grade name must be between 2 and 100 characters")
    val name: String,
    
    @field:Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    val description: String? = null,
    
    @field:Min(value = 1, message = "Grade level must be at least 1")
    @field:Max(value = 12, message = "Grade level must be at most 12")
    val level: Int
)

/**
 * Data Transfer Object for updating a Grade.
 */
data class UpdateGradeDto(
    @field:NotBlank(message = "Grade name cannot be blank")
    @field:Size(min = 2, max = 100, message = "Grade name must be between 2 and 100 characters")
    val name: String,
    
    @field:Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    val description: String? = null,
    
    @field:Min(value = 1, message = "Grade level must be at least 1")
    @field:Max(value = 12, message = "Grade level must be at most 12")
    val level: Int
)

/**
 * Data Transfer Object for Grade response.
 */
data class GradeResponseDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    val level: Int,
    val isActive: Boolean,
    val displayName: String,
    val fullDescription: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime
)
