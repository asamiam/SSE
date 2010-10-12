'''
Created on Apr 30, 2010

@author: TheMarquis
'''

from SSE import Certificate as Certificate, DomainDetail as DomainDetail, \
    DomainFile as DomainFile, IPAddress as IPAddress, SSEDAO as SSEDAO
from urlparse import urlparse
from optparse import OptionParser
import java.net.ConnectException as ConnectException
import java.security.cert.Certificate as javaCertClass
import java.security.cert.X509Certificate as javaX509CertClass
import javax.net.ssl.HttpsURLConnection as HttpsURLConnection
import javax.net.ssl.SSLHandshakeException as SSLHandshakeException
import pprint
import re
import sgmllib
import socket
import urllib







#CUSTOM PARSE CLASS TO PARSE PAGE SOURCE FOR LINKS
class MyParser(sgmllib.SGMLParser):
    
    def parse(self, url):
        #parse the given string 'url'
        self.feed(url)
        self.close()
    
    def __init__(self, scheme, domain, verbose=0):
        #Initialize an object, passing 'verbose' to the superclass
        sgmllib.SGMLParser.__init__(self, verbose)
        self.hyperlinks = []
        self.scheme = scheme
        self.domain = domain    

    def start_a(self, attributes):
        #Process a hyperlink and its 'attributes'

        for name, value in attributes:
########################################URL formalizing currently working on deleting unneccessary query sections of strings            
            if name == "href": 
                #if value.
                
                if value.count("/") > 0:
                    if value.index("/") == 0:
                        value = self.scheme + "://" + self.domain + value
                    
                regexpr = r"""^^(?:http(?:s)?://)?(?:\w+\.)+\w+(?:/\w+(?:\.\w+)?)*/?$"""
                pattern = re.compile(regexpr, re.X)
                result = pattern.match(value)
                if self.hyperlinks.count(value) == 0 and pattern.match(value) != None:
                        
                    self.hyperlinks.append(value)
              
                
    def get_hyperlinks(self):
        #Return the list of hyperlinks.
        return self.hyperlinks 
    
    def sort_hyperlinks(self, depth):
        self.hyperlinks.sort()
        for index in range(0, len(self.hyperlinks)):
            link = self.hyperlinks.pop(index)
            t = link, depth
            self.hyperlinks.insert(index, t)

       
class crawler:
    
    def crawl(self, currentURL):
        #uses the python module urlparse to parse the URL into segments
        scheme, domain, filePath, params, query, fragment = urlparse(currentURL) 
        ipAddr = socket.getaddrinfo(domain, 443)

        #RETRIEVE SSL CERTIFICATES (IF APPLICABLE)
        try:
            #uses the javax.net.ssl.* and java.security.cert.* libraries to obtain certificate
            factory = HttpsURLConnection.getDefaultSSLSocketFactory()
            tmpSocket = factory.createSocket(domain, 443)
            tmpSocket.startHandshake()
            session = tmpSocket.getSession()
            domainCerts = session.getPeerCertificateChain()
        except SSLHandshakeException: #except thrown if the domain does not support SSL
                print 'javax.net.ssl.SSLHandshakeException with domain: ' + domain
        except ConnectException:
                print 'java.net.ConnectException with domain: ' + domain

        #RETRIEVE PAGE SOURCE
        pageSource = urllib.urlopen(currentURL).read()

        #PARSE PAGE SOURCE FOR LINKS
        myparser = MyParser(scheme, domain)
        myparser.parse(pageSource)
        myparser.sort_hyperlinks(depth)
        childLinks = myparser.get_hyperlinks()
        
        return ipAddr, domainCerts, pageSource, childLinks
    
    def crawlFilePath(self, seed):
        currentURL, depth = seed
        depth = depth - 1 #decreases depth of links to be retrieved by 1
        scheme, domain, filePath, params, query, fragment = urlparse(currentURL) 
        pageSource = urllib.urlopen(currentURL).read()  #RETRIEVE PAGE SOURCE

        #PARSE PAGE SOURCE FOR LINKS
        if(depth > 0):
            myparser = MyParser(scheme, domain)
            myparser.parse(pageSource)
            myparser.sort_hyperlinks(depth)
            childLinks = myparser.get_hyperlinks()
        else:
            childLinks = []
            
        tempdf = DomainFile()
        tempdf.setDomainFile(filePath)
        tempdf.setPageSource(pageSource)
        print "pageSource for '" + filePath + "' on domain '" + domain + "' stored"
        return tempdf, childLinks
        

    def crawlDomain(self, seed):
        currentURL, depth = seed
        scheme, domain, filePath, params, query, fragment = urlparse(currentURL)
        #######check for regular traffic on port 80
        #######think of a way to grab information on servers that host on non-standard ports.
        ipAddr = socket.getaddrinfo(domain, 443)
        
        #RETRIEVE SSL CERTIFICATES (IF APPLICABLE)
        try:
            #uses the javax.net.ssl.* and java.security.cert.* libraries to obtain certificate
            factory = HttpsURLConnection.getDefaultSSLSocketFactory()
            tmpSocket = factory.createSocket(domain, 443)
            tmpSocket.startHandshake()
            session = tmpSocket.getSession()
            domainCerts = session.getPeerCertificateChain()
        except SSLHandshakeException: #except thrown if the domain does not support SSL
            print 'javax.net.ssl.SSLHandshakeException with domain: ' + domain
            domainCerts = " "
        except ConnectException:
            print 'java.net.ConnectException with domain: ' + domain
            domainCerts = None
        
            
        
        tempdd = DomainDetail()     #create a temporary DomainDetail object
        tempdd.setDomainName(domain) #set the domainName value in the DomainDetail object

        #stores the IP addresses obtain from the crawlDomain() function
        ipAddresses = []
        ipAddrLen = len(ipAddr)
        for i in range (0, ipAddrLen):
            a, b, c, d, e = ipAddr[i]
            a, b = e
            oct1, sep, leftover = a.partition('.')
            oct2, sep, leftover = leftover.partition('.')
            oct3, sep, leftover = leftover.partition('.')
            oct4, sep, leftover = leftover.partition('.')
            ipAddresses.append(IPAddress(int(oct1), int(oct2), int(oct3), int(oct4)))
        tempdd.setIPAddresses(ipAddresses)
        print "IP Addresses for '" + domain + "' stored"

        #parses and stores certificate Information
        certArray = []

        if(domainCerts != None):
            chainLength = len(domainCerts)
            for i in range(0, chainLength):
                certArray.append(Certificate())
                certArray[i].setFullCertificate(pprint.pformat(domainCerts[i]))
                certArray[i].setIssuer(domainCerts[i].getIssuerDN().getName())
                certArray[i].setHierarchy(i)
                certArray[i].setSubject(domainCerts[i].getSubjectDN().getName())
                certArray[i].setValidFrom(domainCerts[i].getNotBefore())
                certArray[i].setValidTill(domainCerts[i].getNotAfter())  
                certArray[i].setBasicConstraint(0) ###DONT HOW HOW TO DO###

        tempdd.setCertificates(certArray)
        print "certs for '" + domain + "' stored"     
        
        return tempdd
        
    
def storeDomainDetailInfo(domainName, DNSInfo, domainCerts):
    global DomainDetailList
    tempdd = DomainDetail()     #create a temporary DomainDetail object
    tempdd.setdomainName(domainName) #set the domainName value in the DomainDetail object

    #stores the IP addresses obtain from the crawlDomain() function
    ipAddresses = []
    ipAddrLen = len(DNSInfo)
    for i in range (0, ipAddrLen):
        a, b, c, d, e = DNSInfo[i]
        a, b = e
        oct1, sep, leftover = a.partition('.')
        oct2, sep, leftover = leftover.partition('.')
        oct3, sep, leftover = leftover.partition('.')
        oct4, sep, leftover = leftover.partition('.')
        ipAddresses.append(IPAddress(int(oct1), int(oct2), int(oct3), int(oct4)))
    tempdd.setIPAddresses(ipAddresses)
    print "IP Addresses stored"

    #parses and stores certificate Information
    certArray = []
    chainLength = len(domainCerts)
    for i in range(0, chainLength):
        certArray.append(Certificate())
        certArray[i].setFullCertificate(pprint.pformat(domainCerts[i]))
        certArray[i].setIssuer(domainCerts[i].getIssuerDN().getName())
        certArray[i].setHierarchy(i)
        certArray[i].setSubject(domainCerts[i].getSubjectDN().getName())
        certArray[i].setValidFrom(domainCerts[i].getNotBefore())
        certArray[i].setValidTill(domainCerts[i].getNotAfter())  
        certArray[i].setBasicConstraint(0) ###DONT HOW HOW TO DO###

    tempdd.setCertificates(certArray)
    print "certs stored"

    domainDetailList.append(tempdd) #append the temporary DomainDetailObject to the end of the list
    print "tempdd object for '" + domain + "' stored"
        
def storeDomainFileInfo(source, childLinks):
    global DomainFileList
    tempdf.append(DomainFile())
    tempdf[0].setDomainFile(filePath)
    tempdf[0].setPageSource(pageSource)
    tempdd.setDomainFiles(tempdf)
    print "pageSource '" + filePath + "' stored"
    
#function to remove any links in childLinks that are already in uncrawledLinks or crawledLinks

def updateUncrawledLinks(childLinks, uncrawledLinks, crawledLinks):
    CLIndex = 0
    while (CLIndex < len(childLinks)):
        found = False
        if(uncrawledLinks.count(childLinks[CLIndex][0]) != 0):
            childLinks.pop(CLIndex)
            found = True
        elif(crawledLinks.count(childLinks[CLIndex][0]) != 0):
            childLinks.pop(CLIndex)
            found = True
        
        if(found == False):
            CLIndex = CLIndex + 1
    
    while (len(childLinks) > 0):
        uncrawledLinks.append(childLinks.pop(0))
       
    return childLinks, uncrawledLinks, crawledLinks
  
def main(start, d):
           
    seeds = []
    seeds.append(start)  
        
#################LOOP FOR EACH SEED#################
    for x in range(0, len(seeds)):
        depth = d
        seed = seeds.pop(0), depth
        
        domainDetailList = []
        domainFileList = []
        uncrawledLinks = []
        crawledLinks = []
        crawlerObj = crawler()  #initialize crawler object
        domainDetail = crawlerObj.crawlDomain(seed)  #first crawls the domain of the seed URL. returns a DomainDetail object
        domainDetailList.append(domainDetail) #stores the returned DomainDetail object in the domainDetailList[]
        domainFile, childLinks = crawlerObj.crawlFilePath(seed)  #second crawls the file path of the seed URL. returns childLinks and a DomainFile object
        domainFileList.append([])  #appends a list at the first index of the domainFileList
        domainFileList[0].append(domainFile)  #appends the domainFile retrieved from the seed URL to the same row index which is equal to the index of the domain in the domainDetailList
        crawledLinks.append(seed[0]) #adds the seed URL to the list of crawled Links
        updateUncrawledLinks(childLinks, uncrawledLinks, crawledLinks)  #appends the childLinks obtained from the first crawl to the list of uncrawled Links
                                                                        #**may need to have updateUncrawledLinks() return new version of uncrawledLinks and crawledLinks
        
    #################WHILE LOOP FOR CRAWLING CHILDLINKS OF SEED#################
        while(len(uncrawledLinks) > 0):
            currUrl = uncrawledLinks.pop(0) #removes the link at the beginning of the uncrawledLinks list and sets currUrl to that link
            crawledLinks.append(currUrl[0])  #adds the link that was just removed from uncrawledLinks to crawledLinks
            scheme, domain, filePath, params, query, fragment = urlparse(currUrl[0])
            
            domainExists = False  #paramater to specify if the domain of the currUrl already exists in the domainDetailList
            y = 0  #count to represent the index in the domainDetailList
            
            #loops while the domain is not found in the domainDetailList and we are within the bounds of the domainDetailList array
            while((domainExists == False) and (y < len(domainDetailList))):
                if(domainDetailList[y].getDomainName() == domain):  #checks if the domain already exists in the domainDetailList
                    domainFile, childLinks = crawlerObj.crawlFilePath(currUrl)  #since the domain has already been crawled, just crawl the page source of the URL
                    domainFileList[y].append(domainFile)  #append the domainFile to the same row index which is equal to the index of the domain in the domainDetailList
                    domainExists = True  #domain is found in the domainDetailList set domainExists to True
                    updateUncrawledLinks(childLinks, uncrawledLinks, crawledLinks)
                y = y + 1
            
            if(domainExists != True):
                domainDetail = crawlerObj.crawlDomain(currUrl)
                ddIndex = len(domainDetailList)
                domainDetailList.append(domainDetail)
                domainFile, childLinks = crawlerObj.crawlFilePath(currUrl)
                domainFileList.append([])
                domainFileList[ddIndex].append(domainFile)
                updateUncrawledLinks(childLinks, uncrawledLinks, crawledLinks)
    #################WHILE LOOP FOR CRAWLING CHILDLINKS OF SEED#################
    print "seed '" + seed[0] + "' done"
    
    print "commencing storage Of DomainDetail objects into database..."
    ssedaoObj = SSEDAO()
    for index in range(0, len(domainDetailList)):
        domainDetailList[index].setDomainFiles(domainFileList[index])
        ssedaoObj.storeCrawlerInformation(domainDetailList[index])
        print"DomainDetail object for '" + domainDetailList[index].getDomainName() + "' send to database"
    
#################LOOP FOR EACH SEED#################


#################PROGRAM BEGINS HERE!#################
#called via: adhocCrawler.ph -s http://www.wellsfargo.com -d 2
# where -s is the URL and -d is the depth 
if __name__ == '__main__':
    parser = OptionParser()
    parser.add_option("-s", dest="seed")
    parser.add_option("-d", type="int", dest="depth")
    (options, args) = parser.parse_args()
    main(options.seed, options.depth)
    
    