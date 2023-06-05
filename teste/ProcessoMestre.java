import java.io.IOException;
import java.net.*;

public class ProcessoMestre {

    private static final int PORTA_MESTRE = 8080;
    private static final int TAMANHO_BUFFER = 1024;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORTA_MESTRE)) {
            int numEscravos = Integer.parseInt(args[0]);
            long tempoInicial = System.currentTimeMillis();

            for (int i = 0; i < numEscravos; i++) {
                String hostEscravo = args[i + 1];
                int portaEscravo = Integer.parseInt(args[i + numEscravos + 1]);
                InetAddress enderecoEscravo = InetAddress.getByName(hostEscravo);

                // Enviar solicitação de sincronização para o escravo
                enviarSolicitacaoParaEscravo(tempoInicial, enderecoEscravo, portaEscravo);
                

                // Receber resposta do escravo
                long tempoEscravo = receberTempoDoEscravo(socket);

                System.out.println("Escravo " + (i + 1) + " - Tempo: " + tempoEscravo);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void enviarSolicitacaoParaEscravo(long tempoInicial, InetAddress enderecoEscravo, int portaEscravo) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] dadosSolicitacao = String.valueOf(tempoInicial).getBytes();
        DatagramPacket pacote = new DatagramPacket(dadosSolicitacao, dadosSolicitacao.length, enderecoEscravo, portaEscravo);
        socket.send(pacote);
        socket.close();
    }

    private static long receberTempoDoEscravo(DatagramSocket socket) throws IOException {
        byte[] dadosResposta = new byte[TAMANHO_BUFFER];
        DatagramPacket pacote = new DatagramPacket(dadosResposta, dadosResposta.length);
        socket.receive(pacote);

        return Long.parseLong(new String(pacote.getData(), 0, pacote.getLength()));
    }
}
