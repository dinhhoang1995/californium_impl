import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.server.resources.CoapExchange;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;
import static org.eclipse.californium.core.coap.MediaTypeRegistry.*;


import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class CoAP_patchable_resource extends CoapResource {
	
	public CoAP_patchable_resource(String name) {
		super(name);
	}
	
	String jsonOriginal = "{\"x-coord\": 256,\"y-coord\": 45,\"foo\": [\"bar\",\"baz\"]}";
	
	long x_coord = 256;
	long y_coord = 45;
	String[] foo = {"bar","baz"};
	
	@Override
	public void handlePATCH(CoapExchange exchange) {
		// the Max-Age value should match the update interval
		exchange.advanced().setCurrentTimeout(2000);
		exchange.setMaxAge(5);
		int content_format = exchange.getRequestOptions().getContentFormat();
		String pl = exchange.advanced().getCurrentRequest().getPayloadString();
		
		JSONParser parser = new JSONParser();
		try{
	         Object obj = parser.parse(pl);
	         JSONArray array = (JSONArray)obj;

	         JSONObject jsonObject = (JSONObject)array.get(0);
	         JSONArray jarray = new JSONArray();
	         System.out.println("Information of packet received: ");
//----------------------------------------------------------------------------------------------------//
	         if (content_format == 51) { //application/json-patch+json
		         String op = (String) jsonObject.get("op");
		         System.out.println("op: " + jsonObject.get("op"));
		         
		         String path = (String) jsonObject.get("path");
		         System.out.println("path: " + jsonObject.get("path"));
		         
		         long value_long = 0;
		         String value_string = null;
		         if (jsonObject.get("value").getClass() == Long.class) {
		        	 value_long = (long) jsonObject.get("value");
		         }
		         if (jsonObject.get("value").getClass() == String.class) {
		        	 value_string = (String) jsonObject.get("value");
		         }
		         System.out.println("value: " + jsonObject.get("value"));
		         if (op.equals("replace")) {
			         if (path.equals("x-coord")){
			        	x_coord = value_long;
			        	exchange.respond(CHANGED);
			         } else if (path.equals("y-coord")){
			        	y_coord = value_long;
				        exchange.respond(CHANGED);
			         } else if (path.equals("foo")){
			        	String[] tmp = {value_string};
			        	foo = tmp;
					    exchange.respond(CHANGED); 
			         } else {
			        	 exchange.respond(NOT_FOUND);
			         }
			         
			         for (int i = 0; i < foo.length; i++) {
			             jarray.add(foo[i]);
			         }
		         } else if (op.equals("add")) {
		        	 if (path.equals("foo/1")) {
		        		 String[] foo1 = new String[foo.length + 1];
		        		 foo1[0] = value_string;
		        		 for(int i=1;i < foo.length + 1;i++) {
		        			 foo1[i] = foo[i - 1];
		        		 }
		        		 exchange.respond(CHANGED);
		    	         for (int i = 0; i < foo1.length; i++) {
		    	             jarray.add(foo1[i]);
		    	         }
		        	 }
		         }
		         
	         } else if (content_format == 52) { //application/merge-patch+json
		         long value_x_coord = (long) jsonObject.get("x-coord");
		         System.out.println("x-coord: " + jsonObject.get("x-coord"));
		         
		         if (value_x_coord != x_coord){
		        	x_coord = value_x_coord;
		        	exchange.respond(CHANGED);
		         }
		         for (int i = 0; i < foo.length; i++) {
		             jarray.add(foo[i]);
		         }
	         } else {
	        	 exchange.respond(UNSUPPORTED_CONTENT_FORMAT);
	        	 for (int i = 0; i < foo.length; i++) {
		             jarray.add(foo[i]);
		         }
	         }
//------------------------------------------------------------------------------------------------------//
	         JSONObject objOut = new JSONObject();

	         objOut.put("x-coord", x_coord);
	         objOut.put("y-coord", y_coord);
	         objOut.put("foo", jarray);
	         
	         System.out.println("json original: " + jsonOriginal);
	         System.out.println("json final: " + objOut);
	         
		}catch(ParseException pe){
			 exchange.respond(BAD_REQUEST);
	         System.out.println("position: " + pe.getPosition());
	         System.out.println(pe);
		}	
	}	

}
