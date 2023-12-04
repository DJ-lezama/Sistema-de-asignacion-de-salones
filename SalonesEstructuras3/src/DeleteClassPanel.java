import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DeleteClassPanel extends JPanel {
    connection dbHandler = connection.getInstance();
    CampusGraph campus = CampusGraph.getInstance();
    JTable displayTable;
    public DeleteClassPanel(){

        this.setLayout(new FlowLayout());
        JLabel titleLabel = new JLabel("Eliminar clase");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(titleLabel);

        //Table Model setup
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable (int row, int column){
                return false; // Non-editable cells
            }
        };

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
        displayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(scrollPane);

        loadDefaultData();

        JButton updateButton = new JButton("Actualizar tabla");
        this.add(updateButton);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDefaultData();
            }
        });

        displayTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) { //handle only the final selection event
                int selectedClassRow = displayTable.getSelectedRow();
                if (selectedClassRow != -1){
                    String message = "¿Desea eliminar esta clase?";
                    int reply = JOptionPane.showConfirmDialog(null, message, "Inscribir", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        //Obtener el código de la materia a parter del SelectedRow
                        String selectedCodigo = (String) model.getValueAt(selectedClassRow, 0);
                        System.out.println(selectedCodigo);
                        //buscar la clase con el código
                        int index = campus.findClass(selectedCodigo);
                        System.out.println(index);
                        deleteSelection();
                        campus.EliminarClase(campus.clasesAsignadas.get(index));
                        displayTable.getSelectionModel().clearSelection();
                    } else {
                        displayTable.getSelectionModel().clearSelection();
                    }
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
    private void deleteSelection(){
        int selectedRow = displayTable.getSelectedRow();
        if (selectedRow != -1){
            String codigo = displayTable.getModel().getValueAt(selectedRow, 0).toString();
            // Execute delete query
            dbHandler.deleteClass(codigo);
            loadDefaultData();
        }
    }
}
