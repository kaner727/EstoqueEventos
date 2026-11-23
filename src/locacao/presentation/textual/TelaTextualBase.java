package locacao.presentation.textual;

import locacao.application.GerenciadorEstoque;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
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

    protected int lerQuantidadeEstoque() {
        while (true) {
            int quantidade = lerInteiro();

            if (quantidade >= 0) {
                return quantidade;
            }

            System.out.print("Digite uma quantidade inteira igual ou maior que zero: ");
        }
    }

    protected double lerDouble() {
        while (true) {
            String entrada = scanner.nextLine().trim().replace(',', '.');

            try {
                double valor = Double.parseDouble(entrada);
                if (Double.isFinite(valor) && valor >= 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
            }

            System.out.print("Digite um número válido igual ou maior que zero: ");
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
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
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