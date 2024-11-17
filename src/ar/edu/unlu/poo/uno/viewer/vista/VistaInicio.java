package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.poo.uno.listener.VentanaListener;
import ar.edu.unlu.poo.uno.model.Ranking;
import ar.edu.unlu.poo.uno.model.Serializacion;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
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
                String idJugador = "";
                try {
                    if(Serializacion.existeJugador(usuario.getText())){
                        idJugador = Serializacion.leerDatosJugador(usuario.getText());
                    } else {
                        idJugador = Serializacion.escribirDatosJugador(usuario.getText());

                    }
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                /*
                idJugador = ranking.buscarIDJugadorName(usuario.getText());
                if( idJugador == null){
                }
                 */
                try {
                    abrirEleccion(idJugador);
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public void iniciar(){
        frame.setVisible(true);
    }
    public void abrirEleccion(String idJugador) throws IOException, ClassNotFoundException {
        if(!controlador.agregarJugador(idJugador)){
            ingreseSuNombreDeTextArea.setText("No puede entrar a la partida, ya tiene 4 jugadores.");
        } else {
            frame.setVisible(false);
            controlador.agregarJugador(idJugador);
            vistaEleccion = new VistaEleccion(idJugador, VistaInicio.this, controlador);
        }
    }
    public void setControladorVista(ControladorVista controlador){
        this.controlador = controlador;
    }

    @Override
    public void onVentanaCerrada(String ventana) throws IOException, ClassNotFoundException {
        if(ventana.equalsIgnoreCase("vistaeleccion")) {
            vistaEleccion = null;
            controlador.desconectarJugador();
        }
    }
}
