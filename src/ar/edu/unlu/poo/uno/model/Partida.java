package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.*;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Partida extends ObservableRemoto implements IPartida, Serializable {
    private int jugadoresListos = 0;
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private boolean sentido = true;//true = incrementa turno. false = decrementa turno;
    private int turno; //Quizá podría servir hacer una clase que lleve esto
    private MazoDeRobo mazoDeRobo;
    public MazoDeDescarte mazoDeDescarte;

    public Partida() {
        mazoDeDescarte = new MazoDeDescarte();
        mazoDeRobo = new MazoDeRobo();
    }

    public void iniciarPartida() throws RemoteException {
        mazoDeRobo.inicializarMazo(); //Inicializo el mazo de cartas de robo
        for(int i=0; i<7; i++){//Cada jugador levanta 7 cartas
            for(Jugador j : jugadores){
                Carta carta = mazoDeRobo.robar(); //Voy levantando cartas y repartiendolas
                j.levantarCarta(carta);
            }
        }
        turno = 0;
        actualizarInicioPartida();
        Carta carta = mazoDeRobo.robar();
        while(carta.valor() != TipoCarta.COMUN){//Fuerzo a que sea una carta comun
            carta = mazoDeRobo.robar();
        }
        mazoDeDescarte.agregar(carta);//Pongo la carta inicial en el mazo de descarte
        iniciarTurno();
    }
    @Override
    public int cantJugadoresListos(){
        System.out.println("cant jugadores: "+jugadores.size()+"cant jugadores listos:"+jugadoresListos);
        return jugadoresListos;
    }

    public void iniciarTurno() throws RemoteException {
        Jugador jugadorActual = jugadores.get(turno);
        if(mazoDeRobo.sinCartas()){
            mazoDeRobo.inicializarMazo();
        }
        if(jugadorActual.cantCartasMano() == 0){
            finalizarPartida(jugadorActual);
        }
        actualizarCartaDescarte();
        actualizarCartasVista();
        /*
        Si tiene cartas para tirar, puede tirar,
        si no estará obligado a levantar del mazo de robo hasta que quiera o pueda tirar
         */
        this.turno = siguienteTurno();
    }

    public int siguienteTurno() throws RemoteException {
        int turnoActual = this.turno;
        //Turno del 1-4
        //Debo corroborar que todos tengan cartas
        for(Jugador j : jugadores){
            if(j.cantCartasMano() == 0){
                finalizarPartida(j);
                break;
            }
        }
        if(sentido){
            if(turnoActual== (jugadores.size()-1)){
                return 0;
            } else {
                turnoActual++;
                return turnoActual;
            }
        } else { //if(!sentido)
            if(turnoActual == 0){
                return (jugadores.size()-1);
            } else {
                turnoActual--;
                return turnoActual;
            }
        }
    }

    public void finalizarPartida(Jugador ganador) throws RemoteException {
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
        actualizarJugadoresNoListos();
        jugadoresListos=0;
        turno=-1;
    }

    public boolean estadoPartida() throws RemoteException{
        return jugadores.size() == cantJugadoresListos();
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

    public void levantarCuatroCartas() throws RemoteException {
        //Roba 4 cartas
        int sigTurno = siguienteTurno();
        for(int i=0; i<4; i++){
            levantarCartaEspecial(sigTurno);
        }
    }

    public void levantarDosCartas() throws RemoteException {
        int sigTurno = siguienteTurno();
        for(int i=0; i<2; i++){
            levantarCartaEspecial(sigTurno);
        }
    }

    public void levantarCartaEspecial(int sigTurno) throws RemoteException {
        if(mazoDeRobo.sinCartas()){
            mazoDeRobo.inicializarMazo();
        } //Busco al siguiente jugador en la ronda para que robe las cartas que le corresponde
        Jugador j = jugadores.get(sigTurno);
        j.levantarCarta(mazoDeRobo.robar());
    }

    public void levantarCarta() throws RemoteException {
        if(mazoDeRobo.sinCartas()){
            mazoDeRobo.inicializarMazo();
        }
        Jugador j = jugadores.get(turno);
        j.levantarCarta(mazoDeRobo.robar());
        actualizarCartasVista();
    }



    //IPARTIDA

    @Override
    public void agregarJugadorListo() throws RemoteException {
        jugadoresListos+=1;
        actualizarJugadoresVista();
        if(estadoPartida()){
            iniciarPartida();
        }
    }

    @Override
    public int cantJugadores(){
        return jugadores.size();
    }

    @Override
    public Color getColorDescarte() throws RemoteException{
        Carta carta = mazoDeDescarte.ultimaCarta();
        return carta.color();
    }
    @Override
    public TipoCarta getTipoDescarte() throws RemoteException{
        Carta carta = mazoDeDescarte.ultimaCarta();
        return carta.valor();
    }
    @Override
    public int getNumeroDescarte() throws RemoteException{
        Carta carta = mazoDeDescarte.ultimaCarta();
        try{
            CartaNumerica cAux = (CartaNumerica) carta;
            return cAux.getValor();
        } catch (NullPointerException e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean esTurno(String id){
        //Si el jugador por el cual consulto, es el mismo que el de la posicion
        //Del turno, entonces es su turno (Para saber si puede ingresar opcion de carta o no)
        Jugador j = jugadores.get(turno);
        if(jugadoresListos==0){
            return false;
        } else return j.jugadorID().equalsIgnoreCase(id);
    }

    @Override
    public ArrayList<Color> getColores(String id){
        //Este método devuelve el arraylist de colores de su mano
        Jugador j = buscarJugador(id);
        Mano mano = j.mostrarCartas();

        ArrayList<Color> colores = new ArrayList<>();

        for(int i=0; i<mano.cantCartas(); i++){
            Carta carta = mano.leerCartaMano(i);
            colores.add(carta.color());
        }
        return colores;
    }

    @Override
    public ArrayList<TipoCarta> getValores(String id){
        //Este método devuelve el arraylist de colores de su mano
        Jugador j = buscarJugador(id);
        Mano mano = j.mostrarCartas();

        ArrayList<TipoCarta> valores = new ArrayList<>();

        for(int i=0; i<mano.cantCartas(); i++){
            Carta carta = mano.leerCartaMano(i);
            valores.add(carta.valor());
        }
        return valores;
    }

    @Override
    public ArrayList<Boolean> getValidas(String id) {
        Condicion condiciones = new Condicion();

        Jugador j = buscarJugador(id);
        Mano mano = j.mostrarCartas();

        ArrayList<Boolean> posibles = new ArrayList<>();

        for(int i=0; i<mano.cantCartas(); i++){
            Carta carta = mano.leerCartaMano(i);
            Carta descarte = mazoDeDescarte.ultimaCarta();
            if(condiciones.sePuedeTirar(descarte, carta)){
                posibles.add(true);
            } else {
                posibles.add(false);
            }
        }
        return posibles;
    }

    @Override
    public boolean tirarCarta(String id, String posCarta) throws RemoteException {//Quizá debería devolver la carta para luego mostrarla?
        int pos = Integer.parseInt(posCarta);
        pos--;//Porque las opciones empiezan en 1, mientras que la mano empieza en 0

        Jugador j = buscarJugador(id);
        Carta carta = j.mostrarCarta(pos);

        Condicion condiciones = new Condicion();

        boolean seTiro = false;
        boolean color = false;
        if(condiciones.sePuedeTirar(mazoDeDescarte.ultimaCarta(), carta)) {
            seTiro = true;
            j.tirarCarta(pos);
            color = carta.jugar(this);
            /**
           MIRAR POR QUÈ CUANDO PIDE COLOR TAMBIEN LEVANTA CARTAS HASTA PODER TIRAR UNA
             SE SUPONE QUE ESPERE A PEDIR COLOR Y FIN
             **/
        }
        boolean finalizo = false;
        if(j.cantCartasMano() == 0){
            finalizarPartida(j);
            finalizo = true;
        }
        if(!color && seTiro && !finalizo){
            //Si no hay que pedir color al jugador del turno acutal
            //Entonces sigo al siguiente turno
            //Si no espero hasta que me de el color
            mazoDeDescarte.agregar(carta);
            siguienteTurno();
            actualizarCartaDescarte();
        }//Si debe elegir color entonces el color de la carta y el descarte se actualizan en otro metodo
        actualizarCartasVista();
        return seTiro;
    }
    public void cambioDeSentido(){
        this.sentido = !sentido;
    }

    @Override
    public void elegirColor(Color color, String idj) throws RemoteException {
        CartaVacia carta = new CartaVacia(color);
        mazoDeDescarte.agregar(carta);
        actualizarCartaDescarte();
        actualizarCartasVista();
        siguienteTurno();
    }
    @Override
    public void quitarJugador(String idJugador) throws RemoteException {
        Jugador j = buscarJugador(idJugador);
        jugadores.remove(j);
        jugadoresListos=0;
        iniciarPartida();
    }

    @Override
    public boolean agregarJugador(String id){
        if(jugadores.size() == 4){
            return false; //No se puede agregar
        } else {
            Ranking ranking = new Ranking();
            String[] datos = ranking.buscarDatosJugadorID(id);

            Jugador jugador = new Jugador(datos[0], datos[1], datos[2], datos[3]);
            if(buscarJugador(id) == null){
                jugadores.add(jugador);
                //reset partida?
                turno = 0;
                //Si se suma un jugador se resetea la partida?
            }
            return true; //Se puede agregar
        }
    }
    @Override
    public int buscarNumeroCarta(int pos, String idJ){
        Jugador j = buscarJugador(idJ);
        Carta carta = j.mostrarCarta(pos);
        int valor = -1;
        try{
            CartaNumerica cAux = (CartaNumerica) j.mostrarCarta(pos);
            valor = cAux.getValor();
        } catch (ClassCastException e){
            e.printStackTrace();
        }
        return valor;
    }
            //OBSERVABLE REMOTO

    public void actualizarJugadoresNoListos() throws RemoteException {
        this.notificarObservadores(Eventos.NUEVA_PARTIDA);
    }

    public void actualizarInicioPartida() throws RemoteException {
        this.notificarObservadores(Eventos.INICIAR_PARTIDA);
    }

    public void pedirColorJugador() throws RemoteException {
        this.notificarObservadores(Eventos.PEDIR_COLOR);
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
    public void actualizarJugadoresVista() throws RemoteException {
        this.notificarObservadores(Eventos.CAMBIO_JUGADORES);
    }
    @Override
    public void actualizarDesafio() throws RemoteException {
        this.notificarObservadores(Eventos.DESAFIO);
    }

    @Override
    public void noDijoUNO() throws RemoteException {
        /*
        Chequeo si no dijo uno?
        debo hacer un metodo que avise cuándo el jugador ya tiro, eso haría que ya no pueda "avisar"
        Es un evento nuevo
         */
    }

    @Override
    public void desafio() throws RemoteException {

    }
}