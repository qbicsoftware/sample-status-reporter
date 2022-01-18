package life.qbic.samplestatus.reporter

import java.time.Instant

/**
 * <b>Value Class SampleUpdate</b>
 *
 * @since 0.1.0
 */
class SampleUpdate {
    Sample sample
    String updatedStatus
    Instant modificationDate

    @Override
    String toString() {
        return "SampleUpdate{" +
                "sample=" + sample +
                ", updatedStatus='" + updatedStatus + '\'' +
                ", modificationDate=" + modificationDate +
                '}'
    }
}
