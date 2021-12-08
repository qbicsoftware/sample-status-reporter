package life.qbic.samplestatus.reporter.services.users

import life.qbic.samplestatus.reporter.api.Person

/**
 * <b>Provides user information</b>
 *
 * @since 1.0.0
 */
interface UserService {
    /**
     * Retrieves user details for a provided userId from the persistence layer.
     * @param userId the identifier of the user
     * @return user details for a user found.
     * @since 1.0.0
     */
    Optional<Person> getPerson(String userId)

}