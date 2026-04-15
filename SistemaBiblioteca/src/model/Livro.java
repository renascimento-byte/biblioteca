package model;

public class Livro {

    private int    id;
    private String titulo;
    private String autor;
    private int    anoPublicacao;
    private String categoria;
    private int    quantidadeDisponivel;

    public Livro(int id, String titulo, String autor, int anoPublicacao, String categoria, int quantidadeDisponivel) {
        this.id                   = id;
        this.titulo               = titulo;
        this.autor                = autor;
        this.anoPublicacao        = anoPublicacao;
        this.categoria            = categoria;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public boolean verificarDisponibilidade() { return quantidadeDisponivel > 0; }
    public void diminuirQuantidade()          { quantidadeDisponivel--; }
    public void aumentarQuantidade()          { quantidadeDisponivel++; }

    public int    getId()                   { return id; }
    public String getTitulo()               { return titulo; }
    public String getAutor()                { return autor; }
    public int    getAnoPublicacao()        { return anoPublicacao; }
    public String getCategoria()            { return categoria; }
    public int    getQuantidadeDisponivel() { return quantidadeDisponivel; }

    public void setTitulo(String titulo)            { this.titulo = titulo; }
    public void setAutor(String autor)              { this.autor = autor; }
    public void setAnoPublicacao(int ano)           { this.anoPublicacao = ano; }
    public void setCategoria(String categoria)      { this.categoria = categoria; }
    public void setQuantidade(int qtd)              { this.quantidadeDisponivel = qtd; }

    @Override
    public String toString() { return titulo + " - " + autor; }
}
