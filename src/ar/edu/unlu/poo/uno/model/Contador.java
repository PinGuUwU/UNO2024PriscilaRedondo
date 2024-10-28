package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.CartaNumerica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Contador implements Serializable {
    /*
    Cuando el RMI funcione, con esto debe poder contabilizar los puntos de quienes perdieron
    Y mostrarlos por pantalla, solo como dato
     */

    public int contar(Mano mano){
        int contador=0;
        for(int i = 0; i < mano.cantCartas(); i++){
            switch (mano.leerCartaMano(i).valor()) {
                case COMUN -> {
                    CartaNumerica c = (CartaNumerica) mano.leerCartaMano(i);
                    contador += c.getValor();
                }
                case CAMBIO_SENTIDO, BLOQUEO, MAS_DOS -> contador += 20;
                case MAS_CUATRO, CAMBIO_COLOR -> contador += 50;
            }
        }
        return contador;
    }
}
