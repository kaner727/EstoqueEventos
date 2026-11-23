import application.GerenciadorEstoque;
import application.RepositorioDados;
import infraestructure.RepositorioArquivos;
import presentation.FabricaUI;
import presentation.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        RepositorioDados repositorio = new RepositorioArquivos();
        GerenciadorEstoque gerenciador = new GerenciadorEstoque(repositorio);
        FabricaUI fabrica = FabricaUI.getFabrica();
        MenuPrincipal menu = fabrica.criarMenuPrincipal(gerenciador);
        menu.iniciar();
    }
}