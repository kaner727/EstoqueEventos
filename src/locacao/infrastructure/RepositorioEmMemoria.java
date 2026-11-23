package locacao.infrastructure;

import locacao.application.DadosLocacao;
import locacao.application.RepositorioDados;

import java.util.ArrayList;

public class RepositorioEmMemoria implements RepositorioDados {
    private DadosLocacao dados = new DadosLocacao(
            new ArrayList<>(),
            new ArrayList<>()
    );

    @Override
    public DadosLocacao carregar() {
        return dados;
    }

    @Override
    public void salvar(DadosLocacao dados) {
        this.dados = dados;
    }
}
