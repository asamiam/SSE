package SSE;

public class Testing
{
    public static void main(String[] args)
    {
        DomainDetail dd = new DomainDetail();
        dd.setDomainName("google.com");
        
        DomainFile[] df = new DomainFile[4];
        
        for(int i=0;i<df.length;i++)
        {
            df[i] = new DomainFile();
            df[i].setDomainFile("page" + i + ".htm");
            df[i].setPageSource("hello this is page source");
        }

        IPAddress[] ip = new IPAddress[5];

        for(int i=0;i<ip.length;i++)
            ip[i] = new IPAddress(10,10,10,i);
        
        dd.setDomainFiles(df);
        dd.setIPAddresses(ip);
        
        SSEDAO.storeCrawlerInformation(dd);

        DomainDetail result = SSEDAO.getDomainDetailForPhishing("google.com", "page0.htm");
        System.err.println(result.getDomainName());
        System.err.println(result.getDomainFiles()[0].getDomainFile());
        System.err.println(result.getDomainFiles()[0].getIsPhishing());
        System.err.println(result.getDomainFiles()[0].getPageSource());

        for(int i=0;i<result.getIPAddresses().length;i++)
            System.err.println(result.getIPAddresses()[i].toString());
    }
}
