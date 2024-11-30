
# JUEGO DESARROLLADO: UNO
- OBJETIVO:
  - Quedarse sin cartas en la mano.

# DATOS PERSONALES:
  - Apellido y Nombre: Redondo Priscila
  - Legajo: 168540

# REGLAMENTO:
  - Un jugador puede tirar una carta si cumple alguna de las siguientes condiciones:
    - La carta tirada es del mismo color que la carta de mazo de descarte.
    - La carta tirada es del mismo número que la carta de mazo de descarte.
    - La carta tirada es una carta especial.
  - Si un jugador no tiene en su mano ninguna carta que cumpla las anteriores condiciones entonces deberá levantar una carta del mazo de robo. A partir de levantar la carta habrá algunas situaciones posibles:
    - Levanta una carta que puede tirar, entonces puede:
      - Tirarla y finalizar su turno.
      - Guardarse la carta (decir paso) y finalizar su turno.
    - Levanta una carta que no puede tirar y finaliza su turno.
  - Si un jugador tira su penúltima carta, debe decir “UNO”. Si se olvida de decir uno y otro jugador lo nota, antes de que el siguiente jugador tire o levante una carta, puede dar aviso de esto, dándole una penalización de dos cartas al jugador olvidadizo.
  - Un jugador solo puede levantar UNA carta por turno, sin importar si puede o no tirar cartas de su mano.
  - La carta “+4” sólo se puede tirar cuando el jugador no puede utilizar el color actual, con excepción de otras cartas de acción. Si el jugador siguiente a quien la tiró cree que la han jugado mal, puede desafiarlo.
    - En caso de desafío: El jugador que tiró el +4 deberá mostrar su mano: 
      - Si jugó el +4 de formal legal, el jugador que desafió toma 6 cartas en vez de 4
      - Si jugó el +4 de forma ilegal, el jugador desafiado deberá tomar 4 cartas

  - El ganador es el primero que se queda con 0 cartas en la mano.


# SITUACIONES ESPECÍFICAS DENTRO DEL JUEGO:
  - Si estás en una situación en donde pones apelar UNO y también desafiar por un +4 entonces:
    - Primero: deberá decidir si desafiar o no
    - Segundo: apelar, si así quisiera
  - Si se guarda la partida actual, se sigue jugando y se la vuelve a guardar, no se creará un nuevo "slot" de guardado, si no que se reemplazará la primera vez que se guardó por la última vez

  
  # CARTAS DE ACCIÓN:
  - CARTA MAS DOS:
      - Cuando se tira esta carta, el siguiente jugador debe tomar dos cartas y no puede tirar ninguna carta en esa ronda.
    - CARTA CAMBIO DE SENTIDO
      - Se cambia el sentido de la ronda, si iba en sentido horario, irá en sentido anti-horario. 
  - CARTA BLOQUEO
    - El siguiente jugador será salteado.
  - CARTA CAMBIO DE COLOR
    - El jugador que la tira elegirá de qué color se tirará la siguiente carta.
  - CARTA MAS CUATRO
    - Ésta carta hace que el siguiente jugador levante 4 cartas, además, el jugador que la tire podrá cambiar el color.
    - Esta carta solo se puede poner si el jugador no tiene ninguna carta en la mano que pueda tirar. (Leer reglamento para los detalles)

# FORMA DE EJECUCIÓN:
  - Primero: se debe ejecutar una intancia de AppServidor, que se encuentra en al carpeta "servidor"
    - Valores que deben ingresarse (ninguno, lo hice automático para que se conecte a la ip 127.0.0.1 y puerto 8888)
  - Segundo: se debe ejecutar una instancia de AppCliente por cada jugador que se desee unir, este se encuentra en la carpeta "cliente"
    - Valores que deben ingresarse:
      - Puerto del Cliente: un número del 1111 al 9999
        - Cada jugador debe elegir un número de Puerto de Cliente distinto
  - Tercero: Una vez ingresados todos los datos anteriores, se le pedirá su usuario. Deberá ingresar su nombre de usuario, si ya existe cargará sus datos, sino lo creará sin consultar.
  - Cuarto: Se le abrirá una ventana con las opcines "CONSOLA" y "PANTALLA" puede elegir la que quiera para comenzar a jugar.
  - SI UN JUGADOR SE DESCONECTA Y SE CONECTA OTRO CON EL MISMO VALOR DE PUERTO DE CLIENTE PUEDE GENERAR ERRORES, SE RECOMIENDA REINICIAR EL SERVIDOR EN ESOS CASOS

# UML
![UML](UML_UNO_FINAL.PNG)
