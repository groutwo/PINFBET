
import java.util.ArrayList;
import java.util.List;
import org.json.*;

/**
 * Clase que modela los datos que tiene cada usuario jugador de la aplicacion
 * Hereda de la clase Usuario.
 *
 * @author Cristobal Jose Velo Huerta
 */
public class Jugador extends Usuario {

    private int Saldo_Jugador;
    private int Saldo_Expediente;
    private List<Integer> friendlist; //Lista de identificadores de sus amigos
    private List<Integer> subjectlist;//Lista de identificadores de las asignaturas que cursa
    private List<ApuestaParticipacion> misparticipaciones; //Lista de tuplas de id de apuesta e id de participaciones del jugador en dichas apuestas
    private List<Integer> misapuestas; //Lista de apuestas sobre este jugador en la que pueden participar otros jugadores

    //Para jugadores nuevos
    public Jugador(String N, String P, int C) {
        super(N, P);
        Saldo_Jugador = C;
        Saldo_Expediente = 0;
        friendlist = new ArrayList<Integer>();
        subjectlist = new ArrayList<Integer>();
        misparticipaciones = new ArrayList<>();
        misapuestas = new ArrayList<>();
    }

    //Para jugadores cargados del json
    public Jugador(int id, String N, String P, int C, int Cex, List<Integer> f, List<Integer> a, List<Integer> la, List<ApuestaParticipacion> lap) {
        super(id, N, P);
        Saldo_Jugador = C;
        Saldo_Expediente = Cex;
        friendlist = f;
        subjectlist = a;
        misapuestas = la;
        misparticipaciones = lap;
    }

    @Override
    public String toString() {

        String datosjugador = new String();
        datosjugador += "Identificador: " + this.getUsuario_ID() + " ";
        datosjugador += "Nombre: " + this.getNombre_Usuario() + " ";
        datosjugador += "Pasword: " + this.getPassword_Usuario() + " ";
        datosjugador += "Saldo: " + this.getSaldo_Jugador() + " ";

        return datosjugador;
    }

    public JSONObject toJSONObject() {

        JSONObject jsonjugador = new JSONObject();

        jsonjugador.put("Identificador", Usuario_ID);
        jsonjugador.put("Nombre", Nombre_Usuario);
        jsonjugador.put("Password", Password_Usuario);
        jsonjugador.put("Saldo", Saldo_Jugador);
        jsonjugador.put("SaldoExpediente", Saldo_Expediente);
        jsonjugador.put("Amigos", friendlist);
        jsonjugador.put("Asignaturas", subjectlist);
        jsonjugador.put("Participaciones", misparticipaciones);
        jsonjugador.put("Apuestas", misapuestas);

        return jsonjugador;
    }

    /**
     * Anade la diferencia entre el saldo obtenido anteriormente y el obtenido
     * ahora del expediente. Si no hay diferencia o esta es negativa no se
     * anaden nuevas monedas.
     *
     * @param monedasnuevas nuevo saldo que se obtiene del expediente del
     * jugador
     */
    public void ActualizarExpediente(int monedasnuevas) {

        if (monedasnuevas > this.getSaldo_Expediente()) {
            int diferencia = monedasnuevas - Saldo_Expediente;
            this.setSaldo_Expediente(monedasnuevas);
            this.setSaldo_Jugador(Saldo_Jugador + diferencia);
            System.out.println("Has obtenido " + diferencia + " PINFCoins nuevos");
        } else {
            System.out.println("No has obtenido nuevos PINFCoins.");
        }

    }

    /**
     * Se introduce el identificador de la asignatura en la lista subjectlist
     * del objeto Jugador si este identificador no esta ya incluido en la lista.
     *
     * @param asignatura identificador de la asignatura
     */
    public void AddAsignatura(int asignatura) {

        if (subjectlist.contains(asignatura) == false) {
            subjectlist.add(asignatura);
        }
    }

    public void setSaldo_Jugador(int Saldo_Jugador) {
        this.Saldo_Jugador = Saldo_Jugador;
    }

    public void setSaldo_Expediente(int Saldo_Expediente) {
        this.Saldo_Expediente = Saldo_Expediente;
    }

    public void setFriendlist(List<Integer> friendlist) {
        this.friendlist = friendlist;
    }

    public void setSubjectlist(List<Integer> subjectlist) {
        this.subjectlist = subjectlist;
    }

    public void setMisparticipaciones(List<ApuestaParticipacion> misparticipaciones) {
        this.misparticipaciones = misparticipaciones;
    }

    public void setMisapuestas(List<Integer> misapuestas) {
        this.misapuestas = misapuestas;
    }

    public int getSaldo_Jugador() {
        return Saldo_Jugador;
    }

    public int getSaldo_Expediente() {
        return Saldo_Expediente;
    }

    public List<Integer> getFriendlist() {
        return friendlist;
    }

    public List<Integer> getSubjectlist() {
        return subjectlist;
    }

    public List<ApuestaParticipacion> getMisparticipaciones() {
        return misparticipaciones;
    }

    public List<Integer> getMisapuestas() {
        return misapuestas;
    }

    public boolean esamigode(Jugador j) {

        return friendlist.contains(j.getUsuario_ID()) && j.getFriendlist().contains(this.getUsuario_ID());

    }

    public void addSaldo(double s) {
        Saldo_Jugador += s;
    }

}
