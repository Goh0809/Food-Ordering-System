/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Form.DeliveryRunner;

import Class.RunnerTask;
import Form.Customer.CustomerDashboard;
import Form.Login.LoginForm;
import Form.Notification.Notifications;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;

/**
 *
 * @author desmondcwf
 */
public class DeliveryDashboard extends javax.swing.JFrame {

    /**
     * Creates new form AdminDashboard
     */
    private String username;
    private RunnerTask r1;

    public DeliveryDashboard(String username, int userID) {
        initComponents();
        this.username = username;
        lbl_Welcome.setText("Welcome back, " + username);
        btn_ViewTask.setOpaque(true);
        btn_ViewTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runner_ViewTask viewTask = new Runner_ViewTask(username);
                viewTask.setDefaultCloseOperation(DeliveryDashboard.DISPOSE_ON_CLOSE);
                viewTask.setVisible(true);
                viewTask.pack();
                viewTask.setLocationRelativeTo(null);
            }
        });

        btn_UpdateTaskStatus.setOpaque(true);
        btn_UpdateTaskStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runner_UpdateTask updateTaskStatus = new Runner_UpdateTask(username);
                updateTaskStatus.setDefaultCloseOperation(DeliveryDashboard.DISPOSE_ON_CLOSE);
                updateTaskStatus.setVisible(true);
                updateTaskStatus.pack();
                updateTaskStatus.setLocationRelativeTo(null);
            }
        });

        btn_CheckTaskHistory.setOpaque(true);
        btn_CheckTaskHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runner_CheckTaskHistory checkTaskHistory = new Runner_CheckTaskHistory(username);
                checkTaskHistory.setDefaultCloseOperation(DeliveryDashboard.DISPOSE_ON_CLOSE);
                checkTaskHistory.setVisible(true);
                checkTaskHistory.pack();
                checkTaskHistory.setLocationRelativeTo(null);
            }
        });

        btn_ReadCustomerReview.setOpaque(true);
        btn_ReadCustomerReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runner_ReadCustomerReview readCustomerReview = new Runner_ReadCustomerReview(username);
                readCustomerReview.setDefaultCloseOperation(DeliveryDashboard.DISPOSE_ON_CLOSE);
                readCustomerReview.setVisible(true);
                readCustomerReview.pack();
                readCustomerReview.setLocationRelativeTo(null);
            }
        });

        btn_ViewRevenue.setOpaque(true);
        btn_ViewRevenue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runner_ViewRevenue viewRevenue = new Runner_ViewRevenue(username);
                viewRevenue.setDefaultCloseOperation(DeliveryDashboard.DISPOSE_ON_CLOSE);
                viewRevenue.setVisible(true);
                viewRevenue.pack();
                viewRevenue.setLocationRelativeTo(null);
            }
        });
        btn_Notification.setOpaque(true);
        btn_Notification.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Notifications n = new Notifications(userID);
                n.setDefaultCloseOperation(CustomerDashboard.DISPOSE_ON_CLOSE);
                n.setVisible(true);
                n.pack();
                n.setLocationRelativeTo(null);

            }
        });
        btn_logout.setOpaque(true);
        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                LoginForm login = new LoginForm();
                login.setVisible(true);
                login.pack();
                login.setLocationRelativeTo(null);

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

        panel = new javax.swing.JPanel();
        btn_ViewTask = new javax.swing.JButton();
        btn_UpdateTaskStatus = new javax.swing.JButton();
        btn_CheckTaskHistory = new javax.swing.JButton();
        btn_ReadCustomerReview = new javax.swing.JButton();
        btn_ViewRevenue = new javax.swing.JButton();
        panel_Top = new javax.swing.JPanel();
        lbl_icon = new javax.swing.JLabel();
        lbl_Welcome = new javax.swing.JLabel();
        btn_Notification = new javax.swing.JButton();
        btn_logout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(32767, 32767));
        setPreferredSize(new java.awt.Dimension(500, 750));
        setResizable(false);
        setSize(new java.awt.Dimension(500, 750));

        panel.setBackground(new java.awt.Color(204, 204, 255));
        panel.setPreferredSize(new java.awt.Dimension(500, 750));
        panel.setLayout(null);

        btn_ViewTask.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btn_ViewTask.setText("View Task");
        btn_ViewTask.setPreferredSize(new java.awt.Dimension(177, 23));
        btn_ViewTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ViewTaskActionPerformed(evt);
            }
        });
        panel.add(btn_ViewTask);
        btn_ViewTask.setBounds(160, 130, 190, 60);

        btn_UpdateTaskStatus.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btn_UpdateTaskStatus.setText("Update Task Status");
        btn_UpdateTaskStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_UpdateTaskStatusActionPerformed(evt);
            }
        });
        panel.add(btn_UpdateTaskStatus);
        btn_UpdateTaskStatus.setBounds(160, 230, 190, 60);

        btn_CheckTaskHistory.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btn_CheckTaskHistory.setText("Check Task History");
        btn_CheckTaskHistory.setToolTipText("");
        panel.add(btn_CheckTaskHistory);
        btn_CheckTaskHistory.setBounds(160, 330, 190, 60);

        btn_ReadCustomerReview.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btn_ReadCustomerReview.setText("View Review");
        panel.add(btn_ReadCustomerReview);
        btn_ReadCustomerReview.setBounds(160, 420, 190, 60);

        btn_ViewRevenue.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btn_ViewRevenue.setText("View Revenue");
        panel.add(btn_ViewRevenue);
        btn_ViewRevenue.setBounds(160, 520, 190, 60);

        panel_Top.setBackground(new java.awt.Color(255, 204, 255));

        lbl_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Form/profile_picture2.png"))); // NOI18N

        lbl_Welcome.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lbl_Welcome.setText("jLabel2");

        btn_Notification.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Form/notification.png"))); // NOI18N

        javax.swing.GroupLayout panel_TopLayout = new javax.swing.GroupLayout(panel_Top);
        panel_Top.setLayout(panel_TopLayout);
        panel_TopLayout.setHorizontalGroup(
            panel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_TopLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lbl_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Welcome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 259, Short.MAX_VALUE)
                .addComponent(btn_Notification)
                .addGap(94, 94, 94))
        );
        panel_TopLayout.setVerticalGroup(
            panel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_icon, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_TopLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_TopLayout.createSequentialGroup()
                        .addComponent(lbl_Welcome)
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_TopLayout.createSequentialGroup()
                        .addComponent(btn_Notification)
                        .addGap(31, 31, 31))))
        );

        panel.add(panel_Top);
        panel_Top.setBounds(-20, 0, 590, 110);

        btn_logout.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btn_logout.setForeground(new java.awt.Color(255, 0, 0));
        btn_logout.setText("Log Out");
        panel.add(btn_logout);
        btn_logout.setBounds(160, 620, 190, 60);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 65, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ViewTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ViewTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_ViewTaskActionPerformed

    private void btn_UpdateTaskStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_UpdateTaskStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_UpdateTaskStatusActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DeliveryDashboard("",-1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_CheckTaskHistory;
    private javax.swing.JButton btn_Notification;
    private javax.swing.JButton btn_ReadCustomerReview;
    private javax.swing.JButton btn_UpdateTaskStatus;
    private javax.swing.JButton btn_ViewRevenue;
    private javax.swing.JButton btn_ViewTask;
    private javax.swing.JButton btn_logout;
    private javax.swing.JLabel lbl_Welcome;
    private javax.swing.JLabel lbl_icon;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel panel_Top;
    // End of variables declaration//GEN-END:variables
}
