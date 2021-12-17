package life.qbic.samplestatus.reporter.services.users.database

import org.hibernate.Session

/**
 * Provides the ability to connect to a SQL ressource
 *
 * @since: 1.0.0
 */
interface SessionProvider {
    Session getCurrentSession()
}
