import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ProcessoEscravo {

    private static final int TAMANHO_BUFFER = 1024;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            if(args.length<1){
                System.out.println("ProcessoEscravo <porta> <>");
                return;
            }
            int portaEscravo = Integer.parseInt(args[0]);
            long tempoInicial = System.currentTimeMillis();

            while (true) {
                // Receber solicitação de sincronização do mestre
                long tempoMestre = receberSolicitacaoDoMestre(socket);

                // Calcular o tempo do escravo
                long tempoEscravo = tempoInicial + (System.currentTimeMillis() - tempoMestre);

                // Enviar resposta para o mestre
                enviarRespostaAoMestre(tempoEscravo, socket);

                System.out.println("Tempo sincronizado: " + tempoEscravo);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long receberSolicitacaoDoMestre(DatagramSocket socket) throws IOException {
        byte[] dadosSolicitacao = new byte[TAMANHO_BUFFER];
        DatagramPacket pacote = new DatagramPacket(dadosSolicitacao, dadosSolicitacao.length);
        socket.receive(pacote);

        return Long.parseLong(new String(pacote.getData(), 0, pacote.getLength()));
    }

    private static void enviarRespostaAoMestre(long tempoEscravo, DatagramSocket socket) throws IOException {
        String resposta = String.valueOf(tempoEscravo);
        byte[] dadosResposta = resposta.getBytes();
        DatagramPacket pacote = new DatagramPacket(dadosResposta, dadosResposta.length, socket.getInetAddress(), socket.getPort());
        socket.send(pacote);
    }
}
