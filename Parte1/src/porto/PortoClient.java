package porto;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class PortoClient {

    public static void main(String[] args) {
        try {
            String host = (args.length > 0) ? args[0] : "127.0.0.1";
            URL url = new URL("http://" + host + ":9876/hw?wsdl");
            QName qname = new QName("http://porto/", "WSPorto");

            Service service = Service.create(url, qname);
            IServico server = service.getPort(IServico.class);

            System.out.println(server.hello());
            int navioId = server.cadastrar_navio("Navio SOAP", 100);
            System.out.println("Navio cadastrado com ID: " + navioId);
            int cargaId = server.cadastrar_carga("Carga SOAP", 50);
            System.out.println("Carga cadastrada com ID: " + cargaId);
            System.out.println("Navios disponíveis:\n" + server.relatorio_navio());
            System.out.println("Cargas disponíveis:\n" + server.relatorio_carga());
            double total = server.embarcar("Navio SOAP");
            System.out.println("Total embarcado: " + total);
            System.out.println("Relatório de embarque:\n" + server.relatorio_embarque());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
