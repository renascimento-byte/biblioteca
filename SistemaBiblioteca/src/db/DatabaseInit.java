package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Inicializa o schema do banco MySQL na primeira execução.
 * Chamado uma vez no Main, antes de instanciar qualquer DAO.
 */
public class DatabaseInit {

    /** Cria o banco 'biblioteca' caso ainda não exista. */
    private static void criarBancoSeNecessario() {
        String urlSemBanco = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        try (Connection con = DriverManager.getConnection(urlSemBanco, "root", "");
             Statement  st  = con.createStatement()) {
            st.execute("CREATE DATABASE IF NOT EXISTS biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println("[DB] Banco 'biblioteca' verificado/criado.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar banco: " + e.getMessage(), e);
        }
    }

    public static void inicializar() {
        criarBancoSeNecessario();

        Connection con = ConexaoDB.getConexao();
        try (Statement st = con.createStatement()) {

            // ── Tabela de usuários ────────────────────────────────────────────
            st.execute("""
                CREATE TABLE IF NOT EXISTS usuario (
                    id        INT          PRIMARY KEY AUTO_INCREMENT,
                    nome      VARCHAR(150) NOT NULL,
                    matricula VARCHAR(20)  NOT NULL UNIQUE,
                    endereco  VARCHAR(255),
                    tipo      ENUM('Aluno','Professor','Funcionario') NOT NULL
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);

            // ── Tabela de livros ──────────────────────────────────────────────
            st.execute("""
                CREATE TABLE IF NOT EXISTS livro (
                    id                    INT          PRIMARY KEY AUTO_INCREMENT,
                    titulo                VARCHAR(255) NOT NULL,
                    autor                 VARCHAR(150) NOT NULL,
                    ano_publicacao        INT,
                    categoria             VARCHAR(100),
                    quantidade_disponivel INT          NOT NULL DEFAULT 0
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);

            // ── Tabela de empréstimos ─────────────────────────────────────────
            st.execute("""
                CREATE TABLE IF NOT EXISTS emprestimo (
                    id                  INT      PRIMARY KEY AUTO_INCREMENT,
                    usuario_id          INT      NOT NULL,
                    livro_id            INT      NOT NULL,
                    data_retirada       DATETIME NOT NULL,
                    data_prevista       DATETIME NOT NULL,
                    data_devolucao_real DATETIME,
                    status              ENUM('ATIVO','FINALIZADO','ATRASADO') NOT NULL DEFAULT 'ATIVO',
                    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT,
                    FOREIGN KEY (livro_id)   REFERENCES livro(id)   ON DELETE RESTRICT
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);

            // ── Tabela de reservas ────────────────────────────────────────────
            st.execute("""
                CREATE TABLE IF NOT EXISTS reserva (
                    id               INT      PRIMARY KEY AUTO_INCREMENT,
                    usuario_id       INT      NOT NULL,
                    livro_id         INT      NOT NULL,
                    data_solicitacao DATETIME NOT NULL,
                    data_expiracao   DATETIME NOT NULL,
                    status           ENUM('ATIVA','CANCELADA','EXPIRADA') NOT NULL DEFAULT 'ATIVA',
                    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT,
                    FOREIGN KEY (livro_id)   REFERENCES livro(id)   ON DELETE RESTRICT
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);

            // ── Tabela de multas ──────────────────────────────────────────────
            st.execute("""
                CREATE TABLE IF NOT EXISTS multa (
                    id            INT    PRIMARY KEY AUTO_INCREMENT,
                    emprestimo_id INT    NOT NULL,
                    dias_atraso   INT    NOT NULL,
                    valor         DOUBLE NOT NULL,
                    FOREIGN KEY (emprestimo_id) REFERENCES emprestimo(id) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);

            inserirDadosIniciaisSeVazio(st);
            System.out.println("[DB] Schema inicializado com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar banco: " + e.getMessage(), e);
        }
    }

    private static void inserirDadosIniciaisSeVazio(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM usuario");
        rs.next();
        if (rs.getInt(1) > 0) return;

        //st.execute("INSERT INTO usuario(nome,matricula,endereco,tipo) VALUES('Joao Silva','A001','Rua A, 10','Aluno')");
        //st.execute("INSERT INTO usuario(nome,matricula,endereco,tipo) VALUES('Ana Souza','A002','Rua B, 22','Aluno')");
        //st.execute("INSERT INTO usuario(nome,matricula,endereco,tipo) VALUES('Prof. Maria','P001','Av. C, 5','Professor')");
        //st.execute("INSERT INTO usuario(nome,matricula,endereco,tipo) VALUES('Carlos','F001','Rua D, 3','Funcionario')");

        //st.execute("INSERT INTO livro(titulo,autor,ano_publicacao,categoria,quantidade_disponivel) VALUES('Clean Code','Robert Martin',2008,'Tecnologia',3)");
        //st.execute("INSERT INTO livro(titulo,autor,ano_publicacao,categoria,quantidade_disponivel) VALUES('Dom Casmurro','Machado de Assis',1899,'Literatura',5)");
        //st.execute("INSERT INTO livro(titulo,autor,ano_publicacao,categoria,quantidade_disponivel) VALUES('O Hobbit','Tolkien',1937,'Fantasia',2)");

        //System.out.println("[DB] Dados iniciais inseridos.");
    }
}