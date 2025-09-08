package co.edu.school.academic_management_service.infrastructure.database.table

import co.edu.school.academic_management_service.domain.entity.StudentGrade
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object StudentGradeTable : LongIdTable("student_grades") {
    val studentId = long("student_id")
    val gradeId = reference("grade_id", GradeTable.id, onDelete = ReferenceOption.CASCADE)
    val startDate = datetime("start_date").default(LocalDateTime.now())
    val endDate = datetime("end_date").nullable()
    val isActive = bool("is_active").default(true)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class StudentGradeEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<StudentGradeEntity>(StudentGradeTable)
    
    var studentId by StudentGradeTable.studentId
    var grade by GradeEntity referencedOn StudentGradeTable.gradeId
    var startDate by StudentGradeTable.startDate
    var endDate by StudentGradeTable.endDate
    var isActive by StudentGradeTable.isActive
    var createdAt by StudentGradeTable.createdAt
    var updatedAt by StudentGradeTable.updatedAt
    
    fun toDomain(): StudentGrade {
        return StudentGrade(
            id = id.value,
            studentId = studentId,
            gradeId = grade.id.value,
            startDate = startDate,
            endDate = endDate,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

fun StudentGradeEntity.Companion.fromDomain(studentGrade: StudentGrade): StudentGradeEntity {
    val entity = if (studentGrade.id != null) {
        StudentGradeEntity.findById(studentGrade.id) ?: StudentGradeEntity.new { }
    } else {
        StudentGradeEntity.new { }
    }
    
    entity.studentId = studentGrade.studentId
    entity.grade = GradeEntity.findById(studentGrade.gradeId)
        ?: throw IllegalArgumentException("Grade with id ${studentGrade.gradeId} not found")
    entity.startDate = studentGrade.startDate
    entity.endDate = studentGrade.endDate
    entity.isActive = studentGrade.isActive
    entity.createdAt = studentGrade.createdAt
    entity.updatedAt = studentGrade.updatedAt
    
    return entity
}
