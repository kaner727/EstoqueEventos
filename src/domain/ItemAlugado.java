package domain;

public class ItemAlugado {
    private ItemEstrutura item;
    private int quantidade;

    public ItemAlugado(ItemEstrutura item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }

    public double calcularSubTotal(long dias) {
        return item.calcularValorLocacao(dias, this.quantidade);
    }

    public void diminuirQuantidade(int qtd) {
        this.quantidade -= qtd;
        if(this.quantidade < 0) { this.quantidade = 0; }
    }

    public ItemEstrutura getItem() { return this.item; }
    public int getQuantidade() { return this.quantidade; }
}
