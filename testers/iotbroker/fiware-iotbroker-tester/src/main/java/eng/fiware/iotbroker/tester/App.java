package eng.fiware.iotbroker.tester;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javax.ws.rs.core.Application;

/**
 * Application using only the standard JAX-RS API and lightweight HTTP server bundled in JDK.
 */
public class App {

    /**
     * Starts the lightweight HTTP server serving the JAX-RS application.
     *
     * @return new instance of the lightweight HTTP server
     * @throws IOException
     */
	public static final String DATA_CONSUMER = "data-consumer";
	public static final String DATA_PROVIDER = "data-provider";
	
    static HttpServer startServer(int port, Application application) throws IOException {
        // create a new server listening at port xxxx
        final HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		HttpHandler handler;
       		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                server.stop(0);
            }
        }));

        // create a handler wrapping the JAX-RS application		
		handler = RuntimeDelegate.getInstance().createEndpoint(application, HttpHandler.class);
		
		// map JAX-RS handler to the server root
		System.out.println("startServer(): "+getBaseURI(port));
 	   
		server.createContext(getBaseURI(port).getPath(), handler);

        // start the server
        server.start();

        return server;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
		Application application;
		if (args!=null && args.length>0) {
			System.out.println("FIWARE IoTBroker tester: "+args[0]+ " simulator");
			if (args[0].equals(DATA_PROVIDER)) {
				application = new JaxRsDataProviderApplication();
				try {
					int providersQty = Integer.parseInt(args[1]);
					for (int i=0;i<providersQty;i++) {
						startServer(8071+i, application);
					}
				} catch (Exception e){
					System.out.println("Please specify the required0 number of data-providers (ports to be open) in the arguments");
				}
			} else if (args[0].equals(DATA_CONSUMER)) {
				application = new JaxRsDataConsumerApplication();
				startServer(8070, application);				
			} else {
				System.out.println("cannot start server with type="+args[0]);
				return;
			}			
			
			System.out.println("Application started.\n"
					/*+ "Try accessing " + getBaseURI() + " in the browser.\n"*/
					+ "Hit enter to stop the application...");

			Thread.currentThread().join();
		
		} else {
			System.out.println("Please specify data-consumer or data-provider and qty");
		}

    }

    /**
     * Gets base {@link URI}.
     *
     * @return base {@link URI}.
     */
    public static URI getBaseURI(int port) {
		return UriBuilder.fromUri("http://localhost/").port(port).build();
    }
}
