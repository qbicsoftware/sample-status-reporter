package life.qbic.samplestatus.reporter

import life.qbic.samplestatus.reporter.api.LimsQueryService
import life.qbic.samplestatus.reporter.api.UpdateSearchService
import life.qbic.samplestatus.reporter.commands.ReportSinceInstant
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import picocli.CommandLine

/**
 * <b>Main application class</b>
 *
 * <p>The entry-point of the reporter application.</p>
 *
 * @since 0.1.0
 */
@SpringBootApplication
class ReporterApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ReporterApp.class)

    @Autowired
    ApplicationContext applicationContext

    static void main(String[] args) {
        SpringApplication.run(ReporterApp.class, args)
    }

    @Override
    void run(String... args) throws Exception {
        UpdateSearchService updateSearchService = applicationContext.getBean("lastUpdateSearch")
        LimsQueryService limsQueryService = applicationContext.getBean("realLimsQueryService")
        SampleStatusReporter statusReporter = applicationContext.getBean("qbicSampleStatusReporter")

        ReportSinceInstant reportSinceInstant = new ReportSinceInstant(limsQueryService, statusReporter, updateSearchService)

        int exitCode = 1
        try {
            exitCode = new CommandLine(reportSinceInstant).execute(args)
        } catch (Exception e) {
            log.error(e.getMessage(), e)
        }
        System.exit(exitCode)
    }
}
