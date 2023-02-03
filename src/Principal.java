
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

/**
 * Clase que modela el flujo de la aplicacion y cuenta con Listas para guardar
 * la informacion
 *
 * @author Cristobal Jose Velo Huerta
 */
public class Principal {

    static List<Jugador> Jugadores = new ArrayList<Jugador>();
    static List<Administrador> Administradores = new ArrayList<Administrador>();
    static List<Asignatura> Asignaturas = new ArrayList<Asignatura>();
    static List<Apuesta> Apuestas = new ArrayList<>();
    //static List<Participacion> Participaciones = new ArrayList<>();
    static List<SolicitudAmistad> SolicitudesdeAmistad = new ArrayList<>();

    //----------------
    public static byte[] computeHash(String x) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return d.digest();
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }
    //----------------

    /**
     * Programa principal
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        //Cargar datos guardados al iniciar
        cargardatos();

        menuinicio();

        //Guardar datos en los archivos
        guardardatos();

    }

    /**
     * LLama a todos los metodos que cargan informacion en el sistema desde
     * archivos .json
     *
     * @throws IOException
     */
    static void cargardatos() throws IOException {

        cargarAdministradores();
        cargarJugadores();
        cargarAsignaturas();
        cargarApuestas();
        cargarSolicitudesdeAmistad();

    }

    /**
     * LLama a todos los metodos que guardan informacion del sistema en archivos
     * .json
     *
     * @throws IOException
     */
    static void guardardatos() throws IOException {

        guardarAdministradores();
        guardarJugadores();
        guardarAsignaturas();
        guardarApuestas();
        guardarSolicitudesdeAmistad();

    }

    /**
     * Devuelve una List<Integer> equivalente al JSONArray del JSONObject json
     * con el nombre "etiqueta"
     *
     * @param json objeto json con el JSONArray de int que nos interesa
     * @param etiqueta etiqueta identificativa del JSONArray
     * @return List<Integer> templist
     */
    static List<Integer> fromJSONArraytoArrayList(JSONObject json, String etiqueta) {

        List<Integer> templist = new ArrayList<Integer>();

        JSONArray temp_jsonarray = json.getJSONArray(etiqueta);

        for (int k = 0; k < temp_jsonarray.length(); ++k) {
            templist.add(temp_jsonarray.getInt(k));
        }

        return templist;

    }

    static List<ApuestaParticipacion> fromJSONArraytoArryListApuestaParticipacion(JSONObject json, String etiqueta) {
        List<ApuestaParticipacion> templist = new ArrayList<ApuestaParticipacion>();

        JSONArray temp_jsonarray = json.getJSONArray("Participaciones");
        for (int k = 0; k < temp_jsonarray.length(); ++k) {

            //{"ID_Participacion":1,"ID_Apuesta":1}
            JSONObject temp_jsonobject = temp_jsonarray.getJSONObject(k);
            templist.add(new ApuestaParticipacion(temp_jsonobject.getInt("ID_Apuesta"), temp_jsonobject.getInt("ID_Participacion")));
        }

        return templist;
    }

    /**
     * Carga en la lista Jugadores los datos guardados en Jugadores.json Si
     * Jugadores.json no existe se crea.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    static void cargarJugadores() throws FileNotFoundException, IOException {

        File file = new File("Jugadores.json");
        //Si el archivo ya existe y contiene informacion entonces se entra en el if.
        if (!file.createNewFile() && file.length() != 0) {
            Scanner S = new Scanner(file);
            String s_jsonjugadores = S.nextLine();
            S.close();

            JSONArray ja_Jugadores = new JSONArray(s_jsonjugadores);

            for (int i = 0; i < ja_Jugadores.length(); ++i) {

                JSONObject json = ja_Jugadores.getJSONObject(i);
                int j_id = json.getInt("Identificador");
                String j_nombre = json.getString("Nombre");
                String j_pass = json.getString("Password");
                int j_saldo = json.getInt("Saldo");
                int j_saldoexp = json.getInt("SaldoExpediente");

                List<Integer> j_friends = fromJSONArraytoArrayList(json, "Amigos");
                List<Integer> j_asignaturas = fromJSONArraytoArrayList(json, "Asignaturas");
                List<Integer> j_apuestas = fromJSONArraytoArrayList(json, "Apuestas");

                List<ApuestaParticipacion> j_participaciones = fromJSONArraytoArryListApuestaParticipacion(json, "Participaciones");

                Jugador j = new Jugador(j_id, j_nombre, j_pass, j_saldo, j_saldoexp, j_friends, j_asignaturas, j_apuestas, j_participaciones);

                Jugadores.add(j);
            }
        }
    }

    /**
     * Se guarda en el archivo Jugadores.json la informacion contenido en la
     * lista Jugadores
     *
     * @throws IOException
     */
    static void guardarJugadores() throws IOException {

        JSONArray ja_Jugadores = new JSONArray();

        for (int i = 0; i < Jugadores.size(); ++i) {
            ja_Jugadores.put(Jugadores.get(i).toJSONObject());
        }

        File file = new File("Jugadores.json");
        if (file.exists()) {
            file.delete();
        }

        FileWriter fr = new FileWriter(file, true);

        fr.write(ja_Jugadores.toString());

        fr.close();

    }

    /**
     * Carga en la lista Administradores los datos guardados en el archivo
     * Administradores.json Si Administrador.json no existe se creara el
     * archivo. Si Administrador.json no existe o no contiene informacion se
     * pedira registrar un nuevo administrador
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    static void cargarAdministradores() throws FileNotFoundException, IOException {
        File file = new File("Administradores.json");
        //Si el archivo ya existe y contiene informacion se entra en el if
        if (!file.createNewFile() && file.length() != 0) {
            Scanner S = new Scanner(file);
            String s_jsonadmins = S.nextLine();
            S.close();

            JSONArray ja_Administradores = new JSONArray(s_jsonadmins);

            if (ja_Administradores.length() == 0) { //No hay administradores en el sistema
                System.out.println("No existen administradores en el sistema. Iniciando el registro de administrador:");
                registroadmin();

            } else {

                for (int i = 0; i < ja_Administradores.length(); ++i) {

                    JSONObject json = ja_Administradores.getJSONObject(i);

                    String a_nombre = json.getString("Nombre");
                    String a_pass = json.getString("Password");

                    Administrador a = new Administrador(a_nombre, a_pass);

                    Administradores.add(a);
                }
            }
        } else {
            System.out.println("No existen administradores en el sistema. Iniciando el registro de administrador:");
            registroadmin();
        }
    }

    /**
     * Guarda en el archivo Administradores.json la informacion contenida en la
     * lista Administradores
     *
     * @throws IOException
     */
    static void guardarAdministradores() throws IOException {
        JSONArray ja_Administradores = new JSONArray();

        for (int i = 0; i < Administradores.size(); ++i) {
            ja_Administradores.put(Administradores.get(i).toJSONObject());
        }

        File file = new File("Administradores.json");
        if (file.exists()) {
            file.delete();
        }

        FileWriter fr = new FileWriter(file, true);

        fr.write(ja_Administradores.toString());

        fr.close();
    }

    /**
     * Carga en la lista Asignaturas los datos guardados en Asignaturas.json. Si
     * Asignaturas.json no existe se crea el archivo.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    static void cargarAsignaturas() throws FileNotFoundException, IOException {
        File file = new File("Asignaturas.json");
        if (!file.createNewFile() && file.length() != 0) {
            Scanner S = new Scanner(file);
            String s_jsonjugadores = S.nextLine();
            S.close();

            JSONArray ja_Asignaturas = new JSONArray(s_jsonjugadores);

            for (int i = 0; i < ja_Asignaturas.length(); ++i) {

                JSONObject json = ja_Asignaturas.getJSONObject(i);
                int a_id = json.getInt("Identificador");
                String a_nombre = json.getString("Nombre");
                String a_siglas = json.getString("Siglas");
                int a_curso = json.getInt("Curso");
                int a_semestre = json.getInt("Semestre");
                String a_grado = json.getString("Grado");
                int a_difi = json.getInt("Dificultad");

                Asignatura a = new Asignatura(a_id, a_nombre, a_siglas, a_curso, a_semestre, a_grado, a_difi);

                Asignaturas.add(a);
            }
        }
    }

    /**
     * Guarda en el archivo Asignaturas.json la informacion guardada en la lista
     * Asignaturas
     *
     * @throws IOException
     */
    static void guardarAsignaturas() throws IOException {
        JSONArray ja_Asignaturas = new JSONArray();

        for (int i = 0; i < Asignaturas.size(); ++i) {
            ja_Asignaturas.put(Asignaturas.get(i).toJSONObject());
        }

        File file = new File("Asignaturas.json");
        if (file.exists()) {
            file.delete();
        }

        FileWriter fr = new FileWriter(file, true);

        fr.write(ja_Asignaturas.toString());

        fr.close();
    }

    /**
     * Carga las apuestas del fichero Apuestas.json en la lista Apuestas.
     *
     * @throws IOException
     */
    static void cargarApuestas() throws IOException {

        File file = new File("Apuestas.json");

        if (!file.createNewFile() && file.length() != 0) {
            Scanner S = new Scanner(file);
            String s_jsonapuestas = S.nextLine();
            S.close();

            JSONArray ja_Apuestas = new JSONArray(s_jsonapuestas);

            for (int i = 0; i < ja_Apuestas.length(); ++i) {

                JSONObject json = ja_Apuestas.getJSONObject(i);
                int a_id = json.getInt("Identificador");
                int a_jug = json.getInt("Jugador");
                int a_asig = json.getInt("Asignatura");
                int a_conv = json.getInt("Convocatoria");
                boolean a_abierta = json.getBoolean("Abierta");
                boolean a_terminada = json.getBoolean("Terminada");
                double a_resultado = json.getDouble("Resultado");

                List<Participacion> a_lp = JSONArraytoListParticipaciones(json, "Participaciones");

                Apuestas.add(new Apuesta(a_id, a_jug, a_asig, a_conv, a_abierta, a_terminada, a_resultado, a_lp));
            }
        }

    }

    /**
     * Metodo que transforma el jsonarray que contenga objetos de clase
     * Participacion a una Lista
     *
     * @param json JSONObject que contiene el JSONArray que nos interesa
     * @param etiqueta string idetenficativo del JSONArray con las
     * participaciones
     * @return templist Lista con las participaciones.
     */
    public static List<Participacion> JSONArraytoListParticipaciones(JSONObject json, String etiqueta) {
        List<Participacion> templist = new ArrayList<>();

        JSONArray temp_jsonarray = json.getJSONArray("Participaciones");
        for (int k = 0; k < temp_jsonarray.length(); ++k) {
            JSONObject temp_jsonobject = temp_jsonarray.getJSONObject(k);
            //{"rangosuperior":3.33,"rangoinferior":2.22,"cantidad_apostada":333,"ID_Participacion":1,"ID_Apostador":1}

            templist.add(new Participacion(temp_jsonobject.getInt("ID_Participacion"), temp_jsonobject.getInt("ID_Apostador"), temp_jsonobject.getInt("cantidad_apostada"), temp_jsonobject.getDouble("rangoinferior"), temp_jsonobject.getDouble("rangosuperior")));
        }
        return templist;

    }

    /**
     * Muestra por pantalla los datos de las apuestas del jugador de la posicion
     * posJugador de la lista de Jugadores y las participaciones en dichas
     * apuestas.
     *
     * @param posJugador
     *
     */
    public static void mostrarApuestassobre(int posJugador) {

        int idj = Jugadores.get(posJugador).getUsuario_ID();
        for (int i = 0; i < Apuestas.size(); ++i) {
            if (Apuestas.get(i).getID_Jugador() == idj) {
                String nom_asig = NombreAsignaturaporID(Apuestas.get(i).getID_Asignatura());
                System.out.println("Asignatura: " + nom_asig);
                System.out.println("--Participaciones--");
                for (int j = 0; j < Apuestas.get(i).getListaParticipaciones().size(); ++j) {
                    int posApostador = buscarJugadorporID(Apuestas.get(i).getListaParticipaciones().get(j).getID_Apostador());
                    System.out.println(
                            "Apostador: " + Jugadores.get(posApostador).getNombre_Usuario()
                            + " Apostado: " + Apuestas.get(i).getListaParticipaciones().get(j).getCantidad_apostada()
                            + " Nota: [" + Apuestas.get(i).getListaParticipaciones().get(j).getRangoinferior() + "," + Apuestas.get(i).getListaParticipaciones().get(j).getRangosuperior() + "]");
                }
                System.out.println("------");
            }
        }
    }

    /**
     * Muestra los datos de las participaciones en apuestas del un jugador
     *
     * @param posJugador posicion en la lista Jugadores del jugador sobre el que
     * mostramos la informacion de las participaciones
     */
    public static void mostrarParticipacionesde(int posJugador) {

        for (int i = 0; i < Apuestas.size(); ++i) {
            for (int j = 0; j < Apuestas.get(i).getListaParticipaciones().size(); ++j) {
                if (Apuestas.get(i).getListaParticipaciones().get(j).getID_Apostador() == Jugadores.get(posJugador).getUsuario_ID()) {
                    int posProtagonista = buscarJugadorporID(Apuestas.get(i).getID_Jugador());
                    System.out.println("-------");
                    System.out.println("Apuesta sobre " + Jugadores.get(posProtagonista).getNombre_Usuario() + " en la asignatura " + NombreAsignaturaporID(Apuestas.get(i).getID_Asignatura()) + ":");
                    System.out.println("Apostaste: " + Apuestas.get(i).getListaParticipaciones().get(j).getCantidad_apostada());
                    System.out.println("Nota: [" + Apuestas.get(i).getListaParticipaciones().get(j).getRangoinferior() + "," + Apuestas.get(i).getListaParticipaciones().get(j).getRangosuperior() + "]");
                    System.out.println("-------");
                }
            }
        }

    }

    /**
     * Guarda los objetos de la clase Apuesta de la lista Apuestas en el fichero
     * Apuestas.json
     */
    static void guardarApuestas() throws IOException {
        JSONArray ja_Apuestas = new JSONArray();

        for (int i = 0; i < Apuestas.size(); ++i) {
            ja_Apuestas.put(Apuestas.get(i).toJSONObject());
        }

        File file = new File("Apuestas.json");
        if (file.exists()) {
            file.delete();
        }

        FileWriter fr = new FileWriter(file, true);

        fr.write(ja_Apuestas.toString());

        fr.close();
    }

    /**
     * Carga en la lista SolicitudesdeAmistad los datos guardados en
     * SolicitudesAmistad.json. Si no existe SolicitudesAmistad.json se crea.
     *
     * @throws IOException
     */
    static void cargarSolicitudesdeAmistad() throws IOException {
        File file = new File("SolicitudesAmistad.json");

        if (!file.createNewFile() && file.length() != 0) {
            Scanner S = new Scanner(file);
            String s_jsonAmistades = S.nextLine();
            S.close();

            JSONArray ja_Amistades = new JSONArray(s_jsonAmistades);

            for (int i = 0; i < ja_Amistades.length(); ++i) {

                JSONObject json = ja_Amistades.getJSONObject(i);
                int j_id1 = json.getInt("JugadorSolicitante");
                int j_id2 = json.getInt("JugadorSolicitado");

                SolicitudAmistad sa = new SolicitudAmistad(j_id1, j_id2);

                SolicitudesdeAmistad.add(sa);
            }
        }
    }

    /**
     * Se guarda en el archivo SolicitudesAmistad la informacion contenida en la
     * lista SolicitudesdeAmistad
     *
     * @throws IOException
     */
    static void guardarSolicitudesdeAmistad() throws IOException {
        JSONArray ja_SolicitudesAmistad = new JSONArray();

        for (int i = 0; i < SolicitudesdeAmistad.size(); ++i) {
            ja_SolicitudesAmistad.put(SolicitudesdeAmistad.get(i).toJSONObject());
        }

        File file = new File("SolicitudesAmistad.json");
        if (file.exists()) {
            file.delete();
        }

        FileWriter fr = new FileWriter(file, true);

        fr.write(ja_SolicitudesAmistad.toString());

        fr.close();
    }

    /**
     * Menu que se muestra ejecutar la aplicacion. Es lo primero que se muestra
     * por pantalla a no ser que sea necesario registrar un administrador.
     */
    static void menuinicio() throws IOException {

        Scanner S = new Scanner(System.in);

        int choice;

        do {
            System.out.println("Bienvenido a PINFBET");

            System.out.println("1. Iniciar Sesion");
            System.out.println("2. Registrarse");
            System.out.println("3. Iniciar Sesion como administrador");
            System.out.println("0. Salir y cerrar aplicacion");
            System.out.print(">");

            try {
                choice = S.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, introduce de nuevo la opcion");
                choice = -1;
            }
            S.nextLine();

            switch (choice) {
                case 1: //Inicio Sesion

                    int posjugador;
                    int reintentar = 0;
                    Scanner sc = new Scanner(System.in);
                    do {
                        posjugador = iniciarsesion();

                        if (posjugador == -1) {
                            System.out.println("Credenciales introducidas incorrectas. Quiere volver a intentarlo?");
                            do {
                                System.out.println("1. Si");
                                System.out.println("2. No");
                                System.out.print(">");
                                try {
                                    reintentar = S.nextInt();
                                } catch (java.util.InputMismatchException e) {
                                    System.out.println("Por favor, introduce de nuevo la opcion");
                                    reintentar = 0;
                                }
                                S.nextLine();
                            } while (reintentar == 0);
                        }
                    } while (posjugador == -1 && reintentar == 1);

                    if (posjugador != -1) {
                        portada(posjugador);
                    }

                    break;
                case 2: //Registrarse
                    registro();
                    break;
                case 3: //Iniciarsesion Admin
                    int posadmin;
                    int reintentaradmin = 0;
                    Scanner scadmin = new Scanner(System.in);
                    do {
                        posadmin = iniciarsesionadmin();

                        if (posadmin == -1) {
                            System.out.println("Credenciales introducidas incorrectas. Quiere volver a intentarlo?");
                            do {
                                System.out.println("1. Si");
                                System.out.println("2. No");
                                System.out.print(">");
                                try {
                                    reintentaradmin = scadmin.nextInt();
                                } catch (java.util.InputMismatchException e) {
                                    System.out.println("Por favor, introduce de nuevo la opcion");
                                    reintentaradmin = 0;
                                }
                                scadmin.nextLine();
                            } while (reintentaradmin == 0);
                        }
                    } while (posadmin == -1 && reintentaradmin == 1);

                    if (posadmin != -1) {
                        portadaadmin(posadmin);
                    }
                    break;
                case 0: //salir
                default:
                    break;
            }
        } while (choice != 0);

    }

    /**
     * Metodo que permite dar de alta un nuevo jugador en el sistema. El nombre
     * que introduzca no debera estar ya en uso por otro jugador ya registrado.
     * En caso de que el nombre ya este en uso se le pedira que introduzca otro
     * nombre hasta que el introducido no este ya en uso por otro jugador.
     */
    static void registro() {

        System.out.println("Registrando nuevo jugador");
        Scanner S = new Scanner(System.in);
        java.io.StreamTokenizer Input = new java.io.StreamTokenizer(System.in);

        System.out.println("Introduzca su nombre de usuario: ");
        String nombre = S.nextLine();
        System.out.println("Introduzca su contrasena: ");
        try {
            Input.nextToken();
        } catch (Exception e) {
        }
        String password = "";
        try {
            password = byteArrayToHexString(computeHash(Input.sval));
        } catch (Exception e) {
        }
        boolean nombredisponible = true;

        //Comprobar que el nombre esta libre
        for (int i = 0; i < Jugadores.size(); ++i) {

            if (Jugadores.get(i).getNombre_Usuario().equals(nombre)) {
                nombredisponible = false;
            }

        }

        while (nombredisponible == false) {
            nombredisponible = true;
            System.out.println("El nombre introducido ya esta siendo usado por otro usuario. Introduzca uno diferente: ");
            nombre = S.nextLine();
            for (int i = 0; i < Jugadores.size(); ++i) {
                if (Jugadores.get(i).getNombre_Usuario().equals(nombre)) {
                    nombredisponible = false;
                }
            }
        }

        //El nombre elegido ya vale. Creamos el nuevo jugador y lo anadimos a la lista de jugadores
        Jugadores.add(new Jugador(nombre, password, 0));
        try {
            guardarJugadores();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Registro completado. Ahora puede iniciar sesion.");

    }

    /**
     * Metodo que permite dar de alta un nuevo administrador en el sistema. El
     * nombre que introduzca no debera estar ya en uso por otro administrador ya
     * registrado. En caso de que el nombre ya este en uso se le pedira que
     * introduzca otro nombre hasta que el introducido no este ya en uso por
     * otro administrador.
     */
    static void registroadmin() {
        System.out.println("Registrando nuevo administrador");
        Scanner S = new Scanner(System.in);
        java.io.StreamTokenizer Input = new java.io.StreamTokenizer(System.in);
        System.out.println("Introduzca su nombre de usuario: ");
        String nombre = S.nextLine();
        System.out.println("Introduzca su contrasena: ");
        try {
            Input.nextToken();
        } catch (Exception e) {
        }
        String password = "";
        try {
            password = byteArrayToHexString(computeHash(Input.sval));
        } catch (Exception e) {
        }

        boolean nombredisponible = true;

        //Comprobar que el nombre esta libre
        for (int i = 0; i < Administradores.size(); ++i) {

            if (Administradores.get(i).getNombre_Administrador().equals(nombre)) {
                nombredisponible = false;
            }

        }

        while (nombredisponible == false) {
            nombredisponible = true;
            System.out.println("El nombre introducido ya esta siendo usado por otro administrador. Introduzca uno diferente: ");
            nombre = S.nextLine();
            for (int i = 0; i < Administradores.size(); ++i) {
                if (Administradores.get(i).getNombre_Administrador().equals(nombre)) {
                    nombredisponible = false;
                }
            }
        }

        //El nombre elegido ya vale. Creamos el nuevo administrador y lo anadimos a la lista de administradores
        Administradores.add(new Administrador(nombre, password));
        try {
            guardarAdministradores();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Registro completado. Ahora puede iniciar sesion.");
    }

    /**
     * Se pide introducir el nombre y contrasena del jugador que quiere iniciar
     * sesion Si las credenciales son correctas, se devuelve la posicion del
     * jugador en la lista Jugadores. Si no son correctas se devuelve -1;
     *
     * @return posjugador posicion en la lista Jugadores del jugador de las
     * credenciales introducidas
     */
    static int iniciarsesion() {

        Scanner S = new Scanner(System.in);
        java.io.StreamTokenizer Input = new java.io.StreamTokenizer(System.in);
        System.out.println("Introduzca su nombre de usuario: ");
        String nombre = S.nextLine();
        System.out.println("Introduzca su contrasena: ");
        try {
            Input.nextToken();
        } catch (Exception e) {
        }
        String password = "";
        try {
            password = byteArrayToHexString(computeHash(Input.sval));
        } catch (Exception e) {
        }

        int posjugador = -1;

        for (int i = 0; i < Jugadores.size(); ++i) {

            if (Jugadores.get(i).getNombre_Usuario().equals(nombre) && Jugadores.get(i).getPassword_Usuario().equals(password)) {
                posjugador = i;
            }

        }

        return posjugador;

    }

    /**
     * Se pide introducir el nombre y contrasena del administrador que quiere
     * iniciar sesion. Si las credenciales son correctas, se devuelve la
     * posicion del administrador en la lista Administradores. Si no son
     * correctas se devuelve -1;
     *
     * @return posadmin posicion del administrador en la lista Administradores
     */
    static int iniciarsesionadmin() {

        Scanner S = new Scanner(System.in);
        java.io.StreamTokenizer Input = new java.io.StreamTokenizer(System.in);
        System.out.println("Introduzca su nombre de usuario: ");
        String nombre = S.nextLine();
        System.out.println("Introduzca su contrasena: ");
        try {
            Input.nextToken();
        } catch (Exception e) {
        }
        String password = "";
        try {
            password = byteArrayToHexString(computeHash(Input.sval));
        } catch (Exception e) {
        }

        int posadmin = -1;

        for (int i = 0; i < Administradores.size(); ++i) {

            if (Administradores.get(i).getNombre_Administrador().equals(nombre) && Administradores.get(i).getPassword_Administrador().equals(password)) {
                posadmin = i;
            }

        }

        return posadmin;
    }

    /**
     * Menu principal que se le muestra a un jugador cuando inicia sesion.
     *
     * @param posJugador posicion del jugador en la lista Jugadores
     */
    static void portada(int posJugador) throws IOException {

        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            System.out.println("Hola, " + Jugadores.get(posJugador).getNombre_Usuario() + ". Bienvenido a tu portada.");

            System.out.println("Actividades: ");
            System.out.println("1. Mi Perfil " + getNotificaciones(posJugador));
            System.out.println("2. Ver el perfil de otro usuario");
            System.out.println("0. Cerrar Sesion.");
            System.out.print(">");

            try {
                choice = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, introduce de nuevo la opcion");
                choice = -1;
            }
            sc.nextLine();

            switch (choice) {
                case 0:
                    System.out.println("Cerrando Sesion. Gracias por jugar hoy.");
                    break;
                case 1:
                    miperfil(posJugador);
                    break;

                case 2:

                    int posAjeno = buscarJugador(posJugador);
                    if (posAjeno == -1) {
                        System.out.println("No se ha encontrado un jugador con ese nombre.");
                    } else {
                        perfilajeno(posAjeno, posJugador);
                    }

                    break;
                default:
                    break;
            }

        } while (choice != 0);

    }

    /**
     * Menu principal que se le muestra a un administrador cuando inicia sesion
     *
     * @param posAdmin posicion del administrador que ha iniciado sesion en la
     * lista Administradores
     */
    static void portadaadmin(int posAdmin) {

        Scanner sc = new Scanner(System.in);
        int choice = -1;
        do {
            System.out.println("Hola, administrador " + Administradores.get(posAdmin).getNombre_Administrador() + ". Bienvenido a tu terminal.");
            System.out.println("Actividades: ");
            System.out.println("1. Dar de alta nueva cuenta administradora");
            System.out.println("2. Anadir nueva asignatura al sistema");
            System.out.println("3. Gestionar Apuestas del sistema.");
            System.out.println("0. Cerrar Sesion.");
            System.out.print(">");

            try {
                choice = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, introduce de nuevo la opcion");
                choice = -1;
            }
            sc.nextLine();

            switch (choice) {
                case 0:
                    System.out.println("Cerrando Sesion.");
                    break;
                case 1:
                    registroadmin();
                    break;
                case 2:
                    nuevaasignatura();
                    break;
                case 3:
                    gestionarapuestas();
                    break;
                default:
                    break;
            }

        } while (choice != 0);

    }

    /**
     * Menu de gestion de las apuestas del sistema para administradores.
     */
    public static void gestionarapuestas() {
        Scanner sc = new Scanner(System.in);
        int choice = -1;
        System.out.println("MENU APUESTAS:");
        do {
            System.out.println("1. Cerrar la participacion de las apuestas abiertas.");
            System.out.println("2. Terminar las apuestas con la participacion cerrada y repartir los premios");
            System.out.println("0. Volver atras.");

            try {
                choice = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, introduce de nuevo la opcion");
                choice = -1;
            }
            sc.nextLine();

            switch (choice) {
                case 0:
                    break;
                case 1:
                    cerrarapuestasabiertas();
                    break;
                case 2:
                    terminarapuestas();
                    break;
                default:
                    break;

            }

        } while (choice != 0);
    }

    /**
     * Recorre la lista Apuestas estableciendo el campo Abierta de cada objeto
     * de la clase Apuesta en false que lo tenga en true y que ademas tenga el
     * campo Terminada en false.
     */
    public static void cerrarapuestasabiertas() {

        for (int i = 0; i < Apuestas.size(); ++i) {

            if (Apuestas.get(i).isAbierta() && Apuestas.get(i).isTerminada() == false) {
                Apuestas.get(i).setAbierta(false); //Cerramos las apuestas
            }

        }
        try {
            guardarApuestas();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Recorre la lista Apuestas y pone el campo Terminada en true de las
     * apuestas que tengan el campo Abierta en false. Se comprueban las
     * participaciones de dichas apuestas y se le resta los PINFCoins apostados
     * a los perdedores y se le suman PINFCoins en funcion de lo apostado a los
     * ganadores. PINFCoinsGanados = CantidadApostada * (10 - rangosuperior -
     * rangoinferior).
     */
    public static void terminarapuestas() {

        for (int i = 0; i < Apuestas.size(); ++i) {
            if (Apuestas.get(i).isAbierta() == false && Apuestas.get(i).isTerminada() == false && Apuestas.get(i).getResultado() != -1) {

                Apuestas.get(i).setTerminada(true);
                List<Participacion> participaciones = Apuestas.get(i).getListaParticipaciones();
                double resultado = Apuestas.get(i).getResultado();
                for (int j = 0; j < participaciones.size(); ++j) {
                    int idjugador = participaciones.get(j).getID_Apostador();
                    int posjugador = buscarJugadorporID(idjugador);
                    double r_inf = participaciones.get(j).getRangoinferior();
                    double r_sup = participaciones.get(j).getRangosuperior();

                    if (r_inf <= resultado && resultado <= r_sup)//Victoria
                    {
                        double bonificador = 10 - (r_sup - r_inf);
                        double bote = participaciones.get(j).getCantidad_apostada() * bonificador;
                        Jugadores.get(posjugador).addSaldo(bote);

                    } else {//derrota
                        Jugadores.get(posjugador).addSaldo(-1 * participaciones.get(j).getCantidad_apostada());
                    }

                }

            }
        }
        try {
            guardarJugadores();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            guardarApuestas();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Menu de actividades que puedes realizar un jugador desde su perfil
     *
     * @param posJugador posicion del jugador en la lista Jugadores que quiere
     * ver su perfil
     */
    static void miperfil(int posJugador) {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            try {
                System.out.println("Hola, " + Jugadores.get(posJugador).getNombre_Usuario() + ". Bienvenido a tu perfil.");
                System.out.println("Actualmente cuentas con " + Jugadores.get(posJugador).getSaldo_Jugador() + " coin(s)");
                System.out.println("Actividades: ");
                System.out.println("1. Actualizar Expediente Academico");
                System.out.println("2. Gestionar mis solicitudes de amistad. " + getNumSolicitudes(posJugador));
                System.out.println("3. Gestionar mis asignaturas.");
                System.out.println("4. Ver las apuestas en las que participo.");
                System.out.println("5. Ver las apuestas de las que soy protagonista.");
                System.out.println("0. Volver a la portada");
                System.out.print(">");

                try {
                    choice = sc.nextInt();
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Por favor, introduce de nuevo la opcion");
                    choice = -1;
                }
                sc.nextLine();

                switch (choice) {
                    case 0:
                        break;
                    case 1:
                        System.out.println("Asegurate que has renombrado tu expediente academico como e.pdf y que este se encuentra en la ruta raiz de la apliacion.");
                        System.out.println("Si ese es el caso, pulsa ENTER: ");
                        System.in.read();
                        int coinsexpediente = Leepdf.lee() * 100;
                        Jugadores.get(posJugador).ActualizarExpediente(coinsexpediente);
                        break;
                    case 2:
                        gestionarsolicitudesamistad(posJugador);
                        break;
                    case 3:
                        gestionarasignaturas(posJugador);
                        break;
                    case 4:
                        mostrarParticipacionesde(posJugador);
                        break;
                    case 5:
                        mostrarApuestassobre(posJugador);
                        break;

                }

            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while (choice != 0);

    }

    /**
     * Muestra por pantalla las asignaturas que cursa el jugador que ocupa la
     * posicion posj en la lista Jugadores. Se le permite: 1. Indicar que esta
     * cursando una nueva asignatura
     *
     * @param posj posicion en la lista Jugadores del jugador que esta
     * gestionando sus asignaturas
     */
    public static void gestionarasignaturas(int posj) {

        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            System.out.println("Tus asignaturas: ");
            for (int i = 0; i < Jugadores.get(posj).getSubjectlist().size(); ++i) {
                System.out.println(Jugadores.get(posj).getSubjectlist().get(i) + " - " + AsignaturaNombreporID(Jugadores.get(posj).getSubjectlist().get(i)));
            }

            System.out.println("OPCIONES:");
            System.out.println("1. Anadir nueva asignatura que estoy cursando.");
            System.out.println("2. Establecer nota de las asignaturas de las que me he examinado. ");
            System.out.println("0. Volver a mi perfil");
            System.out.print(">");

            try {
                choice = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, introduce de nuevo la opcion");
                choice = -1;
            }
            sc.nextLine();

            switch (choice) {
                case 0:
                    break;
                case 1:
                    CursarNuevaAsignatura(posj);
                    break;
                case 2:
                    establecernotadeconvocatoria(posj);
                default:
                    break;
            }

        } while (choice != 0);
    }

    /**
     * Se pide introducir la nota que el jugador ha obtenido en las asignaturas
     * cuyas apuestas relacionadas tiene el campo Abierta en false. Se pregunta
     * previamente si el jugador conoce la nota de la asignatura.
     *
     * @param posj posicion del jugador en la lista Jugadores que está metiendo
     * sus notas
     */
    public static void establecernotadeconvocatoria(int posj) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < Jugadores.get(posj).getMisapuestas().size(); ++i) {

            int posapuesta = PosicionApuestaporID(Jugadores.get(posj).getMisapuestas().get(i));

            if (Apuestas.get(posapuesta).isAbierta() == false && Apuestas.get(posapuesta).isTerminada() == false && Apuestas.get(posapuesta).getResultado() == -1) {
                String asignombre = NombreAsignaturaporID(Apuestas.get(posapuesta).getID_Asignatura());
                double resultado = -1;
                int opcion = 0;
                System.out.println("¿Conoces ya la nota que ha sacado en la asignatura " + asignombre + "?");
                System.out.println("1. Sí");
                System.out.println("2. No");
                System.out.print(">>");
                opcion = sc.nextInt();

                if (opcion == 1) {
                    do {
                        System.out.println("Introduce la nota que has obtenido en la asignatura " + asignombre + ".");
                        System.out.println("Si no te has presentado debes introducir 0.");
                        System.out.print(">> ");
                        resultado = sc.nextDouble();
                    } while (resultado > 10 || resultado < 0);
                }
                Apuestas.get(posapuesta).setResultado(resultado);

            }
        }
        try {
            guardarApuestas();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("No requerimos mas notas ahora mismo. Muchas gracias.");
    }

    /**
     * Devuelve el String con el nombre de la asignatura con el identificador id
     * de la lista Asignaturas. Si no se encuentra la asignatura, el String que
     * devuelve es "NULO"
     *
     * @param id identificador de la asignatura
     * @return s String con el nombre de la asignatura
     */
    public static String AsignaturaNombreporID(int id) {
        String s = "NULO";

        for (int i = 0; i < Asignaturas.size(); ++i) {
            if (Asignaturas.get(i).getID_Asignatura() == id) {
                s = Asignaturas.get(i).getNombre_Asignatura();
            }
        }

        return s;
    }

    /**
     * Se muestra por pantalla los datos de todas las asignatura que hay en el
     * sistema. Se pide al jugador que introduzca el identificador numerico de
     * la asignatura que quiere indicar que cursa. Si el identificador
     * introducido no corresponde con ninguna asignatura del sistema, se le
     * indica con un mensaje al jugador. Si el identificador se corresponde con
     * una asignatura se anadira a la lista de asignaturas del jugador mediante
     * el metodo de la clase jugador AddAsignatura(). Automaticamente al anadir
     * la nueva asignatura al listado del jugador, se abrira una nueva apuesta.
     *
     * @param posj posicion en la lista Jugadores del jugador que esta metiendo
     * la informacion
     */
    public static void CursarNuevaAsignatura(int posj) {
        System.out.println("Asignaturas disponibles en el sistema: ");
        for (int i = 0; i < Asignaturas.size(); ++i) {
            System.out.println(Asignaturas.get(i).toString());
        }

        Scanner sc = new Scanner(System.in);
        int idasignatura;

        System.out.println("Introduzca el identificador numerico de la asignatura:");
        do {
            System.out.print(">");
            try {
                idasignatura = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, introduce de nuevo el código de asignatura");
                idasignatura = -1;
            }
            sc.nextLine();
        } while (idasignatura == -1);

        boolean idcorrecto = false;

        int i = 0;
        while (idcorrecto == false && i < Asignaturas.size()) {

            if (Asignaturas.get(i).getID_Asignatura() == idasignatura) {
                idcorrecto = true;
            }

            ++i;
        }

        if (idcorrecto) {
            Jugadores.get(posj).AddAsignatura(idasignatura);
            nuevaapuesta(posj, idasignatura);
            try {
                guardarJugadores();
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Asignatura anadida.Se ha abierto una apuesta al respecto. Volviendo a tu portada");

        } else {
            System.out.println("El identificador de la asignatura introducido:" + idasignatura + ", no se corresponde con ninguna asignatura del sistema. Volviendo a tu portada");
        }

    }

    /*
    * Devuelve una cadena "(n)" siendo n el número de solicitudes de amistad
    * que el usuario tiene pendientes de aceptar. devuelve "" en otro caso.
     */
    public static String getNumSolicitudes(int posj) {
        int idj = Jugadores.get(posj).getUsuario_ID();
        int i = 0, cont = 0;

        while (i != SolicitudesdeAmistad.size()) {
            if (SolicitudesdeAmistad.get(i).getID_JugadorSolicitado() == idj) {
                cont++;
            }
            i++;
        }
        if (cont == 0) {
            return "";
        } else {
            return "(" + cont + ")";
        }
    }

    /*
    * Devuelve la cadena "(!)" si el usuario tiene notificaciones.
    * que el usuario tiene pendientes de aceptar. devuelve "" en otro caso.
     */
    public static String getNotificaciones(int posj) {
        String aux = getNumSolicitudes(posj);
        if (!aux.equals("")) {
            return "(!)";
        } else {
            return "";
        }
    }

    /**
     * Se le muestra al jugador las solicitudes de amistad pendientes que ha
     * recibido y este puede aceptarlas o rechazarlas una a una. Si la acepta,
     * ambos jugadores se convierten en amigos y se elimina la solicitud del
     * sistema Si la rechaza, se elimina la solicitud de amistad del sistema. Si
     * el jugador no tiene solicitudes de amistad recibidas pendiente se le
     * indica con un mensaje Cuando el jugador ha terminado de gestionar todas
     * las solicitudes recibidas se le indica con un mensaje.
     *
     * @param posj posicion en la lista Jugadores del jugador que esta
     * gestionando sus solicitudes de amistad
     */
    public static void gestionarsolicitudesamistad(int posj) {

        int idj = Jugadores.get(posj).getUsuario_ID();
        int i = 0;

        if (!SolicitudesdeAmistad.isEmpty()) {
            do {

                if (SolicitudesdeAmistad.get(i).getID_JugadorSolicitado() == idj) {
                    Scanner sc = new Scanner(System.in);
                    int choice = -1;

                    System.out.println(Jugadores.get(buscarJugadorporID(SolicitudesdeAmistad.get(i).getID_JugadorSolicitante())).getNombre_Usuario() + " te ha enviado una solicitud de amistad.");
                    System.out.println("1. Aceptar");
                    System.out.println("2. Rechazar");

                    do {
                        try {
                            choice = sc.nextInt();
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Por favor, introduce de nuevo la opcion");
                            choice = -1;
                        }
                        sc.nextLine();
                    } while (choice != 1 && choice != 2);

                    if (choice == 1) {
                        Jugadores.get(posj).getFriendlist().add(SolicitudesdeAmistad.get(i).getID_JugadorSolicitante());
                        Jugadores.get(buscarJugadorporID(SolicitudesdeAmistad.get(i).getID_JugadorSolicitante())).getFriendlist().add(idj);
                    }
                    SolicitudesdeAmistad.remove(i); //Eliminamos la solicitud de la lista
                } else {
                    ++i;
                }

            } while (i != SolicitudesdeAmistad.size());
            System.out.println("Has terminado de gestionar tus solicitudes de amistad.");
            try {
                guardarSolicitudesdeAmistad();
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("No tienes solicitudes de amistad pendientes.");
        }
    }

    /**
     * Menu de Actividades que puede realizar un jugador en el perfil de otro
     *
     * @param posJugador posicion del jugador del que se quiere ver el perfil en
     * la lista Jugadores
     * @param posJugadorvisitante posicion del jugador que quiere ver el perfil
     * de otro jugador en la lista Jugadores
     */
    static void perfilajeno(int posJugador, int posJugadorvisitante) {

        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            System.out.println("Hola, " + Jugadores.get(posJugadorvisitante).getNombre_Usuario() + ". Bienvenido al perfil de " + Jugadores.get(posJugador).getNombre_Usuario());

            System.out.println("Actividades: ");
            System.out.println("1. Enviarle solicitud de amistad.");
            System.out.println("2. Participar en una apuesta.");
            System.out.println("0. Volver a la portada");
            System.out.print(">");

            try {
                choice = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, introduce de nuevo la opcion");
                choice = -1;
            }
            sc.nextLine();

            switch (choice) {
                case 0:
                    break;
                case 1:

                    if (Jugadores.get(posJugadorvisitante).getFriendlist().contains(Jugadores.get(posJugador).getUsuario_ID())) {
                        //Son amigos. No se puede mandar una nueva solicitud de amistad
                        System.out.println("Ya sois amigos. No puedes enviarle una nueva solicitud");
                    } else {
                        if (SolicitudAmistadyaRegistrada(new SolicitudAmistad(Jugadores.get(posJugadorvisitante).getUsuario_ID(), Jugadores.get(posJugador).getUsuario_ID()))
                                || SolicitudAmistadyaRegistrada(new SolicitudAmistad(Jugadores.get(posJugador).getUsuario_ID(), Jugadores.get(posJugadorvisitante).getUsuario_ID()))) {
                            System.out.println("Ya has enviado una solicitud de amistad a este jugador o este jugador ya te ha enviado una solicitud de amistad.");
                        } else {
                            SolicitudesdeAmistad.add(new SolicitudAmistad(Jugadores.get(posJugadorvisitante).getUsuario_ID(), Jugadores.get(posJugador).getUsuario_ID()));
                            try {
                                guardarSolicitudesdeAmistad();
                            } catch (IOException ex) {
                                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("Solicitud de amistad enviada!");
                        }

                    }

                    break;

                case 2:
                    if (Jugadores.get(posJugador).esamigode(Jugadores.get(posJugadorvisitante))) {
                        participarenapuesta(posJugador, posJugadorvisitante);
                    } else {
                        System.out.println("No eres amigo de " + Jugadores.get(posJugador).getNombre_Usuario() + " por lo que no puedes participar en sus apuestas.");
                    }
                    break;
                case 3:
                    break;
                default:
                    break;
            }

        } while (choice != 0);

    }

    /**
     * Busca en la lista SolicitudesdeAmistad si existe alguna instancia igual
     * al objeto de la clase SolicitudAmistad sa. Si existe se devuelve true. Si
     * no existe se devuelve false. Dos objetos de la clase SolicitudAmistad son
     * iguales si sus JugadorSolicitante y JugadorSolicitado son iguales.
     *
     * @param sa objeto de la clase SolicitudAmistad que queremos comprobar si
     * ya existe en el la lista SolicitudesdeAmistad
     * @return boolean encontrado
     */
    public static boolean SolicitudAmistadyaRegistrada(SolicitudAmistad sa) {
        boolean encontrado = false;

        for (int i = 0; i < SolicitudesdeAmistad.size(); ++i) {
            if (sa.getID_JugadorSolicitado() == SolicitudesdeAmistad.get(i).getID_JugadorSolicitado() && sa.getID_JugadorSolicitante() == SolicitudesdeAmistad.get(i).getID_JugadorSolicitante()) {
                encontrado = true;
            }
        }

        return encontrado;
    }

    /**
     * Metodo para administradores que permite dar de alta manualmente una nueva
     * asignatura en el sistema. Se le pedira una serie de datos por teclado. Si
     * el identificador de la asignatura ya se encuentra en uso por una
     * asignatura de la lista Asignaturas no se permite dar de alta la nueva
     * asignatura.
     */
    static void nuevaasignatura() {

        Scanner sc = new Scanner(System.in);
        int a_id;
        System.out.println("Anadamos una nueva asignatura al sistema: ");
        System.out.println("Introduzca el Identificador de la asignatura: ");
        do {
            System.out.print(">");
            try {
                a_id = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, introduce de nuevo el código de asignatura");
                a_id = -1;
            }
            sc.nextLine();
        } while (a_id == -1);

        boolean idyaenelsistema = false;
        for (int i = 0; i < Asignaturas.size(); ++i) {
            if (Asignaturas.get(i).getID_Asignatura() == a_id) {
                idyaenelsistema = true;
            }
        }

        if (idyaenelsistema == false) {

            System.out.print("Introduzca el Nombre de la asignatura: >");
            String a_nom = sc.nextLine();
            System.out.print("Introduzca las Siglas de la asignatura: >");
            String a_sig = sc.next();
            System.out.print("Introduzca el numero del Curso (1,2,3,4) de la asignatura: >");
            int a_curso = sc.nextInt();
            System.out.print("Introduzca el numero del Semestre de la asignatura: >");
            int a_semes = sc.nextInt();
            sc.nextLine();
            System.out.print("Introduzca el Nombre del grado de la asignatura: >");
            String a_grado = sc.nextLine();
            System.out.print("Introduca el grado de Difilcultad de la asignatura (1,2,3): >");
            int a_dif = sc.nextInt();

            Asignaturas.add(new Asignatura(a_id, a_nom, a_sig, a_curso, a_semes, a_grado, a_dif));
            try {
                guardarAsignaturas();

                System.out.println("Asignatura anadida.");
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("La asignatura con ese identificador ya esta en el sistema. Compruebe y vuelva a intentarlo.");
        }

    }

    /**
     * Se pide introducir el nombre de un jugador. Si existe un jugador con ese
     * nombre, se devuelve su posicion en la lista Jugadores. Si no existe se
     * devuelve -1
     *
     * @param buscador posicion en la lista Jugadores del jugador que esta
     * buscando a otro jugador
     * @return int pos
     */
    public static int buscarJugador(int buscador) {

        Scanner sc = new Scanner(System.in);
        int pos = -1;
        System.out.println("Introduzca el nombre del jugador que busca: ");
        String nombre = sc.nextLine();

        for (int i = 0; i < Jugadores.size(); ++i) {

            if (i != buscador) { //No queremos que nos devuelva la posicion del jugador que está buscando a otro

                if (nombre.equals(Jugadores.get(i).getNombre_Usuario())) {
                    pos = i;
                }

            }

        }

        return pos;

    }

    /**
     * Devuelve la posicion en la lista Jugadores del jugador con el
     * identificador id. Si no se encuentra se devuelve -1.
     *
     * @param id identificador del jugador que se busca
     * @return int pos
     */
    public static int buscarJugadorporID(int id) {
        int pos = -1;
        for (int i = 0; i < Jugadores.size(); ++i) {
            if (Jugadores.get(i).getUsuario_ID() == id) {
                pos = i;
            }
        }
        return pos;
    }

    /**
     * Crea un nuevo objeto de clase Apuesta y lo guarda en la lista Apuestas.
     * El identificador de la nueva apuesta creada se guarda en la lista
     * Misapuestas del jugador en la posicion posj de la lista Jugadores
     *
     * @param posj posicion en la lista Jugadores del jugador al que se le va a
     * crear una nueva apuesta
     * @param idasignatura identificador de la asignatura a la que hace
     * referencia la apuesta
     */
    public static void nuevaapuesta(int posj, int idasignatura) {

        int idj = Jugadores.get(posj).getUsuario_ID();

        Apuesta ap = new Apuesta(idj, idasignatura, 0);

        Apuestas.add(ap);
        Jugadores.get(posj).getMisapuestas().add(ap.getID_Apuesta());
        try {
            guardarApuestas();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            guardarJugadores();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se muestran las apuestas abiertas disponibles sobre el jugador de la
     * posicion posjugadorprotagonista. Se pide al usuario que introduzca uno de
     * los identificadores de las apuestas mostradas. Si no introduce un
     * identificador valido se vuelve al menu anterior. Si se introduce in
     * identificador valido se da inicio al registro de su participacion en
     * dicha apuesta.
     *
     * @param posjugadorprotagonista
     * @param posjugadorparticipante
     */
    public static void participarenapuesta(int posjugadorprotagonista, int posjugadorparticipante) {

        System.out.println("Apuestas disponibles sobre " + Jugadores.get(posjugadorprotagonista).getNombre_Usuario() + ":");
        for (int i = 0; i < Apuestas.size(); ++i) {
            //System.out.println(Apuestas.get(i).getID_Jugador()+" "+Jugadores.get(posjugadorprotagonista).getUsuario_ID());
            if (Apuestas.get(i).getID_Jugador() == Jugadores.get(posjugadorprotagonista).getUsuario_ID() && Apuestas.get(i).isAbierta()) {

                String s = "";
                s += "Identificador: " + Apuestas.get(i).getID_Apuesta() + " ";

                s += "Asignatura: " + NombreAsignaturaporID(Apuestas.get(i).getID_Asignatura()) + " ";
                System.out.println(s);

            }
        }

        Scanner sc = new Scanner(System.in);
        int choice = -1;
        System.out.println("Introduce el identificador de la apuesta en la que quieres participar: ");
        try {
            choice = sc.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Por favor, introduce de nuevo el identificador");
            choice = -1;
        }
        sc.nextLine();

        int posapuesta = PosicionApuestaporID(choice);

        if (posapuesta == -1) {
            System.out.println("El identificador introducido no se corresponde con el de ninguna apuesta del sistema. Volviendo al perfil del jugador.");
        } else {
            if (Apuestas.get(posapuesta).getID_Jugador() != Jugadores.get(posjugadorprotagonista).getUsuario_ID() || Apuestas.get(posapuesta).isAbierta() == false) {
                System.out.println("El identificador de la apuesta introducida no se corresponde con una apuesta del jugador " + Jugadores.get(posjugadorprotagonista).getNombre_Usuario() + ". Volviendo al perfil del jugador.");
            } else {
                nuevaparticipacion(posjugadorprotagonista, posjugadorparticipante, posapuesta);
                System.out.println("Volviendo al perfil del jugador.");
            }
        }

    }

    /**
     * Solicita al jugador apostador el rango de notas y la cantidad de
     * PINFCoins que se juega. Se crea un nuevo objeto de la clase Participacion
     * y un nuevo objeto de la clase ApuestaParticipacion que enlaza la nueva
     * participacion creada con la apuesta en la que se participa. El objeto
     * Participacion se guarda en la lista de participaciones de la apuesta y el
     * objeto de ApuestaParticipacion se guarda en la lista de
     * MisParticipaciones del jugador apostador.
     *
     *
     * @param posjugadorprotagonista posicion en la lista Jugadores del jugador
     * al que se refiere la apuesta
     * @param posjugadorparticipante posicion en la lista Jugadores del jugador
     * que esta creando su participacion
     * @param posapuesta posicion en la lista Apuestas de la apuesta sobre la
     * que se esta haciendo la participacion
     */
    public static void nuevaparticipacion(int posjugadorprotagonista, int posjugadorparticipante, int posapuesta) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Registrando tu participacion en la apuesta: ");

        int cantidad = 0;
        do {
            System.out.println("Tus PINFCoins actuales: " + Jugadores.get(posjugadorparticipante).getSaldo_Jugador());
            System.out.println("Puedes apostar una cantidad mayor que el numero de PINFcoins que posees pero recuerda que si tu saldo llega a un número igual o inferior que 0 tu cuenta se bloqueara.\nRecuerda que tu apuesta debera ser de al menos una unidad de PINFcoin: ");
            System.out.println("Introduce la cantidad que deseas apostar:");
            System.out.print(">");
            try {
                cantidad = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, introduce un numero");
                cantidad = -1;
            }
            sc.nextLine();
        } while (cantidad <= 1);

        System.out.println("Ahora se te pedira introducir el intervalo de la nota que crees que va a obtener tu amigo. ");
        System.out.println("Recuerda que si el rango del intervalo es demasiado grande se te pedira volver a introducirlo hasta que la diferencia entre los extremos sea como mucho de 9,0.");
        System.out.println("Cuanto mas pequeña sea esta diferencia, mas PINFCoins ganaras si aciertas tu prediccion.");
        double rangoinferior = -1;
        double rangosuperior = 11;
        do {

            do {
                System.out.println("Introduce el rango inferior del intervalo al que pertenece la nota que crees que va a obtener. Puedes usar decimales:");
                rangoinferior = sc.nextDouble();
            } while (rangoinferior < 0.0 || rangoinferior > 10.0);

            do {
                System.out.println("Introduce el rango superior del intervalo al que pertenece la nota que crees que va a obtener. Puedes usar decimales:");
                rangosuperior = sc.nextDouble();
            } while (rangosuperior < rangoinferior || rangosuperior > 10.0);
            
        } while (rangosuperior - rangoinferior > 9.0);

        Participacion part = new Participacion(Jugadores.get(posjugadorparticipante).getUsuario_ID(), cantidad, rangoinferior, rangosuperior);
        ApuestaParticipacion ApPart = new ApuestaParticipacion(Apuestas.get(posapuesta).getID_Apuesta(), Jugadores.get(posjugadorparticipante).getUsuario_ID());

        Apuestas.get(posapuesta).getListaParticipaciones().add(part);
        Jugadores.get(posjugadorparticipante).getMisparticipaciones().add(ApPart);

        try {
            guardarApuestas();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            guardarJugadores();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Tu participacion ha quedado registrada!");

    }

    /**
     * Se le pasa el identificador de una asignatura. Devuelve su nombre como
     * String. Si no se encuenta una asignatura con dicho identificador se
     * devuelve el String "NULO"
     *
     * @param id identificador numerico de una asignatura
     * @return s String con el nombre de la asignatura
     */
    public static String NombreAsignaturaporID(int id) {

        String s = "NULO";

        for (int i = 0; i < Asignaturas.size(); ++i) {
            if (Asignaturas.get(i).getID_Asignatura() == id) {
                s = Asignaturas.get(i).getNombre_Asignatura();
            }
        }

        return s;
    }

    /**
     * Se le pasa un int correspondiente a una apuesta y devuelve su posicion en
     * la lista Apuestas. Si no existe una apuesta para ese identificador se
     * devuelve -1
     *
     * @param id identificador numerico de la apuesta
     * @return pos posicion de la apuesta en la lista Apuesta
     */
    public static int PosicionApuestaporID(int id) {

        int pos = -1;

        for (int i = 0; i < Apuestas.size(); ++i) {
            if (Apuestas.get(i).getID_Apuesta() == id) {
                pos = i;
            }
        }

        return pos;

    }

    private static void limpiaPantalla() {
        ProcessBuilder pb = null;
        switch (System.getProperty("os.name")) {
            case "Windows 10":
                limpiaW(pb);
                break;
            case "Linux":
                limpiaUbuntu(pb);
                break;
            default:
                System.out.println("No se ha detectado SO");
        }
    }

    private static void limpiaUbuntu(ProcessBuilder pb) {
        pb = new ProcessBuilder("/bin/bash", "-c", "clear");
        try {
            pb.inheritIO().start().waitFor();
        } catch (Exception e) {
        }
    }

    private static void limpiaW(ProcessBuilder pb) {
        pb = new ProcessBuilder("cmd", "/c", "cls");
        try {
            pb.inheritIO().start().waitFor();
        } catch (Exception e) {
        }
    }
}
