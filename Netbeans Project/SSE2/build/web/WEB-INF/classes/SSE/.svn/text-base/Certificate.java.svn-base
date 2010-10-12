package SSE;

import java.util.*;

/**
 * This class acts as a place holder for certificate information.
 * @author Jordan Wesolowski
 */
public class Certificate
{
	private long id, domainID;
	private Date validFrom, validTill;
	private int basicConstraint, hierarchy;
	private String issuer, subject, fullCert;

        public Certificate()
        {
            
        }

        /**
         * Creates a certificate with the respective values passed in a parameters
         * @param validFrom         The date the certificate is valid from
         * @param validTill         The date the certificate is valid till
         * @param basicConstraint   Used to determine what applications a certificate can be used for
         * @param hierarchy         Where in the certificate chain does this certificate rest
         * @param issuer            Who issued the certificate
         * @param subject           Who the certificate is for
         * @param fullCert          The full certificate text
         */
        public Certificate(Date validFrom, Date validTill,
                int basicConstraint, int hierarchy,
                String issuer, String subject, String fullCert)
        {
            this.validFrom = validFrom;
            this.validTill = validTill;
            this.basicConstraint = basicConstraint;
            this.hierarchy = hierarchy;
            this.issuer = issuer;
            this.subject = subject;
            this.fullCert = fullCert;
        }

        /**
         * Returns the id of the certificate
         * @return  The id of the certificate
         */
	public long getID()
	{
		return id;
	}
        /**
         * Sets the id of the certificate
         * @param id    The desired id of the certificate
         */
	public void setID(long id)
	{
		this.id = id;
	}
        /**
         * Returns the DomainID of the certificate
         * @return  The domainID of the certificate
         */
	public long getDomainID()
	{
		return domainID;
	}
        /**
         * Sets the domainID of the certificate
         * @param domainID  The desired domainID of the certificate
         */
	public void setDomainID(long domainID)
	{
		this.domainID = domainID;
	}
        /**
         * Returns the valid from date of the certificate
         * @return the valid from date of the certificate
         */
	public Date getValidFrom()
	{
		return validFrom;
	}
        /**
         * Sets the valid from date of the certificate
         * @param validFrom The desired valid from date of the certificate
         */
	public void setValidFrom(Date validFrom)
	{
		this.validFrom = validFrom;
	}
        /**
         * Returns the valid till date of the certificate
         * @return  The valid till date of the certificate
         */
	public Date getValidTill()
	{
		return validTill;
	}
        /**
         * Sets the valid till date of the certificate
         * @param validTill The desired valid till date of the certificate
         */
	public void setValidTill(Date validTill)
	{
		this.validTill = validTill;
	}
        /**
         * Returns the basic constraint of the certificate
         * @return  The basic constraint of the certificate
         */
	public int getBasicConstraint()
	{
		return basicConstraint;
	}
        /**
         * Sets the basic constraint of the certificate
         * @param basicConstraint   The desired basic constraint of the certificate
         */
	public void setBasicConstraint(int basicConstraint)
	{
		this.basicConstraint = basicConstraint;
	}
        /**
         * Returns the hierarchy of the certificate
         * @return  The hierarchy of the certificate
         */
	public int getHierarchy()
	{
		return hierarchy;
	}
        /**
         * Sets the hierarchy of the certificate
         * @param hierarchy The desired hierarchy of the certificate
         */
	public void setHierarchy(int hierarchy)
	{
		this.hierarchy = hierarchy;
	}
        /**
         * Returns the issuer of the certificate
         * @return  The issuer of the certificate
         */
	public String getIssuer()
	{
		return issuer;
	}
        /**
         * Sets the issuer of the certificate
         * @param issuer    The desired issuer of the certificate
         */
	public void setIssuer(String issuer)
	{
		this.issuer = issuer;
	}
        /**
         * Returns the subject of the certificate
         * @return  The subject of the certificate
         */
	public String getSubject()
	{
		return subject;
	}
        /**
         * Sets the subject of the certificate
         * @param subject   The desired subject of the certificate
         */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
        /**
         * Returns the full certificate text
         * @return  The full certificate text
         */
	public String getFullCertificate()
	{
		return fullCert;
	}
        /**
         * Sets the full certificate text
         * @param fullCert  The desired certificate text
         */
	public void setFullCertificate(String fullCert)
	{
		this.fullCert = fullCert;
	}
}