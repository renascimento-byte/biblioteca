package controller;

import db.UsuarioDAO;
import model.Dados;
import model.Funcionario;
import model.Usuario;
import view.FuncionarioView;

import javax.swing.*;
import java.util.ArrayList;

public class FuncionarioController {

    private final FuncionarioView view;
    private final Dados           dados;
    private final UsuarioDAO      dao = new UsuarioDAO();

    public FuncionarioController(FuncionarioView view, Dados dados) {
        this.view  = view;
        this.dados = dados;
        carregarTabela();
        view.btnAdicionar.addActionListener(e -> adicionar());
        view.btnEditar.addActionListener(e    -> editar());
        view.btnRemover.addActionListener(e   -> remover());
        view.btnLimpar.addActionListener(e    -> limparCampos());
        view.tabela.getSelectionModel().addListSelectionListener(e -> preencherCampos());
    }

    ArrayList<Funcionario> getFuncionarios() {
        ArrayList<Funcionario> lista = new ArrayList<>();
        for (Usuario u : dados.usuarios)
            if (u instanceof Funcionario) lista.add((Funcionario) u);
        return lista;
    }

    void adicionar() {
        try {
            Funcionario f = new Funcionario(
                view.campoNome.getText().trim(),
                view.campoMatricula.getText().trim(),
                view.campoEndereco.getText().trim()
            );
            dao.inserir(f);
            dados.recarregar();
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
        }
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um funcionário para editar."); return; }
        Funcionario f = getFuncionarios().get(linha);
        f.setNome(view.campoNome.getText().trim());
        f.setEndereco(view.campoEndereco.getText().trim());
        dao.atualizar(f);
        dados.recarregar();
        carregarTabela();
        limparCampos();
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um funcionário para remover."); return; }
        if (JOptionPane.showConfirmDialog(null, "Remover este funcionário?") == JOptionPane.YES_OPTION) {
            dao.removerPorMatricula(getFuncionarios().get(linha).getMatricula());
            dados.recarregar();
            carregarTabela();
            limparCampos();
        }
    }

    void preencherCampos() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) return;
        Funcionario f = getFuncionarios().get(linha);
        view.campoNome.setText(f.getNome());
        view.campoMatricula.setText(f.getMatricula());
        view.campoEndereco.setText(f.getEndereco());
    }

    void limparCampos() {
        view.campoNome.setText("");
        view.campoMatricula.setText("");
        view.campoEndereco.setText("");
        view.tabela.clearSelection();
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        for (Funcionario f : getFuncionarios())
            view.modelo.addRow(new Object[]{f.getNome(), f.getMatricula(), f.getEndereco()});
    }
}