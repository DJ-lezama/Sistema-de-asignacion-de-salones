import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Clase {
    public String codigo;
    public String nombre;
    public String edificio; // a travez de edificio podriamos saber nodo inicial...
    public int dias; // 1 -> Lunes y miercoles, 2 -> Martes y jueves , 3 -> viernes y sabado
    public int capacidadNecesaria;
    public Time horaInicioDB;
    public double horaInicio;
    public int duracion; // 1 -> 50m, 2 -> 1:15, 3 -> 1:40
    public String facultad; // a que facultad pertenece la clase
    public String carrera;
    public int semestre;
    public Salon salon = null;

    public Clase(String codigo, String nombre, int dias, int capacidadNecesaria, String horaInicioDB, double horaInicio, int duracion, String facultad, String carrera, int semestre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidadNecesaria = capacidadNecesaria;
        this.duracion = duracion;
        this.horaInicioDB = parseTimeString(horaInicioDB);
        this.horaInicio = horaInicio;
        this.dias = dias;
        this.facultad = facultad;
        this.carrera = carrera;
        this.semestre = semestre;
    }


    private Time parseTimeString(String timeString) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            return new Time(format.parse(timeString).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // or handle error appropriately
        }
    }

    @Override
    public String toString()
    {
        return "Una clase con codigo " + codigo +" nombre " + nombre + " capacidad " + capacidadNecesaria + " duracion "+ duracion + " hora inicio "+ horaInicioDB +" "+horaInicio + " dias "+dias+ " facultad "+ facultad;
    }
}