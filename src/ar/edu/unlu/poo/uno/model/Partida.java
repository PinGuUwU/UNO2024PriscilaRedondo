package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.*;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Partida extends ObservableRemoto implements IPartida, Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private int jugadoresListos = 0;
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private boolean sentido = true;//true = incrementa turno. false = decrementa turno;
    private TurnoPartida turno; //Quizá podría servir hacer una clase que lleve esto
    private MazoDeRobo mazoDeRobo;
    public MazoDeDescarte mazoDeDescarte;

    public long getId(){
        return id;
    }

    public Partida(long id) {
        this.id = id;
        mazoDeDescarte = new MazoDeDescarte();
        mazoDeRobo = new MazoDeRobo();
    }

    public void iniciarPartida() throws IOException, ClassNotFoundException {
        mazoDeRobo.inicializarMazo(); //Inicializo el mazo de cartas de robo
        for(int i=0; i<7; i++){//Cada jugador levanta 7 cartas
            for(Jugador j : jugadores){
                Carta carta = mazoDeRobo.robar(); //Voy levantando cartas y repartiendolas
                j.levantarCarta(carta);
            }
        }
        turno = new TurnoPartida(jugadores.size());
        actualizarInicioPartida();
        Carta carta = mazoDeRobo.robar();
        while(carta.valor() != TipoCarta.COMUN){//Fuerzo a que sea una carta comun
            carta = mazoDeRobo.robar();
        }
        mazoDeDescarte.agregar(carta);//Pongo la carta inicial en el mazo de descarte
        iniciarTurno();
    }

    public void bloquear(){
        turno.seguirTurno();
    }

    public void iniciarTurno() throws IOException, ClassNotFoundException {
        Jugador jugadorActual = jugadores.get(turno.turnoActual());
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
        turno.siguienteTurno();
    }

    public void finalizarPartida(Jugador ganador) throws IOException, ClassNotFoundException {
        for(Jugador j : jugadores){
            if(j == ganador){
                ganador.actualizarPartidasGanadas();
                System.out.println("Ganador\nPartidas Ganadas:"+j.partidasGanadas()+"Perdidas"+j.partidasPerdidas());
                Serializacion.actualizarJugador(ganador);
            } else {
                j.actualizarPartidasPerdidas();
                System.out.println("Perdedor\nPartidas Ganadas:"+j.partidasGanadas()+"Perdidas"+j.partidasPerdidas());
                Serializacion.actualizarJugador(j);
            }
            for(int i=0; i< j.mostrarCartas().cantCartas(); i++){
                //Borro todas las cartas de las manos de los jugadores
                j.tirarCarta(0);
            }
        }
        actualizarPorCambio();
        actualizarJugadoresNoListos();
        jugadoresListos=0;
    }

    public boolean estadoPartida() throws RemoteException{
        return jugadores.size() == cantJugadoresListos();
    }

    public Jugador buscarJugador(String idJugador){
       Jugador j = null;
        for (Jugador jugador : jugadores) {
            if ((jugador.jugadorID()).equals(idJugador)) {
                j = jugador;
            }
        }
        return j;
    }

    public void levantarCartaEspecial(int sigTurno) throws RemoteException {
        /*
        Usado cuando un jugador esta obligado a levantar cartas
        ya sea por una carta accion, por un desafio o porque no dijo uno y alguien aviso a tiempo
         */
        if(mazoDeRobo.sinCartas()){
            mazoDeRobo.inicializarMazo();
        } //Busco al siguiente jugador en la ronda para que robe las cartas que le corresponde
        Jugador j = jugadores.get(sigTurno);
        j.levantarCarta(mazoDeRobo.robar());
    }

    public void levantarCarta() throws RemoteException {
        //Alguien levanta una carta
        if(mazoDeRobo.sinCartas()){
            mazoDeRobo.inicializarMazo();
        }
        Jugador j = jugadores.get(turno.turnoActual());
        j.levantarCarta(mazoDeRobo.robar());
        actualizarCartasVista();
    }

    public void cambioDeSentido(){
        this.sentido = !sentido;
    }

    public void desafiadoLevantaCuatroCartas() throws RemoteException {
        /*
        Si desafiaron a un jugador (+4) y tiro de forma ilegar la carta
        el desafiado debera levantar 4 cartas
         */
        for(int i=0; i<4; i++){
            levantarCartaEspecial(turno.turnoActual());
        }
    }

    public void levantarSeisCartas() throws RemoteException {
        /*
        Si desafiaron a un jugador (+4) y tiro de forma legar la carta
        el desafiador debera levantar 6 cartas
         */
        for(int i=0; i<6; i++){
            levantarCartaEspecial(turno.turnoAnterior());
        }
    }

    public void levantarCartas(int cant) throws RemoteException {
        //Cuando alguien tira +2
        int sigTurno = turno.siguienteTurno();
        for(int i=0; i<cant; i++){
            levantarCartaEspecial(sigTurno);
        }
        turno.seguirTurno();
    }

    //Métodos de notificación

    private void actualizarSeApeloUNO() throws RemoteException {
        this.notificarObservadores(Eventos.SE_APELO_EL_UNO);
    }

    private void avisarJugadorPuedeDesafiar() throws RemoteException {
        turno.seguirTurno();
        this.notificarObservadores(Eventos.PUEDE_DESAFIAR);
    }

    private void actualizarJugadoresVista() throws RemoteException {
        this.notificarObservadores(Eventos.CAMBIO_JUGADORES);
    }

    private void actualizarJugadoresNoListos() throws RemoteException {
        /*
        Aviso a los jugadores que la partida comenzo, fusionar con "actualizarInicioPartida();"
         */
        this.notificarObservadores(Eventos.NUEVA_PARTIDA);
    }

    private void actualizarInicioPartida() throws RemoteException {
         /*
        Aviso a los jugadores que la partida comenzo
         */
        this.notificarObservadores(Eventos.INICIAR_PARTIDA);
    }

    public void pedirColorJugador() throws RemoteException {
        /*
        Aviso a los jugadores que se espera que la persona con el turno actual
        elija el color al cual quiere cambiar
         */
        this.notificarObservadores(Eventos.PEDIR_COLOR);
    }

    private void actualizarPorCambio() throws RemoteException {
        this.notificarObservadores(Eventos.CAMBIO);
    }

    public void actualizarNoDijoUNO() throws RemoteException {
        this.notificarObservadores(Eventos.NO_DIJO_UNO);
    }

    public void actualizarPorCargaDePartida() throws RemoteException {
        this.notificarObservadores(Eventos.CARGAR_PARTIDA);
    }

    //Métodos IPartida

    @Override
    public Mano manoJugadorAnterior() throws RemoteException {
        //Devuelve la Mano del jugador con el turno anterior
        return jugadores.get(turno.turnoAnterior()).mostrarCartas();
    }

    @Override
    public void elegirColor(Color color, String idj) throws RemoteException {
        //Un jugador cambió el color
        CartaVacia carta = new CartaVacia(color);
        mazoDeDescarte.agregar(carta);
        actualizarCartaDescarte();
        actualizarCartasVista();
        actualizarPorCambio();
        if(mazoDeDescarte.anteultimaCarta().valor() == TipoCarta.MAS_CUATRO){
            //Si tiró un +4, avisa al jugador que puede desafiar
            avisarJugadorPuedeDesafiar();
        }
        turno.seguirTurno();
    }
    @Override
    public void quitarJugador(String idJugador) throws IOException, ClassNotFoundException {
        //Un jugador se desconecto y debo sacarlo, la partida empieza de 0
        //No sé si funciona el método, no lo he testeado
        Jugador j = buscarJugador(idJugador);
        jugadores.remove(j);
        jugadoresListos=0;
        iniciarPartida();
    }

    @Override
    public boolean agregarJugador(String id) throws IOException, ClassNotFoundException {
        //agrego un jugador a la partida
        if(jugadores.size() == 4){
            return false; //No se puede agregar
        } else {
            Jugador jugador = Serializacion.buscarDatosDeJugadorPorID(id);
            if(buscarJugador(id) == null){
                jugadores.add(jugador);
                //reset partida?
                turno = new TurnoPartida(jugadores.size());
                //Si se suma un jugador se resetea la partida?
            }
            return true; //Se puede agregar
        }
    }

    @Override
    public int buscarNumeroCarta(int pos, String idJ){
        //Devuelvo el valor numérico de la carta del jugador y en la posición pasada por parámetro
        Jugador j = buscarJugador(idJ); //Busco al jugador
        int valor = -1;
        try{//Intengo convertirla a carta numérica, si da error, devuelvo -1
            CartaNumerica cAux = (CartaNumerica) j.mostrarCarta(pos);
            valor = cAux.getValor(); //Si no da error, devuelvo el valor int encontrado
        } catch (ClassCastException e){
            e.printStackTrace();
        }
        return valor;
    }

    @Override
    public boolean actualizarCartaDescarte() throws RemoteException {
        /*
        Aviso a los controladores que la carta de descarte cambio y deben actualizarla para los jugadores
         */
        if(jugadoresListos == jugadores.size()){
            this.notificarObservadores(Eventos.CARTA_DESCARTE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean actualizarCartasVista() throws RemoteException {
        //Le aviso a los controladores que deben actualizar la vista de cartas de los jugadores
        if(jugadoresListos == jugadores.size()){
            this.notificarObservadores(Eventos.MOSTRAR_MANO);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void jugadorPaso() throws IOException, ClassNotFoundException {
        turno.seguirTurno();
        iniciarTurno();
    }

    @Override
    public void noDijoUNO() throws RemoteException {
        /*
        Un controlador mandó el mensaje de que un jugador no dijo uno y otro jugador lo apeló
         */
        levantarCartaEspecial(turno.turnoAnterior());
        levantarCartaEspecial(turno.turnoAnterior());
        actualizarSeApeloUNO();
    }

    @Override
    public void agregarJugadorListo() throws IOException, ClassNotFoundException {
        /*
        sumo un jugador listo, cuando todos estén listos empieza la partida
         */
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
    public ArrayList<Jugador> jugadores(){
        return jugadores;
    }
    @Override
    public Color getColorDescarte() throws RemoteException{
        /*
        Obtengo el color de la carta de descarte para
        poder actualizarlo en la vista de los jugadores
         */
        Carta carta = mazoDeDescarte.ultimaCarta();
        return carta.color();
    }
    @Override
    public TipoCarta getTipoDescarte() throws RemoteException{
        /*
        Obtengo el tipo de la carta de descarte para
        poder actualizarlo en la vista de los jugadores
         */
        Carta carta = mazoDeDescarte.ultimaCarta();
        return carta.valor();
    }
    @Override
    public int getNumeroDescarte() throws RemoteException{
        /*
        Si la carta de descarte es numerica, obtengo su valor para poder
        actualizarlo en la vista de los jugadores
         */
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
        Jugador j = jugadores.get(turno.turnoActual());
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
        //Este método devuelve el arraylist de qué cartas son válidas de su mano
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
    public boolean tirarCarta(String id, String posCarta, boolean uno) throws IOException, ClassNotFoundException {//Quizá debería devolver la carta para luego mostrarla?
        int pos = Integer.parseInt(posCarta);
        pos--;//Porque las opciones empiezan en 1, mientras que la mano empieza en 0

        Jugador j = buscarJugador(id);
        Carta carta = j.mostrarCarta(pos);

        boolean color = false;
        boolean seTiro = false;
        if(Condicion.sePuedeTirar(mazoDeDescarte.ultimaCarta(), carta)) {
            seTiro = true;
            j.tirarCarta(pos);
            color = carta.jugar(this);
            /**
             MIRAR POR QUÈ CUANDO PIDE COLOR TAMBIEN LEVANTA CARTAS HASTA PODER TIRAR UNA
             SE SUPONE QUE ESPERE A PEDIR COLOR Y FIN
             **/
        }
        if(carta.valor() == TipoCarta.MAS_CUATRO){
            //Si es un mas cuatro debo activar desafio LUEGO de que elija el color, entonces
            //Agrego la carta al descarte
            mazoDeDescarte.agregar(carta);
            //Y ahora la máquina estará esperando que se elija el color
            //Luego de elegir el color, si se inicia un desafio
            //Se mirará la carta anterior al +4 y corroborará si se tiró de forma legal o ilegal
        }
        boolean finalizo = false;
        if(j.cantCartasMano() == 0){
            finalizarPartida(j);
            finalizo = true;
        }
        if(j.mostrarCartas().cantCartas() == 1 && !uno){
            //Si le queda una única carta entonces y no dijo uno, los demás podrán apelar
            actualizarNoDijoUNO();
        }
        if(!color && seTiro && !finalizo){
            //Si no hay que pedir color al jugador del turno acutal
            //Entonces sigo al siguiente turno
            //Si no espero hasta que me de el color
            mazoDeDescarte.agregar(carta);
            turno.seguirTurno();
            actualizarCartaDescarte();
        }//Si debe elegir color entonces el color de la carta y el descarte se actualizan en otro metodo
        actualizarCartasVista();
        return seTiro;
    }

    @Override
    public void desafio() throws RemoteException {
        //Aca le aviso al del turno anterior que fue desafiado
        Mano manoJ = manoJugadorAnterior();
        Carta descarte = mazoDeDescarte.anteultimaCarta();
        if(Condicion.tieneParaTirar(manoJ, descarte)){
            //Si tenia para tirar, entonces debera levantar esas 4 cartas como castigo
            //Por tirarla de forma ilegal, el turno sigue normal para quien lo desafio
            desafiadoLevantaCuatroCartas();
        } else {
            //Si el jugador no tenia para tirar, el que desafio
            //Levantara 6 cartas
            levantarSeisCartas();
        }
        actualizarPorCambio();
        turno.seguirTurno();
    }

    @Override
    public int cantJugadoresListos(){
        System.out.println("cant jugadores: "+jugadores.size()+"cant jugadores listos:"+jugadoresListos);
        return jugadoresListos;
    }

    @Override
    public void cargarPartida(long id) throws IOException {
        //Método que utiliza ControladorPartidaGuardada para cargar una partida previamente cargada
        Partida nuevaPartida = Serializacion.buscarPartidaPorID(id);
        this.id = nuevaPartida.id;
        this.sentido = nuevaPartida.sentido;
        this.turno = nuevaPartida.turno;
        this.jugadores = nuevaPartida.jugadores;
        this.mazoDeDescarte = nuevaPartida.mazoDeDescarte;
        this.mazoDeRobo = nuevaPartida.mazoDeRobo;
        actualizarPorCargaDePartida(); //Creo que esto debería hacer que to-do se pueda cargar bien
    }

    @Override
    public void noDesafia() throws RemoteException {
        //El controlador le avisa a la partida que el jugador no desafía, entonces se puede seguir la partida
        turno.seguirTurno();
    }

    @Override
    public void guardarPartida() throws IOException, ClassNotFoundException {
        //Método que utiliza el ControladorPartidaGuardada para guardar partidas
        Serializacion.guardarPartida(this);
    }

}