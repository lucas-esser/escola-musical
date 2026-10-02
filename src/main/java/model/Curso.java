package model;

// Classe simples que representa um Curso (ex: Violao Iniciante).
// Cada curso usa um Instrumento e e ministrado por um Professor.
public class Curso {

    private int id;
    private String nome;
    private double valorMensalidade;
    private String diaHorario; // ex: "Segunda 14:00"
    private int idInstrumento;
    private int idProfessor;

    // Esses dois campos servem so para mostrar o nome na tabela da tela
    // (o DAO preenche com o resultado do JOIN)
    private String nomeInstrumento;
    private String nomeProfessor;

    public Curso() {
    }

    public Curso(int id, String nome, double valorMensalidade, String diaHorario,
                 int idInstrumento, int idProfessor) {
        this.id = id;
        this.nome = nome;
        this.valorMensalidade = valorMensalidade;
        this.diaHorario = diaHorario;
        this.idInstrumento = idInstrumento;
        this.idProfessor = idProfessor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorMensalidade() {
        return valorMensalidade;
    }

    public void setValorMensalidade(double valorMensalidade) {
        this.valorMensalidade = valorMensalidade;
    }

    public String getDiaHorario() {
        return diaHorario;
    }

    public void setDiaHorario(String diaHorario) {
        this.diaHorario = diaHorario;
    }

    public int getIdInstrumento() {
        return idInstrumento;
    }

    public void setIdInstrumento(int idInstrumento) {
        this.idInstrumento = idInstrumento;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getNomeInstrumento() {
        return nomeInstrumento;
    }

    public void setNomeInstrumento(String nomeInstrumento) {
        this.nomeInstrumento = nomeInstrumento;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    // Calcula quanto o aluno paga se fizer o curso por uma quantidade de meses
    public double calcularValorTotal(int meses) {
        return valorMensalidade * meses;
    }

    @Override
    public String toString() {
        return id + " - " + nome;
    }
}
