package models;

/** 
 * Cada Tipo de vagao contem diversos possiveis subtipos e, portanto, esta
 * classe foi criada para auxiliar na sua estruturacao.
 * 
 * @author Leonardo Momente
 */
public class Tipo{
    
    /** Número identificador do tipo. */
    private int num;
    
    /** Nomenclatura do tipo de vagão. */
    private String nome;

    /**
     * Construtor parametrizado padrão.
     * 
     * @param num
     *              numero identificador do tipo
     * @param nome 
     *               nome do tipo
     */
    public Tipo(int num, String nome) {
        this.num = num;
        this.nome = nome;
    }

    
    /**
     * Getter que busca o identificador.
     * 
     * @return numero
     */
    public int getNum() {
        return num;
    }

    
    /**
     * Getter que busca o nome.
     * 
     * @return nome
     */
    public String getNome() {
        return nome;
    }            
    
    /**
     * Metodo que sobrescreve toString(), retornando o nome.
     * 
     * @return string com o nome
     */
    @Override
    public String toString(){
        return nome;
    }
}