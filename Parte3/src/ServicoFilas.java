import com.rabbitmq.client.*;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServicoFilas {
    private static String HOST_RABBIT = "localhost";
    private static IServico stubRMI;

    public static void main(String[] args) {
        String hostRMI = (args.length < 1) ? "localhost" : args[0];
        String rabbitHost = (args.length < 2) ? hostRMI : args[1];

        try {
            Registry registry = LocateRegistry.getRegistry(hostRMI, 6600);
            stubRMI = (IServico) registry.lookup("Hello");
            System.out.println("[+] Conexão RMI estabelecida com sucesso.");

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitHost);
            Connection connection = factory.newConnection();

            ExecutorService executor = Executors.newFixedThreadPool(3);

            executor.execute(new WorkerFila(connection, "fila_navio", "navio"));
            executor.execute(new WorkerFila(connection, "fila_carga", "carga"));
            executor.execute(new WorkerFila(connection, "fila_embarque", "embarque"));

            System.out.println("[*] Escutando filas no LabP2D. Aguardando mensagens...");

        } catch (Exception e) {
            System.err.println("[-] Erro na inicialização: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static class WorkerFila implements Runnable {
        private final Connection connection;
        private final String queueName;
        private final String tipoMensagem;

        public WorkerFila(Connection connection, String queueName, String tipoMensagem) {
            this.connection = connection;
            this.queueName = queueName;
            this.tipoMensagem = tipoMensagem;
        }

        @Override
        public void run() {
            try {
                final Channel channel = connection.createChannel();
                channel.queueDeclare(queueName, true, false, false, null);
                channel.basicQos(1); 

                System.out.println(" -> [Thread " + Thread.currentThread().getId() + "] Monitorando: " + queueName);

                // Classe anônima compatível com a versão 4.0.2 do slide do professor
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {
                        
                        String message = new String(body, "UTF-8");
                        System.out.println("\n[Fila: " + queueName + "] Mensagem recebida: " + message);

                        try {
                            processarEInvocarRMI(message);
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        } catch (Exception e) {
                            System.err.println("[-] Erro ao invocar RMI: " + e.getMessage());
                            channel.basicNack(envelope.getDeliveryTag(), false, true);
                        }
                    }
                };

                channel.basicConsume(queueName, false, consumer);

            } catch (IOException e) {
                System.err.println("[-] Erro no canal da fila " + queueName);
                e.printStackTrace();
            }
        }

        private void processarEInvocarRMI(String json) throws Exception {
            if ("navio".equals(tipoMensagem)) {
                String descricao = extrairCampoJson(json, "descricao");
                Integer capacidade = Integer.parseInt(extrairCampoJson(json, "capacidade"));
                int idRemoto = stubRMI.cadastrar_navio(descricao, capacidade);
                System.out.println(" >>> [RMI] Navio cadastrado ID: " + idRemoto);

            } else if ("carga".equals(tipoMensagem)) {
                String descricao = extrairCampoJson(json, "descricao");
                Integer volume = Integer.parseInt(extrairCampoJson(json, "volume"));
                int idRemoto = stubRMI.cadastrar_carga(descricao, volume);
                System.out.println(" >>> [RMI] Carga cadastrada ID: " + idRemoto);

            } else if ("embarque".equals(tipoMensagem)) {
                String descricao = extrairCampoJson(json, "descricao");
                double totalEmbarcado = stubRMI.embarcar(descricao);
                System.out.println(" >>> [RMI] Processo concluído. Total: " + totalEmbarcado);
            }
        }

        private String extrairCampoJson(String json, String campo) {
            String padrao = "\"" + campo + "\":\\s*\"?([^\",}]+)\"?";
            Pattern p = Pattern.compile(padrao);
            Matcher m = p.matcher(json);
            if (m.find()) {
                return m.group(1).trim();
            }
            return "";
        }
    }
}