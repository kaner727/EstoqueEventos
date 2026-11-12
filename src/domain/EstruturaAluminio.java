package domain;

public class EstruturaAluminio extends ItemEstrutura {
    private final double tamanhoM;
    private final String tipoTruss;

    public EstruturaAluminio(int id, String descricao, int quantidadeTotal, double valorDiaria, double taxaMontagem, double tamanhoM, String tipoTruss) {
        super(id, descricao, quantidadeTotal, valorDiaria, taxaMontagem);
        this.tamanhoM = tamanhoM;
        this.tipoTruss = tipoTruss;
    }

    @Override
    public double calcularValorLocacao(long dias, int quantidade) {
        double vlrUnitario = (getValorDiaria() * dias) + getTaxaMontagem();

        double vlrTotal = vlrUnitario * quantidade;

        if(quantidade > 10) {
            vlrTotal *= 0.95;
        }

        return vlrTotal;
    }

    public double getTamanhoM() { return this.tamanhoM; }
    public String getTipoTruss() { return this.tipoTruss; }
}

