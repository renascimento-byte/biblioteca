package model;

public abstract class Usuario {

    private String nome;
    private String matricula;
    private String endereco;
    private String tipo;

    public Usuario(String nome, String matricula, String endereco, String tipo) {
        this.nome      = nome;
        this.matricula = matricula;
        this.endereco  = endereco;
        this.tipo      = tipo;
    }

    public String getNome()      { return nome; }
    public String getMatricula() { return matricula; }
    public String getEndereco()  { return endereco; }
    public String getTipo()      { return tipo; }

    public void setNome(String nome)           { this.nome = nome; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void setEndereco(String endereco)   { this.endereco = endereco; }

    @Override
    public String toString() { return nome + " (" + tipo + ")"; }
}
