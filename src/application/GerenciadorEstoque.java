package application;

import domain.Evento;
import domain.ItemEstrutura;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorEstoque {
    private List<ItemEstrutura> estoqueGlobal;
    private List<Evento> eventos;

    private final RepositorioDados repositorio;

    public GerenciadorEstoque(RepositorioDados repositorio) {
        this.repositorio = repositorio;

        this.estoqueGlobal = repositorio.carregarEstoque();
        this.eventos = repositorio.carregarEventos();

        // caso for a primeira vez abrindo o app
        if(this.estoqueGlobal == null) this.estoqueGlobal = new ArrayList<>();
        if(this.eventos == null) this.eventos = new ArrayList<>();
    }

    public void cadastrarNovoItem(ItemEstrutura item) {
        this.estoqueGlobal.add(item);
        this.repositorio.salvarEstoque(this.estoqueGlobal); // salva estado
    }

    public List<Evento> listarEventos() {
        return this.eventos;
    }

    public void cadastrarEvento(Evento evento) {
        this.eventos.add(evento);
        this.repositorio.salvarEventos(this.eventos);
    }

    public boolean alugarItemNoEvento(int idEvento, int idItem, int qtd) {
        Evento evento = buscarEventoPorId(idEvento);
        ItemEstrutura item = buscarItemPorId(idItem);

        if(evento != null && item != null) {
            boolean ok = evento.alugarItem(item, qtd);

            if(ok) {
                this.repositorio.salvarEventos(this.eventos);
                this.repositorio.salvarEstoque(this.estoqueGlobal);
                return true; // alugou itens corretamente
            }
        }
        return false; // nao encontrou o evento ou item
    }

    private Evento buscarEventoPorId(int idEvento) {
        for(Evento e : eventos) {
            if(e.getId() == idEvento) return e;
        }
        return null;
    }

    private ItemEstrutura buscarItemPorId(int idItem) {
        for(ItemEstrutura i : estoqueGlobal) {
            if(i.getId() == idItem) return i;
        }
        return null;
    }
}
