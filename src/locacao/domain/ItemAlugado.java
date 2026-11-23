package locacao.domain;

import java.io.Serializable;

public class ItemAlugado implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ItemEstrutura item;
    private int quantidade;

    public ItemAlugado(ItemEstrutura item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }

    public double calcularSubTotal(long dias) {
        return item.calcularValorLocacao(dias, this.quantidade);
    }

    public void aumentarQuantidade(int qtd) {
        if (qtd > 0) {
            this.quantidade += qtd;
        }
    }

    public void diminuirQuantidade(int qtd) {
        this.quantidade -= qtd;
        if(this.quantidade < 0) { this.quantidade = 0; }
    }

    public ItemEstrutura getItem() { return this.item; }
    public int getQuantidade() { return this.quantidade; }

    @Override
    public String toString() {
        return String.format("%s (Qtd: %d)", this.item.getDescricao(), this.quantidade);
    }
}
