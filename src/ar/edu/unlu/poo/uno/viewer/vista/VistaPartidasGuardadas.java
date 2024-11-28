package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorPartidasGuardadas;
import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.poo.uno.listener.VentanaListener;
import ar.edu.unlu.poo.uno.model.Jugador;
import ar.edu.unlu.poo.uno.model.Partida;
import ar.edu.unlu.poo.uno.model.Serializacion;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaPartidasGuardadas implements Serializable{
    private final String id;
    private ControladorPartidasGuardadas controlador;
    JFrame frame;
    private JPanel ventana;
    private JButton guardarPartida;
    private JScrollPane mostrarPartidas;
    private JPanel panelPartidas; // Panel contenedor para las partidas

    public VistaPartidasGuardadas(VentanaListener listener, String id, ControladorVista controladorVista) throws IOException, ClassNotFoundException {
        this.controlador = new ControladorPartidasGuardadas(controladorVista);
        this.id = id;

        JFrame frame = new JFrame("Partidas Guardadas");
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);

        ventana = new JPanel(new BorderLayout());
        guardarPartida = new JButton("Guardar Partida");

        // Configurar el panel contenedor para las partidas
        panelPartidas = new JPanel();
        panelPartidas.setLayout(new BoxLayout(panelPartidas, BoxLayout.Y_AXIS));

        // Configurar el JScrollPane
        mostrarPartidas = new JScrollPane(panelPartidas);
        mostrarPartidas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        ventana.add(guardarPartida, BorderLayout.NORTH);
        ventana.add(mostrarPartidas, BorderLayout.CENTER);

        agregarListeners();
        agregarPartidas();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    try {
                        listener.onVentanaCerrada("partidasguardadas");
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        frame.add(ventana);
        frame.setVisible(true);

        // Listener para cerrar la ventana
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (listener != null) {
                    try {
                        listener.onVentanaCerrada("partidasguardadas");
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    private void agregarListeners() {
        guardarPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.guardarPartida();
                    actualizarPartidasMostradas();
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void agregarPartidas() throws IOException, ClassNotFoundException {
        // Limpiar el panel contenedor
        panelPartidas.removeAll();

        // Obtener las partidas guardadas
        ArrayList<Partida> partidasGuardadas = controlador.cargarPartidasGuardadas(id);

        // Crear un panel para cada partida
        for (int i = 0; i < partidasGuardadas.size(); i++) {
            agregarPanelPartida(partidasGuardadas.get(i), i + 1);
        }

        // Actualizar la vista
        panelPartidas.revalidate();
        panelPartidas.repaint();
    }

    private void agregarPanelPartida(Partida partida, int nro) throws RemoteException {
        JPanel opcion = new JPanel();
        opcion.setLayout(new GridLayout(1, 3));

        JTextArea infoPartida = new JTextArea();
        infoPartida.setEditable(false);
        infoPartida.append("PARTIDA " + nro + "\n");

        boolean estanTodosLosJugadores = true;
        infoPartida.append("JUGADORES: ");

        ArrayList<Jugador> jugadores = controlador.jugadoresPartidaActual();
        for (int i = 0; i < partida.cantJugadores(); i++) {
            Jugador j = partida.buscarJugador(jugadores.get(i).jugadorID());
            if (j != null) {
                infoPartida.append(j.name() + ", ");
            } else {
                estanTodosLosJugadores = false;
            }
        }

        opcion.add(infoPartida);

        JButton cargar = new JButton("Cargar");
        cargar.setBackground(estanTodosLosJugadores ? Color.GREEN : Color.RED);
        cargar.setToolTipText(estanTodosLosJugadores ? "SÍ se puede cargar." : "NO se puede cargar.");
        cargar.addActionListener(e -> {
            try {
                controlador.cargarPartida(partida);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton eliminar = new JButton("Eliminar");
        eliminar.addActionListener(e -> {
            try {
                Serializacion.eliminarPartida(partida);
                actualizarPartidasMostradas();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        opcion.add(cargar);
        opcion.add(eliminar);

        panelPartidas.add(opcion);
    }

    public void actualizarPartidasMostradas() throws IOException, ClassNotFoundException {
        agregarPartidas();
    }
}
