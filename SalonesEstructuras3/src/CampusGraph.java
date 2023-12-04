import javax.swing.*;
import java.util.*;

public class CampusGraph {
    Map<EdificiosEnum, ArrayList<AdjDataClass>> campus = new HashMap<>();
    private static CampusGraph instance = null;
    connection dbHandler = connection.getInstance();
    public edificioDistancia[] dists = new edificioDistancia[7];

    public ArrayList<Clase> clasesAsignadas = new ArrayList<>();

    // nodos...
    private ArrayList<AdjDataClass> CN = new ArrayList<>();
    private ArrayList<AdjDataClass> IA = new ArrayList<>();
    private ArrayList<AdjDataClass> NE = new ArrayList<>();
    private ArrayList<AdjDataClass> CS = new ArrayList<>();
    private ArrayList<AdjDataClass> SL = new ArrayList<>();
    private ArrayList<AdjDataClass> HU = new ArrayList<>();
    private ArrayList<AdjDataClass> CL = new ArrayList<>();
    public static CampusGraph getInstance() {
        if (instance == null) {
            instance = new CampusGraph();
        }
        return instance;
    }
    private CampusGraph()
    {
        IA.add( new AdjDataClass(EdificiosEnum.CN,42));
        IA.add( new AdjDataClass(EdificiosEnum.CL,77));

        CL.add( new AdjDataClass(EdificiosEnum.IA,77));
        CL.add( new AdjDataClass(EdificiosEnum.HU,215));
        CL.add( new AdjDataClass(EdificiosEnum.CN,130));

        CN.add( new AdjDataClass(EdificiosEnum.IA,42));
        CN.add( new AdjDataClass(EdificiosEnum.CL,130));
        CN.add( new AdjDataClass(EdificiosEnum.HU,216));
        CN.add( new AdjDataClass(EdificiosEnum.CS,210));
        CN.add( new AdjDataClass(EdificiosEnum.SL,510));

        NE.add( new AdjDataClass(EdificiosEnum.CS,45));

        SL.add( new AdjDataClass(EdificiosEnum.CS,310));
        SL.add( new AdjDataClass(EdificiosEnum.CN,510));
        SL.add( new AdjDataClass(EdificiosEnum.HU,645));

        CS.add( new AdjDataClass(EdificiosEnum.NE,45));
        CS.add( new AdjDataClass(EdificiosEnum.SL,310));
        CS.add( new AdjDataClass(EdificiosEnum.CS,345));
        CS.add( new AdjDataClass(EdificiosEnum.CN,210));

        HU.add( new AdjDataClass(EdificiosEnum.CL,215));
        HU.add( new AdjDataClass(EdificiosEnum.CN,216));
        HU.add( new AdjDataClass(EdificiosEnum.CS,345));
        HU.add( new AdjDataClass(EdificiosEnum.SL,645));

        campus.put(EdificiosEnum.IA,IA);
        campus.put(EdificiosEnum.CL,CL);
        campus.put(EdificiosEnum.CN,CN);
        campus.put(EdificiosEnum.NE,NE);
        campus.put(EdificiosEnum.SL,SL);
        campus.put(EdificiosEnum.CS,CS);
        campus.put(EdificiosEnum.HU,HU);

        //Horarios
        Horarios horarioIA  = new Horarios();
        Horarios horarioCL = new Horarios();
        Horarios horarioCN = new Horarios();
        Horarios horarioNE = new Horarios();
        Horarios horarioSL = new Horarios();
        Horarios horarioCS = new Horarios();
        Horarios horarioHU = new Horarios();

        //Edificios

        Edificio IA = new Edificio("IA",10,horarioIA);
        Edificio CL = new Edificio("CL",10,horarioCL);
        Edificio CN = new Edificio("CN",10,horarioCN);
        Edificio NE = new Edificio("NE",10,horarioNE);
        Edificio SL = new Edificio("SL",10,horarioSL);
        Edificio CS = new Edificio("CS",10,horarioCS);
        Edificio HU = new Edificio("HU",10,horarioHU);

        dists[0] = new edificioDistancia(IA,0);
        dists[1] = new edificioDistancia(CL,0);
        dists[2] = new edificioDistancia(CN,0);
        dists[3] = new edificioDistancia(NE,0);
        dists[4] = new edificioDistancia(SL,0);
        dists[5] = new edificioDistancia(CS,0);
        dists[6] = new edificioDistancia(HU,0);

        if (IA == null || CL == null || CN == null || NE == null || SL == null || CS == null || HU == null) {
            throw new IllegalStateException("Error de inicialización: Las listas de adyacencia no pueden ser nulas.");
        }
    }

    public Boolean AssingClass(Clase clase)
    {
        edificioDistancia[] dist = dijkstra(Facultad2Edificio(clase.facultad));
        Arrays.sort(dist);

        for (edificioDistancia pair:dist) {
            if (pair.edificio.addClass(clase))
            {
                System.out.println("Clase agregada en " + pair.edificio.nombre);
                System.out.println("En el salon " + clase.salon);
                //Agregar a base de datos ya con el salon asignado

                String message = "Clase agregada en " + pair.edificio.nombre;
                JOptionPane.showMessageDialog(null, message);
                clasesAsignadas.add(clase);
                return true;
            }
        }
        System.out.println("Clase no se pudo agregar, code: " + clase.codigo + " hora inicio " + clase.horaInicio +"  duracion: " + clase.duracion + " facultad " + clase.facultad );
        return false;
    }


    private edificioDistancia[] dijkstra(String startNode) {

        int start = getInicial(startNode);

        if (start == -1) {
            throw new IllegalArgumentException("Error: El nombre del edificio proporcionado no es válido.");
        }

        boolean[] visited = new boolean[campus.size()];

        for(int i = 0; i<campus.size(); i++)
        {
            dists[i].dist = Integer.MAX_VALUE;
            visited[i] = false;
        }
        dists[start].dist = 0; // initial node dist is always 0


        for (int i = 0; i < campus.size() - 1; i++)
        {

            // find the vertex with the minimum distance from the starting vertex
            int minDist = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < campus.size(); j++)
            {

                // if not visited and weight is not infinity...
                if (!visited[j] && dists[j].dist < minDist)
                {
                    // this is new min distance from 0 to this node.
                    minDist = dists[j].dist;
                    minIndex = j;
                }
            }

            // mark the vertex as visited
            visited[minIndex] = true;

            // update the distances of all adjacent vertices
            for (AdjDataClass edge:campus.get(EdificiosEnum.Ed2Int(minIndex))) {
                int neighbor = edge.edificio.ordinal();
                int weight = edge.distancia;
                int newDist = dists[minIndex].dist + weight;
                if (newDist < dists[neighbor].dist)
                {
                    dists[neighbor].dist = newDist; // new distance is no longer infinity
                }
            }
        }
        return dists;
    }



    private int String2Node(String ed)
    {
        switch (ed)
        {
            case "IA":
                return 0;
            case "CL":
                return 1;
            case "CN":
                return 2;
            case "NE":
                return 3;
            case "SL":
                return 4;
            case "CS":
                return 5;
            case "HU":
                return 6;
            default:
                return -1;

        }
    }

    public Boolean EliminarClase(Clase clase)
    {
        int ed = getInicial(clase.edificio);

        if (ed == -1) {
            System.out.println("Error: Edificio no encontrado.");
            return false;
        }

        dists[ed].edificio.EliminarClase(clase);

        clase.edificio = null;

        if(clasesAsignadas.remove(clase)) {
            System.out.println("Clase eliminada");
            return true;
        } else {
            System.out.println("Error (eliminacion de clase)");
            return false;
        }
    }

    public void PrintHorario(String edificio)
    {
        int ed = getInicial(edificio);

        dists[ed].edificio.PrintHorario();
    }

    public Boolean InscribirAlumnos(String claseID) {
        int index = findClass(claseID);
        if (index >= 0) {
            Clase clase = clasesAsignadas.get(index);
            if (clase.capacidadNecesaria > 0) {
                clase.capacidadNecesaria--; // Reducir la capacidad
                clasesAsignadas.set(index, clase); // Actualizar el objeto en la lista

                // Actualizar la capacidad en la base de datos
                dbHandler.updateCapacidad(clase.codigo, clase.capacidadNecesaria);

                System.out.println("Alumno inscrito en la clase: " + claseID);
                return true;
            } else {
                System.out.println("No quedan lugares en la clase: " + claseID);
                return false;
            }
        } else {
            System.out.println("Clase no encontrada: " + claseID);
            return false;
        }
    }



    public int findClass(String claseID)//encuentra clase con codigo "IA102"...
    {
        for (int i = 0; i < clasesAsignadas.size(); i++ ) {
            Clase clase = clasesAsignadas.get(i);
            //System.out.println("Comparando: " + clase.codigo + " con " + claseID);
            if (Objects.equals(clase.codigo, claseID)) {
                return i;
            }
        }
        return -1;
    }

    public void PrintSalones(String edificio) {
        int ed = getInicial(edificio);

        dists[ed].edificio.PrintSalones();
    }

    private String Facultad2Edificio(String fac)
    {
        switch (fac)
        {
            case "Ingeniería":
                return "IA";
            case "Ciencias":
                return "CN";
            case "Humanidades":
                return "HU";
            case "Ciencias Sociales":
                return "CS";
            case "Salud":
                return "SL";
            case "Negocios":
                return "NE";
            default:
                throw new IllegalArgumentException("Error: Nombre de facultad no reconocido - " + fac);
        }
    }

    private class edificioDistancia implements  Comparable<edificioDistancia>{
        public Edificio edificio;
        public int dist;

        public edificioDistancia(Edificio edificio,int dist)
        {
            this.dist = dist;
            this.edificio = edificio;
        }

        @Override
        public int compareTo(edificioDistancia o) {
            return Integer.compare(this.dist,o.dist);
        }
    }

    private int getInicial(String startNode)
    {
        for (int i = 0; i < dists.length; i++) {
            if (dists[i].edificio.nombre.equals(startNode)) {
                return i;
            }
        }
        return -1;
    }

    public Boolean LoadDataBaseData(ArrayList<String[]> dbClasses)
    {
        for (String[] dbClass : dbClasses) {
            String codigo = dbClass[0];
            System.out.println(codigo);
            String nomb = dbClass[1];
            System.out.println(nomb);
            int dias = Integer.parseInt(dbClass[2]);
            System.out.println(dias);
            int cap = Integer.parseInt(dbClass[3]);
            System.out.println(cap);
            Double horaInicio = Date2Double.Convert(dbClass[4]);
            System.out.println(horaInicio);
            int duracion = Integer.parseInt(dbClass[5]);
            String salon = dbClass[6];
            String edificio = dbClass[7];
            String escuela = dbClass[8];
            String carrera = dbClass[9];
            int semestre = Integer.parseInt(dbClass[10]) ;

            int index = this.getInicial(edificio);
            if (index == -1){
                System.out.println("Edificio invalido " + edificio);
            }
            int classid = dists[index].edificio.getClassCode(salon);
            Salon sal = new Salon(edificio,classid, cap);

            clasesAsignadas.add(new Clase(codigo,nomb,dias,cap,dbClass[4],horaInicio,duracion,escuela,carrera,semestre));
            clasesAsignadas.get(clasesAsignadas.size()-1).salon = sal;
            System.out.println("Clase agregada con éxito");
            System.out.println(clasesAsignadas.get(clasesAsignadas.size()-1));
            System.out.println(clasesAsignadas.get(clasesAsignadas.size()-1).salon);

            if (!dists[index].edificio.LoadDB(dias, duracion, cap, horaInicio, salon)) {
                System.out.println("No se ha podido agregar clase desde la base de datos");
                return false;
            }
        }
        return true;
    }
}


