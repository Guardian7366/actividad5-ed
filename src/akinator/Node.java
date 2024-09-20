package akinator;

import java.io.Serializable; 
//Interfaz importante para guardar datos del juego
//Esto se logra a partir de que sus instancias 
//Son convertidas a bytes que se almacenan en el programa


/**
 * Clase Node : clase que guarda la informacion de las preguntas y respuestas
 * @author David Silva
 */
public class Node implements Serializable {
    //Es un identificador unico que se usa para la interfaz Serializable
    //Funciona principalemnte para saber en que version de tu clase te encuentras
    //Es decir nos ayuda a prevenir problemas de compatibilidad cuando se hagan cambios en el codigo
    private static final long serialVersionUID = 1L;
    
    String data; //Contenido del nodo, pregunta o nombre de animal
    Node yesBranch; //Nodo hijo si la respuesta es SI
    Node noBranch;  //Nodo hijo si la respuesta es NO
    
    /**
     * Metodo constructor de la clase Node
     * @param data : el contenido que puede ser pregunta o nombre de animal
     */
    public Node(String data) {
        this.data = data; //Inicializamos el contenido
        this.yesBranch = null; //Inicializamos el hijo en nulo
        this.noBranch = null; //Inicializamos el hijo en nulo
        
    }
    
    /**
     * Metodo isLeaf : Determinar si es una rama o hoja
     * La hoja seria el nombre del animal y la rema la pregunta
     * @return : Si es hoja/hijo o no
     */
    public boolean isLeaf() {
        //Retornamos la expresion si la respuestas de si y no, no llevan a otra pregunta
        return yesBranch == null && noBranch == null;
    }
}