package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.clases.*;
import ar.edu.unlu.poo.uno.viewer.vista.VistaConsola;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControladorVista implements IControladorRemoto {
    Ranking ranking = new Ranking();
    IPartida iPartida;
    VistaConsola consola;
    public ControladorVista() throws RemoteException {
        iPartida = new Partida();
        iPartida.agregarObserver(ControladorVista.this);
    }
    public void conectar(VistaConsola consola){
        this.consola = consola;
    }

    public boolean opcion(String op, String jugador) throws RemoteException {
        return iPartida.tirarCarta(jugador, op);
    }
    public void pedirElColor(){
        consola.pedirCambioColor();
    }
    public void iniciar() throws RemoteException {
        iPartida.agregarJugadorListo();
    }
    //Aca se hace la ejecución de "comandos"/"Acciones" en el juego
    public String datosJugadorID(String id){
        return ranking.datosJugador(id);
    }
    public void levantarCartaObligatorio(){
        consola.levantarCarta();
    }
    public boolean esSuTurno(String idJ){
        return iPartida.esTurno(idJ);
    }
    public void cambiarColor(String color, String idj) throws RemoteException {
        iPartida.elegirColor(color, idj);
    }

    public void agregarJugador(String id){
         iPartida.agregarJugador(id);
    }

    public String buscarIdName(String id){
        String[] datos = ranking.buscarDatosJugadorID(id);
        return datos[1];
    } //retorna username


    public boolean esNumero(String valor){
        boolean resultado;
        try{
            Integer.parseInt(valor);
            resultado = true;
        } catch(NumberFormatException e){
            resultado = false;
        }
        return resultado;
    }

    public String tipo(String valor, String color){
        int esp = Integer.parseInt(valor);
        String t = "";
        switch(esp){
            case 11: t = color + " (+2) ";
                break;
            case 12: t = color + " (cambio de sentido) ";
                break;
            case 13: t = color + " (bloqueo) ";
                break;
            case 14: t = "+4 (cambio de color)";
                break;
            case 15: t = "cambio de color";
                break;
            default: t = null;
                break;
        }
        return t;
    }
    public boolean esEspecial(String valor){
        return Integer.parseInt(valor)>13;
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.iPartida = (IPartida) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto instanciaModelo, Object cambio) throws RemoteException {
        if(cambio instanceof Eventos){
            switch((Eventos) cambio){
                case INICIAR_PARTIDA -> consola.avisoInicio();
                case CARTA_DESCARTE -> actualizarDescarte(iPartida.getDescarte());
                case PEDIR_COLOR -> pedirElColor();
                case MOSTRAR_MANO -> actualizarCartasJugador();
            }
        }
    }
    public void actualizarCartasJugador() throws RemoteException {
        ArrayList<String> colores = iPartida.getColores();
        ArrayList<String> valores = iPartida.getValores();
        ArrayList<Boolean> posibles = iPartida.getValidas();
        if(posibles == null){
            //Esto significa que no hay cartas validas para tirar
            levantarCartaObligatorio();
            iPartida.levantarCarta();
        } else {
            consola.mostrarCartasJugador(colores, valores, posibles);
        }
    }
    public void actualizarDescarte(String[] datos){
        consola.setDescarte(datos[0], String.valueOf(datos[1]));
    }
    /*Qué cosas debe notificar la partida a la vista?
    -Mostrar cartas en mano del jugador
    -mostrar la carta de descarte a todos
    -mostrar de quien es el turno actual
    -iniciar la partida
     */
    public boolean mostrarManoJugador() throws RemoteException {
        return iPartida.actualizarCartasVista();
    }
    public boolean mostrarCartaDescarte() throws RemoteException {
        return iPartida.actualizarCartaDescarte();
    }
}
