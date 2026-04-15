package controller;

import db.ReservaDAO;
import db.UsuarioDAO;
import model.*;
import view.ReservaView;

import javax.swing.*;
import java.util.List;

public class ReservaController {

    private final ReservaView view;
    private final Dados       dados;
    private final ReservaDAO  reservaDAO = new ReservaDAO();
    private final UsuarioDAO  userDAO    = new UsuarioDAO();

    private List<Reserva> listaAtual;

    public ReservaController(ReservaView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        recarregarCombos();
        carregarTabela();

        view.btnReservar.addActionListener(e -> reservar());
        view.btnCancelar.addActionListener(e -> cancelar());
    }

    public void recarregarCombos() {
        dados.recarregar();
        view.cbUsuario.removeAllItems();
        view.cbLivro.removeAllItems();
        for (Usuario u : dados.usuarios) view.cbUsuario.addItem(u);
        for (Livro l   : dados.livros)   view.cbLivro.addItem(l);
    }

    void reservar() {
        Usuario u = (Usuario) view.cbUsuario.getSelectedItem();
        Livro   l = (Livro)   view.cbLivro.getSelectedItem();
        if (u == null || l == null) return;

        Reserva r = new Reserva(u, l);
        int usuarioId = userDAO.buscarIdPorMatricula(u.getMatricula());
        reservaDAO.inserir(r, usuarioId, l.getId());

        carregarTabela();
        JOptionPane.showMessageDialog(null, "Reserva feita! Válida por 7 dias.");
    }

    void cancelar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione uma reserva para cancelar."); return; }

        Reserva r = listaAtual.get(linha);
        if (!r.getStatus().equals("ATIVA")) {
            JOptionPane.showMessageDialog(null, "Esta reserva já foi cancelada.");
            return;
        }

        r.setStatus("CANCELADA");
        reservaDAO.cancelar(r);
        carregarTabela();
        JOptionPane.showMessageDialog(null, "Reserva cancelada.");
    }

    void carregarTabela() {
        listaAtual = reservaDAO.listarTodas();
        view.modelo.setRowCount(0);
        for (Reserva r : listaAtual) {
            view.modelo.addRow(new Object[]{
                r.getUsuario().getNome(),
                r.getLivro().getTitulo(),
                r.getDataSolicitacao(),
                r.getDataExpiracao(),
                r.getStatus()
            });
        }
    }
}