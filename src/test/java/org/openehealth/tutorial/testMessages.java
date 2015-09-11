package org.openehealth.tutorial;

import java.util.Random;

/**
 * Created by gregorlenz on 11/09/15.
 */
public class testMessages {
    public static String getRandomNumber(int digCount) {
        StringBuilder sb = new StringBuilder(digCount);
        for(int i=0; i < digCount; i++)
            sb.append((char)('0' + rnd.nextInt(10)));
        return sb.toString();
    }

    private static Random rnd = new Random();
    private static String visitID = getRandomNumber(10);
    private static String visitID2 = "012345";
    private static final String patientOneId = "012345";
    private String PID1 = "PID|1|^^^^PAS||" + patientOneId + "^\"\"^^RDD^HOSP~652 639 8685^NSTS01^^NHS^NHS~QD1320147-1^^^RDD^EVO|DUMMY^Jones Jimbo^^^Mr||19740613000000|F||||||||||||||B^White Irish|||||||\"\"|N";
    private static final String patientTwoId = "012345";
    private static String PID2 = "PID|1|^^^^PAS||" + patientTwoId + "|Jones^Jimbo^^^Badman||19740613000000|F||||||||||||||B^White Irish|||||||\"\"|N";
    private static String WARD = "A";
    private String WARD2 = "B";

    private static String testA01 = "MSH|^~\\&|iPM|iIE|Wardware|Wardware|20120105105702||ADT^A01|607639|P|2.4|||AL|AL\r" +
            "EVN|A01|20120105105702||||20120105105702\r" +
            PID2 + "\r" +
            "PV1|1|I|" + WARD + "^^^^^^^^Cobham Clinic|11||^^^^^^^^|^^^^^|C6035630^Ahmed^R^^^Dr|C5205403^Abdunabi^M^^^Mr|110|||||||C5205403^Abdunabi^M^^^Mr|01|" + visitID + "|||||||||||||||||||||||||20120103090000\r" +
            "AL1|1|CLIN|ISP^MRSA Positive (P)|||20111214";

    public static String getTestA01() {
        return testA01;
    }



    public String getVisitID() {
        return visitID;
    }

    public void setVisitID(String visitID) {
        this.visitID = visitID;
    }

    public String getVisitID2() {
        return visitID2;
    }

    public void setVisitID2(String visitID2) {
        this.visitID2 = visitID2;
    }

    public final String getPatientOneId() {
        return patientOneId;
    }

    public String getPID1() {
        return PID1;
    }

    public void setPID1(String PID1) {
        this.PID1 = PID1;
    }

    public final String getPatientTwoId() {
        return patientTwoId;
    }

    public String getPID2() {
        return PID2;
    }

    public void setPID2(String PID2) {
        this.PID2 = PID2;
    }

    public String getWARD() {
        return WARD;
    }

    public void setWARD(String WARD) {
        this.WARD = WARD;
    }

    public String getWARD2() {
        return WARD2;
    }

    public void setWARD2(String WARD2) {
        this.WARD2 = WARD2;
    }

    public String getPatientNewADT28() {
        return patientNewADT28;
    }

    public void setPatientNewADT28(String patientNewADT28) {
        this.patientNewADT28 = patientNewADT28;
    }

    public String getPatientNewADT05() {
        return patientNewADT05;
    }

    public void setPatientNewADT05(String patientNewADT05) {
        this.patientNewADT05 = patientNewADT05;
    }

    public String getPatientUpdateADT_31() {
        return patientUpdateADT_31;
    }

    public void setPatientUpdateADT_31(String patientUpdateADT_31) {
        this.patientUpdateADT_31 = patientUpdateADT_31;
    }

    public String getPatientUpdateADT_08() {
        return patientUpdateADT_08;
    }

    public void setPatientUpdateADT_08(String patientUpdateADT_08) {
        this.patientUpdateADT_08 = patientUpdateADT_08;
    }

    public String getPatientUpdateADT_08H() {
        return patientUpdateADT_08H;
    }

    public void setPatientUpdateADT_08H(String patientUpdateADT_08H) {
        this.patientUpdateADT_08H = patientUpdateADT_08H;
    }

    public String getPatientMerge() {
        return patientMerge;
    }

    public void setPatientMerge(String patientMerge) {
        this.patientMerge = patientMerge;
    }


    public void setTestA01(String testA01) {
        this.testA01 = testA01;
    }


    private String patientNewADT28 = "MSH|^~\\&|iPM|iIE|T4skr|T4skr|20120124135111||ADT^A28|609299|P|2.4|||AL|AL\r" + "EVN|A28|20120124135111\r" + PID1 + "\r" + "PD1|||Dummy Test Practice^GPPRC^TEST1X|G0000001^Dummy^Leigh^^^Dr";
    private String patientNewADT05 = "MSH|^~\\&|iPM|iIE|T4skr|T4skr|20131007152356.695+0100||ADT^A05|609299|T|2.4\r" + PID2 + "\r" + "PD1|||Dummy Test Practice^GPPRC^TEST1X|G0000001^Dummy^Leigh^^^Dr";
    private String patientUpdateADT_31 = "MSH|^~\\&|iPM|iIE|T4skr|T4skr|20120120143457||ADT^A31|609190|P|2.4|||AL|AL\r" + "EVN|A31|20120120143457\r" + PID1 + "\r" + "PD1|||Branch Practice, Gooseberry Hill HC, Luton^GPPRC^E81046B|G3403352B^Glaze^M E^^^Dr\r" + "AL1|1|CLIN|ISP^MRSA Positive (P)|||20101124";
    private String patientUpdateADT_08 = "MSH|^~\\&|||||20131007152356.695+0100||ADT^A08|201|T|2.4\r" + "EVN|A01|20120105105702||||20120105105702\r" + PID2 + "PV1|1|I|" + WARD + "^^^^^^^^Cobham Clinic|11||^^^^^^^^|^^^^^|C6035630^Ahmed^R^^^Dr|C5205403^He-Man^M^^^Mr|110|||||||C5205403^Skeletor^M^^^Mr|01|" + visitID + "|||||||||||||||||||||||||20120103090000";
    private String patientUpdateADT_08H = "MSH|^~\\&|||||20131007152356.695+0100||ADT^A08|201|T|2.4\r" + "EVN|A01|20120105105702||||20100105105702\r" + "PID|1|^^^^PAS||" + patientTwoId + "^\"\"^^RDD^HOSP~652 639 8685^NSTS01^^NHS^NHS~QD1320147-1^^^RDD^EVO|DUMMY^PATIENT BERYL^^^Miss||19740613000000|F||||||||||||||B^White Irish|||||||\"\"|N\r" + "PV1|1|I|" + WARD + "^^^^^^^^Cobham Clinic|11||^^^^^^^^|^^^^^|C6035630^Ahmed^R^^^Dr|C5205403^He-Man^M^^^Mr|110|||||||C5205403^Skeletor^M^^^Mr|01|" + visitID + "|||||||||||||||||||||||||20120103090000|20120203090000";
    private String patientMerge = "MSH|^~\\&|OTHER_IBM_BRIDGE_TLS|IBM|PAT_IDENTITY_X_REF_MGR_MISYS|ALLSCRIPTS|20090224104210-0600||ADT^A40^ADT_A39|4143361005927619863|P|2.4\r" + "EVN||20090224104210-0600\r" + "PID|1|||" + patientTwoId + "||OTHER_IBM_BRIDGE^MARION||19661109|F\r" + "MRG|" + patientOneId + "^^^IBOT&1.3.6.1.4.1.21367.2009.1.2.370&ISO";
}
