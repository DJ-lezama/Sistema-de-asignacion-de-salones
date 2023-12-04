public class Salon implements Comparable<Salon> {
    public String edificio = "";
    public int ID = 0;

    public int capacidad;
    public Salon(String ed,int id, int cap) // salon id is edificio + num
    {
        this.edificio = ed;
        this.ID = id;
        this.capacidad = cap;
    }

    @Override
    public String toString()
    {
        return this.edificio+this.ID;
    }

    @Override
    public int compareTo(Salon other) {
        if(this.capacidad> other.capacidad)
        {
            return -1;
        }
        else if (this.capacidad< other.capacidad) {
            return +1;
        }
        else
        {
            return 0;
        }
    }
}