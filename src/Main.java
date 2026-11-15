import application.GerenciadorEstoque;
import application.RepositorioDados;
import infraestructure.RepositorioArquivos;

public class Main {
    public static void main(String[] args) {
        RepositorioDados repositorio = new RepositorioArquivos();
        GerenciadorEstoque gerenciador = new GerenciadorEstoque(repositorio);
    }
}
