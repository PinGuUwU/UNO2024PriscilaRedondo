package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.observer.VentanaListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaConsola implements VentanaListener{
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

    public VistaConsola(VentanaListener listener) {
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
                    listener.onVentanaCerrada();
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
                    iPerfil = new VistaPerfil();
                } else {
                    iPerfil.setInTop();
                }
            }
        });
        ranking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iRanking == null){
                    iRanking = new VistaRanking();
                }
            }
        });
    }

    @Override
    public void onVentanaCerrada() {
        //Debería averiguar cómo sé cuando al ventana que se cerro es perfil y cuando ranking
        iPerfil = null;
    }
}
