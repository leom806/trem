package models;

import database.DB;
import database.DBObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Composicao implements DBObject{
    protected ArrayList<Vagao> vagoes = new ArrayList<>();
    protected ArrayList<Locomotiva> locomotivas = new ArrayList<>();
    private String nome;
    private int qntdLocomotivas = 0;
    private double pesoTotal = 0.0;
    private double pesoRebocavel = 0.0;
    private double comprimento = 0.0;
    private static final double COMPRIMENTO_MAX = 2000.0; // dado em metros 
    private static Connection conn = DB.getInstance().getConnection();
    private int id;
    
    
    public Composicao(String nome){
        this.nome = nome;
    }
    
    public Composicao(String nome, Locomotiva locomotiva, List<Vagao> vagoes){
        this.locomotivas.add(locomotiva);
        vagoes.forEach((v)->{
            addVagao(v);
        });
        this.nome = nome;
    }
    
    public Composicao(String nome, List<Locomotiva> locomotivas, List<Vagao> vagoes){
        
        locomotivas.forEach((l)->{
            if(qntdLocomotivas>=3){
                throw new RuntimeException("Há um limite máximo de 3 locomotivas!");
            }
            if(locomotivas.size() < 1){
                throw new RuntimeException("Há um limite mínimo de 1 locomotiva!");
            }
            addLocomotiva(l);            
        });
        
        addVagoes(vagoes);
        this.nome = nome;
    }
    
    public void addVagao(Vagao v){
        addVagao(v, true);
    }
    
    public void addVagao(Vagao v, boolean sort){
        
        verificarComposicao(v);
        
        this.vagoes.add(v); 
        this.pesoTotal += v.getPeso();
        this.comprimento += v.getComprimento();
        
        if(sort){
            this.vagoes.sort(v);
        }
    }       
    
    public void addVagoes(List<Vagao> vagoes){
        vagoes.forEach((v)->{
            addVagao(v, false);
        });
        this.vagoes.sort(vagoes.get(0));
    }
    
    public void addLocomotiva(Locomotiva l){
        
        verificarComposicao(l);
        
        this.locomotivas.add(l);
        qntdLocomotivas++;
        pesoRebocavel += l.getPeso();
    }          

    
    public void removerVagao(Vagao v){
        this.vagoes.remove(v);
        this.vagoes.sort(v);
    }
    
    public void removerVagao(String id){
        for(Vagao v : vagoes) {
            if(v.getId().equals(id)){
                this.vagoes.remove(v);
                this.vagoes.sort(v);
                break;
            }
        }   
    }
    
    public void removerLocomotiva(Locomotiva l){
        if(qntdLocomotivas==1){
            throw new RuntimeException("A composição deve ter ao menos uma locomotiva!");
        }
        this.locomotivas.remove(l);
        qntdLocomotivas--;
        pesoRebocavel -= l.getPeso();
    }
    
    public void removerLocomotiva(int classe){
        for(Locomotiva l : locomotivas) {
            if(l.getClasse()== classe){
                this.locomotivas.remove(l);                
                break;
            }
        }   
    }
    
    private <T extends ElementoComposicao> void verificarComposicao(T elemento){
        
        if(qntdLocomotivas>0){
            if(elemento.getBitola() != locomotivas.get(0).bitola){
                throw new RuntimeException("A bitola dos elementos devem ser iguais a bitola da locotiva principal!");
            }  
        }
                                      
        if(elemento instanceof Vagao && (pesoTotal+elemento.getPeso()) > pesoRebocavel){
            throw new RuntimeException("O somatória do peso máximo admissível deve ser inferior ao somatório do peso máximo rebocável!");
        }
        
        if((comprimento+elemento.getComprimento()) > COMPRIMENTO_MAX){
            throw new RuntimeException(String.format("O comprimento da composição não deve passar de %.2f metros!", COMPRIMENTO_MAX));
        }
    }
    
    /*
    DBObject methods
    */
    
    @Override
    public boolean save() {
        try {
            Statement s = conn.createStatement();            
            StringBuilder sb = new StringBuilder();
            
            sb.append("INSERT INTO composicoes (")
                    .append("nome, pesototal, pesorebocavel, comprimento")
                    .append(") VALUES (")
                    .append("'").append(this.nome).append("', ")                    
                    .append(this.pesoTotal).append(", ")
                    .append(this.pesoRebocavel).append(", ")
                    .append(this.comprimento)
                    .append(")");
            s.executeUpdate(sb.toString());            
                           
            // Pega o id auto incrementado do BD
            ResultSet composicao = s.executeQuery("SELECT * FROM composicoes WHERE nome='"+nome+"'");            
            if(composicao.next()){
                id = composicao.getInt("id");
            }
                        
            for(int i=0; i<locomotivas.size(); i++){
                sb = new StringBuilder();
                sb.append("INSERT INTO locomotiva_composicao (id_locomotiva, id_composicao) VALUES (")
                        .append(locomotivas.get(i).getClasse()).append(", ")
                        .append(this.id)
                        .append(")");
                s.executeUpdate(sb.toString());
            }
            
            for(int i=0; i<vagoes.size(); i++){
                sb = new StringBuilder();
                sb.append("INSERT INTO vagao_composicao (id_vagao, id_composicao) VALUES (")
                        .append("'").append(vagoes.get(i).getId()).append("', ")
                        .append(this.id)
                        .append(")");
                s.executeUpdate(sb.toString());
            }            
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    @Override
    public void delete() {
        try {
            Statement s = conn.createStatement();
            s.executeUpdate("DELETE FROM vagao_composicao WHERE id="+id);
            s.executeUpdate("DELETE FROM locomotiva_composicao WHERE id="+id);
            s.executeUpdate("DELETE FROM composicoes WHERE nome='"+nome+"'");
        } catch (SQLException ex) {
            throw new RuntimeException("Erro durante operação de exclusão.\n"+ex.getMessage());
        }
    }

    /*
    DBObject methods
    */
    @Override
    public Composicao read() {
        
        Composicao c = new Composicao(nome);                
        
        try {
                        
            ResultSet composicao = conn.createStatement().executeQuery("SELECT * FROM composicoes WHERE nome='"+nome+"'");               
            while(composicao.next()){
                c.comprimento = composicao.getDouble("comprimento");
                c.pesoRebocavel = composicao.getDouble("pesorebocavel");
                c.pesoTotal = composicao.getDouble("pesototal");
            }
            
            ResultSet loc_comp = conn.createStatement().executeQuery("SELECT * FROM locomotiva_composicao WHERE id_composicao="+id);             
            while(loc_comp.next()){         
                ResultSet locs = conn.createStatement().executeQuery("SELECT * FROM locomotivas WHERE classe="+loc_comp.getInt("id_locomotiva"));                
                while(locs.next()){
                    Locomotiva l = new Locomotiva(
                        locs.getInt("classe"),
                        locs.getString("descricao"),
                        locs.getDouble("pmr"),
                        locs.getDouble("bitola"),
                        locs.getDouble("comprimento")
                    );
                    c.addLocomotiva(l);
                }
            }

            ResultSet vag_comp = conn.createStatement().executeQuery("SELECT * FROM vagao_composicao WHERE id_composicao="+id);                
            while(vag_comp.next()){
                ResultSet vags = conn.createStatement().executeQuery("SELECT * FROM vagoes WHERE id='"+vag_comp.getString("id_vagao")+"'");                
                while(vags.next()){
                    Vagao v = new Vagao(
                        vags.getString("letras"),
                        vags.getString("id"),
                        vags.getInt("digito")
                    );
                    c.addVagao(v);
                }
            }
            
            return c;
            
        } catch (SQLException ex) {            
            throw new RuntimeException("Erro em SQL: "+ex.getMessage());
        }        
    }


    @Override
    public void update() {
        try {
            Statement s = conn.createStatement();
            this.delete();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException("Interrupção durante processo de exclusão.");
            }
            this.save();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro em SQL: "+ex.getMessage());
        }
    }

    /*
    Getters & Setters
    */
    
    public ArrayList<Vagao> getVagoes() {
        return vagoes;
    }

    public ArrayList<Locomotiva> getLocomotivas() {
        return locomotivas;
    }

    public int getQntdLocomotivas() {
        return qntdLocomotivas;
    }

    public String getNome() {
        return nome;
    }
        
    public Vagao find(String id){
        if(id.length() != 6){
            throw new RuntimeException("Id inválido na pesquisa do vagão!");
        } 
        for(Vagao v : vagoes){
            if(v.getId().equals(id)){
                return v;
            }
        }
        return null;
    }
    
    /*
    toString()
    */

    @Override
    public String toString() {
        return "Composicao{" + "vagoes=" + vagoes + ", locomotivas=" + locomotivas + ", nome=" + nome + ", qntdLocomotivas=" + qntdLocomotivas + ", pesoTotal=" + pesoTotal + ", pesoRebocavel=" + pesoRebocavel + ", comprimento=" + comprimento + '}';
    }            
    
}