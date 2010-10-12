<%-- 
    Document   : index
    Created on : Jan 27, 2010, 9:22:16 PM
    Author     : joe
--%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Secure Search Engine (SSE)</title>
        <link rel="stylesheet" type="text/css" href="styles.css">
    </head>
    <body>

        <div id="outerWrapper">
            <div id="header">
                <div id="title"><html:link forward="Index">Secure Search Engine (SSE)</html:link></div>
            </div>
            <div id="navcontainer">

                <div id="nav">
                    <ul>
                        <li><html:link forward="Index">SSE Home</html:link></li>
                        <li><html:link forward="Download">Download</html:link></li>
                        <li><html:link forward="FAQ">FAQ</html:link></li>
                    </ul>
                </div>
            </div>
            <!-- To change from a 2 column right sidebar to a 2 column left sidebar layout change the id below to "twoColumnleft".
  		To make a 3 column layout with both left and right sidebars, change it to "threeColumns". -->
            <div id="twoColumnleft"><!-- Start left column -->
                <div id="leftColumn">

                    <div class="styledmenu"></div>
                    <!-- InstanceBeginEditable name="left column content" --> <!-- InstanceEndEditable --></div>
                <!-- End left column --> <!-- Start right column --> <!-- End right column -->
                <!-- Start content -->
                <div id="content"><!-- InstanceBeginEditable name="content" -->

                    <html:form action="/search">
                        <table border="0">
                            <tbody>
                                <tr>
                                    <td colspan="2">
                                        <bean:write name="SearchForm" property="error" filter="false"/>
                                        &nbsp;</td>
                                </tr>

                                <tr>
                                    <td>URL:</td>
                                    <td><html:text property="name" /></td>
                                </tr>

                                <tr>
                                    <td></td>
                                    <td><html:submit value="Submit Query" /></td>
                                </tr>
                            </tbody>
                        </table>
                    </html:form>
                </div>
            </div>
        </div>
    </body>
</html>
