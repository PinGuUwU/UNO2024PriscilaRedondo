package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorPartidaGuardada;
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

public class VistaPartidaGuardada implements Serializable{
    private final String id;
    private final ControladorPartidaGuardada controlador;
    JFrame frame;
    private JPanel ventana;
    private JButton guardarPartida;
    private JScrollPane mostrarPartidas;
    private final JPanel panelPartidas; // Panel contenedor para las partidas

    public VistaPartidaGuardada(VentanaListener listener, String id, ControladorVista controladorVista) throws IOException, ClassNotFoundException {
        this.controlador = new ControladorPartidaGuardada(controladorVista);
        this.id = id;

        frame = new JFrame("Partidas Guardadas");
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
        //Genero un panel nuevo para mostrar toda la información importante de la partida pasada por parámetro
        JPanel opcion = new JPanel();
        opcion.setLayout(new GridLayout(1, 3));

        JTextArea infoPartida = new JTextArea();
        infoPartida.setEditable(false);
        infoPartida.append("PARTIDA " + nro + "\n");
        //El nro de partida
        boolean estanTodosLosJugadores = true;
        infoPartida.append("JUGADORES: ");
        //Los nombres de los jugadores que conforman la partida
        ArrayList<Jugador> jugadoresPartidaActual = controlador.jugadoresPartidaActual();
        ArrayList<Jugador> jugadoresPartidaACargar = partida.jugadores();
        if(jugadoresPartidaACargar.size() != jugadoresPartidaActual.size()){
            estanTodosLosJugadores = false;
        }
        for (int i = 0; i < partida.cantJugadores(); i++) {
            Jugador j = partida.jugadores().get(i);
            if (jugadoresPartidaACargar.contains(j)) {
                infoPartida.append(j.name() + ", ");
            } else {
                estanTodosLosJugadores = false;
            }
        }


        opcion.add(infoPartida);
        //El botón para cargar una partida
        JButton cargar = new JButton("CARGAR");
        if(!estanTodosLosJugadores){
            //Si no están todos los jugadores
            cargar.setBackground(java.awt.Color.red);
            cargar.setToolTipText("NO se puede cargar.");
        } else {
            //Si están todos los jugadores
            cargar.setBackground(java.awt.Color.green);
            cargar.setToolTipText("SÍ se puede cargar.");
        }
        cargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cargar.getBackground()!=java.awt.Color.red){
                    try {
                        controlador.cargarPartida(partida.getId());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        //Botón para eliminar una partida guardada
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
