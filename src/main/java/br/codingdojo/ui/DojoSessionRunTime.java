package br.codingdojo.ui;

/**
 * Mantém em memória os parâmetros da sessão que acabou de ser configurada
 * no UC01, para que o próximo caso de uso (execução da rodada, onde a
 * tabela "Rodada" é de fato populada por participante) saiba qual sessão
 * está ativa e qual a duração combinada para cada rodada.
 *
 * O schema atual não tem coluna para "duração da rodada" em nenhuma tabela,
 * então esse valor não é persistido — é só um parâmetro de orquestração do
 * plugin em tempo de execução. Se no futuro o professor precisar retomar
 * uma sessão configurada anteriormente com a mesma duração, será necessário
 * adicionar essa coluna ao schema (ex: em "Sessao").
 *
 * Implementação mínima (singleton simples); em um plugin real isso poderia
 * virar um Service registrado na IntelliJ Platform (com.intellij.openapi.components.Service).
 */
public final class DojoSessionRunTime {

    private static Integer idSessaoAtiva;
    private static Integer duracaoRodadaMinutos;

    private DojoSessionRunTime() {
    }

    public static void iniciar(int idSessao, int duracaoRodadaMin) {
        idSessaoAtiva = idSessao;
        duracaoRodadaMinutos = duracaoRodadaMin;
    }

    public static Integer getIdSessaoAtiva() {
        return idSessaoAtiva;
    }

    public static Integer getDuracaoRodadaMinutos() {
        return duracaoRodadaMinutos;
    }

    public static void encerrar() {
        idSessaoAtiva = null;
        duracaoRodadaMinutos = null;
    }
}