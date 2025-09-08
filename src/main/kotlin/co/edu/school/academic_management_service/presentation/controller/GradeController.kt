package co.edu.school.academic_management_service.presentation.controller

import co.edu.school.academic_management_service.application.port.input.GradeManagementUseCase
import co.edu.school.academic_management_service.application.port.input.GradeQueryUseCase
import co.edu.school.academic_management_service.domain.entity.Grade
import co.edu.school.academic_management_service.presentation.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

/**
 * REST Controller for Grade management.
 * Follows Single Responsibility Principle by focusing only on HTTP concerns.
 */
@RestController
@RequestMapping("/api/v1/grades")
@Tag(name = "Grade Management", description = "Operations for managing grades")
class GradeController(
    private val gradeManagementUseCase: GradeManagementUseCase,
    private val gradeQueryUseCase: GradeQueryUseCase
) {
    
    @PostMapping
    @Operation(summary = "Create a new grade", description = "Creates a new grade in the system")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Grade created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "409", description = "Grade already exists")
        ]
    )
    fun createGrade(@Valid @RequestBody createGradeDto: CreateGradeDto): ResponseEntity<GradeResponseDto> {
        val grade = gradeManagementUseCase.createGrade(
            name = createGradeDto.name,
            description = createGradeDto.description,
            level = createGradeDto.level
        )
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(grade.toResponseDto())
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a grade", description = "Updates an existing grade")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Grade updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "404", description = "Grade not found"),
            ApiResponse(responseCode = "409", description = "Grade name or level already exists")
        ]
    )
    fun updateGrade(
        @PathVariable id: Long,
        @Valid @RequestBody updateGradeDto: UpdateGradeDto
    ): ResponseEntity<GradeResponseDto> {
        val grade = gradeManagementUseCase.updateGrade(
            id = id,
            name = updateGradeDto.name,
            description = updateGradeDto.description,
            level = updateGradeDto.level
        )
        
        return ResponseEntity.ok(grade.toResponseDto())
    }
    
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a grade", description = "Deactivates a grade")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Grade deactivated successfully"),
            ApiResponse(responseCode = "404", description = "Grade not found")
        ]
    )
    fun deactivateGrade(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val success = gradeManagementUseCase.deactivateGrade(id)
        return if (success) {
            ResponseEntity.ok(mapOf("message" to "Grade deactivated successfully"))
        } else {
            ResponseEntity.ok(mapOf("message" to "Grade was already inactive"))
        }
    }
    
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a grade", description = "Activates a grade")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Grade activated successfully"),
            ApiResponse(responseCode = "404", description = "Grade not found")
        ]
    )
    fun activateGrade(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val success = gradeManagementUseCase.activateGrade(id)
        return if (success) {
            ResponseEntity.ok(mapOf("message" to "Grade activated successfully"))
        } else {
            ResponseEntity.ok(mapOf("message" to "Grade was already active"))
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a grade", description = "Deletes a grade (only if inactive)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Grade deleted successfully"),
            ApiResponse(responseCode = "404", description = "Grade not found"),
            ApiResponse(responseCode = "400", description = "Cannot delete active grade")
        ]
    )
    fun deleteGrade(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val success = gradeManagementUseCase.deleteGrade(id)
        return if (success) {
            ResponseEntity.ok(mapOf("message" to "Grade deleted successfully"))
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get grade by ID", description = "Retrieves a grade by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Grade found"),
            ApiResponse(responseCode = "404", description = "Grade not found")
        ]
    )
    fun getGradeById(@PathVariable id: Long): ResponseEntity<GradeResponseDto> {
        val grade = gradeQueryUseCase.getGradeById(id)
        return if (grade != null) {
            ResponseEntity.ok(grade.toResponseDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/name/{name}")
    @Operation(summary = "Get grade by name", description = "Retrieves a grade by its name")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Grade found"),
            ApiResponse(responseCode = "404", description = "Grade not found")
        ]
    )
    fun getGradeByName(@PathVariable name: String): ResponseEntity<GradeResponseDto> {
        val grade = gradeQueryUseCase.getGradeByName(name)
        return if (grade != null) {
            ResponseEntity.ok(grade.toResponseDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/level/{level}")
    @Operation(summary = "Get grade by level", description = "Retrieves a grade by its level")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Grade found"),
            ApiResponse(responseCode = "404", description = "Grade not found")
        ]
    )
    fun getGradeByLevel(@PathVariable level: Int): ResponseEntity<GradeResponseDto> {
        val grade = gradeQueryUseCase.getGradeByLevel(level)
        return if (grade != null) {
            ResponseEntity.ok(grade.toResponseDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all grades", description = "Retrieves all grades")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Grades retrieved successfully")
        ]
    )
    fun getAllGrades(): ResponseEntity<List<GradeResponseDto>> {
        val grades = gradeQueryUseCase.getAllGrades()
        return ResponseEntity.ok(grades.map { it.toResponseDto() })
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active grades", description = "Retrieves all active grades")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Active grades retrieved successfully")
        ]
    )
    fun getActiveGrades(): ResponseEntity<List<GradeResponseDto>> {
        val grades = gradeQueryUseCase.getActiveGrades()
        return ResponseEntity.ok(grades.map { it.toResponseDto() })
    }
    
    @GetMapping("/inactive")
    @Operation(summary = "Get inactive grades", description = "Retrieves all inactive grades")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Inactive grades retrieved successfully")
        ]
    )
    fun getInactiveGrades(): ResponseEntity<List<GradeResponseDto>> {
        val grades = gradeQueryUseCase.getInactiveGrades()
        return ResponseEntity.ok(grades.map { it.toResponseDto() })
    }
    
    @GetMapping("/exists/{id}")
    @Operation(summary = "Check if grade exists", description = "Checks if a grade exists by ID")
    fun gradeExists(@PathVariable id: Long): ResponseEntity<Map<String, Boolean>> {
        val exists = gradeQueryUseCase.gradeExists(id)
        return ResponseEntity.ok(mapOf("exists" to exists))
    }
    
    @GetMapping("/exists/name/{name}")
    @Operation(summary = "Check if grade exists by name", description = "Checks if a grade exists by name")
    fun gradeExistsByName(@PathVariable name: String): ResponseEntity<Map<String, Boolean>> {
        val exists = gradeQueryUseCase.gradeExistsByName(name)
        return ResponseEntity.ok(mapOf("exists" to exists))
    }
    
    @GetMapping("/exists/level/{level}")
    @Operation(summary = "Check if grade exists by level", description = "Checks if a grade exists by level")
    fun gradeExistsByLevel(@PathVariable level: Int): ResponseEntity<Map<String, Boolean>> {
        val exists = gradeQueryUseCase.gradeExistsByLevel(level)
        return ResponseEntity.ok(mapOf("exists" to exists))
    }
}

/**
 * Extension function to convert Grade entity to GradeResponseDto
 */
private fun Grade.toResponseDto(): GradeResponseDto {
    return GradeResponseDto(
        id = this.id ?: throw IllegalStateException("Grade ID cannot be null"),
        name = this.name,
        description = this.description,
        level = this.level,
        isActive = this.isActive,
        displayName = this.getDisplayName(),
        fullDescription = this.getFullDescription(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
