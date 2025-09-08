package co.edu.school.academic_management_service.application.port.input

import co.edu.school.academic_management_service.domain.entity.Grade

/**
 * Input port for grade management use cases.
 * Follows the Dependency Inversion Principle.
 * 
 * This interface defines the business operations for managing grades.
 */
interface GradeManagementUseCase {
    /**
     * Creates a new grade.
     * @param name The name of the grade
     * @param description The description of the grade
     * @param level The level of the grade
     * @return The created grade
     */
    fun createGrade(name: String, description: String?, level: Int): Grade
    
    /**
     * Updates an existing grade.
     * @param id The ID of the grade to update
     * @param name The new name of the grade
     * @param description The new description of the grade
     * @param level The new level of the grade
     * @return The updated grade
     */
    fun updateGrade(id: Long, name: String, description: String?, level: Int): Grade
    
    /**
     * Deactivates a grade.
     * @param id The ID of the grade to deactivate
     * @return true if the grade was deactivated, false otherwise
     */
    fun deactivateGrade(id: Long): Boolean
    
    /**
     * Activates a grade.
     * @param id The ID of the grade to activate
     * @return true if the grade was activated, false otherwise
     */
    fun activateGrade(id: Long): Boolean
    
    /**
     * Deletes a grade.
     * @param id The ID of the grade to delete
     * @return true if the grade was deleted, false otherwise
     */
    fun deleteGrade(id: Long): Boolean
}
