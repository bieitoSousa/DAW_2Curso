/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bieitosousa.ad03_db;
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
/**
 *
 * @author bieito
 */



public class DB_driver {
    Connection con = null;
    
    String[] DBsql = {
		"CREATE TABLE IF NOT EXISTS TIENDA (\n"
		 +"   TIENDA_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
		 +"   TIENDA_name TEXT NOT NULL UNIQUE,\n"
		 +"   TIENDA_provincia TEXT NOT NULL,\n"
		 +"   TIENDA_ciudad TEXT NOT NULL\n" 
		 +");"
		 ,
		"CREATE TABLE IF NOT EXISTS CLIENTE (\n"
		 +"    CLIENTE_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" 
		 +"    CLIENTE_name TEXT NOT NULL,\n"
		 +"    CLIENTE_apellido TEXT NOT NULL,\n" 
		 +"    CLIENTE_email TEXT NOT NULL UNIQUE\n"
		 +");"
		 ,
		 
		"CREATE TABLE IF NOT EXISTS EMPLEADO (\n"
		 +"    EMPLEADO_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
		 +"    EMPLEADO_name TEXT NOT NULL,\n"
		 +"    EMPLEADO_apellido TEXT NOT NULL\n"
		 +");"
		,
                
		 "CREATE TABLE IF NOT EXISTS PRODUCTO (\n"
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
	
    public void DB_driver(){
    
    }
    public void startDB(){
        String db = "novaBaseDeDatos.db";
        Connection con = connectDatabase(db);
        this.con=con;
        
       
       
}
    public void getData(){
        if(true) {// si no esta creada la base de datos la creamos   
          for(int i=0; i < DBsql.length;i++){
                            createTable(con,DBsql[i]);
                    }
        }
    }
    
    public void finishDB(){
     desconnetDatabase(con);
      this.con=null;
    }
    public static void main(String[] args){
        DB_driver db = new DB_driver();
        db.startDB();
        db.getData();
        db.inserts();
        db.selects();
        db.deletes();
        db.finishDB();
        
        
    }
    
	
    /*
    Esta clase conéctase a base de datos SQLLite que se lle pasa o nome da base de datos
    */
    private static Connection connectDatabase(String filename){
        Connection connection = null;
        try
        {
            //Creamos a conexión a base de datos
            connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
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
    Método que crea una tabla  hacerlo con transiciones.
     */
    //https://www.sqlitetutorial.net/sqlite-java/transaction/
    private static void createTable(Connection con, String sql){
        try{
           /* String sql = "CREATE TABLE IF NOT EXISTS person (\n" +
                    "id integer PRIMARY KEY,\n"+
                    "nome text NOT NULL\n"+
                    ");";
            */
            Statement stmt = con.createStatement();
            stmt.execute(sql);
        }
        catch(SQLException e){
            System.out.println(e.getMessage()+ " SQL FALLIDO {{______\n "+sql+"\n______}}");
        }
        
    }
	
	/**********    Este métodos insert     ***********************/
	/*
    Insert TIENDA
    */
    private static void insertTienda(Connection con, String name, String provincia, String ciudad){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO TIENDA(TIENDA_name, TIENDA_provincia, TIENDA_ciudad) VALUES(?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setString(1, name);
			pstmt.setString(2, provincia);
			pstmt.setString(3, ciudad);
            pstmt.executeUpdate();
            System.out.println(" Tienda"+"["+ name+","+ provincia +","+ ciudad +"]" );
        }
        catch(SQLException e){
            System.out.println(e.getMessage()+ "ERROR EN INSERT {{ Tienda"+"["+ name+","+ provincia +","+ ciudad +"] }}");
        }
    }
	/*
   Insert CLIENTE
    */
    private static void insertCliente(Connection con, String name, String apellido, String email){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO CLIENTE(CLIENTE_name, CLIENTE_apellido, CLIENTE_email) VALUES(?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setString(1, name);
			pstmt.setString(2, apellido);
			pstmt.setString(3, email);
            pstmt.executeUpdate();
            System.out.println(" Cliente"+"["+ name+","+ apellido +","+ email +"]" );
        }
        catch(SQLException e){
            System.out.println(e.getMessage()+ "ERROR EN INSERT {{ Cliente"+"["+ name+","+ apellido +","+ email +"]}}");
        }
    }
	
	/*
   Insert EMPLEADO
    */
    private static void insertEmpleado(Connection con, String name, String apellido){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO EMPLEADO(EMPLEADO_name, EMPLEADO_apellido) VALUES(?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setString(1, name);
			pstmt.setString(2, apellido);
            pstmt.executeUpdate();
            System.out.println("Empleado"+"["+ name+","+ apellido+ "]");
        }
        catch(SQLException e){
            System.out.println(e.getMessage()+ " ERROR EN INSERT {{ Empleado"+"["+ name+","+ apellido+ "]}}");
        }
    }
	
    /*
   Insert PRODUCTO
    */
    private static void insertProducto(Connection con, String name, float price, String description){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO PRODUCTO(PRODUCTO_name, PRODUCTO_price, PRODUCTO_description) VALUES(?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setString(1, name);
			pstmt.setFloat(2, price);
            pstmt.setString(3, description);
			pstmt.executeUpdate();
            System.out.println("Producto"+"["+ name+","+ price+","+ description + "]");
        }
        catch(SQLException e){
            System.out.println(e.getMessage()+ " ERROR EN INSERT {{ Producto"+"["+ name+","+ price+","+ description + "] }}");
        }
    }
	    /*
   Insert TIENDA_PRODUCTO
    */
    private static void insertTiendaProducto(Connection con,int id_Tienda, int id_Producto,int stock ){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO TIENDA_PRODUCTO(TIENDA_id, PRODUCTO_id, stock) VALUES(?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setInt(1, id_Tienda);
	    pstmt.setInt(2, id_Producto);
            pstmt.setInt(3, stock);
			pstmt.executeUpdate();
            System.out.println("Tienda ->> Producto"+"["+ stock + "]");
        }
        catch(SQLException e){
            System.out.println(e.getMessage()+ "ERROR INSERT {{ Tienda ->> Producto"+"["+ stock + "] }}");
        }
    }
	    /*
   Insert TIENDA_EMPLEADO
    */
    private static void insertTiendaEmpleado(Connection con, int idTienda, int idEmpleado, float nHoras){
        try{
            //Fixate que no código SQL o valor do nome e "?". Este valor engadiremolo despois
            String sql = "INSERT INTO TIENDA_EMPLEADO(TIENDA_id, EMPLEADO_id, nHoras) VALUES(?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            //Aquí e cando engadimos o valor do nome
            pstmt.setInt(1, idTienda);
			pstmt.setInt(2, idEmpleado);
            pstmt.setFloat(3, nHoras);
			pstmt.executeUpdate();
            System.out.println("Tienda ->> Empleado"+"["+ nHoras + "]");
        }
        catch(SQLException e){
            System.out.println(e.getMessage()+ "ERROR INSERT {{ Tienda ->> Empleado"+"["+ nHoras + "] }}");
        }
    }

    private void inserts() {
       insertTienda( con,"Tname1","Tprov1","Tciud1");
       insertCliente(con, "Cliname1", "Cliapll1", "Cliemail1");
       insertEmpleado(con, "Emplname1","Emplapll1");
       insertProducto(con, "Prodname1", (float) 1.23,"description1");
       insertTiendaProducto(con, 1, 1,10 );
       insertTiendaEmpleado(con, 1, 1, (float)3.4);
       
    }

    private void selects() {
       selectTienda( con,"*");
       selectCliente(con,"*");
       selectEmpleado(con,"*");
       selectProducto(con,"*");
       selectTiendaProducto(con,"*" );
       selectTiendaEmpleado(con,"*");
    }

    private void selectTienda(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void selectCliente(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void selectEmpleado(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void selectProducto(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void selectTiendaProducto(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void selectTiendaEmpleado(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void deletes() {
       deleteTienda( con,"*");
       deleteCliente(con,"*");
       deleteEmpleado(con,"*");
       deleteProducto(con,"*");
    }

    private void deleteTienda(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void deleteCliente(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void deleteEmpleado(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void deleteProducto(Connection con, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
}//fin DB

