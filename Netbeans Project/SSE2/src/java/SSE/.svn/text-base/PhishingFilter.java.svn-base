package SSE;

import java.net.URLConnection;
import java.net.URL;
import java.io.*;
import java.io.File;
import java.util.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.xml.sax.SAXException;
import java.io.IOException;

import com.enigmastation.classifier.FisherClassifier;
import com.enigmastation.classifier.Trainer;
import com.enigmastation.classifier.impl.FisherClassifierImpl;

public class PhishingFilter {

    private double ipCheckerResultWeight = 1; // weight given to the ip checker
    private double bayesResultWeight = 3; // weight given to the nieve bayes classifier
    private double certificateResultWeight = 4; // weight given to if it has certificates
    private double pageRankResultWeight = 2; // weight given to its page rank result
    private double threshold = 3; // the value the final computation is compared against

    /* list of datacenters that can be called to get page rank from google */
    static private int dataCenterIdx = 0;
    static final public String[] GOOGLE_PR_DATACENTER_IPS = new String[]{ //List of available Google dataceneter IPs and addresses
        "64.233.183.91",
        "64.233.189.44",
        "66.102.9.115",
        "66.249.89.83",
        "toolbarqueries.google.com",};

    /*
     * isPhishing
     *
     * This function is called by the SearchAction.java file
     *
     * It takes a URL as a parameter
     *
     * It first checks with the database if it is a phishing site or not, if the value NOTFLAGGED
     * is returned it checks to see if a third party site (phishtank) has said it is a phishing site.
     * If the third party has not crawled that site then we do the computation ourselves
     */
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

        /* get domain detail object based off of domain name and the domain file */
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
            case YES: // if it is a phishing site according to the DB, flag is true													// If result is yes, set flag[1] to true, it is phishing
                flag = true;
                break;
            case NO: // if it is not a phishing site according to the DB, flag is false														// If result is no, set flag[1] to false, not phishing
                flag = false;
                break;
            case NOTFLAGGED: // if the DB does not have the domain file, check third party													// If result is notflagged, run phishing filter alogrithm
                DatabaseStatus thirdPartyPhishing = df.getIsThirdPartyPhishing();

                switch (thirdPartyPhishing) {
                    case YES: // if third party says its a phishing site, flag is true
                        flag = true;
                        break;
                    case NO: // if third party says it is not a phishing site, flag is false
                        flag = phishingFilter(df, dd);

                        if (flag) {
                            isPhishing = DatabaseStatus.YES;
                        } else {
                            isPhishing = DatabaseStatus.NO;
                        }

                        SSEDAO.storePhishingResults(df.getID(), isPhishing);
                        break;
                    case NOTFLAGGED: // if third party does not have information, perform computation ourselves
                        try {
                            DocumentBuilderFactory domFactory  = DocumentBuilderFactory.newInstance();
                            domFactory .setNamespaceAware(true);
                            DocumentBuilder builder = domFactory .newDocumentBuilder();

                            File dir = new File("/var/lib/Tomcat6/webapps/WEB-INF/phishingFiles/"); // C:/Users/CPCOM 473/Desktop/CSE 486 Capstone/SSETesting/Netbeans Project/SSE2/build/web/WEB-INF/phishingFiles/");

                            File[] files = dir.listFiles();
                            File verifiedOnline = null;

                            for (File file : files) {
                                if (file.getName().equals("verified_online.xml")) {
                                    verifiedOnline = file;
                                }
                            }

                            Document doc = builder.parse(verifiedOnline);

                            XPathFactory factory = XPathFactory.newInstance();
                            XPath xpath = factory.newXPath();
                            XPathExpression expr = xpath.compile("//url");

                            Object evalResult = expr.evaluate(doc, XPathConstants.NODESET);

                            NodeList nodes = (NodeList) evalResult;
                            for (int i = 0; i < nodes.getLength(); i++) {
                                String nodeValue = nodes.item(i).getFirstChild().getNodeValue();
                                if (df.getDomainFile().equals((nodeValue))) {
                                    flag = true;
                                    break;
                                } else {
                                    for (IPAddress ipAddress : dd.getIPAddresses()) {
                                        if (Long.toString(ipAddress.getID()).equals(nodeValue)) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                }
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

        File dir = new File("/var/lib/Tomcat6/webapps/WEB-INF/phishingFiles/"); //"C:/Users/CPCOM 473/Desktop/CSE 486 Capstone/SSETesting/Netbeans Project/SSE2/build/web/WEB-INF/phishingFiles/");
        File[] files = dir.listFiles();
        List<File> nonPhishFiles = new ArrayList<File>();
        List<File> goodFiles = new ArrayList<File>();
        File blackList = null;

        for (File file : files) {
            if (file.getName().startsWith("nonPhish")) {
                nonPhishFiles.add(file);
            }

            if (file.getName().equals("blacklist.txt")) {
                blackList = file;
            }
        }

        for (int i = 1; i < 11; i++) {
            for (File file : files) {
                if (file.getName().equals(i + ".txt")) {
                    goodFiles.add(file);
                }
            }
        }

        for (int i = 1; i < 4; i++) {
            try {
                FileInputStream fstream = new FileInputStream(nonPhishFiles.get(i));
                DataInputStream dis = new DataInputStream(fstream);
                BufferedReader in = new BufferedReader(new InputStreamReader(dis));

                String str;

                while ((str = in.readLine()) != null) {
                    t.train(str, "good");
                }

                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 10; i++) {
            try {
                FileInputStream fstream = new FileInputStream(goodFiles.get(i));
                DataInputStream dis = new DataInputStream(fstream);
                BufferedReader in = new BufferedReader(new InputStreamReader(dis));
                
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
            FileInputStream fstream = new FileInputStream(blackList);
            DataInputStream dis = new DataInputStream(fstream);
            BufferedReader in = new BufferedReader(new InputStreamReader(dis));

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

    /*
     * Is the main function that calls all of the methods that will
     * return results that will be used check if it is a phishing
     * site
     */
    private boolean phishingFilter(DomainFile df, DomainDetail dd) {
        boolean result = false;

        double ipCheckerResult = ipChecker(df);
        double bayesResult = getProbabilityForPhishing(df);
        double certificateResult = hasValidCertificate(dd);
        double pageRankResult = getGooglePageRank(dd);

        /* need to add 1 to everything in case a zero is returned, if zero is returned it will make the whole result zero */
        ipCheckerResult += 1;
        bayesResult += 1;
        certificateResult += 1;
        pageRankResult += 1;

        result = getConfidence(ipCheckerResult, bayesResult, certificateResult, pageRankResult); //confidence is true or false

        return result;
    }

    public static void main(String args[]) {
        
    }
}
