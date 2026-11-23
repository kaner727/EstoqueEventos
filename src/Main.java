import locacao.application.GerenciadorEstoque;
import locacao.application.RepositorioDados;
import locacao.infrastructure.RepositorioArquivos;
import locacao.presentation.FabricaUI;
import locacao.presentation.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        RepositorioDados repositorio = new RepositorioArquivos();
        GerenciadorEstoque gerenciador = new GerenciadorEstoque(repositorio);
        FabricaUI fabrica = FabricaUI.getFabrica();
        MenuPrincipal menu = fabrica.criarMenuPrincipal(gerenciador);
        menu.iniciar();
    }
}