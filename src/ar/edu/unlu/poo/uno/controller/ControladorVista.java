package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Jugador;
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
    private String id;

    //Setters y getters

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

    public ArrayList<Jugador> jugadores() throws RemoteException {
        //Retorno la lista de jugadores que están conectados en la partida actual
        return iPartida.jugadores();
    }




    //MÉTODOS DE OBSERVER

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.iPartida = (IPartida) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto instanciaModelo, Object cambio) throws RemoteException {
        //Manejo de eventos que manda el Modelo (Partida) al Controlador (ControladorVista)
        if(cambio instanceof Eventos){
            switch((Eventos) cambio){
                case INICIAR_PARTIDA -> actualizarInicio();
                case CARTA_DESCARTE -> actualizarDescarte();
                case PEDIR_COLOR -> {if(esSuTurno()) pedirElColor();}
                case MOSTRAR_MANO -> actualizarCartasJugador();
                case NUEVA_PARTIDA -> jugadorNoListo();
                case CAMBIO_JUGADORES -> otroJugadorEstaListo();
                case PUEDE_DESAFIAR -> {if(esSuTurno()) actualizarQuePuedeDesafiar();}
                case NO_DIJO_UNO -> actualizarNoDijoUNO();
                case CAMBIO -> actualizarCartasJugador();
                case SE_APELO_EL_UNO -> {
                    yaNoSePuedeApelar();
                    actualizarCartasJugador();
                }
                case CARGAR_PARTIDA -> {
                    actualizarDescarte();
                    actualizarCartasJugador();
                }
            }
        }
    }


    //CONEXIÓN Y DESCONEXIÓN DE JUGADORES

    public void desconectarJugador() throws IOException, ClassNotFoundException {
        if(iPartida!=null){
            //Elimino al jugador de la partida actual
            iPartida.quitarJugador(id);
        }
    }

    public boolean agregarJugador(String id) throws IOException, ClassNotFoundException {
        //Agrego un jugador a la partida y retorno si se pudo agregar con éxito para manejar errores
        return iPartida.agregarJugador(id);
    }

    //GUARDADO Y CARGADO DE PARTIDAS

    public void cargarNuevaPartida(long id) throws IOException {
        //Le pido a la partida que cargue la partida con el id pasado por parámetro
        iPartida.cargarPartida(id);
    }

    public void guardarPartidaActual() throws IOException, ClassNotFoundException {
        //Le avisa a la partida que debe guardarse
        iPartida.guardarPartida();
    }



    //MANEJO DE EVENTOS

    public void actualizarInicio(){
        //Aviso a las vistas que se va a iniciar la partida
        for(IVista vista: vistas){
            vista.avisoInicio();
        }
    }

    public void actualizarDescarte() throws RemoteException{
        //Aviso a las vistas que actualice la carta de descarte que muestra
        for(IVista vista: vistas){
            vista.setDescarte(iPartida.getColorDescarte(), iPartida.getTipoDescarte());
        }
    }

    public void pedirElColor() throws RemoteException {
        //Avisa a la vista que debe pedirle al jugador que ingrese un color
        for(IVista vista: vistas){
            vista.pedirCambioColor();
        }
    }

    public void actualizarCartasJugador() throws RemoteException {
        //aviso a las vistas que actualicen las cartas que muestran
        ArrayList<Color> colores = iPartida.getColores(id);
        ArrayList<TipoCarta> valores = iPartida.getValores(id);
        ArrayList<Boolean> posibles = iPartida.getValidas(id);
        for(IVista vista: vistas) {
            vista.mostrarCartasJugador(colores, valores, posibles);
        }
    }

    public void jugadorNoListo(){
        //Avisa a las vistas que deben actualizarse para mostrar que ya no están en medio de una partida
        for(IVista vista: vistas){
            vista.marcarNoListo();
        }
        levanto = false;
        pidiendoColor = false;
        dijoUNO = false;
    }

    public void otroJugadorEstaListo() throws RemoteException {
        //Aviso a las vistas que le muestren al jugador cuántos jugadores están listos de todos los que están conectados
        for(IVista v: vistas){
            v.otroJugadorListo();
        }
    }

    public void actualizarQuePuedeDesafiar() throws RemoteException {
        //Aviso al jugador con el turno actual que puede desafiar a la persona que le tiró un +4
        for(IVista v: vistas){
            v.puedeDesafiar();
        }
    }

    public void actualizarNoDijoUNO() throws RemoteException {
        //Aviso a las vistas que el jugador anterior no dijo uno al tirar su anteúltima carta
        if(!dijoUNO && !esSuTurno()){//Si no soy el jugador que dijo uno, entonces puedo apelar
            for(IVista v: vistas){
                v.avisarQueNoDijoUNO();
            }
        }

    }

    public void yaNoSePuedeApelar(){
        //Avisa a las vistas que avisen al jugador que ya no se puede apelar
        puedeApelar = false;
        for(IVista v: vistas){
            v.yaSeApelo();
        }
    }

    //MÉTODOS ÚNICOS PARA LA CONSOLA

    public String tipo(TipoCarta valor, Color c, int pos) throws RemoteException {
        //Método utilizado para pasar un TipoCarta a un String para mostrarlo en la consola
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

    public boolean esNumero(String valor){
        //Retorna si es un número cierto comando ingresado en la Consola
        boolean resultado;
        try{
            Integer.parseInt(valor);
            resultado = true;
        } catch(NumberFormatException e){
            resultado = false;
        }
        return resultado;
    }

    public boolean mostrarManoJugador() throws RemoteException {
        //Le pido a la partida que actualice la mano de jugador (si es que la partida ya se inició)
        return iPartida.actualizarCartasVista();
    }

    public boolean mostrarCartaDescarte() throws RemoteException {
        //Le pido a la partida que actualice la carta de descarte (si es que la partida ya se inició)
        return iPartida.actualizarCartaDescarte();
    }


    //MÉTODOS PARA IVISTA

    public boolean isYaTiro(){
        //Devuelve el valor del atributo yaTiro
        return yaTiro;
    }

    public void noPuedeLevantar() throws RemoteException {
        //Avisa a las vistas que ya levantaron una carta, así que ya no pueden levantar otra
        for(IVista v: vistas){
            v.yaLevanto();
        }
    }

    public Color deStringAColorCarta(String color){
        //Método utilizado para padar un string a un Color
        return switch (color.toLowerCase()) {
            case "verde" -> Color.VERDE;
            case "rojo" -> Color.ROJO;
            case "amarillo" -> Color.AMARILLO;
            case "azul" -> Color.AZUL;
            default -> Color.INVALIDO;
        };
    }

    public int obtenerNumero(int pos) throws RemoteException {
        //Devuelvo el número de una carta en cierta posición
        return iPartida.buscarNumeroCarta(pos, id);
    }

    public int obtenerNumeroDescarte() throws RemoteException {
        //Devuelve el número de la carta de descarte, en caso de ser CartaNumerica
        try{
            return iPartida.getNumeroDescarte();
        } catch(NullPointerException e){
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isPuedeApelar(){
        //Devuelve el valor de la variable puedeApelar
        return puedeApelar;
    }

    public void actualizarNoDesafia() throws RemoteException {
        //Avisa a la vista que el jugador decidió no desafiar
        for(IVista v: vistas){
            v.noDesafiar();
        }
        iPartida.noDesafia();
    }

    public boolean puedoAgregarJugador() throws IOException, ClassNotFoundException {
        //Pregunto a la partida si un jugador se puede agregar o si la partida ya está completa
        return iPartida.agregarJugador(id);
    }

    public boolean empezoLaPartida() throws RemoteException{
        //Pregunto a la partida si empezó la partida
        return iPartida.estadoPartida();
    }

    public void levantarCarta() throws RemoteException {
        //Le aviso a la partida que el jugador con el turno actual levantó una carta
        iPartida.levantarCarta();
    }

    public boolean esSuTurno() throws RemoteException{
        //Preguto a la partida si es el turno del jugador que corresponde a este controlador
        boolean turno = iPartida.esTurno(id);
        if(turno){
            yaTiro = false;
            return true;
        } else {
            return false;
        }
    }

    public void cambiarColor(Color color) throws RemoteException {
        //Aviso a la partida que el jugador hizo un cambio de color
        iPartida.elegirColor(color, id);
        for(IVista v: vistas){
            /*
            Esto sirve para que una vista grafica le pueda informar a la otra que cambio el color
            Mas que nada porque Interfaz grafica hace algo especifico cuando ya se cambio el color
             */
            v.seCambioElColor();
        }
    }

    public void desafiarJugador() throws RemoteException {
        //Aviso a la partida que el jugador del turno actual quiere desafiar al jugador anterior (que tiro el +4)
        iPartida.desafio();
    }

    public void pasarTurno() throws IOException, ClassNotFoundException {
        //Aviso a la partida que el jugador con el turno actual paso su turno
        iPartida.jugadorPaso();
    }

    public void apelarNoDijoUNO() throws RemoteException {
        //Aviso a la partida que un jugador apelo que alguien no dijo UNO
        iPartida.noDijoUNO();
    }

    public int cantJugadoresConectados() throws RemoteException{
        //Retorno la cantidad de jugadores conectados a la partida actual
        return iPartida.cantJugadores();
    }

    public int cantJugadoresListos() throws RemoteException {
        //Retorno la cantidad de jugadores listos que hay en la partida actual
        return iPartida.cantJugadoresListos();
    }

    public boolean opcion(String op) throws IOException, ClassNotFoundException {
        //Tiro una carta y le aviso a la partida
        yaTiro = true;
        return iPartida.tirarCarta(id, op, dijoUNO);
    }

    public void jugadorInicio() throws IOException, ClassNotFoundException {
        //Aviso a las vistas que el jugador está listo para iniciar la partida
        for(IVista v: vistas){
            v.esperandoInicio();
        }
        //Le aviso a la partida que un jugador está listo
        iPartida.agregarJugadorListo();
    }

    public void actualizarPuedePasarTurno(){
        //Aviso a la vista que le muestre al jugador que puede pasar el tunro
        for(IVista v: vistas){
            v.mostrarOpcionPasarTurno();
        }
    }

    public boolean buscarJugadorPorID(String idJ) throws RemoteException {
        return iPartida.existeJugadorPorID(idJ);
    }

}
