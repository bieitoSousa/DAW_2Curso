/*
 * The MIT License
 *
 * Copyright 2020 bieito.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.bieitosousa.ad03_db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author bieito
 */
class Franquicia {

    HashMap<String, Tienda> mapTienda = new HashMap<String, Tienda>();
    HashMap<String, Producto> mapProd = new HashMap<String, Producto>();
    HashMap<String, Empleado> mapEmp = new HashMap<String, Empleado>();
    HashMap<String, Cliente> mapCli = new HashMap<String, Cliente>();
    private String name;
    protected DB_driver db = DB_driver.instance();
    protected boolean opCli = true;//operaciones de escritura sobre Cliente
    protected boolean opTi = true;//operaciones de escritura sobre Tienda
    protected boolean opEmp = true;//operaciones de escritura sobre Empleado
    protected boolean opProd = true;//operaciones de escritura sobre Producto

    
    
/***    ... METODOS PUBLICOS ...    ***/
    
     /**************************************************************
     * METODOS toString

     ***************************************************************/
    
    @Override
    public String toString() {
        return "Franquicia{" + "mapTienda=" + mapTienda + ", mapProd=" + mapProd + ", mapEmp=" + mapEmp + ", mapCli=" + mapCli + ", name=" + name + ", db=" + db + '}';
    }
     
    
    /**************************************************************
     * METODOS GET
     # evalua op{name} : determina si hubo operaciones de escritura en la DB 
     # true : Carga los datos de la DB.
     # false : Carga los datos de la memoria.
     *  = getMapEmp
     *  = getMapCli
     *  = getMapProd
     *  = getMapTienda
         *
     ****************************************************************/
    
    
    
    public HashMap<String, Tienda> getMapTienda() {
        if (opTi){
        cargarTiendas();
        }
        return mapTienda;
    }
   
    public HashMap<String, Producto> getMapProd() {
        if(opProd){
        cargarProductos();
        }
        return mapProd;
    }

    public HashMap<String, Empleado> getMapEmp() {
        if(opEmp){
        cargarEmpleados();
        }
        return mapEmp;
    }
    
    public HashMap<String, Cliente> getMapCli() {
        if(opCli){
        cargarClientes();
        }
        return mapCli;
    }
    
     /**************************************************************
     * METODOS SET
     # guarda los datos en memoria
     *  = setMapEmp
     *  = setMapCli
     *  = setMapProd
     *  = setMapTienda
         *
     ****************************************************************/

    public void setMapTienda(HashMap<String, Tienda> mapTienda) {
        this.mapTienda = mapTienda;
    }

    public void setMapProd(HashMap<String, Producto> mapProd) {
        this.mapProd = mapProd;
    }

    public void setMapEmp(HashMap<String, Empleado> mapEmp) {
        this.mapEmp = mapEmp;
    }

    public void setListCli(HashMap<String, Cliente> mapCli) {
        this.mapCli = mapCli;
    }
 
    /**************************************************************
     * METODOS ADD
     # llama a un metodo privado para inserta  los Objetos datos en la DB
     *  = addMapEmp => Obj Empleado
     *  = addMapCli => Obj Cliente
     *  = addMapProd    => Obj Producto
     *  = addMapTienda  => Obj Tienda
     # En el metodo privado se determina op{name} = true 
     *      se ha escrito en la DB 
     ****************************************************************/
    
    public void addClient(Cliente cliente) {
        insertCliente(cliente);
    }
    public void addEmpleado(Empleado empleado) {
         insertEmpleado(empleado);
    }
    public void addProducto(Producto producto) {
        insertProducto(producto);
    }
    public void addTienda(Tienda tienda) {
       insertTienda(tienda);      
    }
    
    
    public void delClient(Cliente cliente) {
        deleteCliente(cliente);
    }
    public void delEmpleado(Empleado empleado) {
         deleteEmpleado(empleado);
    }
    public void delProducto(Producto producto) {
        deleteProducto(producto);
    }
    public void delTienda(Tienda tienda) {
       deleteTienda(tienda);      
    }
    
    
    
    /**************************************************************
     * METODOS VIEW
     * VIEW{Name} recorre e imprimir los mapas 
     *  = mapEmp
     *  = mapCli
     *  = mapProd
     *  = mapTienda
     * ===================
     *  evalua : op{Name} = true --> Los datos se han modificado :
     *                               \-> hay que cargar los datos en memoria. 
     *                       false --> Los datos no se modificaron :
     *                               \-> no hay que cargar datos en memoria.
     ****************************************************************/
    public void viewEmpleados(){
        if (opEmp){
            cargarEmpleados();
        }
        for (Empleado em : mapEmp.values()){
            System.out.println(em.toString());
        }
    }
    public void viewClientes(){
        if (opCli){
            cargarClientes();
        }
        for (Cliente cli : mapCli.values()){
            System.out.println(cli.toString());
        }
    }
    public void viewProductos(){
        if (opProd){
            cargarProductos();
        }
        for (Producto p : mapProd.values()){
            System.out.println(p.toString());
        }
    }
     public void viewTiendas(){
        if (opTi){
            cargarTiendas();
        }
        for (Tienda t : mapTienda.values()){
            System.out.println(t.toString());
        }
    }
     
      /***    ... METODOS PRIVATE ...    ***/
     
    //      ==== OPERACIONES LECTURA  SOBRE DB ======== \\
     /**************************************************************
     * cargar{Name} instancia un mapa de objeto a partir de un consulta en la dB  
     *  cargarEmpleados()   =>   mapEmp
     *  cargarClientes      =>   mapCli
     *  cargarProductos     =>   mapProd
     *  cargarTiendas       =>   mapTienda
     * ===================
     * op{Name} = false --> Los datos no se han modificado desde la ultima carga en memoria
     ****************************************************************/ 
            
    private void cargarEmpleados() {
            try
            {
                Connection con = db.getConn();
                Statement statement = con.createStatement();

                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from EMPLEADO");

                while(rs.next()){
                       Empleado em = new Empleado( 
                                rs.getString("EMPLEADO_name"),
                                rs.getString(" EMPLEADO_apellido")
                            );
                            em.setId(rs.getInt("EMPLEADO_id"));  
                    mapEmp.put(em.getName(),em);   
                }
            }catch(SQLException e){
                System.err.println(e.getMessage());
            }finally{
                opEmp = false;    
                DB_driver.finishDB();
            }
        }
    
    private void cargarClientes() {
            try
            {
                Connection con = db.getConn();
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
                        mapCli.put(c.getName(),c);
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }finally{
            DB_driver.finishDB();
            opCli = false;
            }
        }
    
    private void cargarProductos() {
            try
            {
                Connection con = db.getConn();
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
                        mapProd.put(p.getName(),p);
                }
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }finally{
            DB_driver.finishDB();
            opProd = false;
            }
        }
        
    private void cargarTiendas() {
        // creamos las tiendas
            try
            {
                Connection con = db.getConn();
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
                       mapTienda.put(t.getName(),t );
                } 
            }
            catch(SQLException e){
                System.err.println(e.getMessage());
            }finally{
            DB_driver.finishDB();
            opTi=false;
            }
            
    }       
    
     //      ==== OPERACIONES ESCRITURA SOBRE DB ======== \\
 /**************************************************************
     *   ==========      INSERT   =================== 
     * insert{Name} llama a el drive pasandole un objeto 
     *                      el drive --> Escribe el objeto en la dB  
     *  insertEmpleados()   =>   Escribe un Empleado
     *  insertClientes      =>   Escribe un Cliente
     *  insertProductos     =>   Escribe un Producto
     *  insertTiendas       =>   Escribe una Tienda
     * ===================
     * op{Name} = true --> Los datos se han modificado desde la ultima carga en memoria
     ****************************************************************/ 

    private void insertTienda(Tienda t){
    db.insertTienda(t);
    opTi=true;
    }
    private void insertProducto( Producto p){
    db.insertProducto(p);
    opProd=true;
    }
    private void insertEmpleado(Empleado em){
    db.insertEmpleado(em);
    opEmp=true;
    }
    private void insertCliente(Cliente cli){
    db.insertCliente(cli);
    opCli=true;
    }
     /**************************************************************
     *   ==========      DELETE   =================== 
     * delete{Name} llama a el drive pasandole un objeto 
     *                      el drive --> borra los registros de ese objeto en la dB  
     *  deleteEmpleado   =>   Escribe un Empleado
     *  deleteCliente      =>   Escribe un Cliente
     *  deleteProducto    =>   Escribe un Producto
     *  deleteTienda       =>   Escribe una Tienda
     * ===================
     * op{Name} = true --> Los datos se han modificado desde la ultima carga en memoria
     ****************************************************************/ 
    private void deleteTienda(Tienda t){
    db.deleteTienda(t);
    opTi=true;
    }
    private void deleteProducto( Producto p){
    db.deleteProducto(p);
    opProd=true;
    }
    private void deleteEmpleado(Empleado em){
    db.deleteEmpleado(em);
    opEmp=true;
    }
    private void deleteCliente(Cliente cli){
    db.deleteCliente(cli);
    opCli=true;
    }
}
