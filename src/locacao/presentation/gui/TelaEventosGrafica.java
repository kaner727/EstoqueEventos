package locacao.presentation.gui;

import locacao.application.GerenciadorEstoque;
import locacao.domain.model.Evento;
import locacao.domain.model.ItemEstrutura;
import locacao.presentation.TelaEventos;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

final class TelaEventosGrafica implements TelaEventos {
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final GerenciadorEstoque gerenciador;

    TelaEventosGrafica(GerenciadorEstoque gerenciador) {
        this.gerenciador = gerenciador;
    }

    @Override
    public void exibirMenuEventos() {
        var janela = new JanelaRegistros(
                "Eventos", "ID", "Cliente / Evento", "Início", "Fim", "Custo total (R$)"
        );

        janela.acao("Cadastrar", () -> preencherEvento(janela, null));
        janela.acaoComSelecao("Editar",
                id -> preencherEvento(janela, gerenciador.buscarEventoPorId(id)));
        janela.acaoComSelecao("Excluir", id -> excluir(janela, id));
        janela.acaoComSelecao("Detalhes", id -> exibirDetalhes(janela, id));
        janela.acaoComSelecao("Alugar item", id -> movimentarItem(janela, id, false));
        janela.acaoComSelecao("Devolver item", id -> movimentarItem(janela, id, true));
        janela.acao("Voltar", janela.componente()::dispose);

        atualizarTabela(janela);
        janela.abrir();
    }

    private void atualizarTabela(JanelaRegistros janela) {
        janela.atualizar(gerenciador.listarEventos().stream()
                .map(evento -> new Object[]{
                        evento.getId(), evento.getNomeCliente(),
                        formatarData(evento.getDataInicio()),
                        formatarData(evento.getDataFim()),
                        String.format("%.2f", evento.calcularCustoTotal())
                })
                .toList());
    }

    private void preencherEvento(JanelaRegistros janela, Evento evento) {
        JTextField nome = new JTextField(evento == null ? "" : evento.getNomeCliente(), 24);
        JTextField inicio = new JTextField(evento == null ? "" : formatarData(evento.getDataInicio()));
        JTextField fim = new JTextField(evento == null ? "" : formatarData(evento.getDataFim()));

        if (!janela.formulario(evento == null ? "Cadastrar evento" : "Editar evento",
                "Cliente / Evento", nome,
                "Data de início (DD/MM/AAAA)", inicio,
                "Data de fim (DD/MM/AAAA)", fim)) {
            return;
        }

        try {
            String cliente = nome.getText().strip();
            long dataInicio = LocalDate.parse(inicio.getText().strip(), FORMATO_DATA).toEpochDay();
            long dataFim = LocalDate.parse(fim.getText().strip(), FORMATO_DATA).toEpochDay();

            if (cliente.isBlank() || dataFim < dataInicio) {
                janela.avisar("Informe o nome e uma data final igual ou posterior à inicial.");
                return;
            }

            if (evento == null) {
                gerenciador.cadastrarEvento(new Evento(cliente, dataInicio, dataFim));
            } else if (!gerenciador.atualizarEvento(evento.getId(), cliente, dataInicio, dataFim)) {
                janela.avisar("Não foi possível atualizar o evento.");
                return;
            }

            atualizarTabela(janela);
        } catch (DateTimeParseException e) {
            janela.avisar("Informe datas válidas no formato DD/MM/AAAA.");
        }
    }

    private void excluir(JanelaRegistros janela, int id) {
        if (!janela.confirmar("Excluir o evento #" + id + " e devolver os itens alugados?")) {
            return;
        }

        if (gerenciador.excluirEvento(id)) {
            atualizarTabela(janela);
        } else {
            janela.avisar("Evento não encontrado.");
        }
    }

    private void exibirDetalhes(JanelaRegistros janela, int id) {
        Evento evento = gerenciador.buscarEventoPorId(id);
        JTextArea texto = new JTextArea(evento.toString(), 12, 48);
        texto.setEditable(false);
        texto.setCaretPosition(0);

        JOptionPane.showMessageDialog(
                janela.componente(), new JScrollPane(texto),
                "Detalhes do evento", JOptionPane.PLAIN_MESSAGE
        );
    }

    private void movimentarItem(JanelaRegistros janela, int idEvento, boolean devolucao) {
        var estoque = gerenciador.listarEstoque();

        if (estoque.isEmpty()) {
            janela.avisar("Nenhum item cadastrado no estoque.");
            return;
        }

        String[] opcoes = estoque.stream()
                .map(item -> "#" + item.getId() + " — " + item.getDescricao()
                        + " (" + item.getQuantidadeDisponivel() + " disponíveis)")
                .toArray(String[]::new);

        JComboBox<String> itens = new JComboBox<>(opcoes);
        JTextField quantidade = new JTextField("1");
        String titulo = devolucao ? "Devolver item" : "Alugar item";

        if (!janela.formulario(titulo, "Item", itens, "Quantidade", quantidade)) {
            return;
        }

        try {
            int total = Integer.parseInt(quantidade.getText().strip());

            if (total <= 0) {
                janela.avisar("Informe uma quantidade maior que zero.");
                return;
            }

            ItemEstrutura item = estoque.get(itens.getSelectedIndex());
            boolean sucesso = devolucao
                    ? gerenciador.devolverItemDoEvento(idEvento, item.getId(), total)
                    : gerenciador.alugarItemNoEvento(idEvento, item.getId(), total);

            if (sucesso) {
                atualizarTabela(janela);
            } else {
                janela.avisar(devolucao
                        ? "Esse item não está alugado neste evento."
                        : "Estoque insuficiente para essa quantidade.");
            }
        } catch (NumberFormatException e) {
            janela.avisar("Informe uma quantidade inteira válida.");
        }
    }

    private String formatarData(long dia) {
        return LocalDate.ofEpochDay(dia).format(FORMATO_DATA);
    }
}