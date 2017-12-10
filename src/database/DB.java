package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.tools.Server;

/**
 * Gerenciador do banco de dados implementado em padrão Singleton.
 * 
 * @author Leonardo Momente
 */

public final class DB{
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:file:~/test\\\\cdb;AUTO_SERVER=true";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static Connection conn = null;
    private static DB instance = null;
    
    private DB(){}
    
    public static DB getInstance(){
        return (instance==null) ? instance = new DB() : instance;
    }
    
    public Connection getConnection(){           
        if (conn != null)
            return conn;
        
        try {
            Class.forName(DB_DRIVER);
            // Inicia o servidor do banco de dados
            Server.createTcpServer("-tcpPort", "8082", "-tcpAllowOthers").start();
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);  
            
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return conn;
    }
    
    public void createTables(){
        
        if (conn==null){
            throw new RuntimeException("Conexão com o banco de dados não foi estabelecida!");
        }
                
        // Apenas uma Thread poderá acessar esse bloco por vez
        synchronized(this){
            
            Thread t1, t2, t3, t4;

            /*
            Chamada assíncrona ao banco de dados para criar as tabelas principais.
            */
            t1 = new Thread(() -> {
                try {
                    createVagoes();
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro em SQL na criação da tabela de vagões:\n "+ex.getMessage());                    
                }
            });
            
            t2 = new Thread(() -> {
                try {
                    createLocomotivas();
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro em SQL na criação da tabela de locomotivas:\n "+ex.getMessage());
                }
            });
            
            t3 = new Thread(() -> {
                try {
                    createComposicoes();
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro em SQL na criação da tabela de composicoes: \n"+ex.getMessage());
                }
            });
            
            t4 = new Thread(() -> {
                try {
                    createVagoesComposicao();
                    createLocomotivaComposicao();
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro em SQL na criação da tabela de <elemento>_composicao: \n"+ex.getMessage());
                }
            });
            
            
            t1.start();
            t2.start();
            t3.start();            
            
            try {
                t1.join();
                t2.join();
                t3.join();
                t4.start();
                t4.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException("Erro em sincronismo na criação da estrutura do banco de dados.");
            }
            
        }              
                
    }
    
    public void dropTables() {
        Statement s;
        try {
            s = conn.createStatement();
            s.execute("DROP TABLE IF EXISTS vagao_composicao");
            s.execute("DROP TABLE IF EXISTS locomotiva_composicao");
            s.execute("DROP TABLE IF EXISTS locomotivas");
            s.execute("DROP TABLE IF EXISTS vagoes");
            s.execute("DROP TABLE IF EXISTS composicoes");
        } catch (SQLException ex) {
            throw new RuntimeException("Impossível excluir tabela(s).\n"+ex.getMessage());
        }
        
    }
    
    private void createVagoes() throws SQLException{
        Statement sv = conn.createStatement();
        StringBuilder sb = new StringBuilder();
        sv.execute(sb.append("CREATE TABLE IF NOT EXISTS vagoes (")             
                .append("num INT AUTO_INCREMENT PRIMARY KEY,")
                .append("id CHAR(6) UNIQUE,")
                .append("letras CHAR(3) NOT NULL,")
                .append("digito INT,")
                .append("tipo VARCHAR(15),")
                .append("subtipo CHAR(3),")
                .append("proprietario VARCHAR(50),")
                .append("peso DOUBLE,")
                .append("comprimento DOUBLE,")
                .append("bitola DOUBLE")
                .append(")")
                .toString()
        );
    }        
    
    private void createLocomotivas() throws SQLException{
        Statement sv = conn.createStatement();
        StringBuilder sb = new StringBuilder();
        sv.execute(sb.append("CREATE TABLE IF NOT EXISTS locomotivas (" )               
                .append("classe INT PRIMARY KEY,")
                .append("descricao VARCHAR(50),")
                .append("pmr DOUBLE,")
                .append("comprimento DOUBLE,")
                .append("bitola DOUBLE")
                .append(")")
                .toString()
        );
    }
    
    private void createVagoesComposicao() throws SQLException{
        Statement sv = conn.createStatement();
        StringBuilder sb = new StringBuilder();
        sv.execute(
              sb.append("CREATE TABLE IF NOT EXISTS vagao_composicao (")               
                .append("id INT AUTO_INCREMENT PRIMARY KEY, ")
                .append("id_vagao CHAR(6) REFERENCES vagoes (id), ")
                .append("id_composicao INT NOT NULL REFERENCES composicoes (id) ")
                .append(")")
                .toString()
            );
    }
    
    private void createLocomotivaComposicao() throws SQLException{
        Statement sv = conn.createStatement();
        StringBuilder sb = new StringBuilder();
        sv.execute(
              sb.append("CREATE TABLE IF NOT EXISTS locomotiva_composicao (")               
                .append("id INT AUTO_INCREMENT PRIMARY KEY, ")
                .append("id_locomotiva INT REFERENCES locomotivas (classe), ")
                .append("id_composicao INT NOT NULL REFERENCES composicoes (id) ")
                .append(")")
                .toString()
            );
    }   
    
    private void createComposicoes() throws SQLException{
        Statement sv = conn.createStatement();
        StringBuilder sb = new StringBuilder();
        sv.execute(sb.append("CREATE TABLE IF NOT EXISTS composicoes (")             
                .append("id INT PRIMARY KEY AUTO_INCREMENT, ")
                .append("nome VARCHAR(80) UNIQUE NOT NULL, ")
                .append("pesoTotal DOUBLE, ")
                .append("pesoRebocavel DOUBLE, ")
                .append("comprimento DOUBLE ")
                .append(")")
                .toString()
        );        
    }
    
    
    public static void close(){
        try {
            conn.close();
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
    }
}