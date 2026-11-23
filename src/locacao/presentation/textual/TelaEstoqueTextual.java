package locacao.presentation.textual;

import locacao.application.GerenciadorEstoque;
import locacao.domain.model.EquipamentoSom;
import locacao.domain.model.EstruturaAluminio;
import locacao.domain.model.ItemEstrutura;
import locacao.domain.model.Tenda;
import locacao.presentation.TelaEstoque;

import java.util.List;

public class TelaEstoqueTextual extends TelaTextualBase implements TelaEstoque {

    public TelaEstoqueTextual(GerenciadorEstoque gerenciador) {
        super(gerenciador);
    }

    @Override
    public void exibirMenuEstoque() {
        int opc = -1;
        while (opc != 0) {
            System.out.println("\n/// Gerenciamento de Estoque ///");
            System.out.println("1. Cadastrar novo Item");
            System.out.println("2. Listar Estoque Completo");
            System.out.println("3. Editar Item (por ID)");
            System.out.println("4. Excluir Item (por ID)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("::: ");
            opc = lerInteiro();

            switch (opc) {
                case 1:
                    cadastrarItem();
                    break;
                case 2:
                    listarEstoque();
                    break;
                case 3:
                    editarItem();
                    break;
                case 4:
                    excluirItem();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarItem() {
        System.out.println("\nTipo de estrutura:");
        System.out.println("1. Tenda");
        System.out.println("2. Equipamento de Som");
        System.out.println("3. Estrutura de Alumínio (Box Truss)");
        System.out.print("::: ");
        int tipo = lerInteiro();

        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo inválido. Cadastro cancelado.");
            return;
        }

        System.out.print("Descrição do item: ");
        String descricao = scanner.nextLine();

        System.out.print("Valor unitário da diária (R$): ");
        double valorDiaria = lerDouble();

        System.out.print("Quantidade total disponível: ");
        int quantidade = lerQuantidadeEstoque();

        System.out.print("Taxa de montagem (R$): ");
        double taxaMontagem = lerDouble();

        ItemEstrutura novoItem = null;
        switch (tipo) {
            case 1:
                System.out.print("Metragem quadrada (m²): ");
                double area = lerDouble();
                novoItem = new Tenda(descricao, quantidade, valorDiaria, taxaMontagem, area);
                break;
            case 2:
                System.out.print("Potência (Watts): ");
                int potencia = lerInteiro();
                novoItem = new EquipamentoSom(descricao, quantidade, valorDiaria, taxaMontagem, potencia);
                break;
            case 3:
                System.out.print("Comprimento da peça (m): ");
                double comprimento = lerDouble();
                System.out.print("Tipo Truss: ");
                String tipoTruss = scanner.nextLine();
                novoItem = new EstruturaAluminio(descricao, quantidade, valorDiaria, taxaMontagem, comprimento, tipoTruss);
                break;
        }

        if (novoItem != null) {
            gerenciador.cadastrarNovoItem(novoItem);
            System.out.println("Item cadastrado e salvo com sucesso! (ID: " + novoItem.getId() + ")");
        }
    }

    private void editarItem() {
        listarEstoque();
        System.out.print("\nID do item para editar (digite 0 para sair): ");
        int id = lerInteiro();
        if (id <= 0) return;

        ItemEstrutura itemAtual = gerenciador.buscarItemPorId(id);
        if (itemAtual == null) {
            System.out.println("Item com ID " + id + " não encontrado.");
            return;
        }

        System.out.println("Editando: " + itemAtual);

        System.out.print("Nova descrição: ");
        String novaDescricao = scanner.nextLine();

        System.out.print("Novo valor unitário da diária (R$): ");
        double novoValorDiaria = lerDouble();

        System.out.print("Nova quantidade total (atual: " + itemAtual.getQuantidadeTotal() + "): ");
        int novaQuantidadeTotal = lerQuantidadeEstoque();

        System.out.print("Nova taxa de montagem (R$): ");
        double novaTaxaMontagem = lerDouble();

        if (gerenciador.atualizarItem(id, novaDescricao, novaQuantidadeTotal, novoValorDiaria, novaTaxaMontagem)) {
            System.out.println("Item atualizado e salvo com sucesso!");
        } else {
            System.out.println("Falha ao atualizar: verifique se os valores são válidos ou se a nova quantidade é menor que os itens já alugados.");
        }
    }

    private void excluirItem() {
        listarEstoque();
        System.out.print("\nID do item para excluir (0 para cancelar): ");
        int id = lerInteiro();

        if (id == 0) {
            return;
        }

        if (gerenciador.excluirItem(id)) {
            System.out.println("Item excluído com sucesso!");
        } else {
            System.out.println("Item não encontrado ou com unidades alugadas.");
        }
    }

    private void listarEstoque() {
        List<ItemEstrutura> itens = gerenciador.listarEstoque();
        System.out.println("\n--- ITENS EM ESTOQUE ---");
        if (itens.isEmpty()) {
            System.out.println("Nenhum item cadastrado.");
            return;
        }
        for (ItemEstrutura item : itens) {
            System.out.println(item);
        }
    }
}