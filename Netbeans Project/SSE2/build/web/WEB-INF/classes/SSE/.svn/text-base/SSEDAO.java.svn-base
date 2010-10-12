package SSE;

import java.sql.*;

public class SSEDAO
{
	public static DatabaseStatus checkIfSSLVerified(String domainName)
	{
		DatabaseStatus isSSLVerified = DatabaseStatus.NOTINDB;

		try
		{
			CallableStatement cStmt = null;
			Connection conn = SSE.SSEDAO.getDBConnection();

                        if(conn == null) {
                            return DatabaseStatus.NO;
                        }

			// DomainDetail
			cStmt = conn.prepareCall("{call CheckIsSSLVerified(?)}");
			cStmt.setString(1, domainName); // domainName
			ResultSet rs = cStmt.executeQuery();

			if (rs.first())
			{
				boolean a = rs.getBoolean(1); // is ssl verified
				if (rs.wasNull())
					isSSLVerified = DatabaseStatus.NOTFLAGGED;
				else
					isSSLVerified = a?DatabaseStatus.YES:DatabaseStatus.NO;
			}

			conn.close();
		} catch (SQLException e) {e.printStackTrace();}

		return isSSLVerified;
	}

	/*public static DatabaseStatus checkIfPhishing(String domainName, String domainFile)
	{
		DatabaseStatus isPhishing = DatabaseStatus.NOTINDB;
		DatabaseStatus isThirdPartyPhishing = DatabaseStatus.NOTINDB;

		try
		{
			CallableStatement cStmt = null;
			Connection conn = SSE.SSEDAO.getDBConnection();

                        if(conn == null) {
                            return DatabaseStatus.NO;
                        }

			cStmt = conn.prepareCall("{call CheckIsPhishing(?, ?)}");
			cStmt.setString(1, domainName); // domainName
			cStmt.setString(2, domainFile); // domainFile
			ResultSet rs = cStmt.executeQuery();

			if (rs.first())
			{
				boolean a = rs.getBoolean(1); // is phishing
				if (rs.wasNull())
					isPhishing = DatabaseStatus.NOTFLAGGED;
				else
					isPhishing = a?DatabaseStatus.YES:DatabaseStatus.NO;

				boolean b = rs.getBoolean(2); // is thirdpartyphishing
				if (rs.wasNull())
					isThirdPartyPhishing = DatabaseStatus.NOTFLAGGED;
				else
					isThirdPartyPhishing = b?DatabaseStatus.YES:DatabaseStatus.NO;
			}

			conn.close();
		} catch (SQLException e) {e.printStackTrace();}

		//DatabaseStatus[] dbs = {isPhishing,isThirdPartyPhishing};

		if (isThirdPartyPhishing == DatabaseStatus.NOTINDB || isPhishing == DatabaseStatus.NOTINDB)
			return DatabaseStatus.NOTINDB;
		else if (isThirdPartyPhishing == DatabaseStatus.YES || isPhishing == DatabaseStatus.YES)
			return DatabaseStatus.YES;
		else if (isThirdPartyPhishing == DatabaseStatus.NO || isPhishing == DatabaseStatus.NO)
			return DatabaseStatus.NO;
		else
			return DatabaseStatus.NOTFLAGGED;
	}*/

	public static void storeSSLVerifiedResults(long domainID, DatabaseStatus isSSLVerified)
	{
		try
		{
			CallableStatement cStmt = null;
			Connection conn = SSE.SSEDAO.getDBConnection();

			cStmt = conn.prepareCall("{call StoreSSLVerifiedResults(?, ?)}");
			cStmt.setLong(1, domainID);

			switch (isSSLVerified)
			{
				case NOTFLAGGED: cStmt.setNull(2, Types.TINYINT); break;
				case YES: cStmt.setBoolean(2, true); break;
				case NO: cStmt.setBoolean(2, false); break;
				default: cStmt.setNull(2, Types.TINYINT); break;
			}

			cStmt.execute();

			conn.close();
		} catch (SQLException e) {e.printStackTrace();}
	}

	public static void storePhishingResults(long domainFileID, DatabaseStatus isPhishing)
	{
		try
		{
			CallableStatement cStmt = null;
			Connection conn = SSE.SSEDAO.getDBConnection();

			cStmt = conn.prepareCall("{call StorePhishingResults(?, ?)}");
			cStmt.setLong(1, domainFileID);

			switch (isPhishing)
			{
				case NOTFLAGGED: cStmt.setNull(2, Types.TINYINT); break;
				case YES: cStmt.setBoolean(2, true); break;
				case NO: cStmt.setBoolean(2, false); break;
				default: cStmt.setNull(2, Types.TINYINT); break;
			}

			cStmt.execute();

			conn.close();
		} catch (SQLException e) {e.printStackTrace();}
	}

	public static void storeThirdPartyPhishingResults(long domainFileID, DatabaseStatus isThirdPartyPhishing)
	{
		try
		{
			CallableStatement cStmt = null;
			Connection conn = SSE.SSEDAO.getDBConnection();

			cStmt = conn.prepareCall("{call StoreThirdPartyPhishingResults(?, ?)}");
			cStmt.setLong(1, domainFileID);

			switch (isThirdPartyPhishing)
			{
				case NOTFLAGGED: cStmt.setNull(2, Types.TINYINT); break;
				case YES: cStmt.setBoolean(2, true); break;
				case NO: cStmt.setBoolean(2, false); break;
				default: cStmt.setNull(2, Types.TINYINT); break;
			}

			cStmt.execute();

			conn.close();
		} catch (SQLException e) {e.printStackTrace();}
	}

	private static DomainDetail getDomainDetail(String domainName, Connection conn)
	{
		DomainDetail detail = null;

		try
		{
			// DomainDetail
			CallableStatement cStmt = conn.prepareCall("{call GetDomainDetail(?)}");
			cStmt.setString(1, domainName);

			ResultSet rs = cStmt.executeQuery();
			if (rs.first())
			{
				detail = new DomainDetail();
				detail.setID(rs.getLong(1));
				detail.setDomainName(rs.getString(2));

				boolean a = rs.getBoolean(3); // is ssl verified
				if (rs.wasNull())
					detail.setIsSSLVerified(DatabaseStatus.NOTFLAGGED);
				else
					detail.setIsSSLVerified(a?DatabaseStatus.YES:DatabaseStatus.NO);
			}

		} catch (SQLException e) {e.printStackTrace();}

		return detail;
	}

	private static DomainFile[] getDomainFiles(long domainID, Connection conn)
	{
		DomainFile[] file = null;

		try
		{
			CallableStatement cStmt = conn.prepareCall("{call GetDomainFiles(?)}");
			cStmt.setLong(1, domainID);

			ResultSet rs = cStmt.executeQuery();

			int size = 0;
			if (rs != null)
			{
				rs.beforeFirst();
				rs.last();
				size = rs.getRow();
			}

			if (size > 0)
			{
				file = new DomainFile[size];

				rs.beforeFirst();
				while (rs.next())
				{
					int index = rs.getRow() - 1;
					file[index] = new DomainFile();
					file[index].setID(rs.getLong(1));
					file[index].setDomainID(rs.getLong(2));
					file[index].setDomainFile(rs.getString(3));

					boolean a = rs.getBoolean(4); // is phishing
					if (rs.wasNull())
						file[index].setIsPhishing(DatabaseStatus.NOTFLAGGED);
					else
						file[index].setIsPhishing(a?DatabaseStatus.YES:DatabaseStatus.NO);

					boolean b = rs.getBoolean(5); // is thirdpartyphishing
					if (rs.wasNull())
						file[index].setIsThirdPartyPhishing(DatabaseStatus.NOTFLAGGED);
					else
						file[index].setIsThirdPartyPhishing(b?DatabaseStatus.YES:DatabaseStatus.NO);

					file[index].setPageSource(rs.getString(6));
					file[index].setOriginalTimeStamp(rs.getDate(7));
					file[index].setLastCrawledTimeStamp(rs.getDate(8));
					file[index].setLastPhishedTimeStamp(rs.getDate(9));
				}
			}
		} catch (SQLException e) {e.printStackTrace();}

		return file;
	}

	private static DomainFile getDomainFile(String domainName, String domainFile, Connection conn)
	{
		DomainFile file = null;

		try
		{
			CallableStatement cStmt = conn.prepareCall("{call GetDomainFile(?,?)}");
			cStmt.setString(1, domainName);
			cStmt.setString(2, domainFile);

			ResultSet rs = cStmt.executeQuery();

			if (rs.first())
			{
				file = new DomainFile();
				file.setID(rs.getLong(1));
				file.setDomainID(rs.getLong(2));
				file.setDomainFile(rs.getString(3));

				boolean a = rs.getBoolean(4); // is phishing
				if (rs.wasNull())
					file.setIsPhishing(DatabaseStatus.NOTFLAGGED);
				else
					file.setIsPhishing(a?DatabaseStatus.YES:DatabaseStatus.NO);

				boolean b = rs.getBoolean(5); // is thirdpartyphishing
				if (rs.wasNull())
					file.setIsThirdPartyPhishing(DatabaseStatus.NOTFLAGGED);
				else
					file.setIsThirdPartyPhishing(b?DatabaseStatus.YES:DatabaseStatus.NO);

				file.setPageSource(rs.getString(6));
				file.setOriginalTimeStamp(rs.getDate(7));
				file.setLastCrawledTimeStamp(rs.getDate(8));
				file.setLastPhishedTimeStamp(rs.getDate(9));
			}
		} catch (SQLException e) {e.printStackTrace();}

		return file;
	}

	private static IPAddress[] getIPAddresses(long domainID, Connection conn)
	{
		IPAddress[] ip = null;

		try
		{
			CallableStatement cStmt = conn.prepareCall("{call GetIPAddresses(?)}");
			cStmt.setLong(1, domainID);

			ResultSet rs = cStmt.executeQuery();

			int size = 0;
			if (rs != null)
			{
				rs.beforeFirst();
				rs.last();
				size = rs.getRow();
			}

			if (size > 0)
			{
				ip = new IPAddress[size];

				rs.beforeFirst();
				while (rs.next())
				{
					int index = rs.getRow() - 1;
					ip[index] = new IPAddress();
					ip[index].setID(rs.getLong(1));
					ip[index].setDomainID(rs.getLong(2));
					String[] octets = rs.getString(3).split("\\.");
					for (int i=0;i<4;i++)
						ip[index].setOctet(i, Integer.parseInt(octets[i]));
				}
			}
		} catch (SQLException e) {e.printStackTrace();}

		return ip;
	}

	private static Certificate[] getCertificates(long domainID, Connection conn)
	{
		Certificate[] cert = null;

		try
		{
			CallableStatement cStmt = conn.prepareCall("{call GetCertificates(?)}");
			cStmt.setLong(1, domainID);

			ResultSet rs = cStmt.executeQuery();

			int size = 0;
			if (rs != null)
			{
				rs.beforeFirst();
				rs.last();
				size = rs.getRow();
			}

			if (size > 0)
			{
				cert = new Certificate[size];

				rs.beforeFirst();
				while (rs.next())
				{
					int index = rs.getRow() - 1;
					cert[index] = new Certificate();
					cert[index].setID(rs.getLong(1));
					cert[index].setDomainID(rs.getLong(2));
					cert[index].setIssuer(rs.getString(3));
					cert[index].setSubject(rs.getString(4));
					cert[index].setValidFrom(rs.getDate(5));
					cert[index].setValidTill(rs.getDate(6));
					cert[index].setBasicConstraint(rs.getInt(7));
					cert[index].setHierarchy(rs.getInt(8));
					cert[index].setFullCertificate(rs.getString(9));
				}
			}
		} catch (SQLException e) {e.printStackTrace();}

		return cert;
	}

	public static DomainDetail getDomainDetailForSSLVerified(String domainName)
	{
		DomainDetail detail = null;

		try
		{
			Connection conn = SSE.SSEDAO.getDBConnection();

			detail = getDomainDetail(domainName,conn);

			if (detail != null)
			{
				detail.setIPAddresses(getIPAddresses(detail.getID(),conn));
				detail.setCertificates(getCertificates(detail.getID(),conn));
			}

			conn.close();
		} catch (SQLException e) {e.printStackTrace();}

		return detail;
	}

	public static DomainDetail getDomainDetailForPhishing(String domainName, String domainFile)
	{
		DomainDetail detail = null;

		try
		{
			Connection conn = SSE.SSEDAO.getDBConnection();

			detail = getDomainDetail(domainName,conn);

			if (detail != null)
			{
				DomainFile[] file = {getDomainFile(domainName,domainFile,conn)};
				detail.setDomainFiles(file);

                                detail.setIPAddresses(getIPAddresses(detail.getID(), conn));
			}

			conn.close();
		} catch (SQLException e) {e.printStackTrace();}

		return detail;
	}

	public static DomainDetail getCrawlerInformation(String domainName)
	{
		DomainDetail detail = null;

		try
		{
			Connection conn = SSE.SSEDAO.getDBConnection();

			detail = getDomainDetail(domainName,conn);

			if (detail != null)
			{
				detail.setDomainFiles(getDomainFiles(detail.getID(),conn));
				detail.setIPAddresses(getIPAddresses(detail.getID(),conn));
				detail.setCertificates(getCertificates(detail.getID(),conn));
			}

			conn.close();
		} catch (SQLException e) {e.printStackTrace();}

		return detail;
	}

	public static void storeCrawlerInformation(DomainDetail detail)
	{
		try
		{
			CallableStatement cStmt = null;
			Connection conn = SSE.SSEDAO.getDBConnection();

			// DomainDetail
			cStmt = conn.prepareCall("{call InsertDomainDetail(?, ?)}");
			cStmt.registerOutParameter(2, Types.BIGINT); // id of domainDetail
			cStmt.setString(1, detail.getDomainName()); // domainName
			cStmt.execute(); // store domainDetail
			long domainID = cStmt.getLong(2); // id of domainDetail

			// DomainFile
                        if(detail.getDomainFiles()!=null)
                        {
                            cStmt = conn.prepareCall("{call InsertDomainFile(?, ?, ?)}");
                            cStmt.setLong(1, domainID); // id of domainDetail
                            for (int i=0;i<detail.getDomainFiles().length;i++)
                            {
                                    cStmt.setString(2,detail.getDomainFiles()[i].getDomainFile()); // domainFile
                                    cStmt.setString(3,detail.getDomainFiles()[i].getPageSource()); // pageSource
                                    cStmt.execute(); // store domainFile
                            }
                        }

			// IPAddress
                        if(detail.getIPAddresses() != null)
                        {
                            cStmt = conn.prepareCall("{call InsertIPAddress(?, ?)}");
                            cStmt.setLong(1, domainID); // id of domainDetail
                            for (int i=0;i<detail.getIPAddresses().length;i++)
                            {
                                    cStmt.setString(2, detail.getIPAddresses()[i].toString()); // ipAddress
                                    cStmt.execute(); // store ipAddress
                            }
                        }

			// Certificate
                        if(detail.getCertificates()!=null)
                        {
                            cStmt = conn.prepareCall("{call InsertCertificate(?, ?, ?, ?, ?, ?, ?, ?)}");
                            cStmt.setLong(1, domainID); // id of domainDetail
                            for (int i=0;i<detail.getCertificates().length;i++)
                            {
                                    cStmt.setString(2, detail.getCertificates()[i].getIssuer()); // issuer
                                    cStmt.setString(3, detail.getCertificates()[i].getSubject()); // subject
                                    cStmt.setDate(4, new java.sql.Date(detail.getCertificates()[i].getValidFrom().getTime())); // validFrom
                                    cStmt.setDate(5, new java.sql.Date(detail.getCertificates()[i].getValidTill().getTime())); // validTill
                                    cStmt.setInt(6, detail.getCertificates()[i].getBasicConstraint()); // basicConstraint
                                    cStmt.setInt(7, detail.getCertificates()[i].getHierarchy()); // hierarchy
                                    cStmt.setString(8, detail.getCertificates()[i].getFullCertificate()); // fullCert
                                    cStmt.execute(); // store certificate
                            }
                        }

			conn.close();
			System.err.println(domainID);
		} catch (SQLException e) {e.printStackTrace();}
	}

	private static Connection getDBConnection()
	{
		Connection conn = null;

        try
        {
            String userName = "sseadmin";
            String password = "cloudcomp";
            String url = "jdbc:mysql://10.211.20.30:3306/SSE";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println ("Database connection established");
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            System.err.println ("Cannot connect to database server");
        }

        return conn;
	}
}