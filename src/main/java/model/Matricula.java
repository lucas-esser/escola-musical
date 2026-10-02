package model;

import java.time.LocalDate;

// Classe simples que representa a Matricula de um Aluno em um Curso
public class Matricula {

    public static final String ATIVA = "ATIVA";
    public static final String TRANCADA = "TRANCADA";
    public static final String CANCELADA = "CANCELADA";

    private int id;
    private LocalDate dataMatricula;
    private String status;
    private int idAluno;
    private int idCurso;

    // Servem so para mostrar os nomes na tabela da tela (preenchidos pelo DAO)
    private String nomeAluno;
    private String nomeCurso;

    public Matricula() {
        this.dataMatricula = LocalDate.now();
        this.status = ATIVA;
    }

    public Matricula(int id, int idAluno, int idCurso) {
        this();
        this.id = id;
        this.idAluno = idAluno;
        this.idCurso = idCurso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public boolean estaAtiva() {
        return ATIVA.equals(status);
    }

    public void trancar() {
        this.status = TRANCADA;
    }

    public void cancelar() {
        this.status = CANCELADA;
    }

    public void reativar() {
        this.status = ATIVA;
    }

    @Override
    public String toString() {
        return id + " - " + nomeAluno + " / " + nomeCurso;
    }
}
