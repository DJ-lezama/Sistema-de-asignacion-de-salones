import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Clase AddClassPanel que extiende de JPanel.
 * Esta clase se utiliza para agregar una nueva clase en un sistema de gestión escolar.
 * Incluye campos para ingresar la información de la clase y un botón para asignar un salón.
 */
public class AddClassPanel extends JPanel {
    // Instancia del campus y del manejador de base de datos
    CampusGraph campus = CampusGraph.getInstance();
    connection dbHandler = connection.getInstance();

    // Etiquetas y campos de texto para la entrada de datos de la clase
    JLabel agregarClaseLabel, codigoLabel, nombreLabel, diasLabel, inicioLabel, duracionLabel, escuelaLabel, carreraLabel2, semestreLabel2;
    JTextField codigoTextField, nombreTextField;

    // Botón para asignar el salón a la clase
    JButton asignarSalonButton;
    JPanel addClassPanel;

    // Variables para almacenar la duración y los días de la clase
    int duracion, dias;

    // Menús desplegables para seleccionar la carrera y el semestre
    JComboBox<String> carrerasDDMenu = new JComboBox<>();
    JComboBox<Object> semesterComboBox = new JComboBox<>();

    // Arreglos de strings con las opciones para las carreras en cada facultad
    String [] carrerasIngenieriasArr = {"Ambiental", "Civil", "Biomédica", "Energía", "Industrias Alimentarias", "Logística y Cadena de Suministros", "Robótica y Telecomunicaciónes", "Sistemas Computacionales", "Industrial", "Mecánica", "Mecatrónica", "Química"};
    String [] carrerasHumanidadesArr = {"Animación Digital", "Arquitectura", "Arquitectura de Interiores", "Artes Plásticas", "Danza", "Diseño de Información Visual", "Historia del Arte y Curaduría", "Idiomas", "Idiomas", "Literatura", "Música", "Teatro"};
    String [] carrerasCienciasArr = {"Actuaría", "Biología", "Bioquímica Clínica", "Ciencias de Datos","Nanotecnología e Ingeniería Molecular"};
    String [] carrerasSaludArr = {"Médico Cirujano", "Químico Farmacéutico Biologo", "Cirujano Dentista", "Ciencias de la Nutrición"};
    String [] carrerasSocialesArr= {"Antropología", "Ciencia Política", "Comunicación y Producción de Medios", "Comunicación y Relaciones Públicas", "Derecho", "Pedagogía", "Psicología Clínica", "Psicología Organizacional", "Relaciones Internacionales", "Relaciones Multiculturales"};
    String [] carrerasNegociosArr = {"Administración de Empresas", "Administración de Hoteles y Restaurantes", "Administración de Negocios Internacionales", "Artes Culinarias", "Banca e Inversiones", "Economía", "Estrategias Financieras y Contaduría Pública", "Mercadotécnia"};
    String [] facultadesArr = {"Seleccionar", "Ingeniería", "Ciencias", "Humanidades", "Ciencias Sociales", "Salud", "Negocios"};

    /**
     * Constructor de AddClassPanel.
     * Inicializa los componentes de la interfaz de usuario y define el diseño y comportamiento.
     */

    public AddClassPanel(){
        // Configura el diseño del panel usando GroupLayout
        this.setLayout(new GroupLayout(this));
        GroupLayout layout2 = (GroupLayout) this.getLayout();

        // Inicialización y configuración de los componentes de la interfaz
        agregarClaseLabel = new JLabel("Agregar clase");
        agregarClaseLabel.setFont(new Font("Arial", Font.BOLD, 16));

        codigoLabel = new JLabel("Código:");
        codigoTextField = new JTextField(20);

        nombreLabel = new JLabel("Nombre:");
        nombreTextField = new JTextField(20);

        JLabel capacidadLabel = new JLabel("Capacidad:");
        JTextField capacidadTextField = new JTextField(20);

        diasLabel = new JLabel("Días:");
        JComboBox<String> diasComboBox = new JComboBox<>();
        diasComboBox.addItem("1 - Lunes y Miércoles");
        diasComboBox.addItem("2 - Martes y Jueves");
        diasComboBox.addItem("3 - Viernes y Sábado");

        // Listener para la selección de días
        String diasSelectedItem = (String) diasComboBox.getSelectedItem();
        // Use the selected item to obtain the days the class is taught
        diasComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) diasComboBox.getSelectedItem();
                switch (Objects.requireNonNull(selectedItem)) {
                    case "1 - Lunes y Miércoles":
                        dias = 1;
                        break;
                    case "2 - Martes y Jueves":
                        dias = 2;
                        break;
                    case "3 - Viernes y Sábado":
                        dias = 3;
                        break;
                }
            }
        });

        inicioLabel = new JLabel("Hora de inicio:");
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);

        duracionLabel = new JLabel("Duración:");
        JComboBox<Object> duracionComboBox = new JComboBox<>();
        duracionComboBox.addItem("1 - 50");
        duracionComboBox.addItem("2 - 1:15");
        duracionComboBox.addItem("3 - 1:40");

        // Listener para la selección de la duración
        duracionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String duracionSelectedItem = (String) duracionComboBox.getSelectedItem();
                // Use the selected item to obtain the days the class is taught
                switch (Objects.requireNonNull(duracionSelectedItem)){
                    case "1 - 50":
                        duracion = 1;
                        break;
                    case "2 - 1:15":
                        duracion = 2;
                        break;
                    case "3 - 1:40":
                        duracion = 3;
                        break;
                }
            }
        });

        escuelaLabel = new JLabel("Escuela: ");
        JComboBox<String> facultadesDDMenu = new JComboBox<>(facultadesArr);

        carreraLabel2 = new JLabel("Carrera:");

        semestreLabel2 = new JLabel("Semestre:");
        semesterComboBox.addItem("Seleccionar");
        for (int i = 1; i <= 8; i++) {
            semesterComboBox.addItem(i);
        }
        asignarSalonButton = new JButton("Asignar salón");

        // Define Horizontal Groups
        GroupLayout.SequentialGroup hGroup = layout2.createSequentialGroup();
        hGroup.addGroup(layout2.createParallelGroup()
                .addComponent(agregarClaseLabel)
                .addGroup(layout2.createSequentialGroup()
                        .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(codigoLabel)
                                .addComponent(nombreLabel)
                                .addComponent(capacidadLabel)
                                .addComponent(diasLabel)
                                .addComponent(duracionLabel)
                                .addComponent(inicioLabel)
                                .addComponent(escuelaLabel)
                                .addComponent(carreraLabel2)
                                .addComponent(semestreLabel2))
                        .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(codigoTextField)
                                .addComponent(nombreTextField)
                                .addComponent(capacidadTextField)
                                .addComponent(diasComboBox)
                                .addComponent(duracionComboBox)
                                .addComponent(timeSpinner)
                                .addComponent(facultadesDDMenu)
                                .addComponent(carrerasDDMenu)
                                .addComponent(semesterComboBox)))
                .addComponent(asignarSalonButton));
        layout2.setHorizontalGroup(hGroup);

        // Define Vertical Groups
        GroupLayout.SequentialGroup vGroup = layout2.createSequentialGroup();
        vGroup.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(agregarClaseLabel))
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(codigoLabel)
                        .addComponent(codigoTextField))
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nombreLabel)
                        .addComponent(nombreTextField))
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(capacidadLabel)
                        .addComponent(capacidadTextField))
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(diasLabel)
                        .addComponent(diasComboBox))
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(duracionLabel)
                        .addComponent(duracionComboBox))
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(inicioLabel)
                        .addComponent(timeSpinner))
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(escuelaLabel)
                        .addComponent(facultadesDDMenu))
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(carreraLabel2)
                        .addComponent(carrerasDDMenu))
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(semestreLabel2)
                        .addComponent(semesterComboBox))
                .addComponent(asignarSalonButton);
        layout2.setVerticalGroup(vGroup);

        // carrerasDDMenu initialized in class
        // Action Listener for facultadesDDMenu
        facultadesDDMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) facultadesDDMenu.getSelectedItem();
                DefaultComboBoxModel<String> model;

                switch (Objects.requireNonNull(selected)) {
                    case "Ingeniería":
                        model = new DefaultComboBoxModel<>(carrerasIngenieriasArr);
                        carrerasDDMenu.setModel(model);
                        break;
                    case "Ciencias":
                        model = new DefaultComboBoxModel<>(carrerasCienciasArr);
                        carrerasDDMenu.setModel(model);
                        break;
                    case "Humanidades":
                        model = new DefaultComboBoxModel<>(carrerasHumanidadesArr);
                        carrerasDDMenu.setModel(model);
                        break;
                    case "Ciencias Sociales":
                        model = new DefaultComboBoxModel<>(carrerasSocialesArr);
                        carrerasDDMenu.setModel(model);
                        break;
                    case "Salud":
                        model = new DefaultComboBoxModel<>(carrerasSaludArr);
                        carrerasDDMenu.setModel(model);
                        break;
                    case "Negocios":
                        model = new DefaultComboBoxModel<>(carrerasNegociosArr);
                        carrerasDDMenu.setModel(model);
                        break;
                }
            }
        });

        //asignarSalon ActionLisener
        asignarSalonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get all the information from the GUI
                String codigo = codigoTextField.getText();
                String nombreMateria = nombreTextField.getText();
                //duración obtained before from action listener
                //dias obtained before from action Listener
                String capacidadTextFieldContent = capacidadTextField.getText();
                int capacidad = Integer.parseInt(capacidadTextFieldContent.trim());
                Date horaClase = (Date) timeSpinner.getValue();
                String formattedTime = new SimpleDateFormat("HH:mm:ss").format(horaClase);
                System.out.println(formattedTime);
                double horaInicioDouble = Date2Double.Convert(String.valueOf(formattedTime));
                String escuela = (String) facultadesDDMenu.getSelectedItem();
                String carrera = (String) carrerasDDMenu.getSelectedItem();
                int semestre = (Integer) semesterComboBox.getSelectedItem();

                int finalDuracion = duracion;
                int finalDias = dias;
                Clase nuevaClase = new Clase(codigo, nombreMateria, finalDias, capacidad, formattedTime, horaInicioDouble, finalDuracion, escuela, carrera, semestre);
                if (campus.AssingClass(nuevaClase)){
                    String edificio = String.valueOf(nuevaClase.edificio);
                    System.out.print(edificio + "\n");
                    String salon = String.valueOf(nuevaClase.salon);
                    dbHandler.insertDataToDatabase(codigo, nombreMateria, dias, capacidad, formattedTime, finalDuracion, salon, edificio, escuela, carrera, semestre);
                } else {
                    System.out.println("Error");
                }

                codigoTextField.setText(" ");
                nombreTextField.setText(" ");
                capacidadTextField.setText(" ");

            }
        });

    }

}
