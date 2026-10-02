package model;

// Classe simples que representa um Professor da escola de musica
public class Professor {

    private int id;
    private String nome;
    private String telefone;
    private String especialidade;

    public Professor() {
    }

    public Professor(int id, String nome, String telefone, String especialidade) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.especialidade = especialidade;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return id + " - " + nome;
    }
}
