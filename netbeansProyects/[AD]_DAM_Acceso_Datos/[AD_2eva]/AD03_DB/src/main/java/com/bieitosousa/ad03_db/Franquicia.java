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
    private DB_driver db = DB_driver.instance();

    public Franquicia(String name) {
        this.name = name;
    }

    public HashMap<String, Tienda> getMapTienda() {
        return mapTienda;
    }

    public void setMapTienda(HashMap<String, Tienda> mapTienda) {
        this.mapTienda = mapTienda;
    }

    public HashMap<String, Producto> getMapProd() {
        return mapProd;
    }

    public void setMapProd(HashMap<String, Producto> mapProd) {
        this.mapProd = mapProd;
    }

    public HashMap<String, Empleado> getMapEmp() {
        return mapEmp;
    }

    public void setMapEmp(HashMap<String, Empleado> mapEmp) {
        this.mapEmp = mapEmp;
    }

    public HashMap<String, Cliente> getMapCli() {
        return mapCli;
    }

    public void setListCli(HashMap<String, Cliente> mapCli) {
        this.mapCli = mapCli;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addClient(Cliente cliente) {
        db.insertCliente(cliente);
        mapCli.put(cliente.getName(),cliente);
    }

    public void addEmpleado(Empleado empleado) {
         db.insertEmpleado(empleado);
        mapEmp.put(empleado.getName(),empleado);
    }

    public void addProducto(Producto producto) {
        db.insertProducto(producto);
        mapProd.put(producto.getName(),producto);
    }

    public void addTienda(Tienda tienda) {
       db.insertTienda(tienda);
        mapTienda.put(tienda.getName(),tienda);
    }

    public void addProductoATienda( Tienda t, Producto p) {
       db.insertProductoATienda(t,p);
        mapTienda.get(t.getName()). getMapProd().put(p.getName(),p);
    }
    
    public void addEmpleadoATiendaHoras( Tienda t, Empleado em, float nHoras) {
       db.insertEmpleadoATiendaHoras(t,em,nHoras);
        mapTienda.get(t.getName()). getMapEmp().get(em.getName()).setnHoras(nHoras);
    }
        
    public void addProductoATiendaStock( Tienda t, Producto p, int stock) {
       db.insertProductoATiendaStock(t,p,stock);
       p.setStock(stock);
        mapTienda.get(t.getName()). getMapProd().get(p.getName()).setStock(stock);
    }   

    @Override
    public String toString() {
        return "Franquicia{" + "mapTienda=" + mapTienda + ", mapProd=" + mapProd + ", mapEmp=" + mapEmp + ", mapCli=" + mapCli + ", name=" + name + ", db=" + db + '}';
    }
    
}
