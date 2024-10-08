package ar.edu.unlu.poo.uno.viewer.vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaEleccion {
    JFrame frame;
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
                VistaConsola consola = new VistaConsola();
                //Hacer que solo se cree una consola, lo mismo con perfil y ranking
            }
        });
        pantalla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
