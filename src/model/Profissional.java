package model;

public class Profissional {

    private int id;
    private String nome;
    private double comissaoPercentual;

    public Profissional() {
    }

    public Profissional(String nome, double comissaoPercentual) {
        this.nome = nome;
        this.comissaoPercentual = comissaoPercentual;
    }

    public Profissional(int id, String nome, double comissaoPercentual) {
        this.id = id;
        this.nome = nome;
        this.comissaoPercentual = comissaoPercentual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getComissaoPercentual() {
        return comissaoPercentual;
    }

    public void setComissaoPercentual(double comissaoPercentual) {
        this.comissaoPercentual = comissaoPercentual;
    }

    @Override
    public String toString() {
        return nome;
    }
}