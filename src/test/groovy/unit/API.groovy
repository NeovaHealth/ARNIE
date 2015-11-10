package unit

import junit.framework.TestSuite
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openehealth.tutorial.eObsCalls

/**
 * Created by gregorlenz on 06/11/15.
 */
class API extends TestSuite{
    private caller

    @Before
    void setUp() {
        caller = new eObsCalls()
    }

    @After
    void tearDown() {
        //delete patient data
    }

    @Test
    void visitExists() {
        assert caller.visitExists()
    }

    @Test
    void patientExist() {
        assert caller.patientExists()
    }

    @Test
    void patientAdmit() {
        assert caller.patientAdmit()
    }

    @Test
    void patientTransfer() {
        assert caller.patientTransfer()
    }

    @Test
    void patientDischarge() {
        assert caller.patientDischarge()
    }

    @Test
    void patientNew() {
        assert caller.patientNew()
    }

    @Test
    void patientUpdate() {
        assert caller.patientUpdate()
    }
}
