package controller;

import db.LivroDAO;
import model.Dados;
import model.Livro;
import view.LivroView;

import javax.swing.*;

public class LivroController {

    private final LivroView view;
    private final Dados     dados;
    private final LivroDAO  dao = new LivroDAO();

    public LivroController(LivroView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        carregarTabela();

        view.btnAdicionar.addActionListener(e -> adicionar());
        view.btnEditar.addActionListener(e    -> editar());
        view.btnRemover.addActionListener(e   -> remover());
        view.btnLimpar.addActionListener(e    -> limparCampos());

        view.tabela.getSelectionModel().addListSelectionListener(e -> preencherCampos());
    }

    void adicionar() {
        try {
            Livro l = new Livro(
                0, // ID será gerado pelo banco
                view.campoTitulo.getText().trim(),
                view.campoAutor.getText().trim(),
                Integer.parseInt(view.campoAno.getText().trim()),
                view.campoCategoria.getText().trim(),
                Integer.parseInt(view.campoQtd.getText().trim())
            );
            dao.inserir(l);
            dados.recarregar();
            carregarTabela();
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ano e Qtd devem ser números.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar: " + ex.getMessage());
        }
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um livro para editar."); return; }

        try {
            Livro l = dados.livros.get(linha);
            l.setTitulo(view.campoTitulo.getText().trim());
            l.setAutor(view.campoAutor.getText().trim());
            l.setAnoPublicacao(Integer.parseInt(view.campoAno.getText().trim()));
            l.setCategoria(view.campoCategoria.getText().trim());
            l.setQuantidade(Integer.parseInt(view.campoQtd.getText().trim()));
            dao.atualizar(l);
            dados.recarregar();
            carregarTabela();
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ano e Qtd devem ser números.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao editar: " + ex.getMessage());
        }
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um livro para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este livro?");
        if (confirm == JOptionPane.YES_OPTION) {
            dao.remover(dados.livros.get(linha).getId());
            dados.recarregar();
            carregarTabela();
            limparCampos();
        }
    }

    void preencherCampos() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) return;
        Livro l = dados.livros.get(linha);
        view.campoTitulo.setText(l.getTitulo());
        view.campoAutor.setText(l.getAutor());
        view.campoAno.setText(String.valueOf(l.getAnoPublicacao()));
        view.campoCategoria.setText(l.getCategoria());
        view.campoQtd.setText(String.valueOf(l.getQuantidadeDisponivel()));
    }

    void limparCampos() {
        view.campoTitulo.setText("");
        view.campoAutor.setText("");
        view.campoAno.setText("");
        view.campoCategoria.setText("");
        view.campoQtd.setText("");
        view.tabela.clearSelection();
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        for (Livro l : dados.livros) {
            view.modelo.addRow(new Object[]{
                l.getId(), l.getTitulo(), l.getAutor(),
                l.getAnoPublicacao(), l.getCategoria(), l.getQuantidadeDisponivel()
            });
        }
    }
}