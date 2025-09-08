package co.edu.school.academic_management_service.presentation.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

/**
 * Data Transfer Object for StudentGrade entity.
 * Used for API communication.
 */
data class StudentGradeDto(
    val id: Long? = null,
    val studentId: Long,
    val gradeId: Long,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startDate: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endDate: LocalDateTime? = null,
    val isActive: Boolean,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime? = null
)

/**
 * Data Transfer Object for enrolling a student in a grade.
 */
data class EnrollStudentDto(
    val studentId: Long,
    val gradeId: Long,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startDate: LocalDateTime? = null
)

/**
 * Data Transfer Object for transferring a student between grades.
 */
data class TransferStudentDto(
    val studentId: Long,
    val fromGradeId: Long,
    val toGradeId: Long,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val transferDate: LocalDateTime? = null
)

/**
 * Data Transfer Object for ending a student's enrollment.
 */
data class EndEnrollmentDto(
    val studentId: Long,
    val gradeId: Long,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endDate: LocalDateTime? = null
)

/**
 * Data Transfer Object for graduating a student.
 */
data class GraduateStudentDto(
    val studentId: Long,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val graduationDate: LocalDateTime? = null
)

/**
 * Data Transfer Object for StudentGrade response.
 */
data class StudentGradeResponseDto(
    val id: Long,
    val studentId: Long,
    val gradeId: Long,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startDate: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endDate: LocalDateTime? = null,
    val isActive: Boolean,
    val isCurrentlyActive: Boolean,
    val hasEnded: Boolean,
    val enrollmentDurationDays: Long,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime
)
