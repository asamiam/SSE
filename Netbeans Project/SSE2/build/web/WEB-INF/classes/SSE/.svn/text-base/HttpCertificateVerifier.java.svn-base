package SSE;

import java.util.Calendar;
import java.util.Date;

/**
 * This class is called to verify the authenticity of certificates for a domain.
 * It will check the database for existing entries, if it exists in the database
 * and has already been checked previously, it will return the previously stored
 * result. If it has not been previously checked it will parse the certificate
 * now and store the result for future use. If no entry in the database exists
 * it will need to call an on-demand scan (to be implemented).
 *
 * @author joe
 */
public class HttpCertificateVerifier {

    /**
     * This function makes up the major functions of the class, it will perform
     * all of the checks to the database and the certificate parsing.
     *
     * @param strUrl The domain to be checked
     * @return The boolean result of the certificate check
     */
    public boolean verifyOnline(String strUrl) {

        boolean flag = false;
        String domainName;

        /*
         * Strip any http prefix that can interfere with domain searches
         */
        String[] domainTemp;
        if (strUrl.startsWith("https://") || strUrl.startsWith("http://")) {
            domainTemp = strUrl.split("//");
            strUrl = domainTemp[1];
        }

        /*
         * Strip anything following the domain, e.g. page names
         */
        String[] url = strUrl.split("/");
        domainName = url[0];

        /*
         * Check the database for any existing entries for the domain
         */
        DatabaseStatus result = SSEDAO.checkIfSSLVerified(domainName);

        /*
         * Parse the result from the database
         */
        switch (result) {

            /*
             * Certificate was previously checked and is authentic
             */
            case YES:
                flag = true;
                break;
            /*
             * Certificate was previously checked and is not authentic
             */
            case NO:
                flag = false;
                break;
            /*
             * Entry exists for the domain but certificate was not previously
             * checked, so we must parse the certificate now
             */
            case NOTFLAGGED:
                /*
                 * Get certificates from the database
                 */
                DomainDetail dd = SSEDAO.getDomainDetailForSSLVerified(domainName);
                Certificate[] certificates = dd.getCertificates();

                /*
                 * Establish a current date object to compare times with
                 */
                Calendar calendar = Calendar.getInstance();

                Date currentDate = calendar.getTime();

                for (int i = 0; i < certificates.length; i++) {

                    /*
                     * Compare validFrom and validTill dates to see if they are
                     * still active
                     */
                    Date fromDate = certificates[i].getValidFrom();
                    Date toDate = certificates[i].getValidTill();

                    int dateComparison = currentDate.compareTo(fromDate);

                    if (dateComparison < 0) {
                        break;
                    }

                    dateComparison = currentDate.compareTo(toDate);

                    if (dateComparison > 0) {
                        break;
                    }

                    /*
                     * Check if the domain name is present in the subject
                     */
                    if (certificates[i].getSubject().indexOf(domainName) < 0) {
                        break;
                    }

                    flag = true;

                }

                /*
                 * Store the result in the database for future checks
                 */
                if (flag == true) {
                    result = DatabaseStatus.YES;
                } else {
                    result = DatabaseStatus.NO;
                }
                SSEDAO.storeSSLVerifiedResults(dd.getID(), result);

                break;
            /*
             * Entry does not exist in database, need to call on-demand scan
             */
            case NOTINDB:
                /*
                 * To be implemented
                 */
                /*
                 * Runtime.getRuntime().exec('../../crawler.py');
                 */
                break;

        }

        /*
         * Return the result obtained above
         */
        return flag;
    }
}
