package locacao.application;

import locacao.domain.Evento;
import locacao.domain.ItemEstrutura;

import java.io.Serializable;
import java.util.List;

public record DadosLocacao(
        List<ItemEstrutura> estoque,
        List<Evento> eventos
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
