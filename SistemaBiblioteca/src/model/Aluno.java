package model;

public class Aluno extends Usuario {
    public Aluno(String nome, String matricula, String endereco) {
        super(nome, matricula, endereco, "Aluno");
    }
}
