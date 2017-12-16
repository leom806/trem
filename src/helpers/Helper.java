package helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import models.*;

/**
 * Classe auxiliar que disponibiliza métodos estáticos para tarefas gerais,
 * utilizando o padrão de projeto Singleton.
 * 
 * @author Leonardo Momente
 */
public class Helper {
    
    private static HashMap<Character, Tipo> tipos;
    private static HashMap<String, Tipo> tiposR;
    private static HashMap<String, Character> tiposLetras;
    private static HashMap<Tipo, ArrayList<Subtipo>> subtipos;
    private static HashMap<Character, PB> pmas;
    private static Helper instance = null;
    private Character[] pmasList = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'P', 'Q', 'R', 'S', 'T', 'U'};
    
    public static final String FAST = "com.jtattoo.plaf.fast.FastLookAndFeel";
    public static final String TEXTURE = "com.jtattoo.plaf.texture.TextureLookAndFeel";
    public static final String LUNA = "com.jtattoo.plaf.luna.LunaLookAndFeel";
    public static final String ALUMINIUM = "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel";
    public static final String DEFAULT = "";
    

    public static <T extends JFrame> void switchUI(T component, String look) {
        try {
            // javax.swing.plaf.metal.MetalLookAndFeel
            // com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
            // com.sun.java.swing.plaf.motif.MotifLookAndFeel
            
            if(look.isEmpty()){
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }else{
                UIManager.setLookAndFeel(look);                
            }
            SwingUtilities.updateComponentTreeUI(component);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível alterar o tema.");
        }
    }

    // Helper das views na hora de sair
    public static void onExit() {
        if (JOptionPane.showConfirmDialog(null,
                "Deseja realmente sair?", "Sair",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            File f = new File(".lock");
            if (f.exists()) {
                f.delete();
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado.");
            }
        }
    }
    
    private Tipo gondola,
                 plataforma,
                 tanque,
                 fechado,
                 isotermico, 
                 hooper,
                 gaiola,
                 caboose;
    
    private void init(){        

        if (tipos == null){
            tipos = new HashMap<>();
            
            gondola       = new Tipo(1, "Gôndola"); 
            plataforma    = new Tipo(2, "Plataforma");
            tanque        = new Tipo(3, "Tanque");
            fechado       = new Tipo(4, "Fechado");
            isotermico    = new Tipo(5, "Isotérmico");
            hooper        = new Tipo(6, "Hooper");
            gaiola        = new Tipo(7, "Gaiola");
            caboose       = new Tipo(8, "Caboose");
            
            tipos.put('G', gondola);
            tipos.put('P', plataforma);
            tipos.put('T', tanque);
            tipos.put('F', fechado);
            tipos.put('I', isotermico);
            tipos.put('H', hooper);
            tipos.put('A', gaiola);
            tipos.put('C', caboose);
        }
        
        if(tiposR == null){
            tiposR = new HashMap<>();
            
            // Setando os Tipos pelo nome de forma a poder resgatar pela view
            tiposR.put("Gôndola", gondola);
            tiposR.put("Plataforma", plataforma);
            tiposR.put("Tanque", tanque);
            tiposR.put("Fechado", fechado);
            tiposR.put("Isotérmico", isotermico);
            tiposR.put("Hooper", hooper);
            tiposR.put("Gaiola", gaiola);
            tiposR.put("Caboose", caboose);
        }
        
        if(tiposLetras == null){
            tiposLetras = new HashMap<>();

            tiposLetras.put("Gôndola", 'G');
            tiposLetras.put("Plataforma", 'P');
            tiposLetras.put("Tanque", 'T');
            tiposLetras.put("Fechado", 'F');
            tiposLetras.put("Isotérmico", 'I');
            tiposLetras.put("Hooper", 'H');
            tiposLetras.put("Gaiola", 'A');
            tiposLetras.put("Caboose", 'C');
        }
        
        if (subtipos == null){
            subtipos = new HashMap<>();                        
            
            ArrayList<Subtipo> subtiposGondola = new ArrayList<>();
            ArrayList<Subtipo> subtiposPlataforma = new ArrayList<>();
            ArrayList<Subtipo> subtiposTanque = new ArrayList<>();
            ArrayList<Subtipo> subtiposFechado = new ArrayList<>();
            ArrayList<Subtipo> subtiposIsotermico = new ArrayList<>();
            ArrayList<Subtipo> subtiposHooper = new ArrayList<>();
            ArrayList<Subtipo> subtiposGaiola = new ArrayList<>();
            ArrayList<Subtipo> subtiposCaboose = new ArrayList<>();
            
            
            subtiposGondola.add(new Subtipo(1, 'D', "para descarga em car-dumper"));
            subtiposGondola.add(new Subtipo(2, 'P', "bordas fixas e portas laterais"));
            subtiposGondola.add(new Subtipo(3, 'F', "bordas fixas e fundo móvel (drop-bottom)"));         
            subtiposGondola.add(new Subtipo(4, 'M', "bordas fixas e cobertura móvel"));
            subtiposGondola.add(new Subtipo(5, 'T', "com bordas tombantes"));
            subtiposGondola.add(new Subtipo(6, 'S', "com bordas semi-tombantes"));            
            subtiposGondola.add(new Subtipo(7, 'H', "c/ bordas basculantes ou semi-tombantes c/ fundo em lombo de camelo"));
            subtiposGondola.add(new Subtipo(8, 'C', "c/ bordas basculantes ou semi-tombantes c/ fundo em lombo de camelo e cobertura móvel"));         
            subtiposGondola.add(new Subtipo(9, 'B', "basculante"));
            subtiposGondola.add(new Subtipo(10,'N', "não remunerado"));
            subtiposGondola.add(new Subtipo(12,'Q', "outros tipos"));
            
            
            subtiposPlataforma.add(new Subtipo(13, 'M', "convencional com piso de madeira"));
            subtiposPlataforma.add(new Subtipo(14, 'E', "convencional com piso metálico"));
            subtiposPlataforma.add(new Subtipo(15, 'D', "convencional com disposito p/ containers"));         
            subtiposPlataforma.add(new Subtipo(16, 'C', "para containers"));
            subtiposPlataforma.add(new Subtipo(17, 'R', "com estrado rebaixado"));
            subtiposPlataforma.add(new Subtipo(18, 'T', "para auto-trem"));            
            subtiposPlataforma.add(new Subtipo(19, 'G', "para piggy-back"));
            subtiposPlataforma.add(new Subtipo(20, 'P', "com cabeceira (bulkhead)"));         
            subtiposPlataforma.add(new Subtipo(21, 'B', "para bobinas"));
            subtiposPlataforma.add(new Subtipo(22, 'A', "com 2 pavimentos, para automóveis"));
            subtiposPlataforma.add(new Subtipo(23, 'N', "não remunerado"));
            subtiposPlataforma.add(new Subtipo(24, 'Q', "outros tipos"));
            
            
            subtiposTanque.add(new Subtipo(25, 'C', "convencional"));
            subtiposTanque.add(new Subtipo(26, 'S', "com serpentinas para aquecimento"));
            subtiposTanque.add(new Subtipo(27, 'P', "para produtos pulverulentos"));         
            subtiposTanque.add(new Subtipo(28, 'F', "para fertilizantes"));
            subtiposTanque.add(new Subtipo(29, 'A', "p/ ácidos ou outros corrosivos líquidos"));
            subtiposTanque.add(new Subtipo(30, 'G', "para gás liquifeito de petróleo"));            
            subtiposTanque.add(new Subtipo(31, 'N', "não remunerado"));
            subtiposTanque.add(new Subtipo(32, 'Q', "outros tipos"));
            
            
            subtiposFechado.add(new Subtipo(33, 'R', "convencional, caixa metálica com revestimento"));
            subtiposFechado.add(new Subtipo(34, 'S', "convencional, caixa metálica sem revestimento"));
            subtiposFechado.add(new Subtipo(35, 'M', "convencional, caixa de madeira ou mista"));
            subtiposFechado.add(new Subtipo(36, 'E', "com escotilhas"));
            subtiposFechado.add(new Subtipo(37, 'H', "com escotilhas e tremonhas"));
            subtiposFechado.add(new Subtipo(38, 'L', "laterais corrediças (all door)"));
            subtiposFechado.add(new Subtipo(39, 'P', "c/ escotilhas, portas basculantes, fundo em l. de camelo e prot. anticorrosiva"));
            subtiposFechado.add(new Subtipo(40, 'V', "ventilado"));
            subtiposFechado.add(new Subtipo(41, 'N', "não remunerado"));
            subtiposFechado.add(new Subtipo(42, 'Q', "outros tipos"));
            
            
            subtiposIsotermico.add(new Subtipo(43, 'C', "convencional"));
            subtiposIsotermico.add(new Subtipo(44, 'F', "frigorífico"));
            subtiposIsotermico.add(new Subtipo(45, 'N', "não remunerado"));
            subtiposIsotermico.add(new Subtipo(46, 'Q', "outros tipos"));
            
            
            subtiposHooper.add(new Subtipo(47, 'F', "fechado convencional"));
            subtiposHooper.add(new Subtipo(48, 'P', "fechado com proteção anti-corrosiva"));
            subtiposHooper.add(new Subtipo(49, 'E', "tanque (center-flow) com proteção anti-corrosiva"));
            subtiposHooper.add(new Subtipo(50, 'T', "tanque (center-flow) convencional"));
            subtiposHooper.add(new Subtipo(51, 'A', "aberto"));
            subtiposHooper.add(new Subtipo(52, 'N', "não remunerado"));
            subtiposHooper.add(new Subtipo(53, 'Q', "outros tipos"));
            
            
            subtiposGaiola.add(new Subtipo(54, 'C', "coberta com estrado e estrutura metálica (inclui réguas de madeira)"));
            subtiposGaiola.add(new Subtipo(55, 'M', "cobertura de madeira"));
            subtiposGaiola.add(new Subtipo(56, 'R', "para animais de raça"));
            subtiposGaiola.add(new Subtipo(57, 'V', "para aves"));
            subtiposGaiola.add(new Subtipo(58, 'D', "descoberta"));
            subtiposGaiola.add(new Subtipo(59, 'N', "não remunerado"));
            subtiposGaiola.add(new Subtipo(60, 'Q', "outros tipos"));
            
            
            subtiposCaboose.add(new Subtipo(61, 'C', "convencional"));
            subtiposCaboose.add(new Subtipo(62, 'B', "com compartimento para bagagem"));
            subtiposCaboose.add(new Subtipo(63, 'N', "não remunerado"));
            subtiposCaboose.add(new Subtipo(64, 'Q', "outros tipos"));
            
            
            subtipos.put(gondola, subtiposGondola);
            subtipos.put(plataforma, subtiposPlataforma);
            subtipos.put(tanque, subtiposTanque);
            subtipos.put(fechado, subtiposFechado);
            subtipos.put(isotermico, subtiposIsotermico);
            subtipos.put(hooper, subtiposHooper);
            subtipos.put(gaiola, subtiposGaiola);
            subtipos.put(caboose, subtiposCaboose);
        }
    
        if (pmas == null){    
            pmas = new HashMap<>();
            pmas.put('A', new PB(30,  1.0));
            pmas.put('B', new PB(47,  1.0)); 
            pmas.put('C', new PB(64,  1.0)); 
            pmas.put('D', new PB(80,  1.0)); 
            pmas.put('E', new PB(100, 1.0)); 
            pmas.put('F', new PB(119, 1.0)); 
            pmas.put('G', new PB(143, 1.0)); 
            pmas.put('P', new PB(47,  1.6)); 
            pmas.put('Q', new PB(64,  1.6)); 
            pmas.put('R', new PB(80,  1.6)); 
            pmas.put('S', new PB(100, 1.6)); 
            pmas.put('T', new PB(119, 1.6)); 
            pmas.put('U', new PB(143, 1.6)); 
        }
    }
    
    public boolean verificaTipoESubtipo(String letras){
        char c = letras.toCharArray()[0];
        c = toUpper(c);
        Tipo t = tipos.get(c);
        if(t != null){
            // Recebe os subtipos permitidos para o tipo dado
            ArrayList<Subtipo> subtiposPermitidos =
                    getSubtipos(t);                

            if(subtiposPermitidos==null){
                throw new RuntimeException("Não há subtipos para esse Tipo de vagão!");
            }
            
            // Agora, dentro dos subtipos permitidos, faz-se a busca pelo subtipo
            return 
                getSubtipo(subtiposPermitidos, toUpper(letras.toCharArray()[1])) != null;
        }
        return false;        
    }
    
    public boolean verificaPMA(char c){
        c = toUpper(c);
        return pmas.get(c) != null;
    }
    
    public PB getPMA(char c){
        c = toUpper(c);
        return pmas.get(c);
    }
    
    public Tipo getTipo(char c){
        c = toUpper(c);
        return tipos.get(c);
    }
    
    public Tipo getTipoR(String c){
        return tiposR.get(c);
    }
    
    public Tipo getTipoByName(String c){
        return tiposR.get(c);
    }
    
    public Character getLetraPorTipo(String c){
        return tiposLetras.get(c);
    }
    
    public String[] getTipos(){
        Object[] data = tiposR.values().toArray();
        String[] result = new String[data.length];
        
        for(int i = 0 ; i < data.length ; i ++){
            result[i] = data[i].toString();
        }
        
        return result;
    }
    
    public String[] getPmas()
    {
        String[] result = new String[pmasList.length];
        
        int count = 0;
        
        for(Character element: pmasList){
            result[count] = element + "";
                    
            count++;
        }
        
        return result;
    }
    
    public ArrayList<Subtipo> getSubtipos(Tipo t){      
        return subtipos.get(t);
    }
    
    public Subtipo getSubtipo(ArrayList<Subtipo> array, char c){        
        c = toUpper(c);
        try{
            for (Subtipo s : array){
                if(s.getLetra() == c){
                    return s;
                }
            }
        }catch(RuntimeException e){
            throw new RuntimeException("Subtipo não existente!");
        }    
        return null;
    }
    
    public String getProprietario(String id){
        
        int aux = Integer.parseInt(id);
        
        if( aux>=1 && aux<=99999){
            return "Particular";
        }else if( aux>=100000 && aux<=299999){
            return "Cia. Vale do Rio Doce";
        }else if( aux>=300000 && aux<=599999){
            return "Fepasa";
        }else if( aux>=600000 && aux<=999999){
            return "RFFSA";
        }
        
        throw new RuntimeException("Identificação de proprietário inválida!");
    }
    
    /**
     * Constutor parametrizado para inicialização dos componentes.
     * 
     */
    public Helper(){
        init();
    }
    
    public static final Helper getInstance(){
        if(instance==null){
            instance = new Helper();
        }
        return instance;
    }
    
    /**
     * Método auxiliar para tornar o caracter maiúsculo.
     * 
     * @param c
     * @return 
     */
    public static char toUpper(char c){        
        if (c >= 'a' && c <= 'z'){
            c -= 'a'-'A';
        }
        return c;
    }
}
