package locacao.presentation.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.util.List;
import java.util.function.IntConsumer;

final class JanelaRegistros {
    private final JDialog dialogo;
    private final DefaultTableModel modelo;
    private final JTable tabela;
    private final JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

    JanelaRegistros(String titulo, String... colunas) {
        var janelaAtiva = KeyboardFocusManager
                .getCurrentKeyboardFocusManager().getActiveWindow();

        dialogo = new JDialog(janelaAtiva, titulo, Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFillsViewportHeight(true);

        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setPreferredSize(new Dimension(700, 300));

        JPanel conteudo = new JPanel(new BorderLayout(0, 8));
        conteudo.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        conteudo.add(rolagem, BorderLayout.CENTER);
        conteudo.add(botoes, BorderLayout.SOUTH);
        dialogo.setContentPane(conteudo);
    }

    void atualizar(List<Object[]> linhas) {
        modelo.setRowCount(0);
        linhas.forEach(modelo::addRow);
    }

    void acao(String rotulo, Runnable tarefa) {
        JButton botao = new JButton(rotulo);
        botao.addActionListener(e -> tarefa.run());
        botoes.add(botao);
    }

    void acaoComSelecao(String rotulo, IntConsumer tarefa) {
        acao(rotulo, () -> {
            int linha = tabela.getSelectedRow();

            if (linha < 0) {
                avisar("Selecione um registro na tabela.");
                return;
            }

            int linhaModelo = tabela.convertRowIndexToModel(linha);
            int id = (Integer) modelo.getValueAt(linhaModelo, 0);
            tarefa.accept(id);
        });
    }

    boolean formulario(String titulo, Object... campos) {
        return JOptionPane.showConfirmDialog(
                dialogo, campos, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        ) == JOptionPane.OK_OPTION;
    }

    boolean confirmar(String mensagem) {
        return JOptionPane.showConfirmDialog(
                dialogo, mensagem, "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION;
    }

    void avisar(String mensagem) {
        JOptionPane.showMessageDialog(
                dialogo, mensagem, "EstoqueEventos", JOptionPane.WARNING_MESSAGE
        );
    }

    JDialog componente() {
        return dialogo;
    }

    void abrir() {
        dialogo.pack();
        dialogo.setLocationRelativeTo(dialogo.getOwner());
        dialogo.setVisible(true);
    }
}