package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static view.UsuarioView.*;

public class ProfessorView extends JPanel {

    public DefaultTableModel modelo;
    public JTable tabela;

    public JTextField campoNome      = createField(14);
    public JTextField campoMatricula = createField(10);
    public JTextField campoEndereco  = createField(16);

    public JButton btnAdicionar = createButton("Adicionar", true);
    public JButton btnEditar    = createButton("Editar",    false);
    public JButton btnRemover   = createButton("Remover",   false);
    public JButton btnLimpar    = createButton("Limpar",    false);

    public ProfessorView() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_DARK);

        add(createHeader("GERENCIAR PROFESSORES", "Cadastro, edição e remoção de professores"), BorderLayout.NORTH);

        String[] colunas = {"Nome", "Matrícula", "Endereço"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = createTable(modelo);
        add(createScrollPane(tabela), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(8, 12, 4, 12),
                "DADOS DO PROFESSOR",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Georgia", Font.BOLD, 10),
                ACCENT)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        addField(form, gbc, "Nome",      campoNome,      0);
        addField(form, gbc, "Matrícula", campoMatricula, 1);
        addField(form, gbc, "Endereço",  campoEndereco,  2);

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

    private void addField(JPanel p, GridBagConstraints gbc, String label, JComponent field, int col) {
        gbc.gridx = col * 2; gbc.gridy = 0;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Georgia", Font.PLAIN, 10));
        l.setForeground(TEXT_MUTED);
        p.add(l, gbc);
        gbc.gridx = col * 2 + 1;
        p.add(field, gbc);
    }
}