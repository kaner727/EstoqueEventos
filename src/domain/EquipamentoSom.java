package domain;

public class EquipamentoSom extends ItemEstrutura {
    private int potenciaW;

    public EquipamentoSom(int id, String descricao, int quantidadeTotal, double valorDiaria, int potenciaW) {
        super(id, descricao, quantidadeTotal, valorDiaria);
        this.potenciaW = potenciaW;
    }

    public int getPotenciaW() { return this.potenciaW; }

    @Override
    public double calcularValorLocacao(int dias, int quantidade) {

    }
}
