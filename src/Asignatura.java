
import org.json.JSONObject;

/**
 * Clase que modela una asignatura
 *
 * @author Cristobal Jose Velo Huerta
 */
public class Asignatura {

    //private static final AtomicInteger count = new AtomicInteger(0);
    private int ID_Asignatura;
    private String Nombre_Asignatura;
    private String Siglas_Asignatura;
    private int Curso_Asignatura;
    private int Semestre_Asignatura;
    private String Grado_Asignatura;
    private int Dificultad_Asignatura; //1,2,3

    /*
   public Asignatura(String N,int c,String G, int D){
       ID_Asignatura = count.incrementAndGet();
       Nombre_Asignatura = N;
       Curso_Asignatura = c;
       Grado_Asignatura = G;
       Dificultad_Asignatura = D;
   }
     */
    public Asignatura(int id, String N, String S, int c, int s, String G, int d) {
        ID_Asignatura = id;
        Nombre_Asignatura = N;
        Siglas_Asignatura = S;
        Curso_Asignatura = c;
        Semestre_Asignatura = s;
        Grado_Asignatura = G;
        Dificultad_Asignatura = d;

    }

    @Override
    public String toString() {
        String datosasignatura = new String();
        datosasignatura += "Identificador: " + ID_Asignatura + " ";
        datosasignatura += "Nombre: " + Nombre_Asignatura + " ";
        datosasignatura += "Siglas: " + Siglas_Asignatura + " ";
        datosasignatura += "Curso: " + Curso_Asignatura + " ";
        datosasignatura += "Semestre: " + Semestre_Asignatura + " ";
        datosasignatura += "Grado: " + Grado_Asignatura + " ";
        datosasignatura += "Dificultad: " + Dificultad_Asignatura + " ";

        return datosasignatura;

    }

    public JSONObject toJSONObject() {
        JSONObject jsonasig = new JSONObject();
        jsonasig.put("Identificador", ID_Asignatura);
        jsonasig.put("Nombre", Nombre_Asignatura);
        jsonasig.put("Siglas", Siglas_Asignatura);
        jsonasig.put("Curso", Curso_Asignatura);
        jsonasig.put("Semestre", Semestre_Asignatura);
        jsonasig.put("Grado", Grado_Asignatura);
        jsonasig.put("Dificultad", Dificultad_Asignatura);

        return jsonasig;
    }

    //Setters
    public void setID_Asignatura(int ID_Asignatura) {
        this.ID_Asignatura = ID_Asignatura;
    }

    public void setNombre_Asignatura(String Nombre_Asignatura) {
        this.Nombre_Asignatura = Nombre_Asignatura;
    }

    public void setSiglas_Asignatura(String Siglas_Asignatura) {
        this.Siglas_Asignatura = Siglas_Asignatura;
    }

    public void setCurso_Asignatura(int Curso_Asignatura) {
        this.Curso_Asignatura = Curso_Asignatura;
    }

    public void setSemestre_Asignatura(int Semestre_Asignatura) {
        this.Semestre_Asignatura = Semestre_Asignatura;
    }

    public void setGrado_Asignatura(String Grado_Asignatura) {
        this.Grado_Asignatura = Grado_Asignatura;
    }

    public void setDificultad_Asignatura(int Dificultad_Asignatura) {
        this.Dificultad_Asignatura = Dificultad_Asignatura;
    }

    //getters
    public int getID_Asignatura() {
        return ID_Asignatura;
    }

    public String getNombre_Asignatura() {
        return Nombre_Asignatura;
    }

    public String getSiglas_Asignatura() {
        return Siglas_Asignatura;
    }

    public int getCurso_Asignatura() {
        return Curso_Asignatura;
    }

    public int getSemestre_Asignatura() {
        return Semestre_Asignatura;
    }

    public String getGrado_Asignatura() {
        return Grado_Asignatura;
    }

    public int getDificultad_Asignatura() {
        return Dificultad_Asignatura;
    }
}
