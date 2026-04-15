package view;

import model.Livro;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static view.UsuarioView.*;

public class EmprestimoView extends JPanel {

    public DefaultTableModel modelo;
    public JTable tabela;

    public JComboBox<Usuario> cbUsuario = new JComboBox<>();
    public JComboBox<Livro>   cbLivro   = new JComboBox<>();

    public JButton btnEmprestar = createButton("Emprestar",          true);
    public JButton btnDevolver  = createButton("Devolver selecionado", false);

    public EmprestimoView() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_DARK);

        add(createHeader("EMPRÉSTIMOS", "Registre e acompanhe os empréstimos de livros"), BorderLayout.NORTH);

        String[] colunas = {"Usuário", "Livro", "Retirada", "Prev. Devolução", "Status"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = createTable(modelo);

        // Status colorido
        tabela.getColumnModel().getColumn(4).setCellRenderer(
            new javax.swing.table.DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    String s = val == null ? "" : val.toString();
                    setForeground(s.contains("Atrasado") ? new Color(220, 80, 80)
                                : s.contains("Devolvido") ? new Color(100, 200, 120)
                                : ACCENT);
                    setBackground(sel ? new Color(212,163,75,55) : (row%2==0 ? BG_DARK : BG_ROW_ALT));
                    setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    return this;
                }
            });

        add(createScrollPane(tabela), BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        rodape.setBackground(BG_CARD);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        styleCombo(cbUsuario);
        styleCombo(cbLivro);
        cbUsuario.setPreferredSize(new Dimension(160, 30));
        cbLivro.setPreferredSize(new Dimension(160, 30));

        rodape.add(createLabeledField("Usuário", cbUsuario));
        rodape.add(createLabeledField("Livro",   cbLivro));
        rodape.add(btnEmprestar);
        rodape.add(btnDevolver);

        add(rodape, BorderLayout.SOUTH);
    }
}