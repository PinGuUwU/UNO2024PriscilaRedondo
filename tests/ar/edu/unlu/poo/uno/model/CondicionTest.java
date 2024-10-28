package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.*;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CondicionTest {
    @Test
    public void tirar(){
        Condicion c = new Condicion();
        Carta carta1 = new CartaNumerica(Color.VERDE, 2);
        Carta carta2 = new CartaNumerica(Color.VERDE, 7);
        boolean sePudo = c.sePuedeTirar(carta1, carta2);
        System.out.println(sePudo);
    }

}