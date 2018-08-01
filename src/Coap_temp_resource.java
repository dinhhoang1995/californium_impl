import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.server.resources.CoapExchange;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;
import static org.eclipse.californium.core.coap.MediaTypeRegistry.*;

public class Coap_temp_resource extends CoapResource {

	public Coap_temp_resource(String name, Type type) {
		super(name);

		setObservable(true); 
		setObserveType(type); 
		//getAttributes().setAttribute(attr, value);
		// mark observable in the Link-Format
		getAttributes().setObservable(); 

		// schedule a periodic update timer
		// otherwise just call changed() as appropriate
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {@Override
			public void run() {
			changed(); // notify all observers
		}}, 0, 5000);

	}

	@Override
	public void handleGET(CoapExchange exchange) {
		// the Max-Age value should match the update interval
		exchange.advanced().setCurrentTimeout(2000);
		exchange.setMaxAge(5); 
		Random rand = new Random();
		float randomTemp  = rand.nextFloat() * (24 - 18) + 18;
		if (payload == null) {
			exchange.respond("Current temperature: " + 
					randomTemp + " x-coord : " + 256 + " y-coord : " + 45);
		}
		else {
			//OBS_08
			exchange.respond(CONTENT, payload, currentContentFormat);     	
			//OBS_09 
			//exchange.respond(new String(payload));
		}
	}
	int currentContentFormat = TEXT_PLAIN;
	//String np = null;
	byte[] payload = null;
	@Override
	public void handlePOST(CoapExchange exchange) {
		// the Max-Age value should match the update interval
		exchange.advanced().setCurrentTimeout(5000);
		exchange.setMaxAge(5); 
		payload = exchange.getRequestPayload();
		exchange.respond(CHANGED);
		if (exchange.getRequestOptions().getContentFormat() != currentContentFormat) {
			this.clearAndNotifyObserveRelations(INTERNAL_SERVER_ERROR); //OBS_08
		}
		this.currentContentFormat = exchange.getRequestOptions().getContentFormat();
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {		
		//		clearAndNotifyObserveRelations(NOT_FOUND);
		exchange.respond(DELETED);
	}


}
