public class ModuloSearch {
    // Clase interna para almacenar resultados de la búsqueda
    public static class SearchResult {
        private int index;
        private long executionTimeNanos;
        private int iterations;

        public SearchResult(int index, long executionTimeNanos, int iterations) {
            this.index = index;
            this.executionTimeNanos = executionTimeNanos;
            this.iterations = iterations;
        }

        public int getIndex() {
            return index;
        }

        public long getExecutionTimeNanos() {
            return executionTimeNanos;
        }

        public int getIterations() {
            return iterations;
        }
    }

    // Tamaño de la tabla hash
    private static final int TABLE_SIZE = 10;

    // Tabla hash simple
    private int[] hashTable = new int[TABLE_SIZE];

    // Función hash simple
    private int hashFunction(int key) {
        // Usamos el módulo para distribuir las claves
        return Math.abs(key % TABLE_SIZE);
    }

    // Método para insertar
    public void insert(int value) {
        int index = hashFunction(value);

        // Manejo de colisiones (método de sondeo lineal simple)
        while (hashTable[index] != 0) {
            index = (index + 1) % TABLE_SIZE;
        }

        hashTable[index] = value;
    }

    // Método de búsqueda con métricas de rendimiento
    public SearchResult search(int[] array, int target) {
        // Iniciar temporizador
        long startTime = System.nanoTime();

        int iterations = 0;

        for (int i = 0; i < array.length; i++) {
            iterations++;
            if (array[i] == target) {
                long endTime = System.nanoTime();
                long executionTime = endTime - startTime;
                return new SearchResult(i, executionTime, iterations);
            }
        }

        // Si no se encuentra el objetivo
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        return new SearchResult(-1, executionTime, iterations);
    }

    // Método para calcular la complejidad computacional
    public double calculateComputationalComplexity(int arrayLength) {
        // La búsqueda tiene una complejidad de O(n)
        return arrayLength;
    }

    // Método para imprimir la tabla hash
    public void printHashTable() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            System.out.println("Índice " + i + ": " + hashTable[i]);
        }
    }
}