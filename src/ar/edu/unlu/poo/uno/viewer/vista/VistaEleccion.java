package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.observer.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaEleccion implements VentanaListener{
    JFrame frame;
    VistaConsola iConsola;
    VistaInterfazGrafica iGrafica;
    private JTextArea eligeCómoQuieresJugarTextArea;
    private JButton consola;
    private JButton pantalla;
    private JPanel principal;

    public VistaEleccion(){
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);

        agregarListeners();

        frame.add(principal);

        frame.setVisible(true);
    }

    private void agregarListeners(){
        consola.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iConsola == null){
                    iConsola = new VistaConsola(VistaEleccion.this);
                } else if(!iConsola.frame.isVisible()){
                    iConsola.frame.setVisible(true);
                }
                //Hacer que solo se cree una consola, lo mismo con perfil y ranking
            }
        });
        pantalla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iGrafica == null) {
                    iGrafica = new VistaInterfazGrafica();
                } else if(!iGrafica.frame.isVisible()){
                    iGrafica.frame.setVisible(true);
                }
            }
        });
    }

    @Override
    public void onVentanaCerrada() {
        iConsola = null;
    }
}
