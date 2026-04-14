import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;

public class PortoServer implements IServico {
    static class Navio {
        int id;
        String descricao;
        int capacidade;
        Navio(int id, String d, int c) { this.id = id; this.descricao = d; this.capacidade = c; }
    }

    static class Carga {
        int id;
        String descricao;
        int volume;
        Carga(int id, String d, int v) { this.id = id; this.descricao = d; this.volume = v; }
    }

    private List<Navio> navios = new ArrayList<>();
    private List<Carga> cargas = new ArrayList<>();
    private List<Carga> loaded = new ArrayList<>();
    private int nextNavioId = 1;
    private int nextCargaId = 1;

    public PortoServer() {}

    public static void main(String[] args) {
        try {
            // Instancia o objeto servidor e a sua stub
            PortoServer server = new PortoServer();
            IServico stub = (IServico) UnicastRemoteObject.exportObject(server, 0);
            // Registra a stub no RMI Registry para que ela seja obtAida pelos clientes
            Registry registry = LocateRegistry.createRegistry(6600);
            //Registry registry = LocateRegistry.getRegistry(9999);
            registry.bind("Hello", stub);
            System.out.println("Servidor pronto");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String hello() throws RemoteException {
        System.out.println("Executando hello()");
        return "Hello!!!";
    }

    @Override
    public double embarcar(String descricao) throws RemoteException {
        System.out.println("Executando embarcar()");
        Navio ship = null;
        for (Navio n : navios) {
            if (n.descricao.equals(descricao)) {
                ship = n;
                break;
            }
        }
        if (ship == null) {
            System.out.println("Navio não encontrado: " + descricao);
            return 0.0;
        }
        int n = cargas.size();
        int[] weights = new int[n];
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            weights[i] = cargas.get(i).volume;
            values[i] = cargas.get(i).volume;
        }
        int capacity = ship.capacidade;
        int[][] dp = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - weights[i - 1]] + values[i - 1]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        int maxValue = dp[n][capacity];
        List<Integer> selected = new ArrayList<>();
        int w = capacity;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(i - 1);
                w -= weights[i - 1];
            }
        }
        List<Carga> toLoad = new ArrayList<>();
        for (int idx : selected) {
            toLoad.add(cargas.get(idx));
        }
        loaded.addAll(toLoad);
        cargas.removeAll(toLoad);
        System.out.println("Peso total embarcado: " + maxValue);
        return (double) maxValue;
    }

    @Override
    public String relatorio_embarque() throws RemoteException {
        System.out.println("Executando relatorio_embarque()");
        StringBuilder sb = new StringBuilder();
        for (Carga c : loaded) {
            sb.append("ID: ").append(c.id).append(", Descricao: ").append(c.descricao).append(", Volume: ").append(c.volume).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Integer cadastrar_navio(String descricao, Integer capacidade) throws RemoteException {
        System.out.println("Executando cadastrar_navio()");
        Navio n = new Navio(nextNavioId++, descricao, capacidade);
        navios.add(n);
        return n.id;
    }

    @Override
    public void remover_navio(Integer id) throws RemoteException {
        System.out.println("Executando remover_navio()");
        navios.removeIf(n -> n.id == id);
    }

    @Override
    public String relatorio_navio() throws RemoteException {
        System.out.println("Executando relatorio_navio()");
        StringBuilder sb = new StringBuilder();
        for (Navio n : navios) {
            sb.append("ID: ").append(n.id).append(", Descricao: ").append(n.descricao).append(", Capacidade: ").append(n.capacidade).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Integer cadastrar_carga(String descricao, Integer volume) throws RemoteException {
        System.out.println("Executando cadastrar_carga()");
        Carga c = new Carga(nextCargaId++, descricao, volume);
        cargas.add(c);
        return c.id;
    }

    @Override
    public void remover_carga(Integer id) throws RemoteException {
        System.out.println("Executando remover_carga()");
        System.out.println("Removendo carga com ID: " + id );
        cargas.removeIf(c -> c.id == id);
    }

    @Override
    public String relatorio_carga() throws RemoteException {
        System.out.println("Executando relatorio_carga()");
        StringBuilder sb = new StringBuilder();
        for (Carga c : cargas) {
            sb.append("ID: ").append(c.id).append(", Descricao: ").append(c.descricao).append(", Volume: ").append(c.volume).append("\n");
        }
        return sb.toString();
    }
}