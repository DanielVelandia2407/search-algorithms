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
    private int tableSize;

    // Tabla hash
    private int[] hashTable;

    // Constructor sin argumentos usa tamaño predeterminado
    public ModuloSearch() {
        this(100); // Tamaño predeterminado
    }

    // Constructor con tamaño personalizado
    public ModuloSearch(int size) {
        this.tableSize = size;
        this.hashTable = new int[size];
        // Inicializar con 0 o -1 para indicar posiciones vacías
        // Uso -1 para distinguir más fácilmente entre valores insertados y posiciones vacías
        java.util.Arrays.fill(this.hashTable, -1);
    }

    // Función hash simple
    private int hashFunction(int key) {
        return Math.abs(key % tableSize);
    }

    // Método para insertar
    public void insert(int value) {
        int index = hashFunction(value);
        int startIndex = index;

        while (hashTable[index] != -1 && hashTable[index] != 0) {
            index = (index + 1) % tableSize;
            if (index == startIndex) {
                System.out.println("Tabla hash llena, no se puede insertar: " + value);
                return;
            }
        }
        hashTable[index] = value;
    }

    // Método de búsqueda con métricas de rendimiento
    public SearchResult search(int[] array, int target) {
        // Iniciar temporizador
        long startTime = System.nanoTime();
        int iterations = 0;

        // Calcular el índice donde debería estar el valor
        int index = hashFunction(target);
        int startIndex = index;

        // Buscar en la tabla hash en lugar del arreglo original
        while (hashTable[index] != -1) {
            iterations++;

            if (hashTable[index] == target) {
                long endTime = System.nanoTime();
                return new SearchResult(index, endTime - startTime, iterations);
            }

            // Avanzar al siguiente índice (sondeo lineal)
            index = (index + 1) % tableSize;

            // Si hemos dado una vuelta completa, el elemento no está en la tabla
            if (index == startIndex) {
                break;
            }
        }

        // Si no se encuentra el objetivo
        long endTime = System.nanoTime();
        return new SearchResult(-1, endTime - startTime, iterations);
    }

    // Getter para el tamaño de la tabla
    public int getTableSize() {
        return tableSize;
    }

    // Getter para obtener el valor en una posición específica
    public String getHashTableValue(int index) {
        if (index < 0 || index >= tableSize) {
            return "Índice fuera de rango";
        }
        return hashTable[index] != -1 ? String.valueOf(hashTable[index]) : "VACÍO";
    }
}