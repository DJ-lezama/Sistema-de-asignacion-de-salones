import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends JFrame {
    connection dbHandler = connection.getInstance();
    CampusGraph campus = CampusGraph.getInstance();
    private AddClassPanel addClassPanel;
    private SchedulePanel schedulePanel;
    private DeleteClassPanel deleteClassPanel;
    private DisplayPanel displayPanel;
    private JSplitPane splitPaneV;
    private JPanel optionsPanel;


    public MainGUI(){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        addClassPanel = new AddClassPanel();
        schedulePanel = new SchedulePanel();
        deleteClassPanel = new DeleteClassPanel();
        displayPanel = new DisplayPanel();
        createOptionsPanel();

        //Actualizar la matriz de horarios con los datos guardados anteriormente en la DB
        List<String[]> savedData = dbHandler.loadDataFromDataBase();
        if(campus.LoadDataBaseData((ArrayList<String[]>) savedData)) {
            System.out.println("Done");
            campus.PrintHorario("IA");
            campus.PrintHorario("NE");
            System.out.println("\nClasesAsignadas size: "+campus.clasesAsignadas.size());

        } else {
            System.out.println("No fue posible cargar la información de la base de datos");
        }

        splitPaneV = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, addClassPanel, optionsPanel);
        splitPaneV.setOneTouchExpandable(true);
        splitPaneV.setDividerLocation(0.75); // 75% for the left component and 25% for the right
        mainPanel.add(splitPaneV, BorderLayout.CENTER);

        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680, 400); // Set the size of the frame

    }
    public void createOptionsPanel(){
        optionsPanel = new JPanel();
        GroupLayout layout2 = new GroupLayout(optionsPanel);
        optionsPanel.setLayout(layout2);

        layout2.setAutoCreateGaps(true);
        layout2.setAutoCreateContainerGaps(true);

        //Swing elements set up
        JLabel menuLabel = new JLabel("Menú");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton newScheduleButton = new JButton("Crear un nuevo horario");
        JButton newClassButton = new JButton("Agregar nueva clase");
        JButton deleteClassButton = new JButton("Eliminar clase");
        JButton displayClassesButton = new JButton("Visualizar clases");

        //Layout set up
        // Horizontal group
        GroupLayout.ParallelGroup hGroup = layout2.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(menuLabel)
                .addComponent(newScheduleButton)
                .addComponent(newClassButton)
                .addComponent(deleteClassButton)
                .addComponent(displayClassesButton);

        // Vertical group
        GroupLayout.SequentialGroup vGroup = layout2.createSequentialGroup()
                .addComponent(menuLabel)
                .addComponent(newScheduleButton)
                .addComponent(newClassButton)
                .addComponent(deleteClassButton)
                .addComponent(displayClassesButton);

        layout2.setHorizontalGroup(hGroup);
        layout2.setVerticalGroup(vGroup);

        //Adding the button handler
        ButtonHandler optionsHandler = new ButtonHandler();
        newScheduleButton.addActionListener(optionsHandler);
        newClassButton.addActionListener(optionsHandler);
        deleteClassButton.addActionListener(optionsHandler);
        displayClassesButton.addActionListener(optionsHandler);

        //setting ActionCommands
        newScheduleButton.setActionCommand("newSchedule");
        newClassButton.setActionCommand("newClass");
        deleteClassButton.setActionCommand("delete");
        displayClassesButton.setActionCommand("display");

    }
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();
            splitPaneV.setDividerLocation(0.70); // 75% for the left component and 25% for the right

            switch(command){
                case "newSchedule":
                    splitPaneV.setLeftComponent(schedulePanel); // Replace with schedule panel
                    break;
                case "newClass":
                    splitPaneV.setLeftComponent(addClassPanel); // Replace with add class panel
                    break;
                case "delete":
                    splitPaneV.setLeftComponent(deleteClassPanel);
                    break;
                case "display":
                    splitPaneV.setLeftComponent(displayPanel);
                    break;
            }

            splitPaneV.revalidate(); // Refresh the split pane to update its layout
            splitPaneV.repaint(); // Repaint to ensure the UI updates properly
        }

    }

    public static void main(String args[]) {
        MainGUI gui = new MainGUI();
        gui.setVisible(true);
    }
}
