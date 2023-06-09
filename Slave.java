public class Slave {
    int id;
    String endereco;
    int port;

    long tempoRecebido;//tempo que recebemos do escravo
    long tempoOriginal;//tempo desejado com base no tempo que sera feito a media
    // tempoOriginal = tempoRecebido - ptime - delay/2 - tempoProcesso
    long delay;// rtt
    long tempoAcesso;// tempo do master quando recebeu o pacote
    long tempoProcesso; // tempo que o master demora 
    // real = tempoRecebido + delay/2 + (timeMASTER-tempoAcesso)

    public Slave(int id, String endereco, int port){
        this.id = id;
        this.endereco = endereco;
        this.port = port;
    }

    public void atualizaTempoOriginal(long ptime){
        System.out.println("tempoRecebido: "+ tempoRecebido + " ptime: " + ptime + " (delay/2): " + (delay/2) + " tempoProcesso: " + tempoProcesso);
        tempoOriginal = tempoRecebido - ptime - (delay/2) - tempoProcesso;
    }
}
