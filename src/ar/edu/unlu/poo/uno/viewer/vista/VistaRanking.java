package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorRanking;
import ar.edu.unlu.poo.uno.listener.VentanaListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaRanking implements Serializable {
    VentanaListener listener;
    JFrame frame;
    private final ControladorRanking controlador;
    private JPanel ventana;
    private JTextArea textArea1;
    private JPanel cantPartidas;
    private JTextArea textArea4;
    private JTextArea textArea5;
    private JComboBox<String> comboBox1;
    private JTextArea jugadoresCantidad;
    private JTextArea jugadoresNombre;

    public VistaRanking(VentanaListener listener){
        this.controlador = new ControladorRanking(VistaRanking.this);
        frame = new JFrame("ranking");
        frame.setSize(720,480);
        frame.setLocationRelativeTo(null);

        this.listener = listener;

        agregarListeners();
        iniciarRanking();


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    try {
                        listener.onVentanaCerrada("ranking");
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        frame.add(ventana);

        frame.setVisible(true);
    }

    private void iniciarRanking(){
        comboBox1.addItem("Ordenar por partidas ganadas:");//index 0
        comboBox1.addItem("Ordenar por partidas perdidas:");//index 1
    }

    private void agregarListeners(){
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                int opcion = comboBox1.getSelectedIndex();
                switch (opcion) {
                    case 0 -> {
                        //muestro top ganadores
                        //jugadoresNombres.add() asi voy agregando los username
                        try {
                            controlador.top5Ganadores();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        //llamo a ranking con el metodo ganadores
                        //En ese metodo, ranking llama a VistaRanking
                        // vista.actualizarRanking
                        System.out.println("(actualizaRanking ganadores)");
                    }
                    case 1 -> {
                        //muestr top perdedores
                        //jugadoresCantidad.add() asi voy agregando la cantidad de partidas ganadas/perdidas
                        try {
                            controlador.top5Perdedores();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("(actualizaRanking perdedores");
                    }
                }
                cantPartidas.revalidate();
            }
        });
    }

    public void actualizarRanking(List<String> nombres, List<Integer> cantidad) {
        // Limpiar los JTextAreas antes de añadir nuevos elementos
        jugadoresNombre.setText("");
        jugadoresCantidad.setText("");

        // Formatear y añadir texto a los JTextAreas
        for (int i = 0; i < nombres.size(); i++) {
            jugadoresNombre.append(nombres.get(i) + "\n");
            jugadoresCantidad.append(cantidad.get(i) + "\n");
        }

        // Asegúrate de que los JTextAreas se actualicen
        jugadoresNombre.revalidate();
        jugadoresCantidad.revalidate();
        System.out.println("(actualizaRanking)");
    }

    public void setInTop(){
        frame.setAlwaysOnTop(true);
    }
}
