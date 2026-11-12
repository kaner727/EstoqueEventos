package domain;

import java.util.ArrayList;
import java.util.List;

public class Evento {
        private final int id;
        private String nomeCliente;
        private long dataInicio;
        private long dataFim;
        private List<ItemAlugado> itensAlugados = new ArrayList<>();

        Evento(int id, String nomeCliente, long dataInicio, long dataFim) {
                this.id = id;
                this.nomeCliente = nomeCliente;
                this.dataInicio = dataInicio;
                this.dataFim = dataFim;
        }

        public boolean alugarItem(ItemEstrutura item, int qtd) {
                if(item.debitarEstoque(qtd)) {
                        ItemAlugado novoAlugado = new ItemAlugado(item, qtd);
                        this.itensAlugados.add(novoAlugado);
                        return true;
                }
                return false; // sem estoque
        }

        public boolean recuperarItem(ItemEstrutura item, int qtd) {
                ItemAlugado itemDevolver = null;

                for(ItemAlugado itemA : this.itensAlugados) {
                        if(itemA.getItem().getId() == item.getId()) {
                                itemDevolver = itemA;
                        }
                }

                if(itemDevolver != null) {
                        int qtdReal = Math.min(qtd, itemDevolver.getQuantidade());

                        // adiciona ao estoque, remove a quantidade do item que foi alugado (itemAlugado)
                        item.creditarEstoque(qtdReal);
                        itemDevolver.diminuirQuantidade(qtdReal);

                        // se nao tem mais itens alugados, remove da lista
                        if(itemDevolver.getQuantidade() == 0) {
                                itensAlugados.remove(itemDevolver);
                        }

                        return true; // sucesso
                }
                return false; // esse item nao estava alugado nesse evento
        }

        public double calcularCustoTotal() {
                double total = 0.0;
                for(ItemAlugado item : itensAlugados) {
                        total += item.calcularSubTotal((this.dataFim - this.dataInicio));
                }
                return total;
        }

        public double duracaoDoEvento() {
                return getDataFim() - getDataInicio();
        }

        // getters e setters
        public int getId() { return this.id; }
        public String getNomeCliente() { return this.nomeCliente; }
        public long getDataInicio() { return this.dataInicio; }
        public long getDataFim() { return this.dataFim; }
}