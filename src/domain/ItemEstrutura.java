package domain;

public abstract class ItemEstrutura {
    private final int id;
    private String descricao;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private double valorDiaria;
    private double taxaMontagem;

    protected ItemEstrutura(int id, String descricao, int quantidadeTotal, double valorDiaria, double taxaMontagem) {
        this.id = id;
        this.descricao = descricao;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
        this.valorDiaria = valorDiaria;
        this.taxaMontagem = taxaMontagem;
    }

    // Metodos base
    public abstract double calcularValorLocacao(long dias, int quantidade);

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

    public void aumentarQtdTotal(int qtd) {
        this.quantidadeTotal += qtd;
        this.quantidadeDisponivel += qtd;
    }

    public void diminuirQtdTotal(int qtd) {
        this.quantidadeTotal = Math.max(this.quantidadeTotal - qtd, 0);
        if(this.quantidadeTotal < this.quantidadeDisponivel) quantidadeDisponivel = this.quantidadeTotal;
    }

    // Getters e Setters
    public int    getId() { return this.id; }
    public String getDescricao() { return this.descricao; }
    public int    getQuantidadeTotal() { return this.quantidadeTotal; }
    public int    getQuantidadeDisponivel() { return this.quantidadeDisponivel; }
    public double getValorDiaria() { return this.valorDiaria; }
    public double getTaxaMontagem() { return this.taxaMontagem; }

    public void setValorDiaria(double vlr) {
        if(vlr >= 0.0) {
            this.valorDiaria = vlr;
        } else {
            throw new IllegalArgumentException("O valor da diária não pode ser negativo.");
        }
    }
    public void setDescricao(String novaDesc) { this.descricao = novaDesc; }
    public void setTaxaMontagem(double taxaMontagem) { this.taxaMontagem = taxaMontagem; }
}