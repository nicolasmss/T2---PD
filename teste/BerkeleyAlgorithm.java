import java.util.ArrayList;
import java.util.List;

class Process {
    private int id;
    private String host;
    private int port;
    private long time;
    private long ptime;
    private long adelay;

    // Construtor
    public Process(int id, String host, int port, long time, long ptime, long adelay) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.time = time;
        this.ptime = ptime;
        this.adelay = adelay;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getPtime() {
        return ptime;
    }

    public void setPtime(long ptime) {
        this.ptime = ptime;
    }

    public long getAdelay() {
        return adelay;
    }

    public void setAdelay(long adelay) {
        this.adelay = adelay;
    }

    // Método para atualizar o tempo do processo
    public void updateClock(long correctedTime) {
        this.time = correctedTime;
    }

    // Método para simular o processamento do processo
    public void process() {
        // Simula o processamento do processo
        try {
            Thread.sleep(ptime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para sincronizar o tempo com o mestre
    public void synchronizeTime(Process master) {
        // Simula o envio da requisição de sincronização
        // e o recebimento da resposta do mestre
        try {
            Thread.sleep(adelay);

            // Calcula o tempo corrigido usando a média dos tempos
            long correctedTime = (this.time + master.getTime()) / 2;

            // Atualiza o tempo do processo com o tempo corrigido
            updateClock(correctedTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para imprimir a saída do processo
    public void printOutput() {
        System.out.println("Process ID: " + id);
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
        System.out.println("Time: " + time);
        System.out.println("Processing Time: " + ptime);
        System.out.println("Aditional Delay: " + adelay);
        System.out.println();
    }

}

public class BerkeleyAlgorithm {
    public static void main(String[] args) {
        // Criação dos processos
        Process master = new Process(1, "localhost", 8080, System.currentTimeMillis(), 2000, 0);
        Process slave1 = new Process(2, "localhost", 8081, System.currentTimeMillis(), 1500, 0);
        Process slave2 = new Process(3, "localhost", 8082, System.currentTimeMillis(), 1800, 0);

        // Lista de processos
        List<Process> processes = new ArrayList<>();
        processes.add(master);
        processes.add(slave1);
        processes.add(slave2);

        // Simulação da sincronização dos processos
        for (Process process : processes) {
            process.synchronizeTime(master);
        }

        // Simulação do processamento dos processos
        for (Process process : processes) {
            process.process();
        }

        // Imprime a saída de cada processo
        for (Process process : processes) {
            process.printOutput();
        }
    }
}
