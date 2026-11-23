package locacao.infrastructure;

import locacao.application.DadosLocacao;
import locacao.application.RepositorioDados;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;

public class RepositorioArquivos implements RepositorioDados {
    private static final Path ARQUIVO = Path.of("dados.dat");

    @Override
    public DadosLocacao carregar() {
        try (var entrada = new ObjectInputStream(Files.newInputStream(ARQUIVO))) {
            return (DadosLocacao) entrada.readObject();
        } catch (NoSuchFileException e) {
            return new DadosLocacao(new ArrayList<>(), new ArrayList<>());
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível carregar os dados.", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Formato de dados incompatível.", e);
        }
    }

    @Override
    public void salvar(DadosLocacao dados) {
        try (var saida = new ObjectOutputStream(Files.newOutputStream(ARQUIVO))) {
            saida.writeObject(dados);
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível salvar os dados.", e);
        }
    }
}
