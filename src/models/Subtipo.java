package models;


/** 
 * Cada Tipo de vagão contém diversos possíveis subtipos e, portanto, esta
 * classe foi criada para auxiliar na sua estruturação.
 * 
 * @author Leonardo Momente
 */
public class Subtipo extends Tipo{
    
    private final char letra;
    
    /**
     * Construtor obrigatório por herança.
     * 
     * @param num
     * @param letra
     * @param nome 
     */
    public Subtipo(int num, char letra, String nome) {
        super(num, nome);
        this.letra = letra;
    }    

    /**
     * Retorna letra do subtipo.
     * 
     * @return 
     */
    public char getLetra() {
        return letra;
    }    
    
}