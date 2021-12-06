package life.qbic.samplestatus.reporter.services.users

import life.qbic.samplestatus.reporter.api.UserDetails
import life.qbic.samplestatus.reporter.services.users.database.ConnectionProvider
import life.qbic.samplestatus.reporter.services.users.database.UserDatabaseConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.sql.Connection
import java.sql.ResultSet

/**
 * <b>A user service providing interaction with QBiC users</b>
 *
 * @since 1.0.0
 */
@Component
class QbicUserService  implements UserService {

    @Autowired
    UserDatabaseConfig userServiceConfig

    @Autowired
    ConnectionProvider connectionProvider

    @Override
    UserDetails getUserDetails(String userId) {
        try (Connection connection = connectionProvider.connect()) {
            var statement = connection.prepareStatement("SELECT * FROM person WHERE user_id = ?")
            statement.setString(1, userId)
            ResultSet result = statement.executeQuery()
            String firstName = result.getString("first_name")
            String lastName = result.getString("last_name")
            String email = result.getString("email")
            return new UserDetails(firstName, lastName, email)
        }
    }
}
