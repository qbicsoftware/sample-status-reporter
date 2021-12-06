package life.qbic.samplestatus.reporter.services.users.database

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * <b>Configuration for the user database</b>
 *
 * @since 1.0.0
 */
@Component
@ConfigurationProperties
class UserDatabaseConfig implements DatabaseConfig {

    @Value('${databases.users.user.name}')
    String user
    @Value('${databases.users.user.password}')
    String password
    @Value('${databases.users.database.host}')
    String host
    @Value('${databases.users.database.port}')
    String port
    @Value('${databases.users.database.name}')
    String databaseName
}