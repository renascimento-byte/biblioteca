package db;

import model.Multa;

import java.sql.*;

public class MultaDAO {

    private final Connection con = ConexaoDB.getConexao();

    /**
     * Registra uma multa associada a um empréstimo.
     * O empréstimo_id é obtido buscando o último empréstimo finalizado do usuário.
     */
    public void inserir(Multa multa, String matriculaUsuario) {
        // Busca o ID do empréstimo mais recente finalizado/atrasado deste usuário
        String sqlId = """
            SELECT e.id FROM emprestimo e
            JOIN usuario u ON u.id = e.usuario_id
            WHERE u.matricula = ? AND e.status IN ('FINALIZADO','ATRASADO')
            ORDER BY e.id DESC LIMIT 1
        """;
        try (PreparedStatement ps = con.prepareStatement(sqlId)) {
            ps.setString(1, matriculaUsuario);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return;
            int empId = rs.getInt("id");

            String sql = "INSERT INTO multa(emprestimo_id,dias_atraso,valor) VALUES(?,?,?)";
            try (PreparedStatement pi = con.prepareStatement(sql)) {
                pi.setInt   (1, empId);
                pi.setInt   (2, multa.getDiasAtraso());
                pi.setDouble(3, multa.calcularValor());
                pi.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir multa: " + e.getMessage(), e);
        }
    }

    /** Retorna o total de multas em aberto de um usuário (para relatório futuro). */
    public double totalMultasPorUsuario(String matricula) {
        String sql = """
            SELECT COALESCE(SUM(m.valor),0)
            FROM multa m
            JOIN emprestimo e ON e.id = m.emprestimo_id
            JOIN usuario    u ON u.id = e.usuario_id
            WHERE u.matricula = ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao calcular multas: " + e.getMessage(), e);
        }
        return 0.0;
    }
}