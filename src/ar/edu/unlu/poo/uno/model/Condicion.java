package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.Carta;
import ar.edu.unlu.poo.uno.model.cartas.CartaNumerica;
import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;

import java.io.IOException;
import java.io.Serializable;

import static ar.edu.unlu.poo.uno.model.cartas.TipoCarta.CAMBIO_COLOR;
import static ar.edu.unlu.poo.uno.model.cartas.TipoCarta.MAS_CUATRO;

public class Condicion implements Serializable {
    /*
    Principalmente esta clase se utilizaría para comprobar si las cartas cumplen con los requisitos
    Para poder ser tiradas al mazo de descarte
    Esta clase le entrarín 1 carta que está en el mazo de descarte 2 carta que se quiere tirar
    A paratir de ello devolvera verdadero o falso
    El programa hará un recorrido en la mano del jugador y mostrará todas sus cartas
    A la vez que marcará las que puede tirar en base al verdadero o falso que lanse esta clase
     */
    /*
    Orden de cartas:
    0=1, 1=2, 2=3, 3=4, 4=5, 5=6, 6=7, 7=8, 8=9, 9=10, +2=11, sentido=12, bloqueo=13, +4=14, cambioColor=15;
     */
    public static boolean sePuedeTirar(Carta cartaDescarte, Carta cartaTirar){//Se asume que ambos tienen valores, son notnull
        boolean decision = false;

        if(cartaTirar.color().equals(Color.ESPECIAL)){
            //Si es especial
            decision = true;
        } else if(cartaDescarte.color().equals(cartaTirar.color())){
            //Si son del mismo color
            decision = true;
        } else if(cartaDescarte.valor() == cartaTirar.valor()){//Si son el mismo TipoCarta
            if(cartaDescarte.valor() == TipoCarta.COMUN){//Si son el mismo numero
                try{
                    CartaNumerica c1 = (CartaNumerica) cartaTirar;
                    CartaNumerica c2 = (CartaNumerica) cartaDescarte;
                    if(c1.getValor() == c2.getValor()){
                        decision = true;
                    }
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            } else {
                decision = true;
            }
        }
        return decision;
    }
    public static boolean tieneParaTirar(Mano mano, Carta descarte){
        //Retorna verdadero si tiene al menos 1 carta para tirar
        //Para el descarte se le debe pasar la anterior al +4 que tiro
        for(int i=0; i<mano.cantCartas();i++){
            if(sePuedeTirar(descarte, mano.leerCartaMano(i))){
                return true;
            }
        }
        return false;
    }
}