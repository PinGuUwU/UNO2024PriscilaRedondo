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
    public void tirarCambioColor(){
        for(int i=0; i< manoActual.size(); i++){
            boolean encontrada = false;
            Carta carta = manoActual.get(i);
            if(carta.valor() == 15 && !encontrada){
                manoActual.remove(i);
                encontrada = true;
            }
        }
    }
}
