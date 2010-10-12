<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>

    <!-- InstanceBegin template="/Templates/smoothcss.dwt" codeOutsideHTMLIsLocked="false" -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <!-- InstanceBeginEditable name="title/meta" -->
        <title>Secure Search Engine (SSE)</title>
        <meta name="Description" content="" />
        <meta name="Keywords" content="" />
        <!-- InstanceEndEditable -->
        <link href="styles.css" rel="stylesheet" type="text/css" />
        <!-- InstanceBeginEditable name="head" -->
        <!-- InstanceEndEditable -->

    </head>

    <body>
        <div id="outerWrapper">
            <div id="header">
                <div id="title"><a href="index.jsp">Secure Search Engine (SSE)</a></div>
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
                    <table>
                        <tr>
                            <td>
                                <h2>What is this project all about?</h2>
                                <p>SSE enforces an HTTPS connection whenever a web site supports secure
                                    connection. Now, it is hosted at <a href="https://www.wreferral.com">wreferral.com</a> (originally
                                    <a href="https://securesearch.eas.asu.edu">securesearch.eas.asu.edu</a>). SSE make the web browsing secure by
                                    preventing Man-In-The-Middle attacks (e.g., SSLStrip) and warning when
                                    a user goes to phishing site.This project is conducted by Secure
                                    Networking And Computing <a href="http://snac.eas.asu.edu">(SNAC)</a>  research group. </p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h2>What is MITM attack?</h2>
                                <p>The man-in-the-middle attack (often abbreviated MITM), or
		bucket-brigade attack, or sometimes Janus attack, is a form of active
		eavesdropping in which the attacker makes independent connections with
		the victims and relays messages between them, making them believe that
		they are talking directly to each other over a private connection when
		in fact the entire conversation is controlled by the attacker. The
		attacker must be able to intercept all messages going between the two
		victims and inject new ones, which is straightforward in many
		circumstances (for example, an attacker within a few miles of an
		unencrypted Wi-Fi wireless access point, can insert himself as a
		man-in-the-middle). (Definition from wikipedia)<a
                                        href="http://en.wikipedia.org/wiki/Man-in-the-middle_attack">More
		Details</a></p>
                            <td>
                        </tr>
                        <tr>
                            <td>
                                <h2>What is Phishing attack?</h2>
                                <p>Phishing is the criminally fraudulent process of attempting to
		acquire sensitive information such as usernames, passwords and credit
		card details by masquerading as a trustworthy entity in an electronic
		communication. (Definition from wikipedia)<a
                                        href="http://en.wikipedia.org/wiki/Phishing">More Details</a></p>
                            <td>
                        </tr>
                        <tr>
                            <td>
                                <h2>Can you give us an example describing how SSE works?</h2>
                                <p>Ok! When you open www.usbank.com, in your IE you would notice
		that the site loads with HTTP protocol though usbank.com supports
		HTTPS. Now, try it with SSE enabled Firefox, you would notice that the
		connection is secure(HTTPS). Here is another example.</p>
                                <!--<p>Let us assume Alice wants to visit <a href="www.bankofamerica.com">www.bankofamerica.com</a>. Alice types-in the URL in her favorite browser. (Behind the scene: Firefox requests for the bank of america's web page. Now, the bank of america's web-server will send a 301 redirect along with the new URL to the browser . The browser connects to the new URL, which forces a secure connection.)</p>-->
                                <p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h2>So, does it forces HTTPS on all the website?</h2>
                                <p>No, SSE forece HTTPS only if the web site supports a HTTPS
		connection</p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h2>Is this extension available only for Firefox users?</h2>
                                <p>Chill! Extension for IE will be available soon.</p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h2>How do I install this?</h2>
                                <p><a href="https://www.wreferral.com/downloads.html">https://www.wreferral.com/downloads.html</a></p>
                            </td>
                        </tr>
                    </table>
                    <!-- InstanceEndEditable --></div>
                <!-- End content -->
                <div class="clear"></div>
            </div>
            <!--<div id="footer">Copyright &copy; 2009 Email Trust</div> --> <!--The following code must be left in place and unaltered for free usage of this theme. If you wish to remove the links, visit http://www.justdreamweaver.com/dreamweaver-templates.html and purchase a template license. It's only $19.99 and it helps encourage future template creation.-->
            <div id="credit"><a
                    href="http://www.justdreamweaver.com/dreamweaver-templates.html">Website
                    templates</a> by JustDreamweaver.com</div>
        </div>
    </body>
    <!-- InstanceEnd -->
</html>
