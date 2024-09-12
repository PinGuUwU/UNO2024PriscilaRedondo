package ar.edu.unlu.poo.uno.model.clases;

import java.util.ArrayList;

public class Partida {
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private boolean sentido = true;//true = incrementa turno. false = decrementa turno;
    private int turno; //Quizá podría servir hacer una clase que lleve esto
    private MazoDeRobo mazoDeRobo;
    public MazoDeDescarte mazoDeDescarte;
    public Partida(){
        mazoDeDescarte = new MazoDeDescarte();
        mazoDeRobo = new MazoDeRobo();
    }

    public void iniciarTurno(){

    }
    public void levantarCarta(){//Lo mismo que tirar Carta?

    }
    public void tirarCarta(){//Quizá debería devolver la carta para luego mostrarla?

    }
    public void agregarJugador(Jugador jugadorNuevo){ //También podría recibir los datos y crearlo
        //En esta clase, así solo ella conoce su estructura y no el "Main"
        jugadores.add(jugadorNuevo);
        //Si se suma un jugador se resetea la partida?
        //Entonces tendría que hacer:
        //turno=1; y también en el main un "partida = new Partida();"
    }
    public int siguienteTurno(int turnoActual){//Turno del 1-4
        if(sentido){
            if(turnoActual== jugadores.size()){
                return 1;
            } else {
                return turnoActual++;
            }
        } else { //if(!sentido)
            if(turnoActual == 1){
                return jugadores.size();
            } else {
                return turnoActual--;
            }
        }
    }
}
