package locacao.presentation.gui;

import locacao.application.GerenciadorEstoque;
import locacao.domain.model.EquipamentoSom;
import locacao.domain.model.EstruturaAluminio;
import locacao.domain.model.ItemEstrutura;
import locacao.domain.model.Tenda;
import locacao.presentation.TelaEstoque;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

final class TelaEstoqueGrafica implements TelaEstoque {
    private final GerenciadorEstoque gerenciador;

    TelaEstoqueGrafica(GerenciadorEstoque gerenciador) {
        this.gerenciador = gerenciador;
    }

    @Override
    public void exibirMenuEstoque() {
        var janela = new JanelaRegistros(
                "Estoque", "ID", "Descrição", "Disponível", "Total",
                "Diária (R$)", "Montagem (R$)"
        );

        janela.acao("Cadastrar", () -> cadastrar(janela));
        janela.acaoComSelecao("Editar", id -> editar(janela, id));
        janela.acaoComSelecao("Excluir", id -> excluir(janela, id));
        janela.acao("Voltar", janela.componente()::dispose);
        atualizarTabela(janela);
        janela.abrir();
    }

    private void atualizarTabela(JanelaRegistros janela) {
        janela.atualizar(gerenciador.listarEstoque().stream()
                .map(item -> new Object[]{
                        item.getId(), item.getDescricao(),
                        item.getQuantidadeDisponivel(), item.getQuantidadeTotal(),
                        String.format("%.2f", item.getValorDiaria()),
                        String.format("%.2f", item.getTaxaMontagem())
                })
                .toList());
    }

    private void cadastrar(JanelaRegistros janela) {
        Object[] tipos = {"Tenda", "Equipamento de som", "Estrutura de alumínio"};
        int tipo = JOptionPane.showOptionDialog(
                janela.componente(), "Escolha o tipo de item.", "Cadastrar item",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, tipos, tipos[0]
        );

        if (tipo == JOptionPane.CLOSED_OPTION) {
            return;
        }

        JTextField descricao = new JTextField(24);
        JTextField quantidade = new JTextField("1");
        JTextField diaria = new JTextField();
        JTextField montagem = new JTextField("0");
        JTextField medida = new JTextField();
        JTextField truss = new JTextField();

        String rotuloMedida = switch (tipo) {
            case 0 -> "Área (m²)";
            case 1 -> "Potência (W)";
            default -> "Comprimento (m)";
        };

        var campos = new ArrayList<Object>(List.of(
                "Descrição", descricao, "Quantidade total", quantidade,
                "Diária (R$)", diaria, "Taxa de montagem (R$)", montagem,
                rotuloMedida, medida
        ));

        if (tipo == 2) {
            campos.add("Tipo de truss");
            campos.add(truss);
        }

        if (!janela.formulario("Cadastrar item", campos.toArray())) {
            return;
        }

        try {
            String nome = descricao.getText().strip();
            int total = Integer.parseInt(quantidade.getText().strip());
            double valor = decimal(diaria);
            double taxa = decimal(montagem);

            ItemEstrutura item = switch (tipo) {
                case 0 -> new Tenda(nome, total, valor, taxa, decimal(medida));
                case 1 -> new EquipamentoSom(
                        nome, total, valor, taxa, Integer.parseInt(medida.getText().strip())
                );
                default -> new EstruturaAluminio(
                        nome, total, valor, taxa, decimal(medida), truss.getText().strip()
                );
            };

            gerenciador.cadastrarNovoItem(item);
            atualizarTabela(janela);
        } catch (NumberFormatException e) {
            janela.avisar("Confira os campos numéricos. Quantidade e potência usam números inteiros.");
        } catch (IllegalArgumentException e) {
            janela.avisar(e.getMessage());
        }
    }

    private void editar(JanelaRegistros janela, int id) {
        ItemEstrutura item = gerenciador.buscarItemPorId(id);

        JTextField descricao = new JTextField(item.getDescricao(), 24);
        JTextField quantidade = new JTextField(String.valueOf(item.getQuantidadeTotal()));
        JTextField diaria = new JTextField(String.valueOf(item.getValorDiaria()));
        JTextField montagem = new JTextField(String.valueOf(item.getTaxaMontagem()));

        if (!janela.formulario("Editar item #" + id,
                "Descrição", descricao, "Quantidade total", quantidade,
                "Diária (R$)", diaria, "Taxa de montagem (R$)", montagem)) {
            return;
        }

        try {
            boolean atualizado = gerenciador.atualizarItem(
                    id, descricao.getText().strip(),
                    Integer.parseInt(quantidade.getText().strip()),
                    decimal(diaria), decimal(montagem)
            );

            if (atualizado) {
                atualizarTabela(janela);
            } else {
                janela.avisar("Confira a descrição, os preços e a quantidade já alugada.");
            }
        } catch (NumberFormatException e) {
            janela.avisar("Preencha a quantidade e os preços com números válidos.");
        }
    }

    private void excluir(JanelaRegistros janela, int id) {
        if (!janela.confirmar("Excluir o item #" + id + "?")) {
            return;
        }

        if (gerenciador.excluirItem(id)) {
            atualizarTabela(janela);
        } else {
            janela.avisar("O item não foi encontrado ou ainda tem unidades alugadas.");
        }
    }

    private double decimal(JTextField campo) {
        double valor = Double.parseDouble(campo.getText().strip().replace(',', '.'));

        if (!Double.isFinite(valor) || valor < 0) {
            throw new NumberFormatException("Valor inválido.");
        }

        return valor;
    }
}
