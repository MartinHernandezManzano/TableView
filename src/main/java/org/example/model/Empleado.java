package org.example.model;

public class Empleado {

    private int id;
    private String nombre;
    private double salario;

    public Empleado(int id, String nombre, double salario) {
        this.id = id;
        this.nombre = nombre;
        this.salario = salario;
    }

    public int getId()         { return id; }
    public String getNombre()  { return nombre; }
    public double getSalario() { return salario; }
}