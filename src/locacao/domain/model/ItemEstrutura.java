package locacao.domain.model;

import locacao.domain.GeradorID;

import java.io.Serializable;

public abstract class ItemEstrutura implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String descricao;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private double valorDiaria;
    private double taxaMontagem;

    protected ItemEstrutura(String descricao, int quantidadeTotal,
                            double valorDiaria, double taxaMontagem) {
        if (quantidadeTotal < 0) {
            throw new IllegalArgumentException("A quantidade total não pode ser negativa.");
        }

        if (!Double.isFinite(valorDiaria) || valorDiaria < 0) {
            throw new IllegalArgumentException("Informe uma diária válida e não negativa.");
        }

        if (!Double.isFinite(taxaMontagem) || taxaMontagem < 0) {
            throw new IllegalArgumentException("Informe uma taxa de montagem válida e não negativa.");
        }

        validarDescricao(descricao);
        this.id = GeradorID.gerarIDItem();
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

    public boolean alterarQuantidadeTotal(int novoTotal) {
        int quantidadeAlugada = this.quantidadeTotal - this.quantidadeDisponivel;

        if (novoTotal < quantidadeAlugada) {
            return false;
        }

        this.quantidadeTotal = novoTotal;
        this.quantidadeDisponivel = novoTotal - quantidadeAlugada;
        return true;
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
    private static void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Informe a descrição do item.");
        }
    }

    public void setDescricao(String novaDesc) {
        validarDescricao(novaDesc);
        this.descricao = novaDesc;
    }
    public void setTaxaMontagem(double taxaMontagem) { this.taxaMontagem = taxaMontagem; }

    @Override
    public String toString() {
        return String.format("[ID: %d] %s | Diária: R$ %.2f | Montagem: R$ %.2f | Disp: %d/%d",
                this.id, this.descricao, this.valorDiaria, this.taxaMontagem,
                this.quantidadeDisponivel, this.quantidadeTotal);
    }
}