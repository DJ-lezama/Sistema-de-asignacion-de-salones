import java.util.*;

public class Edificio {

    public String nombre;
    public int salonesTotales;
    private int salonesActuales = 100; // CN TIENE X salones actualmente (en este momento)
    private ArrayList<Salon> edificioSalones = new ArrayList<>(); // hold edificio salones
    private static Random random = new Random(); // random to initialize edificios...
    private Horarios horarios;

    public Edificio(String nombre, int numSalones, Horarios horarios) {
        this.nombre = nombre;
        this.salonesTotales = numSalones;
        this.horarios = horarios; // Initializing the Horarios instance
        this.SetEdificio();
    }

    private void SetEdificio() // agrega "numSalones" salones. inicializar capacidad 15 a 50 de los salones
    {
        for (int i = 0; i < salonesTotales; i++)
        {
            String salonID = this.nombre + salonesActuales;
            edificioSalones.add(new Salon(this.nombre,this.salonesActuales,random.nextInt(15,50))); //15 a 25 random (creo xd)
            salonesActuales++;
        }
    }
    // Revisa si en este edificio se puede agregar esta clase en alguno de los salones del edificio

    public boolean addClass(Clase clase) {
        sortListByCapacity();

        int index = getClosestCapClass(clase.capacidadNecesaria); // list[index] da una capacidad igual o mayor a la deseada

        Clase clase2Add = clase;

        for (int i = index; i < edificioSalones.size(); i++) {

            // Si la capacidad que necesitas se pasa por 5 de la mas cercana disponible, no agregues
            if (clase2Add.capacidadNecesaria > edificioSalones.get(index).capacidad + 5) {
                System.out.println("no agregado por exceso de cap necesaria");
                return false;
            }

            // Checa si este salon esta en horario disponible
            // Si -> agregalo en el horario y edificio
            // No -> checa el siguiente...
            if (this.horarios.AssignHorario(clase2Add, clase2Add.dias, clase2Add.horaInicio, clase2Add.duracion,edificioSalones.get(i).ID,this.nombre)) {
                // if the salon is available in the schedule...
                clase2Add.salon = edificioSalones.get(i);
                clase2Add.edificio = this.nombre;
                return true;
            }
            else{
                System.out.println("horario ocupado, cheando siguiente salon en lista");
            }
        }
        return false;
    }
    public void sortListByCapacity()
    {
        Collections.sort(edificioSalones, new CompareList());
    }

    private int getClosestCapClass(int capacidad) //return index where capacity is = or mmore
    {
        int sal = 0;
        for(int i = 0; i<edificioSalones.size(); i++)
        {
            if(edificioSalones.get(i).capacidad >= capacidad)
            {
                sal = i;
                break;
            }
        }
        return sal;
    }
    public Salon findSalonForClass(Clase clase) {
        for (Salon salon : edificioSalones) {
            if (salon.capacidad >= clase.capacidadNecesaria)
                return salon; // Return the first suitable salon found
        }
        return null; // Return null if no suitable salon is found
    }

    public void PrintSalones()
    {
        for (Salon sal :edificioSalones) {
            System.out.println("Salon "+ sal.ID+" tiene capacidad "+sal.capacidad);
        }
    }

    public void PrintHorario()
    {
        this.horarios.PrintTable();
    }

    public void EliminarClase(Clase clase)
    {
        this.horarios.EliminarClase(clase);
    }
    public void SalonesAsignados()
    {
        this.horarios.PrintTable();
    }

    public static class CompareList implements Comparator<Salon> {
        private ArrayList<Salon> sList;
        @Override
        public int compare(Salon s1, Salon s2) {
            return Integer.compare(s1.capacidad, s2.capacidad);
        }
        public void CompareObj() {
            sList = new ArrayList<Salon>();
        }
        public void add(Salon s1) {
            sList.add(s1);
        }
        public void displayList() {
            for(int i = 0; i<sList.size();i++) {
                System.out.println(sList.get(i).ID);
            }
        }
    }

    public Boolean LoadDB(int dias, int duracion, int cap,double horaInicio,String salonID )
    {
        int ID = getClassCode(salonID);

        return this.horarios.LoadDB(dias,duracion,horaInicio,salonID,new Salon(this.nombre,ID,cap) );

    }

    public int getClassCode(String claseID)
    {
        for(int i = 0; i < edificioSalones.size(); i++)
        {
            if(Objects.equals(edificioSalones.get(i).toString(), claseID))
            {
                return i +100;
            }
        }
        return -1;
    }
}
