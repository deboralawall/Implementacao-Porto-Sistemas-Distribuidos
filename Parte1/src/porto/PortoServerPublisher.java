package porto;

import javax.xml.ws.Endpoint;

public class PortoServerPublisher {

    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "127.0.0.1";
        String url = "http://" + host + ":9876/hw";
        System.out.println("Beginning to publish Porto SOAP service now at " + url);
        Endpoint.publish(url, new PortoServerImpl());
        System.out.println("Done publishing");
    }
}
