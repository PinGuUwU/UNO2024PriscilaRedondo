package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Partida;
import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.Eventos;
import ar.edu.unlu.poo.uno.model.IPartida;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;
import ar.edu.unlu.poo.uno.viewer.vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControladorVista implements IControladorRemoto, Serializable {
    String idJ;
    IPartida iPartida;
    IVista consola;
    IVista grafica;
    public void conectarJugador(String idJ){
        this.idJ = idJ;
    }
    public void conectarConsola(IVista consola){
        this.consola = consola;
    }
    public void conectarGrafica(IVista grafica){
        this.grafica = grafica;
    }
    public boolean partidaYaInicio() throws RemoteException {
        return iPartida.yaEmpezo();
    }
    public int cantJugadoresConectados() throws RemoteException{
        return iPartida.cantJugadores();
    }
    public boolean opcion(String op) throws RemoteException {
        return iPartida.tirarCarta(idJ, op);
    }
    public void iniciar() throws RemoteException {
        iPartida.agregarJugadorListo();
    }
    //Aca se hace la ejecución de "comandos"/"Acciones" en el juego
    public void levantarCartaObligatorio(){
        grafica.levantarCarta();
        consola.levantarCarta();
    }
    public boolean esSuTurno() throws RemoteException{
        return iPartida.esTurno(this.idJ);
    }
    public void cambiarColor(Color color) throws RemoteException {
        iPartida.elegirColor(color, idJ);
    }
    public boolean agregarJugador() throws RemoteException{
         return iPartida.agregarJugador(idJ);
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
    public String tipo(TipoCarta valor, Color c, int pos) throws RemoteException {
        String color = c.toString();
        String t = "";
        switch (valor) {
            case MAS_DOS -> t ="Color: " + color + " | Efecto: +2";
            case CAMBIO_SENTIDO -> t ="Color: " + color + " | Efecto: Cambio de sentido";
            case BLOQUEO -> t ="Color: " + color + " | Efecto: Bloqueo";
            case MAS_CUATRO -> t ="Color: " + color + " | Efecto: +4 y cambio de color";
            case CAMBIO_COLOR -> t ="Color: " + color + " | Efecto: Cambio de color";
            case COMUN -> {
                t ="Color: " + color + " | Valor: " + obtenerNumero(pos);;
            }
            case VACIA -> t ="Color: " + color;
        };
        return t;
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        //iPartida = new Partida();
        this.iPartida = (IPartida) modeloRemoto;
        iPartida.agregarObserver(ControladorVista.this);
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
        grafica.avisoInicio();
        consola.avisoInicio();
    }
    public void actualizarDescarte() throws RemoteException{
        Color color = iPartida.getColorDescarte();
        TipoCarta tipoCarta = iPartida.getTipoDescarte();
        grafica.setDescarte(color, tipoCarta);
        consola.setDescarte(color, tipoCarta);
    }
    public void pedirElColor() throws RemoteException {
        grafica.pedirCambioColor();
        consola.pedirCambioColor();
    }
    public void actualizarCartasJugador() throws RemoteException {
        ArrayList<Color> colores = iPartida.getColores(idJ);
        ArrayList<TipoCarta> valores = iPartida.getValores(idJ);
        ArrayList<Boolean> posibles = iPartida.getValidas(idJ);
        if(posibles == null){
            //Esto significa que no hay cartas validas para tirar
            levantarCartaObligatorio();
            iPartida.levantarCarta();
        } else {
            grafica.mostrarCartasJugador(colores, valores, posibles);
            consola.mostrarCartasJugador(colores, valores, posibles);
        }
    }
    public int obtenerNumero(int pos) throws RemoteException {
        return iPartida.buscarNumeroCarta(pos, idJ);
    }
    public int obtenerNumeroDescarte() throws RemoteException {
        return iPartida.getNumeroDescarte();
    }
    public void desconectarJugador() throws RemoteException {
        if(iPartida!=null){
            //Si la partida ya se "creo" entonces elimino al jugador de ella
            iPartida.quitarJugador(idJ, ControladorVista.this);
        }
    }
    public String getID(){
        return idJ;
    }
    public void jugadorNoListo(){
        grafica.marcarNoListo();
        consola.marcarNoListo();
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
