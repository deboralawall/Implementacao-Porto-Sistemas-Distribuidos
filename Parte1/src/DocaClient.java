import java.rmi.registry.*;

public class DocaClient {
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            // Obtém uma referência para o registro do RMI
            Registry registry = LocateRegistry.getRegistry(host,6600);

            // Obtém a stub do servidor
            IServico stub= (IServico) registry.lookup("Hello");

            // Chama o método do servidor e imprime a mensagem
            String msg = stub.hello();
            System.out.println("Mensagem do Servidor: " + msg);

            // Teste: cadastrar navio
            int navioId = stub.cadastrar_navio("Navio Teste", 100);
            System.out.println("Navio cadastrado com ID: " + navioId);

            // Cadastrar cargas
            int carga1Id = stub.cadastrar_carga("Carga A", 30);
            System.out.println("Carga A cadastrada com ID: " + carga1Id);
            int carga2Id = stub.cadastrar_carga("Carga B", 50);
            System.out.println("Carga B cadastrada com ID: " + carga2Id);
            int carga3Id = stub.cadastrar_carga("Carga C", 20);
            System.out.println("Carga C cadastrada com ID: " + carga3Id);

            // Relatório de cargas disponíveis
            System.out.println("Cargas disponíveis:");
            System.out.println(stub.relatorio_carga());

            // Embarcar no navio
            double totalEmbarcado = stub.embarcar("Navio Teste");
            System.out.println("Total embarcado: " + totalEmbarcado);

            // Relatório de embarque
            System.out.println("Cargas embarcadas:");
            System.out.println(stub.relatorio_embarque());

            // Cargas restantes
            System.out.println("Cargas restantes:");
            System.out.println(stub.relatorio_carga());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}