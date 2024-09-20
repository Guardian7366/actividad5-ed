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
    
    /**
     * Metodo learnNewAnimal : el programa aprende un nuevo animal
     * @param current : el nodo actual en donde nos quedamos del juego
     */
    private void learnNewAnimal(Node current) {
        System.out.println("Me rindo... En que animal pensabas?"); //Akinator se rinde
        String newAnimal = scanner.nextLine(); //Escanemos al nuevo animal para tener en el arbol binario
        
        //Le solicitamos al usuario que nos indique alguna diferencia para ubicar al animal nuevo de otros animales
        System.out.println("¿Que pregunta diferencia a un " + newAnimal + " de un " + current.data + "?");
        String newQuestion = scanner.nextLine(); //Guardamos la pregunta basada en el animal
        
        //Preguntamos al usuario si la pregunta que envio se contesta con un si o no
        System.out.println("Para un " + newAnimal + ", ¿cual es la respuesta a esa pregunta? (si/no)");
        boolean answerForNewAnimal = getYesOrNoAnswer(); //Por medio de este metodo es que guardamos el valor de la pregunta, osea si es un si o un no
  
        Node oldAnimalNode = new Node(current.data); //Apartir de la informacion del nodo que tenemos, lo guardamos en otro nodo
        
        current.data = newQuestion; //El nodo actual su informacion se vuelve en la nueva pregunta
        
        //Si devolvio true la respuesta, osea que es un si entonces...
        if (answerForNewAnimal) {
            current.yesBranch = new Node(newAnimal); //El nuevo animal se vuelve parte de la hoja/hijo si
            current.noBranch = oldAnimalNode; //El viejo animal se vuelve parte de la hoja/hijo no
        } else { //De lo contrario si es un no entonces...
            current.noBranch = new Node(newAnimal); //El nuevo animal se vuelve parte de la hoja/hijo no
            current.yesBranch = oldAnimalNode; //EL viejo animal se vuelve parte de la hoja/hijo si
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
     * Metodo saveTree : salvar el arbol binario de manera serializada 
     */
    private void saveTree() {
        //Intentamos crear un flujo de salida hacia el archivo donde contiene el arbol binario
        //El ObjectOutputStream es la que escribe los objetos en formato binario
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(root); //Serializamos el objeto root (raiz del arbol)
            //Esto causa que cualquier objeto que tenga o referenciado los convierta en una secuencia de bytes
            //Y los guarde en el archivo especificado
            System.out.println("Datos de juego guardados"); //Mostramos que los datos se guardaron de manera correcta
        } catch (IOException e) { //En caso de que exista un error a la hora de guardar el juego
            System.out.println("Error salvando los datos del juego: " + e.getMessage()); //Mostramos el error en la consola
        }
    }

    /**
     * Metodo loadTree : cargar el arbol binario del juego a la memoria del programa
     */
    private void loadTree() {
        //Intentamos crear un flujo de entrada desde el nombre del archivo
        //Lo que hacemos es preparar la lectura del arbol binario
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            root = (Node) ois.readObject(); //Hacamos un casting de Node para que el metodo pueda leer el objeto que se encuentre en el archivo
            //Cuando se termina de cargar el objeto deserializado se lo asignamos a la raiz del arbol
            System.out.println("Cargando datos del juego."); //Mostramos que se cargaron de manera exitosa los datos del juego
        } catch (FileNotFoundException e) { //Excepcion en caso de que no exista el archivo que le pasamos
            System.out.println("No existen datos del juego, cargando un nuevo arbol"); //Mensaje de error en consola
        } catch (IOException | ClassNotFoundException e) { //Error durante la lectura o deserailizacion del archivo
            System.out.println("Error cargando los datos del juego: " + e.getMessage()); //Mensaje de error en consola
        }
    }
 
}