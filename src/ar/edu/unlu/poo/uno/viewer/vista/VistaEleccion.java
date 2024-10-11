package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.observer.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaEleccion implements VentanaListener{
    private String idJugador;
    JFrame frame;
    VistaConsola iConsola;
    VistaInterfazGrafica iGrafica;
    private JTextArea eligeComoQuieresJugarTextArea;
    private JButton consola;
    private JButton pantalla;
    private JPanel ventana;

    public VistaEleccion(String idJugador){
        this.idJugador = idJugador;
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);

        agregarListeners();

        eligeComoQuieresJugarTextArea.setLineWrap(false);
        eligeComoQuieresJugarTextArea.setFocusable(false);

        frame.add(ventana);

        frame.setVisible(true);
    }

    private void agregarListeners(){
        consola.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirConsola();
                //Hacer que solo se cree una consola, lo mismo con perfil y ranking
            }
        });
        pantalla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInterfazGrafica();
            }
        });
    }
    public void abrirConsola(){
        if(iConsola == null){
            iConsola = new VistaConsola(VistaEleccion.this, idJugador);
        } else if(!iConsola.frame.isVisible()){
            iConsola.frame.setVisible(true);
            iConsola.setInTop();
        }
    }
    public void abrirInterfazGrafica(){
        if(iGrafica == null) {
            iGrafica = new VistaInterfazGrafica(VistaEleccion.this);
        } else if(!iGrafica.frame.isVisible()){
            iGrafica.frame.setVisible(true);
            iGrafica.setInTop();
        }
    }

    @Override
    public void onVentanaCerrada(String ventana) {
        if(ventana.equalsIgnoreCase("consola")){
            iConsola = null;
        } else if(ventana.equalsIgnoreCase("interfazgrafica")){
            iGrafica = null;
        }
    }

}
