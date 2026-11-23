package locacao.domain.model;

import locacao.domain.GeradorID;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Evento implements Serializable {
        private static final long serialVersionUID = 1L;
        private final int id;
        private String nomeCliente;
        private long dataInicio;
        private long dataFim;
        private List<ItemAlugado> itensAlugados = new ArrayList<>();

        public Evento(String nomeCliente, long dataInicio, long dataFim) {
                this.id = GeradorID.gerarIDEvento();
                this.nomeCliente = nomeCliente;
                this.dataInicio = dataInicio;
                this.dataFim = dataFim;
        }

        public boolean alugarItem(ItemEstrutura item, int qtd) {
                if (!item.debitarEstoque(qtd)) {
                        return false; // sem estoque
                }

                for (ItemAlugado existente : this.itensAlugados) {
                        if (existente.getItem().getId() == item.getId()) {
                                existente.aumentarQuantidade(qtd);
                                return true;
                        }
                }

                this.itensAlugados.add(new ItemAlugado(item, qtd));
                return true;
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

        public void devolverTodosOsItens() {
                for (ItemAlugado locacao : itensAlugados) {
                        locacao.getItem().creditarEstoque(locacao.getQuantidade());
                }
                itensAlugados.clear();
        }

        public double calcularCustoTotal() {
                long diasCobrados = Math.max(1L, this.dataFim - this.dataInicio);

                double total = 0.0;
                for (ItemAlugado item : itensAlugados) {
                        total += item.calcularSubTotal(diasCobrados);
                }
                return total;
        }

        public double duracaoDoEvento() {
                return getDataFim() - getDataInicio();
        }

        // getters e setters
        public void setNomeCliente(String nomeCliente) {
                if (nomeCliente != null && !nomeCliente.isBlank()) {
                        this.nomeCliente = nomeCliente;
                }
        }

        public void setDataInicio(long dataInicio) {
                this.dataInicio = dataInicio;
        }

        public void setDataFim(long dataFim) {
                if (dataFim >= this.dataInicio) {
                        this.dataFim = dataFim;
                }
        }

        public int getId() { return this.id; }
        public String getNomeCliente() { return this.nomeCliente; }
        public long getDataInicio() { return this.dataInicio; }
        public long getDataFim() { return this.dataFim; }

        @Override
        public String toString() {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String inicioStr = LocalDate.ofEpochDay(this.dataInicio).format(fmt);
                String fimStr = LocalDate.ofEpochDay(this.dataFim).format(fmt);

                StringBuilder sb = new StringBuilder();
                sb.append(String.format("=== Evento [ID: %d]: %s | %s até %s (%.0f dias) ===\n",
                        this.id, this.nomeCliente, inicioStr, fimStr, duracaoDoEvento()));

                if (this.itensAlugados.isEmpty()) {
                        sb.append("   (Nenhum item alugado)\n");
                } else {
                        for (ItemAlugado alocado : this.itensAlugados) {
                                sb.append("   - ").append(alocado.toString()).append("\n");
                        }
                }
                sb.append(String.format("   Custo Total: R$ %.2f", calcularCustoTotal()));
                return sb.toString();
        }
}