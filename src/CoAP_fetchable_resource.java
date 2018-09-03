import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;
import static org.eclipse.californium.core.coap.MediaTypeRegistry.*;

import java.util.Arrays;


public class CoAP_fetchable_resource extends CoapResource {
	
	public CoAP_fetchable_resource(String name) {
		super(name);
	}
	
	String jsonOriginal = "{\"x-coord\": 256,\"y-coord\": 45,\"foo\": [\"bar\",\"baz\"]}";
	
	long x_coord = 256;
	long y_coord = 45;
	String[] foo = {"bar","baz"};
	boolean not_send = false;
	
	@Override
	public void handleFETCH(CoapExchange exchange) {
		// the Max-Age value should match the update interval
		exchange.advanced().setCurrentTimeout(2000);
		exchange.setMaxAge(5);
		int content_format = exchange.getRequestOptions().getContentFormat();
		if (content_format == 50) {
			
			String pl = exchange.advanced().getCurrentRequest().getPayloadString();
			
			JSONParser parser = new JSONParser();
			
			try{
		         Object obj = parser.parse(pl);
		         JSONArray array = (JSONArray)obj;
		         System.out.println("Information of packet received: ");
		         
		         JSONObject objOut = new JSONObject();
//-----------------------------------------------------------------------------------------------------//
		         for (int i = 0; i < array.size(); i++) {
		             //based on you key types
		             String keyStr = (String) array.get(i);
	
		             //Print key and value
		             System.out.println("key: "+ keyStr);
		             
		             if (keyStr.equals("x-coord")) {
		            	 objOut.put(keyStr, x_coord);
		             }
		             else if (keyStr.equals("y-coord")) {
		            	 objOut.put(keyStr, y_coord);
		             }
		             else if (keyStr.equals("foo")) {
		            	 JSONArray jarray = new JSONArray();
		    	         for (int j = 0; j < foo.length; j++) {
		    	             jarray.add(foo[j]);
		    	         }
		    	         objOut.put(keyStr, jarray);
		             } else {
		            	 exchange.respond(NOT_FOUND);
		            	 not_send = true;
		             }
		         }
//------------------------------------------------------------------------------------------------------//
		         
		         System.out.println("json original: " + jsonOriginal);
		         if (not_send == false) {
		        	 String response = objOut.toJSONString();
			 		 exchange.respond(CONTENT, response, APPLICATION_JSON);
		        	 System.out.println("json sent: " + objOut);
		         };
		         
			}catch(ParseException pe){
				 exchange.respond(BAD_REQUEST);
		         System.out.println("position: " + pe.getPosition());
		         System.out.println(pe);
			}
		} else {
			exchange.respond(UNSUPPORTED_CONTENT_FORMAT);
		}
	}
}
