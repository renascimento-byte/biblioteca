package controller;

import db.UsuarioDAO;
import model.Aluno;
import model.Dados;
import model.Usuario;
import view.AlunoView;

import javax.swing.*;
import java.util.ArrayList;

public class AlunoController {

    private final AlunoView   view;
    private final Dados       dados;
    private final UsuarioDAO  dao = new UsuarioDAO();

    public AlunoController(AlunoView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        carregarTabela();

        view.btnAdicionar.addActionListener(e -> adicionar());
        view.btnEditar.addActionListener(e    -> editar());
        view.btnRemover.addActionListener(e   -> remover());
        view.btnLimpar.addActionListener(e    -> limparCampos());

        view.tabela.getSelectionModel().addListSelectionListener(e -> preencherCampos());
    }

    ArrayList<Aluno> getAlunos() {
        ArrayList<Aluno> lista = new ArrayList<>();
        for (Usuario u : dados.usuarios)
            if (u instanceof Aluno) lista.add((Aluno) u);
        return lista;
    }

    void adicionar() {
        String nome = view.campoNome.getText().trim();
        String mat  = view.campoMatricula.getText().trim();
        String end  = view.campoEndereco.getText().trim();
        if (nome.isEmpty() || mat.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nome e matrícula são obrigatórios.");
            return;
        }
        try {
            Aluno a = new Aluno(nome, mat, end);
            dao.inserir(a);
            dados.recarregar();
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar: " + ex.getMessage());
        }
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um aluno para editar."); return; }

        Aluno a = getAlunos().get(linha);
        a.setNome(view.campoNome.getText().trim());
        a.setEndereco(view.campoEndereco.getText().trim());
        dao.atualizar(a);
        dados.recarregar();
        carregarTabela();
        limparCampos();
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um aluno para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este aluno?");
        if (confirm == JOptionPane.YES_OPTION) {
            dao.removerPorMatricula(getAlunos().get(linha).getMatricula());
            dados.recarregar();
            carregarTabela();
            limparCampos();
        }
    }

    void preencherCampos() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) return;
        Aluno a = getAlunos().get(linha);
        view.campoNome.setText(a.getNome());
        view.campoMatricula.setText(a.getMatricula());
        view.campoEndereco.setText(a.getEndereco());
    }

    void limparCampos() {
        view.campoNome.setText("");
        view.campoMatricula.setText("");
        view.campoEndereco.setText("");
        view.tabela.clearSelection();
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        for (Aluno a : getAlunos())
            view.modelo.addRow(new Object[]{a.getNome(), a.getMatricula(), a.getEndereco()});
    }
}