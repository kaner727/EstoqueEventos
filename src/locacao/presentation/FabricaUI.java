package locacao.presentation;

import locacao.application.GerenciadorEstoque;
import locacao.presentation.gui.FabricaGraficaUI;
import locacao.presentation.textual.FabricaTextualUI;

public abstract class FabricaUI {
    public enum TipoInterface {
        TEXTUAL, GRAFICA
    }

    private static final TipoInterface modoAtual = TipoInterface.TEXTUAL;

    public static FabricaUI getFabrica() {
        if(modoAtual == TipoInterface.TEXTUAL) return new FabricaTextualUI();
        return new FabricaGraficaUI();
    }

    public abstract MenuPrincipal criarMenuPrincipal(GerenciadorEstoque gerenciador);
    public abstract TelaEstoque criarTelaEstoque(GerenciadorEstoque gerenciador);
    public abstract TelaEventos criarTelaEventos(GerenciadorEstoque gerenciador);
}
