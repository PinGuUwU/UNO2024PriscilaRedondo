package ar.edu.unlu.poo.uno.model.clases;

import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Partida extends ObservableRemoto implements IPartida {
    private static Partida partida;
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
    @Override
    public void agregarObserver(ControladorVista controlador) throws RemoteException {
        this.agregarObservador(controlador);
    }
    @Override
    public void iniciarPartida() throws RemoteException {
        mazoDeRobo.mezclar(); //Inicializo el mazo de cartas de robo
        for(int i=0; i<7; i++){//Cada jugador levanta 7 cartas
            for(Jugador j : jugadores){
                Carta carta = mazoDeRobo.robar(); //Voy levantando cartas y repartiendolas
                j.levantarCarta(carta);
            }
        }
        turno = 0;
        actualizarInicioPartida();
        Carta carta = mazoDeRobo.robar();
        while(carta.valor()>10){//Fuerzo a que sea una carta comun
            carta = mazoDeRobo.robar();
        }
        mazoDeDescarte.agregar(carta);//Pongo la carta inicial en el mazo de descarte
        iniciarTurno();
    }
    @Override
    public void actualizarInicioPartida() throws RemoteException {
        this.notificarObservadores(Eventos.INICIAR_PARTIDA);
    }
    @Override
    public int cantJugadoresListos(){
        return jugadoresListos;
    }
    @Override
    public void agregarJugadorListo() throws RemoteException {
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
    @Override
    public void iniciarTurno() throws RemoteException {
        Jugador jugadorActual = jugadores.get(turno);
        if(mazoDeRobo.sinCartas()){
            mazoDeRobo.mezclar();
        }
        if(jugadorActual.cantCartasMano() == 0){
            finalizarPartida(jugadorActual);
        }
        actualizarCartaDescarte();
        actualizarCartasVista();
        /*
        Si tiene cartas para tirar, puede tirar,
        si no esta obligado a levantar del mazo de robo hasta que quiera o pueda tirar
         */
        this.turno = siguienteTurno(this.turno);
    }
    /*
    En este metodo compruebo si se puede tirar o no con Condicion
    y luego tira la carta que el jugador decida
    y luego la agrega al mazo de descarte
     */
    @Override
    public ArrayList<String> getColores(){
        //Este método devuelve el arraylist de colores de su mano
        Jugador j = jugadores.get(turno);
        Mano mano = j.mostrarCartas();

        ArrayList<String> colores = new ArrayList<>();

        for(int i=0; i<mano.cantCartas(); i++){
            Carta carta = mano.leerCartaMano(i);
            colores.add(carta.color());
        }
        return colores;
    }
    @Override
    public ArrayList<String> getValores(){
        //Este método devuelve el arraylist de colores de su mano
        Jugador j = jugadores.get(turno);
        Mano mano = j.mostrarCartas();

        ArrayList<String> valores = new ArrayList<>();

        for(int i=0; i<mano.cantCartas(); i++){
            Carta carta = mano.leerCartaMano(i);
            valores.add(String.valueOf(carta.valor()));
        }
        return valores;
    }

    @Override
    public ArrayList<Boolean> getValidas() {
        Condicion condiciones = new Condicion();

        boolean cartasValidas = false;
        Jugador j = jugadores.get(turno);
        Mano mano = j.mostrarCartas();

        ArrayList<Boolean> posibles = new ArrayList<>();

        for(int i=0; i<mano.cantCartas(); i++){
            Carta carta = mano.leerCartaMano(i);
            Carta descarte = mazoDeDescarte.ultimaCarta();
            if(condiciones.sePuedeTirar(descarte, carta)){
                cartasValidas = true;
                posibles.add(true);
            } else {
                posibles.add(false);
            }
        }
        if(!cartasValidas){
            return null;
        } else {
            return posibles;
        }
    }

    @Override
    public boolean tirarCarta(String id, String posCarta) throws RemoteException {//Quizá debería devolver la carta para luego mostrarla?
        int pos = Integer.parseInt(posCarta);
        pos--;//Porque las opciones empiezan en 1, mientras que la mano empieza en 0
        Jugador j = buscarJugador(id);
        Carta carta = j.mostrarCarta(pos);
        Condicion condiciones = new Condicion();
        boolean seTiro = false;
        if(condiciones.sePuedeTirar(mazoDeDescarte.ultimaCarta(), carta)){
            j.tirarCarta(pos);
            mazoDeDescarte.agregar(carta);
            seTiro = true;
        }
        boolean color = false;
        if(seTiro) {
            switch (esEspecial(carta)) {
                //case COMUN -> No hago nada especial si es comun;
                case BLOQUEO:
                    turno = siguienteTurno(turno);
                    turno = siguienteTurno(turno);
                    break;
                case MAS_DOS:
                    this.levantarDosCartas(siguienteTurno(turno));
                    break;
                case MAS_CUATRO:
                    this.levantarCuatroCartas(siguienteTurno(turno));
                    pedirColorJugador();
                    color = true;
                    break;
                case CAMBIO_SENTIDO:
                    sentido = !sentido;
                    break;
                case CAMBIO_COLOR:
                    pedirColorJugador();
                    color = true;
                    break;
            }
        }
        if(j.cantCartasMano() == 0){
            finalizarPartida(j);
        } else if(!color){
            //Si no hay que pedir color al jugador del turno acutal
            //Entonces sigo al siguiente turno
            //Si no espero hasta que me de el color
            siguienteTurno(turno);
            actualizarCartaDescarte();
            actualizarCartasVista();
        }
        return seTiro;
    }
    @Override
    public void pedirColorJugador() throws RemoteException {
        this.notificarObservadores(Eventos.PEDIR_COLOR);
    }
    @Override
    public void elegirColor(String color, String idj) throws RemoteException {
        Carta carta = new Carta(color);
        mazoDeDescarte.agregar(carta);
        Jugador j = buscarJugador(idj);
        j.tirarCartaCambioColor();
        actualizarCartaDescarte();
        actualizarCartasVista();
        siguienteTurno(turno);
    }
    @Override
    public void agregarJugador(String id){
        //Devuelve el id
        /* Debo ver como manejo con RMI el tema de agregar jugadores
        if(jugadores.size() == 4){
            System.out.println("Ya hay demasiados jugadores, intente entrando en otra partida.");
            return null;
        }*/
        Ranking ranking = new Ranking();
        String[] datos = ranking.buscarDatosJugadorID(id);

        Jugador jugador = new Jugador(datos[0], datos[1], datos[2], datos[3]);

        jugadores.add(jugador);
        //reset partida?
        turno = 0;
        //Si se suma un jugador se resetea la partida?
        //Entonces tendría que hacer:
        //turno=1; y también en el main un "partida = new Partida();"
    }
    /*
    Mas que nada comprueba el valor de turno y lo actualiza
     */
    @Override
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
    @Override
    public void finalizarPartida(Jugador ganador){
        Ranking ranking = new Ranking();
        for(Jugador j : jugadores){
            if(j == ganador){
                ganador.actualizarPartidasGanadas();
                ranking.actualizarJugador(ganador);
            } else {
                j.actualizarPartidasPerdidas();
                ranking.actualizarJugador(j);
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
    @Override
    public boolean actualizarCartaDescarte() throws RemoteException {
        if(jugadoresListos == jugadores.size()){
            this.notificarObservadores(Eventos.CARTA_DESCARTE);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean actualizarCartasVista() throws RemoteException {
        if(jugadoresListos == jugadores.size()){
        this.notificarObservadores(Eventos.MOSTRAR_MANO);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String[] getDescarte(){
        Carta carta = mazoDeDescarte.ultimaCarta();
        String datoJunto = carta.color() + "," + carta.valor();
        String[] datos =  datoJunto.split(",");
        return datos;
    }
    @Override
    public boolean esTurno(String id){
        if( (jugadores.get(turno)).jugadorID().equals(id)){
            //Si el jugador por el cual consulto, es el mismo que el de la posicion
            //Del turno, entonces es su turno (Para saber si puede ingresar opcion de carta o no)
            return true;
        } else {
            return false;
        }
    }
    @Override
    public TipoCarta esEspecial(Carta carta){
        switch(carta.valor()){
            case 11: return TipoCarta.MAS_DOS;
            case 12: return TipoCarta.CAMBIO_SENTIDO;
            case 13: return TipoCarta.BLOQUEO;
            case 14: return TipoCarta.MAS_CUATRO;
            case 15: return TipoCarta.CAMBIO_COLOR;
            default: return TipoCarta.COMUN;
        }
    }
    public void levantarCuatroCartas(int pos){
        Jugador j = jugadores.get(pos);
        Carta carta;
        //Roba 4 cartas
        for(int i=0; i<4; i++){
            carta = mazoDeRobo.robar();
            j.levantarCarta(carta);
        }
    }
    public void levantarDosCartas(int pos){
        Jugador j = jugadores.get(pos);
        Carta carta;
        //Roba 2 cartas
        for(int i=0; i<2; i++){
            carta = mazoDeRobo.robar();
            j.levantarCarta(carta);
        }
    }
    public void levantarCarta() throws RemoteException {
        Jugador j = jugadores.get(turno);
        j.levantarCarta(mazoDeRobo.robar());
        actualizarCartasVista();
    }
}