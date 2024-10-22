package ar.edu.unlu.poo.uno.model;

import java.util.ArrayList;

public class MazoDeDescarte {
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
