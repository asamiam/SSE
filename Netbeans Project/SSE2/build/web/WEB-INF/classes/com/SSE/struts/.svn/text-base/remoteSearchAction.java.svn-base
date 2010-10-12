package com.SSE.struts;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This class is used when the plugin performs a remote search
 * and requests validation on its url. Request should be of the form
 * <urlOfHost>:8080/remoteSearch.do?URL=<urlToBeSearched>
 *
 * @author joe
 */
public class remoteSearchAction extends org.apache.struts.action.Action {

    private static final String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String certificateFlag = "no";
        String phishingFlag = "no";
        String returnText = "";

        /*
         * Get requested URL
         */
        String name = request.getQueryString();


        /*
         * Check to make sure URL is not empty
         */
        if ((name != null) && // name parameter does not exist
                !name.equals("") // name parameter is empty
                ) {

            /*
             * Check the site's certificate validity
             */
            SSE.HttpCertificateVerifier verifier = new SSE.HttpCertificateVerifier();

            if (verifier.verifyOnline(name)) {
                certificateFlag = "yes";
            }

            /*
             * Check the site's phishing status
             */
            SSE.PhishingFilter filter = new SSE.PhishingFilter();

            /*if(!filter.isPhishing(name)) {
                phishingFlag = "yes";
            }*/

        }

        /*
         * Build the return values
         */
        returnText = certificateFlag + " " + phishingFlag;

        /*
         * Establish the stream to client
         */
        PrintWriter returnStream = response.getWriter();

        /*
         * Write the return values to client
         */
        returnStream.write(returnText);

        /*
         * Close the stream to client
         */
        returnStream.close();

        System.out.println(returnText);

        /*
         * Must return a forward for the ActionForward class
         */
        return mapping.findForward(SUCCESS);
    }
}
