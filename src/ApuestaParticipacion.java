
import org.json.JSONObject;

/**
 * Clase con la tupla de identificador de una apuesta y una participacion para
 * facilitar su guardado en objetos de la clase Jugador
 *
 * @author Cristobal Jose Velo Huerta
 */
public class ApuestaParticipacion {

    private int ID_Apuesta;
    private int ID_Participacion;

    public ApuestaParticipacion(int a, int p) {

        ID_Apuesta = a;
        ID_Participacion = p;

    }

    public int getID_Apuesta() {
        return ID_Apuesta;
    }

    public int getID_Participacion() {
        return ID_Participacion;
    }

    public void setID_Apuesta(int ID_Apuesta) {
        this.ID_Apuesta = ID_Apuesta;
    }

    public void setID_Participacion(int ID_Participacion) {
        this.ID_Participacion = ID_Participacion;
    }

    public JSONObject toJSONObject() {

        JSONObject jsonAP = new JSONObject();

        jsonAP.put("Apuesta", ID_Apuesta);
        jsonAP.put("Participacion", ID_Participacion);

        return jsonAP;
    }

}
