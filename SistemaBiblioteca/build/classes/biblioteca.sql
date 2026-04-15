CREATE DATABASE IF NOT EXISTS biblioteca
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE biblioteca;

CREATE TABLE IF NOT EXISTS usuario (
    id        INT          PRIMARY KEY AUTO_INCREMENT,
    nome      VARCHAR(150) NOT NULL,
    matricula VARCHAR(20)  NOT NULL UNIQUE,
    endereco  VARCHAR(255),
    tipo      ENUM('Aluno','Professor','Funcionario') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS livro (
    id                    INT          PRIMARY KEY AUTO_INCREMENT,
    titulo                VARCHAR(255) NOT NULL,
    autor                 VARCHAR(150) NOT NULL,
    ano_publicacao        INT,
    categoria             VARCHAR(100),
    quantidade_disponivel INT          NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS reserva (
    id               INT      PRIMARY KEY AUTO_INCREMENT,
    usuario_id       INT      NOT NULL,
    livro_id         INT      NOT NULL,
    data_solicitacao DATETIME NOT NULL,
    data_expiracao   DATETIME NOT NULL,
    status           ENUM('ATIVA','CANCELADA','EXPIRADA') NOT NULL DEFAULT 'ATIVA',
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT,
    FOREIGN KEY (livro_id)   REFERENCES livro(id)   ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS multa (
    id            INT    PRIMARY KEY AUTO_INCREMENT,
    emprestimo_id INT    NOT NULL,
    dias_atraso   INT    NOT NULL,
    valor         DOUBLE NOT NULL,
    FOREIGN KEY (emprestimo_id) REFERENCES emprestimo(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- Usuários
INSERT INTO usuario (nome, matricula, endereco, tipo) VALUES
    ('Munir',   '112322', 'Rua Arujá, 10',  'Aluno'),
    ('Luiza',    '112311', 'Rua Guararema, 22',  'Aluno'),
    ('Renan',    '112333', 'Rua Biritiba, 22',  'Aluno'),
    ('Prof. Maria',  'P001', 'Av. C, 5',   'Professor'),
    ('Carlos',       'F001', 'Rua D, 3',   'Funcionario');

-- Livros
INSERT INTO livro (titulo, autor, ano_publicacao, categoria, quantidade_disponivel) VALUES
    ('Clean Code',   'Robert Martin',    2008, 'Tecnologia', 3),
    ('Dom Casmurro', 'Machado de Assis', 1899, 'Literatura', 5),
    ('A Empregada', 'Freida McFadden', 2023, 'Suspense', 3),
    ('O Hobbit',     'Tolkien',          1937, 'Fantasia',   2);