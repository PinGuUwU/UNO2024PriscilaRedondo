package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.Carta;

import java.io.Serializable;
import java.util.ArrayList;

public class MazoDeDescarte implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Carta> mazoActual;

    public MazoDeDescarte(){
        mazoActual = new ArrayList<>();
    }

    public void agregar(Carta nuevaCarta){
        mazoActual.add(nuevaCarta);
    }

    public Carta ultimaCarta(){
        return mazoActual.get(mazoActual.size()-1);
    }

    public Carta anteultimaCarta(){
        return mazoActual.get(mazoActual.size()-2);
    }

}
