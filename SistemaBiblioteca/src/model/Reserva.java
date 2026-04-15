package model;

import java.util.Date;

public class Reserva {

    private Usuario usuario;
    private Livro   livro;
    private Date    dataSolicitacao;
    private Date    dataExpiracao;
    private String  status;

    public Reserva(Usuario usuario, Livro livro) {
        this.usuario         = usuario;
        this.livro           = livro;
        this.dataSolicitacao = new Date();
        this.dataExpiracao   = new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000);
        this.status          = "ATIVA";
    }

    public boolean validarReserva() {
        return status.equals("ATIVA") && new Date().before(dataExpiracao);
    }

    public Usuario getUsuario()        { return usuario; }
    public Livro   getLivro()          { return livro; }
    public Date    getDataSolicitacao(){ return dataSolicitacao; }
    public Date    getDataExpiracao()  { return dataExpiracao; }
    public String  getStatus()         { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() { return usuario.getNome() + " - " + livro.getTitulo(); }
}
