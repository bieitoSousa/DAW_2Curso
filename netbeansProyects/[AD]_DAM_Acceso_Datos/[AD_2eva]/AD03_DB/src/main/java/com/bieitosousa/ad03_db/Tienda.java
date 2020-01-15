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

import java.util.*;
import java.util.HashMap;

/**
 *
 * @author bieito
 */
public class Tienda {
    private HashMap<String, Producto> mapProd = new HashMap<String, Producto>();
    private HashMap<String, Empleado> mapEmp = new HashMap<String, Empleado>(); 
    private int id;
    private String name;
    private String provincia;
    private String ciudad;        

    public Tienda( String name, String provincia, String ciudad) {
        this.name = name;
        this.provincia = provincia;
        this.ciudad = ciudad;
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

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "Tienda{" + "mapProd=" + mapProd + ", mapEmp=" + mapEmp + ", id=" + id + ", name=" + name + ", provincia=" + provincia + ", ciudad=" + ciudad + '}';
    }
    
    
}
