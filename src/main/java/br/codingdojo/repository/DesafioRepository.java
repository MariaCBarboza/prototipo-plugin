package br.codingdojo.repository;

import br.codingdojo.model.Desafio;
import com.intellij.openapi.application.PathManager;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private static final String DB_URL;

    static {
        // 1. Define onde o banco vai morar na máquina real do professor (ou na Sandbox)
        String configPath = PathManager.getConfigPath();
        Path pluginDir = Paths.get(configPath, "prototipo-plugin");
        Path dbPath = pluginDir.resolve("coding_dojo.db");

        // 2. Garante que a pasta do plugin exista
        try {
            if (!Files.exists(pluginDir)) {
                Files.createDirectories(pluginDir);
            }

            // 3. Se o banco não existir lá, extrai de dentro do .jar (da pasta resources)
           // if (!Files.exists(dbPath)) {
                // Lê o arquivo que está em src/main/resources/coding_dojo.db
                try (InputStream is = DesafioRepository.class.getResourceAsStream("/coding_dojo.db")) {
                    if (is == null) {
                        throw new RuntimeException("Arquivo coding_dojo.db não encontrado nos resources!");
                    }
                    Files.copy(is, dbPath, StandardCopyOption.REPLACE_EXISTING);
                }
            //}

            // NOVO: Força o carregamento do driver JDBC no ClassLoader do Plugin
            Class.forName("org.sqlite.JDBC");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. Monta a string de conexão apontando para o arquivo físico extraído
        DB_URL = "jdbc:sqlite:" + dbPath.toAbsolutePath().toString();
    }

    public List<Desafio> listarDesafios() {
        List<Desafio> desafios = new ArrayList<>();
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
