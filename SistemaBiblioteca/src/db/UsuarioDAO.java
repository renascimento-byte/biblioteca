package db;

import model.Aluno;
import model.Funcionario;
import model.Professor;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final Connection con = ConexaoDB.getConexao();

    // ── CREATE ────────────────────────────────────────────────────────────
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario(nome,matricula,endereco,tipo) VALUES(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getMatricula());
            ps.setString(3, usuario.getEndereco());
            ps.setString(4, usuario.getTipo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário: " + e.getMessage(), e);
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY tipo, nome";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Usuario> listarPorTipo(String tipo) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE tipo=? ORDER BY nome";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários por tipo: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca um usuário pela matrícula para obter seu ID interno no banco.
     * Necessário para inserir empréstimos e reservas.
     */
    public int buscarIdPorMatricula(String matricula) {
        String sql = "SELECT id FROM usuario WHERE matricula=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ID do usuário: " + e.getMessage(), e);
        }
        return -1;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome=?,endereco=? WHERE matricula=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEndereco());
            ps.setString(3, usuario.getMatricula());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────
    public void removerPorMatricula(String matricula) {
        String sql = "DELETE FROM usuario WHERE matricula=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover usuário: " + e.getMessage(), e);
        }
    }

    // ── Mapper ────────────────────────────────────────────────────────────
    private Usuario mapear(ResultSet rs) throws SQLException {
        String nome      = rs.getString("nome");
        String matricula = rs.getString("matricula");
        String endereco  = rs.getString("endereco");
        String tipo      = rs.getString("tipo");
        return switch (tipo) {
            case "Professor"   -> new Professor(nome, matricula, endereco);
            case "Funcionario" -> new Funcionario(nome, matricula, endereco);
            default            -> new Aluno(nome, matricula, endereco);
        };
    }
}