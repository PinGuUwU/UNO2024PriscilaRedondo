package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.Carta;

import java.io.Serializable;
import java.util.ArrayList;

public class MazoDeDescarte implements Serializable {
    private ArrayList<Carta> mazoActual;

    public MazoDeDescarte(){
        mazoActual = new ArrayList<Carta>();
    }
    public void agregar(Carta nuevaCarta){
        mazoActual.add(nuevaCarta);
    }
    public Carta ultimaCarta(){
        return mazoActual.get(mazoActual.size()-1);
    }
}
