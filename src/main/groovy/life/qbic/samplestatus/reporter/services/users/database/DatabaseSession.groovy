package life.qbic.samplestatus.reporter.services.users.database

import life.qbic.samplestatus.reporter.api.Person
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


/**
 * Creates a connection to the user database
 *
 * A class for setting up the connection to the user database. It should be used when data needs to be retrieved from the
 * DB or written into it.
 *
 * @since: 1.0.0
 * @author: Jennifer Bödker
 *
 */
@Singleton(lazy = true)
@Component
class DatabaseSession implements SessionProvider {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSession.class)

    @Autowired
    private UserDatabaseConfig userDatabaseConfig

    private SessionFactory sessionFactory

    @PostConstruct
    void init() {
        sessionFactory = initHibernate(userDatabaseConfig)
    }

    private static SessionFactory initHibernate(UserDatabaseConfig dbConfig) {
        var config = new Configuration()
        var properties = new Properties()
        println dbConfig.getDriver()
        properties[Environment.DRIVER] = dbConfig.getDriver()
        properties[Environment.URL] = dbConfig.getUrl()
        properties[Environment.USER] = dbConfig.getUser()
        properties[Environment.PASS] = dbConfig.getPassword()
        properties[Environment.POOL_SIZE] = 1
        properties[Environment.DIALECT] = dbConfig.getSqlDialect()
        properties[Environment.CURRENT_SESSION_CONTEXT_CLASS] = "thread"

        config.setProperties(properties)

        config.addAnnotatedClass(Person.class).buildSessionFactory()
    }

    @Override
    Session getCurrentSession() {
        return sessionFactory.getCurrentSession()
    }

    @PreDestroy
    void destroy() {
        log.debug("Closing session factory...")
        sessionFactory.close()
    }
}
