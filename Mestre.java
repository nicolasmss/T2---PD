
import java.util.List;
import java.util.Random;

class Mestre {

    private final List<Processo> processos;

    public Mestre(List<Processo> processos) {
        this.processos = processos;
    }

    public long receberHoraAtual(int id, long horaEnvio) {
        // Simulando atraso na comunicação da rede
        try {
            Thread.sleep(processos.get(id - 1).getDelay());
        } catch (InterruptedException e) {
        }

        // Simulando o cálculo da hora de recebimento no mestre
        Random random = new Random();
        long atraso = random.nextInt(100) + 1; // Atraso aleatório entre 1 e 100ms
        long horaRecebimento = horaEnvio + atraso;
        System.out.println("Mestre: Processo " + id + " enviou a hora atual = " + horaEnvio + ", hora de recebimento = " + horaRecebimento);
        return horaRecebimento;
    }

    public long receberHoraAjustada(int processoId, long horaRecebimento) {
        // Simulando o cálculo da hora ajustada no mestre
        Random random = new Random();
        long fatorCorrecao = random.nextInt(100) + 1; // Fator de correção aleatório entre 1 e 100ms
        long horaAjustada = horaRecebimento + fatorCorrecao;
        System.out.println("Mestre: Processo " + processoId + " enviou a hora de recebimento = " + horaRecebimento + ", hora ajustada = " + horaAjustada);
        return horaAjustada;
    }
}
