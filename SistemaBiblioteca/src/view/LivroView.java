package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static view.UsuarioView.*;

public class LivroView extends JPanel {

    public DefaultTableModel modelo;
    public JTable tabela;

    public JTextField campoTitulo    = createField(14);
    public JTextField campoAutor     = createField(14);
    public JTextField campoAno       = createField(6);
    public JTextField campoCategoria = createField(10);
    public JTextField campoQtd       = createField(5);

    public JButton btnAdicionar = createButton("Adicionar", true);
    public JButton btnEditar    = createButton("Editar",    false);
    public JButton btnRemover   = createButton("Remover",   false);
    public JButton btnLimpar    = createButton("Limpar",    false);

    public LivroView() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_DARK);

        add(createHeader("ACERVO DE LIVROS", "Gerenciamento do catálogo da biblioteca"), BorderLayout.NORTH);

        String[] colunas = {"ID", "Título", "Autor", "Ano", "Categoria", "Qtd"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = createTable(modelo);

        // Coluna ID mais estreita
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(40);

        add(createScrollPane(tabela), BorderLayout.CENTER);

        // Formulário em grid 2 linhas
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(8, 12, 4, 12),
                "DADOS DO LIVRO",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Georgia", Font.BOLD, 10),
                ACCENT)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0
        addField(form, gbc, "Título",    campoTitulo,    0, 0);
        addField(form, gbc, "Autor",     campoAutor,     1, 0);
        addField(form, gbc, "Categoria", campoCategoria, 2, 0);
        // Linha 1
        addField(form, gbc, "Ano",       campoAno,       0, 1);
        addField(form, gbc, "Qtd",       campoQtd,       1, 1);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        botoes.setBackground(BG_CARD);
        botoes.add(btnAdicionar);
        botoes.add(btnEditar);
        botoes.add(btnRemover);
        botoes.add(btnLimpar);

        JPanel sul = new JPanel(new BorderLayout());
        sul.setBackground(BG_CARD);
        sul.add(form,   BorderLayout.CENTER);
        sul.add(botoes, BorderLayout.SOUTH);

        add(sul, BorderLayout.SOUTH);
    }

    private void addField(JPanel p, GridBagConstraints gbc, String label, JComponent field, int col, int row) {
        gbc.gridx = col * 2; gbc.gridy = row;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Georgia", Font.PLAIN, 10));
        l.setForeground(TEXT_MUTED);
        p.add(l, gbc);
        gbc.gridx = col * 2 + 1;
        p.add(field, gbc);
    }
}