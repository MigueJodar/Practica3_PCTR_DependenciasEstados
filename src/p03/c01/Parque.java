package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{


	private final int AFORO_MAXIMO = 50;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque() {
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
	}


	@Override
	public synchronized void entrarAlParque(String puerta){		
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		
		// Comprobar antes de entrar
	    comprobarAntesDeEntrar();
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		
		 // Comprobar después de entrar
	    checkInvariante();
		
		
	    // Notificar a todos los hilos que están esperando en este monitor
        notifyAll();
		
	}
	
	// 
	//  Método salirDelParque
	@Override
    public synchronized void salirDelParque(String puerta) {
        // Precondición
        comprobarAntesDeSalir();
        
        // Decremento del contador total y de la puerta correspondiente
        contadorPersonasTotales--;
        contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta) - 1);

     // Imprimimos el estado del parque
        imprimirInfo(puerta, "Salida");
        
        // Postcondición
        checkInvariante();
        
     // Notificar a todos los hilos que están esperando en este monitor
        notifyAll();
    }

	//
	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		 assert contadorPersonasTotales <= AFORO_MAXIMO : "INV: El número total de personas en el parque no debe superar el aforo máximo";
		 assert contadorPersonasTotales >= 0 : "INV: El número total de personas en el parque no debe ser negativo ";
		
		
		
		
	}

	   protected void comprobarAntesDeEntrar() {
	        while (contadorPersonasTotales >= AFORO_MAXIMO) {
	            try {
	                // Si el parque está lleno, esperar hasta que haya espacio disponible
	                wait();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	   protected void comprobarAntesDeSalir() {
	        while (contadorPersonasTotales <= 0) {
	            try {
	                // Si no hay personas en el parque, esperar hasta que haya alguien para salir
	                wait();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

