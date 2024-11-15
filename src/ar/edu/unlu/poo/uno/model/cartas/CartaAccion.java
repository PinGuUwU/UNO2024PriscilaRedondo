package ar.edu.unlu.poo.uno.model.cartas;

import ar.edu.unlu.poo.uno.model.Partida;

import java.rmi.RemoteException;

public class CartaAccion extends Carta{
    //Puede ser +2, bloqueo o cambio de sentido
    public CartaAccion(Color color, TipoCarta valor) {
        super(color, valor);
    }
    public boolean jugar(Partida partida) throws RemoteException {
        switch(super.valor()){
            case CAMBIO_SENTIDO -> partida.cambioDeSentido();
            case MAS_DOS -> partida.levantarDosCartas();
            case BLOQUEO -> partida.bloquear();
        }
        return false;
    }
}
