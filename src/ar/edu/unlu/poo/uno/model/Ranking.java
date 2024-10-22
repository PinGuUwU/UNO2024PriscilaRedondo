package ar.edu.unlu.poo.uno.model;


import java.io.*;
import java.util.*;

public class Ranking {
    private String nombreArchivo = "historico.txt";
    //Se llama ranking pero en realidad es el historico

    /*Esto controla el archivo binario del historico de jugadores (top ganadas/perdidas)
    Hago un ABM
    -agregar jugador
    -eliminar jugador? no
    -actualizar jugador
    tambien necesito que me de los datos de los top ganadores/perdedores
    -top5Ganadores
    -top5Perdedores

    tambien hacer un metodo que valide si el archivo esta creado,
    si no esta creado lo crea
    si esta creado no lo crea
    */

    public void Ranking(){
        crearArchivo();//Lo hago para asegurarme que existe, de no existir lo crea
    }
    public void crearArchivo(){
        File archivo = new File(nombreArchivo);
        try{
            if(archivo.createNewFile()){
                System.out.println("Archivo creado");
            } else {
                System.out.println("El archivo ya existe");
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private String getNombreArchivo(){ return this.nombreArchivo; }
    public String agregarJugador(String username){
        //Tuve que usar estructura try catch porque a la hora de usar
        //Los metodos (File) de estos objetos me lo exigían y lo busqué en internet
            int nuevoID = Integer.parseInt(this.ultimoID());
            nuevoID++;
        try (BufferedWriter estructura = new BufferedWriter(new FileWriter(nombreArchivo, true))){
            estructura.write( nuevoID + "," + username + "," + 0 + "," + 0);
            estructura.newLine();
        } catch(IOException e){
            e.printStackTrace();
        }
        return String.valueOf(nuevoID);
    }
    public ArrayList<Jugador> getTopGanadores(){
        ArrayList<Jugador> jugadoresAll = new ArrayList<Jugador>();
        //Aca leo todos los jugadores del .txt y guardo el top 5
        File file = new File(nombreArchivo);
        try(RandomAccessFile archivo = new RandomAccessFile(file, "r")){
            String lineaActual = archivo.readLine();
            while(lineaActual != null){
                String[] datos = lineaActual.split(",");
                Jugador jugador = new Jugador(datos[0], datos[1], datos[2], datos[3]);
                jugadoresAll.add(jugador);
                lineaActual = archivo.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        /*Esto de jugadoresAll.sort sirve para poder recorrer to-do el ArrayList
        y va comparando las partidas ganadas miembro a miembro
        lo ordena de mayor a menor
         */
        jugadoresAll.sort(new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.partidasGanadas(), j1.partidasGanadas());
            }
        });
        //Esta operacion es muy rebuscada
        //.subList(0, 5)  devuelve una sublista de las primeras 5 posiciones (mejores jugadores)
        // y Math.min(5, .size) hace que si hay menos de 5 jugadores en la ArrayList, te devuelva esos que haya
        return new ArrayList<>(jugadoresAll.subList(0, Math.min(5, jugadoresAll.size())));
    }
    public ArrayList<Jugador> getTopPerdedores(){
        ArrayList<Jugador> jugadoresAll = new ArrayList<Jugador>();
        //Aca leo todos los jugadores del .txt y guardo el top 5
        File file = new File(nombreArchivo);
        try(RandomAccessFile archivo = new RandomAccessFile(file, "r")){
            String lineaActual = archivo.readLine();
            while(lineaActual != null){
                String[] datos = lineaActual.split(",");
                Jugador jugador = new Jugador(datos[0], datos[1], datos[2], datos[3]);
                jugadoresAll.add(jugador);
                lineaActual = archivo.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        /*Esto de jugadoresAll.sort sirve para poder recorrer to-do el ArrayList
        y va comparando las partidas ganadas miembro a miembro
        lo ordena de mayor a menor
         */
        jugadoresAll.sort(new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.partidasPerdidas(), j1.partidasPerdidas());
            }
        });
        //Esta operacion es muy rebuscada
        //.subList(0, 5)  devuelve una sublista de las primeras 5 posiciones (mejores jugadores)
        // y Math.min(5, .size) hace que si hay menos de 5 jugadores en la ArrayList, te devuelva esos que haya
        return new ArrayList<>(jugadoresAll.subList(0, Math.min(5, jugadoresAll.size())));
    }
    public String ultimoID(){
        String ultimaLinea = "";
        File archivoFile = new File(nombreArchivo);
        try(RandomAccessFile archivo = new RandomAccessFile(archivoFile.getPath(), "r")){
            long tamanioArchivo = archivo.length();
            if(tamanioArchivo == 0){
                return "0"; //Retorno el primer ID, 1
            }
            //Si el archivo tiene datos, entonces busco el ID del ultimo jguador registrado
            long pos = tamanioArchivo -1;
            archivo.seek(pos);
            while(pos > 0){ //Este while me ayuda para ir hasta la posicion del id
                pos--;
                archivo.seek(pos);
                if(archivo.readByte() == '\n'){
                    break;
                }
            }
            if(pos == 0){
                archivo.seek(0);
            }
            ultimaLinea = archivo.readLine();
        } catch(IOException e){
            e.printStackTrace();
        }
        String[] separado = ultimaLinea.split(",");
        //Separo el contenigo de la ultima linea con el separador ","
        //Retorno el primer valor, que es el id del jugador
        return separado[0];
    }
    public String datosJugador(String id){
        File file = new File(nombreArchivo);
        String lineaBuscada = "";

        try(RandomAccessFile archivo = new RandomAccessFile(file.getPath(), "r")){
            String lineaActual = archivo.readLine();
            //Mientras que la linea actual leida sea != de null
            while ((lineaActual) != null) {
                String[] datos = lineaActual.split(",");
                if(datos[0].equalsIgnoreCase(id)){
                    lineaBuscada = lineaActual;
                    break;
                    //Interrumpo el while para no buscar en to-do el archivo
                }
                lineaActual = archivo.readLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return lineaBuscada;
        //Retorno los datos tal cual está escrito en el archivo
    }
    public String buscarIDJugadorName(String username){
        File file = new File(nombreArchivo);
        String lineaEncontrada = null;
        try(RandomAccessFile archivo = new RandomAccessFile(file.getPath(), "r")){
            String lineaActual = archivo.readLine();
            String[] partes;
            while(lineaActual != null){
                partes = lineaActual.split(",");
                if(partes[1].equalsIgnoreCase(username)){
                    lineaEncontrada = lineaActual;
                    break;
                }
                lineaActual = archivo.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        if(lineaEncontrada == null){
            return null;
        } else {
            String[] datos = lineaEncontrada.split(",");
            return datos[0];
        }
    }
    public String[] buscarDatosJugadorID(String id){
        File file = new File(nombreArchivo);
        String lineaEncontrada = null;
        try(RandomAccessFile archivo = new RandomAccessFile(file.getPath(), "r")){
            String lineaActual = archivo.readLine();
            String[] partes;
            while(lineaActual != null){
                partes = lineaActual.split(",");
                if(partes[0].equalsIgnoreCase(id)){
                    lineaEncontrada = lineaActual;
                    break;
                }
                lineaActual = archivo.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return lineaEncontrada.split(",");
    }
    public void actualizarNombreJugador(String idJ, String nuevoNombre){
        ArrayList<String> lineas = new ArrayList<>();
        boolean jugadorEncontrado = false;

        try(BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))){
            String lineaActual = lector.readLine();
            while(lineaActual != null){
                String[] datos = lineaActual.split(",");
                if(datos[0].equals(idJ)){ //Si encuentro el ID del jugador
                    datos[1] = nuevoNombre;
                    //Guardo la info modificada en el mismo formato que está en el txt
                    lineaActual = String.join(",",datos);
                    jugadorEncontrado = true;
                }
                lineas.add(lineaActual);
                lineaActual = lector.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        if(jugadorEncontrado){
            try(BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))){
                for(String modificado : lineas){
                    escritor.write(modificado);
                    escritor.newLine();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        } else {
            System.out.println("No se encontró el jugador.");
        }
    }
    public void actualizarJugador(Jugador j){
        ArrayList<String> lineas = new ArrayList<>();
        boolean jugadorEncontrado = false;

        try(BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))){
            String lineaActual = lector.readLine();
            while(lineaActual != null){
                String[] datos = lineaActual.split(",");
                if(datos[0].equals(j.jugadorID())){ //Si encuentro el ID del jugador
                    datos[1] = j.name();
                    datos[2] = String.valueOf(j.partidasGanadas());
                    datos[3] = String.valueOf(j.partidasPerdidas());
                    //Guardo la info modificada en el mismo formato que está en el txt
                    lineaActual = String.join(",",datos);
                    jugadorEncontrado = true;
                }
                lineas.add(lineaActual);
                lineaActual = lector.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        if(jugadorEncontrado){
            try(BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))){
                for(String modificado : lineas){
                    escritor.write(modificado);
                    escritor.newLine();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        } else {
            System.out.println("No se encontró el jugador.");
        }
    }
}
