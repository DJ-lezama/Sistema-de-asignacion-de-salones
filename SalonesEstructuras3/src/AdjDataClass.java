public class AdjDataClass {

    EdificiosEnum edificio;
    int distancia;

    public AdjDataClass(EdificiosEnum ed, int weight) {
        this.edificio = ed;
        this.distancia = weight;
    }

    public static int getDistance(AdjDataClass node)
    {
        return node.distancia;
    }
}