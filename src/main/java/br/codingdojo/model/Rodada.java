package br.codingdojo.model;

/**
 * Entidade que representa uma Rodada de um participante dentro de uma Sessão de Dojo.
 * Mapeia diretamente a tabela "Rodada" do banco SQLite.
 *
 * Cada linha aqui NÃO é "a rodada da sessão" em si, e sim o registro de UM
 * participante em UMA rodada: o código com que ele começou (codigoInicio),
 * o código com que ele terminou (codigoFim) e o feedback automático gerado
 * a partir dessa diferença (feedbackAuto).
 *
 * Essa entidade é populada durante a EXECUÇÃO da rodada (fora do escopo do
 * UC01 - Configurar Sessão), quando cada participante efetivamente programa.
 */

public class Rodada {

    private int id;
    private int idSessao;
    private String nomeParticipante;
    private int numeroRodada;
    private String codigoInicio;
    private String codigoFim;
    private String feedbackAuto;

    public Rodada() {
    }

    public Rodada(int idSessao, String nomeParticipante, int numeroRodada,
                  String codigoInicio, String codigoFim, String feedbackAuto) {
        this.idSessao = idSessao;
        this.nomeParticipante = nomeParticipante;
        this.numeroRodada = numeroRodada;
        this.codigoInicio = codigoInicio;
        this.codigoFim = codigoFim;
        this.feedbackAuto = feedbackAuto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSessao() {
        return idSessao;
    }

    public void setIdSessao(int idSessao) {
        this.idSessao = idSessao;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public void setNomeParticipante(String nomeParticipante) {
        this.nomeParticipante = nomeParticipante;
    }

    public int getNumeroRodada() {
        return numeroRodada;
    }

    public void setNumeroRodada(int numeroRodada) {
        this.numeroRodada = numeroRodada;
    }

    public String getCodigoInicio() {
        return codigoInicio;
    }

    public void setCodigoInicio(String codigoInicio) {
        this.codigoInicio = codigoInicio;
    }

    public String getCodigoFim() {
        return codigoFim;
    }

    public void setCodigoFim(String codigoFim) {
        this.codigoFim = codigoFim;
    }

    public String getFeedbackAuto() {
        return feedbackAuto;
    }

    public void setFeedbackAuto(String feedbackAuto) {
        this.feedbackAuto = feedbackAuto;
    }

    @Override
    public String toString() {
        return "Rodada " + numeroRodada + " - " + nomeParticipante;
    }
}