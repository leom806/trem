package models;

import database.DB;
import database.DBObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Locomotiva implements DBObject, ElementoComposicao{
    
    protected int classe;
    protected double pmr;
    protected double bitola;
    protected double comprimento;
    protected String descricao;
    
    private static Connection conn = DB.getInstance().getConnection();
    private Statement statement = null;
    private ResultSet set = null;
    private StringBuilder sb = new StringBuilder();

    public Locomotiva(int classe){
        this.classe = classe;
    }
    
    public Locomotiva(int classe, String descricao, double pmr, double bitola, double comprimento) {                
        this.classe = classe;
        this.descricao = descricao;
        this.pmr = pmr;
        this.bitola = bitola;
        this.comprimento = comprimento;
    }

    @Override
    public String toString() {
        return  "Locomotiva {" + 
                "\n classe:       " + classe + 
                ",\n descricao:    " + descricao + 
                ",\n peso max reb: " + pmr + "t" +
                ",\n comprimento:  " + comprimento + "m" + 
                ",\n bitola:       " + bitola + "m";
    }
    
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

    @Override
    public void delete() {
        try {                        
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM locomotivas WHERE classe="+classe);
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
    }

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

    @Override
    public void update() {
        try {                        
            statement = conn.createStatement();
            statement.executeUpdate("UPDATE locomotivas SET "+getSQLString());
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
    }
    
    /*
    Getters & Setters
    */

    public int getClasse() {
        return classe;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPeso() {
        return pmr;
    }

    public void setPmr(double pmr) {
        this.pmr = pmr;
    }

    public double getBitola() {
        return bitola;
    }

    public void setBitola(double bitola) {
        this.bitola = bitola;
    }

    public double getComprimento() {
        return comprimento;
    }

    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
    }
    
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