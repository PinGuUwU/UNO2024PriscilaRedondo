package ar.edu.unlu.poo.uno.model.clases;

import java.util.ArrayList;

public class Mano {
    private ArrayList<Carta> manoActual;
    public Mano(){
        manoActual = new ArrayList<Carta>();
    }
    /*La elimina*/
    public Carta entregarCarta(int pos){
        Carta carta = manoActual.get(pos);
        manoActual.remove(pos);
        return carta;
    }
    public void recibeCarta(Carta carta){
        manoActual.add(carta);
    }
    public Carta leerCartaMano(int pos){
        return manoActual.get(pos);
    }
    public int cantCartas(){
        return manoActual.size();
    }
}
