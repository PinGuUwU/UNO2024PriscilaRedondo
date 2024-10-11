package ar.edu.unlu.poo.uno.viewer.vista;


import ar.edu.unlu.poo.uno.observer.VentanaListener;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaInterfazGrafica {
    VentanaListener listener;
    JFrame frame;
    private JPanel ventana;
    private JTextArea acaNoHayNadaTextArea;

    public VistaInterfazGrafica(VentanaListener listener){
        this.listener = listener;
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);
        frame.add(ventana);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    listener.onVentanaCerrada("interfazgrafica");
                }
            }
        });

        frame.setVisible(true);
    }
    public void setInTop(){
        frame.setAlwaysOnTop(true);
    }
}
