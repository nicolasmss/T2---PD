import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Semaphore;

public class Escravo {
    static String id;
    static String host = "localhost";
    static int port = 8001;
    static int portMaster = 8100;
    static long time = 0;
    static long ptime = 0;
    static long adelay = 100;// diferente para cada slave (ou não)

    static Semaphore sem = new Semaphore(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        port = Integer.parseInt(args[0]);
        
        time = time - System.currentTimeMillis();

        while (true) {
            System.out.println("Escravo pronto para receber mensagem");

            DatagramSocket socket = new DatagramSocket(port);
            InetAddress endereco = InetAddress.getByName(host);

            // Aguarda a resposta
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            socket.receive(receivePacket);
            Thread.sleep(adelay); // adelay
            ptime = (System.currentTimeMillis()+time);
            //Thread.sleep(100);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            response = response.trim();
            String[] resp = response.split(":"); // qlqrcoisa:id
            id = resp[1];
            System.out.println("Escravo " + id + " recebeu do mestre requisição de tempo. \t\tTempo agora: " + (System.currentTimeMillis()+time));

            // envio
            ptime = (System.currentTimeMillis()+time) - ptime;
            String mensagem = (System.currentTimeMillis()+time) + ":" + ptime;
            byte[] sendData = mensagem.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, endereco, portMaster);

            Thread.sleep(adelay); // adelay
            socket.send(sendPacket);

            System.out.println("Mestre enviou: " + mensagem + " \t\tTempo agora: " + (System.currentTimeMillis()+time));

            // receber novo tempo
            receiveData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);

            socket.receive(receivePacket);

            Thread.sleep(adelay); // adelay

            response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            response = response.trim();
            resp = response.split(":"); // time : ptimeMaster : delayMasterToSlave(i)

            System.out.println("---resposta: " + response + "\t\tTempo agora: " + (System.currentTimeMillis()+time));
            long novoTime = Long.parseLong(resp[0]);
            long ptimeMaster = Long.parseLong(resp[1]);
            long delayMasterToSlave = Long.parseLong(resp[2]);

            sem.acquire();
            time = novoTime + ptimeMaster + (delayMasterToSlave / 2) - (System.currentTimeMillis());
            sem.release();

            System.out.println("Escravo " + id + " recebeu do mestre requisição de tempo. \t\tTempo agora: " + (System.currentTimeMillis()+time));
            System.out.println("\n-----------------------------------------------------------------------------------\n");
            socket.close();
        }

    }

}
