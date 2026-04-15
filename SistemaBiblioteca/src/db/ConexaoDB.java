package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexaoDB {

    private static final String URL     = "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String SENHA   = "";

    private static Connection instancia = null;

    private ConexaoDB() {}

    public static Connection getConexao() {
        try {
            if (instancia == null || instancia.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instancia = DriverManager.getConnection(URL, USUARIO, SENHA);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "Driver MySQL não encontrado.\n" +
                "Adicione mysql-connector-java-*.jar ao classpath do projeto.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao MySQL: " + e.getMessage(), e);
        }
        return instancia;
    }

    public static void fechar() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                instancia = null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}