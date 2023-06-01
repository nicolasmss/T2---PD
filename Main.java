
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int NUM_PROCESSOS = 5;

    public static void main(String[] args) {
        List<Processo> processos = criarProcessos();
        Mestre mestre = new Mestre(processos);
        for (Processo processo : processos) {
            processo.iniciar(mestre);
        }
    }

    private static List<Processo> criarProcessos() {
        List<Processo> processos = new ArrayList<>();

        // Criar os processos com os parâmetros fornecidos
        for (int i = 0; i < NUM_PROCESSOS; i++) {
            int id = i + 1;
            String host = "localhost";
            int porta = 8080 + i;
            Instant time = Instant.now();
            long ptime = 1000; // 1 segundo de processamento
            long delay = i * 100; // Atraso crescente para cada processo
            Processo processo = new Processo(id, host, porta, time, ptime, delay);
            processos.add(processo);
        }

        return processos;
    }
}
