package br.codingdojo.repository;

import br.codingdojo.model.Desafio;
import com.intellij.openapi.application.PathManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório responsável pelo acesso à tabela "Desafio" no banco SQLite local.
 *
 * Dependência necessária (build.gradle / pom.xml):
 *   org.xerial:sqlite-jdbc:3.45.x
 *
 * A conexão usa o driver JDBC padrão (java.sql.*), sem ORM, conforme solicitado.
 */
public class DesafioRepository {

    // Ajuste o caminho conforme a estratégia de persistência do plugin.
    // Sugestão: usar o diretório de configuração da IDE (PathManager.getConfigPath())
    // em vez de um caminho relativo fixo, para não depender do diretório de execução.
    private static final String DB_URL;
    static {
        String configPath = PathManager.getConfigPath();
        java.io.File dbDir = new java.io.File(configPath, "prototipo-plugin");
        if (!dbDir.exists()) dbDir.mkdirs();
        DB_URL = "jdbc:sqlite:" + new java.io.File(dbDir, "coding_dojo.db").getAbsolutePath();
    }

    private void ensureDatabase() {
        String createTable = "CREATE TABLE IF NOT EXISTS Desafio (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL, " +
                "enunciado TEXT, " +
                "dificuldade INTEGER, " +
                "template TEXT, " +
                "palavras_chave TEXT" +
                ")";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lista todos os desafios cadastrados, ordenados por nível de dificuldade,
     * para popular o JComboBox da Tool Window (Regra de Negócio 2 do UC01).
     */
    public List<Desafio> listarDesafios() {
        List<Desafio> desafios = new ArrayList<>();
        ensureDatabase();

        String sql = "SELECT id, titulo, enunciado, dificuldade, template, palavras_chave "
                + "FROM Desafio ORDER BY dificuldade ASC, titulo ASC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Desafio desafio = new Desafio();
                desafio.setId(rs.getInt("id"));
                desafio.setTitulo(rs.getString("titulo"));
                desafio.setEnunciado(rs.getString("enunciado"));
                desafio.setDificuldade(rs.getInt("dificuldade"));
                desafio.setTemplate(rs.getString("template"));
                desafio.setPalavrasChave(rs.getString("palavras_chave"));
                desafios.add(desafio);
            }

        } catch (SQLException e) {
            // Em um plugin real, prefira logar via com.intellij.openapi.diagnostic.Logger
            // em vez de imprimir o stack trace diretamente.
            e.printStackTrace();
        }

        return desafios;
    }

    /**
     * Busca um único desafio pelo id. Útil para reidratar a entidade completa
     * (com enunciado e template) no momento de iniciar a sessão.
     */
    public Desafio buscarPorId(int id) {
        String sql = "SELECT id, titulo, enunciado, dificuldade, template, palavras_chave "
                + "FROM Desafio WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Desafio desafio = new Desafio();
                    desafio.setId(rs.getInt("id"));
                    desafio.setTitulo(rs.getString("titulo"));
                    desafio.setEnunciado(rs.getString("enunciado"));
                    desafio.setDificuldade(rs.getInt("dificuldade"));
                    desafio.setTemplate(rs.getString("template"));
                    desafio.setPalavrasChave(rs.getString("palavras_chave"));
                    return desafio;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
