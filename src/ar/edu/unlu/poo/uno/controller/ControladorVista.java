package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.cartas.CartaNumerica;
import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.Eventos;
import ar.edu.unlu.poo.uno.model.IPartida;
import ar.edu.unlu.poo.uno.model.Ranking;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;
import ar.edu.unlu.poo.uno.viewer.vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControladorVista implements IControladorRemoto, Serializable {
    Ranking ranking;
    IPartida iPartida;
    IVista vista;
    public void conectar(IVista vista){
        this.vista = vista;
    }
    public boolean mostrarManoJugador() throws RemoteException {
        return iPartida.actualizarCartasVista();
    }
    public boolean mostrarCartaDescarte() throws RemoteException {
        return iPartida.actualizarCartaDescarte();
    }
    public int cantJugadoresConectados() throws RemoteException{
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
    public boolean esSuTurno(String idJ) throws RemoteException{
        return iPartida.esTurno(idJ);
    }
    public void cambiarColor(Color color, String idj) throws RemoteException {
        iPartida.elegirColor(color, idj);
    }
    public boolean agregarJugador(String id) throws RemoteException{
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
    public String tipo(TipoCarta valor, Color c){
        String color = c.toString();
        return switch (valor) {
            case MAS_DOS -> "Color: " + color + " | Efecto: +2";
            case CAMBIO_SENTIDO -> "Color: " + color + " | Efecto: Cambio de sentido";
            case BLOQUEO -> "Color: " + color + " | Efecto: Bloqueo";
            case MAS_CUATRO -> "Color: " + color + " | Efecto: +4 y cambio de color";
            case CAMBIO_COLOR -> "Color: " + color + " | Efecto: Cambio de color";
            case COMUN -> "Color: " + color + " | Valor: " + valor;
            case VACIA -> "Color: " + color;
        };
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        //iPartida = new Partida();
        this.iPartida = (IPartida) modeloRemoto;
        //iPartida.agregarObserver(ControladorVista.this);
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
    public void actualizarDescarte() throws RemoteException{
        vista.setDescarte(iPartida.getColorDescarte(), iPartida.getTipoDescarte());
    }
    public void pedirElColor() throws RemoteException {
        vista.pedirCambioColor();
    }
    public void actualizarCartasJugador() throws RemoteException {
        ArrayList<Color> colores = iPartida.getColores();
        ArrayList<TipoCarta> valores = iPartida.getValores();
        ArrayList<Boolean> posibles = iPartida.getValidas();
        if(posibles == null){
            //Esto significa que no hay cartas validas para tirar
            levantarCartaObligatorio();
            iPartida.levantarCarta();
        } else {
            vista.mostrarCartasJugador(colores, valores, posibles);
        }
    }
    public int obtenerNumero(int pos, String idJ) throws RemoteException {
        return iPartida.buscarNumeroCarta(pos, idJ);
    }
    public int obtenerNumeroDescarte() throws RemoteException {
        return iPartida.getNumeroDescarte();
    }
    public void desconectarJugador(String idJ) throws RemoteException {
        if(iPartida!=null){
            //Si la partida ya se "creo" entonces elimino al jugador de ella
            iPartida.quitarJugador(idJ);
        }
    }
    public void jugadorNoListo(){
        vista.marcarNoListo();
    }
    public Color deStringAColorCarta(String color){
        Color nuevoColor = null;
        switch(color.toLowerCase()){
            case "verde": nuevoColor = Color.VERDE;
            case "rojo": nuevoColor = Color.ROJO;
            case "amarillo": nuevoColor = Color.AMARILLO;
            case "azul": nuevoColor = Color.AZUL;
        }
        return nuevoColor;
    }
}
