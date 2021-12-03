package life.qbic.samplestatus.reporter.services.utils

import java.util.function.Function

/**
 * <b>Class SampleStatusMapper<b>
 *
 * <p>Takes a String value and tries to map it to a known sample status.</p>
 *
 * @since 0.1.0
 */
class SampleStatusMapper implements Function<String, String>{

    private static final String SAMPLE_RECEIVED = "SAMPLE_RECEIVED";
    private static final String SAMPLE_QC_PASS = "SAMPLE_QC_PASS";
    private static final String SAMPLE_QC_FAIL = "SAMPLE_QC_FAIL";
    private static final String LIBRARY_PREP_FINISHED = "LIBRARY_PREP_FINISHED";

    /**
     * <p>Tries to map a String value to a known sample status.</p>
     * @param s the String value you want to have mapped
     * @return the mapped String value
     */
    @Override
    String apply(String s) {
        return mapSampleStatus(s)
    }

    private String mapSampleStatus(String statusString) throws MappingException {
        if (statusString.isEmpty()) {
            throw new MappingException("Status value is empty.")
        }
        switch (statusString) {
            case "SAMPLE_RECEIVED":
                return SAMPLE_RECEIVED
            case "QC_PASSED":
                return SAMPLE_QC_PASS
            case "QC_FAILED":
                return SAMPLE_QC_FAIL
            case "LIBRARY_PREP_FINISHED":
                return LIBRARY_PREP_FINISHED
            default:
                throw new MappingException("Cannot map unkown satus value: $statusString.")
        }
    }

    /**
     * <b>Class MappingException</b>
     * <p>Small mapping exception class that can be used when sample status mapping fails</p>
     */
    class MappingException extends RuntimeException {

        MappingException(String message) {
            super(message)
        }
    }
}


