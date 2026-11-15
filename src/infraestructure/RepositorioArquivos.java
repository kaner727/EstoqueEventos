package infraestructure;

import application.RepositorioDados;
import domain.Evento;
import domain.ItemEstrutura;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioArquivos implements RepositorioDados {
    private static final String ARQUIVO_EVENTOS = "eventos.dat";
    private static final String ARQUIVO_ESTOQUE = "estoque.dat";

    @Override
    public void salvarEstoque(List<ItemEstrutura> estoque) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_ESTOQUE))) {
            oos.writeObject(estoque);
        } catch (IOException e) {
            System.err.println("O estoque não foi salvo corretamente: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ItemEstrutura> carregarEstoque() {
        File arquivo = new File(ARQUIVO_ESTOQUE);
        if(!arquivo.exists()) {
            return new ArrayList<>(); // primeira execução
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<ItemEstrutura>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("O estoque não foi carregado corretamente: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void salvarEventos(List<Evento> eventos) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_EVENTOS))) {
            oos.writeObject(eventos);
        } catch (IOException e) {
            System.err.println("Os eventos não foram salvos corretamente: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Evento> carregarEventos() {
        File arquivo = new File(ARQUIVO_EVENTOS);
        if(!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Evento>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Os eventos não foram carregados corretamente: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
