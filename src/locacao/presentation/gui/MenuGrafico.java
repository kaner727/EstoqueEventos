
package locacao.presentation.gui;

import locacao.presentation.MenuPrincipal;
import locacao.presentation.TelaEstoque;
import locacao.presentation.TelaEventos;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

final class MenuGrafico implements MenuPrincipal {
    private final TelaEstoque telaEstoque;
    private final TelaEventos telaEventos;

    MenuGrafico(TelaEstoque telaEstoque, TelaEventos telaEventos) {
        this.telaEstoque = telaEstoque;
        this.telaEventos = telaEventos;
    }

    @Override
    public void iniciar() {
        SwingUtilities.invokeLater(this::abrirJanela);
    }

    private void abrirJanela() {
        JFrame janela = new JFrame("EstoqueEventos");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("EstoqueEventos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 30f));

        JLabel subtitulo = new JLabel("Gerenciamento de materiais e locações");

        JPanel cabecalho = new JPanel(new BorderLayout(0, 6));
        cabecalho.add(titulo, BorderLayout.NORTH);
        cabecalho.add(subtitulo, BorderLayout.CENTER);

        JButton botaoEstoque = new JButton("Gerenciar estoque");
        botaoEstoque.addActionListener(e -> telaEstoque.exibirMenuEstoque());

        JButton botaoEventos = new JButton("Gerenciar eventos");
        botaoEventos.addActionListener(e -> telaEventos.exibirMenuEventos());

        JPanel acoes = new JPanel(new GridLayout(2, 1, 0, 10));
        acoes.add(botaoEstoque);
        acoes.add(botaoEventos);

        JPanel conteudo = new JPanel(new BorderLayout(0, 24));
        conteudo.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        conteudo.setPreferredSize(new Dimension(440, 240));
        conteudo.add(cabecalho, BorderLayout.NORTH);
        conteudo.add(acoes, BorderLayout.CENTER);

        janela.setContentPane(conteudo);
        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }
}
