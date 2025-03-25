import java.util.concurrent.TimeUnit;

public class BinarySearch {
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

    public SearchResult search(int[] array, int target) {
        if (!isSorted(array)) {
            throw new IllegalArgumentException("El arreglo debe estar ordenado para realizar búsqueda binaria");
        }

        // Iniciar temporizador
        long startTime = System.nanoTime();

        int left = 0, right = array.length - 1;
        int iterations = 0;

        while (left <= right) {
            iterations++;
            int mid = left + (right - left) / 2;

            // Check if the target is found
            if (array[mid] == target) {
                long endTime = System.nanoTime();
                long executionTime = endTime - startTime;
                return new SearchResult(mid, executionTime, iterations);
            } else if (array[mid] < target) left = mid + 1;
            else right = mid - 1;
        }

        // Si no se encuentra el objetivo
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        return new SearchResult(-1, executionTime, iterations);
    }

    // Método para calcular la complejidad computacional
    public double calculateComputationalComplexity(int arrayLength) {
        // La búsqueda binaria tiene una complejidad de O(log n)
        // Calculamos log base 2 del tamaño del arreglo
        return Math.log(arrayLength) / Math.log(2);
    }

    // Método auxiliar para verificar si el arreglo está ordenado
    private boolean isSorted(int[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }

        // Verificar si está ordenado de forma ascendente
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }
}