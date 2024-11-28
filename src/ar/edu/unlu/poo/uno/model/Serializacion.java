package ar.edu.unlu.poo.uno.model;

import java.io.*;
import java.util.ArrayList;

public class Serializacion implements Serializable {
    private static final String datosJugadores = "PlayersData.dat";
    private static final String partidasGuardadas = "GamesData.dat";
    /*
    OBJETIVO DE LA CLASE:
        GUARDAR INFORMACIÓN DE LOS JUGADORES
        GUARDAR PARTIDAS

    Así que a la hora de guardar debería de:
        tener un método que le paso el nombre del archivo y el dato a guardar

    métodos posibles:
        escribirDatosJugador() /que compruebe si existe o no el jugador, si existe lo sobreescribe, si no lo agrega al final
        leerDatosJugador() devuelve un 'string[]' con los datos 'id, name, pg, pp'
        ultimoID()
     */
    //Jugadores
    public static String escribirDatosJugador(String name) throws IOException, ClassNotFoundException {
        //nuevo jugador
        // Obtener el ID del último jugador
        int nuevoID = Integer.parseInt(ultimoID());
        // Crear el nuevo jugador con el nuevo ID
        Jugador nuevoJugador = new Jugador(String.valueOf(nuevoID), name, "0", "0");

        File archivo = new File(datosJugadores);
        ArrayList<Jugador> jugadores;

        // Cargar la lista de jugadores existente
        if (archivo.exists() && archivo.length() > 0) {
            // Si el archivo existe y no está vacío, cargar los jugadores
            try (FileInputStream inputStream = new FileInputStream(archivo);
                 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                jugadores = (ArrayList<Jugador>) objectInputStream.readObject();
            }
        } else {
            // Si el archivo no existe o está vacío, inicializar una lista vacía
            jugadores = new ArrayList<>();
        }

        // Agregar el nuevo jugador a la lista
        jugadores.add(nuevoJugador);

        // Escribir la lista completa de jugadores en el archivo
        try (FileOutputStream outputStream = new FileOutputStream(archivo);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(jugadores);
        }

        return String.valueOf(nuevoID);
    }
    public static String leerDatosJugador(String name) throws IOException, ClassNotFoundException {
        //jugador existente
        String id = "";
        var inputStream = new FileInputStream(datosJugadores);
        var objectInputStream = new ObjectInputStream(inputStream);
        ArrayList<Jugador> jugadores = (ArrayList<Jugador>) objectInputStream.readObject();
        for (Jugador j : jugadores) {
            if(j.name().equalsIgnoreCase(name)){
                id = j.jugadorID();
            }
        }
        return id;
    }
    public static String ultimoID() throws IOException, ClassNotFoundException {
        File archivo = new File(datosJugadores);
        if (!archivo.exists()) {
            return "1";
        }
        // Busco el último ID
        try (FileInputStream inputStream = new FileInputStream(datosJugadores);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

            ArrayList<Jugador> jugadores = (ArrayList<Jugador>) objectInputStream.readObject();

            // Si no hay jugadores, retorno el ID 1
            if (jugadores.isEmpty()) {
                return "1";
            }
            // Obtiene el ID del último jugador
            Jugador ultimoJugador = jugadores.get(jugadores.size() - 1);
            int id = Integer.parseInt(ultimoJugador.jugadorID());
            id++;
            return String.valueOf(id);
        }
    }
    public static boolean existeJugador(String name) throws IOException, ClassNotFoundException {
        //Compruebo acá si el archivo existe ya que es el primer método que se utiliza de Serializacion en todo el juego
        File archivo = new File(datosJugadores);
        // Comprueba si el archivo existe; si no, lo crea e inicializa con un ArrayList vacío
        if (!archivo.exists()) {
            archivo.createNewFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                oos.writeObject(new ArrayList<Jugador>());
            }
        }

        // Lee el archivo y verifica si el jugador existe
        try (FileInputStream inputStream = new FileInputStream(archivo);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

            ArrayList<Jugador> jugadores = (ArrayList<Jugador>) objectInputStream.readObject();
            for (Jugador j : jugadores) {
                if (j.name().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        } catch (EOFException e) {
            // Esto ocurre si el archivo está vacío, ignoramos porque significa que no hay jugadores
        }
        return false;
    }
    public static Jugador buscarDatosDeJugadorPorID(String id) throws IOException, ClassNotFoundException {
        File archivo = new File(datosJugadores);

        Jugador jugador = null;
        // Leo la lista de jugadores del archivo
        try (FileInputStream inputStream = new FileInputStream(archivo);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

            ArrayList<Jugador> jugadores = (ArrayList<Jugador>) objectInputStream.readObject();

            for (Jugador j : jugadores) {
                if (j.jugadorID().equalsIgnoreCase(id)) {
                    jugador = j;
                }
            }
        } catch (EOFException | ClassCastException e) {
            e.printStackTrace();
        }
        return jugador;
    }
    public static void actualizarNombreJugador(String id, String nuevoNombre) throws IOException, ClassNotFoundException {
        File archivo = new File(datosJugadores);

        ArrayList<Jugador> jugadores;
        try (FileInputStream inputStream = new FileInputStream(archivo);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

            jugadores = (ArrayList<Jugador>) objectInputStream.readObject();
        }

        for (Jugador j : jugadores) {
            if (j.jugadorID().equalsIgnoreCase(id)) {
                j.actualizarNombre(nuevoNombre);
                break;
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(archivo);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {

            objectOutputStream.writeObject(jugadores);
        }
    }
    public static void actualizarJugador(Jugador jugador) throws IOException, ClassNotFoundException {
        File archivo = new File(datosJugadores);
        ArrayList<Jugador> jugadores;



        // lista de jugadores existentes
        try (FileInputStream inputStream = new FileInputStream(archivo);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            jugadores = (ArrayList<Jugador>) objectInputStream.readObject();
        }

        // Buscar el jugador para actualizarlo
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).jugadorID().equalsIgnoreCase(jugador.jugadorID())) {
                jugadores.set(i, jugador); // Actualizo el jugador
                //Test
                System.out.println("ganadas:"+jugador.partidasGanadas()+"perdidas:"+jugador.partidasPerdidas());
                break;
            }
        }

        // Escribo la lista con el jugador actualizado
        try (FileOutputStream outputStream = new FileOutputStream(archivo);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(jugadores);
        }
    }
    public static ArrayList<Jugador> jugadores() throws IOException, ClassNotFoundException {
        File archivo = new File(datosJugadores);
        ArrayList<Jugador> jugadores;
        try (FileInputStream inputStream = new FileInputStream(archivo);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            jugadores = (ArrayList<Jugador>) objectInputStream.readObject();
        } catch (EOFException | ClassCastException e) {
            // Si el archivo está vacío o tiene un formato incorrecto, devuelve una lista vacía
            jugadores = new ArrayList<>();
        }
        return jugadores;
    }

    //METODOS PARA GUARDAR, CARGAR Y ELIMINAR PARTIDAS
    public static void eliminarPartida(Partida partida) throws IOException, ClassNotFoundException {
        //La carga y el guardado de partidas se va a hacer desde una pestaña llamada "partidas" se guardará
        //en el momento en que se clickee "guardar", no cuando un jugador se desconecte
        File archivo = new File(partidasGuardadas);
        ArrayList<Partida> partidasGuardadas;

        if(archivo.exists() && archivo.length() > 0){
            try (FileInputStream inputStream = new FileInputStream(archivo);
                 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                partidasGuardadas = (ArrayList<Partida>) objectInputStream.readObject();
            }
        } else {
            partidasGuardadas = new ArrayList<>();
        }

       partidasGuardadas.remove(partida);

        try (FileOutputStream outputStream = new FileOutputStream(archivo);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(partidasGuardadas);
        }
    }
    public static void guardarPartida(Partida partida) throws IOException, ClassNotFoundException {
        //La carga y el guardado de partidas se va a hacer desde una pestaña llamada "partidas" se guardará
        //en el momento en que se clickee "guardar", no cuando un jugador se desconecte
        File archivo = new File(partidasGuardadas);
        ArrayList<Partida> partidasGuardadas;

        if(archivo.exists() && archivo.length() > 0){
            try (FileInputStream inputStream = new FileInputStream(archivo);
                 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                partidasGuardadas = (ArrayList<Partida>) objectInputStream.readObject();
            }
        } else {
            partidasGuardadas = new ArrayList<>();
        }

        partidasGuardadas.add(partida);

        try (FileOutputStream outputStream = new FileOutputStream(archivo);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(partidasGuardadas);
        }
    }
    public static ArrayList<Partida> partidasGuardadas(String id) throws IOException, ClassNotFoundException {
        /*
        Este método busca las partidas guardadas que tiene cierto jugador y las retorna para
        Porder mostrarlas en una vista, la vista se encargará, junto con el controlador correspondiente, de
        informarle al jugador si puede o no cargar esa partida
        El controlador también deberá hacerse cargo de verificar si están los jugadores necesarios para
        retomar esa partida.
         */
        File archivo = new File(partidasGuardadas);
        ArrayList<Partida> partidasGuardadas;
        if(archivo.exists() && archivo.length() > 0){
            try (FileInputStream inputStream = new FileInputStream(archivo);
                 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                partidasGuardadas = (ArrayList<Partida>) objectInputStream.readObject();
            }
        } else {
            partidasGuardadas = new ArrayList<>();
        }

        ArrayList<Partida> partidasJugador = new ArrayList<>();
        for(Partida p: partidasGuardadas){
            Jugador encontrado = p.buscarJugador(id);
            //Si la partida contiene a ese jugador la agrego al array que se devolverá
            if(encontrado != null){
                partidasJugador.add(p);
            }
        }

        return partidasJugador;
    }
}
