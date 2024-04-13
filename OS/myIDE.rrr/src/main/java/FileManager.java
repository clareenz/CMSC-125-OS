
import java.io.File;
import java.io.IOException;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Anunciado
 */
public class FileManager extends javax.swing.JFrame {

    /**
     * Creates new form FileManager
     */
//    private JList<String> fileList;
    
    public FileManager() {
        initComponents();
       
//        String[] sampleData = {"File 1", "File 2", "File 3"};
//
//// Populate the JList with sample data
//        jList1 = new JList<>(sampleData);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList<>();
        String userHome = System.getProperty("user.home");         
        fileList = new JList<>(getFilesInDirectory(userHome + File.separator + "Desktop\\HoneyOS_Documents"));
        NewFile = new javax.swing.JButton();
        OpenFile = new javax.swing.JButton();
        DeleteFile = new javax.swing.JButton();
        FileManagerWindow = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 255, 153), null));

        fileList.setBorder(null);
        fileList.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jScrollPane1.setViewportView(fileList);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 136, 520, 210));

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

        FileManagerWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FileManagerWindow.png"))); // NOI18N
        jPanel4.add(FileManagerWindow, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 854, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 842, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NewFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewFileActionPerformed
        // TODO add your handling code here:
//        NewFileCreation newFile = new NewFileCreation();
//        newFile.setVisible(true);
//            String userHome = System.getProperty("user.home");         
//            jList1 = new JList<>(getFilesInDirectory(userHome + File.separator + "Desktop\\HAHAHAHHAHAHA"));
        createNewFile();
    }//GEN-LAST:event_NewFileActionPerformed

    private void DeleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteFileActionPerformed
        // TODO add your handling code here:
        deleteSelectedFile();
    }//GEN-LAST:event_DeleteFileActionPerformed

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
            java.util.logging.Logger.getLogger(FileManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DeleteFile;
    private javax.swing.JLabel FileManagerWindow;
    private javax.swing.JButton NewFile;
    private javax.swing.JButton OpenFile;
    private javax.swing.JList<String> fileList;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
