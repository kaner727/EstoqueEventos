package locacao.domain.model;

public class EquipamentoSom extends ItemEstrutura {
    private static final long serialVersionUID = 1L;
    private final int potenciaW;

    public EquipamentoSom(String descricao, int quantidadeTotal, double valorDiaria, double taxaMontagem, int potenciaW) {
        super(descricao, quantidadeTotal, valorDiaria, taxaMontagem);
        if (potenciaW <= 0) {
            throw new IllegalArgumentException("Informe uma potência inteira maior que zero.");
        }
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
        return (diariaDescontada * dias * quantidade) + (getPotenciaW() / 30.0) + getTaxaMontagem();
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Potência: %d W", this.potenciaW);
    }
}
