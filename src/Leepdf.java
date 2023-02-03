
import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * Clase que lee archivos .pdf con los la informacion del expediente academico
 * del jugador
 *
 * @author Ignacio Rodigrez Perez Comentado/Modificado por Cristobal Jose Velo
 * Huerta
 */
public class Leepdf {

    /*
    public static void main(String[] args) 
    {
	Integer valor = lee();
	//System.out.println("Tenemos "+ valor+ " pinfcoins");
    }
     */
    /**
     * Lee un expediente academico en formato .pdf y devuelve un entero
     * correspodiente a una puntucion en funcion del numero de aprobados,
     * notables, sobresalientes y matriculas de honor que tiene el estudiante.
     *
     * @return devuelve la puntuacion obtenida del expediente academico
     */
    public static int lee() {
        PdfReader reader;
        Integer cont = 0;
        try {
            reader = new PdfReader("e.pdf");
            //System.out.println("Tu expediente tiene " + reader.getNumberOfPages() + " paginas:\n");

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                String page = PdfTextExtractor.getTextFromPage(reader, i);
                cont += actualizaContador(page, "APROBADO", 1);
                //System.out.println("En la pagina " + i + " contando aprobados, llevas " + cont + " puntos");
                cont += actualizaContador(page, "NOTABLE", 2);
                //System.out.println("En la pagina " + i + " contando notables, llevas " + cont + " puntos");
                cont += actualizaContador(page, "SOBRESALIENTE", 3);
                //System.out.println("En la pagina " + i + " contando sobresalientes, llevas " + cont + " puntos");
                cont += actualizaContador(page, "MATRICULA", 4);
                //System.out.println("En la pagina " + i + " contando matriculas, llevas " + cont + " puntos\n");
            }
        } catch (IOException e) {
            System.out.println("No se ha encontrado expediente(debe nombrarse e.pdf, y encontrarse en la raiz del proyecto)");
        }
        return cont;
    }

    private static int actualizaContador(String page, String calif, Integer aux) {
        int cont = 0;
        while (page.indexOf(calif) > -1) {
            page = page.substring(page.indexOf(calif) + calif.length(), page.length());
            cont += aux;
        }
        return cont;
    }
}
