package locacao.presentation.gui;

import locacao.application.GerenciadorEstoque;
import locacao.presentation.FabricaUI;
import locacao.presentation.MenuPrincipal;
import locacao.presentation.TelaEstoque;
import locacao.presentation.TelaEventos;

import javax.swing.*;
import java.util.List;

public class FabricaGraficaUI extends FabricaUI {
    @Override
    public MenuPrincipal criarMenuPrincipal(GerenciadorEstoque gerenciador) {
        return new MenuGrafico(
                criarTelaEstoque(gerenciador),
                criarTelaEventos(gerenciador)
        );
    }

    @Override
    public TelaEstoque criarTelaEstoque(GerenciadorEstoque gerenciador) {
        return new TelaEstoqueGrafica(gerenciador);
    }

    @Override
    public TelaEventos criarTelaEventos(GerenciadorEstoque gerenciador) {
        return new TelaEventosGrafica(gerenciador);
    }

    private void exibirLista(String titulo, List<?> registros) {
        String texto = registros.isEmpty()
                ? "Nenhum registro cadastrado."
                : String.join("\n\n", registros.stream().map(Object::toString).toList());

        JTextArea area = new JTextArea(texto, 12, 48);
        area.setEditable(false);
        area.setCaretPosition(0);

        JOptionPane.showMessageDialog(
                null, new JScrollPane(area), titulo, JOptionPane.PLAIN_MESSAGE
        );
    }
}
