package br.codingdojo.repository;
import br.codingdojo.model.Rodada;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório responsável pelo acesso à tabela "Rodada" no banco SQLite local.
 *
 * NÃO é usado pelo UC01 (Configurar Sessão) — cada linha de Rodada representa
 * a participação de UM aluno em UMA rodada (código inicial/final + feedback
 * automático), o que só existe durante a EXECUÇÃO da sessão, não na sua
 * configuração. Deixado pronto aqui para o próximo caso de uso.
 */
public class RodadaRepository {

    private static final String DB_URL = "jdbc:sqlite:dojo.db";

    /**
     * Registra a participação de um aluno em uma rodada específica.
     * Normalmente chamado no INÍCIO da rodada (com codigoFim e feedbackAuto
     * ainda nulos) e depois atualizado via atualizarResultado() ao término.
     */
    public int registrar(Rodada rodada) {
        String sql = "INSERT INTO Rodada "
                + "(id_sessao, nome_participante, numero_rodada, codigo_inicio, codigo_fim, feedback_auto) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, rodada.getIdSessao());
            stmt.setString(2, rodada.getNomeParticipante());
            stmt.setInt(3, rodada.getNumeroRodada());
            stmt.setString(4, rodada.getCodigoInicio());
            stmt.setString(5, rodada.getCodigoFim());
            stmt.setString(6, rodada.getFeedbackAuto());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int idGerado = keys.getInt(1);
                    rodada.setId(idGerado);
                    return idGerado;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Atualiza o código final e o feedback automático ao término da rodada
     * de um participante (fluxo típico: registrar() no início, depois
     * atualizarResultado() quando o tempo da rodada se esgota).
     */
    public void atualizarResultado(int idRodada, String codigoFim, String feedbackAuto) {
        String sql = "UPDATE Rodada SET codigo_fim = ?, feedback_auto = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoFim);
            stmt.setString(2, feedbackAuto);
            stmt.setInt(3, idRodada);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lista todas as participações registradas em uma Sessão, ordenadas por
     * número da rodada e participante. Útil para a tela de avaliação formativa.
     */
    public List<Rodada> listarPorSessao(int idSessao) {
        List<Rodada> rodadas = new ArrayList<>();
        String sql = "SELECT id, id_sessao, nome_participante, numero_rodada, "
                + "codigo_inicio, codigo_fim, feedback_auto "
                + "FROM Rodada WHERE id_sessao = ? ORDER BY numero_rodada ASC, nome_participante ASC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSessao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rodada rodada = new Rodada();
                    rodada.setId(rs.getInt("id"));
                    rodada.setIdSessao(rs.getInt("id_sessao"));
                    rodada.setNomeParticipante(rs.getString("nome_participante"));
                    rodada.setNumeroRodada(rs.getInt("numero_rodada"));
                    rodada.setCodigoInicio(rs.getString("codigo_inicio"));
                    rodada.setCodigoFim(rs.getString("codigo_fim"));
                    rodada.setFeedbackAuto(rs.getString("feedback_auto"));
                    rodadas.add(rodada);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rodadas;
    }
}