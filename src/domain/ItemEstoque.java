package domain;

public abstract class ItemEstoque {
    private int id;
    private String descricao;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private double valorDiaria;

    public abstract double calcularValorLocacao(int dias, int quantidade);

    public boolean debitarEstoque(int qtd) {
        if(qtd <= 0 || qtd > this.quantidadeDisponivel) {
            return false; // nao tem estoque o suficiente
        }
        this.quantidadeDisponivel -= qtd;
        return true;
    }

    public void creditarEstoque(int qtd) {
        this.quantidadeDisponivel += qtd;
        if(this.quantidadeDisponivel >= this.quantidadeTotal) {
            // teoricamente nao era pra chegar aqui, mas caso for creditado mais itens que o total, coloca o valor maximo sendo a quantidade total previamente definida
            this.quantidadeDisponivel = this.quantidadeTotal;
        }
    }
}
