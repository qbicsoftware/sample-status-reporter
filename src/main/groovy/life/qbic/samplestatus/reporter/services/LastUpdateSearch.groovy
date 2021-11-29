package life.qbic.samplestatus.reporter.services

import life.qbic.samplestatus.reporter.api.UpdateSearchService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.text.SimpleDateFormat
import java.time.Instant

@Component
class LastUpdateSearch implements UpdateSearchService {

    @Value('${service.last-update.file}')
    String filePath

    SimpleDateFormat isoFormat

    private Instant lastSearch

    @PostConstruct
    void init() {
        // Read the file content
        String lastSearchDate = new File(filePath).getText('UTF-8').trim()

        // Parse the first line as date with format "YYYY-mm-dd'T'HH:mm:ssZ" (ISO 8601)
        // For example "2021-12-01T12:00:00+0200"
        this.isoFormat = new SimpleDateFormat('yyyy-MM-dd\'T\'HH:mm:ssZ')
        Date date = isoFormat.parse(lastSearchDate)

        // Set lastSearch field
        lastSearch = date.toInstant()
    }

    /**
     * @inheritDocs
     */
    @Override
    Instant getLastUpdateSearchTimePoint() {
        return this.lastSearch
    }

    /**
     * @inheritDocs
     */
    @Override
    void saveLastSearchTimePoint(Instant lastSearchTimePoint) {
        Date date = Date.from(lastSearchTimePoint)
        new File(filePath).withWriter {
            it.write(isoFormat.format(date))
            lastSearch = lastSearchTimePoint
        }
    }
}
