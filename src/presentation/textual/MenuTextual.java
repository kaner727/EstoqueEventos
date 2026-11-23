package presentation.textual;

import application.GerenciadorEstoque;
import presentation.FabricaUI;
import presentation.MenuPrincipal;

import java.util.Scanner;

public class MenuTextual extends TelaTextualBase implements MenuPrincipal {
    private final FabricaUI fabrica;

    public MenuTextual(GerenciadorEstoque gerenciador, FabricaUI fabrica) {
        super(gerenciador);
        this.fabrica = fabrica;
    }

    @Override
    public void iniciar() {
        int opc = -1;
        while(opc != 0) {
            System.out.println("\n-----------------------------------------------------");
            System.out.println("EstoqueEventos - Gerenciamento de Materiais e Eventos");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Gerenciar Estoque de Estruturas");
            System.out.println("2. Gerenciar Eventos e Alocações");
            System.out.println("0. Sair");
            System.out.print("::: ");

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
}
