package locacao.application;

import locacao.domain.model.Evento;
import locacao.domain.GeradorID;
import locacao.domain.model.ItemEstrutura;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorEstoque {
    private List<ItemEstrutura> estoqueGlobal;
    private List<Evento> eventos;

    private final RepositorioDados repositorio;

    public GerenciadorEstoque(RepositorioDados repositorio) {
        this.repositorio = repositorio;
        DadosLocacao dados = repositorio.carregar();
        this.estoqueGlobal = dados.estoque();
        this.eventos = dados.eventos();

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
        salvarDados();
    }

    public void cadastrarEvento(Evento evento) {
        this.eventos.add(evento);
        salvarDados();
    }

    public boolean excluirItem(int id) {
        ItemEstrutura item = buscarItemPorId(id);

        if (item == null
                || item.getQuantidadeDisponivel() != item.getQuantidadeTotal()) {
            return false;
        }

        estoqueGlobal.remove(item);
        salvarDados();
        return true;
    }

    public boolean excluirEvento(int id) {
        Evento evento = buscarEventoPorId(id);

        if (evento == null) {
            return false;
        }

        evento.devolverTodosOsItens();
        eventos.remove(evento);
        salvarDados();
        return true;
    }


    public List<ItemEstrutura> listarEstoque() {
        return Collections.unmodifiableList(this.estoqueGlobal);
    }

    public List<Evento> listarEventos() {
        return Collections.unmodifiableList(this.eventos);
    }


    public boolean atualizarItem(int id, String novaDescricao, int novaQuantidadeTotal, double novoValorDiaria, double novaTaxaMontagem) {
        ItemEstrutura item = buscarItemPorId(id);
        if (item == null
                || !Double.isFinite(novoValorDiaria) || novoValorDiaria < 0
                || !Double.isFinite(novaTaxaMontagem) || novaTaxaMontagem < 0) {
            return false;
        }

        if(!item.alterarQuantidadeTotal(novaQuantidadeTotal)) {
            return false;
        }

        item.setDescricao(novaDescricao);
        item.setValorDiaria(novoValorDiaria);
        item.setTaxaMontagem(novaTaxaMontagem);

        salvarDados();
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

        salvarDados();
        return true;
    }

    public boolean devolverItemDoEvento(int idEvento, int idItem, int qtd) {
        Evento evento = buscarEventoPorId(idEvento);
        ItemEstrutura item = buscarItemPorId(idItem);

        if (evento != null && item != null && qtd > 0) {
            if (evento.recuperarItem(item, qtd)) {
                salvarDados();
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
                salvarDados();
                return true; // alugou itens corretamente
            }
        }
        return false; // nao encontrou o evento ou item
    }

    private void salvarDados() {
        repositorio.salvar(new DadosLocacao(estoqueGlobal, eventos));
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