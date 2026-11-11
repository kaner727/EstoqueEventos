package domain;

public class EquipamentoSom extends ItemEstrutura {
    private int potenciaW;

    public EquipamentoSom(int id, String descricao, int quantidadeTotal, double valorDiaria, int potenciaW) {
        super(id, descricao, quantidadeTotal, valorDiaria);
        this.potenciaW = potenciaW;
    }

    public int getPotenciaW() { return this.potenciaW; }

    @Override
    public double calcularValorLocacao(long dias, int quantidade) {
        double diariaDescontada = this.getValorDiaria();

        // aplica descontos baseados na quantidade
        if(quantidade > 10) {
            diariaDescontada *= 0.95; // 5%
        } else if (quantidade > 5) {
            diariaDescontada *= 0.98; // 2%
        }

        // adiciona uma pequena taxa pela potencia do tipo alugado (so uma funcionalidade para deixar unico, nao faz muito sentido de negocio)
        return (diariaDescontada * dias * quantidade) + (getPotenciaW() / 30.0);
    }
}
