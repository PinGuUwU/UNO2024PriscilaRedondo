package ar.edu.unlu.poo.uno.model.clases;

import java.util.ArrayList;

public class Jugador {
    private String username;
    private int partidasGanadas;
    private int partidasPerdidas;
    private int puntosPartidaActual;
    private ArrayList<Carta> mano;

    public Jugador(String username){
        this.username = username;
    }

    public void levantarCarta(Carta nuevaCarta){
        mano.add(nuevaCarta);
    }

    public void tirarCarta(Carta tirar){
        /*
        Posibilidades:
        void tirarCarta(Carta) //voy a hacer esta versión por ahora 11/09
        Las cartas del jugador son leídas por el programa, para que el jugador le indique al programa
        qué carta quiere tirar y el programa le envíe al jugador qué carta debe tirar.
        En esta versión, como el programa ya leyó sus cartas y el jugador ya eligió cuál tirar,
        entonces solo se actualiza la mano y el programa ya tiene el valor de la carta, no
        necesito retornarla, sería redundante
        Carta tirarCarta()
        el jugador muestra las cartas al jugador y le da a elegir cuál debe tirar, pero debería tener una
        lógica para saber si puede tirarla o no
        Boolean tirarCarta()
        Esto sería para que el programa sepa si puede tirarla, pero si creo bien la lógica, no debería de
        haber este tipo de problemas, porque estarían previamente verificados
         */
        if(mano.contains(tirar)){ //Si la logica del programa es correcta, luego puedo sacar este if y va a funcionar bien
            mano.remove(tirar);
        }
    }
    public void actualizarPartidasPerdidas(){
        partidasPerdidas++;
    }
    public void actualizarPartidasGanadas(){
        partidasGanadas++;
    }
    public void actualizarPuntosActuales(int cantidad){//Este conteo lo llevaría el jugador o el programa?
        puntosPartidaActual =+ cantidad;
        /*Caso que el jugador cuenta sus puntos:

         */
        for(Carta carta: mano){

        }
    }
    /*
    Pienso que los puntos debería de llevarlos el programa
    Ya que si los lleva el jugador, se actualizaría al finalizar la partida, para luego de actualizarlo, leerlo
    y luego consultarlo para poner con qué puntaje acabaron los jugadores de la partida
    Pero eso perfectamente puede hacerlo el programa
    Y si no:
    actualizarPuntosActuales() podría simplemente leer la mano y sumar los puntos que tiene
    entonces ahí sí los puntos van a depender del jugador y ahí tendría sentido consultarle a él su puntaje actual
    al finalizar una partida
     */
    public int puntosActuales(){ //Si puedo actualizar los puntos en el jugador, entonces
        //Voy a necesitar leerlos en algún momento
        return puntosPartidaActual;
    }
}
