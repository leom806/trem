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
    /** Loader do driver SQL. */
    private static final String DB_DRIVER = "org.h2.Driver";
    /** Conexao com o banco de dados. */
    private static final String DB_CONNECTION = "jdbc:h2:file:~/test\\\\cdb;AUTO_SERVER=true";
    /** String que recebe nome do usuario para o BD. */
    private static final String DB_USER = "sa";
    /** String que recebe nome da senha para o BD. */
    private static final String DB_PASSWORD = "";
    /** Conexao do BD que recebe null. */
    private static Connection conn = null;
    /** Instancia da classe que recebe null. */
    private static DB instance = null;
    
     /**
     * Construtor default.
     */    
    private DB(){}
    
    /**
     * Metodo estatico que retorna uma instancia de uma classe DB.
     * Nao havendo uma instancia existente, uma eh criada.
     * 
     * @return instancia de BD
     */
    public static DB getInstance(){
        return (instance==null) ? instance = new DB() : instance;
    }
    
    /**
     * Metodo publico que busca uma conexao SQL.
     * Nao havendo uma conexao existente, uma eh criada usando a importacao DriverManager.
     * Caso haja erro na comunicacao com o banco de dados, uma excecao eh lancada.
     * 
     * @return conexao
     */
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
    
     /**
     * Metodo publico que cria tabelas.
     * Nao havendo uma conexao existente com o banco de dados, uma excecao eh lancada.
     * Para a criacao de vagoes, locomotivas, composicoes, e seus respectivos relacionamentos,
     * sao lancadas excecoes no caso de falhas com o SQL.
     * Uso de threads para aproveitamento de recursos.
     * Eh lancada uma excecao caso nao seja possivel encerrar a conexao com o SQL.
     */
    public void createTables(){
        
        if (conn==null){
            throw new RuntimeException("Conexão com o banco de dados não foi estabelecida!");
        }
                
        // Apenas uma Thread poderá acessar esse bloco por vez
        synchronized(this){
            
            Thread t1, t2, t3, t4;

            /*
            Chamada assincrona ao banco de dados para criar as tabelas principais.
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
    
    /**
     * Metodo publico que deleta todas as tabelas.
     * Havendo erros com o SQL, uma excecao eh lancada.
     */
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
    
    /**
     * Metodo privado que cria uma tabela do tipo vagao.
     * @throws java.sql.SQLException
     */
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
    
    /**
     * Metodo privado que cria uma tabela do tipo locomotiva.
     * @throws java.sql.SQLException
     */
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
    
    /**
     * Metodo privado que cria uma tabela que relaciona vagao com composicao.
     * @throws java.sql.SQLException
     */
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
    
    /**
     * Metodo privado que cria uma tabela que relaciona locomotiva com composicao.
     * @throws java.sql.SQLException
     */
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
    
    /**
     * Metodo privado que cria uma tabela do tipo composicao.
     * @throws java.sql.SQLException
     */
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
    
    /**
     * Metodo publico que encerra a conexao com o SQL.
     * Caso nao seja possivel encerrar a conexao, uma excecao eh lancada.
     */    
    public static void close(){
        try {
            conn.close();
        } catch (SQLException ex) {
            System.err.println("Erro de SQL: "+ex.getMessage());
        }
    }
}