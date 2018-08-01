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
		String pl = exchange.advanced().getCurrentRequest().getPayloadString();
		
		JSONParser parser = new JSONParser();
		
		try{
	         Object obj = parser.parse(pl);
	         JSONArray array = (JSONArray)obj;

	         JSONObject jsonObject = (JSONObject)array.get(0);
	         System.out.println("Information of packet received: ");
//----------------------------------------------------------------------------------------------------//
	         /*String op = (String) jsonObject.get("op");
	         System.out.println("op: " + jsonObject.get("op"));
	         
	         String path = (String) jsonObject.get("path");
	         System.out.println("path: " + jsonObject.get("path"));
	         
	         long value = (long) jsonObject.get("value");
	         System.out.println("value: " + jsonObject.get("value"));
	         
	         if ((op.equals("replace")) && (path.equals("x-coord"))){
	        	x_coord = value;
	        	exchange.respond(CHANGED);
	         } else {
	        	exchange.respond(BAD_REQUEST);
	         }*/

//----------------------------------------------------------------------------------------------------//
	         /*long value_x_coord = (long) jsonObject.get("x-coord");
	         System.out.println("x-coord: " + jsonObject.get("x-coord"));
	         
	         if (value_x_coord != x_coord){
	        	x_coord = value_x_coord;
	        	exchange.respond(CHANGED);
	         } else {
	        	exchange.respond(BAD_REQUEST);
	         }*/

//-----------------------------------------------------------------------------------------------------//
	         String op = (String) jsonObject.get("op");
	         System.out.println("op: " + jsonObject.get("op"));
	         
	         String path = (String) jsonObject.get("path");
	         System.out.println("path: " + jsonObject.get("path"));
	         
	         String value = (String) jsonObject.get("value");
	         System.out.println("value: " + jsonObject.get("value"));
	         
	         String[] foo1 = {"bar","bar","baz"};
	         
	         JSONArray jarray = new JSONArray();
	         
	         if ((op.equals("add")) && (path.equals("foo/1"))){
	        	 for (int i = 0; i < foo1.length; i++) {
		             jarray.add(foo1[i]);
		         }
	        	 exchange.respond(CHANGED);
	         } else {
	        	 for (int i = 0; i < foo.length; i++) {
		             jarray.add(foo[i]);
		         }
	        	 exchange.respond(BAD_REQUEST);
	         }

//------------------------------------------------------------------------------------------------------//
	         JSONObject objOut = new JSONObject();

	         objOut.put("x-coord", x_coord);
	         objOut.put("y-coord", y_coord);
	         /*JSONArray jarray = new JSONArray();
	         for (int i = 0; i < foo.length; i++) {
	             jarray.add(foo[i]);
	         }*/
	         objOut.put("foo", jarray);
	         
	         System.out.println("json original: " + jsonOriginal);
	         System.out.println("json final: " + objOut);
	         
		}catch(ParseException pe){
			
	         System.out.println("position: " + pe.getPosition());
	         System.out.println(pe);
		}
	}

}
