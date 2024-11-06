package ar.edu.unlu.poo.uno.model.cartas;

public enum Color {
    AMARILLO{
        @Override
        public String toString() {
            return "amarillo";
        }
    },
    ROJO{
        @Override
        public String toString() {
            return "rojo";
        }
    },
    AZUL{
        @Override
        public String toString() {
            return "azul";
        }
    },
    VERDE{
        @Override
        public String toString() {
            return "verde";
        }
    },
    ESPECIAL{
        @Override
        public String toString() {
            return "especial";
        }
    },
    INVALIDO
}
