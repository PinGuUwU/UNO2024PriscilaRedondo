package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.poo.uno.listener.VentanaListener;
import ar.edu.unlu.poo.uno.model.Ranking;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.rmi.RemoteException;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaInicio implements VentanaListener, Serializable {
    JFrame frame;
    VistaEleccion vistaEleccion;
    ControladorVista controlador;
    private JTextArea ingreseSuNombreDeTextArea;
    private JTextField usuario;
    private JButton confirmarButton;
    private JPanel ventana;
    String idJugador;

    public VistaInicio(ControladorVista controlador) throws RemoteException {
        setControladorVista(controlador);
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        agregarListeners();
        ingreseSuNombreDeTextArea.setFocusable(false);

        frame.add(ventana);
    }
    public void agregarListeners(){
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ranking ranking = new Ranking();
                idJugador = ranking.buscarIDJugadorName(usuario.getText());
                if( idJugador == null){
                    idJugador = ranking.agregarJugador(usuario.getText());
                }
                try {
                    controlador.conectarJugador(idJugador);
                    abrirEleccion();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public void iniciar(){
        frame.setVisible(true);
    }
    public void abrirEleccion() throws RemoteException {
        if(!controlador.agregarJugador()){
            ingreseSuNombreDeTextArea.setText("No puede entrar a la partida, ya tiene 4 jugadores.");
        } else {
            frame.setVisible(false);
            controlador.agregarJugador();
            vistaEleccion = new VistaEleccion(idJugador, VistaInicio.this, controlador);
        }
    }
    public void setControladorVista(ControladorVista controlador){
        this.controlador = controlador;
    }

    @Override
    public void onVentanaCerrada(String ventana) throws RemoteException {
        if(ventana.equalsIgnoreCase("vistaeleccion")) {
            vistaEleccion = null;
            if(idJugador!=null){
                controlador.desconectarJugador();
            }
        }
    }
}
