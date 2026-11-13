package application;

public class GeradorID {
    private GeradorID() {}

    private static int ultimoIDEvento = 0;
    private static int ultimoIDItem = 0;

    public static int gerarIDEvento() {
        ultimoIDEvento++;
        return ultimoIDEvento;
    }

    public static void setUltimoIDEvento(int id) {
        ultimoIDEvento = id;
    }

    public static int gerarIDItem() {
        ultimoIDItem++;
        return ultimoIDItem;
    }

    public static void setUltimoIDItem(int id) {
        ultimoIDItem = id;
    }
}
