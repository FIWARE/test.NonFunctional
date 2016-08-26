// libraries
import java.util.Random;
import java.util.*;
import java.text.DecimalFormat;
import java.text.*;
import java.util.Stack;

Random rnd;
DecimalFormat dfType, dfId;

String tempRequest;
String request;
String isPattern = "false";
String typePrefix = "agent";
String idPrefix = "id";

int maxEntities, maxAgents;
int entitiesNumber;

// constants
try {
	rnd = new Random();
	dfType = new DecimalFormat("00"); 
	dfId = new DecimalFormat("000"); 
	tempRequest = " ";
	maxEntities = Integer.parseInt(vars.get("MAX_ENTITIES"));
	maxAgents = Integer.parseInt(vars.get("MAX_AGENTS"));
	entitiesNumber = Integer.parseInt(vars.get("ENTITIES"));
	request = "{ \"entities\": [";

	// determine number of entities (entity max is 20)
	if (entitiesNumber > maxEntities){
		entitiesNumber = maxEntities;
		vars.put("ENTITIES",entitiesNumber.toString());
		System.out.println ("WARN - ENTITIES maximum value is 20, now ENTITIES is 20...");
	}
	// if ENTITIES property is zero or negative value, the entity number will be a random value.
	if (entitiesNumber <= 0){
		entitiesNumber= rnd.nextInt(maxEntities)+1;	
	}
	//System.out.println ("ENTITIES nr="+ entitiesNumber);
	
	// generate a payload with N entities.
	for (int i=0; i<entitiesNumber; i++) {		
		typeSuffix = dfType.format(rnd.nextInt(maxAgents)+1);
		idSuffix = dfId.format(rnd.nextInt(maxEntities)+1);
		
		type =  typePrefix + typeSuffix; 	 			//e.g "type": "agent07"
		id =  type + "_" + idPrefix + "_" + idSuffix;   //e.g "id": "agent07_id_001"
		tempRequest = tempRequest + "{ \"type\": \""+ type + "\", \"isPattern\": \"" + isPattern + "\", \"id\": \"" + id +"\"" + "},";	
	}
	tempRequest = tempRequest.substring(0, tempRequest.length()-1);
	request = request + tempRequest;
	request += "] }";

	vars.put("queryRequest", request.toString());
	
} catch (Exception e) {
	e.printStackTrace();
}
