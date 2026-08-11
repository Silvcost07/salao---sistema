package model;

import java.time.LocalDateTime;

public class MovimentoCaixa {

    private int id;
    private String tipo; // ENTRADA ou SAIDA
    private String descricao;
    private double valor;
    private LocalDateTime dataMovimento;
    private Integer agendamentoId; // pode ser nulo

    public MovimentoCaixa() {
    }

    public MovimentoCaixa(String tipo, String descricao, double valor) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
    }

    public MovimentoCaixa(String tipo, String descricao, double valor, Integer agendamentoId) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.agendamentoId = agendamentoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(LocalDateTime dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public Integer getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(Integer agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    @Override
    public String toString() {
        return tipo + " - " + descricao + " - R$" + valor;
    }
}