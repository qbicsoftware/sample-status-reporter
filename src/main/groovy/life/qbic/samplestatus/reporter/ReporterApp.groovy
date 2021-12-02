package life.qbic.samplestatus.reporter

import life.qbic.samplestatus.reporter.api.Location
import life.qbic.samplestatus.reporter.api.LocationService
import life.qbic.samplestatus.reporter.api.UpdateSearchService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext

import java.time.Instant

/**
 * <b>Main application class</b>
 *
 * <p>The entry-point of the reporter application.</p>
 *
 * @since 0.1.0
 */
@SpringBootApplication
class ReporterApp implements CommandLineRunner {

    @Autowired
    ApplicationContext applicationContext

    static void main(String[] args) {
        SpringApplication.run(ReporterApp.class, args)
    }

    @Override
    void run(String... args) throws Exception {
        LocationService locationService = applicationContext.getBean("ncctLocationService", LocationService.class)
        Location location = locationService.getCurrentLocation()
                .orElseThrow({ new ReporterAppException("No current location found!") })

        UpdateSearchService updateSearchService = applicationContext.getBean("lastUpdateSearch", UpdateSearchService.class)
        updateSearchService.saveLastSearchTimePoint(Instant.now())
    }

    class ReporterAppException extends RuntimeException {
        ReporterAppException() {
            super()
        }

        ReporterAppException(String message) {
            super(message)
        }
    }
}
