package ucan.edu.academic.utils;

public class Sitio
{
    private String nome, pai, avo;

    public Sitio(String nome)
    {
        this.nome = nome;
    }

    public Sitio(String nome, String pai)
    {
        this.nome = nome;
        this.pai = pai;
    }

    public Sitio(String nome, String pai, String avo)
    {
        this.nome = nome;
        this.pai = pai;
        this.avo = avo;
    }

    @Override
    public String toString()
    {
        return "{" 
            + ", nome =" + nome
            + ", pai =" + pai
            + ", avo =" + avo
            + '}';
    }

    // Getters

    public String getNome()
    {
        return nome;
    }

    public String getPai()
    {
        return pai;
    }

    public String getAvo()
    {
        return avo;
    }
    

}