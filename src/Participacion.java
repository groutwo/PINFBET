
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que modela los datos que debe tener una participacion de una apuesta.
 *
 * @author Cristobal Jose Velo Huerta
 */
public class Participacion {

    private static AtomicInteger count = new AtomicInteger(0);

    private int ID_Participacion;
    private int ID_Apostador;
    private int cantidad_apostada;
    private double rangoinferior;
    private double rangosuperior;

    public void setID_Participacion(int ID_Participacion) {
        this.ID_Participacion = ID_Participacion;
    }

    public void setID_Apostador(int ID_Apostador) {
        this.ID_Apostador = ID_Apostador;
    }

    public void setCantidad_apostada(int cantidad_apostada) {
        this.cantidad_apostada = cantidad_apostada;
    }

    public void setRangoinferior(double rangoinferior) {
        this.rangoinferior = rangoinferior;
    }

    public void setRangosuperior(double rangosuperior) {
        this.rangosuperior = rangosuperior;
    }

    public int getID_Participacion() {
        return ID_Participacion;
    }

    public int getID_Apostador() {
        return ID_Apostador;
    }

    public int getCantidad_apostada() {
        return cantidad_apostada;
    }

    public double getRangoinferior() {
        return rangoinferior;
    }

    public double getRangosuperior() {
        return rangosuperior;
    }

    //Constructor para nuevas participaciones
    public Participacion(int c, int d, double ri, double rs) {
        ID_Participacion = count.incrementAndGet();
        ID_Apostador = c;
        cantidad_apostada = d;
        rangoinferior = ri;
        rangosuperior = rs;
    }

    //Constructor para participaciones cargadas desde un .json
    public Participacion(int a, int c, int d, double ri, double rs) {
        AtomicInteger temp = new AtomicInteger(a);

        if (count.get() < temp.get()) {
            count.set(a);
        }

        ID_Participacion = count.get();
        ID_Apostador = c;
        cantidad_apostada = d;
        rangoinferior = ri;
        rangosuperior = rs;
    }

}
