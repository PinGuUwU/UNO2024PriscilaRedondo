package ar.edu.unlu.poo.uno.model.clases;

import java.util.ArrayList;
import java.util.Arrays;

public class Mezclador {
    /*
    El mezclador no mezcla (xDn't) Sirve para generar el mazo desde cero,
    El mazo queda siempre ordenado porque serían muchas iteraciones el mezclarlo
    Es más rápido generar un número random y entregar la carta que se encuentre en esa posicion¿
     */
    private ArrayList<Integer> cantCartas = new ArrayList<>();
    public Mezclador(){
        cantCartas.addAll(Arrays.asList(1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2));
    }
    public ArrayList<Integer> getCantCartas(){
        return cantCartas;
    }
    /*
    Esta clase se encarga de retornar un mazoDeRobo de cartas coimpleto y mezclado
     */
     /*
    Orden de cartas:
    0=1, 1=2, 2=3, 3=4, 4=5, 5=6, 6=7, 7=8, 8=9, 9=10, +2=11, sentido=12, bloqueo=13, +4=14, cambioColor=15;
    existencias:

    colores:
    "verde", "rojo", "azul", "amarillo"
     */
    public ArrayList<Carta> mezclar(){
        ArrayList<Carta> nuevoMazo = new ArrayList<Carta>();
        Carta carta;
        ArrayList<String> colores = new ArrayList<>();
        colores.addAll(Arrays.asList("verde", "rojo", "azul", "amarillo"));

        for(int j = 0; j<4; j++){
            int contador1=0, contador2=0;
            while(contador1<13){
                for(int i = 0; i< cantCartas.get(contador2); i++){
                    carta = new Carta(contador1,colores.get(j));
                    nuevoMazo.add(carta);
                }
                contador1++;
                contador2++;
            }
        }

        //Agrego los especiales
        int esp=14;
        String color ="especial";
        for(int i=0; i<2; i++){
            carta = new Carta(esp,color);
            for(int j=0; j<4; j++){
                nuevoMazo.add(carta);
            }
            esp++;
        }

        return nuevoMazo;
    }
}