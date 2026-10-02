
CREATE DATABASE IF NOT EXISTS db_escola_musica;
USE db_escola_musica;


-- Tabela: tb_aluno

CREATE TABLE IF NOT EXISTS tb_aluno (
    id_aluno   INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    idade      INT          NOT NULL,
    telefone   VARCHAR(20)  NOT NULL,
    email      VARCHAR(100) NOT NULL
);


-- Tabela: tb_professor

CREATE TABLE IF NOT EXISTS tb_professor (
    id_professor   INT AUTO_INCREMENT PRIMARY KEY,
    nome           VARCHAR(100) NOT NULL,
    telefone       VARCHAR(20)  NOT NULL,
    especialidade  VARCHAR(80)  NOT NULL
);


-- Tabela: tb_instrumento

CREATE TABLE IF NOT EXISTS tb_instrumento (
    id_instrumento  INT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(60) NOT NULL,
    tipo            VARCHAR(20) NOT NULL   -- CORDAS, SOPRO, PERCUSSAO ou TECLAS
);


-- Tabela: tb_curso

CREATE TABLE IF NOT EXISTS tb_curso (
    id_curso           INT AUTO_INCREMENT PRIMARY KEY,
    nome               VARCHAR(100)  NOT NULL,
    valor_mensalidade  DECIMAL(10,2) NOT NULL,
    dia_horario        VARCHAR(40)   NOT NULL,
    id_instrumento     INT NOT NULL,
    id_professor       INT NOT NULL,
    CONSTRAINT fk_curso_instrumento
        FOREIGN KEY (id_instrumento) REFERENCES tb_instrumento(id_instrumento),
    CONSTRAINT fk_curso_professor
        FOREIGN KEY (id_professor) REFERENCES tb_professor(id_professor)
);


-- Tabela: tb_matricula

CREATE TABLE IF NOT EXISTS tb_matricula (
    id_matricula    INT AUTO_INCREMENT PRIMARY KEY,
    data_matricula  DATE        NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'ATIVA',   -- ATIVA, TRANCADA ou CANCELADA
    id_aluno        INT NOT NULL,
    id_curso        INT NOT NULL,
    CONSTRAINT fk_matricula_aluno
        FOREIGN KEY (id_aluno) REFERENCES tb_aluno(id_aluno),
    CONSTRAINT fk_matricula_curso
        FOREIGN KEY (id_curso) REFERENCES tb_curso(id_curso)
);


-- DADOS DE EXEMPLO (para testar o sistema)


INSERT INTO tb_aluno (nome, idade, telefone, email) VALUES
('Maria Silva', 15, '(51) 99999-1111', 'maria@email.com'),
('Joao Souza', 28, '(51) 99999-2222', 'joao@email.com'),
('Ana Pereira', 12, '(51) 99999-3333', 'ana@email.com');

INSERT INTO tb_professor (nome, telefone, especialidade) VALUES
('Carlos Mendes', '(51) 98888-0001', 'Violao e Guitarra'),
('Beatriz Lima', '(51) 98888-0002', 'Piano e Teclado'),
('Rafael Costa', '(51) 98888-0003', 'Bateria');

INSERT INTO tb_instrumento (nome, tipo) VALUES
('Violao', 'CORDAS'),
('Piano', 'TECLAS'),
('Bateria', 'PERCUSSAO'),
('Flauta', 'SOPRO');

INSERT INTO tb_curso (nome, valor_mensalidade, dia_horario, id_instrumento, id_professor) VALUES
('Violao Iniciante', 150.00, 'Segunda 14:00', 1, 1),
('Piano Basico', 200.00, 'Terca 15:00', 2, 2),
('Bateria para Jovens', 180.00, 'Quinta 16:00', 3, 3);

INSERT INTO tb_matricula (data_matricula, status, id_aluno, id_curso) VALUES
(CURDATE(), 'ATIVA', 1, 1),
(CURDATE(), 'ATIVA', 2, 2),
(CURDATE(), 'TRANCADA', 3, 3);
