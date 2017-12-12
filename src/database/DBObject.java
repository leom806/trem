package database;

/**
 * Interface implementada nas classes Vagao, Locomotiva e Composicao.
 * Contem os metodos gravar, deletar, ler e atualizar.
 * 
 * @author Breno Velasco
 */
public interface DBObject {        
    
    boolean save();
          
    void delete();    
      
    Object read();
       
    void update();
}