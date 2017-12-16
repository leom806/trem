package models;


/** 
 * Cada Tipo de vagão contém diversos possíveis subtipos e, portanto, esta
 * classe foi criada para auxiliar na sua estruturação.
 * 
 * @author Leonardo Momente
 */
public class Tipo{
    
    /**
     * Número identificador do tipo.
     * 
     */
    private int num;
    
    /**
     * Nomenclatura do tipo de vagão.
     * 
     */
    private String nome;

    /**
     * Construtor parametrizado padrão.
     * 
     * @param num
     * @param nome 
     */
    public Tipo(int num, String nome) {
        this.num = num;
        this.nome = nome;
    }

    
    /**
     * Retorna o identificador.
     * 
     * @return 
     */
    public int getNum() {
        return num;
    }

    
    /**
     * Retorna o nome.
     * 
     * @return 
     */
    public String getNome() {
        return nome;
    }            
    
    @Override
    public String toString(){
        return nome;
    }
}