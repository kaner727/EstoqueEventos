package presentation.textual;

import application.GerenciadorEstoque;
import presentation.FabricaUI;
import java.util.Scanner;

public class MenuTextual implements MenuPrincipal {
    private final GerenciadorEstoque gerenciador;
    private final FabricaUI fabrica;
    private final Scanner scanner;

    public MenuTextual(GerenciadorEstoque gerenciador, FabricaUI fabrica) {
        this.gerenciador = gerenciador;
        this.fabrica = fabrica;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void iniciar() {
        int opc = -1;
        while(opc != 0) {
            System.out.println("\n-----------------------------------------------------");
            System.out.println("EstoqueEventos - Gerenciamento de Materiais e Eventos");
            System.out.println("-----------------------------------------------------");
            System.out.println("1: Gerenciar Estoque de Estruturas");
            System.out.println("2: Gerenciar Eventos e Alocações");
            System.out.println("0: Sair");
            System.out.println("::: ");

            opc = lerInteiro();

            switch(opc) {
                case 1:
                    fabrica.criarTelaEstoque(gerenciador).exibirMenuEstoque();
                    break;
                case 2:
                    fabrica.criarTelaEventos(gerenciador).exibirMenuEventos();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opcão inválida!");
            }
        }
    }

    private int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
