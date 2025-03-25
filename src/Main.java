public class Main {
    public static void main(String[] args) {
        BinarySearch bs = new BinarySearch();
        ModuloSearch ms = new ModuloSearch();
        MultiplicationSearch mps = new MultiplicationSearch();

        int[] array = {2, 1};
        int target = 100;

        // Binary Search
        try {
            BinarySearch.SearchResult binarySearchResult = bs.search(array, target);
            System.out.println("Binary Search Result:");
            System.out.println("Index: " + binarySearchResult.getIndex());
            System.out.println("Execution Time: " + binarySearchResult.getExecutionTimeNanos() + " nanoseconds");
            System.out.println("Iterations: " + binarySearchResult.getIterations());

            // Compute computational complexity
            double complexity = bs.calculateComputationalComplexity(array.length);
            System.out.println("Computational Complexity (log2(n)): " + complexity);
        } catch (IllegalArgumentException e) {
            System.out.println("Binary Search Error: " + e.getMessage());
        }

        // Modulo Search
        try {
            ModuloSearch.SearchResult moduloSearchResult = ms.search(array, target);
            System.out.println("\nModulo Search Result:");
            System.out.println("Index: " + moduloSearchResult.getIndex());
            System.out.println("Execution Time: " + moduloSearchResult.getExecutionTimeNanos() + " nanoseconds");
            System.out.println("Iterations: " + moduloSearchResult.getIterations());

            // Compute computational complexity
            double complexity = ms.calculateComputationalComplexity(array.length);
            System.out.println("Computational Complexity (O(n)): " + complexity);
        } catch (Exception e) {
            System.out.println("Modulo Search Error: " + e.getMessage());
        }

        // Multiplication Search
        try {
            MultiplicationSearch.SearchResult multiplicationSearchResult = mps.search(array, target);
            System.out.println("\nMultiplication Search Result:");
            System.out.println("Index: " + multiplicationSearchResult.getIndex());
            System.out.println("Execution Time: " + multiplicationSearchResult.getExecutionTimeNanos() + " nanoseconds");
            System.out.println("Iterations: " + multiplicationSearchResult.getIterations());

            // Compute computational complexity
            double complexity = mps.calculateComputationalComplexity(array.length);
            System.out.println("Computational Complexity (O(n)): " + complexity);
        } catch (Exception e) {
            System.out.println("Multiplication Search Error: " + e.getMessage());
        }
    }
}