package views;

import database.DB;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import models.Composicao;
import models.Locomotiva;
import models.Vagao;

/**
 * Objeto de testes do modelo.
 *
 * @author Leonardo Momente
 */
public class CadastroComposicao extends JDialog{

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;

    private DefaultListModel dm1, dm2, dm3;
    private DB db = DB.getInstance();
    private Connection conn = db.getConnection();

    private ArrayList<Vagao> vagoes = Vagao.getAll();
    private ArrayList<Locomotiva> locomotivas = Locomotiva.getAll();

    private ArrayList<Locomotiva> locs_na_lista = new ArrayList<>();
    private ArrayList<Vagao> vags_na_lista = new ArrayList<>();
    
    /**
     * Classe aninhada para controle do Drag & Drop.
     */
    class ListHandler extends TransferHandler {

        public boolean canImport(TransferSupport support) {
            if (!support.isDrop()) {
                return false;
            }
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }
            Transferable t = support.getTransferable();
            String line;
            try {
                line = (String) t.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return false;
            }

            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            int index = dl.getIndex();

            // Regras customizadas
            if (line.contains("-")) { // É um vagão?
                Vagao v = new Vagao(
                        line.substring(0, 3),
                        line.substring(4, 10),
                        Integer.parseInt(line.substring(11, 12))
                );
                
                // FAZER VALIDAÇÃO DOS ELEMENTOS!
                
                vags_na_lista.add(v);
                vags_na_lista.sort(v);
            } else {
                if (locs_na_lista.size() > 2) { // Se for uma locomotiva, então há mais de 2?
                    JOptionPane.showMessageDialog(null, "A composição só pode ter 3 locomotivas!");

                } else { // Se for possível, adiciona locomotiva
                    locs_na_lista.add(new Locomotiva(Integer.parseInt(line)));
                }
            }

            dm3.clear();

            locs_na_lista.forEach((l) -> {
                dm3.addElement(l.getClasse());
            });

            vags_na_lista.forEach((v) -> {
                dm3.addElement(v.getLetras() + "/" + v.getId() + "-" + v.getDigito());
            });

            jList3.setModel(dm3);

            return true;
        }
    }

    /**
     * Metodo para cadastro de composicao.
     */
    public CadastroComposicao() {
        initComponents();

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        /*
            Processo para salvar antes de sair
        */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "Deseja salvar a sua composição?");
                /*
                    0 - Sim
                    1 - Não
                    2 - Cancelar
                */
                switch(i){
                    case 0:
                        salvar();   
                        dispose();
                        break;
                    case 1:
                        dispose();
                        break;
                    case 2:
                        
                        break;
                }
            }
        });
        
        jList3.setTransferHandler(new ListHandler());
        jList3.setDropMode(DropMode.INSERT);

        dm1 = new DefaultListModel();
        dm2 = new DefaultListModel();
        dm3 = new DefaultListModel();

        // adiciona aos modelos das listas
        vagoes.forEach((v) -> {
            dm1.addElement(v.getLetras() + "/" + v.getId() + "-" + v.getDigito());
        });

        locomotivas.forEach((l) -> {
            dm2.addElement(l.getClasse());
        });

        jList1.setModel(dm1);
        jList2.setModel(dm2);

    }
    
    /**
     * Metodo que salva no banco de dados.
     */
    private void salvar(){
        String nome = (String) JOptionPane.showInputDialog("Digite o nome da composição:");
        Composicao c = new Composicao(nome);
        
        // Para todos os elementos do jList
        for(int i=0; i<jList3.getModel().getSize(); i++){
            String elemento = String.valueOf(jList3.getModel().getElementAt(i));                        
            
            if(elemento.contains("-")){ // É um vagão
                vagoes.forEach((v) -> {
                    if(v.getId().equals(elemento.substring(4, 10))){
                        c.addVagao(v);
                    }
                });
            }else{ // Então é uma locomotiva
                locomotivas.forEach((l) -> {
                    if(l.getClasse() == Integer.parseInt(elemento)){
                        c.addLocomotiva(l);
                    }
                });
            }
        }
        
        c.save();
        JOptionPane.showMessageDialog(null, "Composição: "+nome+" adicionada!");
        
        Menu.getInstance().atualizarComposicoes();
        
    }

    @SuppressWarnings("unchecked")
    
    /**
     * Metodo que inicializa componentes.
     */
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Composição");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

            /**
             * Metodo que busca o tamanho.
             * @return tamanho 
             */
            @Override
            public int getSize() {
                return strings.length;
            }

            /**
             * Metodo que busca um elemento especifico.
             * @return elemento
             */
            @Override
            public String getElementAt(int i) {
                return strings[i];
            }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jList1.setDragEnabled(true);
        jScrollPane1.setViewportView(jList1);

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        jList2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jList2.setDragEnabled(true);
        jScrollPane2.setViewportView(jList2);

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Vagões");

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Locomotivas");

        jList3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList3.setDropMode(javax.swing.DropMode.INSERT);
        jScrollPane3.setViewportView(jList3);

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Composição");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                                        .addComponent(jScrollPane1))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jScrollPane2)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)))
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                                        .addComponent(jScrollPane2))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

}
