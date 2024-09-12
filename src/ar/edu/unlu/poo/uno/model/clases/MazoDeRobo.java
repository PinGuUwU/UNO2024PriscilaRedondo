package ar.edu.unlu.poo.uno.model.clases;

import java.util.ArrayList;
import java.util.Random;

public class MazoDeRobo {
    private ArrayList<Carta> mazoActual;
    public Carta robar(){//Debo de retirar una carta al azar, porque el mazo está en orden.
        Random random = new Random(System.currentTimeMillis());
        int numAleatorio = random.nextInt(mazoActual.size());
        Carta carta = mazoActual.get(numAleatorio);
        mazoActual.remove(numAleatorio); //Primero elimino esa carta del mazo
        return carta;//Luego se la doy al "jugador"
    }
    public void mezclar(){
        Mezclador mezclar = new Mezclador();
        mazoActual = mezclar.mezclar();
    }
}
