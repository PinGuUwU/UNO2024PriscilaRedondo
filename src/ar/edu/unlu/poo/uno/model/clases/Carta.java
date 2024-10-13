package ar.edu.unlu.poo.uno.model.clases;

public class Carta {
    private final String color;
    private final int valor;
    /*private int posiblesNumeros;
    private String[] posiblesColores;
    Estas dos clases no las uso porque ya me aseguré en "mezclador que se generen cartas validas."*/

    public Carta(int valor, String color){
        this.color = color;
        this.valor = valor;
    }
    public Carta(int valor){ //Para crear las especiales
        color = null;
        this.valor = valor;
    }
    public Carta(String color){
        this.color = color;
        valor = 15;
    }

    public int valor(){
        return valor;
    }
    public String color(){
        return color;
    }
}
