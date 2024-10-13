package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorPartida;
import ar.edu.unlu.poo.uno.observer.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaInicio implements VentanaListener {
    JFrame frame;
    ControladorPartida controlador;
    private JTextArea ingreseSuNombreDeTextArea;
    private JTextField usuario;
    private JButton confirmarButton;
    private JPanel ventana;
    String idJugador;

    public VistaInicio(ControladorPartida controlador){
        this.controlador = controlador;
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);

        agregarListeners();

        frame.add(ventana);

        frame.setVisible(true);
    }
    public void agregarListeners(){
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controlador.existeJugador(usuario.getText()) == null){
                    idJugador = controlador.agregarJugador(usuario.getText());
                } else {
                    idJugador = controlador.buscarIdName(usuario.getText());
                }
                abrirEleccion();
                frame.setVisible(false);
            }
        });
    }
    public void abrirEleccion(){
        VistaEleccion eleccion = new VistaEleccion(idJugador, controlador.partida(), VistaInicio.this);
    }
    public String IdJugador(){ return idJugador; }
    @Override
    public void onVentanaCerrada(String ventana) {

    }
}
