
import org.json.JSONObject;

/**
 * Clase para modelar los administradores de la aplicacion.
 *
 * @author Cristobal Jose Velo Huerta
 */
public class Administrador {

    protected String Nombre_Administrador;
    protected String Password_Administrador;

    public Administrador(String N, String P) {
        Nombre_Administrador = N;
        Password_Administrador = P;
    }

    @Override
    public String toString() {
        String datosadmin = new String();
        datosadmin += "Nombre: " + this.Nombre_Administrador + " ";
        datosadmin += "Pasword: " + this.Password_Administrador + " ";

        return datosadmin;
    }

    /**
     *
     * @return jsonadmin JSONObject con la informacion del administrador
     */
    public JSONObject toJSONObject() {

        JSONObject jsonadmin = new JSONObject();

        jsonadmin.put("Nombre", this.Nombre_Administrador);
        jsonadmin.put("Password", this.Password_Administrador);

        return jsonadmin;
    }

    //getters
    String getNombre_Administrador() {
        return this.Nombre_Administrador;
    }

    String getPassword_Administrador() {
        return this.Password_Administrador;
    }

    //setters
    void setNombre_Administrador(String n) {
        this.Nombre_Administrador = n;
    }

    void setPassword_Administrador(String p) {
        this.Password_Administrador = p;
    }
}
