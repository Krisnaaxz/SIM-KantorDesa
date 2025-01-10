/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sim.kantordesa.mailtemplate;

import java.sql.*;
import javax.swing.JPanel;
import sim.kantordesa.config.AppContext;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.dashboard.Dashboard;

/**
 *
 * @author manii
 */
public class templateselector extends javax.swing.JFrame {

    public templateselector() {
        initComponents();
        loadTemplateSurat(); // Panggil metode untuk memuat data ke dropdown
    }

    public JPanel getContentPanel() {
        return (JPanel) this.getContentPane();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        body = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        nama_pengaju = new javax.swing.JLabel();
        text_namapengaju = new javax.swing.JTextField();
        template_surat = new javax.swing.JLabel();
        box_template_surat = new javax.swing.JComboBox<>();
        btn_next = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        header.setBackground(new java.awt.Color(19, 128, 97));

        title.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        title.setForeground(new java.awt.Color(255, 255, 255));
        title.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        title.setText("TEMPLATE SURAT");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(title)
                .addContainerGap(434, Short.MAX_VALUE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(title)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        nama_pengaju.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nama_pengaju.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nama_pengaju.setText("Nama Pengaju");

        text_namapengaju.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        text_namapengaju.setPreferredSize(new java.awt.Dimension(640, 35));

        template_surat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        template_surat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        template_surat.setText("Template Surat");

        box_template_surat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        box_template_surat.setMaximumRowCount(7);
        box_template_surat.setToolTipText("");
        box_template_surat.setPreferredSize(new java.awt.Dimension(640, 35));

        btn_next.setBackground(new java.awt.Color(19, 128, 97));
        btn_next.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_next.setForeground(new java.awt.Color(255, 255, 255));
        btn_next.setText("Lanjutkan");
        btn_next.setPreferredSize(new java.awt.Dimension(120, 35));
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(bodyLayout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_next, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(box_template_surat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(template_surat)
                        .addComponent(nama_pengaju)
                        .addComponent(text_namapengaju, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(nama_pengaju)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(text_namapengaju, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(template_surat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(box_template_surat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btn_next, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_nextActionPerformed
        String applicantName = text_namapengaju.getText().trim();
        String templateName = (String) box_template_surat.getSelectedItem();

        // Validasi input
        if (applicantName.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Nama pengaju tidak boleh kosong!", "Peringatan",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (templateName == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih template surat!", "Peringatan",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil ID template surat
        int mailTypeId = getMailTypeId(templateName);
        if (mailTypeId == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Template surat tidak valid!", "Peringatan",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simpan data pengaju
        Connection conn = koneksi.getConnection();
        try {
            saveApplicantData(conn, applicantName, mailTypeId);

        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke database!", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        // Buka form berikutnya
        AppContext.put("mailform_templateName", templateName);
        AppContext.put("mailform_mailTypeId", mailTypeId);
        mailform mailPage = (mailform) Dashboard.getPage("Form Surat Keluar");
        mailPage.updateData();
        Dashboard.card.revalidate();
        Dashboard.card.repaint();
        Dashboard.switchPanel("Form Surat Keluar");
    }// GEN-LAST:event_btn_nextActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(templateselector.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        }
        // </editor-fold>

        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new templateselector().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JComboBox<String> box_template_surat;
    private javax.swing.JButton btn_next;
    private javax.swing.JPanel header;
    private javax.swing.JLabel nama_pengaju;
    private javax.swing.JLabel template_surat;
    private javax.swing.JTextField text_namapengaju;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables

    private void saveApplicantData(Connection conn, String applicantName, int mailTypeId) throws SQLException {
        String query = "INSERT INTO mail_content (applicant_name, mail_type_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, applicantName);
            ps.setInt(2, mailTypeId);
            ps.executeUpdate();
        }
    }

    private int getMailTypeId(String templateName) {
        try {
            Connection conn = koneksi.getConnection();
            String query = "SELECT mail_type_id FROM mail_type WHERE type_name = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, templateName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("mail_type_id");
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(this, "Template Surat tidak ditemukan!", "Peringatan",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Kesalahan sistem saat mengambil ID Template!", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    private void loadTemplateSurat() {
        try {
            Connection conn = koneksi.getConnection();
            String query = "SELECT type_name FROM mail_type";
            try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                box_template_surat.removeAllItems();
                while (rs.next()) {
                    box_template_surat.addItem(rs.getString("type_name"));
                }
            }
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal memuat template surat!", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
