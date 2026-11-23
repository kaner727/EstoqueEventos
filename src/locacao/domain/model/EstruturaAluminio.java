package locacao.domain.model;

public class EstruturaAluminio extends ItemEstrutura {
    private static final long serialVersionUID = 1L;
    private final double tamanhoM;
    private final String tipoTruss;

    public EstruturaAluminio(String descricao, int quantidadeTotal, double valorDiaria, double taxaMontagem, double tamanhoM, String tipoTruss) {
        super(descricao, quantidadeTotal, valorDiaria, taxaMontagem);
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

    @Override
    public String toString() {
        return super.toString() + String.format(" | Comp.: %.2f m | Tipo: %s", this.tamanhoM, this.tipoTruss);
    }
}

