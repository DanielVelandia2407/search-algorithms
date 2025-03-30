import java.util.Arrays;

public class MultiplicationSearch {
    private int[] hashTable;
    private int tableSize;

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

        @Override
        public String toString() {
            return String.format(
                    "SearchResult{index=%d, executionTime=%d ns, iterations=%d}",
                    index, executionTimeNanos, iterations
            );
        }
    }

    public MultiplicationSearch() {
        this(100); // Tamaño predeterminado
    }

    public MultiplicationSearch(int size) {
        this.tableSize = size;
        this.hashTable = new int[size];
        Arrays.fill(this.hashTable, -1); // Inicializar con -1 para representar espacios vacíos
    }

    public void insert(int key) {
        int index = hashFunction(key);

        // Manejo de colisiones por sondeo lineal
        int originalIndex = index;
        int i = 0;

        while (hashTable[index] != -1 && hashTable[index] != key) {
            i++;
            index = (originalIndex + i) % tableSize; // Sondeo lineal

            // Evitar bucle infinito si la tabla está llena
            if (i >= tableSize) {
                break;
            }
        }

        // Solo insertar si encontramos un espacio vacío o no está ya presente
        if (hashTable[index] == -1) {
            hashTable[index] = key;
        }
    }

    public SearchResult search(int[] array, int target) {
        long startTime = System.nanoTime();
        int iterations = 0;

        int index = hashFunction(target);
        int originalIndex = index;
        int i = 0;

        while (hashTable[index] != -1) {
            iterations++;

            if (hashTable[index] == target) {
                long endTime = System.nanoTime();
                return new SearchResult(index, endTime - startTime, iterations);
            }

            // Sondeo lineal
            i++;
            index = (originalIndex + i) % tableSize;

            // Evitar bucle infinito
            if (i >= tableSize || index == originalIndex) {
                break;
            }
        }

        long endTime = System.nanoTime();
        return new SearchResult(-1, endTime - startTime, iterations);
    }

    private int hashFunction(int key) {
        // Calculamos el cuadrado de la clave
        long squared = (long) key * key;
        String squaredStr = String.valueOf(squared);

        // Determinamos cuántos dígitos necesitamos basado en el tamaño de la tabla
        int digitsNeeded = determineDigitsNeeded();

        // Si no hay suficientes dígitos, usamos módulo simple
        if (digitsNeeded >= squaredStr.length()) {
            return key % tableSize;
        }

        // Calculamos la posición central
        int midPosition = squaredStr.length() / 2;
        String extractedDigits;

        // Extraemos los dígitos centrales
        if (digitsNeeded % 2 == 0) {
            // Si necesitamos un número par de dígitos
            int startPos = midPosition - (digitsNeeded / 2);
            extractedDigits = squaredStr.substring(startPos, startPos + digitsNeeded);
        } else {
            // Si necesitamos un número impar de dígitos
            int startPos = midPosition - (digitsNeeded / 2);
            extractedDigits = squaredStr.substring(startPos, startPos + digitsNeeded);
        }

        // Convertimos a entero y aseguramos que esté dentro del rango de la tabla
        int hashValue = Integer.parseInt(extractedDigits) % tableSize;
        return hashValue;
    }

    private int determineDigitsNeeded() {
        // Calculamos cuántos dígitos necesitamos basado en el tamaño de la tabla
        int digits = 0;
        int temp = tableSize - 1; // El índice máximo posible

        while (temp > 0) {
            digits++;
            temp /= 10;
        }

        return digits;
    }

    public int getTableSize() {
        return tableSize;
    }

    public String getHashTableValue(int index) {
        if (index < 0 || index >= tableSize) {
            return "Índice fuera de rango";
        }
        return hashTable[index] != -1 ? String.valueOf(hashTable[index]) : "VACÍO";
    }
}