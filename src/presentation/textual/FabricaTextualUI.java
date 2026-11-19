package presentation.textual;

import application.GerenciadorEstoque;
import presentation.*;

public class FabricaTextualUI extends FabricaUI {
    @Override
    public MenuPrincipal criarMenuPrincipal(GerenciadorEstoque gerenciador) {
        return new MenuTextual(gerenciador, this);
    }

    @Override
    public TelaEstoque criarTelaEstoque(GerenciadorEstoque gerenciador) {
        return new TelaEstoqueTextual(gerenciador);
    }

    @Override
    public TelaEventos criarTelaEventos(GerenciadorEstoque gerenciador) {
        return new TelaEventosTextual(gerenciador);
    }
}
