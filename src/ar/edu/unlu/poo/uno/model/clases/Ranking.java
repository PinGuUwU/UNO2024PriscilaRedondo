package ar.edu.unlu.poo.uno.model.clases;


import java.util.ArrayList;

public class Ranking {
    private String nombreArchivo = "ranking.bat";

    /*Esto controla el archivo binario del historico de jugadores (top ganadas/perdidas)
    Hago un ABM
    -agregar jugador
    -eliminar jugador? no
    -actualizar jugador
    tambien necesito que me de los datos de los top ganadores/perdedores
    -top5Ganadores
    -top5Perdedores

    tambien hacer un metodo que valide si el archivo esta creado,
    si no esta creado lo crea
    si esta creado no lo crea
    */
    private String getNombreArchivo(){ return this.nombreArchivo; }
    public void agregarJugador(Jugador j){

    }
    public ArrayList<Jugador> getTopGanadores(){
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        //Aca leo todos los jugadores del .bat y guardo el top 5
        return jugadores;
    }
    public ArrayList<Jugador> getTopPerdedores(){
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        //Aca leo todos los jugadores del .bat y guardo el top 5
        return jugadores;
    }

}
