package ar.edu.unlu.poo.uno.model.clases;

import ar.edu.unlu.poo.uno.controller.ControladorPartida;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class Partida extends ObservableRemoto {
    private ControladorPartida controlador;
    private Scanner leer = new Scanner(System.in);
    private int jugadoresListos = 0;
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private boolean sentido = true;//true = incrementa turno. false = decrementa turno;
    private int turno; //Quizá podría servir hacer una clase que lleve esto
    private MazoDeRobo mazoDeRobo;
    public MazoDeDescarte mazoDeDescarte;
    public Partida(){
        mazoDeDescarte = new MazoDeDescarte();
        mazoDeRobo = new MazoDeRobo();
    }
    public void conectar(ControladorPartida controlador){
        this.controlador = controlador;
    }
    public void iniciarPartida() throws RemoteException {
        mazoDeRobo.mezclar(); //Inicializo el mazo de cartas de robo
        for(int i=0; i<7; i++){//Cada jugador levanta 7 cartas
            for(Jugador j : jugadores){
                Carta carta = mazoDeRobo.robar(); //Voy levantando cartas y repartiendolas
                j.levantarCarta(carta);
            }
        }
        turno = 1;
        controlador.avisarInicioPartida();
        Carta carta = mazoDeRobo.robar();
        while(carta.valor()>10){//Fuerzo a que sea una carta comun
            carta = mazoDeRobo.robar();
        }
        mazoDeDescarte.agregar(carta);//Pongo la carta inicial en el mazo de descarte
        iniciarTurno();
    }
    public int cantJugadoresListos(){
        return jugadoresListos;
    }
    public void agregarJugadorListo(String idJugador) throws RemoteException {
        jugadoresListos+=1;
        if(jugadores.size() == cantJugadoresListos()){
            iniciarPartida();
        }
    }
    /*
    Debo leer y comprobar la condicion de la mano del jugador
    este metodo se utiliza en el sistema principal del juego, la clase partida
    Solo se encarga de crear los metodos necesarios para una partida del uno
     */
    public void iniciarTurno() throws RemoteException {
        int turnoActual = turno-1;
        Jugador jugadorActual = jugadores.get(turnoActual);
        if(mazoDeRobo.sinCartas()){
            mazoDeRobo.mezclar();
        }
        if(jugadorActual.cantCartasMano() == 0){
            finalizarPartida(jugadorActual);
        }
        actualizarCartaDescarte(mazoDeDescarte.ultimaCarta());
        actualizarCartasVista(jugadorActual);
        /*
        Si tiene cartas para tirar, puede tirar,
        si no esta obligado a levantar del mazo de robo hasta que quiera o pueda tirar
         */
        pedirCartaJugador(jugadorActual);
        this.turno = siguienteTurno(this.turno);
    }
    /*
    En este metodo compruebo si se puede tirar o no con Condicion
    y luego tira la carta que el jugador decida
    y luego la agrega al mazo de descarte
     */
    public void pedirCartaJugador(Jugador j) throws RemoteException {
        Condicion condiciones = new Condicion();
        Carta carta = null;
        Boolean estado=true;
        Boolean cartasValidas = false;
        //Primero verifico si tiene cartas validas para ser tiradas
        for(int i = 0; i < j.cantCartasMano(); i++){
            if(condiciones.sePuedeTirar(mazoDeDescarte.ultimaCarta(), j.mostrarCarta(i))){
                cartasValidas = true;
            }
        }/*
        if(!cartasValidas){
            controlador.debeLevantarCarta(true);
            //El true indica que es obligatorio que levante carta
            Si no tengo cartas validas, pido que levante cartas
            Aca pienso, si levanta una valida, lo dejo seguir levantando?

        }

        Aca enviaria un mensaje a la interfaz para que le pida al jugador que ingrese
        algo por consola para leerlo aca
         */
        /*
        Si el jugador puede levantar cartas siempre que pueda, entonces el valor '0'
        Significaria que quiere levantar otra carta
         */
        /*Este while se reemplaza por un mensaje hacia la VistaConsola, quien le mostrará las
        cartas disponibles que tiene el jugador y le pedirá que elija una para tirar
        o, caso de no poder, que levante una
        while(estado){
            System.out.println("Ingrese carta: ");
            int pos = leer.nextInt();
            carta = j.mostrarCarta(pos);
            //Puede levantar cartas cuantas veces quiera
            if(carta.valor() == 0){
                levantarCarta(j);
            }
            if(condiciones.sePuedeTirar(carta, mazoDeDescarte.ultimaCarta())){
                //Si puede tirar esa carta, entonces la tira y salgo del while
                j.tirarCarta(pos);
                mazoDeDescarte.agregar(carta);
                estado = false;
            } else {
                //Sigo pidiendo una posicion/carta valida hasta que la de
                System.out.println("Ingrese el valor valido.");
            }*/
        }

    public ControladorPartida controlador(){ return controlador; }
    public void actualizarCartasVista(Jugador jugadorActual) throws RemoteException {

        int cantCartas = jugadorActual.cantCartasMano();
        Condicion condiciones = new Condicion();
        ArrayList<Boolean> estadoMano = new ArrayList<>();
        Boolean cartasValidas = false;
        /*
        Voy a hacer un Array Boolean que diga que cartas se pueden tirar
        Luego cuando haga la interfaz sabre como manejar ese array, si es que sigue siendo un array,ç
        quiza se convierte en un mensaje para la interfaz

        for(int i=0; i< cantCartas; i++){ //Muestro al jugador que cartas puede tirar y cuales no
            Boolean estado = false;
            if(condiciones.sePuedeTirar(jugadorActual.mostrarCarta(i),mazoDeDescarte.ultimaCarta())){
                estado = true;
            }
            estadoMano.add(i, estado);
        }*/
        Mano mano = jugadorActual.mostrarCartas();
        ArrayList<String> colores = new ArrayList<>();
        ArrayList<String> valores = new ArrayList<>();
        ArrayList<Boolean> posibles = new ArrayList<>();
        for(int i=0; i<jugadorActual.cantCartasMano(); i++){
            Carta carta = mano.leerCartaMano(i);
            colores.add(carta.color());
            valores.add(String.valueOf(carta.valor()));
            Carta descarte = mazoDeDescarte.ultimaCarta();
            if(condiciones.sePuedeTirar(descarte, carta)){
                cartasValidas = true;
                posibles.add(true);
            } else {
                posibles.add(false);
            }
        }
        controlador.actualizarCartasEnMano(colores, valores, posibles);
        if(!cartasValidas){
            controlador.debeLevantarCarta();
            jugadorActual.levantarCarta(mazoDeRobo.robar());
            actualizarCartasVista(jugadorActual);
            actualizarCartaDescarte(mazoDeDescarte.ultimaCarta());
        }
    }
    public void levantarCarta(Jugador j) throws RemoteException {//Lo mismo que tirar Carta?
        j.levantarCarta(mazoDeRobo.robar());
        actualizarCartasVista(j);
    }
    public boolean tirarCarta(String id, String posCarta) throws RemoteException {//Quizá debería devolver la carta para luego mostrarla?
        int pos = Integer.parseInt(posCarta);
        pos--;//Porque las opciones empiezan en 1, mientras que la mano empieza en 0
        Jugador j = buscarJugador(id);
        Carta carta = j.mostrarCarta(pos);
        Condicion condiciones = new Condicion();
        if(esEspecial(carta)){
            pedirColorJugador();
        }
        if(condiciones.sePuedeTirar(carta, mazoDeDescarte.ultimaCarta())){
            j.tirarCarta(pos);
            mazoDeDescarte.agregar(carta);
            actualizarCartaDescarte(carta);
            actualizarCartasVista(j);
            return true;
        } else {
            return false;
        }
    }
    public void pedirColorJugador(){
        controlador.pedirColor();
    }
    public void elegirColor(String color, String idj) throws RemoteException {
        Carta carta = new Carta(color);
        mazoDeDescarte.agregar(carta);
        Jugador j = buscarJugador(idj);
        j.tirarCartaCambioColor();
        actualizarCartaDescarte(carta);
        actualizarCartasVista(j);
        siguienteTurno(turno);
    }
    public String agregarJugador(String username){
        //Devuelve el id
        /* Debo ver como manejo con RMI el tema de agregar jugadores
        if(jugadores.size() == 4){
            System.out.println("Ya hay demasiados jugadores, intente entrando en otra partida.");
            return null;
        }*/
        Jugador jugador;
        String idStr;
        if(controlador.existeJugador(username) != null){
            //Lo leo
            String[] datos = controlador.datosDataJugadorName(username);
            jugador = new Jugador(datos[0], datos[1], datos[2], datos[3]);
            idStr = datos[0];
        } else {//lo creo
            int idInt = Integer.parseInt(controlador.ultimoID()) + 1;
            idStr = String.valueOf(idInt);
            jugador = new Jugador(idStr, username);
            controlador.cargarJugador(jugador);
        }
        jugadores.add(jugador);
        //reset partida?
        turno = 0;
        //Si se suma un jugador se resetea la partida?
        //Entonces tendría que hacer:
        //turno=1; y también en el main un "partida = new Partida();"
        return idStr;
    }
    /*
    Mas que nada comprueba el valor de turno y lo actualiza
     */
    public int siguienteTurno(int turnoActual){
        //Turno del 1-4
        //Debo corroborar que todos tengan cartas
        for(Jugador j : jugadores){
            if(j.cantCartasMano() == 0){
                finalizarPartida(j);
            }
        }
        if(sentido){
            if(turnoActual== jugadores.size()){
                return 0;
            } else {
                return turnoActual++;
            }
        } else { //if(!sentido)
            if(turnoActual == 0){
                return jugadores.size();
            } else {
                return turnoActual--;
            }
        }
    }
    public void finalizarPartida(Jugador ganador){
        for(Jugador j : jugadores){
            if(j == ganador){
                ganador.actualizarPartidasGanadas();
                controlador.actualizarPartidasGanadas(ganador);
            } else {
                j.actualizarPartidasPerdidas();
                controlador.actualizarPartidasPerdidas(j);
            }
        }

    }

    private Jugador buscarJugador(String idJugador){
       Jugador j = null;
        for(int i=0; i<jugadores.size(); i++){
            if((jugadores.get(i).jugadorID()).equals(idJugador)){
                j = jugadores.get(i);
            }
        }
        return j;
    }
    //Ingresos posibles en consola del jugador
    public void tirarCarta(int pos) throws RemoteException {
        Jugador j = jugadores.get(turno);
        Carta carta = j.tirarCarta(pos);
        mazoDeDescarte.agregar(carta);
        actualizarCartaDescarte(carta);
        siguienteTurno(turno);
        /*controlador.actualizarSiguienteJugador();
        Aca debería poder acceder a la vista del jugador que le toca el siguiente turno para que le muestre
        que es su turno, muestre sus cartas y le pida una opcion
         */
    }
    public void actualizarCartaDescarte(Carta carta) throws RemoteException {
        String color = carta.color();
        int valor = carta.valor();
        controlador.actualizarCartaTirada(color, valor);
        notificarObservadores();
    }
    public boolean esTurno(String id){
        if( (jugadores.get(turno)).jugadorID().equals(id)){
            //Si el jugador por el cual consulto, es el mismo que el de la posicion
            //Del turno, entonces es su turno (Para saber si puede ingresar opcion de carta o no)
            return true;
        } else {
            return false;
        }
    }
    public boolean esEspecial(Carta carta){
        return carta.valor()>13;
    }
}