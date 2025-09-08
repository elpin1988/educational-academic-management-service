package co.edu.school.academic_management_service.domain.repository

import co.edu.school.academic_management_service.domain.entity.Grade

/**
 * Command repository interface for Grade entity.
 * Follows Command Query Responsibility Segregation (CQRS) pattern.
 * 
 * This interface defines write operations for Grade entities.
 */
interface GradeCommandRepository {
    /**
     * Saves a new grade or updates an existing one.
     * @param grade The grade to save
     * @return The saved grade with generated ID
     */
    fun save(grade: Grade): Grade
    
    /**
     * Updates an existing grade.
     * @param grade The grade to update
     * @return The updated grade
     */
    fun update(grade: Grade): Grade
    
    /**
     * Deletes a grade by its ID.
     * @param id The ID of the grade to delete
     * @return true if the grade was deleted, false otherwise
     */
    fun deleteById(id: Long): Boolean
    
    /**
     * Deletes a grade entity.
     * @param grade The grade to delete
     * @return true if the grade was deleted, false otherwise
     */
    fun delete(grade: Grade): Boolean
}
