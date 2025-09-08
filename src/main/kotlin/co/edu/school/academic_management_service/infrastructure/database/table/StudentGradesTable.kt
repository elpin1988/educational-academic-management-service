package co.edu.school.academic_management_service.infrastructure.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

/**
 * Exposed table definition for student grades.
 * Maps to the student_grades table in the database.
 */
object StudentGradesTable : LongIdTable("student_grades") {
    val studentId = long("student_id")
    val gradeId = reference("grade_id", GradesTable)
    val startDate = timestamp("start_date")
    val endDate = timestamp("end_date").nullable()
    val isActive = bool("is_active").default(true)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}
