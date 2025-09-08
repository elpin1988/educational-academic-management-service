# Implementación de Repositorios con Exposed ORM

## Estado Actual

Los repositorios están **IMPLEMENTADOS** usando el **API de DAO de Exposed ORM** siguiendo el patrón exitoso del proyecto `attendance-service`. La implementación incluye operaciones CRUD completas y consultas básicas, con algunas consultas complejas pendientes de implementación.

## Implementación Realizada

### 1. Patrón de Entidades
```kotlin
// GradeEntity con métodos toDomain()
class GradeEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<GradeEntity>(GradeTable)
    
    var name by GradeTable.name
    var description by GradeTable.description
    var level by GradeTable.level
    var isActive by GradeTable.isActive
    var createdAt by GradeTable.createdAt
    var updatedAt by GradeTable.updatedAt
    
    fun toDomain(): Grade {
        return Grade(
            id = id.value,
            name = name,
            description = description,
            level = level,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
```

### 2. Mappers para Conversión
```kotlin
object GradeMapper {
    fun toDomain(entity: GradeEntity): Grade {
        return Grade(
            id = entity.id.value,
            name = entity.name,
            description = entity.description,
            level = entity.level,
            isActive = entity.isActive,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(domain: Grade): GradeEntity {
        return if (domain.id != null) {
            GradeEntity.findById(domain.id) ?: throw IllegalArgumentException("Grade with id ${domain.id} not found")
        } else {
            GradeEntity.new {
                this.name = domain.name
                this.description = domain.description
                this.level = domain.level
                this.isActive = domain.isActive
                this.createdAt = domain.createdAt
                this.updatedAt = domain.updatedAt
            }
        }
    }
}
```

### 3. Configuración de Base de Datos
```kotlin
@Configuration
class DatabaseConfig {
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
        return Database.connect(
            url = databaseUrl,
            driver = databaseDriver,
            user = databaseUsername,
            password = databasePassword
        )
    }
}
```

## Operaciones Implementadas

### ✅ Operaciones CRUD Completas
- **Create**: `GradeEntity.new { }` y `StudentGradeEntity.new { }`
- **Read**: `GradeEntity.findById()`, `GradeEntity.find { }`, `GradeEntity.all()`
- **Update**: Modificación directa de propiedades de entidades
- **Delete**: `entity.delete()`

### ✅ Consultas Básicas
- **Por ID**: `GradeEntity.findById(id)`
- **Por nombre**: `GradeEntity.find { GradeTable.name eq name }`
- **Por nivel**: `GradeEntity.find { GradeTable.level eq level }`
- **Todas las entidades**: `GradeEntity.all()`
- **Con condiciones simples**: `GradeEntity.find { GradeTable.isActive eq true }`

### ⚠️ Consultas Complejas Pendientes
- **Múltiples condiciones**: `(StudentGradeTable.studentId eq studentId) and (StudentGradeTable.isActive eq true)`
- **Operadores complejos**: `isNull()`, `lessEq()`, `greater()`, `or()`
- **Consultas de rango de fechas**: Requieren sintaxis específica de Exposed

## Próximos Pasos

1. **Implementar consultas complejas** con múltiples condiciones
2. **Probar con base de datos real** (PostgreSQL)
3. **Agregar tests de integración** con Testcontainers
4. **Optimizar consultas** para mejor rendimiento

## Notas Técnicas

- **Versión de Exposed**: 0.50.1
- **Base de datos**: H2 (tests), PostgreSQL (producción)
- **Patrón**: DAO API de Exposed (Entity-based)
- **Transacciones**: Usar `transaction { }` para todas las operaciones
- **Mappers**: Conversión entre entidades de dominio y entidades de Exposed
