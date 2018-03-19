/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador_dase_de_datos;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorBD {

       private final String url = "jdbc:derby://localhost:1527/persona";
    private final String usuario = "administrador";
    private final String clave = "1234";
    private java.sql.Connection conexion;
    private java.sql.PreparedStatement seleccionarPersonas;
    private java.sql.PreparedStatement seleccionarPersonasPorApellido;
    private java.sql.PreparedStatement insertarNuevaPersona;

    public ControladorBD() {
        try {
            conexion = DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException ex) {
            System.out.println("error al establecer coneccion " + ex);

//            Logger.getLogger(Controlador_base.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Persona> getPersona() {
        List<Persona> listita = new ArrayList<Persona>();

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet reg = sentencia.executeQuery("select*from persona");

            while (reg.next()) {
                int id = reg.getInt("id");
                String nombre = reg.getString("nombre");
                String apellido = reg.getString("apellido");
                String email = reg.getString("email");
                String telefono = reg.getString("telefono");

                Persona nuevo = new Persona(id, nombre, apellido, email, telefono);

                listita.add(nuevo);
            }

            reg.close();
            sentencia.close();

        } catch (SQLException ex) {
            Logger.getLogger(ControladorBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listita;
    }

    public List<Persona> getPersonaApellido(String apellido) {
        List<Persona> listita = new ArrayList<Persona>();

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet reg = sentencia.executeQuery("select*from persona where apellido like '" + apellido + "%'");

            while (reg.next()) {
                int id = reg.getInt("id");
                String nombre = reg.getString("nombre");
                String apellidob = reg.getString("apellido");
                String email = reg.getString("email");
                String telefono = reg.getString("telefono");

                Persona nuevo = new Persona(id, nombre, apellidob, email, telefono);

                listita.add(nuevo);
            }

            reg.close();
            sentencia.close();

        } catch (SQLException ex) {
            Logger.getLogger(ControladorBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listita;
    }

    public int agregarPersona(String nombre, String apellido, String email, String telefono) {
        int r = 0;
        boolean satisfac = true;
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet reg = sentencia.executeQuery("select*from persona");

            //while (reg.next()) {
            String sqlInsert = String.format("INSERT INTO persona VALUES('%s','%s','%s','%s',%d)",
                    nombre,
                    email,
                    telefono,
                    apellido,
                    this.getPersona().size() + 1
            );//insertando un nueva fila en la tabla

            r = sentencia.executeUpdate(sqlInsert);

            //}
            reg.close();
            sentencia.close();
            

        } catch (SQLException e) {
            System.out.println("error al enviar consulta: " + e);
            satisfac=false;
        }
        if(satisfac==true){
            JOptionPane.showMessageDialog(null, "LOS DATOS HAN SIDO INGRESADOS SATISFACTORIAMENTE");
        }

        return r;
    }

    public static void main(String[] args) {
        ControladorBD con = new ControladorBD();

        con.agregarPersona("Carlos", "Hernandez", "carlos.h123@gmail.com", "123456");
        for (Persona p : con.getPersona()) {
            System.out.println(p);
        }

        System.out.println("------------------");
        for (Persona p : con.getPersonaApellido("Hernandez")) {
            System.out.println(p);
        }

    }
}
