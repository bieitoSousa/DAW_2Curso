import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    
    String[] DBsql = {
		"CREATE TABLE IF NOT EXISTS TIENDA (\n"
		 +"   TIENDA_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
		 +"   TIENDA_name TEXT NOT NULL UNIQUE,\n"
		 +"   TIENDA_provincia TEXT NOT NULL,\n"
		 +"   TIENDA_ciudad TEXT NOT NULL,\n" 
		 +");"
		 ,
		"CREATE TABLE IF NOT EXISTS CLIENTE (\n"
		 +"    CLIENTE_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" 
		 +"    CLIENTE_name TEXT NOT NULL,\n"
		 +"    CLIENTE_apellido TEXT NOT NULL,\n" 
		 +"    CLIENTE_email TEXT NOT NULL UNIQUE,\n"
		 +");"
		 ,
		 
		"CREATE TABLE IF NOT EXISTS EMPLEADO (\n"
		 +"    EMPLEADO_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
		 +"    EMPLEADO_name TEXT NOT NULL,\n"
		 +"    EMPLEADO_apellido TEXT NOT NULL,\n"
		 +");"
		,

		 +"CREATE TABLE IF NOT EXISTS PRODUCTO (\n"
		 +"    PRODUCTO_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
		 +"    PRODUCTO_name TEXT NOT NULL,\n"
		 +"	   PRODUCTO_price REAL NOT NULL UNIQUE,\n"
		 +"    PRODUCTO_description TEXT NOT NULL UNIQUE\n"
		 +");"
		,

		"CREATE TABLE IF NOT EXISTS TIENDA_EMPLEADO(\n"
		 +"   TIENDA_id INTEGER,\n"
		 +"   EMPLEADO_id INTEGER,\n"
		 +"   nHoras REAL,\n"
		 +"   PRIMARY KEY (TIENDA_id, EMPLEADO_id),\n"
		 +"   FOREIGN KEY (TIENDA_id) \n"
		 +"      REFERENCES TIENDA (TIENDA_id) \n"
		 +"         ON DELETE CASCADE \n"
		 +"         ON UPDATE NO ACTION,\n"
		 +"   FOREIGN KEY (EMPLEADO_id) \n"
		 +"      REFERENCES EMPLEADO (EMPLEADO_id) \n"
		 +"         ON DELETE CASCADE \n"
		 +"         ON UPDATE NO ACTION\n"
		 +");"
		,

		"CREATE TABLE IF NOT EXISTS TIENDA_PRODUCTO(\n"
		 +"   TIENDA_id INTEGER,\n"
		 +"   PRODUCTO_id INTEGER,\n"
		 +"   stock INTEGER,\n"
		 +"   PRIMARY KEY (TIENDA_id, PRODUCTO_id),\n"
		 +"   FOREIGN KEY (TIENDA_id) \n"
		 +"      REFERENCES TIENDA (TIENDA_id) \n"
		 +"         ON DELETE CASCADE \n"
		 +"         ON UPDATE NO ACTION,\n"
		 +"   FOREIGN KEY (PRODUCTO_id) \n"
		 +"      REFERENCES PRODUCTO (PRODUCTO_id) \n"
		 +"         ON DELETE CASCADE \n"
		 +"         ON UPDATE NO ACTION\n"
		 +");"
 
	};
	
    public static void main(String[] args){
        String db = "novaBaseDeDatos.db";
        Connection con = connectDatabase(db);
        for(int i=0; i < DBsql.length;i++){
			createTable(con,DBsql[i]);
		}
        desconnetDatabase(con);
    }
    
	
    /*
    Esta clase conéctase a base de datos SQLLite que se lle pasa o nome da base de datos
    */
    private static Connection connectDatabase(String filename){
        Connection connection = null;
        try
        {
            //Creamos a conexión a base de datos
            connection = DriverManager.getConnection("jdbc:sqlite:/home/" + filename);
            System.out.println("Conexión realizada con éxito");
            return connection;
             
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    /*
    Este método desconectase dunha base de datos en SQLLite a que se lle pasa a conexión
    */
    private static void desconnetDatabase(Connection connection){
        try{
            if(connection != null){
                connection.close();
                System.out.println("Desconexión realizada con éxito");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /*
    Método que crea una tabla  
     */
    private static void createTable(Connection con, String sql){
        try{
            String sql = "CREATE TABLE IF NOT EXISTS person (\n" +
                    "id integer PRIMARY KEY,\n"+
                    "nome text NOT NULL\n"+
                    ");";

            Statement stmt = con.createStatement();
            stmt.execute(sql);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }
	
	/**********    Este métodos insert     ***********************/
	/*
    Insert TIENDA
    */
    private static void insertTienda(Connection con, String name, String provincia, String ciudad){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO TIENDA(TIENDA_name, TIENDA_provincia, TIENDA_ciudad) VALUES(?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setString(1, name);
			pstmt.setString(2, provincia);
			pstmt.setString(3, ciudad);
            pstmt.executeUpdate();
            System.out.println("Persoa engadida con éxito");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
	/*
   Insert CLIENTE
    */
    private static void insertCliente(Connection con, String name, String apellido, String email){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO CLIENTE(CLIENTE_name, CLIENTE_apellido, CLIENTE_email) VALUES(?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setString(1, name);
			pstmt.setString(2, apellido);
			pstmt.setString(3, email);
            pstmt.executeUpdate();
            System.out.println("Persoa engadida con éxito");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
	
	/*
   Insert EMPLEADO
    */
    private static void insertEmpleado(Connection con, String name, String apellido){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO EMPLEADO(EMPLEADO_name, EMPLEADO_apellido) VALUES(?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setString(1, name);
			pstmt.setString(2, apellido);
            pstmt.executeUpdate();
            System.out.println("Persoa engadida con éxito");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
	
    /*
   Insert PRODUCTO
    */
    private static void insertProducto(Connection con, String name, float price, String description){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO PRODUCTO(PRODUCTO_name, PRODUCTO_price, PRODUCTO_description) VALUES(?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setString(1, name);
			pstmt.setFloat(2, price);
            pstmt.setString(3, description);
			pstmt.executeUpdate();
            System.out.println("Persoa engadida con éxito");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
	    /*
   Insert TIENDA_PRODUCTO
    */
    private static void insertTiendaProducto(Connection con,int id_Tienda, int id_Producto,int stock ){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO TIENDA_PRODUCTO(TIENDA_id, PRODUCTO_id, stock) VALUES(?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setInteger(1, id_Tienda);
			pstmt.setInteger(2, id_Producto);
            pstmt.setInteger(3, stock);
			pstmt.executeUpdate();
            System.out.println("Persoa engadida con éxito");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
	    /*
   Insert TIENDA_EMPLEADO
    */
    private static void insertTiendaEmpleado(Connection con, int idtienda, int idEmpleado, float nHoras){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO TIENDA_EMPLEADO(TIENDA_id, EMPLEADO_id, nHoras) VALUES(?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setInt(1, idTienda);
			pstmt.setInt(2, idEmpleado);
            pstmt.setFoat(3, nHoras);
			pstmt.executeUpdate();
            System.out.println("Persoa engadida con éxito");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
	
}//fin DB

Insertar datos
Imos proceder a engadir unha persoa a táboa creada no paso anterior.



public class Main {
    
    
    public static void main(String[] args){
        String db = "novaBaseDeDatos.db";
        Connection con = connectDatabase(db);
        insertPerson(con, "Manuel");
        desconnetDatabase(con);
    }
    
    /*
    Esta clase conéctase a base de datos SQLLite que se lle pasa o nome da base de datos
    */
    private static Connection connectDatabase(String filename){
        Connection connection = null;
        try
        {
            //Creamos a conexión a base de datos
            connection = DriverManager.getConnection("jdbc:sqlite:/home/" + filename);
            System.out.println("Conexión realizada con éxito");
            return connection;
             
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    /*
    Este método desconectase dunha base de datos en SQLLite a que se lle pasa a conexión
    */
    private static void desconnetDatabase(Connection connection){
        try{
            if(connection != null){
                connection.close();
                System.out.println("Desconexión realizada con éxito");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    
    
}