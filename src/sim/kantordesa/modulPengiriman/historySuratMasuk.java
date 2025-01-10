package sim.kantordesa.modulPengiriman;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import sim.kantordesa.config.AppContext;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.dashboard.Dashboard;

public class historySuratMasuk extends javax.swing.JFrame {

    Connection conn;

    public historySuratMasuk() {
        initComponents();

        DefaultTableModel model = new DefaultTableModel();

        tbl_historySuratMasuk.setModel(model);

        model.addColumn("ID Penerimaan Surat");
        model.addColumn("Tanggal Penerimaan Surat");
        model.addColumn("Nomor Surat");
        model.addColumn("Nama Pengirim");
        model.addColumn("Jabatan Pengirim");
        model.addColumn("Asal Instansi");
        model.addColumn("Jenis Surat");
        model.addColumn("Kerahasiaan Surat");
        model.addColumn("Urgensi Surat");
        model.addColumn("Hal");
        model.addColumn("Isi Ringkas Surat");
        model.addColumn("Tanggal Surat Dibuat");

        conn = koneksi.getConnection();
        loadData();

    }

    public Container getContentPanel() {
        return this.getContentPane();
    }

    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) tbl_historySuratMasuk.getModel();
        model.setRowCount(0);

        try {
            String sql = "Select * From mail_received";
            try (java.sql.PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

                while (rs.next()) {
                    String mail_received_id = rs.getString("mail_received_id");
                    String mail_received_date = rs.getString("mail_received_date");
                    String mail_number = rs.getString("mail_number");
                    String send_by = rs.getString("send_by");
                    String sender_position = rs.getString("sender_position");
                    String sender_instance = rs.getString("sender_instance");
                    String mail_type = rs.getString("mail_type");
                    String mail_secrecy = rs.getString("mail_secrecy");
                    String mail_urgency = rs.getString("mail_urgency");
                    String mail_about = rs.getString("mail_about");
                    String mail_content = rs.getString("mail_content");
                    String mail_date = rs.getString("mail_date");

                    Object[] rowData = {mail_received_id, mail_received_date, mail_number, send_by, sender_position, sender_instance, mail_type, mail_secrecy, mail_urgency, mail_about, mail_content, mail_date};
                    model.addRow(rowData);
                }

            }
        } catch (SQLException e) {
            Logger.getLogger(registrasiNaskah.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public void bukaFile(String path_file) throws IOException {
        String sql = "Select * From mail_received WHERE path_file = ?";
        try (java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, path_file);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String pathPdf = rs.getString("path_file");
                File pdfFile = new File(pathPdf);

                // Cek apakah file ada dan bisa dibuka
                if (pdfFile.exists()) {
                    // Jika file ditemukan, buka file PDF menggunakan Desktop
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    JOptionPane.showMessageDialog(null, "File tidak ditemukan!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Surat dengan ID tersebut tidak ditemukan!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error membuka file: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_historySuratMasuk = new javax.swing.JTable();
        btn_tambahSurat = new javax.swing.JButton();
        label1 = new java.awt.Label();
        jPanel7 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane1.setToolTipText("");
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1300, 640));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1300, 640));

        tbl_historySuratMasuk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_historySuratMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_historySuratMasukMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_historySuratMasuk);

        btn_tambahSurat.setText("Tambah Surat Masuk");
        btn_tambahSurat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahSuratActionPerformed(evt);
            }
        });

        label1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        label1.setText("DAFTAR SURAT MASUK");

        jPanel7.setBackground(new java.awt.Color(0, 102, 51));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1276, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_tambahSurat)
                .addGap(49, 49, 49))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_tambahSurat)
                        .addGap(8, 8, 8)))
                .addGap(19, 19, 19)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1297, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahSuratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahSuratActionPerformed
        Dashboard.switchPanel("Form Surat Masuk");
    }//GEN-LAST:event_btn_tambahSuratActionPerformed

    private void tbl_historySuratMasukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_historySuratMasukMouseClicked
        int selectedRow = tbl_historySuratMasuk.getSelectedRow();
        String mail_received_id = tbl_historySuratMasuk.getValueAt(selectedRow, 0).toString();

        detailSurat detail = (detailSurat) Dashboard.getPage("Detail Surat");
        AppContext.put("historymasuk_mailRcvId", mail_received_id);
        detail.updateData();
        Dashboard.card.revalidate();
        Dashboard.card.repaint();
        Dashboard.switchPanel("Detail Surat");
    }//GEN-LAST:event_tbl_historySuratMasukMouseClicked

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
            java.util.logging.Logger.getLogger(registrasiNaskah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(registrasiNaskah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(registrasiNaskah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(registrasiNaskah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new historySuratMasuk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_tambahSurat;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private java.awt.Label label1;
    private javax.swing.JTable tbl_historySuratMasuk;
    // End of variables declaration//GEN-END:variables
}
