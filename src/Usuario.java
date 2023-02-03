
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase base para los diferentes tipos de usuario que pueda tener la
 * aplicacion.
 *
 * @author Cristobal Jose Velo Huerta
 */
public class Usuario implements Serializable {

    private static AtomicInteger count = new AtomicInteger(0);
    protected int Usuario_ID;
    protected String Nombre_Usuario;
    protected String Password_Usuario;

    public Usuario(String N, String P) {
        Usuario_ID = count.incrementAndGet();
        Nombre_Usuario = N;
        Password_Usuario = P;
    }

    public Usuario(int id, String N, String P) {

        AtomicInteger temp = new AtomicInteger(id);

        if (count.get() < temp.get()) {
            count.set(id);
        }
        Usuario_ID = count.get();
        Nombre_Usuario = N;
        Password_Usuario = P;

    }

    //setters
    void setNombre_Usuario(String N) {
        Nombre_Usuario = N;
    }

    void setPassword_Usuario(String P) {
        Password_Usuario = P;
    }

    //getters
    String getNombre_Usuario() {
        return this.Nombre_Usuario;
    }

    String getPassword_Usuario() {
        return this.Password_Usuario;
    }

    int getUsuario_ID() {
        return this.Usuario_ID;
    }

}
