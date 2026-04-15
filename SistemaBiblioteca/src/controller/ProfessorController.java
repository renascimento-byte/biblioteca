package controller;

import db.UsuarioDAO;
import model.Dados;
import model.Professor;
import model.Usuario;
import view.ProfessorView;

import javax.swing.*;
import java.util.ArrayList;

public class ProfessorController {

    private final ProfessorView view;
    private final Dados         dados;
    private final UsuarioDAO    dao = new UsuarioDAO();

    public ProfessorController(ProfessorView view, Dados dados) {
        this.view  = view;
        this.dados = dados;
        carregarTabela();
        view.btnAdicionar.addActionListener(e -> adicionar());
        view.btnEditar.addActionListener(e    -> editar());
        view.btnRemover.addActionListener(e   -> remover());
        view.btnLimpar.addActionListener(e    -> limparCampos());
        view.tabela.getSelectionModel().addListSelectionListener(e -> preencherCampos());
    }

    ArrayList<Professor> getProfessores() {
        ArrayList<Professor> lista = new ArrayList<>();
        for (Usuario u : dados.usuarios)
            if (u instanceof Professor) lista.add((Professor) u);
        return lista;
    }

    void adicionar() {
        try {
            Professor p = new Professor(
                view.campoNome.getText().trim(),
                view.campoMatricula.getText().trim(),
                view.campoEndereco.getText().trim()
            );
            dao.inserir(p);
            dados.recarregar();
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
        }
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um professor para editar."); return; }
        Professor p = getProfessores().get(linha);
        p.setNome(view.campoNome.getText().trim());
        p.setEndereco(view.campoEndereco.getText().trim());
        dao.atualizar(p);
        dados.recarregar();
        carregarTabela();
        limparCampos();
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um professor para remover."); return; }
        if (JOptionPane.showConfirmDialog(null, "Remover este professor?") == JOptionPane.YES_OPTION) {
            dao.removerPorMatricula(getProfessores().get(linha).getMatricula());
            dados.recarregar();
            carregarTabela();
            limparCampos();
        }
    }

    void preencherCampos() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) return;
        Professor p = getProfessores().get(linha);
        view.campoNome.setText(p.getNome());
        view.campoMatricula.setText(p.getMatricula());
        view.campoEndereco.setText(p.getEndereco());
    }

    void limparCampos() {
        view.campoNome.setText("");
        view.campoMatricula.setText("");
        view.campoEndereco.setText("");
        view.tabela.clearSelection();
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        for (Professor p : getProfessores())
            view.modelo.addRow(new Object[]{p.getNome(), p.getMatricula(), p.getEndereco()});
    }
}