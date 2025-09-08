package co.edu.school.academic_management_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main application class for Academic Management Service.
 * 
 * This service manages grades and student grade enrollments in the school system.
 * It follows Clean Architecture + Hexagonal Architecture principles.
 */
@SpringBootApplication
class AcademicManagementServiceApplication

fun main(args: Array<String>) {
	runApplication<AcademicManagementServiceApplication>(*args)
}
