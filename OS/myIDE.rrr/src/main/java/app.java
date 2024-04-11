
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author lylem
 */
public class app extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    String Filename = "";
    String fname = "";
    String text = "";
    String copied = "heyheyheyhey";
//    String ogText;
    boolean isSaved = true;
    boolean addedSomething = true;
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<Boolean> saved = new ArrayList<>();
    UndoManager undoManager = new UndoManager();
    DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
    JTextComponent selectedTextAreax;
    public app() {
        initComponents();

         //------------------sisiw logo-----//
        Image img = new ImageIcon(this.getClass().getResource("/SplashScreen/Bee.png")).getImage();
        this.setIconImage(img);
        setTitle("Honey OS");
        
        
        fileNames.add(null);
        saved.add(true);
        addNewFile();

    }

        private void addNewFile(){
        addedSomething = true;
        JPanel newTab = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.getDocument().addDocumentListener(new DocumentListener(){
        @Override
        public void insertUpdate(DocumentEvent e){
            updateLabel();
        }

        @Override
        public void removeUpdate(DocumentEvent e){
            updateLabel();
        }

        @Override
        public void changedUpdate(DocumentEvent e){
            updateLabel();
        }

        private void updateLabel(){
            saved.set(jTabbedPane1.getSelectedIndex(), false);
            saveAs.setEnabled(true);
            if(fileNames.get(jTabbedPane1.getSelectedIndex()) != null){
                save.setEnabled(true);
            }
        }
        });
        
        textArea.getDocument().addUndoableEditListener(undoManager);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        newTab.add(scrollPane, BorderLayout.CENTER);

        int tabCount = jTabbedPane1.getTabCount() + 1;
        jTabbedPane1.addTab("Tab " + tabCount, newTab);
        jTabbedPane1.setSelectedIndex(tabCount - 1);
        jTabbedPane1.setTabComponentAt(jTabbedPane1.indexOfComponent(newTab),getTitlePanel(jTabbedPane1, newTab, "new Tab"));
    }

        
private void saveAsFunction(){
        int selectedTabIndex = jTabbedPane1.getSelectedIndex();
        saved.set(selectedTabIndex, true);
            String text = "";
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(this);
                   String extension = ".txt";
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Filename = selectedFile.getAbsolutePath() + extension;
                    fname = selectedFile.getName();
                    try{
                        BufferedWriter bw = new BufferedWriter(new FileWriter(Filename));
                        if (selectedTabIndex != -1) {
                        Component selectedTab = jTabbedPane1.getComponentAt(selectedTabIndex);
                        JTextComponent selectedTextArea = findTextAreaInScrollPane(selectedTab);
                        if (selectedTextArea != null) {
                        text = selectedTextArea.getText();
                        jTabbedPane1.setTabComponentAt(selectedTabIndex,getTitlePanel(jTabbedPane1,(JPanel)jTabbedPane1.getComponentAt(selectedTabIndex), fname+".txt"));
                        jTabbedPane1.getSelectedIndex();
                        }
                        }
                        bw.write(text);
                        bw.close();
                        fileNames.set(selectedTabIndex, Filename);
                        }
                    catch(Exception ex){
                        ex.printStackTrace();
                        }
                    JOptionPane.showMessageDialog(this, "File saved: " + Filename );
                    isSaved = true;
                }
        
        save.setEnabled(false);
        saveAs.setEnabled(false);
    }

        private void saveFunction(){
        int selectedTabIndex = jTabbedPane1.getSelectedIndex();
        saved.set(selectedTabIndex, true);
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileNames.get(selectedTabIndex)));
            if (selectedTabIndex != -1) {
            Component selectedTab = jTabbedPane1.getComponentAt(selectedTabIndex);
            JTextComponent selectedTextArea = findTextAreaInScrollPane(selectedTab);
            if (selectedTextArea != null) {
            text = selectedTextArea.getText();
            }
            }
            bw.write(text);
            bw.close();
            
            JOptionPane.showMessageDialog(this, "File saved: " + fileNames.get(selectedTabIndex));
//            isSaved = true;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        save.setEnabled(false);
        saveAs.setEnabled(false);
    }


    private JPanel getTitlePanel(final JTabbedPane tabbedPane, final JPanel panel, String title)
    {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        titlePanel.add(titleLbl);
        JButton closeButton = new JButton();
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/closeclose.png")));
        closeButton.setRolloverIcon(new ImageIcon(getClass().getResource("/icons/closeIcon.png"))); // Specify a different icon for hover
        closeButton.setOpaque(true);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);

closeButton.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseEntered(MouseEvent e) {
        closeButton.setBackground(Color.RED); // Change the background color on hover
    }

    @Override
    public void mouseExited(MouseEvent e) {
        closeButton.setBackground(null); // Reset the background color when the mouse exits
    }
});

//// Add an ActionListener to handle button click events
//closeButton.addActionListener(new ActionListener() {
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        // Handle button click action here (e.g., closing the tab)
//    }
//});

        closeButton.addMouseListener(new MouseAdapter()
    {
                @Override
        public void mouseClicked(MouseEvent e)
        {
//            if (fileNames.size() > 1){
//            }
            if(fileNames.size() == 1){
                    int responseClose = JOptionPane.showConfirmDialog(null, "Deleting the tab would close the application, Continue?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (responseClose == JOptionPane.YES_OPTION){
                        if (saved.get(jTabbedPane1.getSelectedIndex()) == false){
                            int response = JOptionPane.showConfirmDialog(null, "Do you want to save your changes first?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);

                            if (response == JOptionPane.YES_OPTION){
                                if(fileNames.get(jTabbedPane1.getSelectedIndex()) != null){
                                saveFunction();
                                }
                                else {
                                    saveAsFunction();
                                }
                            
                        }
                        else if (response == JOptionPane.NO_OPTION){
                            tabbedPane.remove(panel);
                            fileNames.remove(jTabbedPane1.getSelectedIndex()+1);
                            saved.remove(jTabbedPane1.getSelectedIndex()+1);
                        }
                        }
                        else{
                        tabbedPane.remove(panel);
                        fileNames.remove(jTabbedPane1.getSelectedIndex()+1);
                        saved.remove(jTabbedPane1.getSelectedIndex()+1);
                        }
                                System.exit(0);
                        }
                }
                else{
//                    int responseClose = JOptionPane.showConfirmDialog(null, "Deleting the tab would close the application, Continue?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
//                    if (responseClose == JOptionPane.YES_OPTION){
                        if (saved.get(jTabbedPane1.getSelectedIndex()) == false){
                            int response = JOptionPane.showConfirmDialog(null, "Do you want to save your changes first?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);

                            if (response == JOptionPane.YES_OPTION){
                                if(fileNames.get(jTabbedPane1.getSelectedIndex()) != null){
                                saveFunction();
                                tabbedPane.remove(panel);
                                fileNames.remove(jTabbedPane1.getSelectedIndex()+1);
                                saved.remove(jTabbedPane1.getSelectedIndex()+1);
                            }   
                            else {
                            saveAsFunction();
                            tabbedPane.remove(panel);
                            fileNames.remove(jTabbedPane1.getSelectedIndex()+1);
                            saved.remove(jTabbedPane1.getSelectedIndex()+1);
                            }
                        }
                        else if (response == JOptionPane.NO_OPTION){
                            tabbedPane.remove(panel);
                            fileNames.remove(jTabbedPane1.getSelectedIndex()+1);
                            saved.remove(jTabbedPane1.getSelectedIndex()+1);
                        }
                        }
                        else{
                        tabbedPane.remove(panel);
                        fileNames.remove(jTabbedPane1.getSelectedIndex()+1);
                        saved.remove(jTabbedPane1.getSelectedIndex()+1);
                        }
//                                System.exit(0);
//                        }
            }
            
        }
        });
        titlePanel.add(closeButton);

        return titlePanel;
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        ImageIcon icon=new ImageIcon(getClass().getResource("bee 3.jpg"));
        Image img=icon.getImage();
        jDesktopPane1 = new javax.swing.JDesktopPane()
        {
            public void paintComponent(Graphics g)
            {
                g.drawImage(img,0,0,getWidth(),
                    getHeight(),this);
            }

        };
        jTabbedPane1 = new raven.tabbed.TabbedPaneCustom();
        jScrollPane1 = new javax.swing.JScrollPane();
        notes = new javax.swing.JTextArea();
        undo = new javax.swing.JButton();
        redo = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        newFile = new javax.swing.JButton();
        openFile = new javax.swing.JButton();
        save = new javax.swing.JButton();
        saveAs = new javax.swing.JButton();
        run = new javax.swing.JButton();
        compile = new javax.swing.JButton();
        Note = new javax.swing.JButton();
        highlight = new javax.swing.JButton();
        unhighlight = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Filename);
        setBackground(new java.awt.Color(209, 197, 223));

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jTabbedPane1.setSelectedColor(new java.awt.Color(254, 191, 61));

        notes.setColumns(20);
        notes.setRows(5);
        jScrollPane1.setViewportView(notes);

        undo.setBackground(new java.awt.Color(254, 191, 61));
        undo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/undooo.png"))); // NOI18N
        compile.setEnabled(false);
        undo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                undoMouseEntered(evt);
            }
        });
        undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoActionPerformed(evt);
            }
        });

        redo.setBackground(new java.awt.Color(254, 191, 61));
        redo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/redooo.png"))); // NOI18N
        compile.setEnabled(false);
        redo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                redoMouseEntered(evt);
            }
        });
        redo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(88, 51, 33));

        newFile.setBackground(new java.awt.Color(254, 191, 61));
        newFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/new file.png"))); // NOI18N
        newFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newFileMouseEntered(evt);
            }
        });
        newFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newFileActionPerformed(evt);
            }
        });

        openFile.setBackground(new java.awt.Color(254, 191, 61));
        openFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open file.png"))); // NOI18N
        openFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openFileMouseEntered(evt);
            }
        });
        openFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileActionPerformed(evt);
            }
        });

        save.setBackground(new java.awt.Color(254, 191, 61));
        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save.png"))); // NOI18N
        save.setEnabled(false);
        save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveMouseEntered(evt);
            }
        });
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        saveAs.setBackground(new java.awt.Color(254, 191, 61));
        saveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save as.png"))); // NOI18N
        saveAs.setEnabled(false);
        //saveAs.setEnabled(false);
        saveAs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveAsMouseEntered(evt);
            }
        });
        saveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsActionPerformed(evt);
            }
        });

        run.setBackground(new java.awt.Color(254, 191, 61));
        run.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/run.png"))); // NOI18N
        run.setEnabled(false);
        run.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                runMouseEntered(evt);
            }
        });
        run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runActionPerformed(evt);
            }
        });

        compile.setBackground(new java.awt.Color(254, 191, 61));
        compile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/compile.png"))); // NOI18N
        compile.setEnabled(false);
        compile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                compileMouseEntered(evt);
            }
        });
        compile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileActionPerformed(evt);
            }
        });

        Note.setBackground(new java.awt.Color(254, 191, 61));
        Note.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/noteee.png"))); // NOI18N
        compile.setEnabled(false);
        Note.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                NoteMouseEntered(evt);
            }
        });
        Note.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoteActionPerformed(evt);
            }
        });

        highlight.setBackground(new java.awt.Color(254, 191, 61));
        highlight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/highlight.png"))); // NOI18N
        compile.setEnabled(false);
        highlight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                highlightMouseEntered(evt);
            }
        });
        highlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightActionPerformed(evt);
            }
        });

        unhighlight.setBackground(new java.awt.Color(254, 191, 61));
        unhighlight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/unhighlight.png"))); // NOI18N
        compile.setEnabled(false);
        unhighlight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                unhighlightMouseEntered(evt);
            }
        });
        unhighlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unhighlightActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newFile, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(openFile, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveAs, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(run, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compile, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Note, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(highlight, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unhighlight, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(highlight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(unhighlight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(openFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Note, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(compile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(run, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveAs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //save.setEnabled(false);

        jDesktopPane1.setLayer(jTabbedPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(undo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(redo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1259, Short.MAX_VALUE)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(undo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(redo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(redo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(undo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (jTabbedPane1.getSelectedIndex()>=0){
                    int selectedTabIndex = jTabbedPane1.getSelectedIndex();
                    Component selectedTab = jTabbedPane1.getComponentAt(selectedTabIndex);

                    if (saved.get(selectedTabIndex) == true){
                        save.setEnabled(false);
                        saveAs.setEnabled(false);
                    }
                    else{
                        saveAs.setEnabled(true);
                        if (fileNames.get(selectedTabIndex) != null){
                            save.setEnabled(true);
                        }
                    }

                    if (fileNames.get(selectedTabIndex) != null){
                        run.setEnabled(true);
                        compile.setEnabled(true);
                    }
                    else{
                        run.setEnabled(false);
                        compile.setEnabled(false);
                    }
                }
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

 
     private static JTextArea findTextAreaInScrollPane(Component component) {
        if (component instanceof Container) {
            Component[] components = ((Container) component).getComponents();
            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    Component[] scrollPaneComponents = ((JScrollPane) comp).getViewport().getComponents();
                    for (Component scrollComp : scrollPaneComponents) {
                        if (scrollComp instanceof JTextArea) {
                            return (JTextArea) scrollComp;
                        }
                    }
                }
            }
        }
        return null;
    }
     
    
    private void undoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_undoMouseEntered
        // TODO add your handling code here:
        undo.setToolTipText("undo");
    }//GEN-LAST:event_undoMouseEntered

    private void undoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoActionPerformed
        // TODO add your handling code here:
        if(undoManager.canUndo()){
            try{
                undoManager.undo();
            }
            catch(CannotUndoException ex){
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_undoActionPerformed

    private void redoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redoMouseEntered
        // TODO add your handling code here:
        redo.setToolTipText("redo");
    }//GEN-LAST:event_redoMouseEntered

    private void redoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoActionPerformed
        // TODO add your handling code here:
        if(undoManager.canRedo()){
            try{
                undoManager.redo();
            }
            catch(CannotRedoException ex){
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_redoActionPerformed

    private void unhighlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unhighlightActionPerformed
        // TODO add your handling code here:
        selectedTextAreax.getHighlighter().removeAllHighlights();
    }//GEN-LAST:event_unhighlightActionPerformed

    private void unhighlightMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unhighlightMouseEntered
        // TODO add your handling code here:
        unhighlight.setToolTipText("Unhighlight");
    }//GEN-LAST:event_unhighlightMouseEntered

    private void highlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightActionPerformed
        // TODO add your handling code here:
        try{
            int selectedTabIndex = jTabbedPane1.getSelectedIndex();
            Component selectedTab = jTabbedPane1.getComponentAt(selectedTabIndex);
            selectedTextAreax = findTextAreaInScrollPane(selectedTab);
            String selectedText = selectedTextAreax.getSelectedText();

            if(selectedText != null){
                int start = selectedTextAreax.getSelectionStart();
                int end = selectedTextAreax.getSelectionEnd();
                selectedTextAreax.getHighlighter().addHighlight(start, end, highlightPainter);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

    }//GEN-LAST:event_highlightActionPerformed

    private void highlightMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_highlightMouseEntered
        // TODO add your handling code here:
        highlight.setToolTipText("Highlight");
    }//GEN-LAST:event_highlightMouseEntered

    private void NoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoteActionPerformed
        // TODO add your handling code here:
        int selectedTabIndex = jTabbedPane1.getSelectedIndex();
        Component selectedTab = jTabbedPane1.getComponentAt(selectedTabIndex);
        JTextComponent selectedTextArea = findTextAreaInScrollPane(selectedTab);
        String selectedText = selectedTextArea.getSelectedText();
        notes.append(selectedText);
        notes.append("\n---------------------------------------------------\n\n");
    }//GEN-LAST:event_NoteActionPerformed

    private void NoteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NoteMouseEntered
        // TODO add your handling code here:
        Note.setToolTipText("Note");
    }//GEN-LAST:event_NoteMouseEntered

    private void saveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsActionPerformed

        // TODO add your handling code here:
        saveAsFunction();
        saveAs.setEnabled(false);
    }//GEN-LAST:event_saveAsActionPerformed

    private void saveAsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveAsMouseEntered
        // TODO add your handling code here:
        saveAs.setToolTipText("Save As");
    }//GEN-LAST:event_saveAsMouseEntered

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        // TODO add your handling code here:
        saveFunction();
    }//GEN-LAST:event_saveActionPerformed

    private void saveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseEntered
        // TODO add your handling code here:
        save.setToolTipText("Save");
    }//GEN-LAST:event_saveMouseEntered

    private void openFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                StringBuilder fileContents = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContents.append(line).append("\n");
                }
                reader.close();
                JPanel newTab = new JPanel(new BorderLayout());
                JTextArea textArea = new JTextArea();
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                JScrollPane scrollPane = new JScrollPane(textArea);
                newTab.add(scrollPane, BorderLayout.CENTER);
                textArea.setText(fileContents.toString());
                textArea.getDocument().addDocumentListener(new DocumentListener(){
                    @Override
                    public void insertUpdate(DocumentEvent e){
                        updateLabel();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e){
                        updateLabel();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e){
                        updateLabel();
                    }

                    private void updateLabel(){
                        saved.set(jTabbedPane1.getSelectedIndex(), false);
                        saveAs.setEnabled(true);
                        save.setEnabled(true);
                    }
                });
                Filename = selectedFile.getAbsolutePath();
                fname = selectedFile.getName();
                fileNames.add(Filename);
                saved.add(true);
                int tabCount = jTabbedPane1.getTabCount() + 1;
                jTabbedPane1.addTab("Tab " + tabCount, newTab);
                jTabbedPane1.setSelectedIndex(tabCount - 1);
                jTabbedPane1.setTabComponentAt(jTabbedPane1.indexOfComponent(newTab),getTitlePanel(jTabbedPane1, newTab, fname));
                setTitle(Filename);
                save.setEnabled(false);
                saveAs.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_openFileActionPerformed

    private void openFileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openFileMouseEntered
        // TODO add your handling code here:
        openFile.setToolTipText("Open File");
    }//GEN-LAST:event_openFileMouseEntered

    private void newFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newFileActionPerformed
        // TODO add your handling code here:
        fileNames.add(null);
        saved.add(true);
        addNewFile();
    }//GEN-LAST:event_newFileActionPerformed

    private void newFileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newFileMouseEntered
        // TODO add your handling code here:
        newFile.setToolTipText("New File");
    }//GEN-LAST:event_newFileMouseEntered

    private void compileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileActionPerformed
        // TODO add your handling code here:
        saveFunction();
        compile Compile = new compile();
        Compile.setVisible(true);
    }//GEN-LAST:event_compileActionPerformed

    private void compileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_compileMouseEntered
        // TODO add your handling code here:
        compile.setToolTipText("Compile");
    }//GEN-LAST:event_compileMouseEntered

    private void runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runActionPerformed
        // TODO add your handling code here:
        saveFunction();

        compile Compile = new compile();
        Compile.setVisible(true);

        run Run = new run();
        Run.setVisible(true);
        //       JOptionPane.showMessageDialog(this, "File saved: " + Filename );
    }//GEN-LAST:event_runActionPerformed

    private void runMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_runMouseEntered
        // TODO add your handling code here:
        run.setToolTipText("Run");
    }//GEN-LAST:event_runMouseEntered

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Mainmain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mainmain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mainmain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mainmain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
//        System.out.println("HELLO WORLD!");
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mainmain().setVisible(true);
            }
        });
        

        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Note;
    private javax.swing.JButton compile;
    private javax.swing.JButton highlight;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private raven.tabbed.TabbedPaneCustom jTabbedPane1;
    private javax.swing.JButton newFile;
    private javax.swing.JTextArea notes;
    private javax.swing.JButton openFile;
    private javax.swing.JButton redo;
    private javax.swing.JButton run;
    private javax.swing.JButton save;
    private javax.swing.JButton saveAs;
    private javax.swing.JButton undo;
    private javax.swing.JButton unhighlight;
    // End of variables declaration//GEN-END:variables
}
