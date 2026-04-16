package porto;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "porto.IServico", serviceName = "WSPorto")
public class PortoServerImpl implements IServico {
    static class Navio {
        int id;
        String descricao;
        int capacidade;

        Navio(int id, String descricao, int capacidade) {
            this.id = id;
            this.descricao = descricao;
            this.capacidade = capacidade;
        }
    }

    static class Carga {
        int id;
        String descricao;
        int volume;

        Carga(int id, String descricao, int volume) {
            this.id = id;
            this.descricao = descricao;
            this.volume = volume;
        }
    }

    private final List<Navio> navios = new ArrayList<>();
    private final List<Carga> cargas = new ArrayList<>();
    private final List<Carga> loaded = new ArrayList<>();
    private int nextNavioId = 1;
    private int nextCargaId = 1;

    @Override
    public String hello() {
        return "Hello from Porto SOAP service!";
    }

    @Override
    public double embarcar(String descricao) {
        Navio ship = null;
        for (Navio n : navios) {
            if (n.descricao.equals(descricao)) {
                ship = n;
                break;
            }
        }
        if (ship == null) {
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
        List<Carga> selected = new ArrayList<>();
        int w = capacity;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(cargas.get(i - 1));
                w -= weights[i - 1];
            }
        }

        loaded.addAll(selected);
        cargas.removeAll(selected);
        return maxValue;
    }

    @Override
    public String relatorio_embarque() {
        StringBuilder sb = new StringBuilder();
        for (Carga c : loaded) {
            sb.append("ID: ").append(c.id)
              .append(", Descricao: ").append(c.descricao)
              .append(", Volume: ").append(c.volume)
              .append("\n");
        }
        return sb.toString();
    }

    @Override
    public Integer cadastrar_navio(String descricao, Integer capacidade) {
        Navio n = new Navio(nextNavioId++, descricao, capacidade);
        navios.add(n);
        return n.id;
    }

    @Override
    public void remover_navio(Integer id) {
        navios.removeIf(n -> n.id == id);
    }

    @Override
    public String relatorio_navio() {
        StringBuilder sb = new StringBuilder();
        for (Navio n : navios) {
            sb.append("ID: ").append(n.id)
              .append(", Descricao: ").append(n.descricao)
              .append(", Capacidade: ").append(n.capacidade)
              .append("\n");
        }
        return sb.toString();
    }

    @Override
    public Integer cadastrar_carga(String descricao, Integer volume) {
        Carga c = new Carga(nextCargaId++, descricao, volume);
        cargas.add(c);
        return c.id;
    }

    @Override
    public void remover_carga(Integer id) {
        cargas.removeIf(c -> c.id == id);
    }

    @Override
    public String relatorio_carga() {
        StringBuilder sb = new StringBuilder();
        for (Carga c : cargas) {
            sb.append("ID: ").append(c.id)
              .append(", Descricao: ").append(c.descricao)
              .append(", Volume: ").append(c.volume)
              .append("\n");
        }
        return sb.toString();
    }
}
