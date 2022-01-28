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
@ConfigurationProperties(prefix = "databases.users")
class UserDatabaseConfig {

  @Value('${databases.users.user.name}')
  String user
  @Value('${databases.users.user.password}')
  String password
  @Value('${databases.users.database.url}')
  String url
  @Value('${databases.users.database.dialect}')
  String sqlDialect
  @Value('${databases.users.database.driver}')
  String driver


}
