/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SSE.struts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author nbuser
 */
public class SearchForm extends org.apache.struts.action.ActionForm {

    private String name;
    // error message
    private String error;

    public String getError() {
        return error;
    }

    public void setError(int statusCode) {

        switch (statusCode) {

            case 1:
                this.error =
                        "<span style='color:red'>Please enter a URL in the search box</span>";
                break;
            case 2:
                this.error =
                        "<span style='color:red'>The URL entered is not safe.</span>";
                break;
            default:
                break;
        }

    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     *
     */
    public SearchForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getName() == null || getName().length() < 1) {
            errors.add("name", new ActionMessage("error.name.required"));
            // TODO: add 'error.name.required' key to your resources
        }
        return errors;
    }
}
