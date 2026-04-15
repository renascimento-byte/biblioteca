package model;

import db.LivroDAO;
import db.UsuarioDAO;

import java.util.ArrayList;

/**
 * Dados agora é um cache em memória carregado do banco SQLite.
 * Os controllers continuam funcionando igual — só a fonte de dados mudou.
 */
public class Dados {

    public ArrayList<Usuario>    usuarios    = new ArrayList<>();
    public ArrayList<Livro>      livros      = new ArrayList<>();
    public ArrayList<Emprestimo> emprestimos = new ArrayList<>();
    public ArrayList<Reserva>    reservas    = new ArrayList<>();

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final LivroDAO   livroDAO   = new LivroDAO();

    public Dados() {
        recarregar();
    }

    /** Recarrega usuários e livros do banco (chame após qualquer CRUD). */
    public void recarregar() {
        usuarios = new ArrayList<>(usuarioDAO.listarTodos());
        livros   = new ArrayList<>(livroDAO.listarTodos());
        // emprestimos e reservas são gerenciados diretamente nos seus DAOs
        // e recarregados sob demanda pelos controllers de emprestimo/reserva
    }

    /** Próximo ID de livro — delega ao banco para consistência. */
    public int proximoIdLivro() {
        return livros.stream().mapToInt(Livro::getId).max().orElse(0) + 1;
    }
}