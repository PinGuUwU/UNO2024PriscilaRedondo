package ar.edu.unlu.poo.uno.model.clases;

import java.io.Serializable;

public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id; //id unico
    private String username;
    private int partidasGanadas;
    private int partidasPerdidas;
    private int puntosPartidaActual;
    private Mano mano = new Mano();

    public Jugador(String username){
        this.username = username;
    }

    public void levantarCarta(Carta nuevaCarta){
        mano.recibeCarta(nuevaCarta);
    }

    public Carta tirarCarta(int tirar){
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
         esto es si siguiera teniendo un array 17/09
        if(mano.contains(tirar)){ //Si la logica del programa es correcta, luego puedo sacar este if y va a funcionar bien
            mano.remove(tirar);
        }
         */
        return mano.entregarCarta(tirar);
    }
    public void actualizarPartidasPerdidas(){
        partidasPerdidas++;
    }
    public void actualizarPartidasGanadas(){
        partidasGanadas++;
    }
    public int actualizarPuntosActuales(){
        //Caso que hay una clase contador
        Contador cont = new Contador();
        return cont.contar(mano);
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
            (El return puedo hacerlo en actualizarPuntosActuales
    public int puntosActuales(){ //Si puedo actualizar los puntos en el jugador, entonces
        //Voy a necesitar leerlos en algún momento
        return puntosPartidaActual;
    }*/
    public int cantCartasMano(){
        return mano.cantCartas();
    }
    /*
    Muestra la carta de su mano en la pos dada
    Utilidad: Poder leer las cartas para mostrarselas al jugador,
    para corroborar su condicion y marcar al jugador cuales puede tirar y cuales no
    (O decirle si debe levantar obligatoriamente)
     */
    public Carta mostrarCarta(int pos){
        /*if(pos > this.cantCartasMano()){
            return null;
        } else {

        }
        No uso este If porque significaria que en el programa tengo que validar si es null
        o no lo que devuelve
        para eso mejor que el programa compare desde un principio si la longitud supera o no
        a la longitud de la mano que tiene el jugador
        */
        return mano.leerCartaMano(pos);
    }
    public String name(){ return this.username; }
    public int partidasGanadas(){ return this.partidasGanadas; }
    public int partidasPerdidas(){ return this.partidasPerdidas; }
}