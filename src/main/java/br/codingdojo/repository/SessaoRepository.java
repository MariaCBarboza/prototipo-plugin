package br.codingdojo.repository;
import br.codingdojo.model.Sessao;
import com.intellij.openapi.application.PathManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Repositório responsável pelo acesso à tabela "Sessão" no banco SQLite local.
 */
public class SessaoRepository {

    private static final String DB_URL;

    // Configura o caminho absoluto apontando para o banco extraído na Sandbox
    static {
        String configPath = PathManager.getConfigPath();
        Path pluginDir = Paths.get(configPath, "prototipo-plugin");
        Path dbPath = pluginDir.resolve("coding_dojo.db");

        try {
            // Força o carregamento do driver JDBC no ClassLoader do Plugin
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        DB_URL = "jdbc:sqlite:" + dbPath.toAbsolutePath().toString();
    }

    /**
     * Registra uma nova Sessão e retorna o id gerado pelo banco,
     * necessário para vincular as Rodadas a essa sessão.
     */
    public int registrar(Sessao sessao) {
        // Removida a coluna "data". O SQLite usará o DEFAULT CURRENT_TIMESTAMP automaticamente.
        String sql = "INSERT INTO Sessao (id_desafio) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, sessao.getIdDesafio());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int idGerado = keys.getInt(1);
                    sessao.setId(idGerado);
                    return idGerado;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
