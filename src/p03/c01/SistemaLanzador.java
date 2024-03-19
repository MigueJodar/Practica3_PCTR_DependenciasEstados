package src.p03.c01;

public class SistemaLanzador {
    public static void main(String[] args) {
        
        IParque parque = new Parque(); // Instanciar el parque
        
        char letra_puerta = 'A';
        
        System.out.println("¡Parque abierto!");
        
        // Crear y lanzar hilos para simular las entradas
        for (int i = 0; i < 5; i++) {
            String puerta = "" + ((char) (letra_puerta++));
            ActividadEntradaPuerta entradas = new ActividadEntradaPuerta(puerta, parque);
            new Thread(entradas).start();
        }
        
        // Crear y lanzar hilos para simular las salidas
        for (int i = 0; i < 5; i++) {
            String puerta = "" + ((char) ('A' + i));
            ActividadSalidaPuerta salidas = new ActividadSalidaPuerta(puerta, parque);
            new Thread(salidas).start();
        }
    }   
}
