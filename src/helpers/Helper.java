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
 * Classe auxiliar que disponibiliza metodos estaticos para tarefas gerais,
 * utilizando o padrão de projeto Singleton.
 * 
 * @author Leonardo Momente
 */
public class Helper {
    /** HashMap para controlar chaves e elementos dos tipos. */
    private static HashMap<Character, Tipo> tipos;
    /** HashMap para controlar chaves e elementos dos tipos. */
    private static HashMap<String, Tipo> tiposR;
    /** HashMap para controlar chaves e elementos dos tipos. */
    private static HashMap<String, Character> tiposLetras;
    /** HashMap para controlar chaves e elementos dos subtipos. */
    private static HashMap<Tipo, ArrayList<Subtipo>> subtipos;
    /** HashMap para controlar chaves e elementos dos pesos e bitolas. */
    private static HashMap<Character, PB> pmas;
    
    /** Instancia do objeto que recebe nulo. */
    private static Helper instance = null;
    /** Array de caracteres contendo todos os caracteres dos tipos peso e bitola. */
    private Character[] pmasList = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'P', 'Q', 'R', 'S', 'T', 'U'};
    /** Look and Feel inicial. */
    private static int lookAndFeel = 1;

    /**
     * Metodo publico que altera a interface grafica da janela.
     * @param <T>
     *             Objeto que eh filho de JFrame
     * @param component
     *                    componente swing
     */    
    public static <T extends JFrame> void switchUI(T component) {
        try {
            // javax.swing.plaf.metal.MetalLookAndFeel
            // com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
            // com.sun.java.swing.plaf.motif.MotifLookAndFeel

            String currLook = "";

            lookAndFeel = (lookAndFeel > 3) ? 0 : lookAndFeel;

            switch (lookAndFeel) {
                case 0:
                    currLook = "com.jtattoo.plaf.texture.TextureLookAndFeel";
                    lookAndFeel++;
                    break;
                case 1:
                    currLook = "com.jtattoo.plaf.luna.LunaLookAndFeel";
                    lookAndFeel++;
                    break;
                case 2:
                    currLook = "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel";
                    lookAndFeel++;
                    break;
                case 3:
                    currLook = "com.jtattoo.plaf.fast.FastLookAndFeel";
                    lookAndFeel++;
                    break;
            }

            UIManager.setLookAndFeel(currLook);
            SwingUtilities.updateComponentTreeUI(component);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para auxiliar a saida sem ocorrencia de erros do programa.
     */
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
    
    /**
     * Declaracoes dos tipos que definem um vagao.
     */    
    private Tipo gondola,
                 plataforma,
                 tanque,
                 fechado,
                 isotermico, 
                 hooper,
                 gaiola,
                 caboose;
    
    /**
     * Metodo privado que inicializa as variaveis da classe.
     */
    private void init(){        
        // inicializa tipos caso seja nulo
        if (tipos == null){
            tipos = new HashMap<>();
            
            // atribui aos tipos das hash maps valores por padrao
            gondola       = new Tipo(1, "Gôndola"); 
            plataforma    = new Tipo(2, "Plataforma");
            tanque        = new Tipo(3, "Tanque");
            fechado       = new Tipo(4, "Fechado");
            isotermico    = new Tipo(5, "Isotérmico");
            hooper        = new Tipo(6, "Hooper");
            gaiola        = new Tipo(7, "Gaiola");
            caboose       = new Tipo(8, "Caboose");
            
            // atribui diretamente as hash maps valores por padrão, utilizando das variaveis
            // declaradas acima
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
            
            // setando os Tipos pelo nome, de forma a poder resgatar pela view
            tiposR.put("Gôndola", gondola);
            tiposR.put("Plataforma", plataforma);
            tiposR.put("Tanque", tanque);
            tiposR.put("Fechado", fechado);
            tiposR.put("Isotérmico", isotermico);
            tiposR.put("Hooper", hooper);
            tiposR.put("Gaiola", gaiola);
            tiposR.put("Caboose", caboose);
        }
        
        // setando os Tipos pelo caracter de designacao, de forma a poder resgatar pela view
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
        
        // inicializa subtipos caso seja nulo
        if (subtipos == null){
            subtipos = new HashMap<>();                        
            
            // cria array lists para cada subtipo do hash map
            ArrayList<Subtipo> subtiposGondola = new ArrayList<>();
            ArrayList<Subtipo> subtiposPlataforma = new ArrayList<>();
            ArrayList<Subtipo> subtiposTanque = new ArrayList<>();
            ArrayList<Subtipo> subtiposFechado = new ArrayList<>();
            ArrayList<Subtipo> subtiposIsotermico = new ArrayList<>();
            ArrayList<Subtipo> subtiposHooper = new ArrayList<>();
            ArrayList<Subtipo> subtiposGaiola = new ArrayList<>();
            ArrayList<Subtipo> subtiposCaboose = new ArrayList<>();
            
            // atribui aos subtipos das hash maps valores por padrão
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
            
            // atribui diretamente as hash maps valores por padrão, utilizando das variaveis
            // declaradas acima       
            subtipos.put(gondola, subtiposGondola);
            subtipos.put(plataforma, subtiposPlataforma);
            subtipos.put(tanque, subtiposTanque);
            subtipos.put(fechado, subtiposFechado);
            subtipos.put(isotermico, subtiposIsotermico);
            subtipos.put(hooper, subtiposHooper);
            subtipos.put(gaiola, subtiposGaiola);
            subtipos.put(caboose, subtiposCaboose);
        }
    
        // se pmas for nulo, inicializa-o com uma hash map e popula com valores
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
    
    /**
     * Metodo que verifica a validez de um tipo ou subtipo.
     * 
     * @param letras
     *                 letra a ser verificada
     * @return variavel booleana
     */    
    public boolean verificaTipoESubtipo(String letras){
        char c = letras.toCharArray()[0];
        c = toUpper(c);
        Tipo t = tipos.get(c);
        if(t != null){
            // recebe os subtipos permitidos para o tipo dado
            ArrayList<Subtipo> subtiposPermitidos =
                    getSubtipos(t);                

            // caso nao haja um subtipo permitido para a letra passada como parametro,
            // uma excecao eh lancada.            
            if(subtiposPermitidos==null){
                throw new RuntimeException("Não há subtipos para esse Tipo de vagão!");
            }
            
            // agora, dentro dos subtipos permitidos, faz-se a busca pelo subtipo
            return 
                getSubtipo(subtiposPermitidos, toUpper(letras.toCharArray()[1])) != null;
        }
        return false;        
    }
   
    /**
     * Metodo que verifica um PMA e o retorna.
     * 
     * @param c
     *            caractere a ser verificado
     * @return boolean indicando se o caractere foi obtido de pmas
     */    
    public boolean verificaPMA(char c){
        c = toUpper(c);
        return pmas.get(c) != null;
    }
    
    /**
     * Metodo que busca um caractere de pmas e o retorna.
     * 
     * @param c
     *            caractere a ser buscado
     * @return caractere buscado
     */
    public PB getPMA(char c){
        c = toUpper(c);
        return pmas.get(c);
    }
    
    /**
     * Metodo que busca um tipo e o retorna.
     * 
     * @param c
     *            caractere a ser buscado
     * @return tipo buscado a partir do caractere
     */    
    public Tipo getTipo(char c){
        c = toUpper(c);
        return tipos.get(c);
    }

    /**
     * Metodo que busca um tipo e o retorna.
     * 
     * @param c
     *            caractere a ser buscado
     * @return tipo buscado a partir do caractere
     */        
    public Tipo getTipoR(String c){
        return tiposR.get(c);
    }
    
    /**
     * Metodo que busca um tipo pelo nome e o retorna.
     * 
     * @param c
     *            caractere a ser buscado
     * @return tipo buscado a partir do caractere
     */        
    public Tipo getTipoByName(String c){
        return tiposR.get(c);
    }
    
    /**
     * Metodo que busca um tipo pela letra e o retorna.
     * 
     * @param c
     *            caractere a ser buscado
     * @return tipo buscado a partir do caractere
     */        
    public Character getLetraPorTipo(String c){
        return tiposLetras.get(c);
    }
    
    /**
     * Metodo que busca por todos os tipos.
     * 
     * @return tipos
     */          
    public String[] getTipos(){
        Object[] data = tiposR.values().toArray();
        String[] result = new String[data.length];
        
        for(int i = 0 ; i < data.length ; i ++){
            result[i] = data[i].toString();
        }
        
        return result;
    }
    
    /**
     * Metodo que busca por todos os pesos e bitolas.
     * 
     * @return pmas
     */          
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
    
    /**
     * Metodo que busca os subtipos de um tipo.
     * 
     * @param t
     *            
     * @return subtipos buscados
     */    
    public ArrayList<Subtipo> getSubtipos(Tipo t){      
        return subtipos.get(t);
    }
    
    /**
     * Metodo que busca um subtipo especifico de um tipo.
     * 
     * @param array
     *                array list de subtipos
     * @param c
     *            caractere a ser usado para busca do subtipo
     * @return subtipo, ou nulo caso não seja encontrado
     */    
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
    
    /**
     * Metodo que busca um proprietario a partir de definicoes pre estabelecidas.
     * 
     * @param id
     *             string contendo o id do proprietario
     * @return
     *           retorna o tipo de proprietario, ou lanca uma excecao caso este seja invalido
     */    
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
     */
    public Helper(){
        init();
    }
   
    /**
     * Metodo que retorna a instancia da classe.
     * 
     * @return instancia
     */
    public static final Helper getInstance(){
        if(instance==null){
            instance = new Helper();
        }
        return instance;
    }
    
    /**
     * Metodo auxiliar para tornar o caracter maiusculo.
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
