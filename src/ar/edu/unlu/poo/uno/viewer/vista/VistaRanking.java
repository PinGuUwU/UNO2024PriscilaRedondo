package ar.edu.unlu.poo.uno.viewer.vista;

import ar.edu.unlu.poo.uno.controller.ControladorRanking;
import ar.edu.unlu.poo.uno.model.clases.Jugador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaRanking {
    JFrame frame;
    private ControladorRanking ranking;
    private JPanel ventana;
    private JTextArea textArea1;
    private JPanel ganadas;
    private JList jugadoresNombres;
    private JTextArea textArea4;
    private JTextArea textArea5;
    private JComboBox<String> comboBox1;
    private JList jugadoresCantidad;

    public VistaRanking(){
        frame = new JFrame("Ranking");
        frame.setSize(720,480);
        frame.setLocationRelativeTo(null);

        agregarListeners();
        iniciarRanking();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        jugadoresNombres = new JList<>(listModel);
        jugadoresCantidad = new JList<>(listModel);

        //ranking = new ControladorRanking();

        frame.add(ventana);

        frame.setVisible(true);
    }

    private void iniciarRanking(){
        comboBox1.addItem("Elige una opcion:");//index 0
        comboBox1.addItem("Partidas ganadas");//index 1
        comboBox1.addItem("Partidas perdidas");//index 2
    }
    private void agregarListeners(){
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                int opcion = (int) comboBox1.getSelectedIndex();
                if("Elige una opcion:".equals(comboBox1.getItemAt(0)) && !"Elige una opcion:".equals(comboBox1.getSelectedItem())){
                    //Esto pasa si el item "Elige una opcion" existe y si la opcion elegida es distinta a "Elige una opcion"
                    //Esto hace para que el item "Elige una opcion" se elimine la primera vez que aparece, asì no vuelve a aparecer, hasta que se vuelva a abrir la ventana
                    comboBox1.removeItemAt(0);
                }
                switch(opcion){
                    case 1:
                        //muestro top ganadores
                        //jugadoresNombres.add() asi voy agregando los username
                        ranking.top5Ganadores();
                        //llamo a ranking con el metodo ganadores
                        //En ese metodo, ranking llama a VistaRanking vista.actualizarRanking
                        break;
                    case 2:
                        //muestr top perdedores
                        //jugadoresCantidad.add() asi voy agregando la cantidad de partidas ganadas/perdidas
                        ranking.top5Perdedores();
                        break;
                }
            }
        });
    }
    public void actualizarRanking(List<String> nombres, List<Integer> cantidad){

    }
}
