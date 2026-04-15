package model;

public class Funcionario extends Usuario {
    public Funcionario(String nome, String matricula, String endereco) {
        super(nome, matricula, endereco, "Funcionario");
    }
}
