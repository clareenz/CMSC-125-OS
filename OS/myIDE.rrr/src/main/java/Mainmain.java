
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author lylem
 */
public class Mainmain extends javax.swing.JFrame {

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
    app appPage = new app();

    //For Voice Recognition
    boolean keyHeldDown;
    File audioFile = new File("audio.wav"); // Temporarily save audio to a file;
    // Initialize AssemblyAI client
    AssemblyAI client = AssemblyAI.builder()
            .apiKey("927564b14b94443aa18ea5d9ee6eb6db")
            .build();

    // Set up microphone audio format
    AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
    DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
    TargetDataLine targetDataLine;

    public Mainmain() {
        initComponents();
        // Set the JFrame to fullscreen
        // setExtendedState(JFrame.MAXIMIZED_BOTH);

        //------------------sisiw logo-----//
        Image img = new ImageIcon(this.getClass().getResource("/SplashScreen/Bee.png")).getImage();
        this.setIconImage(img);
        setTitle("Honey OS");

        fileNames.add(null);
        saved.add(true);

        //Voice
        keyHeldDown = false;

        // Add this code inside your constructor or initialization method
        InputMap inputMap = jDesktopPane1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = jDesktopPane1.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "startRecording");
        actionMap.put("startRecording", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!keyHeldDown) {
                    keyHeldDown = true;
                    try {
                        // Check if microphone is supported
                        if (!AudioSystem.isLineSupported(targetInfo)) {
                            System.out.println("Microphone not supported");
                            System.exit(1);
                        }

                        // Open microphone
                        targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
                        targetDataLine.open(audioFormat);

                    } catch (LineUnavailableException x) {
                        System.out.println("Microphone not available: " + x.getMessage());
                    }
                    System.out.println("Push-to-talk activated. Start speaking...");

                    // Reopen the TargetDataLine
                    try {
                        targetDataLine.open(audioFormat);
                        targetDataLine.start();
                    } catch (LineUnavailableException ex) {
                        System.out.println("Error opening the TargetDataLine: " + ex.getMessage());
                    }

                    // Create a new thread for recording audio
                    Thread audioRecord = new Thread() {
                        @Override
                        public void run() {
                            try {
                                AudioSystem.write(new AudioInputStream(targetDataLine), AudioFileFormat.Type.WAVE, audioFile);
                            } catch (IOException ex) {
                                Logger.getLogger(Mainmain.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    };
                    audioRecord.start(); // Start the recording thread
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "stopRecording");
        actionMap.put("stopRecording", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Stop capturing audio when key is released
                keyHeldDown = false;
                System.out.println("Push-to-talk deactivated.");
                targetDataLine.stop();
                targetDataLine.close();
                try {
                    Transcript transcript = client.transcripts().transcribe(audioFile);
                    System.out.println("Transcript: " + transcript.getText());
                    String regex = "Optional\\[(.*)\\]";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(transcript.getText().toString());

                    if (matcher.find()) {
                        String extractedString = matcher.group(1).toLowerCase(); // Convert to lowercase
                        extractedString = extractedString.replace(",", ""); // Remove commas
                        extractedString = extractedString.replace(".", ""); // Remove periods
                        System.out.println(extractedString);
                        Commands(extractedString);
                    }

                } catch (IOException ex) {
                    System.out.println("Error during transcription: ");
                }
            }
        });
        //Voice

    }

    public void Commands(String a) {
        if (a.equals("open notepad please")) {
            appPage.setVisible(true);
        }
        if (a.equals("close notepad please")) {
            appPage.setVisible(false);
        }
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

        ImageIcon icon=new ImageIcon(getClass().getResource("Background.png"));
        Image img=icon.getImage();
        jDesktopPane1 = new javax.swing.JDesktopPane()
        {
            public void paintComponent(Graphics g)
            {
                g.drawImage(img,0,0,getWidth(),
                    getHeight(),this);
            }

        };
        jPanel1 = new javax.swing.JPanel();
        NotepadButton = new javax.swing.JButton();
        WordButton = new javax.swing.JButton();
        SteamButton = new javax.swing.JButton();
        FileManagerButton = new javax.swing.JButton();
        ChromeButton = new javax.swing.JButton();
        DotaButton = new javax.swing.JButton();
        ExcelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        DatePanel = new javax.swing.JPanel();
        Country = new javax.swing.JLabel();
        Date = new javax.swing.JLabel();
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the label with the current time
                Date currentTime = new Date();

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Time.setText(sdf.format(currentTime));

                sdf.applyPattern("a");
                amPm.setText(sdf.format(currentTime));

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                Date.setText(dateFormat.format(currentTime));

            }
        });
        timer.start();
        Time = new javax.swing.JLabel();
        timeWidget = new javax.swing.JLabel();
        amPm = new javax.swing.JLabel();
        dateWidget = new javax.swing.JLabel();
        MusicPanel = new javax.swing.JPanel();
        Widget = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        NewFile = new javax.swing.JButton();
        OpenFile = new javax.swing.JButton();
        DeleteFile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList<>();
        String userHome = System.getProperty("user.home");         
        fileList = new JList<>(getFilesInDirectory(userHome + File.separator + "Desktop\\HoneyOS_Documents"));
        FileManagerWindow = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(Filename);
        setBackground(new java.awt.Color(209, 197, 223));
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(209, 197, 223));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.setPreferredSize(new java.awt.Dimension(2000, 2000));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NotepadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Notepad.png"))); // NOI18N
        NotepadButton.setBorder(null);
        NotepadButton.setBorderPainted(false);
        NotepadButton.setContentAreaFilled(false);
        NotepadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                NotepadButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                NotepadButtonMouseExited(evt);
            }
        });
        NotepadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NotepadButtonActionPerformed(evt);
            }
        });
        jPanel1.add(NotepadButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, 200, 220));

        WordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Word.png"))); // NOI18N
        WordButton.setBorder(null);
        WordButton.setBorderPainted(false);
        WordButton.setContentAreaFilled(false);
        WordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                WordButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                WordButtonMouseExited(evt);
            }
        });
        jPanel1.add(WordButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, -1, -1));

        SteamButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Steam.png"))); // NOI18N
        SteamButton.setBorder(null);
        SteamButton.setBorderPainted(false);
        SteamButton.setContentAreaFilled(false);
        SteamButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SteamButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SteamButtonMouseExited(evt);
            }
        });
        SteamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SteamButtonActionPerformed(evt);
            }
        });
        jPanel1.add(SteamButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, -1, -1));

        FileManagerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/FileManager.png"))); // NOI18N
        FileManagerButton.setBorder(null);
        FileManagerButton.setBorderPainted(false);
        FileManagerButton.setContentAreaFilled(false);
        FileManagerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                FileManagerButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                FileManagerButtonMouseExited(evt);
            }
        });
        FileManagerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileManagerButtonActionPerformed(evt);
            }
        });
        jPanel1.add(FileManagerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 160, -1));

        ChromeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Chrome.png"))); // NOI18N
        ChromeButton.setBorder(null);
        ChromeButton.setBorderPainted(false);
        ChromeButton.setContentAreaFilled(false);
        ChromeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ChromeButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ChromeButtonMouseExited(evt);
            }
        });
        jPanel1.add(ChromeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 30, -1, -1));

        DotaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Dota.png"))); // NOI18N
        DotaButton.setBorder(null);
        DotaButton.setBorderPainted(false);
        DotaButton.setContentAreaFilled(false);
        DotaButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                DotaButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                DotaButtonMouseExited(evt);
            }
        });
        jPanel1.add(DotaButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 40, -1, -1));

        ExcelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Excel.png"))); // NOI18N
        ExcelButton.setBorder(null);
        ExcelButton.setBorderPainted(false);
        ExcelButton.setContentAreaFilled(false);
        ExcelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ExcelButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ExcelButtonMouseExited(evt);
            }
        });
        jPanel1.add(ExcelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1330, 50, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bar.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 0, -1, -1));

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DatePanel.setOpaque(false);
        DatePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Country.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        Country.setForeground(new java.awt.Color(224, 0, 0));
        Country.setText("PHILIPPINES");
        DatePanel.add(Country, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        Date.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        Date.setText("April 12, 2024");
        DatePanel.add(Date, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        Time.setFont(new java.awt.Font("Arial", 1, 170)); // NOI18N
        Time.setText("88:88");
        DatePanel.add(Time, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        timeWidget.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TimeWidget.png"))); // NOI18N
        DatePanel.add(timeWidget, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 470, -1));

        amPm.setFont(new java.awt.Font("Arial", 1, 60)); // NOI18N
        amPm.setForeground(new java.awt.Color(224, 0, 0));
        amPm.setText("am");
        DatePanel.add(amPm, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 310, -1, -1));

        dateWidget.setIcon(new javax.swing.ImageIcon(getClass().getResource("/WidgetDate.png"))); // NOI18N
        DatePanel.add(dateWidget, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 680, -1));

        jPanel3.add(DatePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 680, 450));

        MusicPanel.setOpaque(false);

        javax.swing.GroupLayout MusicPanelLayout = new javax.swing.GroupLayout(MusicPanel);
        MusicPanel.setLayout(MusicPanelLayout);
        MusicPanelLayout.setHorizontalGroup(
            MusicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        MusicPanelLayout.setVerticalGroup(
            MusicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        jPanel3.add(MusicPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, 260, 450));

        Widget.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Widget.png"))); // NOI18N
        jPanel3.add(Widget, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NewFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/NewFile.png"))); // NOI18N
        NewFile.setBorderPainted(false);
        NewFile.setContentAreaFilled(false);
        NewFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewFileActionPerformed(evt);
            }
        });
        jPanel4.add(NewFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, -1, -1));

        OpenFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/OpenFile.png"))); // NOI18N
        OpenFile.setBorderPainted(false);
        OpenFile.setContentAreaFilled(false);
        OpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenFileActionPerformed(evt);
            }
        });
        jPanel4.add(OpenFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, -1, -1));

        DeleteFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/DeleteFile.png"))); // NOI18N
        DeleteFile.setBorderPainted(false);
        DeleteFile.setContentAreaFilled(false);
        DeleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteFileActionPerformed(evt);
            }
        });
        jPanel4.add(DeleteFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, -1, -1));

        jScrollPane1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        fileList.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jScrollPane1.setViewportView(fileList);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 520, 210));

        FileManagerWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FileManagerWindow.png"))); // NOI18N
        jPanel4.add(FileManagerWindow, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        jDesktopPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 842, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(823, Short.MAX_VALUE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1926, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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


    private void NotepadButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NotepadButtonMouseEntered
        // TODO add your handling code here:
        NotepadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/NotepadHover.png"))); // NOI18N
    }//GEN-LAST:event_NotepadButtonMouseEntered

    private void NotepadButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NotepadButtonMouseExited
        // TODO add your handling code here:
        NotepadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Notepad.png")));
    }//GEN-LAST:event_NotepadButtonMouseExited

    private void NotepadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NotepadButtonActionPerformed
        // TODO add your handling code here:
        appPage.setVisible(true);
    }//GEN-LAST:event_NotepadButtonActionPerformed

    private void FileManagerButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileManagerButtonMouseEntered
        // TODO add your handling code here:
        FileManagerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/FileManagerHover.png")));
    }//GEN-LAST:event_FileManagerButtonMouseEntered

    private void FileManagerButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileManagerButtonMouseExited
        // TODO add your handling code here:
        FileManagerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/FileManager.png")));
    }//GEN-LAST:event_FileManagerButtonMouseExited

    private void ChromeButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChromeButtonMouseEntered
        // TODO add your handling code here:
        ChromeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/ChromeHover.png")));
    }//GEN-LAST:event_ChromeButtonMouseEntered

    private void ChromeButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChromeButtonMouseExited
        // TODO add your handling code here:
        ChromeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Chrome.png")));
    }//GEN-LAST:event_ChromeButtonMouseExited

    private void SteamButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SteamButtonMouseEntered
        // TODO add your handling code here:
        SteamButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/SteamHover.png")));
    }//GEN-LAST:event_SteamButtonMouseEntered

    private void SteamButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SteamButtonMouseExited
        // TODO add your handling code here:
        SteamButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Steam.png")));
    }//GEN-LAST:event_SteamButtonMouseExited

    private void WordButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_WordButtonMouseEntered
        // TODO add your handling code here:
        WordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/WordHover.png")));
    }//GEN-LAST:event_WordButtonMouseEntered

    private void WordButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_WordButtonMouseExited
        // TODO add your handling code here:
        WordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Word.png")));
    }//GEN-LAST:event_WordButtonMouseExited

    private void DotaButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DotaButtonMouseEntered
        // TODO add your handling code here:
        DotaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/DotaHover.png")));
    }//GEN-LAST:event_DotaButtonMouseEntered

    private void DotaButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DotaButtonMouseExited
        // TODO add your handling code here:
        DotaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Dota.png")));
    }//GEN-LAST:event_DotaButtonMouseExited

    private void ExcelButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExcelButtonMouseEntered
        // TODO add your handling code here:
        ExcelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/ExcelHover.png")));
    }//GEN-LAST:event_ExcelButtonMouseEntered

    private void ExcelButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExcelButtonMouseExited
        // TODO add your handling code here:
        ExcelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProgramIcons/Excel.png")));
    }//GEN-LAST:event_ExcelButtonMouseExited

    private void SteamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SteamButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SteamButtonActionPerformed

    private void NewFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewFileActionPerformed
        // TODO add your handling code here:
        createNewFile();
    }//GEN-LAST:event_NewFileActionPerformed

    private void DeleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteFileActionPerformed
        // TODO add your handling code here:
        deleteSelectedFile();
    }//GEN-LAST:event_DeleteFileActionPerformed

    private void FileManagerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileManagerButtonActionPerformed
        // TODO add your handling code here:
        FileManager fileManager = new FileManager();
        fileManager.setVisible(true);
    }//GEN-LAST:event_FileManagerButtonActionPerformed

    private void OpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenFileActionPerformed
        // TODO add your handling code here:
        openFile();
    }//GEN-LAST:event_OpenFileActionPerformed

    /**
     * @param args the command line arguments
     */
    private void refreshFrame() {
        SwingUtilities.invokeLater(() -> {
            setVisible(false); // Hide the frame
            dispose(); // Dispose the frame
            new FileManager().setVisible(true); // Create and show a new instance of FileManager
        });
    }

    private void openFile() {
        String userHome = System.getProperty("user.home");
        int selectedIndex = fileList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedFileName = fileList.getSelectedValue();
            File fileToOpen = new File(userHome + File.separator + "Desktop\\HoneyOS_Documents" + File.separator + selectedFileName);
            app newApp = new app();
            newApp.openFileFromOutside(fileToOpen);
            newApp.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a file to open!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void createNewFile() {
        String userHome = System.getProperty("user.home");
        String fileName = JOptionPane.showInputDialog(this, "Enter the name of the new file:");
        fileName = fileName + ".txt";
        if (fileName != null && !fileName.isEmpty()) {
            File newFile = new File(userHome + File.separator + "Desktop\\HoneyOS_Documents" + File.separator + fileName);
            try {
                if (newFile.createNewFile()) {
                    JOptionPane.showMessageDialog(this, "New file created: " + fileName);
                    refreshFrame();
                } else {
                    JOptionPane.showMessageDialog(this, "File already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error creating file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        fileList = new JList<>(getFilesInDirectory(userHome + File.separator + "Desktop\\HoneyOS_Documents"));
    }

    private String[] getFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            System.out.println("Directory does not exist. Creating directory...");
            boolean created = directory.mkdirs(); // Attempt to create the directory
            if (!created) {
                System.err.println("Failed to create directory: " + directoryPath);
                return new String[0];
            }
        }

        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {

            System.out.println("Files in directory:");
            for (File file : files) {
                if (file.isFile()) { // Check if it's a regular file
                    System.out.println(file.getName());
                }
            }

            String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
            return fileNames;
        } else {
            System.out.println("No files found in directory: " + directoryPath);
            return new String[0];
        }

    }

    private void deleteSelectedFile() {
        String userHome = System.getProperty("user.home");
        int selectedIndex = fileList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedFileName = fileList.getSelectedValue();
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the file: " + selectedFileName + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                File fileToDelete = new File(userHome + File.separator + "Desktop\\HoneyOS_Documents" + File.separator + selectedFileName);
                if (fileToDelete.delete()) {
                    JOptionPane.showMessageDialog(this, "File deleted: " + selectedFileName);
                    refreshFrame(); // Refresh the frame after deleting the file
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a file to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
    private javax.swing.JButton ChromeButton;
    private javax.swing.JLabel Country;
    private javax.swing.JLabel Date;
    private javax.swing.JPanel DatePanel;
    private javax.swing.JButton DeleteFile;
    private javax.swing.JButton DotaButton;
    private javax.swing.JButton ExcelButton;
    private javax.swing.JButton FileManagerButton;
    private javax.swing.JLabel FileManagerWindow;
    private javax.swing.JPanel MusicPanel;
    private javax.swing.JButton NewFile;
    private javax.swing.JButton NotepadButton;
    private javax.swing.JButton OpenFile;
    private javax.swing.JButton SteamButton;
    private javax.swing.JLabel Time;
    private javax.swing.JLabel Widget;
    private javax.swing.JButton WordButton;
    private javax.swing.JLabel amPm;
    private javax.swing.JLabel dateWidget;
    private javax.swing.JList<String> fileList;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel timeWidget;
    // End of variables declaration//GEN-END:variables
}
