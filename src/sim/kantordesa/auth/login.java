/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sim.kantordesa.auth;

import java.sql.*;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.mailtemplate.templateselector;

/**
 *
 * @author manii
 */
public class login extends javax.swing.JFrame {

    /**
     * Creates new form login
     */
    public login() {
        initComponents();
        showpass.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        body = new javax.swing.JPanel();
        PanelKiri = new javax.swing.JPanel();
        logodesa = new javax.swing.JLabel();
        judul = new javax.swing.JLabel();
        PanelKanan = new javax.swing.JPanel();
        LOGIN = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        text_username = new javax.swing.JTextField();
        password = new javax.swing.JLabel();
        showpass = new javax.swing.JLabel();
        hidepass = new javax.swing.JLabel();
        text_password = new javax.swing.JPasswordField();
        login = new javax.swing.JButton();
        registertext = new javax.swing.JLabel();
        register = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGIN");
        setMaximumSize(new java.awt.Dimension(1920, 1080));

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setMaximumSize(new java.awt.Dimension(1920, 1080));
        body.setPreferredSize(new java.awt.Dimension(800, 500));
        body.setLayout(null);

        PanelKiri.setBackground(new java.awt.Color(19, 128, 97));
        PanelKiri.setMaximumSize(new java.awt.Dimension(1920, 1080));
        PanelKiri.setMinimumSize(new java.awt.Dimension(0, 0));
        PanelKiri.setPreferredSize(new java.awt.Dimension(400, 500));

        logodesa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logodesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/auth/icon/logorandom.png"))); // NOI18N

        judul.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        judul.setForeground(new java.awt.Color(255, 255, 255));
        judul.setText("SIM-Desa");

        javax.swing.GroupLayout PanelKiriLayout = new javax.swing.GroupLayout(PanelKiri);
        PanelKiri.setLayout(PanelKiriLayout);
        PanelKiriLayout.setHorizontalGroup(
            PanelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelKiriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logodesa, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(PanelKiriLayout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(judul)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelKiriLayout.setVerticalGroup(
            PanelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelKiriLayout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(logodesa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(judul)
                .addGap(133, 133, 133))
        );

        body.add(PanelKiri);
        PanelKiri.setBounds(0, 0, 400, 500);

        PanelKanan.setBackground(new java.awt.Color(255, 255, 255));
        PanelKanan.setMaximumSize(new java.awt.Dimension(1920, 1080));
        PanelKanan.setMinimumSize(new java.awt.Dimension(0, 0));
        PanelKanan.setPreferredSize(new java.awt.Dimension(400, 500));
        PanelKanan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LOGIN.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        LOGIN.setForeground(new java.awt.Color(19, 128, 97));
        LOGIN.setText("LOGIN");
        PanelKanan.add(LOGIN, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 49, -1, -1));

        username.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        username.setText("Username");
        PanelKanan.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 149, -1, -1));

        text_username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PanelKanan.add(text_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 172, 300, 35));

        password.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        password.setText("Password");
        PanelKanan.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 225, -1, -1));

        showpass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/auth/icon/eye_opened.png"))); // NOI18N
        showpass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showpassMouseClicked(evt);
            }
        });
        PanelKanan.add(showpass, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 260, -1, -1));

        hidepass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/auth/icon/eye_closed.png"))); // NOI18N
        hidepass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hidepassMouseClicked(evt);
            }
        });
        PanelKanan.add(hidepass, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 260, -1, -1));

        text_password.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PanelKanan.add(text_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 251, 260, 35));

        login.setBackground(new java.awt.Color(19, 128, 97));
        login.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        login.setForeground(new java.awt.Color(255, 255, 255));
        login.setText("Login");
        login.setPreferredSize(new java.awt.Dimension(100, 35));
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });
        PanelKanan.add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 313, -1, -1));

        registertext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        registertext.setText("Saya belum memiliki akun");
        PanelKanan.add(registertext, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 379, -1, -1));

        register.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        register.setForeground(new java.awt.Color(19, 128, 97));
        register.setText("Register");
        register.setPreferredSize(new java.awt.Dimension(100, 35));
        register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerActionPerformed(evt);
            }
        });
        PanelKanan.add(register, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 370, -1, -1));

        body.add(PanelKanan);
        PanelKanan.setBounds(400, 0, 400, 500);

        getContentPane().add(body, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerActionPerformed
        register RegisterFrame = new register();
        RegisterFrame.setVisible(true);
        RegisterFrame.pack();
        RegisterFrame.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_registerActionPerformed

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginActionPerformed
        String usernameIn = text_username.getText().trim();
        String passwordIn = new String(text_password.getPassword());
        
        if (usernameIn.isEmpty() || passwordIn.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Username atau Password Kosong!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection conn = koneksi.getConnection();
            String query = "SELECT password FROM users WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, usernameIn);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(passwordIn)) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Login Berhasil!", "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    templateselector DashboardFrame = new templateselector();
                    DashboardFrame.setVisible(true);
                    DashboardFrame.pack();
                    DashboardFrame.setLocationRelativeTo(null);
                    this.dispose();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Password Salah!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Username tidak ditemukan!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Sistem Error!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_loginActionPerformed

    private void hidepassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hidepassMouseClicked
        hidepass.setVisible(false);
        showpass.setVisible(true);
        text_password.setEchoChar ((char)0);
        
    }//GEN-LAST:event_hidepassMouseClicked

    private void showpassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showpassMouseClicked
        hidepass.setVisible(true);
        showpass.setVisible(false);
        text_password.setEchoChar ('*');
    }//GEN-LAST:event_showpassMouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LOGIN;
    private javax.swing.JPanel PanelKanan;
    private javax.swing.JPanel PanelKiri;
    private javax.swing.JPanel body;
    private javax.swing.JLabel hidepass;
    private javax.swing.JLabel judul;
    private javax.swing.JButton login;
    private javax.swing.JLabel logodesa;
    private javax.swing.JLabel password;
    private javax.swing.JButton register;
    private javax.swing.JLabel registertext;
    private javax.swing.JLabel showpass;
    private javax.swing.JPasswordField text_password;
    private javax.swing.JTextField text_username;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
