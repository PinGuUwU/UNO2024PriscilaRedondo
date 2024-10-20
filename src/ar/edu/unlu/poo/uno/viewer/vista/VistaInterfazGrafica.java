package ar.edu.unlu.poo.uno.viewer.vista;


import ar.edu.unlu.poo.uno.listener.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaInterfazGrafica {
    VentanaListener listener;
    private String idJugador;
    JFrame frame;
    private JPanel ventana;
    private JTextArea estadoTurno;
    private JButton confirmarButton;
    private JPanel linea3;
    private ArrayList<JButton> botonesLinea3;
    private JPanel linea2;
    private ArrayList<JButton> botonesLinea2;
    private JPanel linea1;
    private ArrayList<JButton> botonesLinea1;

    /*
    Cosas por hacer:
    Que cuando inicia tenga un boton con la opcion de iniciar
        Al iniciar, ese boton debe desaparecer, el texto del medio se actualiza y dirá "esperando a los demás jugadores"
    en algun lado debe decir siempre la cantidad de jugadores que hay en la partida tipo jugadores: 3
    en el texto del medio irá diciendo de quien es el turno (Ya sea con numero o username)
        caso numero: turno: jugador Nro 1/ jugador Nro 2 y así
            En este caso en alguna parte de la pantalla deberá decir "tu turno es el Nro 'numero'
    arriba debe tener boton de perfil y ranking como VistaConsola
    a la izquierda habrá un boton del MazoDeRobo (No se puede agarrar)
    a la derecha habrá un botón donde se verá la ultima carta del MazoDeDescarte

     */

    public VistaInterfazGrafica(VentanaListener listener, String idJugador){
        this.listener = listener;
        this.idJugador = idJugador;



    }
    private void iniciarConsola(){
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
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

        frame.add(ventana);
        bienvenida();

        frame.setVisible(true);
    }
    private void bienvenida(){
        estadoTurno.setText("Bienvenido, esperando que comience la partida");
    }
    private void agregarListeners(){
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    private void agregarListenerBoton(JButton boton){
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    public void agregarCarta(String valor, String color){
        boolean lleno1 = botonesLinea1.size()==10;
        boolean lleno2 = botonesLinea1.size()==10;
        JButton carta = new JButton();
        ImageIcon icon = new ImageIcon("D://Imágenes/pinguPio.jpg");
        carta.setIcon(icon);
        agregarListenerBoton(carta);
        if(lleno2){//Si la linea 1 y 2 están llenas uso la 3
            linea3.add(carta);
        } else if(lleno1){//Si solo la linea 1 está llena uso la linea 2
            linea2.add(carta);
        } else {//Si la linea 1 no está llena, la uso
            linea1.add(carta);
        }
    }
    public void setInTop(){
        frame.setAlwaysOnTop(true);
    }
}
