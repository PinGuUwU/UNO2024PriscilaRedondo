package ar.edu.unlu.poo.uno.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Contador {
    /*
    Cuando el RMI funcione, con esto debe poder contabilizar los puntos de quienes perdieron
    Y mostrarlos por pantalla, solo como dato
     */

    public int contar(Mano mano){
        int contador=0;
        for(int i = 0; i < mano.cantCartas(); i++){
            switch (mano.leerCartaMano(i).valor()) {
                case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 -> contador += mano.leerCartaMano(i).valor();
                case 11, 12, 13 -> contador += 20;
                case 14, 15 -> contador += 50;
            }
        }
        return contador;
    }
}
