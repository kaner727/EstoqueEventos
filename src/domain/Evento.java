package domain;

public class Evento {
        private static int contadorId;
        private int id;
        private String nomeCliente;
        private long dataInicio;
        private long dataFim;
        // private ArrayList<ItemEstoque> itensAlugados;

        Evento(String nomeCliente, long dataInicio, long dataFim) {
                this.id = contadorId++;
                this.nomeCliente = nomeCliente;
                this.dataInicio = dataInicio;
                this.dataFim = dataFim;
        }


}
