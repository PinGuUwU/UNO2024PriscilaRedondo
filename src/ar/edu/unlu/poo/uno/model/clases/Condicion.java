package ar.edu.unlu.poo.uno.model.clases;

public class Condicion {
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
    public boolean sePuedeTirar(Carta cartaDescarte, Carta cartaTirar){//Se asume que ambos tienen valores, son notnull
        boolean decision = false;
        switch (cartaTirar.valor()){ //Compruebo primero los especiales
            //Podria comprobarlo por el color, pero mas facil por su valor
            case 14, 15: decision = true; //+4 y cambio color
                break;
        }
        //Luego por valor y luego por color
        if(cartaDescarte.valor() == cartaTirar.valor()){
            return decision = true;
        } else if(cartaDescarte.color().equalsIgnoreCase(cartaTirar.color())){
            return decision = true;
        }
        if(decision){
            System.out.println("Se puede tirar la carta");
        } else {
            System.out.println("No se puede tirar la carta");
        }
        return decision;
    }
}