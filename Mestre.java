import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Mestre {

    static String id;
    static int portaMestre = 8100;
    static List<String> AdrSlaves = List.of(
            "localhost",
            "localhost");
    static List<Integer> ports = List.of(
            8001,
            8002);
    static long time = 0;
    static long timeEnvio = 0;
    static long ptime = 0;
    static long delayTime = 0;
    static long adelay = 0;// diferente para cada slave
    static List<Slave> slaves = new ArrayList<>();
    static long desvio = 2000; // tolerancia da mediana

    static Semaphore sem = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        time = time - (System.currentTimeMillis() + time);

        for (int i = 0; i < AdrSlaves.size(); i++) {
            slaves.add(new Slave(i, AdrSlaves.get(i), ports.get(i)));
        }

        try {
            while (true) {
                timeEnvio = (System.currentTimeMillis() + time);

                for (int i = 0; i < AdrSlaves.size(); i++) {
                    slaves.get(i).tempoProcesso = (System.currentTimeMillis() + time);
                }

                for (int i = 0; i < AdrSlaves.size(); i++) {
                    DatagramSocket socket = new DatagramSocket(portaMestre);
                    InetAddress endereco = InetAddress.getByName(AdrSlaves.get(i));

                    String mensagem = "ID:" + i;
                    byte[] sendData = mensagem.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, endereco, ports.get(i));

                    slaves.get(i).tempoProcesso = (System.currentTimeMillis() + time) - slaves.get(i).tempoProcesso;
                    delayTime = (System.currentTimeMillis() + time);
                    socket.send(sendPacket);

                    System.out.println(
                            "Mestre enviou: " + mensagem + " \t\ttempo agora: " + (System.currentTimeMillis() + time));

                    // Aguarda a resposta
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                    socket.receive(receivePacket);
                    slaves.get(i).tempoAcesso = (System.currentTimeMillis() + time);
                    delayTime = (System.currentTimeMillis() + time) - delayTime;

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Mestre recebeu do escravo " + i + ": " + response + " \t\ttempo agora: "
                            + (System.currentTimeMillis() + time));
                    response = response.trim();
                    String[] resp = response.split(":"); // tempo:ptempo
                    delayTime -= Long.parseLong(resp[1]);
                    slaves.get(i).delay = delayTime;
                    slaves.get(i).tempoRecebido = Long.parseLong(resp[0]);
                    slaves.get(i).atualizaTempoOriginal(Long.parseLong(resp[1])); // ptime

                    socket.close();
                    System.out.println();
                }

                long somaTempos = 0;
                List<Long> organiza = new ArrayList<>();

                organiza.add(timeEnvio);

                System.out.println("Tempo do mestre: " + timeEnvio + " ms");
                for (int i = 0; i < AdrSlaves.size(); i++) {
                    // somaTempos += slaves.get(i).tempoOriginal;
                    organiza.add(slaves.get(i).tempoOriginal);
                    System.out.println("Tempo de escravo " + i + ": " + slaves.get(i).tempoOriginal + " ms"); // sout
                }
                Collections.sort(organiza);

                long numMedia = 0;

                for (int i = 0; i < AdrSlaves.size() + 1; i++) {
                    if (organiza.get(i) < (organiza.get(organiza.size() / 2) - desvio)
                            || organiza.get(i) > (organiza.get(organiza.size() / 2) + desvio)) {
                        continue;
                    }
                    numMedia++;
                    somaTempos += organiza.get(i);
                }

                // calculo da média
                long media = (somaTempos / numMedia);
                System.out.println("media: " + media + " numMedia: " + numMedia + " somaTempos: " + somaTempos
                        + " time atual: " + (System.currentTimeMillis() + time) + " timeEnvio: " + timeEnvio
                        + " somatempos: "
                        + somaTempos);// sout
                long novoTime = media + ((System.currentTimeMillis() + time) - timeEnvio);

                System.out.println("\ncalculo novo tempo = media: " + media
                        + " + (tempo atual (pode estar diferente pois trabalha com tempo real): "
                        + (System.currentTimeMillis() + time) + " - tempo de envio: " + timeEnvio + ")");
                System.out.println("novotime: " + novoTime);

                time = novoTime - (System.currentTimeMillis());

                System.out.println("Novo tempo: " + media + " Tempo atualizado: " + novoTime);

                for (int i = 0; i < AdrSlaves.size(); i++) {
                    DatagramSocket socket = new DatagramSocket(portaMestre);
                    InetAddress endereco = InetAddress.getByName(AdrSlaves.get(i));

                    long pntime = (System.currentTimeMillis() + time) - novoTime; // tempo de processo do master
                    String mensagem = novoTime + ":" + pntime + ":" + slaves.get(i).delay; // time:ptimeMaster:delayMasterToSlave(i)
                    byte[] sendData = mensagem.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, endereco, ports.get(i));

                    socket.send(sendPacket);

                    System.out.println("Mestre enviou para " + i + ": " + mensagem + " \t\ttempo agora: "
                            + (System.currentTimeMillis() + time));
                    socket.close();
                }

                System.out.println(
                        "\n-----------------------------------------------------------------------------------\n");

                Thread.sleep(10000);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}