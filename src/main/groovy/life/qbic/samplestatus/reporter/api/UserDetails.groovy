package life.qbic.samplestatus.reporter.api

/**
 * <p>A users object that represents a users responsible for a location</p>
 *
 * @since <version tag>
 */
class UserDetails {
    String firstName
    String lastName
    String email

    UserDetails(String firstName, String lastName, String email) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
    }

    String getFullName() {
        return getFirstName() + " " + getLastName()
    }
}
