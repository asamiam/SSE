package SSE;

/**
 * Provides a place holder for DomainDetail information
 * @author Jordan Wesolowski
 */
public class DomainDetail
{
	private long id;
	private DatabaseStatus isSSLVerified;
	private String domainName;
	private DomainFile[] domainFiles;
	private IPAddress[] ipAddresses;
	private Certificate[] certificates;

        public DomainDetail()
        {
            
        }

        /**
         * Creates a domainDetail object with the given domain name
         * @param domainName Domain name of domain detail object
         */
        public DomainDetail(String domainName)
        {
            this.domainName = domainName;
        }
        /**
         * Returns the id of the DomainDetail
         * @return  The id of the DomainDetail
         */
	public long getID()
	{
		return id;
	}
        /**
         * Sets the id of the DomainDetail
         * @param id    The desired id of the DomainDetail
         */
	public void setID(long id)
	{
		this.id = id;
	}
        /**
         * Returns the status of SSLVerified of the DomainDetail
         * @return  The status of SSLVerified of the DomainDetial
         */
	public DatabaseStatus getIsSSLVerified()
	{
		return isSSLVerified;
	}
        /**
         * Sets the status of SSLVerified of the DomainDetail
         * @param isSSLVerified The desired status of SSLVerified of the DomainDetail
         */
	public void setIsSSLVerified(DatabaseStatus isSSLVerified)
	{
		this.isSSLVerified = isSSLVerified;
	}
        /**
         * Returns the domain name of the DomainDetail
         * @return  The domain name of the DomainDetail
         */
	public String getDomainName()
	{
		return domainName;
	}
        /**
         * Sets the domain name of the DomainDetail
         * @param domainName    The desired domain name of the DomainDetail
         */
	public void setDomainName(String domainName)
	{
		this.domainName = domainName;
	}
        /**
         * Returns the domainFiles of the DomainDetail
         * @return  The domainFiles of the DomainDetail
         */
	public DomainFile[] getDomainFiles()
	{
		return domainFiles;
	}
        /**
         * Sets the domainFiles of the DomainDetail
         * @param domainFiles   The desired domainFiles of the DomainDetail
         */
	public void setDomainFiles(DomainFile[] domainFiles)
	{
		this.domainFiles = domainFiles;
	}
        /**
         * Returns the IPAddresses of the DomainDetail
         * @return  The IPAddresses of the DomainDetail
         */
	public IPAddress[] getIPAddresses()
	{
		return ipAddresses;
	}
        /**
         * Sets the IPAddresses of the DomainDetail
         * @param ipAddresses   The desired IPAddresses of the DomainDetail
         */
	public void setIPAddresses(IPAddress[] ipAddresses)
	{
		this.ipAddresses = ipAddresses;
	}
        /**
         * Returns the Certificates of the DomainDetail
         * @return  The Certificates of the DomainDetail
         */
	public Certificate[] getCertificates()
	{
		return certificates;
	}
        /**
         * Sets the Certificates of the DomainDetail
         * @param certificates  The desired Certificates of the DomainDetail
         */
	public void setCertificates(Certificate[] certificates)
	{
		this.certificates = certificates;
	}
}