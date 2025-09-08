package co.edu.school.academic_management_service.application.port.input

import co.edu.school.academic_management_service.domain.entity.Grade

/**
 * Input port for grade query use cases.
 * Follows the Dependency Inversion Principle.
 * 
 * This interface defines the query operations for grades.
 */
interface GradeQueryUseCase {
    /**
     * Gets a grade by its ID.
     * @param id The ID of the grade
     * @return The grade if found, null otherwise
     */
    fun getGradeById(id: Long): Grade?
    
    /**
     * Gets a grade by its name.
     * @param name The name of the grade
     * @return The grade if found, null otherwise
     */
    fun getGradeByName(name: String): Grade?
    
    /**
     * Gets a grade by its level.
     * @param level The level of the grade
     * @return The grade if found, null otherwise
     */
    fun getGradeByLevel(level: Int): Grade?
    
    /**
     * Gets all grades.
     * @return List of all grades
     */
    fun getAllGrades(): List<Grade>
    
    /**
     * Gets all active grades.
     * @return List of all active grades
     */
    fun getActiveGrades(): List<Grade>
    
    /**
     * Gets all inactive grades.
     * @return List of all inactive grades
     */
    fun getInactiveGrades(): List<Grade>
    
    /**
     * Checks if a grade exists by its ID.
     * @param id The ID of the grade
     * @return true if the grade exists, false otherwise
     */
    fun gradeExists(id: Long): Boolean
    
    /**
     * Checks if a grade exists by its name.
     * @param name The name of the grade
     * @return true if the grade exists, false otherwise
     */
    fun gradeExistsByName(name: String): Boolean
    
    /**
     * Checks if a grade exists by its level.
     * @param level The level of the grade
     * @return true if the grade exists, false otherwise
     */
    fun gradeExistsByLevel(level: Int): Boolean
}
