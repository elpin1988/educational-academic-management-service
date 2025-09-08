package co.edu.school.academic_management_service.presentation.controller

import co.edu.school.academic_management_service.application.port.input.StudentGradeManagementUseCase
import co.edu.school.academic_management_service.application.port.input.StudentGradeQueryUseCase
import co.edu.school.academic_management_service.domain.entity.StudentGrade
import co.edu.school.academic_management_service.presentation.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * REST Controller for Student Grade management.
 * Follows Single Responsibility Principle by focusing only on HTTP concerns.
 */
@RestController
@RequestMapping("/api/v1/student-grades")
@Tag(name = "Student Grade Management", description = "Operations for managing student grade enrollments")
class StudentGradeController(
    private val studentGradeManagementUseCase: StudentGradeManagementUseCase,
    private val studentGradeQueryUseCase: StudentGradeQueryUseCase
) {
    
    @PostMapping("/enroll")
    @Operation(summary = "Enroll student in grade", description = "Enrolls a student in a specific grade")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Student enrolled successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "409", description = "Student already enrolled")
        ]
    )
    fun enrollStudent(@RequestBody enrollStudentDto: EnrollStudentDto): ResponseEntity<StudentGradeResponseDto> {
        val studentGrade = studentGradeManagementUseCase.enrollStudentInGrade(
            studentId = enrollStudentDto.studentId,
            gradeId = enrollStudentDto.gradeId,
            startDate = enrollStudentDto.startDate ?: LocalDateTime.now()
        )
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(studentGrade.toResponseDto())
    }
    
    @PostMapping("/transfer")
    @Operation(summary = "Transfer student between grades", description = "Transfers a student from one grade to another")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Student transferred successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "404", description = "Student not enrolled in source grade")
        ]
    )
    fun transferStudent(@RequestBody transferStudentDto: TransferStudentDto): ResponseEntity<StudentGradeResponseDto> {
        val studentGrade = studentGradeManagementUseCase.transferStudentToGrade(
            studentId = transferStudentDto.studentId,
            fromGradeId = transferStudentDto.fromGradeId,
            toGradeId = transferStudentDto.toGradeId,
            transferDate = transferStudentDto.transferDate ?: LocalDateTime.now()
        )
        
        return ResponseEntity.ok(studentGrade.toResponseDto())
    }
    
    @PostMapping("/end")
    @Operation(summary = "End student enrollment", description = "Ends a student's enrollment in a grade")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Enrollment ended successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "404", description = "Student not enrolled in grade")
        ]
    )
    fun endEnrollment(@RequestBody endEnrollmentDto: EndEnrollmentDto): ResponseEntity<StudentGradeResponseDto> {
        val studentGrade = studentGradeManagementUseCase.endStudentEnrollment(
            studentId = endEnrollmentDto.studentId,
            gradeId = endEnrollmentDto.gradeId,
            endDate = endEnrollmentDto.endDate ?: LocalDateTime.now()
        )
        
        return if (studentGrade != null) {
            ResponseEntity.ok(studentGrade.toResponseDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PostMapping("/graduate")
    @Operation(summary = "Graduate student", description = "Graduates a student from their current grade")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Student graduated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "404", description = "Student not enrolled")
        ]
    )
    fun graduateStudent(@RequestBody graduateStudentDto: GraduateStudentDto): ResponseEntity<StudentGradeResponseDto> {
        val studentGrade = studentGradeManagementUseCase.graduateStudent(
            studentId = graduateStudentDto.studentId,
            graduationDate = graduateStudentDto.graduationDate ?: LocalDateTime.now()
        )
        
        return if (studentGrade != null) {
            ResponseEntity.ok(studentGrade.toResponseDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove enrollment", description = "Removes a student grade enrollment")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Enrollment removed successfully"),
            ApiResponse(responseCode = "404", description = "Enrollment not found"),
            ApiResponse(responseCode = "400", description = "Cannot remove active enrollment")
        ]
    )
    fun removeEnrollment(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val success = studentGradeManagementUseCase.removeEnrollment(id)
        return if (success) {
            ResponseEntity.ok(mapOf("message" to "Enrollment removed successfully"))
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Retrieves an enrollment by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Enrollment found"),
            ApiResponse(responseCode = "404", description = "Enrollment not found")
        ]
    )
    fun getEnrollmentById(@PathVariable id: Long): ResponseEntity<StudentGradeResponseDto> {
        val enrollment = studentGradeQueryUseCase.getEnrollmentById(id)
        return if (enrollment != null) {
            ResponseEntity.ok(enrollment.toResponseDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get enrollments by student ID", description = "Retrieves all enrollments for a student")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
        ]
    )
    fun getEnrollmentsByStudentId(@PathVariable studentId: Long): ResponseEntity<List<StudentGradeResponseDto>> {
        val enrollments = studentGradeQueryUseCase.getEnrollmentsByStudentId(studentId)
        return ResponseEntity.ok(enrollments.map { it.toResponseDto() })
    }
    
    @GetMapping("/grade/{gradeId}")
    @Operation(summary = "Get enrollments by grade ID", description = "Retrieves all enrollments for a grade")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
        ]
    )
    fun getEnrollmentsByGradeId(@PathVariable gradeId: Long): ResponseEntity<List<StudentGradeResponseDto>> {
        val enrollments = studentGradeQueryUseCase.getEnrollmentsByGradeId(gradeId)
        return ResponseEntity.ok(enrollments.map { it.toResponseDto() })
    }
    
    @GetMapping("/student/{studentId}/current")
    @Operation(summary = "Get current enrollment", description = "Retrieves the current active enrollment for a student")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Current enrollment found"),
            ApiResponse(responseCode = "404", description = "No active enrollment found")
        ]
    )
    fun getCurrentEnrollmentByStudentId(@PathVariable studentId: Long): ResponseEntity<StudentGradeResponseDto> {
        val enrollment = studentGradeQueryUseCase.getCurrentEnrollmentByStudentId(studentId)
        return if (enrollment != null) {
            ResponseEntity.ok(enrollment.toResponseDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/grade/{gradeId}/active")
    @Operation(summary = "Get active enrollments by grade", description = "Retrieves all active enrollments for a grade")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Active enrollments retrieved successfully")
        ]
    )
    fun getActiveEnrollmentsByGradeId(@PathVariable gradeId: Long): ResponseEntity<List<StudentGradeResponseDto>> {
        val enrollments = studentGradeQueryUseCase.getActiveEnrollmentsByGradeId(gradeId)
        return ResponseEntity.ok(enrollments.map { it.toResponseDto() })
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get all active enrollments", description = "Retrieves all active enrollments")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Active enrollments retrieved successfully")
        ]
    )
    fun getAllActiveEnrollments(): ResponseEntity<List<StudentGradeResponseDto>> {
        val enrollments = studentGradeQueryUseCase.getAllActiveEnrollments()
        return ResponseEntity.ok(enrollments.map { it.toResponseDto() })
    }
    
    @GetMapping("/active-on-date")
    @Operation(summary = "Get enrollments active on date", description = "Retrieves all enrollments active on a specific date")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
        ]
    )
    fun getEnrollmentsActiveOnDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) date: LocalDateTime
    ): ResponseEntity<List<StudentGradeResponseDto>> {
        val enrollments = studentGradeQueryUseCase.getEnrollmentsActiveOnDate(date)
        return ResponseEntity.ok(enrollments.map { it.toResponseDto() })
    }
    
    @GetMapping("/student/{studentId}/date-range")
    @Operation(summary = "Get enrollments by date range", description = "Retrieves enrollments for a student within a date range")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
        ]
    )
    fun getEnrollmentsByStudentIdAndDateRange(
        @PathVariable studentId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<List<StudentGradeResponseDto>> {
        val enrollments = studentGradeQueryUseCase.getEnrollmentsByStudentIdAndDateRange(
            studentId, startDate, endDate
        )
        return ResponseEntity.ok(enrollments.map { it.toResponseDto() })
    }
    
    @GetMapping("/student/{studentId}/grade/{gradeId}/enrolled")
    @Operation(summary = "Check if student is enrolled in grade", description = "Checks if a student is enrolled in a specific grade")
    fun isStudentEnrolledInGrade(
        @PathVariable studentId: Long,
        @PathVariable gradeId: Long
    ): ResponseEntity<Map<String, Boolean>> {
        val isEnrolled = studentGradeQueryUseCase.isStudentEnrolledInGrade(studentId, gradeId)
        return ResponseEntity.ok(mapOf("enrolled" to isEnrolled))
    }
    
    @GetMapping("/student/{studentId}/has-active")
    @Operation(summary = "Check if student has active enrollment", description = "Checks if a student has any active enrollment")
    fun hasActiveEnrollment(@PathVariable studentId: Long): ResponseEntity<Map<String, Boolean>> {
        val hasActive = studentGradeQueryUseCase.hasActiveEnrollment(studentId)
        return ResponseEntity.ok(mapOf("hasActive" to hasActive))
    }
    
    @GetMapping("/grade/{gradeId}/active-count")
    @Operation(summary = "Get active enrollment count", description = "Gets the count of active enrollments for a grade")
    fun getActiveEnrollmentCountByGradeId(@PathVariable gradeId: Long): ResponseEntity<Map<String, Long>> {
        val count = studentGradeQueryUseCase.getActiveEnrollmentCountByGradeId(gradeId)
        return ResponseEntity.ok(mapOf("count" to count))
    }
    
    @GetMapping("/student/{studentId}/enrollment-count")
    @Operation(summary = "Get enrollment count for student", description = "Gets the total count of enrollments for a student")
    fun getEnrollmentCountByStudentId(@PathVariable studentId: Long): ResponseEntity<Map<String, Long>> {
        val count = studentGradeQueryUseCase.getEnrollmentCountByStudentId(studentId)
        return ResponseEntity.ok(mapOf("count" to count))
    }
}

/**
 * Extension function to convert StudentGrade entity to StudentGradeResponseDto
 */
private fun StudentGrade.toResponseDto(): StudentGradeResponseDto {
    return StudentGradeResponseDto(
        id = this.id ?: throw IllegalStateException("StudentGrade ID cannot be null"),
        studentId = this.studentId,
        gradeId = this.gradeId,
        startDate = this.startDate,
        endDate = this.endDate,
        isActive = this.isActive,
        isCurrentlyActive = this.isCurrentlyActive(),
        hasEnded = this.hasEnded(),
        enrollmentDurationDays = this.getEnrollmentDuration(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
