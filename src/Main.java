import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

class BinarySearchGUI extends JFrame {
    private JTextField sizeField, targetField;
    private JTextArea resultArea;
    private int[] array;
    private BinarySearch bs;
    private ModuloSearch ms;
    private MultiplicationSearch multSearch;
    private JTextArea hashTableArea;
    private JTabbedPane hashTableTabs;
    private JTextArea multHashTableArea;
    private boolean isSorted;
    // Variable para seguir la posición actual en el array
    private int currentIndex = 0;

    public BinarySearchGUI() {
        bs = new BinarySearch();
        ms = new ModuloSearch();
        multSearch = new MultiplicationSearch();
        setTitle("Búsqueda Binaria y Hash");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior para controles
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controles"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Primera fila - Tamaño del arreglo
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(new JLabel("Tamaño del arreglo:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        sizeField = new JTextField(10);
        controlPanel.add(sizeField, gbc);

        // Segunda fila - Botones de generación
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JButton createEmptyArrayButton = new JButton("Crear Arreglo Vacío");
        createEmptyArrayButton.addActionListener(e -> createEmptyArray());
        controlPanel.add(createEmptyArrayButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton sortArrayButton = new JButton("Ordenar Arreglo");
        sortArrayButton.addActionListener(e -> sortArray());
        controlPanel.add(sortArrayButton, gbc);

        // Tercera fila - Valor a buscar
        gbc.gridx = 0;
        gbc.gridy = 2;
        controlPanel.add(new JLabel("Valor a buscar:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        targetField = new JTextField(10);
        controlPanel.add(targetField, gbc);

        // Cuarta fila - Botones de búsqueda
        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton binarySearchButton = new JButton("Buscar Binario");
        binarySearchButton.addActionListener(e -> performBinarySearch());
        controlPanel.add(binarySearchButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton moduloSearchButton = new JButton("Buscar por Módulo");
        moduloSearchButton.addActionListener(e -> performModuloSearch());
        controlPanel.add(moduloSearchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton multiplicationSearchButton = new JButton("Buscar por Multiplicación");
        multiplicationSearchButton.addActionListener(e -> performMultiplicationSearch());
        controlPanel.add(multiplicationSearchButton, gbc);

        // Añadir botón para introducir valores
        gbc.gridx = 2;
        gbc.gridy = 0;
        JButton inputValueButton = new JButton("Introducir Valor");
        inputValueButton.addActionListener(e -> inputArrayValue());
        controlPanel.add(inputValueButton, gbc);

        // Añadir panel de controles a la parte superior
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Panel para áreas de texto
        JPanel resultsPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Panel para resultados de búsqueda
        JPanel searchResultPanel = new JPanel(new BorderLayout());
        searchResultPanel.setBorder(BorderFactory.createTitledBorder("Resultados de Búsqueda"));
        resultArea = new JTextArea(20, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        searchResultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        resultsPanel.add(searchResultPanel);

        // Panel para tablas hash
        JPanel hashTablesPanel = new JPanel(new BorderLayout());
        hashTablesPanel.setBorder(BorderFactory.createTitledBorder("Tablas Hash"));

        // Crear pestañas para diferentes tablas hash
        hashTableTabs = new JTabbedPane();

        // Pestaña para tabla hash por módulo
        hashTableArea = new JTextArea(20, 30);
        hashTableArea.setEditable(false);
        hashTableArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        hashTableTabs.addTab("Tabla Hash (Módulo)", new JScrollPane(hashTableArea));

        // Pestaña para tabla hash por multiplicación
        multHashTableArea = new JTextArea(20, 30);
        multHashTableArea.setEditable(false);
        multHashTableArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        hashTableTabs.addTab("Tabla Hash (Multiplicación)", new JScrollPane(multHashTableArea));

        hashTablesPanel.add(hashTableTabs, BorderLayout.CENTER);
        resultsPanel.add(hashTablesPanel);

        // Añadir panel de resultados al centro
        mainPanel.add(resultsPanel, BorderLayout.CENTER);

        // Añadir panel principal al frame
        add(mainPanel);

        pack(); // Ajustar tamaño automáticamente
        setVisible(true);
    }

    // Método para crear un array vacío
    private void createEmptyArray() {
        try {
            int size = Integer.parseInt(sizeField.getText());
            array = new int[size];
            currentIndex = 0; // Inicializar índice para seguimiento de posición de inserción

            // Crear nuevas instancias de las tablas hash con el tamaño especificado
            ms = new ModuloSearch(size);
            multSearch = new MultiplicationSearch(size);

            // Inicializar el array con valores predeterminados (0)
            for (int i = 0; i < size; i++) {
                array[i] = 0; // Inicializar con ceros
            }

            isSorted = false;
            resultArea.setText("Arreglo vacío creado con tamaño " + size +
                    "\nUtilice el botón 'Introducir Valor' para añadir valores." +
                    "\nValores introducidos: 0 de " + size);
            updateHashTableViews();
        } catch (NumberFormatException ex) {
            resultArea.setText("Error: Ingrese un tamaño válido.");
        }
    }

    // Método para que el usuario introduzca un valor al array
    private void inputArrayValue() {
        if (array == null) {
            resultArea.setText("Error: Primero debe crear un arreglo.");
            return;
        }

        if (currentIndex >= array.length) {
            resultArea.setText("Error: El arreglo ya está lleno. No se pueden añadir más valores.");
            return;
        }

        String input = JOptionPane.showInputDialog(this,
                "Introduzca el valor para la posición " + currentIndex + ":");

        if (input != null) {
            try {
                int value = Integer.parseInt(input);
                array[currentIndex] = value;

                // Añadir el valor a las tablas hash
                ms.insert(value);
                multSearch.insert(value);

                currentIndex++;

                // Actualizar la vista
                resultArea.setText("Valor " + value + " añadido en la posición " + (currentIndex-1) +
                        "\nValores introducidos: " + currentIndex + " de " + array.length +
                        "\nArreglo actual: " + Arrays.toString(array));
                updateHashTableViews();

            } catch (NumberFormatException ex) {
                resultArea.setText("Error: Ingrese un valor numérico válido.");
            }
        }
    }

    // Método para ordenar el array
    private void sortArray() {
        if (array == null) {
            resultArea.setText("Error: No hay arreglo para ordenar.");
            return;
        }

        Arrays.sort(array);
        isSorted = true;
        resultArea.setText("Arreglo ordenado: " + Arrays.toString(array));
    }

    private void performBinarySearch() {
        try {
            if (array == null) {
                throw new IllegalStateException("Primero debe generar un arreglo.");
            }
            if (!isSorted) {
                throw new IllegalArgumentException("El arreglo no está ordenado. La búsqueda binaria requiere un arreglo ordenado.");
            }
            int target = Integer.parseInt(targetField.getText());
            BinarySearch.SearchResult result = bs.search(array, target);
            resultArea.append("\n\nResultado de la búsqueda binaria:");
            resultArea.append("\nValor buscado: " + target);
            resultArea.append("\nÍndice encontrado: " + (result.getIndex() != -1 ? result.getIndex() : "No encontrado"));
            resultArea.append("\nTiempo de ejecución: " + result.getExecutionTimeNanos() + " nanosegundos");
            resultArea.append("\nIteraciones: " + result.getIterations());
        } catch (NumberFormatException ex) {
            resultArea.setText("Error: Ingrese un valor válido para buscar.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void performModuloSearch() {
        try {
            if (array == null) {
                throw new IllegalStateException("Primero debe generar un arreglo.");
            }
            int target = Integer.parseInt(targetField.getText());
            ModuloSearch.SearchResult result = ms.search(array, target);
            resultArea.append("\n\nResultado de la búsqueda por módulo:");
            resultArea.append("\nValor buscado: " + target);
            resultArea.append("\nÍndice encontrado: " + (result.getIndex() != -1 ? result.getIndex() : "No encontrado"));
            resultArea.append("\nTiempo de ejecución: " + result.getExecutionTimeNanos() + " nanosegundos");
            resultArea.append("\nIteraciones: " + result.getIterations());
        } catch (NumberFormatException ex) {
            resultArea.setText("Error: Ingrese un valor válido para buscar.");
        } catch (IllegalStateException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void performMultiplicationSearch() {
        try {
            if (array == null) {
                throw new IllegalStateException("Primero debe generar un arreglo.");
            }
            int target = Integer.parseInt(targetField.getText());
            MultiplicationSearch.SearchResult result = multSearch.search(array, target);
            resultArea.append("\n\nResultado de la búsqueda por multiplicación:");
            resultArea.append("\nValor buscado: " + target);
            resultArea.append("\nÍndice encontrado: " + (result.getIndex() != -1 ? result.getIndex() : "No encontrado"));
            resultArea.append("\nTiempo de ejecución: " + result.getExecutionTimeNanos() + " nanosegundos");
            resultArea.append("\nIteraciones: " + result.getIterations());
        } catch (NumberFormatException ex) {
            resultArea.setText("Error: Ingrese un valor válido para buscar.");
        } catch (IllegalStateException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    // Método único para actualizar las vistas de las tablas hash
    private void updateHashTableViews() {
        // Actualizar tabla hash por módulo
        hashTableArea.setText("Tabla Hash (Módulo):\n");
        for (int i = 0; i < ms.getTableSize(); i++) {
            hashTableArea.append(String.format("Índice %2d: %s\n", i, ms.getHashTableValue(i)));
        }

        // Actualizar tabla hash por multiplicación
        multHashTableArea.setText("Tabla Hash (Multiplicación):\n");
        for (int i = 0; i < multSearch.getTableSize(); i++) {
            multHashTableArea.append(String.format("Índice %2d: %s\n", i, multSearch.getHashTableValue(i)));
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // This creates and displays your GUI
        new BinarySearchGUI();
    }
}