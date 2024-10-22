package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.poo.uno.listener.VentanaListener;
import ar.edu.unlu.poo.uno.model.Ranking;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaInicio implements VentanaListener {
    JFrame frame;
    ControladorVista controlador;
    private JTextArea ingreseSuNombreDeTextArea;
    private JTextField usuario;
    private JButton confirmarButton;
    private JPanel ventana;
    String idJugador;

    public VistaInicio(ControladorVista controlador){
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
                abrirEleccion();
                frame.setVisible(false);
            }
        });
    }
    public void iniciar(){
        frame.setVisible(true);
    }
    public void abrirEleccion(){
        if(!controlador.agregarJugador(idJugador)){
            ingreseSuNombreDeTextArea.setText("No puede entrar a la partida, ya tiene 4 jugadores.");
        } else {
            controlador.agregarJugador(idJugador);
            VistaEleccion eleccion = new VistaEleccion(idJugador, VistaInicio.this);
        }
    }
    public void setControladorVista(ControladorVista controlador){
        this.controlador = controlador;
    }
    public String IdJugador(){ return idJugador; }

    @Override
    public void onVentanaCerrada(String ventana) {

    }
}
