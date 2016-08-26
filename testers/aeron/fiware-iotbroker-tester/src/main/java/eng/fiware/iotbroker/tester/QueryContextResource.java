package eng.fiware.iotbroker.tester;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/*
* GE owner suggestion: 
* "We usually use a simple HTTP server that responds with a fixed QueryContextResponse message 
*  to every QueryContextRequest message forwarded by the IoT Broker. 
*  You can maybe do something like that (and replicate of course)."
*/

@Path("/ngsi10/queryContext")
public class QueryContextResource {
	private String queryContextResponse; 
	//private int counter;
	private final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss:SSS";
	private String currentInstanceId;
	
	public QueryContextResource () {
		currentInstanceId = this.toString();
		int start = currentInstanceId.indexOf("@");
		currentInstanceId = currentInstanceId.substring(start, currentInstanceId.length()-1);		
	}
	
	private String getTimeStamp(String s, boolean incr){
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			//return sdf.format(cal.getTime()) + ": QueryContext.POST nr. "+ (incr ? ++counter : counter) + " " + s;
			return sdf.format(cal.getTime()) + " [instance id= " + currentInstanceId + "] " + s;
		} catch (Exception e){
			e.printStackTrace();
			//return "err with timestamp! nr=" + counter;
			return "err with timestamp! instance id=" + currentInstanceId;
		}
	}
	
    /*@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getQueryContext() {
		System.out.println(this.getTimeStamp()+ ": GET invoked!");
		return queryContextResponse;
    }*/
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public String post(String message) {
		synchronized (this) {
			System.out.println(this.getTimeStamp("begin..", true));
			//System.out.println(this.getTimeStamp("begin..", true) + " (received msg='"+ (message!=null ? message.replaceAll("\\s+","") : "") + "')") ;
				
			int idStartPos = message.indexOf("<id>");// length=4
			int idEndPos = message.indexOf("</id>");
			int typeStartPos = message.indexOf(" type=\"");// length=7
			int typeEndPos = message.indexOf("\"",typeStartPos+7);

			String id="";
			String type="";
		
			try {
				//System.out.println("idStartPos ="+ idStartPos + ", idEndPos ="+ idEndPos);								
				id = message.substring(idStartPos + 4, idEndPos);
				//System.out.println("id="+id);

				//System.out.println("typeStartPos ="+ typeStartPos + ", typeEndPos ="+ typeEndPos);	
				type = message.substring(typeStartPos + 7, typeEndPos);									
				//System.out.println("type="+type);
				
			} catch (Exception e){
				e.printStackTrace();
				System.out.println(this.getTimeStamp("Exception: "+e.getMessage(), false));
			}

			queryContextResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +		
									"<queryContextResponse>"  +
										"<contextResponseList>" +
											"<contextElementResponse>" +
												"<contextElement>" +
													"<entityId type=\""+type+"\" isPattern=\"false\">" +
														"<id>"+id+"</id>" +
													"</entityId>" +
													"<contextAttributeList>" +
														"<contextAttribute>" +
															"<name>dummyName</name>" +
															"<type>dummyType</type>" +
															"<contextValue>dummyValue</contextValue>" +
														"</contextAttribute>" +
													"</contextAttributeList>" +
												"</contextElement>" +
												"<statusCode>" +
													"<code>200</code>" +
													"<reasonPhrase>OK</reasonPhrase>" +
													"<details>fiware-iotbroker-tester_v1.0.5</details>" +
												"</statusCode>" +
											"</contextElementResponse>" +
										"</contextResponseList>" +
									"</queryContextResponse>" ; 		
			
			// Store the message
			//System.out.println("message="+message);
			
			System.out.println(this.getTimeStamp("..end", false));
			
			return queryContextResponse;
		}
	}

}
