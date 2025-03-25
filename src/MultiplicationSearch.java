// MultiplicationSearch.java - Search by Multiplication Implementation with Performance Metrics
public class MultiplicationSearch {
    // Clase interna para almacenar resultados de la búsqueda
    public class SearchResult {
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

    // Método de búsqueda con métricas de rendimiento
    public SearchResult search(int[] array, int target) {
        // Iniciar temporizador
        long startTime = System.nanoTime();

        int length = array.length;
        int iterations = 0;

        for (int i = 1; i < length; i++) {
            iterations++;
            // Multiply index to create jumps
            int currentIndex = (i * 2) % length;
            if (array[currentIndex] == target) {
                long endTime = System.nanoTime();
                long executionTime = endTime - startTime;
                return new SearchResult(currentIndex, executionTime, iterations);
            }
        }

        // Si no se encuentra el objetivo
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        return new SearchResult(-1, executionTime, iterations);
    }

    // Método para calcular la complejidad computacional
    public double calculateComputationalComplexity(int arrayLength) {
        // La búsqueda por multiplicación tiene una complejidad de O(n)
        // Similar a una búsqueda lineal, retornamos la longitud del arreglo
        return arrayLength;
    }
}