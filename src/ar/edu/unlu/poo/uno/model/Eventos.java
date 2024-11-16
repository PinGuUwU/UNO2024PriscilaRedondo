package ar.edu.unlu.poo.uno.model;

public enum Eventos {
    /*Mostrar cartas en mano del jugador
    -mostrar la carta de descarte a todos
    -mostrar de quien es el turno actual
    -iniciar la partida

     */
    CARTA_DESCARTE, //Se avisa que debe actualizar la vista de la carta de descarte de los jugadores
    INICIAR_PARTIDA, //lo mismo que nueva partida, fusionar
    MOSTRAR_MANO, //se avisa que debe actualizar la vista de cartas de los jugadores
    PEDIR_COLOR, //Se esta esperando que un jugador elija el color al que desea cambiar
    NUEVA_PARTIDA, //Se inicia una nueva partida
    CAMBIO_JUGADORES, //Cuando un jugador avisa que esta listo para empezar la partida
    DESAFIO, //Se le avisa al jugador que tiro un +4 que puede desafiar
    PUEDE_DESAFIAR, //Se le avisa al jugador del turno actual que puede desafiar antes de tirar una carta
    YA_NO_SE_PUEDE_DESAFIAR, //Ya no puede desafiar por el +4 porque tiro una carta ?
    NO_DIJO_UNO, //Aviso a los jugadores que pueden "avisar" que el anterior jugador no dijo uno antes de tirar su anteultima carta
    YA_PASO_UNO // Aviso a los jugadores que el siguiente jugador ya tiró así que ya no pueden avisar que alguien no dijo uno
}
