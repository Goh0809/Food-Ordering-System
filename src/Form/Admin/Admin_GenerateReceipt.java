/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Form.Admin;

import Class.User;
import Class.UserCredit;
import Class.UserManager;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author desmondcwf
 */
public class Admin_GenerateReceipt extends javax.swing.JFrame {

    private UserManager userManager;
    private UserCredit userCredit;

    public Admin_GenerateReceipt() {
        initComponents();
        userManager = new UserManager();
        userCredit = new UserCredit(userManager);
        //Change header font for table
        JTableHeader tableHeader1 = table_GenerateReceipt.getTableHeader();
        Font headerFont1 = new Font("Georgia", Font.BOLD, 14);
        tableHeader1.setFont(headerFont1);

        //Generate Transaction Receipt Button
        btn_Generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table_GenerateReceipt.getModel();
                model.setRowCount(0);
                int userId;
                try {
                    userId = Integer.parseInt(txtbox_Userid.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid user ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = userManager.getUserById(userId);

                if (user != null) {
                    userCredit.displayTransactions(userId, model);
                } else {
                    JOptionPane.showMessageDialog(null, "User ID not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_GenerateReceipt = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtbox_Userid = new javax.swing.JTextField();
        btn_Generate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFocusable(false);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(850, 600));
        jPanel1.setSize(new java.awt.Dimension(850, 600));

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 204));
        jLabel1.setText("Generate Transaction Receipt");

        table_GenerateReceipt.setFont(new java.awt.Font("Georgia", 0, 16)); // NOI18N
        table_GenerateReceipt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "TransactionID", "Transaction Amount", "Transaction Time", "Transaction Type", "Remark"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_GenerateReceipt.setRowHeight(30);
        table_GenerateReceipt.setRowSelectionAllowed(false);
        table_GenerateReceipt.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table_GenerateReceipt);
        if (table_GenerateReceipt.getColumnModel().getColumnCount() > 0) {
            table_GenerateReceipt.getColumnModel().getColumn(0).setMinWidth(100);
            table_GenerateReceipt.getColumnModel().getColumn(0).setPreferredWidth(100);
            table_GenerateReceipt.getColumnModel().getColumn(0).setMaxWidth(100);
            table_GenerateReceipt.getColumnModel().getColumn(1).setMinWidth(150);
            table_GenerateReceipt.getColumnModel().getColumn(1).setPreferredWidth(150);
            table_GenerateReceipt.getColumnModel().getColumn(1).setMaxWidth(150);
            table_GenerateReceipt.getColumnModel().getColumn(2).setMinWidth(200);
            table_GenerateReceipt.getColumnModel().getColumn(2).setPreferredWidth(200);
            table_GenerateReceipt.getColumnModel().getColumn(2).setMaxWidth(200);
            table_GenerateReceipt.getColumnModel().getColumn(3).setMinWidth(150);
            table_GenerateReceipt.getColumnModel().getColumn(3).setPreferredWidth(150);
            table_GenerateReceipt.getColumnModel().getColumn(3).setMaxWidth(150);
            table_GenerateReceipt.getColumnModel().getColumn(4).setMinWidth(200);
            table_GenerateReceipt.getColumnModel().getColumn(4).setPreferredWidth(200);
        }

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel2.setText("User ID:");

        txtbox_Userid.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N

        btn_Generate.setBackground(new java.awt.Color(204, 255, 204));
        btn_Generate.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        btn_Generate.setText("Generate");
        btn_Generate.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(98, 98, 98)
                        .addComponent(txtbox_Userid, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addComponent(btn_Generate, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addContainerGap(154, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtbox_Userid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Generate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
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
            java.util.logging.Logger.getLogger(Admin_GenerateReceipt.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin_GenerateReceipt.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin_GenerateReceipt.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin_GenerateReceipt.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin_GenerateReceipt().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Generate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table_GenerateReceipt;
    private javax.swing.JTextField txtbox_Userid;
    // End of variables declaration//GEN-END:variables
}
