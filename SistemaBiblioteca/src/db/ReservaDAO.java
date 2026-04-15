package db;

import model.Aluno;
import model.Funcionario;
import model.Livro;
import model.Professor;
import model.Reserva;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    private final Connection con = ConexaoDB.getConexao();

    // ── CREATE ────────────────────────────────────────────────────────────
    public void inserir(Reserva reserva, int usuarioId, int livroId) {
        String sql = """
            INSERT INTO reserva(usuario_id,livro_id,data_solicitacao,data_expiracao,status)
            VALUES(?,?,?,?,?)
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt      (1, usuarioId);
            ps.setInt      (2, livroId);
            ps.setTimestamp(3, new Timestamp(reserva.getDataSolicitacao().getTime()));
            ps.setTimestamp(4, new Timestamp(reserva.getDataExpiracao().getTime()));
            ps.setString   (5, reserva.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir reserva: " + e.getMessage(), e);
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────
    public List<Reserva> listarTodas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = """
            SELECT r.*,
                   u.nome as u_nome, u.matricula, u.endereco, u.tipo,
                   l.id as livro_id, l.titulo, l.autor, l.ano_publicacao,
                   l.categoria, l.quantidade_disponivel
            FROM reserva r
            JOIN usuario u ON u.id = r.usuario_id
            JOIN livro   l ON l.id = r.livro_id
            ORDER BY r.id
        """;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas: " + e.getMessage(), e);
        }
        return lista;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
    public void cancelar(Reserva reserva) {
        String sqlId = """
            SELECT r.id FROM reserva r
            JOIN usuario u ON u.id = r.usuario_id
            JOIN livro   l ON l.id = r.livro_id
            WHERE u.matricula=? AND l.titulo=? AND r.status='ATIVA'
            LIMIT 1
        """;
        try (PreparedStatement ps = con.prepareStatement(sqlId)) {
            ps.setString(1, reserva.getUsuario().getMatricula());
            ps.setString(2, reserva.getLivro().getTitulo());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return;
            int id = rs.getInt("id");

            String sqlUpd = "UPDATE reserva SET status='CANCELADA' WHERE id=?";
            try (PreparedStatement pu = con.prepareStatement(sqlUpd)) {
                pu.setInt(1, id);
                pu.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cancelar reserva: " + e.getMessage(), e);
        }
    }

    // ── Mapper ────────────────────────────────────────────────────────────
    private Reserva mapear(ResultSet rs) throws SQLException {
        Usuario u = criarUsuario(rs);
        Livro   l = new Livro(
            rs.getInt   ("livro_id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getInt   ("ano_publicacao"),
            rs.getString("categoria"),
            rs.getInt   ("quantidade_disponivel")
        );
        Reserva r = new Reserva(u, l);
        String status = rs.getString("status");
        if (!status.equals("ATIVA")) r.setStatus(status);
        return r;
    }

    private Usuario criarUsuario(ResultSet rs) throws SQLException {
        String nome      = rs.getString("u_nome");
        String matricula = rs.getString("matricula");
        String endereco  = rs.getString("endereco");
        return switch (rs.getString("tipo")) {
            case "Professor"   -> new Professor(nome, matricula, endereco);
            case "Funcionario" -> new Funcionario(nome, matricula, endereco);
            default            -> new Aluno(nome, matricula, endereco);
        };
    }
}