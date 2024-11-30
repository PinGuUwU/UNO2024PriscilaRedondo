package ar.edu.unlu.poo.uno.model;

public enum Eventos {
    CARTA_DESCARTE, //Se avisa que debe actualizar la vista de la carta de descarte de los jugadores
    INICIAR_PARTIDA, //lo mismo que nueva partida, fusionar
    MOSTRAR_MANO, //se avisa que debe actualizar la vista de cartas de los jugadores
    PEDIR_COLOR, //Se esta esperando que un jugador elija el color al que desea cambiar
    NUEVA_PARTIDA, //Se inicia una nueva partida
    CAMBIO_JUGADORES, //Cuando un jugador avisa que esta listo para empezar la partida
    PUEDE_DESAFIAR, //Se le avisa al jugador del turno actual que puede desafiar antes de tirar una carta
    NO_DIJO_UNO, //Aviso a los jugadores que pueden "avisar" que el anterior jugador no dijo uno antes de tirar su anteultima carta
    CAMBIO, //Aviso de un cambio
    SE_APELO_EL_UNO, //Un jugador apelo el uno
    CARGAR_PARTIDA //Se cargo una partida
}
