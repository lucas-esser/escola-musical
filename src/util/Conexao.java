package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe responsavel por abrir e fechar a conexao com o banco MySQL
public class Conexao {

    // Se o seu MySQL tiver outro usuario ou senha, e so mudar aqui
    private static final String URL = "jdbc:mysql://localhost:3306/db_escola_musica";
    private static final String USUARIO = "root";
    private static final String SENHA = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static void closeConnection(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexao: " + e.getMessage());
            }
        }
    }

    // Usado no Main para avisar o usuario se o banco nao estiver ligado
    public static boolean testarConexao() {
        try (Connection c = getConnection()) {
            return c != null;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco: " + e.getMessage());
            return false;
        }
    }
}
