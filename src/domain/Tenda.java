package domain;

public class Tenda extends ItemEstrutura {
    private final double aream2;

    public Tenda(int id, String descricao, int quantidadeTotal, double valorDiaria, double taxaMontagem, double aream2) {
        super(id, descricao, quantidadeTotal, valorDiaria, taxaMontagem);
        this.aream2 = aream2;
    }

    @Override
    public double calcularValorLocacao(long dias, int quantidade) {
        double vlrAdicional = 0.0;
        if(this.aream2 > 50) vlrAdicional = 100.0;
        return (dias * quantidade * getValorDiaria()) + getTaxaMontagem() + vlrAdicional;
    }

    public double getAream2() { return this.aream2; }
}
