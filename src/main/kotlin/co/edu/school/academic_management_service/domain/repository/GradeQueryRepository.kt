package co.edu.school.academic_management_service.domain.repository

import co.edu.school.academic_management_service.domain.entity.Grade

/**
 * Query repository interface for Grade entity.
 * Follows Command Query Responsibility Segregation (CQRS) pattern.
 * 
 * This interface defines read operations for Grade entities.
 */
interface GradeQueryRepository {
    /**
     * Finds a grade by its ID.
     * @param id The ID of the grade
     * @return The grade if found, null otherwise
     */
    fun findById(id: Long): Grade?
    
    /**
     * Finds a grade by its name.
     * @param name The name of the grade
     * @return The grade if found, null otherwise
     */
    fun findByName(name: String): Grade?
    
    /**
     * Finds a grade by its level.
     * @param level The level of the grade
     * @return The grade if found, null otherwise
     */
    fun findByLevel(level: Int): Grade?
    
    /**
     * Finds all grades.
     * @return List of all grades
     */
    fun findAll(): List<Grade>
    
    /**
     * Finds all active grades.
     * @return List of all active grades
     */
    fun findAllActive(): List<Grade>
    
    /**
     * Finds all inactive grades.
     * @return List of all inactive grades
     */
    fun findAllInactive(): List<Grade>
    
    /**
     * Checks if a grade exists by its ID.
     * @param id The ID of the grade
     * @return true if the grade exists, false otherwise
     */
    fun existsById(id: Long): Boolean
    
    /**
     * Checks if a grade exists by its name.
     * @param name The name of the grade
     * @return true if the grade exists, false otherwise
     */
    fun existsByName(name: String): Boolean
    
    /**
     * Checks if a grade exists by its level.
     * @param level The level of the grade
     * @return true if the grade exists, false otherwise
     */
    fun existsByLevel(level: Int): Boolean
    
    /**
     * Counts the total number of grades.
     * @return The total count of grades
     */
    fun count(): Long
    
    /**
     * Counts the number of active grades.
     * @return The count of active grades
     */
    fun countActive(): Long
}
