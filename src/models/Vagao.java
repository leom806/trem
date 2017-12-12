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
 * Classe de objetos do tipo Vagao.
 * 
 * @author Breno Velasco e Leonardo Momente
 */
public class Vagao implements Comparator, DBObject, ElementoComposicao{   
    /** 3 letras que indicam o tipo, subtipo e peso máximo admissivel/bitola do
    vagao. */
    protected String letras;  
    /** Tipo do vagao. */
    protected Tipo tipo;
    /** Subtipo do vagao. */
    protected Subtipo subtipo;
    /** Peso maximo admissível. */
    protected double peso;
    /** 6 algarismos de identificacao. */
    protected String id;                    
    /** Digito verificador. */
    protected int digito;                   
    /** Comprimento do vagao dado em metros. */
    protected double comprimento;           
    /** Tamanho da bitola, tambem em metros. */
    protected double bitola;                
    /** Proprietario do vagao. */
    protected String proprietario;   
    /** Objeto auxiliar. */
    private final Helper helper = Helper.getInstance();
    
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
     * Construtor padrao que verifica as letras e instancia um objeto Vagao,
     * não havendo irregularidades.
     * 
     * @param letras
     *                 indica o tipo, subtipo e peso maximo do vagao
     * @param id
     *             algarismos de identificacao
     * @param digito
     *                 digito verificador
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
    }

    /**
     * Metodo que sobrescreve toString(), retornando o vagao e suas caracteristicas.
     * 
     * @return string com os dados do vagao
     */
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

    /**
     * Metodo que sobrescreve compare(), retornando a comparacao entre os vagoes.
     * 
     * @return inteiro com a comparacao
     */
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
    
    /**
     * Metodo que retorna as propriedades do vagao.
     * 
     * @return string com as propriedades
     */
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

    /**
     * Metodo que realiza um UPDATE no SQL para um determinado vagao, afim de gravar seu estado atual.
     * 
     * @return boolean indicando se houve ou não gravação
     */
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

    /**
     * Metodo que realiza um DELETE no SQL para um determinado vagao, afim de deleta-lo.
     */
    @Override
    public void delete() {
        try {                        
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM vagoes WHERE id='"+id+"'");
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
    }

    /**
     * Metodo que realiza um SELECT no SQL para um determinado vagao, afim de le-lo.
     * 
     * @return um vagao contendo as letras, id e digito, senao lanca uma excecao
     */
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

    /**
     * Metodo que realiza um UPDATE no SQL para um determinado vagao, afim de atualiza-lo/altera-lo.
     */  
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
    
    /**
     * Metodo que verifica se um vagao esta ou nao instanciado.
     * 
     * @return a verificacao do vagao, ou retorna false caso nao foi verificado
     * @param letras
     *                 letras identificadoras do vagao
     */
    private boolean verificaInstanciacao(String letras){
        if(letras == null){
            return false;
        }
        // Converte a String em arranjo de caracteres
        char[] chars = letras.toCharArray();
        return  helper.verificaTipoESubtipo(letras) &&
                helper.verificaPMA(chars[2]) ;
    }
    
    /**
     * Getter que busca letras.
     * 
     * @return letras
     */
    public String getLetras() {
        return letras;
    }

    /**
     * Setter que estabelece letras.
     * 
     * @param letras
     *                 letras identificadoras do vagao
     */
    public void setLetras(String letras) {
        // verifica se a String letras, passada por parâmetro, não é nula é compatível 
        // o tamanho necessário.
        if (!verificaInstanciacao(letras)){
            throw new RuntimeException ("Letras incompatíveis!");
        }        
        this.letras = letras;
        
        // converte a String em arranjo de caracteres
        char[] chars = letras.toCharArray();
        Tipo t = helper.getTipo(chars[0]);
        setTipo(t);

        setSubtipo(letras);

        PB aux = helper.getPMA(chars[2]);

        setPeso(aux.getPeso());
        setBitola(aux.getBitola());

        setComprimento(comprimento);
    }

    /**
     * Getter que busca tipo.
     * 
     * @return tipo
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Setter que estabelece tipo.
     * 
     * @param tipo
     *               tipo do vagao
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Getter que busca subtipo.
     * 
     * @return subtipo
     */
    public Subtipo getSubtipo() {
        return subtipo;
    }

    /**
     * Setter que estabelece subtipo.
     * 
     * @param letras
     *                 letras identificadoras do vagao
     */
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

    /**
     * Getter que busca peso.
     * 
     * @return peso
     */
    @Override
    public double getPeso() {
        return peso;
    }

    /**
     * Setter que estabelece peso.
     * 
     * @param peso
     *               peso do vagao
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Getter que busca Id.
     * 
     * @return Id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter que estabelece Id.
     * @param id
     *             id do vagao
     */
    public void setId(String id) {
        if (id == null || id.length() != 6){
            throw new RuntimeException ("Algarismos de identificação incompatíveis!");
        }
        this.id = id;
        setProprietario(id);
    }

    /**
     * Getter que busca digito.
     * 
     * @return digito verificador
     */
    public int getDigito() {
        return digito;
    }

    /**
     * Setter que estabelece digito.
     * 
     * @param digito
     *                 digito verificador
     */
    public void setDigito(int digito) {
        this.digito = digito;
    }

    /**
     * Getter que busca comprimento do vagao.
     * 
     * @return comprimento do vagao
     */
    @Override
    public double getComprimento() {
        return comprimento;
    }

    /**
     * Setter que estabelece comprimento do vagao.
     * 
     * @param comprimento
     *                      comprimento do vagao
     */
    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
    }

    /**
     * Getter que busca bitola do vagao.
     * 
     * @return bitola do vagao
     */
    @Override
    public double getBitola() {
        return bitola;
    }

    /**
     * Setter que estabelece bitola do vagao.
     * 
     * @param bitola
     *                 bitola do vagao
     */
    public void setBitola(double bitola) {
        this.bitola = bitola;
    }

    /**
     * Getter que busca proprietario do vagao.
     * 
     * @return proprietario do vagao.
     */
    public String getProprietario() {
        return proprietario;
    }

    /**
     * Setter que estabelece proprietario do vagao.
     * 
     * @param id_proprietario
     *                       proprietario do vagao
     */
    public void setProprietario(String id_proprietario) {        
        this.proprietario = helper.getProprietario(id_proprietario);
    }
    
    /**
     * Metodo que busca todos os vagoes.
     * 
     * @return todos os vagoes
     */
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