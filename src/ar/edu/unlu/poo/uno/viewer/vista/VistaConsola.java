package ar.edu.unlu.poo.uno.viewer.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaConsola {
    JFrame frame;
    private JButton enter;
    private JTextField entradaDeTexto;
    private JTextArea consola;
    private JPanel barra;
    private JButton perfil;
    private JButton ranking;
    private JButton button3;
    private JPanel principal;

    public VistaConsola() {
        iniciarConsola();
    }

    private void iniciarConsola() {
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);

        agregarListeners();

        frame.add(principal);

        frame.setVisible(true);
    }

    private void agregarListeners(){
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consola.append(entradaDeTexto.getText());
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
                VistaPerfil perfil = new VistaPerfil();
            }
        });
        ranking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
