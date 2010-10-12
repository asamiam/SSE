var cache = {};
//cacheing variable
setInterval(function(){cache = {}; alert("Cache cleared");}, 1000 * 60 * 10);
//clear cache every 1 minute(s)
var webservice = "http://securesearch-test.eas.asu.edu:8080/";
var query = "remoteSearch.do";
var qname = "URL=";
var debug = false;


var cls = Components.classes['@mozilla.org/network/dns-service;1'];
var iface = Components.interfaces.nsIDNSService;
var dns = cls.getService(iface);


//This function is not fully functional, it does not compare addresses to database
// And it only supports domain name urls e.g.
// www.google.com is fine but
// www.google.com/ is not fine nor is
// www.chase.com/chase.html is not fine
function verifyIP(url)
{

    //The IP address functionality adapted from 2007 Jan Dittmer <jdi@l4x.org>
    //Plugin called ShowIP

    //alert(url);

    if(url.substring(0,8)=="https://")
    {
	url=url.substring(8, url.length);
    }
    if(url.substring(0,7)=="http://")
    {
        url=url.substring(7, url.length);
    }





    try {

    var nsrecord = dns.resolve(url, true);



    while (nsrecord && nsrecord.hasMore())
    {
        //compare with database results
        var ip = nsrecord.getNextAddrAsString();
        if(debug)
        {
            alert("IP address of " + url + ": " + ip);//this will change to checking ip address with database info
        }
    }

    } catch (e)
    {
	alert("URL not resolvable");
    }
}


/**
* This function actually performs the query to wreferral, returns a string
* containing answer to two yes or no questions:
*
* Is the site non-SSL-verified?
* Is the site a phishing site?
*
* @param ajax-the type of request (XMLHttp),
* @param searchengine-the URL of the webservice to use,
* @param site-the URL to be tested
*/

function ajaxcall(ajax,searchengine, site) {

  var parameters=qname+encodeURI(site);
  ajax.open("POST", searchengine, false);
  ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded");
  ajax.setRequestHeader("Content-length", parameters.length);
  ajax.setRequestHeader("Connection", "close");
  ajax.send(parameters);
  if(ajax.readyState==4){
	  var result=ajax.responseText;
          if(debug)
          {
            var resSplit=result.split(" ");
            alert("No https? " + resSplit[0] + ", Phishing? " + resSplit[1] + " according to " + searchengine);
          }
          return result;
  }
  else
      return "fail fail";
}

/**
* Upon entering a URL into the address bar and navigating to it, this function
* checks if the data for the URL is in the cache and if not, queries the wreferral
* webservice to determine security.
* @param event-the navigation event to the URL in the address bar
*/

function forceSSL(event)
{
	var ajax = false;
        var res = null;
	if (!ajax && typeof XMLHttpRequest != 'undefined') {
	  ajax = new XMLHttpRequest();
	}
	var urlbar = document.getElementById("urlbar");
	var str=urlbar.value;
	if(document.getElementById("sseen-button").label=="SSE enabled")
	{

		if(cache[str]) //if this url is cached, use cached data (using url as cache index)
		{
			res = cache[str];
			if(debug)
                        {
                            alert("Cached data found and used: site=" + str + " result=" + res);
                        }
		}

		else //otherwise, query wreferral and add to cache
		{
			res=ajaxcall(ajax, webservice + query,urlbar.value);
			cache[str] = res;
			if(debug)
                        {
                            alert("Data added to cache: " + cache[str]);
		        }
                }

		resSplit=res.split(" ");
		if(resSplit[0]=="no" && resSplit[1]=="no"){
			if(str.substring(0,5)=="https"){
				str=str;
			}else if(str.substring(0,4)=="http"){
				str="https"+str.substring(4,str.length);
			}else if(!(str.substring(0,3)=="www")){
				str="https://www."+str;
			}else if(!(str.substring(0,4)=="http")){
						str="https://"+str;
			}
			urlbar.value=str;
			if(debug)
                        {
                            alert("Site safe(secure)");
			}
                        gURLBar.handleCommand(event);
		}
		else if(resSplit[1]=="yes"){
			alert("This may be a phishing site! Proceed with caution! (old: SSE will not load this site!)");
			gURLBar.handleCommand(event);//allowing user to decide, this used to not be here!
		}
		else if(resSplit[0]=="yes" && resSplit[1]=="no"){
			if(debug)
                        {
                            alert("Site safe");
			}
                        gURLBar.handleCommand(event);
		}
	} else{
		gURLBar.handleCommand(event);
	}
        //alert("Going into verifyIP");
        verifyIP(str);

}

/**
* Upon clicking a hyperlink, this function checks if the data
* for the URL is in the cache and if not, queries the wreferral
* webservice to determine security.
* @param event-the navigation event to the URL in hyperlink
*/

function forceSSLOnclick(event)
{
	var ajax = false;
        var res = null;

	if (!ajax && typeof XMLHttpRequest != 'undefined') {
	  ajax = new XMLHttpRequest();
	}

	var str=event.target.href;
	if(document.getElementById("sseen-button").label=="SSE enabled")
	{

		if(cache[str]) //if this url is cached, use cached data (using url as cache index)
		{
			res = cache[str];
			if(debug)
                        {
                            alert("Cached data found and used: site=" + str + " result=" + res);
		        }
                }

		else //otherwise, query wreferral and add to cache
		{
			res=ajaxcall(ajax,webservice + query,str);
			cache[str] = res;
			if(debug)
                        {
                            alert("Data added to cache:" + cache[str]);
		        }
                }

		//------------------------------------------------------------------------
		//                       Change above URL for new site?
		//------------------------------------------------------------------------

		resSplit=res.split(" ");
		if(resSplit[0]=="no" && resSplit[1]=="no"){
			if(str.substring(0,5)=="https"){
				str=str;
			}else if(str.substring(0,4)=="http"){
				str="https"+str.substring(4,str.length);
			}else if(!(str.substring(0,3)=="www")){
				str+="https://www."+str;
			}else if(!(str.substring(0,4)=="http")){
				str="https://"+str;
			}
			urlbar.value=str;
			if(debug)
                        {
                            alert("Site safe(secure)");
			}
                        gURLBar.handleCommand(event);
		}
		else if(resSplit[1]=="yes"){
			alert("This is a phishing site! Proceed with caution! (old: SSE will not load this site!)");
			gURLBar.handleCommand(event);//allowing user to decide, this used to not be here!
		}
		else if(resSplit[0]=="yes" && resSplit[1]=="no"){
			if(debug)
                        {
                            alert("Site safe");
			}
                        gURLBar.handleCommand(event);
		}
	} else{
		gURLBar.handleCommand(event);
	}
        verifyIP(str);

}

/**
* Initializes the plugin, establishing a listener for address bar
* or hyperlink click events
*
*/

function init()
{
	window.addEventListener( 'mouseup',
		    function(e){
		    	//alert("e");
		        if(e.target.nodeName=='A'){ forceSSLOnclick(e); }
	    }, false);
	var urlbar = document.getElementById("urlbar");
	urlbar.onTextEntered=forceSSL;


}

/**
* Enables or disables the verification aspect of the plugin.
*/

function toggle()
{
	var button=document.getElementById("sseen-button");
	if(button.label=="SSE enabled")
	{
		button.label="SSE disabled";
	}
	else
	{
		button.label="SSE enabled";
	}
}

/**
* Changes between webservices to use (default wreferral) for verification
* choice-an integer signifying a certain webservice
*/

function switchWebservice(choice)
{
	if (choice == 1)
	{
		webservice = "https://www.wreferral.com/SSEService/";
		query = "query.do";
		qname = "name=";
		alert("Now using webservice: " + webservice);
                debug = false;
	}
	if (choice == 2)
	{
		webservice = "http://securesearch-test.eas.asu.edu:8080/";//this will be https at a later date
		query = "remoteSearch.do";
		qname = "URL=";
		alert("Now using webservice: " + webservice);
                debug = false;
	}
        if (choice == 3)
	{
		webservice = "http://securesearch-test.eas.asu.edu:8080/";//this will be https as a later date
		query = "remoteSearch.do";
		qname = "URL=";
		alert("Now using webservice: " + webservice);
                debug = true;
	}
}

/**
* This function submits a query to a secondary webservice at wreferral and
* navigates to a full webpage synopsis of a given URL.
* @param text-the URL the synopsis will be about
*/

//this function will eventually generate a synopsis webpage for the user
function webpagegen(text)
{
    if(debug)
	alert("Text altered: " + text);
}

window.addEventListener("load", init, false);