package presentation.textual;

import application.GerenciadorEstoque;
import domain.Evento;
import domain.ItemEstrutura;
import presentation.TelaEventos;

import java.util.List;

public class TelaEventosTextual extends TelaTextualBase implements TelaEventos {

    public TelaEventosTextual(GerenciadorEstoque gerenciador) {
        super(gerenciador);
    }

    @Override
    public void exibirMenuEventos() {
        int opc = -1;
        while (opc != 0) {
            System.out.println("\n/// Gerenciamento de Eventos ///");
            System.out.println("1. Cadastrar novo Evento");
            System.out.println("2. Listar Eventos cadastrados");
            System.out.println("3. Editar Evento (por ID)");
            System.out.println("4. Alugar Item para Evento");
            System.out.println("5. Devolver Item ao Estoque");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("::: ");
            opc = lerInteiro();

            switch (opc) {
                case 1:
                    cadastrarEvento();
                    break;
                case 2:
                    listarEventos();
                    break;
                case 3:
                    editarEvento();
                    break;
                case 4:
                    alugarItem();
                    break;
                case 5:
                    devolverItem();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarEvento() {
        System.out.print("\nNome do Cliente / Evento: ");
        String nomeCliente = scanner.nextLine();

        System.out.print("Data de Início (DD/MM/AAAA): ");
        long dataInicio = lerData();

        System.out.print("Data de Fim (DD/MM/AAAA): ");
        long dataFim = lerData();

        if (dataFim < dataInicio) {
            System.out.println("A data final não pode ser menor que a inicial. Cadastro cancelado.");
            return;
        }

        Evento novoEvento = new Evento(nomeCliente, dataInicio, dataFim);
        gerenciador.cadastrarEvento(novoEvento);
        System.out.println("Evento cadastrado com sucesso! (ID: " + novoEvento.getId() + ")");
    }

    private void listarEventos() {
        List<Evento> eventos = gerenciador.listarEventos();
        System.out.println("\n--- EVENTOS CADASTRADOS ---");
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
            return;
        }
        for (Evento evento : eventos) {
            System.out.println(evento);
        }
    }

    private void editarEvento() {
        listarEventos();
        System.out.print("\nID do evento para editar (digite 0 para sair): ");
        int id = lerInteiro();
        if (id <= 0) return;

        Evento eventoAtual = gerenciador.buscarEventoPorId(id);
        if (eventoAtual == null) {
            System.out.println("Evento com ID " + id + " não encontrado.");
            return;
        }

        System.out.println("Editando:\n" + eventoAtual);

        System.out.print("Novo nome do Cliente: ");
        String novoNome = scanner.nextLine();

        System.out.print("Nova data de Início (DD/MM/AAAA): ");
        long novaInicio = lerData();

        System.out.print("Nova data de Fim (DD/MM/AAAA): ");
        long novaFim = lerData();

        if (gerenciador.atualizarEvento(id, novoNome, novaInicio, novaFim)) {
            System.out.println("Evento atualizado e salvo com sucesso!");
        } else {
            System.out.println("Falha ao atualizar: verifique os dados informados.");
        }
    }

    private void alugarItem() {
        if (gerenciador.listarEventos().isEmpty() || gerenciador.listarEstoque().isEmpty()) {
            System.out.println("É necessário ter ao menos um evento e um item cadastrados.");
            return;
        }

        listarEventos();
        System.out.print("\nID do Evento: ");
        int idEvento = lerInteiro();

        System.out.println("\n--- ITENS EM ESTOQUE ---");
        for (ItemEstrutura item : gerenciador.listarEstoque()) {
            System.out.println(item);
        }

        System.out.print("ID do Item para alugar: ");
        int idItem = lerInteiro();

        System.out.print("Quantidade desejada: ");
        int qtd = lerInteiro();

        if (gerenciador.alugarItemNoEvento(idEvento, idItem, qtd)) {
            System.out.println("Item alugado para o evento e estoque atualizado!");
        } else {
            System.out.println("Não foi possível alugar: verifique os IDs ou o saldo disponível.");
        }
    }

    private void devolverItem() {
        listarEventos();
        System.out.print("\nID do Evento: ");
        int idEvento = lerInteiro();

        System.out.println("\n--- ITENS EM ESTOQUE ---");
        for (ItemEstrutura item : gerenciador.listarEstoque()) {
            System.out.println(item);
        }

        System.out.print("ID do Item a devolver: ");
        int idItem = lerInteiro();

        System.out.print("Quantidade a devolver: ");
        int qtd = lerInteiro();

        if (gerenciador.devolverItemDoEvento(idEvento, idItem, qtd)) {
            System.out.println("Item devolvido ao estoque com sucesso!");
        } else {
            System.out.println("Falha na devolução: verifique os IDs ou se o item está alocado nesse evento.");
        }
    }
}