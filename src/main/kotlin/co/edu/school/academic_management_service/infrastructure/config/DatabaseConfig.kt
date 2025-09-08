package co.edu.school.academic_management_service.infrastructure.config

import co.edu.school.academic_management_service.infrastructure.database.table.GradeEntity
import co.edu.school.academic_management_service.infrastructure.database.table.StudentGradeEntity
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfig {

    private val logger = LoggerFactory.getLogger(DatabaseConfig::class.java)

    @Value("\${app.database.url:jdbc:h2:mem:testdb}")
    private lateinit var databaseUrl: String

    @Value("\${app.database.username:sa}")
    private lateinit var databaseUsername: String

    @Value("\${app.database.password:}")
    private lateinit var databasePassword: String

    @Value("\${app.database.driver:org.h2.Driver}")
    private lateinit var databaseDriver: String

    @Bean
    fun database(): Database {
        logger.info("Connecting to database: $databaseUrl")
        logger.info("Using driver: $databaseDriver")
        logger.info("Username: $databaseUsername")
        
        return try {
            val db = Database.connect(
                url = databaseUrl,
                driver = databaseDriver,
                user = databaseUsername,
                password = databasePassword
            )
            logger.info("Database connection established successfully")
            db
        } catch (e: Exception) {
            logger.error("Failed to connect to database", e)
            throw e
        }
    }
}