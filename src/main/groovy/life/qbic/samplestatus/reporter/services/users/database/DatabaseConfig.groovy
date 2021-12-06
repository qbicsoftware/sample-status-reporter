package life.qbic.samplestatus.reporter.services.users.database
/**
 * <h1><short description></h1>
 *
 * <p><detailed description></p>
 *
 * @since <version tag>
 */
interface DatabaseConfig {
    String getUser()
    String getPassword()
    String getHost()
    String getPort()
    String getDatabaseName()
}