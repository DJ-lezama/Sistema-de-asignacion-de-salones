import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Main class for the scheduling system
public class Main {
    public static void main(String[] args) {

        CampusGraph campus = CampusGraph.getInstance();


        Clase clase2Add1 = new Clase("LIS2022", "Arquitecturas Computacionales", 1,20,"08:30:00", 8.5,2,"Ingeniería", "Sistemas Computacionales", 4);
        Clase clase2Add2 = new Clase("LIS3062", "Ingeniería de Software", 2,20,"08:30:00", 8.5,3,"Ciencias", "Sistemas Computacionales", 6);


        //Inicializa el programa:
        // Antes de cargar contenido de la base...
        System.out.println("Cargando la base de datos");
        campus.PrintHorario("CN");
        String[] row = new String[11];
        row[0] = "LIS2082";
        row[1] = "bases de datos"; // nombre
        row[2] = "1"; // dias
        row[3] = "30"; // cap
        row[4] = "08:30:00"; // inicio
        row[5] = "2"; // duracion
        row[6] = "CN101"; // salon id
        row[7] = "CN"; // ed
        row[8] = "Ingeniería"; // facultad/ escuela
        row[9] = "Sistemas Computacionales"; //carrera
        row[10] = "1"; // semestre
        ArrayList<String[]> dbClases = new ArrayList<>();
        dbClases.add(row);
        if(campus.LoadDataBaseData(dbClases)) {
            System.out.println("Done");
            campus.PrintHorario("CN");
        } else {
            System.out.println("No se pudo cargar la información de la base de datos.");
        }


    }
}
