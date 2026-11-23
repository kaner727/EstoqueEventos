package locacao.application;

public interface RepositorioDados {
    DadosLocacao carregar();
    void salvar(DadosLocacao dados);
}
