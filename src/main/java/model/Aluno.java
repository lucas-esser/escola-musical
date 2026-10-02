package model;

// Classe simples que representa um Aluno da escola de musica
public class Aluno {

    private int id;
    private String nome;
    private int idade;
    private String telefone;
    private String email;

    public Aluno() {
    }

    public Aluno(int id, String nome, int idade, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.telefone = telefone;
        this.email = email;
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

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Retorna true se o aluno tem menos de 18 anos
    public boolean ehMenorDeIdade() {
        return idade < 18;
    }

    @Override
    public String toString() {
        // usado para exibir o aluno dentro dos JComboBox das telas
        return id + " - " + nome;
    }
}
