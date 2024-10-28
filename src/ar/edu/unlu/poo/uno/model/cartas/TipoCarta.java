package ar.edu.unlu.poo.uno.model.cartas;

public enum TipoCarta {
    COMUN,
    CAMBIO_SENTIDO{
        @Override
        public String toString() {
            return "cambiosentido";
        }
    },
    BLOQUEO{
        @Override
        public String toString() {
            return "bloqueo";
        }
    },
    MAS_DOS{
        @Override
        public String toString() {
            return "+2";
        }
    },
    MAS_CUATRO{
        @Override
        public String toString() {
            return "+4";
        }
    },
    CAMBIO_COLOR{
        @Override
        public String toString() {
            return "cambiocolor";
        }
    },
    VACIA{
        @Override
        public String toString() {
            return "vacia";
        }
    }
}
