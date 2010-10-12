package SSE;

import java.util.Date;

/**
 * Place holder for DomainFile information
 * @author Jordan Wesolowski
 */
public class DomainFile
{
	private long id, domainID;
	private String domainFile, pageSource;
	private DatabaseStatus isPhishing, isThirdPartyPhishing;
	private Date originalTimeStamp, lastCrawledTimeStamp, lastPhishedTimeStamp;

        public DomainFile()
        {
            
        }

        /**
         * Creates a DomainFile with the corresponding domainFile name and page source
         * @param domainFile
         * @param pageSource
         */
        public DomainFile(String domainFile, String pageSource)
        {
            this.domainFile = domainFile;
            this.pageSource = pageSource;
        }
        /**
         * Returns the DomainFile id
         * @return  The DomainFile id
         */
	public long getID()
	{
		return id;
	}
        /**
         * Sets the DomainFile id
         * @param id    The desired domainFile id
         */
	public void setID(long id)
	{
		this.id = id;
	}
        /**
         * Returns the domain id of the DomainFile
         * @return  The domain id of the DomainFile
         */
	public long getDomainID()
	{
		return domainID;
	}
        /**
         * Sets the domain id of the DomainFile
         * @param domainID  The desired domain id of the DomainFile
         */
	public void setDomainID(long domainID)
	{
		this.domainID = domainID;
	}
        /**
         * Returns the domainFile name
         * @return  The DomainFile name
         */
	public String getDomainFile()
	{
		return domainFile;
	}
        /**
         * Sets the DomainFile name
         * @param domainFile    The desired DomainFile name
         */
	public void setDomainFile(String domainFile)
	{
		this.domainFile = domainFile;
	}
        /**
         * Returns the DomainFile page source
         * @return  The DomainFile page source
         */
	public String getPageSource()
	{
		return pageSource;
	}
        /**
         * Sets the DomainFile page source
         * @param pageSource    The desired DomainFile page source
         */
	public void setPageSource(String pageSource)
	{
		this.pageSource = pageSource;
	}
        /**
         * Returns status of ThirdPartyPhishing of DomainFile
         * @return  Status of ThirdPartyPhishing of DomainFile
         */
	public DatabaseStatus getIsThirdPartyPhishing()
	{
		return isThirdPartyPhishing;
	}
        /**
         * Sets status of ThirdPartyPhishing of DomainFile
         * @param isThirdPartyPhishing  The desired status of ThirdPartyPhishing of DomainFile
         */
	public void setIsThirdPartyPhishing(DatabaseStatus isThirdPartyPhishing)
	{
		this.isThirdPartyPhishing = isThirdPartyPhishing;
	}
        /**
         * Returns status of IsPhishing of DomainFile
         * @return  Status of IsPhishing of DomainFile
         */
	public DatabaseStatus getIsPhishing()
	{
		return isPhishing;
	}
        /** Sets status of IsPhishing of DomainFile
         * @param isPhishing    Desired status of IsPhishing of DomainFile
         */
	public void setIsPhishing(DatabaseStatus isPhishing)
	{
		this.isPhishing = isPhishing;
	}
        /**
         * Returns the original time stamp of DomainFile
         * @return  Original time stamp of DomainFile
         */
	public Date getOriginalTimeStamp()
	{
		return originalTimeStamp;
	}
        /**
         * Sets the original time stamp of DomainFile
         * @param originalTimeStamp Desired original time stamp of DomainFile
         */
	public void setOriginalTimeStamp(Date originalTimeStamp)
	{
		this.originalTimeStamp = originalTimeStamp;
	}
        /**
         * Returns the last crawled time stamp of DomainFile
         * @return  Last crawled time stamp of DomainFile
         */
	public Date getLastCrawledTimeStamp()
	{
		return lastCrawledTimeStamp;
	}
        /**
         * Sets the last crawled time stamp of DomainFile
         * @param lastCrawledTimeStamp  Desired last crawled time stamp of DomainFile
         */
	public void setLastCrawledTimeStamp(Date lastCrawledTimeStamp)
	{
		this.lastCrawledTimeStamp = lastCrawledTimeStamp;
	}
        /**
         * Returns the last phishing time stamp of DomainFile
         * @return  Last phishing time stamp of DomainFile
         */
	public Date getLastPhishedTimeStamp()
	{
		return lastPhishedTimeStamp;
	}
        /**
         * Sets the last phished time stamp of DomainFile
         * @param lastPhishedTimeStamp  Desired last phished time stamp of DomainFile
         */
	public void setLastPhishedTimeStamp(Date lastPhishedTimeStamp)
	{
		this.lastPhishedTimeStamp = lastPhishedTimeStamp;
	}
}