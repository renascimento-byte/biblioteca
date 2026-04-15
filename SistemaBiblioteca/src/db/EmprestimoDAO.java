package db;

import model.Emprestimo;
import model.Livro;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    private final Connection con = ConexaoDB.getConexao();

    // ── CREATE ────────────────────────────────────────────────────────────
    public void inserir(Emprestimo emp, int usuarioId, int livroId) {
        String sql = """
            INSERT INTO emprestimo(usuario_id,livro_id,data_retirada,data_prevista,status)
            VALUES(?,?,?,?,?)
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt      (1, usuarioId);
            ps.setInt      (2, livroId);
            ps.setTimestamp(3, new Timestamp(emp.getDataRetirada().getTime()));
            ps.setTimestamp(4, new Timestamp(emp.getDataPrevista().getTime()));
            ps.setString   (5, emp.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir empréstimo: " + e.getMessage(), e);
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────
    public List<Emprestimo> listarTodos() {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = """
            SELECT e.*,
                   u.nome as u_nome, u.matricula, u.endereco, u.tipo,
                   l.titulo, l.autor, l.ano_publicacao, l.categoria, l.quantidade_disponivel
            FROM emprestimo e
            JOIN usuario u ON u.id = e.usuario_id
            JOIN livro   l ON l.id = e.livro_id
            ORDER BY e.id
        """;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empréstimos: " + e.getMessage(), e);
        }
        return lista;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
    public void finalizarEmprestimo(Emprestimo emp) {
        String sqlId = """
            SELECT e.id FROM emprestimo e
            JOIN usuario u ON u.id = e.usuario_id
            JOIN livro   l ON l.id = e.livro_id
            WHERE u.matricula=? AND l.titulo=? AND e.status='ATIVO'
            LIMIT 1
        """;
        try (PreparedStatement ps = con.prepareStatement(sqlId)) {
            ps.setString(1, emp.getUsuario().getMatricula());
            ps.setString(2, emp.getLivro().getTitulo());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return;
            int id = rs.getInt("id");

            String sqlUpd = "UPDATE emprestimo SET data_devolucao_real=?,status=? WHERE id=?";
            try (PreparedStatement pu = con.prepareStatement(sqlUpd)) {
                pu.setTimestamp(1, new Timestamp(
                    emp.getDataDevolucaoReal() != null
                        ? emp.getDataDevolucaoReal().getTime()
                        : System.currentTimeMillis()));
                pu.setString(2, emp.getStatus());
                pu.setInt   (3, id);
                pu.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao finalizar empréstimo: " + e.getMessage(), e);
        }
    }

    // ── Mapper ────────────────────────────────────────────────────────────
    private Emprestimo mapear(ResultSet rs) throws SQLException {
        Usuario u = criarUsuario(rs);
        Livro   l = new Livro(
            rs.getInt   ("livro_id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getInt   ("ano_publicacao"),
            rs.getString("categoria"),
            rs.getInt   ("quantidade_disponivel")
        );
        Emprestimo emp = new Emprestimo(u, l);
        String status = rs.getString("status");
        if (!status.equals("ATIVO")) emp.finalizarEmprestimo();
        return emp;
    }

    private Usuario criarUsuario(ResultSet rs) throws SQLException {
        String nome      = rs.getString("u_nome");
        String matricula = rs.getString("matricula");
        String endereco  = rs.getString("endereco");
        return switch (rs.getString("tipo")) {
            case "Professor"   -> new model.Professor(nome, matricula, endereco);
            case "Funcionario" -> new model.Funcionario(nome, matricula, endereco);
            default            -> new model.Aluno(nome, matricula, endereco);
        };
    }
}