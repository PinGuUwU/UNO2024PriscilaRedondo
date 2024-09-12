package ar.edu.unlu.poo.uno.model.clases;

public class Carta {
    private final String color;
    private final int valor;
    private int posiblesNumeros;
    private String[] posiblesColores;
    /*
    Orden de cartas:
    0=1, 1=2, 2=3, 3=4, 4=5, 5=6, 6=7, 7=8, 8=9, 9=10, +2=11, sentido=12, bloqueo=13, +4=14, cambioColor=15;
     */

    public Carta(int valor, String color){
        this.color = color;
        this.valor = valor;
    }
    public Carta(int valor){ //Para crear las especiales
        color = null;
        this.valor = valor;
    }

    public int valor(){
        return valor;
    }
    public String color(){
        return color;
    }
}
