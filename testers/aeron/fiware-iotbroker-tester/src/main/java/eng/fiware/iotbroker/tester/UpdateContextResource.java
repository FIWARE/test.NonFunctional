package eng.fiware.iotbroker.tester;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.Calendar;
import java.text.SimpleDateFormat;

@Path("/ngsi10/updateContext")
public class UpdateContextResource {
    public static final String CLICHED_MESSAGE = "/ngsi10/updateContext invoked!";

	public String UPDATE_CONTEXT_RESPONSE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +		
											"<updateContextResponse>"  +
											"<errorCode>" +
											"<code>200</code>" +
											"<reasonPhrase>OK</reasonPhrase>" +
											"<details>fiware-iotbroker-tester_v1.0.4</details>" +
											"</errorCode>" +
											"</updateContextResponse>";
									
	/*
	public String UPDATE_CONTEXT_RESPONSE_JSON = "{"+
											"\"errorCode\": {"+
											"\"details\": \"\","+
											"\"code\": 200,"+
											"\"reasonPhrase\": \"OK\""+
											"}}";											
	*/

	public String UPDATE_CONTEXT_RESPONSE_JSON = "{\"errorCode\":{\"code\":200,\"reasonPhrase\":\"OK\",\"details\":\"stress test\"}}";
	// with this JSON: 
	// {"errorCode":{"code":200,"reasonPhrase":"OK","details":"stress test"}}
	// IoTBroker log is: 
	// 2016-07-21 12:56:23 ERROR [spring]:843 - Servlet.service() for servlet spring threw exception
	// java.util.regex.PatternSyntaxException: Illegal repetition
	

	/*
	public String UPDATE_CONTEXT_RESPONSE_JSON = "{\"updateContextResponse\": {"+
											"\"errorCode\": {"+
											"\"details\": \"mockup agent response\","+
											"\"code\": \"200\","+
											"\"reasonPhrase\": \"OK\""+
											"}}}";	
	// with this JSON: 
	// {"updateContextResponse": {"errorCode": {"details": "mockup agent response","code": "200","reasonPhrase": "OK"}}}
	// IoTBroker log is: 
	// 2016-07-21 09:41:36 ERROR [spring]:843 - Servlet.service() for servlet spring threw exception
	// java.lang.StringIndexOutOfBoundsException: String index out of range: 3
					
	*/
		
	private static int counter;
	private final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss:SSS";

	
	/*
    @GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getUpdateContext() {
		System.out.println("GET invoked to "+CLICHED_MESSAGE);
		return UPDATE_CONTEXT_RESPONSE;
    }*/
	
	private String getTimeStamp(String s, boolean incr){
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			return sdf.format(cal.getTime()) + ": UpdateContext.POST nr. " + (incr ? ++counter : counter) + " " + s;
		} catch (Exception e){
			e.printStackTrace();
			return "err with timestamp! nr=" + counter;
		}
	}
	
	
	@POST
	//@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	//@Produces({MediaType.APPLICATION_XML})
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public String post(String message) {
		System.out.println(this.getTimeStamp("begin..", true));
		
		//System.out.println("message IN ="+message);
		//System.out.println(this.getTimeStamp("begin..", true) + " (received msg='"+ (message!=null ? message.replaceAll("\\s+","") : "") + "')") ;
		
		// Store the message
		//
		//System.out.println("message OUT ="+UPDATE_CONTEXT_RESPONSE_XML);
		
		System.out.println(this.getTimeStamp("..end", false));
		
		return UPDATE_CONTEXT_RESPONSE_XML;
	}

}
