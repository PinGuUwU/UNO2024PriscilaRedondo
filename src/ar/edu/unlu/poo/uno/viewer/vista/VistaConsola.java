package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.poo.uno.listener.VentanaListener;
import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaConsola implements VentanaListener, IVista, Serializable {
    private final boolean puedeJugar;
    private boolean desafiar = false;
    private boolean listo = false;
    private boolean puedeDecirUNO = false;
    ControladorVista controlador;
    VentanaListener listener;
    JFrame frame;
    VistaRanking iRanking;
    VistaPerfil iPerfil;
    VistaPartidaGuardada iPartidasGuardadas;
    private JButton enter;
    private JTextField entradaDeTexto;
    private JTextArea consola;
    private JPanel barra;
    private JButton perfil;
    private JButton ranking;
    private JPanel principal;
    private JButton partidas;

    public VistaConsola(VentanaListener listener,ControladorVista controlador) throws IOException, ClassNotFoundException {
        this.controlador = controlador;
        this.listener = listener;
        if(!controlador.puedoAgregarJugador()){//Si no se puede agregar jugador
            consola.append("        Debe entrar a otra partida, esta ya está llena.\n\n");
            puedeJugar = false;
        } else {//Solo doy el aviso, no voy a manejar nada mas
            puedeJugar = true;
            iniciar();
        }
    }

    private void agregarListeners(){
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a = entradaDeTexto.getText();
                consola.append(a);
                consola.append("\n");
                entradaDeTexto.setText("");
                try {
                    buscarComando(a);
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        perfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iPerfil == null){
                    iPerfil = new VistaPerfil(VistaConsola.this, controlador.idJugador());
                } else if(!iPerfil.frame.isVisible()){
                    iPerfil.frame.setVisible(true);
                    iPerfil.setInTop();
                }
                try {
                    iPerfil.actualizarPerfil(controlador.idJugador());
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ranking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iRanking == null){
                    iRanking = new VistaRanking(VistaConsola.this);
                } else if(!iRanking.frame.isVisible()){
                    iRanking.frame.setVisible(true);
                    iRanking.setInTop();
                }
            }
        });
        partidas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iPartidasGuardadas == null){
                    try {
                        iPartidasGuardadas = new VistaPartidaGuardada(VistaConsola.this, controlador.idJugador(), controlador);
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if(!iPartidasGuardadas.frame.isVisible()){
                    iPartidasGuardadas.frame.setVisible(true);
                }
            }
        });
    }

    private void bienvenida(){
        consola.selectAll();
        consola.replaceSelection("");
        consola.append("            Bienvenido al juego del UNO \nSi quieres saber los comandos ingresa '/help'\n");
        consola.append("            Para iniciar la partida coloca '/iniciar'\n");
    }

    private void buscarComando(String comando) throws IOException, ClassNotFoundException {
        if(!puedeJugar){ //Si no puede jugar no lo dejo ingresar nunca nada, debe si o si cambiar de server/puerto o no sé como funciona RMI con eso
            consola.append("        Debe entrar a otra partida, esta ya está llena.\n\n");
        } else {
            switch (comando.toLowerCase()) {
                //case "/uno" -> controlador.decirUNO();
                case "/iniciar" -> {
                    consola.append("\n");
                    if (!listo) {
                        marcarListo();
                        controlador.jugadorInicio();
                    } else {
                        consola.append("Ya se ha confirmado su particitación, espere a los demás jugadores\n\n");
                    }
                    consola.append("\n");
                }
                case "/robar" -> {
                    if(desafiar){
                        controlador.actualizarNoDesafia();
                    }
                    if(controlador.isDijoUNO()){
                        controlador.setDijoUNO(false);
                        //Si había dicho uno anteriormente pero ahora levanto una carta, entonces tendrá que volver a decir uno si fuera el caso
                    }
                    if(!controlador.empezoLaPartida()){
                        consola.append("\nAún no empezó la partida");
                    } else {
                        if(controlador.esSuTurno() && !controlador.isLevanto()){
                            controlador.setLevanto(true);
                            controlador.levantarCarta();
                            controlador.actualizarPuedePasarTurno();
                        } else if(controlador.isLevanto()){
                            controlador.noPuedeLevantar();
                            consola.append("\nSolo puede levantar una carta por turno, si quiere puede pasar con '/pasar'.\n");
                        } else {
                            consola.append("\nDebe esperar a que sea su turno para levantar una carta.\n");
                        }
                    }

                }
                case "/help" -> mostrarComandos();
                case "/pasar" -> {
                    if(controlador.esSuTurno()){
                        pasarTurno();
                    } else {
                        consola.append("Es el turno de otro jugador.");
                    }
                }
                case "/ultimacarta" -> mostrarUltimaCarta();
                case "/mostrarmano" -> mostrarManoJugador();
                case "/cantjugadores" -> mostrarCantJugadores();
                case "/desafiar" -> desafiarJugadorAnterior();
                case "/uno" -> {
                    if(puedeDecirUNO) decirUNO();
                }
                case "/apelaruno" -> {
                    if(controlador.isPuedeApelar())apelarUNO();
                }
                case "/nodesafiar" -> controlador.actualizarNoDesafia();
                default -> {
                    if (controlador.esNumero(comando) && !controlador.isPidiendoColor() && controlador.esSuTurno()) {
                        //Si le toca pero no debe ingresar color
                        boolean seTiro = controlador.opcion(comando);
                        if (!seTiro) {
                            consola.append("No se pudo tirar la carta, elija una que sea posible tirar.\n\n");
                        }
                        desafiar = false;
                    } else if (controlador.isPidiendoColor()) { //Si ingreso y le toca ingresar color
                        isColor(comando);
                    } else {
                        consola.append("No se reconoce el comando ingresado, ingrese algo válido o revise el /help\n\n");
                    }
                }
            }
        }
    }

    private void isColor(String comando) throws RemoteException {
        //Para saber si un comando es un color valido o no
        Color color = controlador.deStringAColorCarta(comando.toLowerCase());
        switch (color) {
            case VERDE, ROJO, AMARILLO, AZUL -> {
                controlador.cambiarColor(color);
                controlador.setPidiendoColor(false);
            }
            case INVALIDO -> {
                consola.append("\nIngrese un color válido de la lista.\n\n");
                pedirCambioColor();
            }
        }
    }

    public void seCambioElColor(){
        consola.append("\nSe cambio el color exitosamente.\n");
        controlador.setPidiendoColor(false);
    }

    private void mostrarComandos(){
        consola.append("            Lista de comandos:\n1-'/ultimaCarta' -- para mostrar la ultima carta descartada.\n");
        consola.append("2-'/mostrarMano' -- para mostrar tu mano de cartas.\n");
        consola.append("3-'/robar' -- para levantar una carta.\n");
        consola.append("4-'/cantJugadores' -- para mostrar la cantidad de jugadores que hay en la partida actual.\n");
        consola.append("5-'/iniciar' -- para iniciar la partida.\n");
        consola.append("6-'/help' -- para mostrar todos los comandos.\n");
        consola.append("7-'/pasar' -- para pasar el turno (solo si es tu turno).\n");
        consola.append("8-'/uno' -- para decir uno. Solo si cumple las condiciones para hacerlo.\n");
        consola.append("9-'/apelaruno' -- para apelar que un jugador no dijo uno. Solo si cumple las condiciones para hacerlo.\n");
        consola.append("10-'/desafiar' -- para desafiar a un jugador que le tiró un +4. Solo si cumple las condiciones para hacerlo.\n");
        consola.append("11-'/nodesafiar' -- para no desafiar a un jugador que le tiró un +4. Solo si cumple las condiciones para hacerlo.\n");
    }

    private void mostrarUltimaCarta() throws RemoteException {
        if(!controlador.mostrarCartaDescarte()){
            consola.append("        Debes esperar que inicie la partida.\n\n");
            consola.append("            Para iniciar la partida coloca '/iniciar'\n");
        } else {
            controlador.actualizarDescarte();
        }
    }

    private void mostrarManoJugador() throws RemoteException {
        if(!controlador.mostrarManoJugador()){
            consola.append("        Debes esperar que inicie la partida.\n\n");
            consola.append("            Para iniciar la partida coloca '/iniciar'\n");
        } else {
            controlador.actualizarCartasJugador();
        }
    }

    public void apelarUNO() throws RemoteException {
        controlador.apelarNoDijoUNO();
    }

    private void mostrarCantJugadores() throws RemoteException{
        consola.append("        La cantidad de jugadores conectados a esta partida son: " + cantJugadoresTotal() + ".\n\n");
    }

    //Métodos IVista

    @Override
    public void marcarNoListo(){
        listo = false;
        bienvenida();
    }

    @Override
    public int cantJugadoresListos() throws RemoteException {
        return controlador.cantJugadoresListos();
    }

    @Override
    public int cantJugadoresTotal() throws RemoteException {
        return controlador.cantJugadoresConectados();
    }

    @Override
    public void decirUNO() {
        controlador.setDijoUNO(true);
        consola.append("\nDijiste uno");
    }

    @Override
    public void yaLevanto(){
        controlador.setLevanto(true);
    }

    @Override
    public void desafiarJugadorAnterior() throws RemoteException {
        desafiar = false;
        controlador.desafiarJugador();
    }

    @Override
    public void avisarQueNoDijoUNO(){
        consola.append("\nEl último jugador no dijo uno, escriba '/apelaruno' para apelar a tiempo.\n");
    }

    @Override
    public void puedeDesafiar(){
        desafiar = true;
        consola.append("\n  El jugador anterior tiro un +4, si quiere desafiarlo ingrese /desafiar (Leer reglamento de desafio).\n");
    }

    @Override
    public void pasarTurno() throws IOException, ClassNotFoundException {
        controlador.setLevanto(false);
        controlador.pasarTurno();
    }

    @Override
    public void mostrarOpcionPasarTurno() {
        consola.append("\nPara pasar turno '/pasar'.\n");
    }

    @Override
    public void yaSeApelo(){
        consola.append("\nYa no se puede apelar por el jugador que no dijo UNO.\n");
    }

    @Override
    public void onVentanaCerrada(String ventana) {
        if(ventana.equalsIgnoreCase("perfil")){
            iPerfil = null;
        } else if(ventana.equalsIgnoreCase("ranking")){
            iRanking = null;
        } else if(ventana.equalsIgnoreCase("partidasguardadas")){
            iPartidasGuardadas = null;
        }
    }

    @Override
    public void noDesafiar(){
        desafiar = false;
    }

    @Override
    public void marcarListo() {
        listo = true;
    }

    @Override
    public void setDescarte(Color color, TipoCarta valor) throws RemoteException {
        if(valor == TipoCarta.COMUN){
            int v = controlador.obtenerNumeroDescarte();
            consola.append("            Ultima carta tirada: " + color.toString() + " " + v + "\n");
        } else {
            consola.append("            Ultima carta tirada: " + color.toString() + " " + valor.toString() + "\n");
        }
    }

    @Override
    public void mostrarCartasJugador(ArrayList<Color> colores, ArrayList<TipoCarta> valores, ArrayList<Boolean> posibles) throws RemoteException {
        String carta;
        consola.append("    Tu mano de cartas es: \n");
        boolean cartasValidas = false;
        for(int i=0; i<colores.size(); i++){
            carta = "Carta " + (i+1) + " | " + (controlador.tipo(valores.get(i), colores.get(i), i));
            if(posibles.get(i)){
                cartasValidas = true;
                carta += " | ('SI' se puede tirar)";
            } else {
                carta += " | ('NO' se puede tirar)";
            }
            consola.append(carta + "\n");
        }
        if(cartasValidas && controlador.esSuTurno()){
            consola.append("\n\n");
            consola.append("        Si quiere puede pasar su turno con '/pasar' o robar con '/robar'.\n");
            consola.append("    Ingrese el nro de la carta que desee tirar:\n");
        } else if(!cartasValidas){
            consola.append("\nNo tiene cartas que pueda tirar, robe una con '/robar'.\n");
        } else if(!controlador.esSuTurno()){
            consola.append("\nEs el turno de otro jugador. Esperando turno.\n");
        }

        if(valores.size() == 2 && (posibles.get(0) || posibles.get(1)) && controlador.esSuTurno() && !controlador.isYaTiro()){
            puedeDecirUNO = true;
        } else {
            puedeDecirUNO = false;
        }

    }

    @Override
    public void pedirCambioColor() throws RemoteException {
        controlador.setPidiendoColor(true);
        mostrarManoJugador();
        consola.append("Ingrese el color al que desea cambiar. opciones:\n      Verde\n     Azul\n      Amarillo\n      Rojo\n");
    }

    @Override
    public void avisoInicio(){
        marcarListo();
        partidas.setEnabled(true);
        consola.append("\nTodos los jugadores están listos, la partida está por comenzar.\n\n\n");
    }

    @Override
    public void iniciar() {
        //Inicializo la ventana
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);
        consola.setFocusable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    try {
                        listener.onVentanaCerrada("consola");
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.setVisible(false);
                }
            }
        });

        agregarListeners();

        frame.add(principal);
        bienvenida();
        consola.setForeground(java.awt.Color.decode("#873600"));
        consola.setFont(new Font("Arial", Font.PLAIN, 19));

        frame.setVisible(true);
    }

    @Override
    public void otroJugadorListo() throws RemoteException {
        consola.append("Uno de los jugadores ya está listo.("+cantJugadoresListos()+"/"+cantJugadoresTotal()+")");
    }

    @Override
    public void esperandoInicio() throws RemoteException {
        if(!controlador.empezoLaPartida()){
            consola.append("\n      Se te ha agregado a la partida. Espere a que todos los jugadores estén listos\n\n");
        }
    }

}