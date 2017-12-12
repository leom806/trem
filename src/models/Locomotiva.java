package models;

import database.DB;
import database.DBObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Classe de objetos do tipo Locomotiva.
 * 
 * @author Breno Velasco e Leonardo Momente
 */
public class Locomotiva implements DBObject, ElementoComposicao{
    /** Inteiro que identifica classe da locomotiva. */
    protected int classe;
    /** Peso Máximo Rebocável. */
    protected double pmr;
    /** Tamanho da bitola. */
    protected double bitola;
    /** Comprimento em metros. */
    protected double comprimento;
    /** Descrição verbal da locomotiva. */
    protected String descricao;
    /** Conexao com o banco de dados, utilizando-se de metodos da classe DB, para pegar
    a instancia e a conexao. */
    private static Connection conn = DB.getInstance().getConnection();
    /** Statement para execucao de instrucoes SQL. */
    private Statement statement = null;
    /** Tabela de dados que representa o resultado de uma instrucao SQL. */
    private ResultSet set = null;
    /** Tipo StringBuilder com metodos para melhor manipulacao de Strings. */
    private StringBuilder sb = new StringBuilder();

    /**
     * Construtor padrao que recebe a classe de uma locomotiva.
     * 
     * @param classe
     *                 classe da locomotiva
     */
    public Locomotiva(int classe){
        this.classe = classe;
    }
    
    /**
     * Construtor especifico que recebe diversos parametros.
     * 
     * @param classe
     *                 classe da locomotiva
     * @param descricao
     *                    descricao verbal da locomotiva
     * @param pmr
     *              peso maximo rebocavel
     * @param bitola
     *                 comprimento da bitola
     * @param comprimento
     *                      comprimento em metros da locomotiva
     */
    public Locomotiva(int classe, String descricao, double pmr, double bitola, double comprimento) {                
        this.classe = classe;
        this.descricao = descricao;
        this.pmr = pmr;
        this.bitola = bitola;
        this.comprimento = comprimento;
    }

    /**
     * Metodo que sobrescreve toString(), retornando a locomotiva e suas caracteristicas.
     * 
     * @return string com os dados da locomotiva
     */
    @Override
    public String toString() {
        return  "Locomotiva {" + 
                "\n classe:       " + classe + 
                ",\n descricao:    " + descricao + 
                ",\n peso max reb: " + pmr + "t" +
                ",\n comprimento:  " + comprimento + "m" + 
                ",\n bitola:       " + bitola + "m";
    }
    
    /**
     * Metodo que retorna as propriedades da locomotiva.
     * 
     * @return string com as propriedades
     */
    public String getProperties(){
        
        sb.append(" ( ");
        sb.append(classe).append(",");
        sb.append("'").append(descricao).append("',");
        sb.append(pmr).append(",");
        sb.append(comprimento).append(",");
        sb.append(bitola);        
        sb.append(" ) ");
        
        return sb.toString();
    }
    
    /**
     * Metodo para insercao de parametros no VALUES para um INSERT INTO no SQL
     * 
     * @return string SQL
     */
    public String getSQLString(){
        return sb.append("descricao = '").append(getDescricao()).append("', ")
                .append("pmr = ")
                .append(getPeso()).append(", ")
                .append("comprimento = ")
                .append(getComprimento()).append(", ")
                .append("bitola = ")
                .append(getBitola()).append(" ")
                .append("WHERE classe=")
                .append(classe)
            .toString();
    }
    
    /**
     * Metodo que realiza um UPDATE no SQL para uma determinada locomotiva, afim de gravar seu estado atual.
     * 
     * @return boolean indicando se houve ou não gravação
     */
    @Override
    public boolean save() {
        try {                        
            statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO locomotivas VALUES "+this.getProperties());
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    /**
     * Metodo que realiza um DELETE no SQL para uma determinada locomotiva, afim de deleta-la.
     */
    @Override
    public void delete() {
        try {                        
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM locomotivas WHERE classe="+classe);
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
    }

    /**
     * Metodo que realiza um SELECT no SQL para uma determinada locomotiva, afim de le-la.
     * 
     * @return uma locomotiva contendo o resultado da consulta
     */
    @Override
    public ResultSet read() {
        try {                        
            statement = conn.createStatement();
            set = statement.executeQuery("SELECT * FROM locomotivas WHERE classe="+classe);
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
        return set;
    }

    /**
     * Metodo que realiza um UPDATE no SQL para uma determinada locomotiva, afim de atualiza-la/altera-la.
     */ 
    @Override
    public void update() {
        try {                        
            statement = conn.createStatement();
            statement.executeUpdate("UPDATE locomotivas SET "+getSQLString());
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
    }
    
    /**
     * Getter que busca classe
     * 
     * @return classe da locomotiva
     */
    public int getClasse() {
        return classe;
    }

    /**
     * Setter que estabelece classe
     * 
     * @param classe
     *                 classe da locomotiva
     */
    public void setClasse(int classe) {
        this.classe = classe;
    }

    /**
     * Getter que busca descricao
     * 
     * @return descricao da locomotiva
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Setter que estabelece descricao
     * 
     * @param descricao
     *                 descricao da locomotiva
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Getter que busca peso
     * 
     * @return peso da locomotiva
     */  
    public double getPeso() {
        return pmr;
    }

    /**
     * Setter que estabelece peso maximo rebocavel
     * 
     * @param pmr
     *                 peso maximo rebocavel da locomotiva
     */
    public void setPmr(double pmr) {
        this.pmr = pmr;
    }

    /**
     * Getter que busca bitola
     * 
     * @return bitola da locomotiva
     */  
    public double getBitola() {
        return bitola;
    }

    /**
     * Setter que estabelece bitola
     * 
     * @param bitola
     *                 bitola da locomotiva
     */
    public void setBitola(double bitola) {
        this.bitola = bitola;
    }

    /**
     * Getter que busca comprimento
     * 
     * @return comprimento da locomotiva
     */ 
    public double getComprimento() {
        return comprimento;
    }

    /**
     * Setter que estabelece comprimento
     * 
     * @param comprimento
     *                 comprimento da locomotiva
     */   
    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
    }
    
    /**
     * Metodo que busca todas as locomotivas.
     * 
     * @return todas as locomotivas
     */
    public static ArrayList<Locomotiva> getAll(){
        ArrayList<Locomotiva> response = new ArrayList<>();
        try {
            ResultSet locs = conn.createStatement().executeQuery("SELECT * FROM locomotivas");
            while(locs.next()){
                response.add(new Locomotiva(
                    locs.getInt("classe"),
                    locs.getString("descricao"),
                    locs.getDouble("pmr"),
                    locs.getDouble("bitola"),
                    locs.getDouble("comprimento")
                ));
            }
            return response;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível ler as locomotivas.");
        }
        return null;
    }
    
}