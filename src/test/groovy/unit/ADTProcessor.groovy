package unit

import junit.framework.TestSuite
import org.junit.Before
import org.junit.Test
import support.MessageGenerator
import support.Patient
import uk.co.neovahealth.ARNIE.ADTProcessing

/**
 * Created by gregorlenz on 03/12/15.
 */
class ADTProcessor extends TestSuite{
    MessageGenerator gen
    Patient dummy1
    ADTProcessing processor

    @Before
    void setUp() {
        gen = new MessageGenerator()
        processor = new ADTProcessing()
        dummy1 = new Patient(nhsNumber: 1223334444, hospitalNumber: '012345', familyName: 'Dummy', givenName: 'Dillon', dateOfBirth: '19441231090000', sex:'M', address: 'Main Street', admitLocation: '08BS')
    }

    @Test
    void testGetValue() {
        def msg02 = gen.createMessage('A01', dummy1, '2.4')
        assert processor.getValueFromFields(msg02, 'PID', 4, 1).toString() == dummy1.hospitalNumber
    }
}
