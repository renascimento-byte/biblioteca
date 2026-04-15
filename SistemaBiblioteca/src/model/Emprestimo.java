package model;

import java.util.Date;

public class Emprestimo {

    private Usuario usuario;
    private Livro   livro;
    private Date    dataRetirada;
    private Date    dataPrevista;
    private Date    dataDevolucaoReal;
    private String  status;

    public Emprestimo(Usuario usuario, Livro livro) {
        this.usuario      = usuario;
        this.livro        = livro;
        this.dataRetirada = new Date();
        this.dataPrevista = new Date(System.currentTimeMillis() + 14L * 24 * 60 * 60 * 1000);
        this.status       = "ATIVO";
    }

    public boolean verificarAtraso() {
        Date ref = dataDevolucaoReal != null ? dataDevolucaoReal : new Date();
        return ref.after(dataPrevista);
    }

    public void finalizarEmprestimo() {
        this.dataDevolucaoReal = new Date();
        this.status = verificarAtraso() ? "ATRASADO" : "FINALIZADO";
    }

    public Multa gerarMulta() {
        if (!verificarAtraso()) return null;
        long diffMs  = dataDevolucaoReal.getTime() - dataPrevista.getTime();
        int  dias    = (int) (diffMs / (1000 * 60 * 60 * 24));
        return new Multa(dias);
    }

    public Usuario getUsuario()           { return usuario; }
    public Livro   getLivro()             { return livro; }
    public Date    getDataRetirada()      { return dataRetirada; }
    public Date    getDataPrevista()      { return dataPrevista; }
    public Date    getDataDevolucaoReal() { return dataDevolucaoReal; }
    public String  getStatus()            { return status; }

    @Override
    public String toString() { return usuario.getNome() + " - " + livro.getTitulo() + " [" + status + "]"; }
}
