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
import java.util.*;
import java.util.HashMap;

/**
 *
 * @author bieito
 */
public class Tienda  {
    private HashMap<String, Producto> mapProd = new HashMap<String, Producto>();
    private HashMap<String, Empleado> mapEmp = new HashMap<String, Empleado>(); 
    
    private Franquicia f = null;
    private int id =-1;
    private String name;
    private String provincia;
    private String ciudad;
    private boolean opStock;
    private boolean opHoras;
   

    public Tienda( String name, String provincia, String ciudad) {
        super();
        this.name = name;
        this.provincia = provincia;
        this.ciudad = ciudad;
    }
    
 /***    ... METODOS PUBLICOS ...    ***/
    
     /**************************************************************
     * METODOS GET/SET atributos del constructor

     ***************************************************************/
    public Franquicia getFranquicia(){
        if (f == null){
            f=Franquicia.instance();
//        }else if (!f.mapTienda.containsKey(this.name)){
//            f.addTienda(this);
       }
    return f;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
     /**************************************************************
     * METODOS toString

     ***************************************************************/
    @Override
    public String toString() {
        return "Tienda{" + "mapProd=" + mapProd + ", mapEmp=" + mapEmp + ", id=" + id + ", name=" + name + ", provincia=" + provincia + ", ciudad=" + ciudad + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tienda other = (Tienda) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.provincia, other.provincia)) {
            return false;
        }
        if (!Objects.equals(this.ciudad, other.ciudad)) {
            return false;
        }
        return true;
    }

    /**************************************************************
     * METODOS GET
     # evalua super.op{name} : determina si hubo operaciones de escritura en la DB 
     # true : Carga los datos de la DB.
     # false : Carga los datos de la memoria.
     *  = getMapEmp     Devuelve los empleados de la tienda
     *  = getMapProd    Devuelve los productos de la tienda 
     *  = getId     Recupera el id de la tienda
     ****************************************************************/
    
    public HashMap<String, Producto> getMapProd() {
        if(getFranquicia().opProd){
        cargarProductos();
        }
        return mapProd;
    }
    
    public HashMap<String, Empleado> getMapEmp() {
        if(getFranquicia().opEmp){
        cargarEmpleados();
        }
        return mapEmp;
    }
    
        public int getId() {
        if (id==-1){
        cargarId();
        }
        return id;
    }

     /**************************************************************
     * METODOS SET
     # guarda los datos en memoria
     *  = setMapEmp
     *  = setMapProd
         *
     ****************************************************************/
    public void setMapProd(HashMap<String, Producto> mapProd) {
        this.mapProd = mapProd;
    }

    public void setMapEmp(HashMap<String, Empleado> mapEmp) {
        this.mapEmp = mapEmp;
    }
    
     /**************************************************************
     * METODOS ADD
     # llama a un metodo privado para inserta  los registros datos en la DB
     *  = addProducto => inserta Producto_id Tienda_id 
     *  = addStock    => inserta Producto_id Tienda_id stock
     *  = addHoras    => inserta Producto_id Empleado_id nHoras
     # En el metodo privado se determina op{name} = true 
     *      se ha escrito en la DB 
     * @param p
     ****************************************************************/    
    

    public void addProducto( Producto p ){
    if (getFranquicia().getMapProd().get(p.getName())!= null){
        operateTiendaProducto(p,0);
    }else{
       System.out.println("Producto "+p.getName()+" No es parte de la Franquicia");
       }
    }  

    public void addProducto( String name ){
    Producto p = getFranquicia().getMapProd().get(name);
        if (p!= null){
            operateTiendaProducto(p,0);
        }else{
           System.out.println("Producto "+name+" No es parte de la Franquicia");
           }
    } 
      
 
    public void addEmpleado( Empleado em ){
     if (getFranquicia().getMapEmp().get(em.getName())!= null){
         operateTiendaEmpleado(em,0);
        }else{
               System.out.println("Empleado "+em.getName()+" No es parte de la Franquicia");
           }
    }
    
    public void addEmpleado( String name ){
     Empleado em = getFranquicia().getMapEmp().get(name);
     if (em != null){
         operateTiendaEmpleado(em,0);
        }else{
               System.out.println("Empleado "+name+" No es parte de la Franquicia");
           }
    }
    
    public void addHoras(  Empleado em, float nHoras) {
        if (getMapEmp().get(em.getName())!= null){
            nHoras = nHoras + em.getnHoras(this);
            operateTiendaEmpleado( em,  nHoras);
        }else{
               System.out.println("Empleado "+em.getName()+" No es parte de la Franquicia");
           }
    }  
    public void addHoras(  String name, float nHoras) {
        Empleado em = getFranquicia().mapEmp.get(name);
        if (em != null){
            nHoras = nHoras + em.getnHoras(this);
            operateTiendaEmpleado( em,  nHoras);
        }else{
               System.out.println("Empleado "+name+" No es parte de la Franquicia");
           }
    }  
    public void addStock(  Producto p, int stock) {
        if (getMapProd().get(p.getName())!= null){
        stock = stock + p.getStock(this);    
        operateTiendaProducto(p,stock);
       }else{
           System.out.println("Producto "+p.getName()+" No es parte de la Franquicia");
       }
    } 
    public void addStock(  String name, int stock) {
        Producto p = getFranquicia().mapProd.get(name);
        if (p != null){
        stock = stock + p.getStock(this);    
        operateTiendaProducto(p,stock);
       }else{
           System.out.println("Producto "+name+" No es parte de la Franquicia");
       }
    } 
        
         /**************************************************************
     * METODOS DELETE
     # llama a un metodo privado para Eliminar los registros datos en la DB
     *  = delProducto => elimina Producto_id Tienda_id  elimina el stock y el producto
     *  = delEmpleado    => elimina Producto_id Tienda_id stock elimna las horas del trabajador
     # En el metodo privado se determina op{name} = true 
     *      se ha escrito en la DB 
     * @param p
     ****************************************************************/ 
    
    
 
    public void delProducto( Producto p ){
    if (getFranquicia().mapProd.get(p.getName())!= null){
        deleteTiendaProducto(this,p);
    }else{
       System.out.println("Producto "+p.getName()+" No es parte de la Franquicia");
       }
    }  

    public void delProducto( String name ){
    Producto p = getFranquicia().mapProd.get(name);
        if (p!= null){
           deleteTiendaProducto(this,p);
        }else{
           System.out.println("Producto "+p.getName()+" No es parte de la Franquicia");
           }
    } 
      
 
    public void delEmpleado( Empleado em ){
     if (getFranquicia().mapEmp.get(em.getName())!= null){
         deleteTiendaEmpleado(this,em);
        }else{
               System.out.println("Empleado "+em.getName()+" No es parte de la Franquicia");
           }
    }
    
    public void delEmpleado( String name ){
     Empleado em = getFranquicia().mapEmp.get(name);
     if (em != null){
         deleteTiendaEmpleado(this,em);
        }else{
               System.out.println("Empleado "+em.getName()+" No es parte de la Franquicia");
           }
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
    
    public void viewProductos(){
        if (opStock){
            cargarProductos();
            System.out.println("Cargando PRODUCTOS [........]");
        }
        System.out.println( "_____________ FRANQUICIA : "+this.name+" PRODUCTOS _____________");
        for (Producto p : mapProd.values()){
            System.out.println( p.toString(this) );
        }
        System.out.println( "===================================");
    }
    public void viewEmpleados(){
        if (opHoras){
            cargarEmpleados();
             System.out.println("Cargando EMPLEADOS [........]");
        }
        System.out.println( "_____________ FRANQUICIA : "+this.name+" EMPLEADOS _____________");
        for (Empleado em : mapEmp.values()){
            System.out.println(em.toString(this) );
        }
        System.out.println( "===================================");
    }
    
    
    
     //      ==== OPERACIONES LEECTURA  SOBRE DB ======== \\
     /**************************************************************
     *  = getTiendaId --> Recupera el id del Objeto : con una consulta en la DB  
     ****************************************************************/
    
    private void cargarId() {
        int tID = -1;
           try
            {
                Connection con = getFranquicia().db.getConn();
                Statement statement = con.createStatement();
                //Probamos a realizar unha consulta
                ResultSet rs = statement.executeQuery("select * from TIENDA  where TIENDA_name = "+this.name );
                while(rs.next()){  
                    tID = rs.getInt("TIENDA_id");
                }
            this.id= tID;    
            }catch(SQLException e){
                System.err.println(e.getMessage());
            }finally{
            DB_driver.finishDB();
            }
    }

    private void cargarProductos() {
         ArrayList<int[]> listInfoProd = cargarProductosInfo();
          if (listInfoProd.size()>0){
            for (int[]  infoProducto : listInfoProd){
                System.out.println("idProducto ["+ infoProducto[0] +"] stockProducto ["+infoProducto[1] +"]" );
                try
                    {
                        Connection con = getFranquicia().db.getConn();
                        Statement statement = con.createStatement();
                        //Probamos a realizar unha consulta
                        ResultSet rs = statement.executeQuery("select * from PRODUCTO where PRODUCTO_id  = "+infoProducto[0] );
                        mapProd.clear();
                        while(rs.next()){

                                Producto p = new Producto( 
                                        rs.getString("PRODUCTO_name"),
                                        rs.getFloat(" PRODUCTO_price"),
                                        rs.getString("PRODUCTO_description")
                                );
                                p.setId(rs.getInt("PRODUCTO_id"));
                                p.setStock(infoProducto[1]);
                                mapProd.put(p.getName(),p);
                        }
                    }
                    catch(SQLException e){
                        System.err.println(e.getMessage());
                    }finally{
                    DB_driver.finishDB();
                    }
            }
         }else{
             System.out.println(" Error ... No se han cargado los Empleados");
         }   
    }

    private void cargarEmpleados() {
         ArrayList<float[]> listInfoEmp = cargarEmpleadosInfo();
         if (listInfoEmp.size()>0){
            for (float[]  infoEmpleado : listInfoEmp){
                System.out.println("idEmpleado ["+ (int) infoEmpleado[0] +"] horaEmpleado ["+infoEmpleado[1] +"]" );
                try {
                        DB_driver.finishDB();   
                        Connection con = getFranquicia().db.getConn();
                        Statement statement = con.createStatement(); 
                        System.out.println("select * from EMPLEADO where EMPLEADO_id = " +(int)infoEmpleado[0]);
                        ResultSet rs = statement.executeQuery("select * from EMPLEADO where EMPLEADO_id = " +(int)infoEmpleado[0]+";" );
                        mapEmp.clear();
                        while(rs.next()){
                               Empleado em = new Empleado( 
                                        rs.getString("EMPLEADO_name"),
                                        rs.getString(" EMPLEADO_apellido")
                                    );
                               System.out.println("empleado ["+ (int) infoEmpleado[0]+ "]" +em.toString());
                                    em.setId(rs.getInt("EMPLEADO_id")); 
                                    em.setnHoras(infoEmpleado[1]);
                            mapEmp.put(em.getName(),em);   
                        }
                    }catch(SQLException e){
                        System.err.println(e.getMessage());
                    }finally{
                          
                        DB_driver.finishDB();
                    }
            }
         }else{
             System.out.println(" Error ... No se han cargado los Empleados");
         }
    }

    private ArrayList<int[]> cargarProductosInfo() {
        ArrayList<int[]> list = new ArrayList<int[]>();
            try{
                        Connection con = getFranquicia().db.getConn();
                        Statement statement = con.createStatement();
                        ResultSet rs = statement.executeQuery("select * from TIENDA_PRODUCTO where TIENDA_id = " +this.getId() );
                        while(rs.next()){
                            int[] arry = {
                                 rs.getInt("PRODUCTO_id"),
                                 rs.getInt("stock")
                            };
                         list.add(arry);  
                        }
                    }catch(SQLException e){
                        System.err.println(e.getMessage());
                    }finally{
                        opStock = false;    
                        DB_driver.finishDB();
                    }
        return list;
    }

    private ArrayList<float[]> cargarEmpleadosInfo() {
        ArrayList<float[]> list = new ArrayList<>();   
            try{
                        Connection con = getFranquicia().db.getConn();
                        Statement statement = con.createStatement();

                        //Probamos a realizar unha consulta
                        ResultSet rs = statement.executeQuery("select * from TIENDA_EMPLEADO where TIENDA_id = " +this.getId() );
                        while(rs.next()){
                              float[] arry = {
                                 (float)rs.getInt("EMPLEADO_id"),
                                 rs.getFloat("nHoras")
                            };
                         list.add(arry);  
                        }
                    }catch(SQLException e){
                        System.err.println(e.getMessage());
                    }finally{
                        opHoras = false;    
                        DB_driver.finishDB();
                    }
        return list;
    }
        //      ==== OPERACIONES ESCRITURA  SOBRE DB ======== \\
      /**************************************************************
     * insert{Name} llama a el drive pasandole un objeto 
     *                      el drive --> Escribe el objeto en la dB  
     *  insertTiendaEmpleado   =>   Escribe en la tabla  TIENDA_EMPLEADO
     *                              los datos : TIENDA_id EMPLEADO_id nHoras
     *  insertTiendaProducto   =>   Escribe en la tabla  TIENDA_PRODUCTO
     *                              los datos : TIENDA_id PRODUCTO_id stock
     *  updateTiendaEmpleado   =>   Escribe en la tabla  TIENDA_EMPLEADO
     *                              los datos :  nHoras
     *  updateTiendaProducto   =>   Escribe en la tabla  TIENDA_PRODUCTO
     *                              los datos :  stock
     * ===================
     * op{Name} = true --> Los datos se han modificado desde la ultima carga en memoria
     ****************************************************************/
    private void operateTiendaProducto(  Producto p, int stock) {
       if (p.getStock(this)<0){
           insertTiendaProducto(p,stock);
        }else{
            updateTiendaProducto(p,stock);
        }
    }
    
     private void operateTiendaEmpleado(  Empleado em, float nHoras) {
       if (em.getnHoras(this)<0){
            insertTiendaEmpleado(em,nHoras);
        }else{
            updateTiendaEmpleado(em,nHoras);
        }
    }
    
    
    private void insertTiendaEmpleado(  Empleado em ,float nHoras) {
        if (nHoras < 0)nHoras=0;
        getFranquicia().db.insertTiendaEmpleado(this.getId(),em.getId(),0);
         this.opHoras=true;
    }
        
    private void insertTiendaProducto(  Producto p,  int stock) {
        if (stock < 0)stock=1;
        getFranquicia().db.insertTiendaProducto(this.getId(),p.getId(),stock);
         this.opStock=true;
    } 
    private void updateTiendaEmpleado(  Empleado em, float nHoras) {
        getFranquicia().db.insertTiendaEmpleado(this.getId(),em.getId(),nHoras);
         this.opHoras=true;
    }
        
    private void updateTiendaProducto(  Producto p,  int stock) {
        getFranquicia().db.insertTiendaProducto(this.getId(),p.getId(),stock);
         this.opStock=true;
    } 
    private void  deleteTiendaProducto(Tienda t, Producto p){
       getFranquicia().db.deleteTiendaProducto(t,p); 
        this.opStock=true;
    }
    private void  deleteTiendaEmpleado(Tienda t, Empleado em){
       getFranquicia().db.deleteTiendaEmpleado(t,em); 
       this.opHoras=true;
    }

}
