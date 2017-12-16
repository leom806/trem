package database;


public interface DBObject {        
    
    boolean save();
          
    void delete();    
      
    Object read();
       
    void update();
}