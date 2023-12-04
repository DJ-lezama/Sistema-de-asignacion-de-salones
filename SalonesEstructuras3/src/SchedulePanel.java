import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SchedulePanel extends JPanel {
    CampusGraph campus = CampusGraph.getInstance();
    connection dbHandler = connection.getInstance();
    private JTable scheduleTable;
    boolean whereAdded;
    JComboBox<String> carrerasDDMenu = new JComboBox<>();
    JComboBox<Object> semesterComboBox = new JComboBox<>();
    String [] carrerasIngenieriasArr = {"Ambiental", "Civil", "Biomédica", "Energía", "Industrias Alimentarias", "Logística y Cadena de Suministros", "Robótica y Telecomunicaciónes", "Sistemas Computacionales", "Industrial", "Mecánica", "Mecatrónica", "Química"};
    String [] carrerasHumanidadesArr = {"Animación Digital", "Arquitectura", "Arquitectura de Interiores", "Artes Plásticas", "Danza", "Diseño de Información Visual", "Historia del Arte y Curaduría", "Idiomas", "Idiomas", "Literatura", "Música", "Teatro"};
    String [] carrerasCienciasArr = {"Actuaría", "Biología", "Bioquímica Clínica", "Ciencias de Datos","Nanotecnología e Ingeniería Molecular"};
    String [] carrerasSaludArr = {"Médico Cirujano", "Químico Farmacéutico Biologo", "Cirujano Dentista", "Ciencias de la Nutrición"};
    String [] carrerasSocialesArr= {"Antropología", "Ciencia Política", "Comunicación y Producción de Medios", "Comunicación y Relaciones Públicas", "Derecho", "Pedagogía", "Psicología Clínica", "Psicología Organizacional", "Relaciones Internacionales", "Relaciones Multiculturales"};
    String [] carrerasNegociosArr = {"Administración de Empresas", "Administración de Hoteles y Restaurantes", "Administración de Negocios Internacionales", "Artes Culinarias", "Banca e Inversiones", "Economía", "Estrategias Financieras y Contaduría Pública", "Mercadotécnia"};
    String [] facultadesArr = {"Seleccionar", "Ingeniería", "Ciencias", "Humanidades", "Ciencias Sociales", "Salud", "Negocios"};

    public SchedulePanel(){
        this.setLayout(new GroupLayout (this));
        GroupLayout layout = (GroupLayout) this.getLayout();

        // Components initialization
        JLabel tituloLabel = new JLabel("Generador de horarios");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel facultadLabel = new JLabel("Escuela: ");
        JComboBox<String> facultadesDDMenu = new JComboBox<>(facultadesArr);

        JLabel carreraLabel = new JLabel("Carrera: ");
        // carrerasDDMenu initialized in class

        JLabel semestreLabel = new JLabel("Semestre: ");
        semesterComboBox.addItem("Seleccionar");
        for (int i = 1; i <= 8; i++) {
            semesterComboBox.addItem(i);
        }

        JLabel tableTitle = new JLabel("Horario disponible");

        //Table Model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Código");
        model.addColumn("Clase");
        model.addColumn("Dias");
        model.addColumn("Capacidad");
        model.addColumn("Hora");
        model.addColumn("Duración");
        model.addColumn("Salon");
        scheduleTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton generateScheduleButton = new JButton("Generar horario");

        // Define Horizontal Groups
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(tituloLabel)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(facultadLabel)
                                .addComponent(carreraLabel)
                                .addComponent(semestreLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(facultadesDDMenu)
                                .addComponent(carrerasDDMenu)
                                .addComponent(semesterComboBox)))
                .addComponent(generateScheduleButton)
                .addComponent(tableTitle)
                .addComponent(scrollPane));
        layout.setHorizontalGroup(hGroup);

        // Define Vertical Groups
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(tituloLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(facultadLabel)
                        .addComponent(facultadesDDMenu))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(carreraLabel)
                        .addComponent(carrerasDDMenu))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(semestreLabel)
                        .addComponent(semesterComboBox))
                .addComponent(generateScheduleButton)
                .addComponent(tableTitle,  GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE);
        layout.setVerticalGroup(vGroup);

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

        //generateScheduleButton actionListener
        generateScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sqlQueryBuilder = new StringBuilder("FROM Clases c ");
                ArrayList<Object> parameters = new ArrayList<>();

                //get carrera
                if (!Objects.equals(carrerasDDMenu.getSelectedItem(), "Seleccionar")) {
                    sqlQueryBuilder.append(parameters.isEmpty() ? "WHERE " : "AND ").append("c.carrera = ? ");
                    parameters.add(carrerasDDMenu.getSelectedItem());
                    whereAdded = true;
                }

                //get semester
                if (!Objects.equals(semesterComboBox.getSelectedItem(), "Seleccionar")) {
                    sqlQueryBuilder.append(parameters.isEmpty() ? "WHERE " : "AND ").append("c.semestre = ? ");
                    parameters.add(semesterComboBox.getSelectedItem());
                    whereAdded = true;
                }

                //Build query
                sqlQueryBuilder.insert(0, "SELECT c.* ");
                String sqlQuery = sqlQueryBuilder.toString();

                List<String[]> results = dbHandler.getSchedule(sqlQuery, parameters.toArray());
                //Update the table model with the results
                DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
                model.setRowCount(0);
                for (String[] row: results){
                    model.addRow(row);
                }

                scheduleTable.getSelectionModel().addListSelectionListener(event -> {
                    if (!event.getValueIsAdjusting()) { //handle only the final selection event
                        int selectedClassRow = scheduleTable.getSelectedRow();
                        if (selectedClassRow != -1){
                            String message = "¿Desea inscribir esta clase?";
                            int reply = JOptionPane.showConfirmDialog(null, message, "Inscribir", JOptionPane.YES_NO_OPTION);
                            if (reply == JOptionPane.YES_OPTION) {
                                //Obtener el código de la materia a parter del SelectedRow
                                String selectedCodigo = (String) model.getValueAt(selectedClassRow, 0);
                                System.out.println(selectedCodigo);
                                //buscar la clase con el código
                                int index = campus.findClass(selectedCodigo);
                                System.out.println(index);
                                //inscribir la clase
                                campus.InscribirAlumnos(selectedCodigo);
                                //if (campus.InscribirAlumno(campus.clasesAsignadas.get(index))){
                                    //int nuevaCapacidad = campus.clasesAsignadas.get(index).capacidadNecesaria;
                                    //System.out.println("\n Nueva capacidad: "+nuevaCapacidad);
                                    //dbHandler.updateCapacidad(selectedCodigo, nuevaCapacidad);
                                   // model.removeRow(selectedClassRow);
                               // } else {
                                   // scheduleTable.getSelectionModel().clearSelection();
                                //}

                            } else {
                                scheduleTable.getSelectionModel().clearSelection();
                            }

                        }
                    }
                });
            }
        });
    }
}
