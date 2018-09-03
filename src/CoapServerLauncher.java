import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.Type;


public class CoapServerLauncher {

	public static void main(String[] args) throws InterruptedException {			
		CoapServer server = new CoapServer();	

		final CoapResource fetch = new CoAP_fetchable_resource("fetchable");
		final CoapResource patch = new CoAP_patchable_resource("patchable");
		server.add(fetch);
		server.add(patch);
		
		server.add(new DefaultTest());
		server.add(new LongPath());
		server.add(new Query());
		server.add(new Separate());
		server.add(new Large());
		server.add(new LargeUpdate());
		server.add(new LargeCreate());
		server.add(new LargePost());
		server.add(new LargeSeparate());
		server.add(new Observe());
		server.add(new ObserveNon());
		server.add(new ObserveReset());
		server.add(new ObserveLarge());
		server.add(new ObservePumping());
		server.add(new ObservePumping(Type.NON));
		server.add(new LocationQuery());
		server.add(new MultiFormat());
		server.add(new Link1());
		server.add(new Link2());
		server.add(new Link3());
		server.add(new Path());
		server.add(new Validate());
		server.add(new Create());
		server.add(new Shutdown());
		
		server.start();
	}
}
