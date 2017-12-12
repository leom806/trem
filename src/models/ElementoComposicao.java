package models;

/**
 * Interface implementada nas classes Vagao e Locomotiva.
 * Contem os metodos para buscar bitola, comprimento e peso.
 * 
 * @author Breno Velasco
 */
public interface ElementoComposicao {
    double getBitola();
    double getComprimento();
    double getPeso();
}
