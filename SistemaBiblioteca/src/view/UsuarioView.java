package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class UsuarioView extends JPanel {

    // Paleta de cores
    static final Color BG_DARK      = new Color(15, 17, 23);
    static final Color BG_CARD      = new Color(22, 26, 35);
    static final Color BG_ROW_ALT   = new Color(28, 33, 44);
    static final Color ACCENT       = new Color(212, 163, 75);
    static final Color ACCENT_DIM   = new Color(150, 110, 45);
    static final Color TEXT_PRIMARY = new Color(230, 225, 210);
    static final Color TEXT_MUTED   = new Color(130, 125, 115);
    static final Color BORDER_COLOR = new Color(40, 46, 60);
    static final Color BTN_ADD_BG   = new Color(212, 163, 75);
    static final Color BTN_ADD_FG   = new Color(15, 17, 23);

    static final Font FONT_TITLE  = new Font("Georgia", Font.BOLD, 13);
    static final Font FONT_LABEL  = new Font("Serif", Font.PLAIN, 12);
    static final Font FONT_FIELD  = new Font("Monospaced", Font.PLAIN, 12);
    static final Font FONT_HEADER = new Font("Georgia", Font.BOLD, 11);
    static final Font FONT_BTN    = new Font("Georgia", Font.BOLD, 12);

    public DefaultTableModel modelo;
    public JTextField campoNome      = createField(14);
    public JTextField campoMatricula = createField(8);
    public JComboBox<String> cbTipo  = new JComboBox<>(new String[]{"Aluno", "Professor", "Funcionario"});
    public JButton btnAdicionar      = createButton("Adicionar", true);

    public UsuarioView() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_DARK);

        // Header
        JPanel header = createHeader("GESTÃO DE USUÁRIOS", "Cadastro e listagem de todos os usuários do sistema");
        add(header, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Nome", "Matrícula", "Tipo"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = createTable(modelo);
        JScrollPane scroll = createScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        // Rodapé form
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 14));
        rodape.setBackground(BG_CARD);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        styleCombo(cbTipo);
        rodape.add(createLabeledField("Nome", campoNome));
        rodape.add(createLabeledField("Matrícula", campoMatricula));
        rodape.add(createLabeledField("Tipo", cbTipo));
        rodape.add(btnAdicionar);

        add(rodape, BorderLayout.SOUTH);
    }

    // ── helpers estáticos reutilizáveis ──────────────────────────────────

    static JTextField createField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(new Color(30, 35, 48));
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT);
        f.setFont(FONT_FIELD);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return f;
    }

    static JButton createButton(String text, boolean primary) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = primary
                    ? (getModel().isRollover() ? ACCENT_DIM : BTN_ADD_BG)
                    : (getModel().isRollover() ? new Color(45,52,68) : new Color(35,41,55));
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(FONT_BTN);
        b.setForeground(primary ? BTN_ADD_FG : TEXT_PRIMARY);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
        return b;
    }

    static JTable createTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setBackground(BG_DARK);
        t.setForeground(TEXT_PRIMARY);
        t.setFont(FONT_FIELD);
        t.setRowHeight(32);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(212, 163, 75, 55));
        t.setSelectionForeground(TEXT_PRIMARY);
        t.getTableHeader().setFont(FONT_HEADER);
        t.getTableHeader().setBackground(BG_CARD);
        t.getTableHeader().setForeground(ACCENT);
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_DIM));
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                setBackground(sel ? new Color(212,163,75,55) : (row % 2 == 0 ? BG_DARK : BG_ROW_ALT));
                setForeground(sel ? new Color(255,225,150) : TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setFont(FONT_FIELD);
                return this;
            }
        });
        return t;
    }

    static JScrollPane createScrollPane(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBackground(BG_DARK);
        sp.getViewport().setBackground(BG_DARK);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        return sp;
    }

    static JPanel createHeader(String title, String subtitle) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(212, 163, 75, 18));
                g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                // decorative line
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(16, getHeight() - 1, 60, getHeight() - 1);
                g2.dispose();
            }
        };
        p.setLayout(new BorderLayout());
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Georgia", Font.BOLD, 16));
        lTitle.setForeground(ACCENT);

        JLabel lSub = new JLabel(subtitle);
        lSub.setFont(new Font("Serif", Font.ITALIC, 11));
        lSub.setForeground(TEXT_MUTED);

        p.add(lTitle, BorderLayout.NORTH);
        p.add(lSub,   BorderLayout.SOUTH);
        return p;
    }

    static JPanel createLabeledField(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setBackground(BG_CARD);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Georgia", Font.PLAIN, 10));
        l.setForeground(TEXT_MUTED);
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    static void styleCombo(JComboBox<?> cb) {
        cb.setBackground(new Color(30, 35, 48));
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_FIELD);
        cb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object val,
                    int idx, boolean sel, boolean foc) {
                super.getListCellRendererComponent(list, val, idx, sel, foc);
                setBackground(sel ? new Color(50, 58, 78) : new Color(30, 35, 48));
                setForeground(TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
    }
}