package application;

import domain.Evento;
import domain.ItemEstrutura;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorEstoque {
    private List<ItemEstrutura> estoqueGlobal;
    private List<Evento> eventos;

    private final RepositorioDados repositorio;

    public GerenciadorEstoque(RepositorioDados repositorio) {
        this.repositorio = repositorio;
        this.estoqueGlobal = repositorio.carregarEstoque();
        this.eventos = repositorio.carregarEventos();

        // Garante listas válidas ANTES de percorrer no for
        if (this.estoqueGlobal == null) this.estoqueGlobal = new ArrayList<>();
        if (this.eventos == null) this.eventos = new ArrayList<>();

        int maxIdItem = 0;
        for (ItemEstrutura item : this.estoqueGlobal) {
            if (item.getId() > maxIdItem) {
                maxIdItem = item.getId();
            }
        }
        GeradorID.setUltimoIDItem(maxIdItem);

        int maxIdEvento = 0;
        for (Evento evento : this.eventos) {
            if (evento.getId() > maxIdEvento) {
                maxIdEvento = evento.getId();
            }
        }
        GeradorID.setUltimoIDEvento(maxIdEvento);
    }

    public void cadastrarNovoItem(ItemEstrutura item) {
        this.estoqueGlobal.add(item);
        this.repositorio.salvarEstoque(this.estoqueGlobal); // salva estado
    }

    public void cadastrarEvento(Evento evento) {
        this.eventos.add(evento);
        this.repositorio.salvarEventos(this.eventos);
    }

    public List<ItemEstrutura> listarEstoque() {
        return Collections.unmodifiableList(this.estoqueGlobal);
    }

    public List<Evento> listarEventos() {
        return Collections.unmodifiableList(this.eventos);
    }


    public boolean atualizarItem(int id, String novaDescricao, int novaQuantidadeTotal, double novoValorDiaria, double novaTaxaMontagem) {
        ItemEstrutura item = buscarItemPorId(id);
        if (item == null || novoValorDiaria < 0 || novaTaxaMontagem < 0) {
            return false;
        }

        // Impede reduzir o total para menos do que já está alugado em eventos
        int qtdAlugada = item.getQuantidadeTotal() - item.getQuantidadeDisponivel();
        if (novaQuantidadeTotal < qtdAlugada) {
            return false;
        }

        int diferenca = novaQuantidadeTotal - item.getQuantidadeTotal();
        if (diferenca > 0) {
            item.aumentarQtdTotal(diferenca);
        } else if (diferenca < 0) {
            item.diminuirQtdTotal(Math.abs(diferenca));
        }

        item.setDescricao(novaDescricao);
        item.setValorDiaria(novoValorDiaria);
        item.setTaxaMontagem(novaTaxaMontagem);

        this.repositorio.salvarEstoque(this.estoqueGlobal);
        return true;
    }

    public boolean atualizarEvento(int id, String novoNomeCliente, long novaDataInicio, long novaDataFim) {
        Evento evento = buscarEventoPorId(id);
        if (evento == null || novoNomeCliente == null || novoNomeCliente.isBlank() || novaDataFim < novaDataInicio) {
            return false;
        }

        evento.setNomeCliente(novoNomeCliente);
        evento.setDataInicio(novaDataInicio);
        evento.setDataFim(novaDataFim);

        this.repositorio.salvarEventos(this.eventos);
        return true;
    }

    public boolean devolverItemDoEvento(int idEvento, int idItem, int qtd) {
        Evento evento = buscarEventoPorId(idEvento);
        ItemEstrutura item = buscarItemPorId(idItem);

        if (evento != null && item != null && qtd > 0) {
            if (evento.recuperarItem(item, qtd)) {
                this.repositorio.salvarEventos(this.eventos);
                this.repositorio.salvarEstoque(this.estoqueGlobal);
                return true;
            }
        }
        return false;
    }

    public boolean alugarItemNoEvento(int idEvento, int idItem, int qtd) {
        Evento evento = buscarEventoPorId(idEvento);
        ItemEstrutura item = buscarItemPorId(idItem);

        if (evento != null && item != null) {
            boolean ok = evento.alugarItem(item, qtd);

            if (ok) {
                this.repositorio.salvarEventos(this.eventos);
                this.repositorio.salvarEstoque(this.estoqueGlobal);
                return true; // alugou itens corretamente
            }
        }
        return false; // nao encontrou o evento ou item
    }


    public Evento buscarEventoPorId(int idEvento) {
        for (Evento e : eventos) {
            if (e.getId() == idEvento) return e;
        }
        return null;
    }

    public ItemEstrutura buscarItemPorId(int idItem) {
        for (ItemEstrutura i : estoqueGlobal) {
            if (i.getId() == idItem) return i;
        }
        return null;
    }
}