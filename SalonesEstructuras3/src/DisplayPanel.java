import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DisplayPanel extends JPanel {
    connection dbHandler = connection.getInstance();
        String [] facultadesArr = {"Todas", "Ingeniería", "Ciencias", "Humanidades", "Ciencias Sociales", "Salud", "Negocios"};
    JTable displayTable;
    public DisplayPanel(){

        this.setLayout(new GroupLayout (this));
        GroupLayout layout = (GroupLayout) this.getLayout();

        JLabel titleLabel = new JLabel("Clases");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel escuelaLabel = new JLabel("Escuela:");
        JComboBox<String> escuelasComboBox = new JComboBox<>(facultadesArr);

        JButton applyFilterButton = new JButton("Aplicar filtro");

        //Table Model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Código");
        model.addColumn("Materia");
        model.addColumn("Dias");
        model.addColumn("Capacidad");
        model.addColumn("Hora");
        model.addColumn("Duración");
        model.addColumn("Salon");
        model.addColumn("Edificio");
        model.addColumn("Escuela");
        model.addColumn("Carrera");
        model.addColumn("Semestre");
        displayTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(displayTable);

        loadDefaultData();

        // Define Horizontal Groups
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(titleLabel)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(escuelaLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(escuelasComboBox)))
                .addComponent(applyFilterButton)
                .addComponent(scrollPane));
        layout.setHorizontalGroup(hGroup);

        // Define Vertical Groups
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(titleLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(escuelaLabel)
                        .addComponent(escuelasComboBox))
                .addComponent(applyFilterButton)
                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE);
        layout.setVerticalGroup(vGroup);

        applyFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sqlQueryBuilder = new StringBuilder("FROM Clases c ");
                ArrayList<Object> parameters = new ArrayList<>();

                if (!Objects.equals(escuelasComboBox.getSelectedItem(), "Todas")){
                    sqlQueryBuilder.append(parameters.isEmpty() ? "WHERE " : "AND ").append("c.escuela = ?  ");
                    parameters.add(escuelasComboBox.getSelectedItem());
                }
                sqlQueryBuilder.insert(0, "SELECT c.* ");
                sqlQueryBuilder.append(" ORDER BY c.carrera, c.semestre ASC");

                // Convert StringBuilder to String to get the completed query
                String sqlQuery = sqlQueryBuilder.toString();
                List<String[]> results = dbHandler.getData(sqlQuery, parameters.toArray());
                //Update the table model with the results
                DefaultTableModel model = (DefaultTableModel) displayTable.getModel();
                model.setRowCount(0);
                for (String[] row: results){
                    model.addRow(row);
                }

            }
        });
    }
    private void loadDefaultData() {
        String sqlQuery = "SELECT * FROM Clases ORDER BY carrera, semestre ASC;";
        List<String[]> results = dbHandler.getData(sqlQuery, new Object[]{});
        DefaultTableModel model = (DefaultTableModel) displayTable.getModel();
        model.setRowCount(0);
        for (String[] row : results) {
            model.addRow(row);
        }
    }
}
