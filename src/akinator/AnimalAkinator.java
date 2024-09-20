package akinator;

import java.io.*;
import java.util.Scanner;


/**
 * Clase AnimalAkinator : logica del juego de Akinator
 * @author David Silva
 */
public class AnimalAkinator implements Serializable {
    
    //Es un identificador unico que se usa para la interfaz Serializable
    //Funciona principalemnte para saber en que version de tu clase te encuentras
    //Es decir nos ayuda a prevenir problemas de compatibilidad cuando se hagan cambios en el codigo
    private static final long serialVersionUID = 1L;
    
    private Node root; //El nodo raiz del arbol binario
    private transient Scanner scanner; //Un scanner transitorio, osea se carga un nuevo Scanner cada vez que inicia el juego
    private final String fileName = "animalAkinatorTree.ser"; //Nombre del archivo donde se guardan los datos del arbol binario
    
    /**
     * Metodo constructor de la clase AnimalAkinator
     */
    public AnimalAkinator() {
        scanner = new Scanner(System.in); //Se inicia un objeto de tipo Scanner
        loadTree(); //Intentamos cargar el archivo serializado
        
        //Si el nodo raiz es nulo, quiere decir que el archivo no cargo bien
        if (root == null) { 
            //Se crea un nuevo nodo raiz que contiene la primera pregunta del juego
            root = new Node("Tu animal tiene cuernos?");
            root.yesBranch = new Node("Vaca"); //La respuesta a si daria que es una vaca
            root.noBranch = new Node("Perro"); //La respuesta a no daria que es un perro
        }
    }
    
    /**
     * Metodo play : Iniciar con el gameplay del juego
     */
    public void play() {
        System.out.println("Piensa en un animal, yo intentare adivinarlo"); //Para que el usuario piense en un animal
        
        //Ciclo infinito para que el usuario siempre este dando respuestas
        //O tambien para que ponga los inputs correctos en el juego
        while (true) {
            playRound(root); //Metodo que recibe el nodo raiz para iniciar el juego y sus secuencias
            System.out.println("Quieres jugar de nuevo (si/no)"); //Mostramos si el usuario quiere jugar de nuevo
            //Si lo que del el usuario es difetente de si entonces...
            if (!scanner.nextLine().equalsIgnoreCase("si")) {
                break; //Salimos del juego
            }
        }
        saveTree(); //Salvamos los datos del juego de manera exitosa
    }
    
    /**
     * Metodo playRound : maneja las rondas del juego hasta llegar a una hoja
     * @param current : el punto actual del arbol, puede ser una pregunta o un animal
     */
    private void playRound(Node current) {
        //Mientras el nodo actual no sea una hoja...
        //Osea que solo queda un nombre y no mas preguntas posteriores
        while (!current.isLeaf()) {
            //Imprimimos la informacion del nodo actual
            System.out.println(current.data);
            
            //Si el nodo actual la respuesta es si...
            if (getYesOrNoAnswer()) {
                current = current.yesBranch; //Se actualiza a la rama de si
            } else { //De lo contrario
                current = current.noBranch; //Se actualiza a la rama de no
            }
        }

        //Si se sale del ciclo infinito quiere decir que llego con una posible respuesta
        System.out.println("Es un " + current.data + "?"); //Mostramos la posible respuesta
        
        //Si la respuesta es un si entontes...
        if (getYesOrNoAnswer()) {
            System.out.println("Le atine!"); //Dira que le atino
        } else { //De lo contrario
            learnNewAnimal(current); //Llamamos al metodo para que aprenda un nuevo animal
        }
    }

    private void learnNewAnimal(Node current) {
        System.out.println("Me rindo... En que animal pensabas?");
        String newAnimal = scanner.nextLine();

        System.out.println("¿Que pregunta diferencia a un " + newAnimal + " de un " + current.data + "?");
        String newQuestion = scanner.nextLine();

        System.out.println("Para un " + newAnimal + ", ¿cual es la respuesta a esa pregunta? (si/no)");
        boolean answerForNewAnimal = getYesOrNoAnswer();

        // Create new nodes and reassign current node's data
        Node oldAnimalNode = new Node(current.data);
        current.data = newQuestion;
        if (answerForNewAnimal) {
            current.yesBranch = new Node(newAnimal);
            current.noBranch = oldAnimalNode;
        } else {
            current.noBranch = new Node(newAnimal);
            current.yesBranch = oldAnimalNode;
        }
    }
    
    /**
     * Metodo getYerOrNoAnswer : Obtener un si o un no del usario
     * @return : Saber si el usuario puso un si o un no
     */
    private boolean getYesOrNoAnswer() {
        //Creamos un ciclo infinito en donde preguntamos al usuario por una respuesta
        while (true) {
            //Creamos un Scanner para leer de la mejor manera posible la respuestas
            String input = scanner.nextLine().trim().toLowerCase();
            //Si es un si entonces...
            if (input.equals("si")) {
                return true; //Devolvemos un true
            //Si es un no entonces..
            } else if (input.equals("no")) {
                return false; //Devolvemos un false
            //Si no es ninguna de las anteriores
            } else {
                System.out.println("Por favor responde si o no"); //Recalcamos que diga que si o no
            }
        }
    }

    /**
     * Metodo saveTree : salvar el arbol binario de manera serializada e
     */
    private void saveTree() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(root);
            System.out.println("Datos de juego guardados");
        } catch (IOException e) {
            System.out.println("Error salvando los datos del juego: " + e.getMessage());
        }
    }

    /**
     * Metodo loadTree : cargar el arbol binario del juego a la memoria del programa
     */
    private void loadTree() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            root = (Node) ois.readObject();
            System.out.println("Cargando datos del juego.");
        } catch (FileNotFoundException e) {
            System.out.println("No existen datos del juego, cargando un nuevo arbol");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error cargando los datos del juego: " + e.getMessage());
        }
    }
 
}