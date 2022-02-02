package life.qbic.samplestatus.reporter.services.users


import life.qbic.samplestatus.reporter.api.Person
import life.qbic.samplestatus.reporter.api.ServiceException
import life.qbic.samplestatus.reporter.services.users.database.SessionProvider
import life.qbic.samplestatus.reporter.services.users.database.UserDatabaseConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.hibernate.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * <b>A user service providing interaction with QBiC users</b>
 *
 * @since 1.0.0
 */
@Component
class QbicUserService implements UserService {

  @Autowired
  UserDatabaseConfig userServiceConfig

  @Autowired
  SessionProvider connectionProvider

  static Logger logger = LogManager.getLogger(QbicUserService.class)

  Optional<Person> getPerson(String userId) {
    return fetchPerson(userId)
  }

  private Optional<Person> fetchPerson(String userId) {
    try (Session session = connectionProvider.getCurrentSession()) {
      session.beginTransaction()
      var query = session.createQuery("FROM Person p WHERE p.userId = :user_id")
      query.setParameter("user_id", userId)
      var personsFound = query.getResultList() as List<Person>
      return Optional.ofNullable(personsFound.first())
    } catch (Exception e) {
      logger.error(e.getMessage(), e)
      throw new ServiceException("Unable to execute person search for person with id = $userId.")
    }
  }
}

