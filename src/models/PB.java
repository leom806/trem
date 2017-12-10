package models;


/**
 * Classe auxiliar para peso e bitola.
 * 
 * @author Leonardo Momente
 */
public class PB{
    
    /**
     * Peso do objeto dado em toneladas.
     */
    private final long peso;          
    
    /**
     * Tamanho em metros da bitola.
     */
    private final double bitola;   
    
    /**
     * Construtor parametrizado padrão.
     * 
     * @param peso
     * @param bitola 
     */
    public PB(long peso, double bitola){
        this.peso = peso;
        this.bitola = bitola;
    }
    
    /**
     * Retorna o peso.
     * 
     * @return 
     */
    public long getPeso() {
        return peso;
    }
    
    
    /**
     * Retorna o tamanho da bitola.
     * 
     * @return 
     */
    public double getBitola() {
        return bitola;
    }
    
    
}