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
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaConsola implements VentanaListener, IVista, Serializable {
    private final boolean puedeJugar;
    private boolean listo = false;
    private boolean pidiendoColor = false;
    private final String idJugador;
    ControladorVista controlador;
    VentanaListener listener;
    JFrame frame;
    VistaRanking iRanking;
    VistaPerfil iPerfil;
    private JButton enter;
    private JTextField entradaDeTexto;
    private JTextArea consola;
    private JPanel barra;
    private JButton perfil;
    private JButton ranking;
    private JPanel principal;

    public VistaConsola(VentanaListener listener, String idJugador,ControladorVista controlador) throws RemoteException {
        this.controlador = controlador;
        consola.setFocusable(false);
        this.idJugador = idJugador;
        this.listener = listener;
        if(!controlador.agregarJugador(idJugador)){//Si no se puede agregar jugador
            consola.append("        Debe entrar a otra partida, esta ya está llena.\n\n");
            puedeJugar = false;
        } else {//Solo doy el aviso, no voy a manejar nada mas
            puedeJugar = true;
            iniciar();
        }
    }
    @Override
    public void iniciar() {
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    try {
                        listener.onVentanaCerrada("consola");
                    } catch (RemoteException ex) {
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
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });//Tanto enter como entrada de texto deberían enviar el comando al main
        entradaDeTexto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a = entradaDeTexto.getText();
                consola.append(a);
                consola.append("\n");
                entradaDeTexto.setText("");
                try {
                    buscarComando(a);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        perfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iPerfil == null){
                    iPerfil = new VistaPerfil(VistaConsola.this, idJugador);
                } else if(!iPerfil.frame.isVisible()){
                    iPerfil.frame.setVisible(true);
                    iPerfil.setInTop();
                }
                iPerfil.actualizarPerfil(idJugador);
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
                    //Tengo qeu volver a agregar el item "elige opcion"
                }
            }
        });
    }
    private void bienvenida(){
        consola.append("            Bienvenido al juego del UNO \nSi quieres saber los comandos ingresa '/help'\n");
        consola.append("            Para iniciar la partida coloca '/iniciar'\n");
    }
    private void buscarComando(String comando) throws RemoteException {
        if(!puedeJugar){ //Si no puede jugar no lo dejo ingresar nunca nada, debe si o si cambiar de server/puerto o no sé como funciona RMI con eso
            consola.append("        Debe entrar a otra partida, esta ya está llena.\n\n");
        } else {
            switch (comando.toLowerCase()) {
                case "/iniciar" -> {
                    consola.append("\n");
                    if (!listo) {
                        marcarListo();
                        //Aca debería corroborar que todos los jugadores hayan puesto /iniciar
                        consola.append("\n      Se te ha agregado a la partida. Espere a que todos los jugadores estén listos\n\n");
                        controlador.iniciar();
                        //ac á a su vez tengo que avisarle a las demás consolas que hay x/4 listos
                    } else {
                        consola.append("Ya se ha confirmado su particitación, espere a los demás jugadores\n\n");
                    }
                    consola.append("\n");
                }
                case "/help" -> mostrarComandos();
                case "/ultimacarta" -> mostrarUltimaCarta();
                case "/mostrarmano" -> mostrarManoJugador();
                case "/cantjugadores" -> mostrarCantJugadores();
                default -> {
                    if (controlador.esNumero(comando) && !pidiendoColor && controlador.esSuTurno(idJugador)) {
                        //Si le toca pero no debe ingresar color
                        boolean seTiro = controlador.opcion(comando, idJugador);
                        if (!seTiro) {
                            consola.append("No se pudo tirar la carta, elija una que sea posible tirar.\n\n");
                        }
                    } else if (pidiendoColor) { //Si ingreso y le toca ingresar color
                        isColor(comando);
                    } else {
                        consola.append("No se reconoce el comando ingresado, ingrese algo válido o revise el /help\n\n");
                    }
                }
            }
        }
    }

    @Override
    public void onVentanaCerrada(String ventana) {
        if(ventana.equalsIgnoreCase("perfil")){
            iPerfil = null;
        } else if(ventana.equalsIgnoreCase("ranking")){
            iRanking = null;
        }
    }

    @Override
    public void marcarListo() {
        listo = true;
    }

    @Override
    public void setDescarte(Color color, TipoCarta valor) throws RemoteException {
        String esp = controlador.tipo(valor, color, -1, idJugador);
        if(esp != null){
            consola.append("            Ultima carta tirada: " + esp + "\n");
        } else {
            consola.append("            Ultima carta tirada: " + color.toString() + " " + valor.toString() + "\n");
        }
    }

    @Override
    public void mostrarCartasJugador(ArrayList<Color> colores, ArrayList<TipoCarta> valores, ArrayList<Boolean> posibles) throws RemoteException {
        String carta;
        consola.append("    Tu mano de cartas es: \n");
        for(int i=0; i<colores.size(); i++){
            carta = "Carta " + i + " | " + (controlador.tipo(valores.get(i), colores.get(i), i, idJugador));
            if(posibles.get(i)){
                carta += " | ('SI' se puede tirar)";
            } else {
                carta += " | ('NO' se puede tirar)";
            }
            consola.append(carta + "\n");
        }
        consola.append("\n\n");
        consola.append("    Ingrese el nro de la carta que desee tirar:\n");

    }
    @Override
    public void pedirCambioColor() throws RemoteException {
        pidiendoColor = true;
        mostrarManoJugador();
        consola.append("Ingrese el color al que desea cambiar. opciones:\n      Verde\n     Azul\n      Amarillo\n      Rojo\n");
    }
    @Override
    public void avisoInicio(){
        marcarListo();
        consola.append("\nTodos los jugadores están listos, la partida está por comenzar.\n\n\n");
    }
    @Override
    public void levantarCarta(){
        consola.append("\nLas cartas que tiene no puede tirarlas, automaticamente levantará una del mazo de descarte hasta que alguna\nsea válida para tirar.\n");
    }
    private void isColor(String comando) throws RemoteException {
        Color color = controlador.deStringAColorCarta(comando.toLowerCase());
        switch (color) {
            case VERDE, ROJO, AMARILLO, AZUL -> {
                controlador.cambiarColor(color, idJugador);
                pidiendoColor = false;
            }
            default -> {
                consola.append("\nIngrese un color válido de la lista.\n\n");
                pedirCambioColor();
            }
        }
    }


    private void mostrarComandos(){
        consola.append("            Lista de comandos:\n1-'/ultimaCarta' -- para mostrar la ultima carta descartada.\n");
        consola.append("2-'/mostrarMano' -- para mostrar tu mano de cartas.\n");
        consola.append("3-'/cantJugadores' -- para mostrar la cantidad de jugadores que hay en la partida actual.\n\n");
    }
    private void mostrarUltimaCarta() throws RemoteException {
        if(!controlador.mostrarCartaDescarte()){
            consola.append("        Debes esperar que inicie la partida.\n\n");
            consola.append("            Para iniciar la partida coloca '/iniciar'\n");
        }
    }
    private void mostrarManoJugador() throws RemoteException {
        if(!controlador.mostrarManoJugador()){
            consola.append("        Debes esperar que inicie la partida.\n\n");
            consola.append("            Para iniciar la partida coloca '/iniciar'\n");
        }
    }
    @Override
    public void marcarNoListo(){
        listo = false;
        bienvenida();
    }
    private void mostrarCantJugadores() throws RemoteException{
        int cant = controlador.cantJugadoresConectados();
        consola.append("        La cantidad de jugadores conectados a esta partida son: " + cant + ".\n\n");
    }

}