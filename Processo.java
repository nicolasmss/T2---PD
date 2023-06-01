import java.time.Instant;

class Processo {
    private int id;
    private String host;
    private int porta;
    private Instant time;
    private long ptime;
    private long delay;
    private long tempoAtual;

    public Processo(int id, String host, int porta, Instant time, long ptime, long delay) {
        this.id = id;
        this.host = host;
        this.porta = porta;
        this.time = time;
        this.ptime = ptime;
        this.delay = delay;
        this.tempoAtual = 0;
    }

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

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public long getPtime() {
        return ptime;
    }

    public void setPtime(long ptime) {
        this.ptime = ptime;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getTempoAtual() {
        return tempoAtual;
    }

    public void setTempoAtual(long tempoAtual) {
        this.tempoAtual = tempoAtual;
    }

    public void iniciar(Mestre mestre) {
        System.out.println("Processo " + id + " iniciado às " + time);

        // Processamento local
        try {
            Thread.sleep(ptime);
        } catch (InterruptedException e) {
        }

        // Sincronização com o Mestre
        sincronizarComMestre(mestre);
    }

    private void sincronizarComMestre(Mestre mestre) {
        // Simulando o envio da hora atual para o Mestre
        System.out.println("Processo " + id + ": Enviando hora atual para o mestre");
        long horaEnvio = System.currentTimeMillis();
        long horaRecebimento = mestre.receberHoraAtual(id, horaEnvio);
        long rtt = System.currentTimeMillis() - horaEnvio;
        System.out.println("Processo " + id + ": RTT = " + rtt + "ms");

        // Simulando o recebimento da hora ajustada do Mestre
        System.out.println("Processo " + id + ": Recebendo hora ajustada do mestre");
        long horaAjustada = mestre.receberHoraAjustada(id, horaRecebimento);
        long fatorCorrecao = horaAjustada - horaRecebimento;
        System.out.println("Processo " + id + ": Fator de correção = " + fatorCorrecao + "ms");

        // Atualizando o tempo atual do processo com a hora ajustada
        tempoAtual = time.toEpochMilli() + fatorCorrecao;

        // Realizando ações com o tempo atualizado
        System.out.println("Processo " + id + ": Tempo atualizado = " + Instant.ofEpochMilli(tempoAtual));

        // Finalizando o processo
        System.out.println("Processo " + id + " finalizado");
    }
}