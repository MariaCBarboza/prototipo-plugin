package br.codingdojo.repository;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseConfig {

    // Define o nome do arquivo que será criado na raiz do projeto
    private static final String URL = "jdbc:sqlite:coding_dojo.db";

    public static void inicializarBanco() {
        // Ao tentar conectar, se o arquivo coding_dojo.db não existir, o SQLite cria um novo.
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            if (conn != null) {
                System.out.println("Arquivo de banco de dados conectado/criado com sucesso.");

                // Criação da Tabela Desafio
                String sqlDesafio = "CREATE TABLE IF NOT EXISTS Desafio ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "titulo VARCHAR(255) NOT NULL,"
                        + "enunciado TEXT NOT NULL,"
                        + "dificuldade INTEGER NOT NULL,"
                        + "template TEXT,"
                        + "palavras_chave VARCHAR(255)"
                        + ");";
                stmt.execute(sqlDesafio);

                // Criação da Tabela Sessao
                String sqlSessao = "CREATE TABLE IF NOT EXISTS Sessao ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "id_desafio INTEGER NOT NULL,"
                        + "data DATETIME DEFAULT CURRENT_TIMESTAMP,"
                        + "FOREIGN KEY (id_desafio) REFERENCES Desafio(id)"
                        + ");";
                stmt.execute(sqlSessao);

                // Criação da Tabela Rodada
                String sqlRodada = "CREATE TABLE IF NOT EXISTS Rodada ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "id_sessao INTEGER NOT NULL,"
                        + "nome_participante VARCHAR(255) NOT NULL,"
                        + "numero_rodada INTEGER NOT NULL,"
                        + "codigo_inicio TEXT,"
                        + "codigo_fim TEXT,"
                        + "feedback_auto TEXT,"
                        + "FOREIGN KEY (id_sessao) REFERENCES Sessao(id)"
                        + ");";
                stmt.execute(sqlRodada);

                System.out.println("Tabelas inicializadas com sucesso de acordo com o Diagrama ER.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    // Método para ser chamado em outras partes do código quando precisar fazer INSERTs ou SELECTs
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
