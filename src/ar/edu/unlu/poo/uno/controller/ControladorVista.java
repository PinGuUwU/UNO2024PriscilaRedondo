package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Jugador;
import ar.edu.unlu.poo.uno.model.Partida;
import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.Eventos;
import ar.edu.unlu.poo.uno.model.IPartida;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;
import ar.edu.unlu.poo.uno.viewer.vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControladorVista implements IControladorRemoto, Serializable {
    private boolean pidiendoColor = false;
    private boolean yaTiro = false;
    private boolean levanto = false;
    private boolean dijoUNO = false;
    private boolean puedeApelar = false;
    IPartida iPartida;
    ArrayList<IVista> vistas = new ArrayList<>();
    String id;
    public boolean isPidiendoColor(){return pidiendoColor;}
    public void setPidiendoColor(boolean pidiendoColor){ this.pidiendoColor = pidiendoColor; }
    public boolean isLevanto(){
        return levanto;
    }
    public void setLevanto(boolean levanto){
        this.levanto = levanto;
    }
    public boolean isDijoUNO(){
        return dijoUNO;
    }
    public void setDijoUNO(boolean dijoUNO){
        this.dijoUNO = dijoUNO;
    }
    public void conectar(IVista vista){
        vistas.add(vista);
    }
    public void conectarID(String id){
        this.id = id;
    }
    public String idJugador(){
        return this.id;
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
    public int cantJugadoresListos() throws RemoteException {
        return iPartida.cantJugadoresListos();
    }
    public boolean opcion(String op) throws IOException, ClassNotFoundException {
        yaTiro = true;
        return iPartida.tirarCarta(id, op, dijoUNO);
    }
    public void iniciar() throws IOException, ClassNotFoundException {
        for(IVista v: vistas){
            v.esperandoInicio();
        }
        iPartida.agregarJugadorListo();
    }
    public void preguntarSiPasaTurno(){
        for(IVista v: vistas){
            v.mostrarOpcionPasarTurno();
        }
    }
    public void avisarQuePuedeDesafiar() throws RemoteException {
        //Solo debe avisar a quien tiene el turno actual
        for(IVista v: vistas){
            v.puedeDesafiar();
        }
    }
    public void desafiarJugador() throws RemoteException {
        //Aviso a la partida que el jugador del turno actual quiere desafiar al jugador anterior (que tiro el +4)
        iPartida.desafio();
    }
    public void pasarTurno() throws IOException, ClassNotFoundException {
        iPartida.jugadorPaso();
    }
    public void jugadorDesafiado() throws RemoteException {
        iPartida.desafio();
    }
    public void apelarNoDijoUNO() throws RemoteException {
        iPartida.noDijoUNO();
    }
    public void avisarNoDijoUNO() throws RemoteException {
        //Aviso a las vistas que el jugador anterior no dijo uno al tirar su anteúltima carta
        if(!dijoUNO && !esSuTurno()){//Si no soy el jugador que dijo uno, entonces puedo apelar
            for(IVista v: vistas){
                v.avisarQueNoDijoUNO();
            }
        }

    }
    public ArrayList<Jugador> jugadores() throws RemoteException {
        return iPartida.jugadores();
    }
    //Aca se hace la ejecución de "comandos"/"Acciones" en el juego
    public void levantarCarta() throws RemoteException {
        iPartida.levantarCarta();
    }
    public boolean esSuTurno() throws RemoteException{
        boolean turno = iPartida.esTurno(id);
        if(turno){
            yaTiro = false;
            return true;
        } else {
            return false;
        }
    }
    public void cambiarColor(Color color) throws RemoteException {
        iPartida.elegirColor(color, id);
        for(IVista v: vistas){
            /*
            Esto sirve para que una vista grafica le pueda informar a la otra que cambio el color
            Mas que nada porque Interfaz grafica hace algo especifico cuando ya se cambio el color
             */
            v.seCambioElColor();
        }
    }
    public boolean empezoLaPartida() throws RemoteException{
        return iPartida.estadoPartida();
    }
    public boolean agregarJugador(String id) throws IOException, ClassNotFoundException {
         return iPartida.agregarJugador(id);
    }
    public boolean puedoAgregarJugador() throws IOException, ClassNotFoundException {
        return iPartida.agregarJugador(id);
    }
    public void avisarNoDesafia() throws RemoteException {
        for(IVista v: vistas){
            v.noDesafiar();
        }
        iPartida.noDesafia();
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
            case COMUN -> t ="Color: " + color + " | Valor: " + obtenerNumero(pos);
            case VACIA -> t ="Color: " + color;
        };
        return t;
    }
    public void otroJugadorEstaListo() throws RemoteException {
        for(IVista v: vistas){
            v.otroJugadorListo();
        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.iPartida = (IPartida) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto instanciaModelo, Object cambio) throws RemoteException {
        if(cambio instanceof Eventos){
            switch((Eventos) cambio){
                case INICIAR_PARTIDA -> avisarInicio();//No modificar
                case CARTA_DESCARTE -> actualizarDescarte();
                case PEDIR_COLOR -> {if(esSuTurno()) pedirElColor();}
                case MOSTRAR_MANO -> actualizarCartasJugador();//{if(esSuTurno()) actualizarCartasJugador();}
                case NUEVA_PARTIDA -> jugadorNoListo();
                case CAMBIO_JUGADORES -> otroJugadorEstaListo();
                case DESAFIO -> jugadorDesafiado();
                case PUEDE_DESAFIAR -> {if(esSuTurno())avisarQuePuedeDesafiar();}
                //case YA_NO_SE_PUEDE_DESAFIAR -> avisarQueYaNoPuedeDesafiar();
                case NO_DIJO_UNO -> avisarNoDijoUNO();
                //case YA_PASO_UNO -> avisarPasoUNO();
                case CAMBIO -> actualizarCartasJugador();
                case SE_APELO_EL_UNO -> {
                    yaNoSePuedeApelar();
                    actualizarCartasJugador();
                }
            }
        }
    }
    public void yaNoSePuedeApelar(){
        puedeApelar = false;
        for(IVista v: vistas){
            v.yaSeApelo();
        }
    }
    public boolean isPuedeApelar(){
        return puedeApelar;
    }
    public void avisarInicio(){
        for(IVista vista: vistas){
            vista.avisoInicio();
        }
    }
    public void actualizarDescarte() throws RemoteException{
        for(IVista vista: vistas){
            vista.setDescarte(iPartida.getColorDescarte(), iPartida.getTipoDescarte());
        }
    }


    public void pedirElColor() throws RemoteException {
        for(IVista vista: vistas){
            vista.pedirCambioColor();
        }
    }
    public void actualizarCartasJugador() throws RemoteException {
        ArrayList<Color> colores = iPartida.getColores(id);
        ArrayList<TipoCarta> valores = iPartida.getValores(id);
        ArrayList<Boolean> posibles = iPartida.getValidas(id);
        for(IVista vista: vistas) {
            vista.mostrarCartasJugador(colores, valores, posibles);
        }
    }
    public int obtenerNumero(int pos) throws RemoteException {
        return iPartida.buscarNumeroCarta(pos, id);
    }
    public int obtenerNumeroDescarte() throws RemoteException {
        try{
            return iPartida.getNumeroDescarte();
        } catch(NullPointerException e){
            e.printStackTrace();
            return 0;
        }
    }
    public void desconectarJugador() throws IOException, ClassNotFoundException {
        if(iPartida!=null){
            //Si la partida ya se "creo" entonces elimino al jugador de ella
            iPartida.quitarJugador(id);
        }
    }
    public void cargarNuevaPartida(Partida p) throws RemoteException {
        iPartida.cargarPartida(p);
    }
    public void jugadorNoListo(){
        for(IVista vista: vistas){
            vista.marcarNoListo();
        }
        levanto = false;
        pidiendoColor = false;
        dijoUNO = false;
    }
    public void guardarPartidaActual() throws IOException, ClassNotFoundException {
        iPartida.guardarPartida();
    }
    public boolean isYaTiro(){
        return yaTiro;
    }
    public Color deStringAColorCarta(String color){
        return switch (color.toLowerCase()) {
            case "verde" -> Color.VERDE;
            case "rojo" -> Color.ROJO;
            case "amarillo" -> Color.AMARILLO;
            case "azul" -> Color.AZUL;
            default -> Color.INVALIDO;
        };
    }

    public void noPuedeLevantar() throws RemoteException {
        for(IVista v: vistas){
            v.yaLevanto();
        }
    }
}
