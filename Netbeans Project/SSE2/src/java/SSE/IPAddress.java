package SSE;

/**
 * Provides a class to store an ip address in
 * @author Jordan Wesolowski
 */
public class IPAddress
{
	private long id, domainID;
	private int[] octet = new int[4];

	public IPAddress() {}
        /**
         * Creates an IPAddress with the corresponding octets
         * @param a Octet 1
         * @param b Octet 2
         * @param c Octet 3
         * @param d Octet 4
         */
	public IPAddress(int a, int b, int c, int d)
	{
		setOctet(0,a);
		setOctet(1,b);
		setOctet(2,c);
		setOctet(3,d);
	}
        /**
         * Returns the IPAddress ID
         * @return  The IPAddress ID
         */
	public long getID()
	{
		return id;
	}
        /**
         * Sets the IPAddress ID
         * @param id    The desired IPAddress ID
         */
	public void setID(long id)
	{
		this.id = id;
	}
        /**
         * Returns the domain id of the IPAddress
         * @return  Domain id of IPAddress
         */
	public long getDomainID()
	{
		return domainID;
	}
        /**
         * Sets the domain id of the IPAddress
         * @param domainID  The desired domain id of the IPAddress
         */
	public void setDomainID(long domainID)
	{
		this.domainID = domainID;
	}
        /**
         * Sets the corresponding octet to the desired value
         * @param index Index of octet to set
         * @param val   Value to set for octet
         * @return  True if valid operation, false otherwise
         */
	public boolean setOctet(int index, int val)
	{
		if ((index >= 0) && (index <= 3) && (val >= 0) && (val <= 255))
		{
			octet[index] = val;
			return true;
		}
		else return false;
	}
        /**
         * Returns the corresponding octet
         * @param index Index of octet to return
         * @return  Corresponding octet
         */
	public int getOctet(int index)
	{
		try
		{
			return octet[index];
		} catch (Exception e) {return -1;}
	}
        /**
         * Returns a string representation of the IPAddress
         * @return String representation of the IPAddress
         */
	public String toString()
	{
		try
		{
			return octet[0] + "." + octet[1] + "." + octet[2] + "." + octet[3];
		} catch (Exception e) {return "error with ip address";}
	}
}