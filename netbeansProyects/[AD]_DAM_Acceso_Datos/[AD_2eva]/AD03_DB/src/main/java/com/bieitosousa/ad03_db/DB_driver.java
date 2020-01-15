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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 *
 * @author bieito
 */



public class DB_driver {

    static DB_driver instance() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    Connection con = null;
    Franquicia franquicia = null;
    
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
        /*********
        db.startDB();
        db.getData();
        db.inserts();
        db.selects();
        db.deletes();
        db.selects();
        db.finishDB();
         */////////////
        db.startDB();
        db.CargarObj();
        db.getClientes();
        db.getEmpleados();
        db.getProductos();
        db.getTiendas();
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
    
    public void insertTienda (Tienda t){
    insertTienda(con, t.getName(),t.getCiudad(),t.getProvincia());
    }
    
    private  void insertTienda(Connection con, String name, String provincia, String ciudad){
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
    public void  insertCliente(Cliente c){
        insertCliente(con, c.getName(),c.getApellido(),c.getEmail());
    }
    
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
    
    public void insertEmpleado(Empleado e){
        insertEmpleado(con, e.getName(),e.getApellidos());
    }
    
    private void insertEmpleado(Connection con, String name, String apellido){
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
    public void insertProducto (Producto p){
        insertProducto(con , p.getName(),p.getPrice(),p.getDescription());
    }
    
    private  void insertProducto(Connection con, String name, float price, String description){
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
    private  void insertTiendaProducto(Connection con,int id_Tienda, int id_Producto,int stock ){
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
       selectTienda( con);
       selectCliente(con);
       selectEmpleado(con);
       selectProducto(con);
       selectTiendaProducto(con );
       selectTiendaEmpleado(con);
    }

    private void selectTienda(Connection con) {
        try
        {

            Statement statement = con.createStatement();

            //Probamos a realizar unha consulta
            ResultSet rs = statement.executeQuery("select * from TIENDA");
            
            while(rs.next()){
                System.out.println("TIENDA_id = [" + rs.getInt("TIENDA_id")+"]");
                System.out.println("TIENDA_name = [" + rs.getString("TIENDA_name")+"]");
                System.out.println("TIENDA_provincia = [" + rs.getString("TIENDA_provincia")+"]");
                System.out.println("TIENDA_ciudad = [" + rs.getString("TIENDA_ciudad")+"]");
            }
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    private void selectCliente(Connection con) {
        try
        {

            Statement statement = con.createStatement();

            //Probamos a realizar unha consulta
            ResultSet rs = statement.executeQuery("select * from CLIENTE");
            
            while(rs.next()){
                System.out.println("CLIENTE_id = [" + rs.getInt("CLIENTE_id")+"]");
                System.out.println("CLIENTE_name = [" + rs.getString("CLIENTE_name")+"]");
                System.out.println("CLIENTE_apellido = [" + rs.getString("CLIENTE_apellido")+"]");
                System.out.println("CLIENTE_email = [" + rs.getString("CLIENTE_email")+"]");
            }
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    private void selectEmpleado(Connection con) {
         try
        {

            Statement statement = con.createStatement();

            //Probamos a realizar unha consulta
            ResultSet rs = statement.executeQuery("select * from EMPLEADO");
            
            while(rs.next()){
                System.out.println("EMPLEADO_id = [" + rs.getInt("EMPLEADO_id")+"]");
                System.out.println("EMPLEADO_name = [" + rs.getString("EMPLEADO_name")+"]");
                System.out.println("EMPLEADO_apellido = [" + rs.getString("EMPLEADO_apellido")+"]");
            }   
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }    }

    private void selectProducto(Connection con) {
        try
        {

            Statement statement = con.createStatement();

            //Probamos a realizar unha consulta
            ResultSet rs = statement.executeQuery("select * from PRODUCTO");
            
            while(rs.next()){
                System.out.println("PRODUCTO_id = [" + rs.getInt("PRODUCTO_id")+"]");
                System.out.println("PRODUCTO_name = [" + rs.getString("PRODUCTO_name")+"]");
                System.out.println("PRODUCTO_price = [" + rs.getFloat("PRODUCTO_price")+"]");
                System.out.println("PRODUCTO_description = [" + rs.getString("PRODUCTO_description")+"]");
            }
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    private void selectTiendaEmpleado(Connection con) {
        try
        {

            Statement statement = con.createStatement();

            //Probamos a realizar unha consulta
            ResultSet rs = statement.executeQuery("select * from TIENDA_EMPLEADO");
            
            while(rs.next()){
                System.out.println("TIENDA_id = [" + rs.getInt("TIENDA_id")+"]");
                System.out.println("EMPLEADO_id = [" + rs.getInt("EMPLEADO_id")+"]");
                System.out.println("nHoras = [" + rs.getFloat("nHoras")+"]");
            }
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }    }

    private void selectTiendaProducto(Connection con) {
         try
        {

            Statement statement = con.createStatement();

            //Probamos a realizar unha consulta
            ResultSet rs = statement.executeQuery("select * from TIENDA_PRODUCTO");
            
            while(rs.next()){
                System.out.println("TIENDA_id = [" + rs.getInt("TIENDA_id")+"]");
                System.out.println("PRODUCTO_id = [" + rs.getInt("PRODUCTO_id")+"]");
                System.out.println("stock = [" + rs.getFloat("stock")+"]");
            }
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }    
    }    

    private void deletes() {
       deleteTienda( con,"Tname1");
       deleteCliente(con,"Cliname1");
       deleteEmpleado(con,"Emplname1");
       deleteProducto(con,"Prodname1");
       deleteTiendaProducto(con, 1, 1 );
       deleteTiendaEmpleado(con, 1, 1);
    }

    private void deleteTienda(Connection con, String name) {
        try{
            String sql = "DELETE FROM TIENDA WHERE TIENDA_name = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println(" TIENDA ["+ name + "] --> borrada con éxito");
        }
        catch(SQLException e){
            System.err.println( " TIENDA ["+ name + "] ERROR no se a podido borrar" + e.getMessage());
        }
    }

    private void deleteCliente(Connection con, String name) {
        try{
            String sql = "DELETE FROM CLIENTE WHERE CLIENTE_name = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("CLIENTE ["+ name + "] --> borrado con éxito");
        }
        catch(SQLException e){
            System.err.println(" CLIENTE ["+ name + "] ERROR no se a podido borrar" +e.getMessage());
        }  
    }

    private void deleteEmpleado(Connection con, String name) {
        try{
            String sql = "DELETE FROM EMPLEADO WHERE EMPLEADO_name = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("EMPLEADO ["+ name + "] --> borrado con éxito");
        }
        catch(SQLException e){
            System.err.println(" EMPLEADO ["+ name + "] ERROR no se a podido borrar" +e.getMessage());
        }  
    }

    private void deleteProducto(Connection con, String name) {
         try{
            String sql = "DELETE FROM PRODUCTO WHERE PRODUCTO_name = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("PRODUCTO ["+ name + "] --> borrado con éxito");
        }
        catch(SQLException e){
            System.err.println(" PRODUCTO ["+ name + "] ERROR no se a podido borrar" +e.getMessage());
        }  
    }
    
    

    private void deleteTiendaProducto(Connection con, int idTiend, int idProd) {
        try{
            String sql = "DELETE FROM TIENDA_PRODUCTO WHERE TIENDA_id = ? and PRODUCTO_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, idTiend);
            pstmt.setInt(2, idProd);
            pstmt.executeUpdate();
            System.out.println("TIENDA_PRODUCTO borrada con éxito");
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }  
    }

    private void deleteTiendaEmpleado(Connection con, int idTiend, int idEmp) {
         try{
            String sql = "DELETE FROM TIENDA_EMPLEADO WHERE TIENDA_id = ? and EMPLEADO_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, idTiend);
            pstmt.setInt(2, idEmp);
            pstmt.executeUpdate();
            System.out.println("TIENDA_EMPLEADO borrada con éxito");
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }  
    }
    
    
    private static void update(Connection con,String oldName,String newName){
        try{
            String sql = "UPDATE person SET nome = ? "
                + "WHERE nome = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newName);
            pstmt.setString(2, oldName);
            pstmt.executeUpdate();
            System.out.println("Nome da persoa actualizada con éxito");
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    private void getClientes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void getEmpleados() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void getProductos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void getTiendas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void CargarObj() {
        cargarClientes(con);
        cargarEmpleados(con);
        cargarProductos(con);
        cargarTiendas(con);
    }

    private void cargarClientes(Connection con) {
        if (franquicia != null){
            try
            {

                Statement statement = con.createStatement();

                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from CLIENTE");

                while(rs.next()){
                
                        Cliente c = new Cliente( 
                                rs.getString("CLIENTE_name"),
                                rs.getString("CLIENTE_apellido"),
                                rs.getString("CLIENTE_email")
                            );
                           c.setId(rs.getInt("CLIENTE_id"));
                        franquicia.addClient(c);
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        }
        else {
         System.out.println(" Franquicia no definida");
        }
    }

    private void cargarEmpleados(Connection con) {
        if (franquicia != null){
            try
            {

                Statement statement = con.createStatement();

                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from EMPLEADO");

                while(rs.next()){
                
                       Empleado em = new Empleado( 
                                rs.getString("EMPLEADO_name"),
                                rs.getString(" EMPLEADO_apellido")
                            );
                            em.setId(rs.getInt("EMPLEADO_id"));  
                
                      franquicia.addEmpleado(em);
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        }
        else {
         System.out.println(" Franquicia no definida");
        }
    }
    

    private void cargarProductos(Connection con) {
       if (franquicia != null){
            try
            {
                Statement statement = con.createStatement();
                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from PRODUCTO");
                while(rs.next()){
               
                        Producto p = new Producto( 
                                rs.getString("PRODUCTO_name"),
                                rs.getFloat(" PRODUCTO_price"),
                                rs.getString("PRODUCTO_description")
                        );
                        p.setId(rs.getInt("PRODUCTO_id"));
                        franquicia.addProducto(p);
                        
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        }
        else {
         System.out.println(" Franquicia no definida");
        }
    }

    private void cargarTiendas(Connection con) {
        // creamos las tiendas
        if (franquicia != null){
            try
            {
                Statement statement = con.createStatement();
                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from TIENDA");
                while(rs.next()){
                
                        Tienda t = new Tienda( 
                                rs.getString("TIENDA_name"),
                                rs.getString(" TIENDA_provincia"),
                                rs.getString("TIENDA_ciudad")
                            );
                        t.setId(rs.getInt("TIENDA_id"));
                       franquicia.getMapTienda().put(t.getName(),t );
                } 
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
            for (Tienda t : franquicia.getMapTienda().values()){
                //creamos lisProd dentro de la tienda
                try
                {
                    Statement statement = con.createStatement();
                    //recuperamos el id de los productos de la tienda
                    ResultSet rs = statement.executeQuery("select PRODUCTO_name, stock from TIENDA_PRODUCTO where TIENDA_id = "+t.getId()+" )");
                    // añadimos los productos a la tienda
                    while(rs.next()){
                   Producto p =getProducto(con, rs.getString("PRODUCTO_name"),rs.getInt("stock"));
                     t.getMapProd().put(p.getName(),p);
                    }
                }
                catch(SQLException e){
                    System.err.println(e.getMessage());
                }
                 //creamos lisEmpleado dentro de la tienda
                try
                {
                    Statement statement = con.createStatement();
                    //recuperamos el id de los productos de la tienda
                    ResultSet rs = statement.executeQuery("select EMPLEADO_name, nHoras from TIENDA_EMPLEADO where TIENDA_id = "+t.getId()+" )");
                    // añadimos los productos a la tienda
                    while(rs.next()){
                    Empleado em =getEmpleado(con, rs.getString("EMPLEADO_name"),rs.getFloat("nHoras"));
                    t.getMapEmp().put(em.getName(),em);
                    }
                }
                catch(SQLException e){
                    System.err.println(e.getMessage());
                } 
                
            }    
        }
        else {
         System.out.println(" Franquicia no definida");
        }
    }
    
    private Producto getProducto(Connection con, String name , int stock) {
         Producto p= null;
            try
            {
                Statement statement = con.createStatement();
                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from PRODUCTO where PRODUCTO_name = "+name);
                while(rs.next()){
                    p = new Producto( 
                                     rs.getString("PRODUCTO_name"),
                                     rs.getFloat(" PRODUCTO_price"),
                                     rs.getString("PRODUCTO_description")
                                 );
                    p.setId(rs.getInt("PRODUCTO_id"));
                    p.setStock(stock);
                      
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        return p;
     }
     
    private Producto getProducto(Connection con, String name ) {
         Producto p= null;
            try
            {
                Statement statement = con.createStatement();
                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from PRODUCTO where PRODUCTO_name = "+name);
                while(rs.next()){
                    p = new Producto( 
                                     rs.getString("PRODUCTO_name"),
                                     rs.getFloat(" PRODUCTO_price"),
                                     rs.getString("PRODUCTO_description")
                                 );
                    p.setId(rs.getInt("PRODUCTO_id"));
                      
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        return p;
     }
     

     private Empleado getEmpleado(Connection con, String name , float nhoras) {
         Empleado em = null;
           try
            {
                Statement statement = con.createStatement();

                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from EMPLEADO where EMPLEADO_name = "+name );
                while(rs.next()){
                    em =new Empleado( 
                                rs.getString("EMPLEADO_name"),
                                rs.getString(" EMPLEADO_apellido")
                        );
                    em.setId(rs.getInt("EMPLEADO_id"));
                    em.setnHoras(nhoras);
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        return em;
     }
     
      private Empleado getEmpleado(Connection con, String name) {
         Empleado em = null;
           try
            {

                Statement statement = con.createStatement();

                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from EMPLEADO where EMPLEADO_name = "+name );

                while(rs.next()){
                    em =new Empleado( 
                                rs.getString("EMPLEADO_name"),
                                rs.getString(" EMPLEADO_apellido")
                        );
                    em.setId(rs.getInt("EMPLEADO_id"));
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        return em;
     }

    private int getTiendaId(String name){
     int tID = -1;
           try
            {
                Statement statement = con.createStatement();
                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from TIENDA  where TIENDA_name = "+name );
                while(rs.next()){  
                    tID = rs.getInt("TIENDA_id");
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        return tID;
    }  
    
    private int getProductoId(String name){
         int pID = -1;
           try
            {
                Statement statement = con.createStatement();
                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from PRODUCTO where PRODUCTO_name = "+name );
                while(rs.next()){  
                    pID = rs.getInt("PRODUCTO_id");
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        return pID;
    } 
    
    private int getEmpleadoId(String name){
     int emplID = -1;
           try
            {
                Statement statement = con.createStatement();
                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from EMPLEADO where EMPLEADO_name = "+name );
                while(rs.next()){
                    emplID= rs.getInt("EMPLEADO_id");
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }
        return emplID;
    }  
      
    public void insertProductoATienda(Tienda t, Producto producto) {
        insertTiendaProducto(con, getTiendaId(t.getName()), getProductoId(producto.getName()),0 );      
    }

    public void insertEmpleadoATiendaHoras(Tienda t, Empleado em, float nHoras) {
       insertTiendaEmpleado(con, getTiendaId(t.getName()), getEmpleadoId(em.getName()),nHoras);
    }

    public void insertProductoATiendaStock(Tienda t, Producto p, int stock) {
       insertTiendaProducto(con, getTiendaId(t.getName()), getProductoId(p.getName()),stock );  
    }
    
}//fin DB

