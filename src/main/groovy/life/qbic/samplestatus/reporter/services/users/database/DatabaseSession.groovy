package life.qbic.samplestatus.reporter.services.users.database

import life.qbic.samplestatus.reporter.api.Person
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
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
        config[Environment.DRIVER] = dbConfig.getDriver()
        config[Environment.URL] = dbConfig.getUrl()
        config[Environment.USER] = dbConfig.getUser()
        config[Environment.PASS] = dbConfig.getPassword()
        config[Environment.POOL_SIZE] = 1
        config[Environment.DIALECT] = dbConfig.getSqlDialect()
        config[Environment.CURRENT_SESSION_CONTEXT_CLASS] = "thread"

        config.setProperties(properties)

        config.addAnnotatedClass(Person.class).buildSessionFactory()
    }

    @Override
    Session getCurrentSession() {
        return sessionFactory.getCurrentSession()
    }

    @PreDestroy
    void destroy() {
        println "Closing session factory..."
        sessionFactory.close()
    }
}
