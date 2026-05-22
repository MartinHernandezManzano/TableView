package org.example.view;

// JavaFX
import javafx.application.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.collections.transformation.*;

// Proyecto
import org.example.controller.*;
import org.example.model.*;

public class Vista extends Application {

    // los declaramos aquí para que todos los métodos puedan acceder a ellos
    private TableView<Empleado> tabla = new TableView<>();
    private TextField campNombre  = new TextField();
    private TextField campSalario = new TextField();
    private Controlador controlador = new Controlador();
    private ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();
    private FilteredList<Empleado> listaFiltrada;

    @Override
    public void start(Stage stage) {

        // ── TABLA ──────────────────────────────────────────────
        TableColumn<Empleado, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Empleado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Empleado, Double> colSalario = new TableColumn<>("Salario");
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));

        tabla.getColumns().addAll(colId, colNombre, colSalario);
        tabla.setMaxWidth(Double.MAX_VALUE);
        tabla.setMaxHeight(Double.MAX_VALUE);

        // al seleccionar una fila, autocompleta los campos del formulario
        tabla.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        campNombre.setText(seleccionado.getNombre());
                        campSalario.setText(String.valueOf(seleccionado.getSalario()));
                    }
                }
        );

        cargarTabla();

        // ── FORMULARIO ─────────────────────────────────────────
        campNombre.setPromptText("Nombre");
        campSalario.setPromptText("Salario");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Nombre:"),  0, 0);
        form.add(campNombre,            1, 0);
        form.add(new Label("Salario:"), 0, 1);
        form.add(campSalario,           1, 1);

        // ── BOTONES ────────────────────────────────────────────
        Button btnInsertar  = new Button("Insertar");
        Button btnActualizar = new Button("Actualizar");
        Button btnEliminar  = new Button("Eliminar");

        // INSERT — coge los campos y llama al controlador
        btnInsertar.setOnAction(e -> {
            String nombre = campNombre.getText();
            double salario = Double.parseDouble(campSalario.getText());
            controlador.insertar(nombre, salario);
            cargarTabla(); // recarga la tabla para ver el cambio
            limpiarCampos();
        });

        // UPDATE — necesita el id del empleado seleccionado en la tabla
        btnActualizar.setOnAction(e -> {
            Empleado seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                String nombre = campNombre.getText();
                double salario = Double.parseDouble(campSalario.getText());
                controlador.actualizar(seleccionado.getId(), nombre, salario);
                cargarTabla();
                limpiarCampos();
            }
        });

        // DELETE — elimina el empleado seleccionado en la tabla
        btnEliminar.setOnAction(e -> {
            Empleado seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                controlador.eliminar(seleccionado.getId());
                cargarTabla();
                limpiarCampos();
            }
        });

        HBox botones = new HBox(10, btnInsertar, btnActualizar, btnEliminar);

        TextField campBuscar = new TextField();
        campBuscar.setPromptText("Buscar por nombre...");
        campBuscar.setMaxWidth(Double.MAX_VALUE);

        // listener que se dispara cada vez que cambia el texto del campo
                campBuscar.textProperty().addListener((obs, anterior, nuevo) -> {
                    listaFiltrada.setPredicate(empleado -> {
                        // si el campo está vacío mostramos todos
                        if (nuevo == null || nuevo.isEmpty()) return true;
                        // comparamos en minúsculas para que no distinga mayúsculas
                        return empleado.getNombre().toLowerCase().contains(nuevo.toLowerCase());
                    });
                });
        // ── LAYOUT FINAL ───────────────────────────────────────
        VBox root = new VBox(10, campBuscar, tabla, form, botones);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 450, 500);
        stage.setTitle("Ejercicio 24 - CRUD");
        stage.setScene(scene);
        stage.show();
    }

    // recarga la tabla pidiendo todos los empleados al controlador
    private void cargarTabla() {
        // cargamos los datos en la lista observable
        listaEmpleados.setAll(controlador.obtenerEmpleados());

        // FilteredList envuelve la lista y aplica el filtro dinámicamente
        listaFiltrada = new FilteredList<>(listaEmpleados, p -> true);
        tabla.setItems(listaFiltrada);
    }

    // limpia los campos del formulario tras cada operación
    private void limpiarCampos() {
        campNombre.setText("");
        campSalario.setText("");
    }

    public static void main(String[] args) {
        launch();
    }
}