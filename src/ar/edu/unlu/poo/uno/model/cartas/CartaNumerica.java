package ar.edu.unlu.poo.uno.model.cartas;

public class CartaNumerica extends Carta{
    //Carta numerica
    private final int valor;
    public CartaNumerica(Color color, int valor) {
        super(color, TipoCarta.COMUN);
        this.valor = valor;
    }
    public int getValor(){
        return this.valor;
    }
}
