package ar.edu.unlu.poo.uno.model.clases;

import java.util.ArrayList;

public class MazoDeRobo {
    private ArrayList<Carta> mazoActual;
    public Carta robar(){
        Carta carta = mazoActual.get(mazoActual.size()-1);
        mazoActual.remove(mazoActual.size()-1);
        return carta;
    }
    public void mezclar(){
        Mezclador mezclar = new Mezclador();
        mazoActual = mezclar.mezclar();
    }
}
