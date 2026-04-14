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


            /*--------------------------------------- CADASTRAR NAVIOS --------------------------------------------*/
            // navio
            int navioId = stub.cadastrar_navio("Navio Teste", 100);
            System.out.println("Navio cadastrado com ID: " + navioId);

            // navio 2
            int navioId2 = stub.cadastrar_navio("Navio Teste 2", 10);
            System.out.println("Navio cadastrado com ID: " + navioId2);
            /*------------------------------------------------------------------------------------------------------*/


            /*---------------------------------- CADASTRAR CARGAS --------------------------------------------------*/
            int carga1Id = stub.cadastrar_carga("Carga A", 80);
            System.out.println("Carga A cadastrada com ID: " + carga1Id);
            int carga2Id = stub.cadastrar_carga("Carga B", 10);
            System.out.println("Carga B cadastrada com ID: " + carga2Id);
            int carga3Id = stub.cadastrar_carga("Carga C", 20);
            System.out.println("Carga C cadastrada com ID: " + carga3Id);

            int carga4Id = stub.cadastrar_carga("Carga D", 5);
            System.out.println("Carga D cadastrada com ID: " + carga4Id);
            /*------------------------------------------------------------------------------------------------------*/

            /* --------------------------- RELATÓRIO DE NAVIOS DISPONÍVEIS -----------------------------------------*/
            System.out.println("Navios disponíveis:");
            System.out.println(stub.relatorio_navio());
            /*------------------------------------------------------------------------------------------------------*/

            /* --------------------------- RELATÓRIO DE CARGAS DISPONÍVEIS -----------------------------------------*/
            System.out.println("Cargas disponíveis:");
            System.out.println(stub.relatorio_carga());
            /*------------------------------------------------------------------------------------------------------*/

            /*----------------------------------- EMBARCAR NO NAVIO ------------------------------------------------*/
            double totalEmbarcado = stub.embarcar("Navio Teste");
            System.out.println("Total embarcado: " + totalEmbarcado);

            // Embarcar no navio
            double totalEmbarcado2 = stub.embarcar("Navio Teste 2");
            System.out.println("Total embarcado 2: " + totalEmbarcado2);
            /*------------------------------------------------------------------------------------------------------*/

            /*-------------------------------------- RELATÓRIO DE EMBARQUE -----------------------------------------*/
            System.out.println("Cargas embarcadas:");
            System.out.println(stub.relatorio_embarque());
            /*------------------------------------------------------------------------------------------------------*/

            /*--------------------------------------- CARGAS RESTANTES ---------------------------------------------*/
            System.out.println("Cargas restantes:");
            System.out.println(stub.relatorio_carga());
            /*------------------------------------------------------------------------------------------------------*/



            /*--------------------------------------- REMOVER CARGAS -----------------------------------------------*/
            //System.out.println("Cargas removidas:");
            System.out.println("Removendo carga com ID 4 ");
            stub.remover_carga(4);
            /*------------------------------------------------------------------------------------------------------*/

            /*--------------------------------------- REMOVER NAVIOS -----------------------------------------------*/
            //System.out.println("Navios removidos:");
            System.out.println("Removendo navio com ID 1 ");
            stub.remover_navio(1);
            /*------------------------------------------------------------------------------------------------------*/

            /* --------------------------- RELATÓRIO DE NAVIOS DISPONÍVEIS -----------------------------------------*/
            System.out.println("\nNavios:");
            System.out.println(stub.relatorio_navio());
            /*------------------------------------------------------------------------------------------------------*/

            /* --------------------------- RELATÓRIO DE CARGAS DISPONÍVEIS -----------------------------------------*/
            System.out.println("Cargas disponíveis:");
            System.out.println(stub.relatorio_carga());
            /*------------------------------------------------------------------------------------------------------*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}