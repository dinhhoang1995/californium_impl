
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.Type;

public class CoapServerLauncher {

	public static void main(String[] args) throws InterruptedException {			
		CoapServer server = new CoapServer();	

		final CoapResource res_obs_01_04_05_06_07 = new Coap_temp_resource("obs", Type.CON);
		final CoapResource fetch = new CoAP_fetchable_resource("fetchable");
		final CoapResource patch = new CoAP_patchable_resource("object");
		final CoapResource res_obs_02 = new Coap_temp_resource("obs-non", Type.NON);
		final CoapResource res_blk_large = new Large();
		server.add(fetch);
		server.add(patch);
		server.add(res_blk_large);
		server.add(res_obs_01_04_05_06_07);
		server.add(res_obs_02);
		server.start();
	}
}
