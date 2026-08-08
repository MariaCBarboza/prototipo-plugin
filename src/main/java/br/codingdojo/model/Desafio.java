package br.codingdojo.model;

// trocar para lombok

/**
 * Entidade que representa um desafio de programação disponível para uma sessão de Dojo.
 * Mapeia diretamente a tabela "Desafio" do banco SQLite.
 */

public class Desafio {

    private int id;
    private String titulo;
    private String enunciado;
    private int dificuldade;
    private String template;
    private String palavrasChave;

    public Desafio() {
    }

    public Desafio(int id, String titulo, String enunciado, int dificuldade,
                   String template, String palavrasChave) {
        this.id = id;
        this.titulo = titulo;
        this.enunciado = enunciado;
        this.dificuldade = dificuldade;
        this.template = template;
        this.palavrasChave = palavrasChave;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public int getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(int dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getPalavrasChave() {
        return palavrasChave;
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    /**
     * IMPORTANTE: o JComboBox<Desafio> usa toString() para renderizar cada item.
     * Aqui exibimos o título junto com o nível de dificuldade para facilitar
     * a escolha do professor.
     */
    @Override
    public String toString() {
        return titulo + "  (Dificuldade: " + dificuldade + ")";
    }
}
