package ar.edu.unlu.poo.uno.viewer.vista;


import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.poo.uno.listener.VentanaListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaInterfazGrafica implements VentanaListener, IVista {
    private final boolean puedeJugar;
    private boolean listo = false;
    private boolean pidiendoColor = false;
    private final String idJugador;
    ControladorVista controlador;
    VentanaListener listener;
    VistaPerfil iPerfil;
    VistaRanking iRanking;
    JFrame frame;
    private JPanel ventana;
    private JTextArea estadoTurno;
    private JButton confirmarButton;
    private JPanel linea3;
    private ArrayList<JButton> botonesLinea3 = new ArrayList<>();
    private JPanel linea2;
    private ArrayList<JButton> botonesLinea2 = new ArrayList<>();
    private JPanel linea1;
    private JButton perfil;
    private JButton ranking;
    private JButton descarte;
    private JButton robo;
    private JPanel cambioColor;
    private JComboBox<String> elegirColor;
    private ArrayList<JButton> botonesLinea1 = new ArrayList<>();

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

    public VistaInterfazGrafica(VentanaListener listener, String idJugador) throws RemoteException {
        controlador = new ControladorVista();
        controlador.conectar(VistaInterfazGrafica.this);
        estadoTurno.setFocusable(false);
        this.listener = listener;
        this.idJugador = idJugador;
        if(!controlador.agregarJugador(idJugador)){//Si no se puede agregar jugador
            estadoTurno.setText("Partida llena.");
            puedeJugar = false;
        } else {//Solo doy el aviso, no voy a manejar nada mas
            puedeJugar = true;
            iniciar();
        }
    }
    @Override
    public void iniciar(){
        frame = new JFrame("UNO");
        frame.setLocation(720, 480);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        ImageIcon icon = new ImageIcon("src/ar/edu/unlu/poo/uno/data/cartasImages/detras.jpg");
        Image imagen = icon.getImage();
        imagen = imagen.getScaledInstance(70, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(imagen);
        robo.setIcon(icon);
        //robo.setFocusPainted(false);
        robo.setContentAreaFilled(false);
        robo.setBorder(BorderFactory.createEmptyBorder());

        elegirColor.setEnabled(false);

        agregarListeners();

        frame.add(ventana);
        bienvenida();

        frame.setVisible(true);
    }
    private void bienvenida(){
        JButton listoParaJugar= new JButton();
        listoParaJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linea1.remove(listoParaJugar);
                linea1.repaint();
                linea1.revalidate();
                listo = true;
                try {
                    controlador.iniciar();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                estadoTurno.setText("Esperando a los demás jugadores.");
            }
        });
        listoParaJugar.setText("COMENZAR");
        linea1.add(listoParaJugar);
        estadoTurno.setText("Bienvenido, dale click en comenzar");
    }
    private void agregarListeners(){
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(elegirColor.isEnabled()){
                    String color = (String) elegirColor.getSelectedItem();
                    try {
                        controlador.cambiarColor(color, idJugador);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    elegirColor.removeAllItems();
                    elegirColor.setEnabled(false);
                    pidiendoColor = false;
                }
            }
        });
        perfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iPerfil == null){
                    iPerfil = new VistaPerfil(VistaInterfazGrafica.this, idJugador);
                } else if(!iPerfil.frame.isVisible()){
                    iPerfil.frame.setVisible(true);
                    iPerfil.setInTop();
                }
                iPerfil.actualizarPerfil(controlador.datosJugadorID(idJugador));
            }
        });
        ranking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iRanking == null){
                    iRanking = new VistaRanking(VistaInterfazGrafica.this);
                } else if(!iRanking.frame.isVisible()){
                    iRanking.frame.setVisible(true);
                    iRanking.setInTop();
                    //Tengo qeu volver a agregar el item "elige opcion"
                }
            }
        });
    }
    private void agregarListenerCarta(JButton boton){
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(boton.getBackground() == Color.green && !pidiendoColor){//Si se puede tirar
                    try {
                        controlador.opcion(buscarPosicionBoton(boton), idJugador);
                        estadoTurno.setText("Esperando turno.");
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }

                } else if(pidiendoColor){
                    estadoTurno.setText("Debe elegir un color y apretar 'confirmar'");
                }
            }
        });
    }
    private String buscarPosicionBoton(JButton b){
        //Manejo numeros a partir de 1, no de cero
        String pos = "";
        ImageIcon icon = (ImageIcon) b.getIcon();
        String cyvBuscado = icon.getDescription();
        System.out.println(cyvBuscado);
        ArrayList<JButton> listaCompleta = new ArrayList<>();
        listaCompleta.addAll(botonesLinea1);
        listaCompleta.addAll(botonesLinea2);
        listaCompleta.addAll(botonesLinea3);
        boolean encontrado = false;
        for(int i=0; i<listaCompleta.size(); i++){
            ImageIcon icon1 = (ImageIcon) listaCompleta.get(i).getIcon();
            String cyvActual = icon1.getDescription();
            if(cyvBuscado.equals(cyvActual) && !encontrado){
                int j = i + 1;
                pos = String.valueOf(j);
                quitarBotonCarta(listaCompleta.get(i));
                encontrado = true;
            }
        }

        return pos;
    }
    public void quitarBotonCarta(JButton b){
        boolean l1 = buscarBotonEnPanel(linea1, b);
        boolean l2 = buscarBotonEnPanel(linea2, b);
        boolean l3 = buscarBotonEnPanel(linea3, b);
        if(l1){
            linea1.remove(b);
        } else if(l2){
            linea2.remove(b);
        } else if(l3){
            linea3.remove(b);
        }//Todos son "elseif" porque puede estar en mas de una linea
        //Y yo quiero que solo se borre UNA carta, no varias
    }
    public boolean buscarBotonEnPanel(JPanel panel, JButton b){
        boolean encontrado = false;
        for(int i=0; i<panel.getComponentCount(); i++){
            if(panel.getComponent(i).equals(b)){
                encontrado = true;
            }
        }
        return encontrado;
    }
    private void agregarCarta(JButton carta){
        boolean lleno1 = botonesLinea1.size()==10;
        boolean lleno2 = botonesLinea1.size()==10;
        agregarListenerCarta(carta);
        if(lleno2){//Si la linea 1 y 2 están llenas uso la 3
            linea3.add(carta);
            botonesLinea3.add(carta);
        } else if(lleno1){//Si solo la linea 1 está llena uso la linea 2
            linea2.add(carta);
            botonesLinea2.add(carta);
        } else {//Si la linea 1 no está llena, la uso
            linea1.add(carta);
            botonesLinea1.add(carta);
        }
    }
    @Override
    public void setInTop(){
        frame.setAlwaysOnTop(true);
    }

    @Override
    public void onVentanaCerrada(String ventana) {
        if(ventana.equalsIgnoreCase("perfil")){
            iPerfil = null;
        } else if(ventana.equalsIgnoreCase("ranking")){
            iRanking = null;
        }
    }

    @Override
    public void levantarCarta() {
        //Se levanta una carta automaticamente porque el jugador no
        // tiene posibilidades de tirar otra carta
        estadoTurno.setText("Se levantarán cartas hasta poder tirar una.");
    }

    @Override
    public void avisoInicio() {
        //Avisa que todos los jugadores están listos y el juego está por comenzar
        estadoTurno.setText("Iniciando la partida.");
    }

    @Override
    public void setDescarte(String color, String valor) {
        //Actualiza la carta de MazoDeDescarte
        ImageIcon icon = new ImageIcon("src/ar/edu/unlu/poo/uno/data/cartasImages/" + color + "/" + valor + ".jpg");
        Image imagen = icon.getImage();
        imagen = imagen.getScaledInstance(70, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(imagen);
        descarte.setIcon(icon);
        descarte.setContentAreaFilled(false);
        descarte.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public void pedirCambioColor() throws RemoteException {
        //Pide al jugador que elija qué color quiere que sea el siguiente
        //Acá agrego al JPanel "cambioColor" un JComboBox que tenga las 4 opciones de color
        //El jugador elige el que quiere y luego le da click al boton "confirmar"
        elegirColor.addItem("verde");
        elegirColor.addItem("amarillo");
        elegirColor.addItem("rojo");
        elegirColor.addItem("azul");
        elegirColor.setEnabled(true);
        estadoTurno.setText("Elija un color para \nel cambio de color.");
        pidiendoColor = true;
    }

    @Override
    public void mostrarCartasJugador(ArrayList<String> colores, ArrayList<String> valores, ArrayList<Boolean> validos) {
        //Acá hago to-do el proceso de Agregar botones y agregarle la correspondiente imagen
        borrarCartas();
        JButton carta;
        for(int i=0; i<colores.size(); i++){
            carta = deBotonACarta(colores.get(i), valores.get(i), validos.get(i));
            agregarCarta(carta);
        }
        estadoTurno.setText("Es su turno, elija una carta.");
    }
    private void borrarCartas(){
        botonesLinea1.removeAll(botonesLinea1);
        botonesLinea2.removeAll(botonesLinea2);
        botonesLinea3.removeAll(botonesLinea3);
        linea1.removeAll();
        linea2.removeAll();
        linea3.removeAll();
    }
    private JButton deBotonACarta(String color, String valor, Boolean posible){
        //Metodo que le paso los valores de una carta para que cree el boton correspondiente a esa carta
        JButton carta = new JButton();
        if(posible){
            carta.setBackground(Color.green);
            carta.setToolTipText("SÍ se puede tirar.");
        } else {
            carta.setBackground(Color.red);
            carta.setToolTipText("NO se puede tirar.");
        }
        ImageIcon icon = new ImageIcon("src/ar/edu/unlu/poo/uno/data/cartasImages/" + color + "/" + valor + ".jpg");
        Image imagen = icon.getImage();
        imagen = imagen.getScaledInstance(70, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(imagen);
        icon.setDescription(color + "," + valor);
        carta.setIcon(icon);
        return carta;
    }

    @Override
    public void marcarNoListo() {
        listo = false;
        bienvenida();
    }
}
