package models;


import database.DB;
import database.DBObject;

import helpers.Helper;
import static helpers.Helper.toUpper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JOptionPane;


/**
 * Classe de objetos do tipo Vagão.
 * 
 * @author Leonardo Momente
 */
public class Vagao implements Comparator, DBObject, ElementoComposicao{
    
    /**
     * 3 letras que indicam o tipo, subtipo e peso máximo admissível/bitola do
     * vagão.
     */
    protected String letras;  
    
    /**
     * Tipo do vagão.
     */
    protected Tipo tipo;
    
    /**
     * Subtipo do vagão.
     */
    protected Subtipo subtipo;
    
    /**
     * Peso máximo admissível.
     */
    protected double peso;
    
    /**
     * 6 algarismos de identificação.
     */
    protected String id;                    
    
    /**
     * Dígito verificador.
     */
    protected int digito;                   
    
    /**
     * Comprimento do vagão dado em metros.
     */
    protected double comprimento;           
    
    /**
     * Tamanho da bitola, também em metros.
     */
    protected double bitola;                
    
    /**
     * Proprietário do vagão.
     */
    protected String proprietario;   
    
    /**
     * Objeto auxiliar.
     */
    private final Helper helper = Helper.getInstance();
    
    
    private static Connection conn = DB.getInstance().getConnection();
    private Statement statement = null;
    private ResultSet set = null;
    private StringBuilder sb = new StringBuilder();
    
    
    /**
     * Construtor padrão que verifica as letras e instancia um objeto Vagao
     * se não houver irregularidades.
     * 
     * @param letras
     * @param id
     * @param digito
     * @throws RuntimeException 
     */
    public Vagao(String letras, String id, int digito) throws RuntimeException{                                
        if(letras != null && id != null){
            setLetras(letras);
            setId(id);
            setDigito(digito);                                                                               
        }else{
            throw new RuntimeException("Vagão não pôde ser instânciado");
        }                
    } // Fim construtor

    @Override
    public String toString() {
        return  "Vagao {" + 
                "\n letras:       " + letras + 
                ",\n tipo:         " + tipo + 
                ",\n subtipo:      " + subtipo + 
                ",\n peso:         " + peso + "t" +
                ",\n id:           " + id + 
                ",\n digito:       " + digito + 
                ",\n comprimento:  " + comprimento + "m" + 
                ",\n bitola:       " + bitola + "m" +
                ",\n proprietario: " + proprietario + "\n}\n";
    }

    @Override
    public int compare(Object o1, Object o2) {
        if (!(o1 instanceof Vagao && o2 instanceof Vagao)){
            throw new RuntimeException("Impossível comparar objetos!");
        }
        Vagao v1 = (Vagao) o1;
        Vagao v2 = (Vagao) o2;
        
        if(v1.tipo.getNum() > v2.tipo.getNum()){
            return  (v1.subtipo.getNum() > v2.subtipo.getNum()) ?  1 :
                    (v1.subtipo.getNum() < v2.subtipo.getNum()) ? -1 :
                    0;
        }else if (v1.tipo.getNum() < v2.tipo.getNum()){
            return  (v1.subtipo.getNum() > v2.subtipo.getNum()) ?  1 :
                    (v1.subtipo.getNum() < v2.subtipo.getNum()) ? -1 :
                    0;
        }else{
            return  (v1.subtipo.getNum() > v2.subtipo.getNum()) ?  1 :
                    (v1.subtipo.getNum() < v2.subtipo.getNum()) ? -1 :
                    0;            
        }        
    }
    
    public String getProperties(){        
        sb.append(" ( ");
        sb.append("'").append(id).append("',"); 
        sb.append("'").append(letras).append("',");
        sb.append(digito).append(","); 
        sb.append("'").append(tipo.getNome()).append("',");
        sb.append("'").append(subtipo.getLetra()).append("',");
        sb.append("'").append(proprietario).append("',");
        sb.append(peso).append(","); 
        sb.append(0.0).append(",");
        sb.append(bitola);
        
        sb.append(" ) ");
        return sb.toString();
    }

    @Override
    public boolean save() {
        try {
            statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO vagoes VALUES " + this.getProperties());
        } catch (SQLException ex) {          
            return false;
        }
        return true;
    }

    @Override
    public void delete() {
        try {                        
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM vagoes WHERE id='"+id+"'");
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
    }

    @Override
    public Vagao read() {
        try {                        
            statement = conn.createStatement();
            set = statement.executeQuery("SELECT * FROM vagoes WHERE id='"+id+"'");
            Vagao v = new Vagao(
                    set.getString("letras"),
                    set.getString("id"),
                    set.getInt("digito")
            );
            return v;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro em leitura SQL do vagão.");
        }
    }

    @Override
    public void update() {
        try {                        
            statement = conn.createStatement();
            statement.executeUpdate("UPDATE vagoes SET "
                    + "id = '" + getId() + "',"
                    + "letras = '" + getLetras()+ "',"
                    + "digito = " + getDigito()+ ","                    
                    + "tipo = '" + getTipo().getNome()+ "',"
                    + "subtipo = '" + getSubtipo().getLetra()+ "',"
                    + "proprietario = '" + getProprietario()+ "',"
                    + "peso = " + getPeso()+ ","
                    + "comprimento = " + getComprimento()+ ","
                    + "bitola = " + getBitola()
                    + "WHERE id='"+getId()+"'");
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
    }
    
    private boolean verificaInstanciacao(String letras){
        if(letras == null){
            return false;
        }
        // Converte a String em arranjo de caracteres
        char[] chars = letras.toCharArray();
        return  helper.verificaTipoESubtipo(letras) &&
                helper.verificaPMA(chars[2]) ;
    }
    
    /*
    Getters & Setters
    */

    public String getLetras() {
        return letras;
    }

    public void setLetras(String letras) {
        // Verifica se a String letras, passada por parâmetro, não é nula é compatível 
        // o tamanho necessário.
        if (!verificaInstanciacao(letras)){
            throw new RuntimeException ("Letras incompatíveis!");
        }        
        this.letras = letras;
        
        // Converte a String em arranjo de caracteres
        char[] chars = letras.toCharArray();
        Tipo t = helper.getTipo(chars[0]);
        setTipo(t);

        setSubtipo(letras);

        PB aux = helper.getPMA(chars[2]);

        setPeso(aux.getPeso());
        setBitola(aux.getBitola());

        setComprimento(comprimento);
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Subtipo getSubtipo() {
        return subtipo;
    }

    public void setSubtipo(String letras) {
        if(helper.verificaTipoESubtipo(letras)){
            char c_tipo = letras.toCharArray()[0];
            c_tipo = toUpper(c_tipo);
            
            char c_subtipo = letras.toCharArray()[1];
            c_subtipo = toUpper(c_subtipo);
            
            ArrayList<Subtipo> subtipos = helper.getSubtipos(helper.getTipo(c_tipo));
            this.subtipo = helper.getSubtipo(subtipos, c_subtipo);
        }        
    }

    @Override
    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.length() != 6){
            throw new RuntimeException ("Algarismos de identificação incompatíveis!");
        }
        this.id = id;
        setProprietario(id);
    }

    public int getDigito() {
        return digito;
    }

    public void setDigito(int digito) {
        this.digito = digito;
    }

    @Override
    public double getComprimento() {
        return comprimento;
    }

    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
    }

    @Override
    public double getBitola() {
        return bitola;
    }

    public void setBitola(double bitola) {
        this.bitola = bitola;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String id_proprietario) {        
        this.proprietario = helper.getProprietario(id_proprietario);
    }
    
    public static ArrayList<Vagao> getAll(){
        ArrayList<Vagao> response = new ArrayList<>();
        try {
            ResultSet vagoes = conn.createStatement().executeQuery("SELECT * FROM vagoes");
            while(vagoes.next()){
                response.add(new Vagao(
                    vagoes.getString("letras"),
                    vagoes.getString("id"),
                    vagoes.getInt("digito")
                ));
            }
            return response;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível ler os vagões.");
        }
        return null;
    }
    
}