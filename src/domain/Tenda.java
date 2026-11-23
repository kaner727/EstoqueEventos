package domain;

public class Tenda extends ItemEstrutura {
    private static final long serialVersionUID = 1L;
    private final double aream2;

    public Tenda(String descricao, int quantidadeTotal, double valorDiaria, double taxaMontagem, double aream2) {
        super(descricao, quantidadeTotal, valorDiaria, taxaMontagem);
        this.aream2 = aream2;
    }

    @Override
    public double calcularValorLocacao(long dias, int quantidade) {
        double vlrAdicional = 0.0;
        if(this.aream2 > 50) vlrAdicional = 100.0;
        return (dias * quantidade * getValorDiaria()) + getTaxaMontagem() + vlrAdicional;
    }

    public double getAream2() { return this.aream2; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Área: %.1f m²", this.aream2);
    }
}
