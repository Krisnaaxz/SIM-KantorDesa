package sim.kantordesa.modulPengiriman;


import java.awt.Container;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sim.kantordesa.config.AppContext;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.dashboard.Dashboard;

public class replyStaff extends javax.swing.JFrame {

    Connection conn;
    private String mail_received_id;
    public replyStaff(String mail_received_id) {
        this.mail_received_id = mail_received_id;
        initComponents();
        conn = koneksi.getConnection();
        Date();
    }
    
    public Container getContentPanel() {
        return this.getContentPane();
    }
    
    public void Date(){
        DateTimeFormatter dates = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        t_tanggalDisposisi.setText(dates.format(now));
        t_tanggalDisposisi.setEnabled(false);
    }
    
public final void updateStatusTabel(String mail_received_id) {
    System.out.println("Masuk ke updateStatus");
    System.out.println("Mail Received ID: " + mail_received_id);

    String updatesql = "UPDATE mail_received SET status_id = 3 WHERE mail_received_id = ?";
    try (PreparedStatement st = conn.prepareStatement(updatesql)) {
        System.out.println("PreparedStatement berhasil dibuat");
        st.setString(1, mail_received_id.trim());

        int rowUpdated = st.executeUpdate();
        System.out.println("Rows updated: " + rowUpdated);

        if (rowUpdated == 0) {
            System.out.println("Tidak ada baris yang diperbarui. Periksa data input.");
        }
    } catch (SQLException e) {
        System.err.println("Kesalahan saat memperbarui status:");
    }
}

public final void updateStatus(String mail_received_id) {
    System.out.println("Masuk ke updateStatus");
    System.out.println("Mail Received ID: " + mail_received_id);

    String updatesql = "UPDATE mail_disposition SET status_id = 3 WHERE mail_received_id = ?";
    try (PreparedStatement st = conn.prepareStatement(updatesql)) {
        System.out.println("PreparedStatement berhasil dibuat");
        st.setString(1, mail_received_id.trim());

        int rowUpdated = st.executeUpdate();
        System.out.println("Rows updated: " + rowUpdated);

        if (rowUpdated == 0) {
            System.out.println("Tidak ada baris yang diperbarui. Periksa data input.");
        }
    } catch (SQLException e) {
        System.err.println("Kesalahan saat memperbarui status:");
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        j_formDisposisi = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        j_petunjukPimpinan = new javax.swing.JLabel();
        j_instruksi = new javax.swing.JLabel();
        t_instruksi = new javax.swing.JTextField();
        j_tanggalDisposisi = new javax.swing.JLabel();
        t_tanggalDisposisi = new javax.swing.JTextField();
        btn_kirim = new javax.swing.JButton();
        b_kembali = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(32767, 32767));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1300, 640));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(1300, 640));
        jPanel2.setPreferredSize(new java.awt.Dimension(1300, 640));

        j_formDisposisi.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        j_formDisposisi.setText("Balas Disposisi");

        jPanel5.setBackground(new java.awt.Color(0, 102, 51));

        j_petunjukPimpinan.setBackground(new java.awt.Color(255, 255, 255));
        j_petunjukPimpinan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        j_petunjukPimpinan.setForeground(new java.awt.Color(255, 255, 255));
        j_petunjukPimpinan.setText(" ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(420, 420, 420)
                .addComponent(j_petunjukPimpinan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(j_petunjukPimpinan)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        j_instruksi.setText("Pesan Penyelesaian");

        t_instruksi.setText(" ");

        j_tanggalDisposisi.setText("Tanggal Penyelesaian");

        t_tanggalDisposisi.setText(" ");
        t_tanggalDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_tanggalDisposisiActionPerformed(evt);
            }
        });

        btn_kirim.setText("Kirim");
        btn_kirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kirimActionPerformed(evt);
            }
        });

        b_kembali.setText("Kembali");
        b_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_kembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(j_tanggalDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t_tanggalDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(t_instruksi, javax.swing.GroupLayout.PREFERRED_SIZE, 642, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j_instruksi, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(j_formDisposisi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_kembali)
                .addGap(43, 43, 43))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_kirim)
                .addGap(47, 47, 47))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(j_formDisposisi)
                    .addComponent(b_kembali))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_tanggalDisposisi)
                    .addComponent(t_tanggalDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(j_instruksi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(t_instruksi, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_kirim)
                .addContainerGap(366, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_kembaliActionPerformed
        detailSurat detail = new detailSurat(mail_received_id);
        AppContext.put("historymasuk_mailRcvId", mail_received_id);
        detail.updateData();
        Dashboard.card.revalidate();
        Dashboard.card.repaint();
        Dashboard.switchPanel("Detail Surat");
    }//GEN-LAST:event_b_kembaliActionPerformed

    private void btn_kirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kirimActionPerformed
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String disposition_date = t_tanggalDisposisi.getText();
        String disposition_notes_staff = t_instruksi.getText();
        int status_id = 3;


        String sql = "UPDATE mail_disposition SET disposition_notes_staff =?, disposition_date =? WHERE mail_received_id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, disposition_notes_staff);
            st.setString(2, disposition_date);
            st.setString(3, mail_received_id);

            int rowInserted = st.executeUpdate();
            if(rowInserted>0){
                JOptionPane.showMessageDialog(this, "Pesan Berhasil Dikirimkan");}
        }catch (HeadlessException | SQLException e){
            Logger.getLogger(replyStaff.class.getName()).log(Level.SEVERE,null,e);
        }
        updateStatusTabel(mail_received_id);
        updateStatus(mail_received_id);
        
        detailSurat detail = new detailSurat(mail_received_id);
        AppContext.put("historymasuk_mailRcvId", mail_received_id);
        detail.updateData();
        Dashboard.card.revalidate();
        Dashboard.card.repaint();
        Dashboard.switchPanel("Detail Surat");
    }//GEN-LAST:event_btn_kirimActionPerformed

    private void t_tanggalDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_tanggalDisposisiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_tanggalDisposisiActionPerformed

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
            java.util.logging.Logger.getLogger(replyStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(replyStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(replyStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(replyStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new historySuratMasuk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_kembali;
    private javax.swing.JButton btn_kirim;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel j_formDisposisi;
    private javax.swing.JLabel j_instruksi;
    private javax.swing.JLabel j_petunjukPimpinan;
    private javax.swing.JLabel j_tanggalDisposisi;
    private javax.swing.JTextField t_instruksi;
    private javax.swing.JTextField t_tanggalDisposisi;
    // End of variables declaration//GEN-END:variables
}
