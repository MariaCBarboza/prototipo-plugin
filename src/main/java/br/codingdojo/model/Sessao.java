package br.codingdojo.model;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma Sessão de Dojo iniciada pelo professor.
 * Mapeia diretamente a tabela "Sessão" do banco SQLite.
 */

public class Sessao {

    private int id;
    private int idDesafio;
    private LocalDateTime data;

    public Sessao() {
    }

    public Sessao(int id, int idDesafio, LocalDateTime data) {
        this.id = id;
        this.idDesafio = idDesafio;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDesafio() {
        return idDesafio;
    }

    public void setIdDesafio(int idDesafio) {
        this.idDesafio = idDesafio;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Sessao{id=" + id +
                ", idDesafio=" + idDesafio +
                ", data=" + data +
                '}';
    }
}