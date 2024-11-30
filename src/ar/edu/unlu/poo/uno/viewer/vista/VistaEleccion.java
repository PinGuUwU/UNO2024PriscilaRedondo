package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.poo.uno.listener.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaEleccion implements VentanaListener, Serializable {
    JFrame frame;
    VistaConsola iConsola;
    VistaInterfazGrafica iGrafica;
    private JTextArea eligeComoQuieresJugarTextArea;
    private JButton consola;
    private JButton pantalla;
    private JPanel ventana;
    ControladorVista controlador;

    public VistaEleccion(String idJugador, VentanaListener listener, ControladorVista controlador) throws IOException, ClassNotFoundException {
        this.controlador = controlador;
        controlador.conectarID(idJugador);
        frame = new JFrame("UNO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(720, 480);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    try {
                        listener.onVentanaCerrada("vistaeleccion");
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.setVisible(false);
                }
            }
        });

        agregarListeners();

        eligeComoQuieresJugarTextArea.setLineWrap(false);
        eligeComoQuieresJugarTextArea.setFocusable(false);

        iConsola = new VistaConsola(VistaEleccion.this,controlador);
        controlador.conectar(iConsola);
        iConsola.frame.setVisible(false);
        iGrafica = new VistaInterfazGrafica(VistaEleccion.this,controlador);
        controlador.conectar(iGrafica);
        iGrafica.frame.setVisible(false);


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
        iConsola.frame.setVisible(true);
    }

    public void abrirInterfazGrafica() throws RemoteException {
        iGrafica.frame.setVisible(true);
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
