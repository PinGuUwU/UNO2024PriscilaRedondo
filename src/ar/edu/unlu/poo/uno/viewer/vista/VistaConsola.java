package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorConsola;
import ar.edu.unlu.poo.uno.controller.ControladorPartida;
import ar.edu.unlu.poo.uno.controller.ControladorRanking;
import ar.edu.unlu.poo.uno.model.clases.Partida;
import ar.edu.unlu.poo.uno.observer.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaConsola implements VentanaListener{
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
        controlador = new ControladorConsola(controladorP, VistaConsola.this);
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

        frame.setVisible(true);
    }

    private void agregarListeners(){
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consola.append(entradaDeTexto.getText());
                buscarComando(entradaDeTexto.getText());
                consola.append("\n");
                entradaDeTexto.setText("");
            }
        });//Tanto enter como entrada de texto deberían enviar el comando al main
        entradaDeTexto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consola.append(entradaDeTexto.getText());
                consola.append("\n");
                entradaDeTexto.setText("");
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
        consola.append("Bienvenido al juego del UNO \n si quieres saber los comandos ingresa '/help'\n");
        consola.append("Para iniciar la partida coloca '/iniciar'\n");

    }
    private void buscarComando(String comando){
        if(comando.equalsIgnoreCase("/iniciar")){
            //Aca debería corroborar que todos los jugadores hayan puesto /iniciar
            controlador.iniciar();
        } else if(esNumero(comando)){
            //controlador
            controlador.opcion(comando);
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
        consola.append("Se tiró la carta: " + color + " " + valor + "\n");
    }
}
