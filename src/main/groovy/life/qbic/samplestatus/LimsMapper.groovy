package life.qbic.samplestatus

import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import java.util.Date
import java.util.HashMap
import java.util.List
import java.util.Map
import life.qbic.samplestatus.reporter.SampleUpdate

/**
 * <b>Creates a SampleUpdate from an openBIS sample fetched from the LIMS</b>
 *
 *
 * <p>This class is used to extract the sample information from the LIMS that is necessary to update sample tracking information.
 *    These are the sample barcode (named so because the sample code in the LIMS is not the correct code!), the modification date and the status of the sample.
 *    The type of LIMS model is used to distinguis between naming differences between the labs. This class does not make decisions based on which status should
 *    be updated. This decision might be taken elsewhere, as the default status 'METADATA_REGISTERED´ could overwrite later, lab-independent statuses.</p>
 *
 * @since <version tag>
 */
class LimsMapper {

    public static SampleUpdate createSampleUpdate(Sample limsSample) {
        
        Map<String,String> properties = limsSample.getProperties()
        String sampleBarcode = properties.get("QBIC_BARCODE")

        life.qbic.samplestatus.reporter.Sample sample = new life.qbic.samplestatus.reporter.Sample(sampleBarcode)
        
        Date modificationDate = sample.getModificationDate()
        Status sampleStatus = mapSampleStatus(properties.get("SAMPLE_STATUS"))

        return new SampleUpdate(sample = sample, updatedStatus = sampleStatus, modificationDate = modificationDate)
    }

    private String mapSampleStatus(String statusString) {
        switch (statusString) {
            case "SAMPLE_RECEIVED":
                return statusString
            case "QC_PASSED":
                return "SAMPLE_QC_PASS"
            case "QC_FAILED":
                return "SAMPLE_QC_FAIL"
            case "LIBRARY_PREP_FINISHED":
                return statusString
            default:
                return "METADATA_REGISTERED"
            }
    }
}