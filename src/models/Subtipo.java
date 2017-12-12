package models;

/** 
 * Cada Tipo de vagao contem diversos possiveis subtipos e, portanto, esta
 * classe foi criada para auxiliar na sua estruturacao.
 * 
 * @author Leonardo Momente
 */
public class Subtipo extends Tipo{
    
    private final char letra;
    
    /**
     * Construtor obrigatorio por herança.
     * 
     * @param num
     *              numero do vagao
     * @param letra
     *                letra do vagao
     * @param nome 
     *               nome que identifica o vagao
     */
    public Subtipo(int num, char letra, String nome) {
        super(num, nome);
        this.letra = letra;
    }    

    /**
     * Retorna letra do subtipo.
     * 
     * @return letra do vagao
     */
    public char getLetra() {
        return letra;
    }    
    
}