package model;

public class Multa {

    private int    diasAtraso;
    private double valor;

    public Multa(int diasAtraso) {
        this.diasAtraso = diasAtraso;
        this.valor      = diasAtraso * 2.50;
    }

    public double calcularValor() { return valor; }
    public int    getDiasAtraso() { return diasAtraso; }

    @Override
    public String toString() {
        return diasAtraso + " dia(s) de atraso - R$ " + String.format("%.2f", valor);
    }
}
