package ar.edu.unlu.poo.uno.model.cartas;

import ar.edu.unlu.poo.uno.model.Partida;

import java.rmi.RemoteException;

public class CartaEspecial extends Carta{
    //Carta +4 o cambio de color
    public CartaEspecial(TipoCarta valor) {
        super(Color.ESPECIAL, valor);
    }

    @Override
    public boolean jugar(Partida partida) throws RemoteException {
        switch(super.valor()){
            case CAMBIO_COLOR -> partida.pedirColorJugador();
            case MAS_CUATRO -> {
                partida.levantarCuatroCartas();
                partida.pedirColorJugador();
            }
        }
        //Siempre retorna true porque en ambos casos de cambia de color
        return true;
    }
}
