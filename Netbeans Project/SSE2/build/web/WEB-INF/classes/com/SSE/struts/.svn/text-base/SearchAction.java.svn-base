package com.SSE.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This class is used by Struts when a submit is performed on
 * the search form.
 *
 * @author joe
 */
public class SearchAction extends org.apache.struts.action.Action {

    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";

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

        /*
         * Grab site name from form bean
         */
        SearchForm formBean = (SearchForm) form;
        String name = formBean.getName();

        /*
         * Check to make sure name is not empty
         */
        if ((name == null) || // name parameter does not exist
                name.equals("") // name parameter is empty
                ) {

            formBean.setError(1);
            return mapping.findForward(FAILURE);
        }

        /*
         * Check the site's certificate validity
         */
        SSE.HttpCertificateVerifier verifier = new SSE.HttpCertificateVerifier();

        if (!verifier.verifyOnline(name)) {
            formBean.setError(2);
            return mapping.findForward(FAILURE);
        }

        /*
         * Check the site's phishing status
         */
        SSE.PhishingFilter filter = new SSE.PhishingFilter();

        /*if(filter.isPhishing(name)) {
            formBean.setError(2);
            return mapping.findForward(FAILURE);
        }*/

        return mapping.findForward(SUCCESS);
    }
}
