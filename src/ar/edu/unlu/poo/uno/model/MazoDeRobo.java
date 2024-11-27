package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MazoDeRobo implements Serializable {
    private ArrayList<Carta> mazo = new ArrayList<>();
    public Carta robar(){//Debo de retirar una carta al azar, porque el mazo está en orden.
        Random random = new Random(System.currentTimeMillis());
        int numAleatorio = random.nextInt(mazo.size());
        Carta carta = mazo.get(numAleatorio);
        mazo.remove(numAleatorio); //Primero elimino esa carta del mazo
        return carta;//Luego se la doy al "jugador"
    }
    /*
    public void inicializarMazo(){
        for(Color color: Arrays.asList(Color.ROJO, Color.AZUL, Color.VERDE, Color.AMARILLO)){
            //Inicializo el 0
            mazo.add(new CartaNumerica(color,0));

            //Inicializo las numericas (1-9)
            for(int i=1; i<10; i++){
                mazo.add(new CartaNumerica(color,i));
            }

            //inicializo las especiales con color
            for(int i=0; i<2; i++){
                mazo.add(new CartaAccion(color, TipoCarta.CAMBIO_SENTIDO));
                mazo.add(new CartaAccion(color, TipoCarta.MAS_DOS));
                mazo.add(new CartaAccion(color, TipoCarta.BLOQUEO));
            }

        }
        //Inicializo las especiales sin color
        for(int i=0; i<2; i++){
            mazo.add(new CartaEspecial(TipoCarta.CAMBIO_COLOR));
            mazo.add(new CartaEspecial(TipoCarta.MAS_CUATRO));
        }

    }*/
    //INICIALIZAR PARA PRUEBAS RÁPIDAS DE UN SOLO COLOR
     public void inicializarMazo(){
        for(Color color: List.of(Color.VERDE)){
            //Inicializo el 0
            mazo.add(new CartaNumerica(color,0));

            //Inicializo las numericas (1-9)
            for(int i=1; i<10; i++){
                mazo.add(new CartaNumerica(color,i));
            }

            //inicializo las especiales con color
            for(int i=0; i<2; i++){
                mazo.add(new CartaAccion(color, TipoCarta.CAMBIO_SENTIDO));
                mazo.add(new CartaAccion(color, TipoCarta.MAS_DOS));
                mazo.add(new CartaAccion(color, TipoCarta.BLOQUEO));
            }

        }
        //Inicializo las especiales sin color
        for(int i=0; i<2; i++){
            mazo.add(new CartaEspecial(TipoCarta.CAMBIO_COLOR));
            mazo.add(new CartaEspecial(TipoCarta.MAS_CUATRO));
        }

    }

    public boolean sinCartas(){
        //retorna true si no tiene cartas
        if(mazo.size() == 0){
            return true;
        } else {
            return false;
        }
    }
}
