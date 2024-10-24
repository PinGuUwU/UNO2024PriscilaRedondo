package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Eventos;
import ar.edu.unlu.poo.uno.model.IPartida;
import ar.edu.unlu.poo.uno.model.Partida;
import ar.edu.unlu.poo.uno.model.Ranking;
import ar.edu.unlu.poo.uno.viewer.vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControladorVista implements IControladorRemoto {
    //100% terminado
    Ranking ranking = new Ranking();
    IPartida iPartida;
    IVista vista;
    public ControladorVista() throws RemoteException {
        iPartida = new Partida();
        iPartida.agregarObserver(ControladorVista.this);
    }
    public void conectar(IVista vista){
        this.vista = vista;
    }
    public boolean mostrarManoJugador() throws RemoteException {
        return iPartida.actualizarCartasVista();
    }
    public boolean mostrarCartaDescarte() throws RemoteException {
        return iPartida.actualizarCartaDescarte();
    }
    public int cantJugadoresConectados(){
        return iPartida.cantJugadores();
    }
    public boolean opcion(String op, String jugador) throws RemoteException {
        return iPartida.tirarCarta(jugador, op);
    }
    public void iniciar() throws RemoteException {
        iPartida.agregarJugadorListo();
    }
    //Aca se hace la ejecución de "comandos"/"Acciones" en el juego
    public String datosJugadorID(String id){
        return ranking.datosJugador(id);
    }
    public void levantarCartaObligatorio(){
        vista.levantarCarta();
    }
    public boolean esSuTurno(String idJ){
        return iPartida.esTurno(idJ);
    }
    public void cambiarColor(String color, String idj) throws RemoteException {
        iPartida.elegirColor(color, idj);
    }
    public boolean agregarJugador(String id){
         return iPartida.agregarJugador(id);
    }
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
        return switch (esp) {
            case 11 -> "Color: " + color + " | Efecto: +2";
            case 12 -> "Color: " + color + " | Efecto: Cambio de sentido";
            case 13 -> "Color: " + color + " | Efecto: Bloqueo";
            case 14 -> "Color: " + color + " | Efecto: +4 y cambio de color";
            case 15 -> "Color: " + color + " | Efecto: Cambio de color";
            default -> "Color: " + color + " | Valor: " + valor;
        };
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.iPartida = (IPartida) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto instanciaModelo, Object cambio) throws RemoteException {
        if(cambio instanceof Eventos){
            switch((Eventos) cambio){
                case INICIAR_PARTIDA -> avisarInicio();
                case CARTA_DESCARTE -> actualizarDescarte();
                case PEDIR_COLOR -> pedirElColor();
                case MOSTRAR_MANO -> actualizarCartasJugador();
                case NUEVA_PARTIDA -> jugadorNoListo();
            }
        }
    }
    public void avisarInicio(){
        vista.avisoInicio();
    }
    public void actualizarDescarte(){
        String[] datos = iPartida.getDescarte();
        vista.setDescarte(datos[0], String.valueOf(datos[1]));
    }
    public void pedirElColor() throws RemoteException {
        vista.pedirCambioColor();
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
            vista.mostrarCartasJugador(colores, valores, posibles);
        }
    }
    public void jugadorNoListo(){
        vista.marcarNoListo();
    }
}
