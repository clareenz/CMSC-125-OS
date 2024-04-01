
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import static javax.print.DocFlavor.BYTE_ARRAY.GIF;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Nikka Mendoza
 */
public class welcome extends javax.swing.JFrame {

    /**
     * Creates new form welcome
     */
    private Timer welcomeTimer;

    public welcome() {
        initComponents();

    //------------------sisiw logo-----//
    Image img = new ImageIcon(this.getClass().getResource("/sisiwlanglogo.png")).getImage();
     

    this.setIconImage(img);
        // Create a JLabel to hold the GIF image
        JLabel gifLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/welcomeScreen.gif")));
        
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Set the size of the label to match the screen size
        gifLabel.setSize(screenSize);
        gifLabel.setPreferredSize(screenSize);
        
        // Scale the image to fit the label
        ImageIcon imageIcon = (ImageIcon) gifLabel.getIcon();
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(screenSize.width, screenSize.height, java.awt.Image.SCALE_DEFAULT));
        gifLabel.setIcon(imageIcon);
        
        // Add the label to the frame
        add(gifLabel);
        
        // Set the JFrame to fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    
    //---timer-----//
    int welcomeDuration = 19000; // 9.9 seconds
    welcomeTimer  = new Timer(welcomeDuration, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Close the welcome box when the timer expires
            welcomeTimer.stop();
            dispose(); // Close the WelcomeFrame

            // -------call the main-----//
            Mainmain Main = new Mainmain();
            Main.setVisible(true);
        }
    });

    // Start the timer
    welcomeTimer.start ();
}

/**
 * This method is called from within the constructor to initialize the form.
 * WARNING: Do NOT modify this code. The content of this method is always
 * regenerated by the Form Editor.
 */
@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1537, 1537, 1537)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(750, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(186, 186, 186))
        );

        setSize(new java.awt.Dimension(1543, 936));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(welcome.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);

} catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(welcome.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);

} catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(welcome.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);

} catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(welcome.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new welcome().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
