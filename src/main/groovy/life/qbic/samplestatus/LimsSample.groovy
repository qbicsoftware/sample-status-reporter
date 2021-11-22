package life.qbic.samplestatus

import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import java.util.Date
import java.util.HashMap
import java.util.List
import java.util.Map

import life.qbic.datamodel.samples.Status

/**
 * <b>Model of the sample properties stored in the LIMS. Provides fields and getters for sample tracking - related information.</b>
 *
 *
 * <p>This class is used to extract and model the sample information from the LIMS that is necessary to update sample tracking information.
 *    These are the sample barcode (named so because the sample code in the LIMS is not the correct code!), the modification date and the status of the sample.
 *    Any additional properties can be fetched via a map.</p>
 *
 * @since <version tag>
 */
class LimsSample {

    String sampleBarcode
    Date modificationDate
    Status sampleStatus
    Map<String,String> properties
    
    LimsSample(Sample sample) {
        this.properties = new HashMap<>()
        Map<String,String> rawProps = sample.getProperties()
        this.sampleBarcode = props.get("QBIC_CODE")
        this.modificationDate = s.getModificationDate()
        this.sampleStatus = parseSampleStatus(props.get("SAMPLE_STATUS"))

        cleanProperties(rawProps)
    }

    private void cleanProperties(Map<String, String> rawProps) {
        for(String prop : rawProps.keySet()) {
            this.properties.put(prop.toLowerCase().trim(), rawProps.get(prop).trim())
        }
    }

    private Status parseSampleStatus(String statusString) {
        switch (statusString) {
            case "SAMPLE_RECEIVED":
                return Status.SAMPLE_RECEIVED
            case "QC_PASSED":
                return Status.SAMPLE_QC_PASS
            case "QC_FAILED":
                return Status.SAMPLE_QC_FAIL
            case "LIBRARY_PREP_FINISHED":
                return Status.LIBRARY_PREP_FINISHED
            default:
                return Status.METADATA_REGISTERED
            }
    }
}
