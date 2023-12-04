
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Horarios {

    private ArrayList<Salon>[][] horarios = new ArrayList[29][5]; // 5 dias, 30 posibles hroas (dividir 7-22 en intervalos de 30m)
    public Horarios()
    {
        for(int i = 0; i< 5;i++)
        {
            for(int j = 0; j<29; j++)
            {
                horarios[j][i] = new ArrayList<>();
            }
        }
    }

    // recibe un salon y checa si esta disponible en el horario especificado...
    // dia: 0 -> lunes y miercoles, 1 martes y jeves, 2  viernes y sabado
    // recibe un salon y checa si esta disponible en el horario especificado...
    // dia: 0 -> lunes y miercoles, 1 martes y jeves, 2  viernes y sabado
    public boolean AssignHorario(Clase clase, int dia, double hora, int duracion, int salonID,String ed)
    {

        int horaInt = HoraFromDouble2Int(hora);
        duracion = RoundUp(duracion); // las clases deben acabar a las en punto o y media
        System.out.println("hora y dur " + horaInt + " " + duracion);
        if( Available(dia,horaInt,duracion,salonID))
        {
            for ( int i = horaInt; i <= horaInt + duracion; i++)
            {
                horarios[i][dia].add(new Salon(ed,salonID,clase.capacidadNecesaria));
            }
            System.out.println("agregando horario...");
            return true;
        }
        System.out.println("Horario ocupado...");
        return false;
    }

    public boolean Available(int dia, int hora, int duracion,int classID)
    {
        System.out.println("available");
        System.out.println(hora);
        System.out.println(duracion);

        for ( int i = hora; i <= hora + duracion; i++)
        {
            if(isInList(horarios[i][dia],classID))
            {
                return false;
            }
        }
        return true;
    }

    // entrada "7.5 -> sal 1" (1 = 7:30, 2 = 8:00 ...)
    private int HoraFromDouble2Int(double num)
    {
        int result = (int)(2*num - 14);
        if(result<0)
        {
            return 9999;
        }
        else{
            return result;
        }

    }

    public void EliminarClase(Clase clase)
    {
        int dias = clase.dias;
        double inicio = clase.horaInicio;
        int horaInicio = HoraFromDouble2Int(inicio);
        int duracion = RoundUp(clase.duracion);
        for(int i = horaInicio; i <= horaInicio+ duracion; i++)
        {
            for(int j = 0; j <= this.horarios[i][dias].size(); j++)
            {
                if (Objects.equals(this.horarios[i][dias].get(j).toString(), clase.salon.toString()))
                {
                    this.horarios[i][dias].remove(j);
                }
            }
        }
    }

    // entrada: duracion -> 0 = 50m, 1 -> 1:15, 2 -> 1:40
    private int RoundUp(int num)
    {
        int sal=0;
        switch (num)
        {
            case 0: // 50M
                sal = 2;
                break;
            case 1: // 1:15
                sal = 3;
                break;
            case 2: //1:40
                sal = 4;
                break;
            default:
                System.out.println("duracion out of range");
                break;
        }
        return sal;
    }

    //Checa si el salon esta en la lista de horarios
    private boolean isInList(ArrayList<Salon> salones, int ID)
    {
        for (Salon salon: salones) {
            if(salon.ID == ID)
            {
                return true;
            }
        }
        return false;
    }

    public void PrintTable()
    {
        for(int j = 0; j<29; j++)
        {
            System.out.print(int2Double(j)+"    "); // muestra la hora
            for(int i = 0; i< 5;i++) {
                System.out.print(horarios[j][i].toString());
                System.out.print("");
            }
            System.out.println("");
        }
    }

    public Boolean LoadDB(int dias, int duracion,double inicio,String salonID, Salon salon)
    {
        int horaInicio = HoraFromDouble2Int(inicio);
        duracion = RoundUp(duracion);

        if( Available(dias,horaInicio,duracion,salon.ID))
        {
            for(int i = horaInicio; i <= horaInicio+ duracion; i++)
            {
                this.horarios[i][dias].add(salon);
            }
            return true;
        }
        System.out.println("Horario ya ocupado");
        return false;
    }

    private double int2Double(int horaInt)//show the hour
    {
        double num = (double)horaInt;
        return (num+14)/2;
    }
}

