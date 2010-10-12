package SSE;

import java.net.URLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.xml.sax.SAXException;
import java.io.IOException;

import com.enigmastation.classifier.FisherClassifier;
import com.enigmastation.classifier.Trainer;
import com.enigmastation.classifier.impl.FisherClassifierImpl;

public class PhishingFilter {

    private double ipCheckerResultWeight = 1;
    private double bayesResultWeight = 3;
    private double certificateResultWeight = 4;
    private double pageRankResultWeight = 2;
    private double threshold = 3;
    static private int dataCenterIdx = 0;
    static final public String[] GOOGLE_PR_DATACENTER_IPS = new String[]{ //List of available Google dataceneter IPs and addresses
        "64.233.183.91",
        "64.233.189.44",
        "66.102.9.115",
        "66.249.89.83",
        "toolbarqueries.google.com",};

    public boolean isPhishing(String strUrl) {

        boolean flag = false;
        String domainName = null;
        String domainFile = null;

        //Strip off the http.
        String[] domainTemp;
        if (strUrl.startsWith("https://") || strUrl.startsWith("http://")) {
            domainTemp = strUrl.split("//");
            strUrl = domainTemp[1];
        }

        //Strip off any pages after the main domain

        String[] url = strUrl.split("/");
        domainName = url[0];

        if (url.length > 1) {
            domainFile = url[1];
        } else {
            domainFile = "";
        }

        DomainDetail dd = SSEDAO.getDomainDetailForPhishing(domainName, domainFile);
        DomainFile[] dfs = dd.getDomainFiles();

        DomainFile df = null;

        for (DomainFile file : dfs) {
            if (file.getDomainFile().equals(domainFile)) {
                df = file;
                break;
            }
        }

        DatabaseStatus result = df.getIsPhishing();
        DatabaseStatus isPhishing;

        switch (result) {
            case YES:														// If result is yes, set flag[1] to true, it is phishing
                flag = true;
                break;
            case NO:																// If result is no, set flag[1] to false, not phishing
                flag = false;
                break;
            case NOTFLAGGED:														// If result is notflagged, run phishing filter alogrithm
                DatabaseStatus thirdPartyPhishing = df.getIsThirdPartyPhishing();

                switch (thirdPartyPhishing) {
                    case YES:
                        flag = true;
                        break;
                    case NO:
                        flag = phishingFilter(df, dd);

                        if (flag) {
                            isPhishing = DatabaseStatus.YES;
                        } else {
                            isPhishing = DatabaseStatus.NO;
                        }

                        SSEDAO.storePhishingResults(df.getID(), isPhishing);
                        break;
                    case NOTFLAGGED:
                        try {
                            DocumentBuilderFactory domFactory  = DocumentBuilderFactory.newInstance();
                            domFactory .setNamespaceAware(true);
                            DocumentBuilder builder = domFactory .newDocumentBuilder();
                            Document doc = builder.parse("verified_online.xml");

                            XPathFactory factory = XPathFactory.newInstance();
                            XPath xpath = factory.newXPath();
                            XPathExpression expr = xpath.compile("//url");

                            Object evalResult = expr.evaluate(doc, XPathConstants.NODESET);

                            NodeList nodes = (NodeList) evalResult;
                            for (int i = 0; i < nodes.getLength(); i++) {
                                if (df.getDomainFile().equals((nodes.item(i).getNodeValue()))) {
                                    flag = true;
                                    break;
                                }
                                System.out.println(nodes.item(i).getNodeValue());
                            }
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (XPathExpressionException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (flag == false) {
                            flag = phishingFilter(df, dd);
                        }

                        if (flag) {
                            isPhishing = DatabaseStatus.YES;
                        } else {
                            isPhishing = DatabaseStatus.NO;
                        }

                        SSEDAO.storePhishingResults(df.getID(), isPhishing);
                        break;
                }
                break;
            default:
                return flag;
        }

        return flag;
    }

    private double ipChecker(DomainFile df) {
        double result = -1;
        int t = 30;

        Date date = df.getLastPhishedTimeStamp();

        if (date != null) {
            Calendar cal = Calendar.getInstance();

            int daysBetween = 0;
            while (cal.before(date)) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                daysBetween++;
            }

            t = daysBetween;
        }

        if (t > 1) {
            result = 1 - (1 / Math.log(t));
        } else if (t < 1) {
            result = 1;
        }

        return result;
    }

    private double getProbabilityForPhishing(DomainFile df) {
        double result = -1;

        String pageText = df.getPageSource();

        pageText = pageText.toLowerCase();

        FisherClassifier fc = new FisherClassifierImpl();
        Trainer t = (Trainer) fc;

        for (int i = 1; i < 5; i++) {
            try {
                InputStream stream = getClass().getResourceAsStream("nonPhish" + i + ".txt");

                BufferedReader in = new BufferedReader(new InputStreamReader(stream));
                String str;

                while ((str = in.readLine()) != null) {
                    t.train(str, "good");
                }

                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 1; i < 11; i++) {
            try {
                InputStream stream = getClass().getResourceAsStream(i + ".txt");

                BufferedReader in = new BufferedReader(new InputStreamReader(stream));
                String str;

                while ((str = in.readLine()) != null) {
                    t.train(str, "good");
                }

                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            InputStream stream = getClass().getResourceAsStream("blacklist.txt");

            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String str;

            while ((str = in.readLine()) != null) {
                t.train(str, "bad");
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String classification = fc.getClassification(pageText);

        if (classification.equals("good")) {
            result = 0;
        } else if (classification.equals("bad")) {
            result = 1;
        }

        return result;
    }

    private double hasValidCertificate(DomainDetail dd) {
        double result = -1;

        DatabaseStatus isSSLVerified = dd.getIsSSLVerified();


        switch (isSSLVerified) {
            case YES:
                result = 1;
                break;
            case NO:
                result = 0;
                break;
            case NOTFLAGGED:
                break;
        }

        return result;
    }

    private double getGooglePageRank(DomainDetail dd) {
        String domain = dd.getDomainName();

        double result = -1;
        JenkinsHash jHash = new JenkinsHash();

        String googlePrResult = "";

        long hash = jHash.hash(("info:" + domain).getBytes());

        String url = "http://" + GOOGLE_PR_DATACENTER_IPS[dataCenterIdx] + "/search?client=navclient-auto&hl=en&"
                + "ch=6" + hash + "&ie=UTF-8&oe=UTF-8&features=Rank&q=info:" + domain;

        try {
            URLConnection con = new URL(url).openConnection();
            InputStream is = con.getInputStream();
            byte[] buff = new byte[1024];
            int read = is.read(buff);
            while (read > 0) {
                googlePrResult = new String(buff, 0, read);
                read = is.read(buff);
            }
            googlePrResult = googlePrResult.split(":")[2].trim();
            result = new Long(googlePrResult).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataCenterIdx++;
        if (dataCenterIdx == GOOGLE_PR_DATACENTER_IPS.length) {
            dataCenterIdx = 0;
        }

        return result;
    }

    private boolean getConfidence(double ipResult, double bayesResult, double certificateResult, double prResult) {
        boolean result = false;

        double confidence = (ipCheckerResultWeight * ipResult)
                + (bayesResultWeight * bayesResult)
                + (certificateResultWeight * certificateResult)
                + (pageRankResultWeight * prResult);

        if (confidence < threshold) {
            result = true;
        } else if (confidence > threshold) {
            result = false;
        }

        return result;
    }

    private boolean phishingFilter(DomainFile df, DomainDetail dd) {
        boolean result = false;
        
        double ipCheckerResult = ipChecker(df);
        double bayesResult = getProbabilityForPhishing(df);
        double certificateResult = hasValidCertificate(dd);
        double pageRankResult = getGooglePageRank(dd);

        ipCheckerResult += 1;
        bayesResult += 1;
        certificateResult += 1;
        pageRankResult += 1;

        result = getConfidence(ipCheckerResult, bayesResult, certificateResult, pageRankResult);	//confidence is true or false

        return result;
    }

    public static void main(String args[]) {
        try {
            DocumentBuilderFactory domFactory  = DocumentBuilderFactory.newInstance();
            domFactory .setNamespaceAware(true);
            DocumentBuilder builder = domFactory .newDocumentBuilder();
            Document doc = builder.parse("verified_online.xml");

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile("//url");

            Object evalResult = expr.evaluate(doc, XPathConstants.NODESET);

            NodeList nodes = (NodeList) evalResult;
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println(nodes.item(i).getNodeValue());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
