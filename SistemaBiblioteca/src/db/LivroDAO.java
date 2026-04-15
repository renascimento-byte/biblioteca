package db;

import model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    private final Connection con = ConexaoDB.getConexao();

    // ── CREATE ────────────────────────────────────────────────────────────
    public void inserir(Livro livro) {
        String sql = "INSERT INTO livro(titulo,autor,ano_publicacao,categoria,quantidade_disponivel) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setInt   (3, livro.getAnoPublicacao());
            ps.setString(4, livro.getCategoria());
            ps.setInt   (5, livro.getQuantidadeDisponivel());
            ps.executeUpdate();

            // Atualiza o ID gerado pelo banco no objeto
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                // Livro não tem setId, então usamos reflexão ou simplesmente recriamos —
                // como o construtor exige id, vamos buscar o próximo id via listarTodos()
                // O ID será sincronizado ao recarregar a tabela.
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir livro: " + e.getMessage(), e);
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────
    public List<Livro> listarTodos() {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM livro ORDER BY id";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros: " + e.getMessage(), e);
        }
        return lista;
    }

    public Livro buscarPorId(int id) {
        String sql = "SELECT * FROM livro WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro: " + e.getMessage(), e);
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
    public void atualizar(Livro livro) {
        String sql = "UPDATE livro SET titulo=?,autor=?,ano_publicacao=?,categoria=?,quantidade_disponivel=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setInt   (3, livro.getAnoPublicacao());
            ps.setString(4, livro.getCategoria());
            ps.setInt   (5, livro.getQuantidadeDisponivel());
            ps.setInt   (6, livro.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro: " + e.getMessage(), e);
        }
    }

    /** Atalho usado pelo controller de empréstimo/reserva para ajustar estoque. */
    public void atualizarQuantidade(int livroId, int novaQuantidade) {
        String sql = "UPDATE livro SET quantidade_disponivel=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, novaQuantidade);
            ps.setInt(2, livroId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar quantidade: " + e.getMessage(), e);
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────
    public void remover(int id) {
        String sql = "DELETE FROM livro WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover livro: " + e.getMessage(), e);
        }
    }

    // ── Mapper ────────────────────────────────────────────────────────────
    private Livro mapear(ResultSet rs) throws SQLException {
        return new Livro(
            rs.getInt   ("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getInt   ("ano_publicacao"),
            rs.getString("categoria"),
            rs.getInt   ("quantidade_disponivel")
        );
    }
}