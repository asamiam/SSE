var cache = {};
//cacheing variable
setInterval(function(){cache = {}; alert("Cache cleared");}, 1000 * 60 * 10);
//clear cache every 1 minute(s)
var webservice = "https://www.wreferral.com/SSEService/";

/**
* This function does not seem to be called at all, remove?
*/

function verify(searchengine, site)
{

	poststring="name="+site;
	//sslurl=site.replace(/\./g, "\\.").replace(/https?:\/\//i, "");
	//sslurl="https://(www\\.)?"+sslurl;
	alert("here");
	sslurl=searchengine;

	alert(sslurl);
	var result="";
	var ex;
	try
	{
		var soc=new Packages.java.net.URL(searchengine).openConnection();
		soc.setDoInput(true);
		soc.setDoOutput(true);
		alert("here1 "+poststring);
		var k=new java.io.OutputStreamWriter(soc.getOutputStream());
		alert("here1.3");
		var writer=new Packages.java.io.BufferedWriter(k);
		alert("here1.4");
		writer.write(poststring);
		alert("here1.5");
		writer.flush();
		writer.close();
		alert("here2");
		var reader=new java.io.BufferedReader(new java.io.InputStreamReader(soc.getInputStream()));
		var line=null;
		while((line=reader.readLine())!=null)
			result+=line;
		alert(result);
		reader.close();
	}catch(ex)
	{
		alert(" Danger!\n Cannot connect to the secure search engine!\n Any information entered into the following page can be exposed to the attacker!!\n");
		alert(ex);
	}
	//var pattern=new RegExp(sslurl, "i");
	//return pattern.exec(result);
	return result;
}

/**
* This function actually performs the query to wreferral
* @param ajax-the type of request (XMLHttp), searchengine-the URL of the webservice to use,
*  site-the URL to be tested
*/

function ajaxcall(ajax,searchengine, site) {

  var parameters="name="+encodeURI(site)
  ajax.open("POST", searchengine, false);
  ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded");
  ajax.setRequestHeader("Content-length", parameters.length);
  ajax.setRequestHeader("Connection", "close");
  ajax.send(parameters);
  if(ajax.readyState==4){
	  var result=ajax.responseText;
	  //alert(result);
	  return result;
  }
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

	if (!ajax && typeof XMLHttpRequest != 'undefined') {
	  ajax = new XMLHttpRequest();
	}
	var urlbar = document.getElementById("urlbar");
	var str=urlbar.value;
	if(document.getElementById("sseen-button").label=="SSE enabled")
	{

		if(cache[str]) //if this url is cached, use cached data (using url as cache index)
		{
			var res = cache[str];
			//alert("Cached data found and used: site=" + str + " result=" + res);
		}

		else //otherwise, query wreferral and add to cache
		{
			var res=ajaxcall(ajax, webservice + "query.do",urlbar.value);
			cache[str] = res;
			//alert("Data added to cache:" + cache[str]);
		}

		//var res=ajaxcall(ajax,"https://www.wreferral.com/SSEService/query.do",urlbar.value);
		resSplit=res.split(" ");
		if(resSplit[0]=="yes" && resSplit[1]=="no"){
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
			gURLBar.handleCommand(event);
		}
		else if(resSplit[1]=="yes"){
			alert("This is a phishing site! Proceed with caution! (old: SSE will not load this site!)");
			gURLBar.handleCommand(event);//allowing user to decide, this used to not be here!
		}
		else if(resSplit[0]=="no" && resSplit[1]=="no"){
			gURLBar.handleCommand(event);
		}
	} else{
		gURLBar.handleCommand(event);
	}


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

	if (!ajax && typeof XMLHttpRequest != 'undefined') {
	  ajax = new XMLHttpRequest();
	}

	var str=event.target.href;
	if(document.getElementById("sseen-button").label=="SSE enabled")
	{

		if(cache[str]) //if this url is cached, use cached data (using url as cache index)
		{
			var res = cache[str];
			//alert("Cached data found and used: site=" + str + " result=" + res);
		}

		else //otherwise, query wreferral and add to cache
		{
			var res=ajaxcall(ajax,webservice + "query.do",str);
			cache[str] = res;
			//alert("Data added to cache:" + cache[str]);
		}

		//------------------------------------------------------------------------
		//                       Change above URL for new site?
		//------------------------------------------------------------------------

		resSplit=res.split(" ");
		if(resSplit[0]=="yes" && resSplit[1]=="no"){
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
			gURLBar.handleCommand(event);
		}
		else if(resSplit[1]=="yes"){
			alert("This is a phishing site! Proceed with caution! (old: SSE will not load this site!)");
			gURLBar.handleCommand(event);//allowing user to decide, this used to not be here!
		}
		else if(resSplit[0]=="no" && resSplit[1]=="no"){
			gURLBar.handleCommand(event);
		}
	} else{
		gURLBar.handleCommand(event);
	}


}

/**
* Initializes the plugin, establishing a listener for address bar
* or hyperlink click events
* @param none
*/

function init()
{
	//alert("init");
	window.addEventListener( 'mouseup',
		    function(e){
		    	//alert("e");
		        if(e.target.nodeName=='A'){ forceSSLOnclick(e); }
	    }, false);
	//alert("done");
	var urlbar = document.getElementById("urlbar");
	urlbar.onTextEntered=forceSSL;
	//var href=document.getElementById('href');
	//href.onclick=forceSSLOnclick;
	//alert("done registering urlbar");

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
		button.style="color: red; font-size: 14pt;";//doesn't work
	}
	else
	{
		button.label="SSE enabled";
		button.style="color: blue; font-size: 12pt;";//doesn't work
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
		alert("Now using webservice:" + webservice);
	}
	if (choice == 2)
	{
		webservice = "https://testserver.com";
		alert("Now using webservice:" + webservice);
	}
}

/**
* This function submits a query to a secondary webservice at wreferral and
* navigates to a full webpage synopsis of a given URL.
* @param text-the URL the synopsis will be about
*/

function webpagegen(text)
{
	alert("Text altered: " + text);
}

//this function will eventually generate a synopsis webpage for the user
function testwebpagegen(text)
{
	alert("Text altered: " + text);
}

window.addEventListener("load", init, false);