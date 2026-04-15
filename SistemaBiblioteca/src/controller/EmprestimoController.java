package controller;

import db.EmprestimoDAO;
import db.LivroDAO;
import db.MultaDAO;
import db.UsuarioDAO;
import model.*;
import view.EmprestimoView;

import javax.swing.*;
import java.util.List;

public class EmprestimoController {

    private final EmprestimoView view;
    private final Dados          dados;
    private final EmprestimoDAO  empDAO   = new EmprestimoDAO();
    private final LivroDAO       livroDAO = new LivroDAO();
    private final UsuarioDAO     userDAO  = new UsuarioDAO();
    private final MultaDAO       multaDAO = new MultaDAO();

    // Cache local dos empréstimos exibidos na tabela (para mapear linha → objeto)
    private List<Emprestimo> listaAtual;

    public EmprestimoController(EmprestimoView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        recarregarCombos();
        carregarTabela();

        view.btnEmprestar.addActionListener(e -> emprestar());
        view.btnDevolver.addActionListener(e  -> devolver());
    }

    public void recarregarCombos() {
        dados.recarregar();
        view.cbUsuario.removeAllItems();
        view.cbLivro.removeAllItems();
        for (Usuario u : dados.usuarios) view.cbUsuario.addItem(u);
        for (Livro l   : dados.livros)   view.cbLivro.addItem(l);
    }

    void emprestar() {
        Usuario u = (Usuario) view.cbUsuario.getSelectedItem();
        Livro   l = (Livro)   view.cbLivro.getSelectedItem();
        if (u == null || l == null) return;

        if (!l.verificarDisponibilidade()) {
            JOptionPane.showMessageDialog(null, "Livro indisponível!");
            return;
        }

        // Persiste no banco
        Emprestimo emp = new Emprestimo(u, l);
        int usuarioId = userDAO.buscarIdPorMatricula(u.getMatricula());
        empDAO.inserir(emp, usuarioId, l.getId());

        // Atualiza estoque no banco e no cache
        l.diminuirQuantidade();
        livroDAO.atualizarQuantidade(l.getId(), l.getQuantidadeDisponivel());

        dados.recarregar();
        carregarTabela();
        JOptionPane.showMessageDialog(null, "Empréstimo realizado!");
    }

    void devolver() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um empréstimo na tabela."); return; }

        Emprestimo emp = listaAtual.get(linha);
        if (!emp.getStatus().equals("ATIVO")) {
            JOptionPane.showMessageDialog(null, "Este empréstimo já foi finalizado.");
            return;
        }

        emp.finalizarEmprestimo();
        empDAO.finalizarEmprestimo(emp);

        // Atualiza estoque
        Livro l = emp.getLivro();
        l.aumentarQuantidade();
        livroDAO.atualizarQuantidade(l.getId(), l.getQuantidadeDisponivel());

        Multa multa = emp.gerarMulta();
        if (multa != null) {
            multaDAO.inserir(multa, emp.getUsuario().getMatricula());
            JOptionPane.showMessageDialog(null, "Devolvido com atraso!\n" + multa);
        } else {
            JOptionPane.showMessageDialog(null, "Devolvido com sucesso!");
        }

        dados.recarregar();
        carregarTabela();
    }

    void carregarTabela() {
        listaAtual = empDAO.listarTodos();
        view.modelo.setRowCount(0);
        for (Emprestimo e : listaAtual) {
            view.modelo.addRow(new Object[]{
                e.getUsuario().getNome(),
                e.getLivro().getTitulo(),
                e.getDataRetirada(),
                e.getDataPrevista(),
                e.getStatus()
            });
        }
    }
}