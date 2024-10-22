package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.listener.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaEleccion implements VentanaListener{
    private String idJugador;
    JFrame frame;
    VistaConsola iConsola;
    VistaInterfazGrafica iGrafica;
    private JTextArea eligeComoQuieresJugarTextArea;
    private JButton consola;
    private JButton pantalla;
    private JPanel ventana;

    public VistaEleccion(String idJugador, VentanaListener listener){
        this.idJugador = idJugador;
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    listener.onVentanaCerrada("consola");
                    frame.setVisible(false);
                }
            }
        });

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
                try {
                    abrirConsola();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                //Hacer que solo se cree una consola, lo mismo con perfil y ranking
            }
        });
        pantalla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    abrirInterfazGrafica();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public void abrirConsola() throws RemoteException {
        if(iConsola == null){
            iConsola = new VistaConsola(VistaEleccion.this, idJugador);
        } else if(!iConsola.frame.isVisible()){
            iConsola.frame.setVisible(true);
            iConsola.setInTop();
        }
    }
    public void abrirInterfazGrafica() throws RemoteException {
        if(iGrafica == null) {
            iGrafica = new VistaInterfazGrafica(VistaEleccion.this, idJugador);
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
