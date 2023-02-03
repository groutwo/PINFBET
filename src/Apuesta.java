
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

/**
 * Clase que modela los datos que debe tener una apuesta del sistema
 *
 * @author Cristobal Jose Velo Huerta
 */
public class Apuesta {

    private static AtomicInteger count = new AtomicInteger(0);

    private int ID_Apuesta;
    private int ID_Jugador;
    private int ID_Asignatura;
    private int Convocatoria; // 1 = diciembre , 2= febrero, 3= junio , 4=septiembre, 0= paracualquiera De momento no se usa para nada
    private boolean Abierta;
    private boolean Terminada;
    private double resultado;

    List<Participacion> ListaParticipaciones;

    //Constructor para crear una apuesta nueva
    public Apuesta(int j, int a, int c) {
        ID_Apuesta = count.incrementAndGet();
        ID_Jugador = j;
        ID_Asignatura = a;
        Convocatoria = c;
        Abierta = true;
        Terminada = false;
        ListaParticipaciones = new ArrayList<>();
        resultado = -1;
    }

    //Constructor para cargar una apuesta desde el json de Apuestas
    public Apuesta(int id, int j, int a, int c, boolean b1, boolean b2, double r, List<Participacion> lp) {

        AtomicInteger temp = new AtomicInteger(id);

        if (count.get() < temp.get()) {
            count.set(id);
        }

        ID_Apuesta = count.get();
        ID_Jugador = j;
        ID_Asignatura = a;
        Convocatoria = c;
        Abierta = b1;
        Terminada = b2;
        resultado = r;
        ListaParticipaciones = lp;
    }

    /**
     *
     * @return jsonapuesta JSONObject con los datos del objeto apuesta
     */
    public JSONObject toJSONObject() {
        JSONObject jsonapuesta = new JSONObject();

        jsonapuesta.put("Identificador", ID_Apuesta);
        jsonapuesta.put("Jugador", ID_Jugador);
        jsonapuesta.put("Asignatura", ID_Asignatura);
        jsonapuesta.put("Convocatoria", Convocatoria);
        jsonapuesta.put("Abierta", Abierta);
        jsonapuesta.put("Terminada", Terminada);
        jsonapuesta.put("Resultado", resultado);
        jsonapuesta.put("Participaciones", ListaParticipaciones);

        return jsonapuesta;
    }

    public int getID_Apuesta() {
        return ID_Apuesta;
    }

    public int getID_Jugador() {
        return ID_Jugador;
    }

    public int getID_Asignatura() {
        return ID_Asignatura;
    }

    public int getConvocatoria() {
        return Convocatoria;
    }

    public boolean isAbierta() {
        return Abierta;
    }

    public boolean isTerminada() {
        return Terminada;
    }

    public List<Participacion> getListaParticipaciones() {
        return ListaParticipaciones;
    }

    public void setID_Apuesta(int ID_Apuesta) {
        this.ID_Apuesta = ID_Apuesta;
    }

    public void setID_Jugador(int ID_Jugador) {
        this.ID_Jugador = ID_Jugador;
    }

    public void setID_Asignatura(int ID_Asignatura) {
        this.ID_Asignatura = ID_Asignatura;
    }

    public void setConvocatoria(int Convocatoria) {
        this.Convocatoria = Convocatoria;
    }

    public void setAbierta(boolean Abierta) {
        this.Abierta = Abierta;
    }

    public void setTerminada(boolean Terminada) {
        this.Terminada = Terminada;
    }

    public void setListaParticipaciones(List<Participacion> ListaParticipaciones) {
        this.ListaParticipaciones = ListaParticipaciones;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getResultado() {
        return resultado;
    }

    /**
     *
     * @return max valor de la mayor participacion en la apuesta
     */
    public int mayorparticipacion() {

        int max = 0;
        for (int i = 0; i < ListaParticipaciones.size(); ++i) {
            if (ListaParticipaciones.get(i).getCantidad_apostada() > max) {
                max = ListaParticipaciones.get(i).getCantidad_apostada();
            }
        }

        return max;
    }

}
