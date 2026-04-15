
import db.ConexaoDB;
import db.DatabaseInit;
import model.Dados;
import controller.*;
import view.*;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // 1. Inicializa o banco (cria arquivo biblioteca.db + tabelas + dados iniciais)
        DatabaseInit.inicializar();

        SwingUtilities.invokeLater(() -> {

            // 2. Carrega cache de dados do banco
            Dados dados = new Dados();

            // 3. Monta as views
            AlunoView      alunoView      = new AlunoView();
            ProfessorView  professorView  = new ProfessorView();
            FuncionarioView funcView      = new FuncionarioView();
            LivroView      livroView      = new LivroView();
            EmprestimoView empView        = new EmprestimoView();
            ReservaView    reservaView    = new ReservaView();

            // 4. Liga os controllers (que conectam ao banco via DAO)
            new AlunoController     (alunoView,     dados);
            new ProfessorController (professorView, dados);
            new FuncionarioController(funcView,     dados);
            new LivroController     (livroView,     dados);
            EmprestimoController empCtrl = new EmprestimoController(empView,     dados);
            ReservaController    resCtrl = new ReservaController   (reservaView, dados);

            // 5. Monta a janela principal com abas
            JTabbedPane abas = new JTabbedPane();
            abas.addTab("Alunos",       alunoView);
            abas.addTab("Professores",  professorView);
            abas.addTab("Funcionários", funcView);
            abas.addTab("Livros",       livroView);
            abas.addTab("Empréstimos",  empView);
            abas.addTab("Reservas",     reservaView);

            // Recarrega combos ao abrir as abas de empréstimo/reserva
            abas.addChangeListener(e -> {
                int idx = abas.getSelectedIndex();
                if (idx == 4) empCtrl.recarregarCombos();
                if (idx == 5) resCtrl.recarregarCombos();
            });

            JFrame frame = new JFrame("Sistema de Biblioteca");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override public void windowClosing(java.awt.event.WindowEvent e) {
                    ConexaoDB.fechar();   // fecha conexão antes de sair
                    System.exit(0);
                }
            });
            frame.add(abas);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}