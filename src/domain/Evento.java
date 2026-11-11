package domain;

import java.util.ArrayList;

public class Evento {
        private int id;
        private String nomeCliente;
        private long dataInicio;
        private long dataFim;
        private ArrayList<ItemEstoque> itensAlugados;

        Evento(int id, String nomeCliente, long dataInicio, long dataFim) {
                this.id = id;
                this.nomeCliente = nomeCliente;
                this.dataInicio = dataInicio;
                this.dataFim = dataFim;
        }

        public boolean alugarItem(ItemEstoque item, int qtd) {
                if(item.debitarEstoque(qtd)) {
                        for(int i = 0; i < qtd; i++) this.itensAlugados.add(item);
                        return false;
                }
                return true; // sem estoque
        }

        public void recuperarItem(ItemEstoque item, int qtd) {
                int qtd_removidos = 0;
                for(int i = 0; i < qtd; i++) {
                        // se o item foi removido, adiciona ao contador para ser creditado depois
                        if(this.itensAlugados.remove(item)) qtd_removidos++;
                }
                item.creditarEstoque(qtd_removidos);
        }

        public double calcularCustoTotal() {
                double total = 0.0;
                for(ItemEstoque item : itensAlugados) {
                        // total += item.calcularValorLocacao((this.dataFim - this.dataInicio), );
                }
                return total;
        }
}
