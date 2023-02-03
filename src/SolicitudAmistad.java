
import org.json.JSONObject;

/**
 * Clase que sirve para guardar las peticiones de amistad pendientes de los
 * jugadores
 *
 * @author Cristobal Jose Velo Huerta
 */
public class SolicitudAmistad {

    private int ID_JugadorSolicitante;
    private int ID_JugadorSolicitado;

    public SolicitudAmistad(int j1, int j2) {

        ID_JugadorSolicitante = j1;
        ID_JugadorSolicitado = j2;

    }

    public JSONObject toJSONObject() {

        JSONObject jsonamistad = new JSONObject();

        jsonamistad.put("JugadorSolicitante", this.ID_JugadorSolicitante);
        jsonamistad.put("JugadorSolicitado", this.ID_JugadorSolicitado);

        return jsonamistad;
    }

    public int getID_JugadorSolicitante() {
        return ID_JugadorSolicitante;
    }

    public int getID_JugadorSolicitado() {
        return ID_JugadorSolicitado;
    }

    public void setID_JugadorSolicitante(int ID_JugadorSolicitante) {
        this.ID_JugadorSolicitante = ID_JugadorSolicitante;
    }

    public void setID_JugadorSolicitado(int ID_JugadorSolicitado) {
        this.ID_JugadorSolicitado = ID_JugadorSolicitado;
    }

}
