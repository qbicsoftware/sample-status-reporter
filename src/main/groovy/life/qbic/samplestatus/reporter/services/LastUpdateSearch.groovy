package life.qbic.samplestatus.reporter.services

import life.qbic.samplestatus.reporter.api.UpdateSearchService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.time.Instant

@Component
class LastUpdateSearch implements UpdateSearchService {

  @Value('${service.last-update.file}')
  String filePath

  private Instant lastSearch

  @PostConstruct
  void init() {
    // Read the file content
    String lastSearchDate = new File(filePath).getText('UTF-8').trim()

    if (lastSearchDate) {
      // Parse the first line as date with format "YYYY-mm-dd'T'HH:mm:ss.SSSZ" (ISO 8601)
      // For example "2021-12-01T12:00:00.000Z"
      lastSearch = Instant.parse(lastSearchDate)
    }
  }

  /**
   * {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  Optional<Instant> getLastUpdateSearchTimePoint() {
    return Optional.ofNullable(this.lastSearch)
  }

  /**
   * {@inheritDoc}
   * @param lastSearchTimePoint {@inheritDoc}
   */
  @Override
  void saveLastSearchTimePoint(Instant lastSearchTimePoint) {
    new File(filePath).withWriter {
      it.write(lastSearchTimePoint.toString())
      lastSearch = lastSearchTimePoint
    }
  }
}
