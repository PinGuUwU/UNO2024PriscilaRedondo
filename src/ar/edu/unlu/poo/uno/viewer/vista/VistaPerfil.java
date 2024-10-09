package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.observer.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaPerfil{
    VentanaListener listener;
    JFrame frame;
    private JPanel principal;
    private JTextField nombreDeUsuarioTextField;
    private JButton confirmarButton;
    private JTextField user;
    private JTextField partidasGanadasTextField;
    private JTextField partidasPerdidasTextField;
    private JTextField puntosActualesTextField;
    private JTextField partidasGanadas;
    private JTextField partidasPerdidas;
    private JTextField puntosActuales;
    private JTextField nuevoUsuarioTextField;
    private JTextField nuevoUsuario;

    public VistaPerfil(VentanaListener listener){
        this.listener = listener;
        frame = new JFrame("Perfil");
        frame.setLocation(720, 480);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);
        agregarListeners();
        frame.add(principal);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    listener.onVentanaCerrada();
                }
            }
        });

        frame.setVisible(true);
    }

    private void agregarListeners(){
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            /*
            consola.append(entradaDeTexto.getText());
                consola.append("\n");
                entradaDeTexto.setText("");
             */
                //Cambiar en el archivo y to-do
                user.setText(nuevoUsuario.getText());
            }
        });
        nuevoUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoNombre = e.getActionCommand();
            /*
            consola.append(entradaDeTexto.getText());
                consola.append("\n");
                entradaDeTexto.setText("");
             */
                //Cambiar en el archivo y to-do
                user.setText(nuevoNombre);
            }
        });
    }
    public void setInTop(){
        frame.setAlwaysOnTop(true);
    }
}
