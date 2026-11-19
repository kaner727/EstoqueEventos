package presentation;

import application.GerenciadorEstoque;

public abstract class FabricaUI {
    public enum TipoInterface {
        TEXTUAL, GRAFICA
    }

    private static TipoInterface modoAtual = TipoInterface.TEXTUAL;

    public static FabricaUI getFabrica() {
        if(modoAtual == TipoInterface.GRAFICA) return new FabricaTextualUI();
        return new FabricaGraficaUI();
    }

    public abstract MenuPrincipal criarMenuPrincipal(GerenciadorEstoque gerenciador);
    public abstract TelaEstoque criarTelaEstoque(GerenciadorEstoque gerenciador);
    public abstract TelaEventos criarTelaEventos(GerenciadorEstoque gerenciador);
}
