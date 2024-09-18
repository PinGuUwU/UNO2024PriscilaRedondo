package ar.edu.unlu.poo.uno.model.clases;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Contador {
    //No sé qué utilidad darle
    //Podría ser que le paso los jugadores y devuelve los jugadores en orden de punto, pero no sé
    //Sería para encapsular más, pero no creo que sea muy largo de hacer en el programa
    public Contador(){
    }
    /*
    Orden de cartas:
    0=1, 1=2, 2=3, 3=4, 4=5, 5=6, 6=7, 7=8, 8=9, 9=10, +2=11, sentido=12, bloqueo=13, +4=14, cambioColor=15;

    numerica: valor (0-10)
    +2, sentido, bloqueo: 20 (11-12-13)
    +4, cambioColor: 50 (14-15)
     */
    public ArrayList<Jugador> puestos(ArrayList<Jugador> jugadores){
        ArrayList<Jugador> jugadoresPorPuntos = new ArrayList<>(jugadores);

        Collections.sort(jugadoresPorPuntos, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                // Compara los números en orden inverso (de mayor a menor)
                return Integer.compare(j1.actualizarPuntosActuales(), j2.actualizarPuntosActuales()); // n2 antes que n1
            }
        });

        return jugadoresPorPuntos;
    }

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
