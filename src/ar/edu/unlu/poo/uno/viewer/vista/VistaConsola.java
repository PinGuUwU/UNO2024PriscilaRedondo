package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorConsola;
import ar.edu.unlu.poo.uno.controller.ControladorPartida;
import ar.edu.unlu.poo.uno.listener.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaConsola implements VentanaListener{

    private boolean listo = false;
    private boolean pidiendoColor = false;
    private String idJugador;
    ControladorConsola controlador;
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

    public VistaConsola(VentanaListener listener, String idJugador, ControladorPartida controladorP) {
        controlador = new ControladorConsola(controladorP);
        controlador.conectar(VistaConsola.this);
        this.idJugador = idJugador;
        this.listener = listener;
        iniciarConsola();
    }

    private void iniciarConsola() {
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    listener.onVentanaCerrada("consola");
                    frame.setVisible(false);
                }
            }
        });

        agregarListeners();

        frame.add(principal);
        bienvenida();

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
                iPerfil.actualizarPerfil(controlador.datosJugadorID(idJugador));
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
        consola.append("Bienvenido al juego del UNO \nSi quieres saber los comandos ingresa '/help'\n");
        consola.append("Para iniciar la partida coloca '/iniciar'\n");

    }
    private void buscarComando(String comando) throws RemoteException {
        if(comando.equalsIgnoreCase("/iniciar")){
            consola.append("\n");
            if(!listo){
                listo = true;
                //Aca debería corroborar que todos los jugadores hayan puesto /iniciar
                consola.append("Se te ha agregado a la partida. Espere a que todos los jugadores estén listos\n");
                controlador.iniciar(idJugador);
                //ac á a su vez tengo que avisarle a las demás consolas que hay x/4 listos
            } else {
                consola.append("Ya se ha confirmado su particitación, espere a los demás jugadores\n");
            }
            consola.append("\n");
        } else if(comando.equalsIgnoreCase("/help")){

        } else if(esNumero(comando)){
            if(!pidiendoColor){ //No puede ingresar numeros hasta que cambie de color
                boolean seTiro = controlador.opcion(comando, idJugador);
                if(!seTiro) {
                    consola.append("No se pudo tirar la carta, elija una que sea posible tirar.\n");
                }
            }

            //controlador
        } else if(pidiendoColor){
          isColor(comando);
        } else {
            consola.append("No se reconoce el comando ingresado, ingrese algo válido o revise el /help\n");
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
    public void setInTop(){
        frame.setAlwaysOnTop(true);
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
    public void setDescarte(String color, String valor){
        String esp = tipo(valor, color);
        if(esp != null){
            consola.append("Ultima carta tirada: " + esp + "\n");
        } else {
            consola.append("Ultima carta tirada: " + color + " " + valor + "\n");
        }
    }
    public void mostrarCartasJugador(ArrayList<String> colores, ArrayList<String> valores, ArrayList<Boolean> posibles){
        String carta = "";
        consola.append("Tu mano de cartas es: \n");
        for(int i=0; i<colores.size(); i++){
            if(esEspecial(valores.get(i))) {
                carta = "Carta " + (i + 1) + " | Especial: " + tipo(valores.get(i), colores.get(i)) + " | (se puede tirar)";
            } else {
                if(Integer.parseInt(valores.get(i))>10){
                    carta = "Carta " + (i+1) + " | Color: " + tipo(valores.get(i), colores.get(i)) + " | Valor: " + valores.get(i);
                } else {
                    carta = "Carta " + (i+1) + " | Color: " + colores.get(i) + " | Valor: " + valores.get(i);
                }
                if(posibles.get(i)){
                    carta += " | (se puede tirar)";
                } else {
                    carta += " | (no se puede tirar)";
                }
            }
            consola.append(carta + "\n");
        }
        consola.append("Ingrese el nro de la carta que desee tirar\n");

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
    public void pedirCambioColor(){
        pidiendoColor = true;
        consola.append("Ingrese el color al que desea cambiar. opciones:\nVerde\nAzul\nAmarillo\nRojo\n");
    }
    public void avisoInicio(){
        consola.append("\nTodos los jugadores están listos, la partida está por comenzar.\n\n\n");
    }
    public void levantarCarta(){
        consola.append("\nLas cartas que tiene no puede tirarlas, automaticamente levantará una del mazo de descarte hasta que alguna\nsea válida para tirar.\n");

    }
    public void isColor(String comando) throws RemoteException {
        comando = comando.toLowerCase();
        switch(comando){
            case "verde","rojo","amarillo","azul":
                controlador.cambiarColor(comando, idJugador);
                pidiendoColor = false;
                break;
            default:
                consola.append("ingrese un color válido de la lista.\n");
                pedirCambioColor();
                break;
        }
    }
}
