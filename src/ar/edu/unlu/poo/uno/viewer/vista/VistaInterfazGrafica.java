package ar.edu.unlu.poo.uno.viewer.vista;


import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.poo.uno.listener.VentanaListener;
import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class VistaInterfazGrafica implements VentanaListener, IVista, Serializable {
    private final boolean puedeJugar;
    private boolean listo = false;
    private boolean pidiendoColor = false;
    private boolean levanto = false;
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
    private JPanel panelBajo;
    private JButton pasarTurno;
    private JButton desafio;
    private JButton uno;
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

    public VistaInterfazGrafica(VentanaListener listener,ControladorVista controlador) throws RemoteException {
        this.controlador = controlador;
        this.listener = listener;
        if(!controlador.puedoAgregarJugador()){//Si no se puede agregar jugador
            estadoTurno.append("Partida llena.");
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
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);
        estadoTurno.setFocusable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                    try {
                        listener.onVentanaCerrada("consola");
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
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
                    marcarListo();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    esperandoInicio();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        listoParaJugar.setText("COMENZAR");
        linea1.add(listoParaJugar);
        estadoTurno.append("Bienvenido, dale click en comenzar");
    }
    @Override
    public void esperandoInicio() throws RemoteException {
        if (!controlador.empezoLaPartida()){
            estadoTurno.setText("Esperando a los demás jugadores.");
        }
    }
    private void agregarListeners(){
        pasarTurno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    pasarTurno();
                    levanto = false;
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(elegirColor.isEnabled()){
                    String color = (String) elegirColor.getSelectedItem();
                    assert color != null;
                    Color colorCarta = controlador.deStringAColorCarta(color);
                    try {
                        controlador.cambiarColor(colorCarta);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    elegirColor.removeAllItems();
                    elegirColor.setEnabled(false);
                    pidiendoColor = false;
                }
            }
        });
        robo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(desafio.isEnabled()){ //Si podía desafiar y levantó una carta, entonces ya no puede desafiar
                    desafio.setEnabled(false);
                }
                try {
                    if(!controlador.empezoLaPartida()){
                        estadoTurno.setText("Aún no empezó la partida.");
                    } else if(pidiendoColor){
                        estadoTurno.setText("Ya tiró una carta y debe elegir un color");
                    } else if(!levanto && controlador.esSuTurno()){
                        try {
                            controlador.levantarCarta();
                            levanto = true;
                            controlador.preguntarSiPasaTurno(); //Muestro en ambas vistas si el jugador quiere pasar o tirar una carta
                            //En cada turno solo puede levantar una carta
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else if(controlador.esSuTurno()){
                        controlador.noPuedeLevantar();
                        estadoTurno.setText("Ya levantó una carta, debe tirar o pasar el turno.");
                    } else {
                        estadoTurno.setText("Debe esperar a que sea su turno para levantar una carta.");
                    }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        desafio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Debería poder presionarse SOLO si hay posibilidad de desafiar
            }
        });
        perfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(iPerfil == null){
                    iPerfil = new VistaPerfil(VistaInterfazGrafica.this, controlador.idJugador());
                } else if(!iPerfil.frame.isVisible()){
                    iPerfil.frame.setVisible(true);
                    iPerfil.setInTop();
                }
                iPerfil.actualizarPerfil(controlador.idJugador());
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
    @Override
    public void yaLevanto(){
        levanto=true;
    }
    private void agregarListenerCarta(JButton boton){
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(desafio.isEnabled()){
                    //Si podía desafiar al jugador anterior y tiró una carta, entonce ya no puede desafiarlo, siguiente turno
                    desafio.setEnabled(false);
                }
                try {
                    if(boton.getBackground() == java.awt.Color.green && !pidiendoColor && controlador.esSuTurno()){//Si se puede tirar
                        try {
                            controlador.opcion(buscarPosicionBoton(boton));
                            finalizoTurno();
                            if(!controlador.esSuTurno()){
                                estadoTurno.append("Esperando turno.");
                            }
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }

                    } else if(pidiendoColor){
                        estadoTurno.append("Debe elegir un color y apretar 'confirmar'");
                    }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
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
                quitarBotonCarta(b);
                encontrado = true;
            }
        }

        return pos;
    }
    @Override
    public void otroJugadorListo() throws RemoteException {
        estadoTurno.setText("Uno de los jugadores ya está listo.("+cantJugadoresListos()+"/"+cantJugadoresTotal()+")");
    }
    @Override
    public int cantJugadoresListos() throws RemoteException {
        return controlador.cantJugadoresListos();
    }
    @Override
    public int cantJugadoresTotal() throws RemoteException {
        return controlador.cantJugadoresConectados();
    }

    @Override
    public void decirUNO() {

    }

    @Override
    public void desafiarJugadorAnterior() throws RemoteException {
        desafio.setEnabled(false);
        controlador.desafiarJugador();
    }

    @Override
    public void avisarQueNoDijoUNO() throws RemoteException {
        controlador.avisarNoDijoUNO();
    }
    @Override
    public void puedeDesafiar(){
        //Muestro el boton de desafio
        desafio.setEnabled(true);
    }

    @Override
    public void pasarTurno() throws RemoteException {
        pasarTurno.setEnabled(false);
        controlador.pasarTurno();
    }

    @Override
    public void mostrarOpcionPasarTurno(){
        pasarTurno.setEnabled(true);
    }

    private void finalizoTurno(){
        levanto = false;
        pasarTurno.setEnabled(false);
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
    public void onVentanaCerrada(String ventana) {
        if(ventana.equalsIgnoreCase("perfil")){
            iPerfil = null;
        } else if(ventana.equalsIgnoreCase("ranking")){
            iRanking = null;
        }
    }

    @Override
    public void avisoInicio() {
        //Avisa que todos los jugadores están listos y el juego está por comenzar
        estadoTurno.append("Iniciando la partida.");
        marcarListo();
    }

    @Override
    public void setDescarte(Color color, TipoCarta valor) throws RemoteException {
        //Actualiza la carta de MazoDeDescarte
        ImageIcon icon;
        if(valor == TipoCarta.COMUN){
            int v = controlador.obtenerNumeroDescarte();
            icon = new ImageIcon("src/ar/edu/unlu/poo/uno/data/cartasImages/" + color.toString() + "/" + v + ".jpg");
        } else {
            icon = new ImageIcon("src/ar/edu/unlu/poo/uno/data/cartasImages/" + color.toString() + "/" + valor.toString() + ".jpg");
        }
            Image imagen = icon.getImage();
            imagen = imagen.getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            icon = new ImageIcon(imagen);
            descarte.setIcon(icon);
            descarte.setContentAreaFilled(false);
            descarte.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public void pedirCambioColor() {
        //Pide al jugador que elija qué color quiere que sea el siguiente
        //Acá agrego al JPanel "cambioColor" un JComboBox que tenga las 4 opciones de color
        //El jugador elige el que quiere y luego le da click al boton "confirmar"
        elegirColor.addItem("verde");
        elegirColor.addItem("amarillo");
        elegirColor.addItem("rojo");
        elegirColor.addItem("azul");
        elegirColor.setEnabled(true);
        estadoTurno.append("Elija un color para \nel cambio de color.");
        pidiendoColor = true;
    }

    @Override
    public void mostrarCartasJugador(ArrayList<Color> colores, ArrayList<TipoCarta> valores, ArrayList<Boolean> validos) throws RemoteException {
        //Acá hago to-do el proceso de Agregar botones y agregarle la correspondiente imagen
        borrarCartas();
        JButton carta;
        boolean cartasValidas = false;
        for(int i=0; i<colores.size(); i++){
            if(validos.get(i)){
                cartasValidas = true;
            }
            if(valores.get(i) == TipoCarta.COMUN){
                int v = controlador.obtenerNumero(i);
                carta = deBotonACarta(colores.get(i).toString(), String.valueOf(v), validos.get(i));
            } else {
                carta = deBotonACarta(colores.get(i).toString(), valores.get(i).toString(), validos.get(i));
            }
            agregarCarta(carta);
        }
        if(cartasValidas){
            estadoTurno.setText("Es su turno, elija una carta.");
        } else{
            estadoTurno.setText("Es su turno, elija una carta.");
            estadoTurno.setText("No tiene cartas para tirar, levante una del mazo de robo.");
        } /*else if(!controlador.esSuTurno()){
            estadoTurno.setText("Es el turno de otro jugador.\nEsperando turno");
        }*/
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
            carta.setBackground(java.awt.Color.green);
            carta.setToolTipText("SÍ se puede tirar.");
        } else {
            carta.setBackground(java.awt.Color.red);
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
    public void seCambioElColor(){
        if(pidiendoColor){
            elegirColor.removeAllItems();
            elegirColor.setEnabled(false);
            pidiendoColor = false;
        }
    }

    @Override
    public void marcarNoListo() {
        listo = false;
        bienvenida();
    }
    @Override
    public void marcarListo(){
        listo = true;
    }

}
