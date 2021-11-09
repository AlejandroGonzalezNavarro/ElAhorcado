package ahorcado.logica;

import ahorcado.bbdd.conexion;
import java.text.Normalizer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * Contiene las funciones que se ejecutarán dentro del juego del ahorcado.
 * @author Alejandro González Navarro
 */
public class logica {
    private String palabra;
    private String palabraOculta;
    private int fallos;
    private int aciertos;
    private conexion Conexion;
    private int indexPista;
    public logica(JLabel JLabelPalabra) {
        // TODO Implementar sacar palabra cuando se inicia
        fallos = 1;
        aciertos=0;
        Conexion = new conexion();
        iniciarPalabra(JLabelPalabra);
        indexPista = 1;
    }
    
    private void iniciarPalabra(JLabel JLabelPalabra){
        palabra = Conexion.selectPalabraAleatoria();
        // Se rellena la palabra oculta con "_" con tantas letras como la palabra original
        palabraOculta = "_";
        for(int i = 1; i < palabra.length(); i++){
            palabraOculta += " _";
        }
        // Establece en la interfaz la palabra oculta
        JLabelPalabra.setText(palabraOculta);
    }
    /**
     * Comprueba si la letra presionada es correcta en la palabra seleccionada en el juego
     * @param boton boton presionado con solo una letra asignada
     * @param jLabelPalabra Palabra a la que se le asignará las letras de la palabra oculta
     * @param jLabelImagen JLabel donde aparecerá del ahorcado
     * @param jPanelTeclado JPanel que contiene los botones que se desactivará al perder
     * @param jPanelBotonesFinales JPanel que contiene los botones finales
     * @param jLabelPista
     * @param jButtonPista
     */
    public void comprobarLetra(JButton boton, JLabel jLabelPalabra, JLabel jLabelImagen, JPanel jPanelTeclado,JPanel jPanelBotonesFinales,JLabel jLabelPista, JButton jButtonPista){
        String palabraSinTildes;
        // Al pulsar un botón lo deshabilita
        boton.setEnabled(false);
        // Si la palabra contiene esa tecla
        palabraSinTildes = Normalizer.normalize(palabra , Normalizer.Form.NFD);
        palabraSinTildes = palabraSinTildes.replaceAll("[^\\p{ASCII}]", "");
        if(palabraSinTildes.toLowerCase().contains(boton.getText().toLowerCase())){
            int index = 0;
            // Introduce la letr, las veces que este dentro de la palabra
            while(index != -1){
            // Indice donde se encuentra la letra
            index = palabraSinTildes.toLowerCase().indexOf(boton.getText().toLowerCase(), index);
                if (index != -1) {
                    // Generación de la palabra oculta,sustituye solo la letra correspondiente
                    palabraOculta =
                    palabraOculta.substring(0,index*2) +
                    palabraOculta.substring(index*2, index*2+1).replace("_", palabra.charAt(index)+"") +
                    palabraOculta.substring(index*2+1);
                    index++;
                    aciertos++;
                }
            }
            // Se establece la palabra en el juego
            jLabelPalabra.setText(palabraOculta);
            if(aciertos >= palabra.length()){
                ganar(jLabelPalabra, jPanelTeclado, jPanelBotonesFinales, jLabelPista, jButtonPista);
            }
        } else {
            boton.setText("");
            boton.setIcon(new ImageIcon(getClass().getResource("/ahorcado/imagen/cruz.png")));
            fallos += 1;
            jLabelImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ahorcado/imagen/ahocado0"+fallos+".jpg")));
            if(fallos >= 7){
                perder(jLabelPalabra, jPanelTeclado, jPanelBotonesFinales, jLabelPista, jButtonPista);
            }
        }
    }
    
    public void getPista(JLabel jLabelPista){
        jLabelPista.setText(Conexion.selectPistaAleatoria(palabra,indexPista));
        jLabelPista.setVisible(true);
        indexPista++;
    }
    // Funcion que se activa cuando el usuario tiene 7 fallos
    public void perder(JLabel jLabelPalabra, JPanel jPanelTeclado, JPanel jPanelBotonesFinales,JLabel jLabelPista, JButton jButtonPista){
        jLabelPalabra.setText("¡Has perdido!");
        // Se desactiva el panel de botones
        jPanelTeclado.setVisible(false);
        jPanelBotonesFinales.setVisible(true);
        jLabelPista.setVisible(false);
        jButtonPista.setVisible(false);
    }
    public void ganar(JLabel jLabelPalabra, JPanel jPanelTeclado, JPanel jPanelBotonesFinales,JLabel jLabelPista, JButton jButtonPista){
        jLabelPalabra.setText("¡Has ganado!");
        jPanelTeclado.setVisible(false);
        jPanelBotonesFinales.setVisible(true);
        jLabelPista.setVisible(false);
        jButtonPista.setVisible(false);
    }
    public void jugarDeNuevo(JLabel JLabelPalabra,JPanel jPanelTeclado, JLabel jLabelImagen, JPanel jPanelBotonesFinales,JButton jButtonPista){
        // Restablecemos los fallos
        this.aciertos = 0;
        this.fallos = 1;
        // Recarga la palabra
        iniciarPalabra(JLabelPalabra);
        // Reiniciar la imagen
        jLabelImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ahorcado/imagen/ahocado01.jpg")));
        // Se activa el panel de botones
        for (Object object : jPanelTeclado.getComponents()) {
            if(object instanceof JButton){
                ((JButton) object).setIcon(null);
                ((JButton) object).setText(String.valueOf((char)(((JButton) object).getMnemonic())));
                ((JButton) object).setEnabled(true);
            }
        }
        jButtonPista.setVisible(true);
        jPanelBotonesFinales.setVisible(false);
        jPanelTeclado.setVisible(true);
    }
}
