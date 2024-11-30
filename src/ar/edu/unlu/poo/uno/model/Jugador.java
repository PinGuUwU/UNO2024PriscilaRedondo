package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.Carta;

import java.io.Serializable;

public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id; //id unico
    private String username;
    private int partidasGanadas; //PUEDE QUE NO NECESITE GUARDARLAS, YA QUE ESTAN EN EL ARCHIVO, NO SE
    private int partidasPerdidas;
    private final Mano mano = new Mano();

    public Jugador(String id, String name, String pg, String pp){
        this.id = id;
        username = name;
        partidasGanadas = Integer.parseInt(String.valueOf(pg));
        partidasPerdidas = Integer.parseInt(String.valueOf(pp));
    }

    public void levantarCarta(Carta nuevaCarta){
        mano.recibeCarta(nuevaCarta);
    }

    public void tirarCarta(int pos){
        mano.entregarCarta(pos);
    }

    public void actualizarPartidasPerdidas(){
        partidasPerdidas++;
    }

    public void actualizarPartidasGanadas(){
        partidasGanadas++;
    }

    public int cantCartasMano(){
        return mano.cantCartas();
    }

    public Carta mostrarCarta(int pos){
        return mano.leerCartaMano(pos);
    }

    public Mano mostrarCartas(){ return mano; }

    public String name(){ return this.username; }

    public void actualizarNombre(String name){
        this.username = name;
    }

    public int partidasGanadas(){ return this.partidasGanadas; }

    public int partidasPerdidas(){ return this.partidasPerdidas; }

    public String jugadorID(){ return this.id; }

}