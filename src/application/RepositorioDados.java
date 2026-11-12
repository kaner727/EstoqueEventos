package application;
import domain.Evento;
import domain.ItemEstrutura;
import java.util.List;

public interface RepositorioDados {
    void salvarEstoque(List<ItemEstrutura> estoque);
    List<ItemEstrutura> carregarEstoque();

    void salvarEventos(List<Evento> eventos);
    List<Evento> carregarEventos();
}
