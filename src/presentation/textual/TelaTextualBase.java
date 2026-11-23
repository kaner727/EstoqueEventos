package presentation.textual;

import application.GerenciadorEstoque;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public abstract class TelaTextualBase {
    protected static final Scanner scanner = new Scanner(System.in);
    protected final GerenciadorEstoque gerenciador;

    public TelaTextualBase(GerenciadorEstoque gerenciador) {
        this.gerenciador = gerenciador;
    }

    protected int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected double lerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    protected long lerLong() {
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    protected long lerData() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                LocalDate data = LocalDate.parse(entrada, formato);
                return data.toEpochDay();
            } catch (DateTimeParseException e) {
                System.out.print("Data inválida! Digite no formato DD/MM/AAAA (ex: 25/12/2025): ");
            }
        }
    }
}