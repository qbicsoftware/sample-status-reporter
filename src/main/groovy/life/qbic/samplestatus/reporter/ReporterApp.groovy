package life.qbic.samplestatus.reporter


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext

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
        SampleUpdate update = new SampleUpdate()
        update.setSample(new Sample("QSTTS002A3"))
        update.setUpdatedStatus("SAMPLE_QC_FAIL")
        SampleStatusReporter statusReporter = applicationContext.getBean("qbicSampleStatusReporter", SampleStatusReporter.class)
        println statusReporter
        statusReporter.reportSampleStatusUpdate(update)
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
