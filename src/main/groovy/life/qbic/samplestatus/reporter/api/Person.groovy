package life.qbic.samplestatus.reporter.api

import groovy.transform.ToString

import javax.persistence.*

/**
 * <p>Represents a user responsible for a location</p>
 *
 * @since 1.0.0
 */
@ToString
@Entity
@Table(name = "person")
class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id

    @Column(name = "user_id")
    String userId

    @Column(name = "first_name")
    String firstName

    @Column(name = "last_name")
    String lastName

    @Column(name = "title")
    String title

    @Column(name = "email")
    String email

    @Column(name = "active")
    Integer active

    Person() {}

    Person(String userId, String firstName, String lastName, String title, String email, Integer active) {
        this.userId = userId
        this.firstName = firstName
        this.lastName = lastName
        this.title = title
        this.email = email
        this.active = active
    }

    Integer getId() {
        return id
    }

    void setId(Integer id) {
        this.id = id
    }

    String getUserId() {
        return userId
    }

    void setUserId(String userId) {
        this.userId = userId
    }

    String getFirstName() {
        return firstName
    }

    void setFirstName(String firstName) {
        this.firstName = firstName
    }

    String getLastName() {
        return lastName
    }

    void setLastName(String lastName) {
        this.lastName = lastName
    }

    String getTitle() {
        return title
    }

    void setTitle(String title) {
        this.title = title
    }

    String getEmail() {
        return email
    }

    void setEmail(String email) {
        this.email = email
    }

    Integer getActive() {
        return active
    }

    void setActive(Integer active) {
        this.active = active
    }
}
