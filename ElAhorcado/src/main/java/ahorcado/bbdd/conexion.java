package ahorcado.bbdd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Alejandro González Navarro
 */
public class conexion {
        // Atributos
    private static Connection con;
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String user="<<Usuario>>";
    private static final String pass="<<Contraseña>>";
    private static final String url="jdbc:mysql://<<URL>>:<<PORT>>/<<URL adicional>>";
    private Statement statement;
    // Constructor
    public conexion(){
        con();
    }
    // Funciones extras
    public void con(){
        con = null;
        try{
            Class.forName (driver);
            // Nos conectamos a la bd
            con = (Connection) DriverManager.getConnection(url, user, pass);
        }
        // Si la conexion NO fue exitosa mostramos un mensaje de error
        catch (ClassNotFoundException | SQLException e){
            System.err.println("Error en la base de datos: "+e);
        }
    }
    public String selectPalabraAleatoria(){
        String palabra = null;
        try{
            statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery("SELECT palabra from palabras ORDER BY RAND() ");
            rs.next();
            System.out.println (rs.getString(1));
            palabra = rs.getString(1);
            statement.close();
        }
        catch (SQLException e){
            System.err.println("Error en la base de datos: "+e);
        }
        return palabra;
    }
    public String selectPistaAleatoria(String palabra, int index){
        String pista = "No hay más pistas";
        try{
            statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery("SELECT consejo FROM consejos WHERE palabraC LIKE '" + palabra+"'");
            while(rs.next() && index == 0){
                index--;
            }
            System.out.println (rs.getString(1));
            pista = rs.getString(1);
            statement.close();
        }
        catch (SQLException e){
            System.err.println("Error en la base de datos: "+e);
        }
        return pista;
    }
}