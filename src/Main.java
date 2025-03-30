import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BinarySearchGUI::new);
    }
}

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
        JButton generateSortedButton = new JButton("Generar Arreglo Ordenado");
        generateSortedButton.addActionListener(e -> generateArray(true));
        controlPanel.add(generateSortedButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton generateUnsortedButton = new JButton("Generar Arreglo Desordenado");
        generateUnsortedButton.addActionListener(e -> generateArray(false));
        controlPanel.add(generateUnsortedButton, gbc);

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

    private void generateArray(boolean sorted) {
        try {
            int size = Integer.parseInt(sizeField.getText());
            array = new int[size];
            Random rand = new Random();

            // Crear nuevas instancias de las tablas hash con el tamaño especificado
            ms = new ModuloSearch(size);
            multSearch = new MultiplicationSearch(size);

            for (int i = 0; i < size; i++) {
                array[i] = rand.nextInt(100);
                ms.insert(array[i]);
                multSearch.insert(array[i]);
            }

            if (sorted) {
                Arrays.sort(array);
                isSorted = true;
            } else {
                isSorted = false;
            }

            resultArea.setText("Arreglo generado: " + Arrays.toString(array));
            updateHashTableViews();
        } catch (NumberFormatException ex) {
            resultArea.setText("Error: Ingrese un tamaño válido.");
        }
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