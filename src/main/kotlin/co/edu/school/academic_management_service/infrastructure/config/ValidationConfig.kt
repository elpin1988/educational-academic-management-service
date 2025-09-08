package co.edu.school.academic_management_service.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor

/**
 * Configuration for validation.
 * Sets up Bean Validation (JSR-303) for the application.
 */
@Configuration
class ValidationConfig {
    
    @Bean
    fun validator(): LocalValidatorFactoryBean {
        return LocalValidatorFactoryBean()
    }
    
    @Bean
    fun methodValidationPostProcessor(): MethodValidationPostProcessor {
        val processor = MethodValidationPostProcessor()
        processor.setValidator(validator())
        return processor
    }
}
