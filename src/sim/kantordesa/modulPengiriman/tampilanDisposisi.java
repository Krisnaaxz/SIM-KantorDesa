package sim.kantordesa.modulPengiriman;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sim.kantordesa.config.AppContext;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.dashboard.Dashboard;

public class tampilanDisposisi extends javax.swing.JFrame {

    Connection conn;
    public String mail_received_id;
    public tampilanDisposisi(String mail_received_id) {
        this.mail_received_id = mail_received_id;
        initComponents();
        conn = koneksi.getConnection();
        Date();
        role();   
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
    
private Map<String, Integer> roleMap = new HashMap<>();
public void role() {
    String sql = "SELECT * FROM role WHERE id_role > 1";

    try (PreparedStatement st = conn.prepareStatement(sql);
         ResultSet rs = st.executeQuery()) {

        while (rs.next()) {
            String roleName = rs.getString("role_name");
            int roleId = rs.getInt("id_role");
            roleMap.put(roleName, roleId);
            t_tujuanDisposisi.addItem(roleName);
        }
           } catch (SQLException ex) {
        Logger.getLogger(tampilanDisposisi.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public final void updateStatus(String mail_received_id) {
    System.out.println("Masuk ke updateStatus");
    System.out.println("Mail Received ID: " + mail_received_id);

    String updatesql = "UPDATE mail_received SET status_id = 2 WHERE mail_received_id = ?";
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
        jPanel5 = new javax.swing.JPanel();
        j_petunjukPimpinan = new javax.swing.JLabel();
        j_tujuanDisposisi = new javax.swing.JLabel();
        t_tujuanDisposisi = new javax.swing.JComboBox<>();
        j_pesanDisposisi = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        j_tindakLanjut = new javax.swing.JCheckBox();
        j_untukDiketahui = new javax.swing.JCheckBox();
        j_laporkan = new javax.swing.JCheckBox();
        j_koordinasikan = new javax.swing.JCheckBox();
        j_jawabTertulis = new javax.swing.JCheckBox();
        j_hadiri = new javax.swing.JCheckBox();
        j_telaah = new javax.swing.JCheckBox();
        j_untukDiselesaikan = new javax.swing.JCheckBox();
        j_dampingi = new javax.swing.JCheckBox();
        j_jadwalkan = new javax.swing.JCheckBox();
        j_siapkanBahan = new javax.swing.JCheckBox();
        j_arsipkan = new javax.swing.JCheckBox();
        j_wakili = new javax.swing.JCheckBox();
        j_buatSambutan = new javax.swing.JCheckBox();
        j_bahanKoordinasi = new javax.swing.JCheckBox();
        j_ACC = new javax.swing.JCheckBox();
        j_batasWaktu = new javax.swing.JLabel();
        j_instruksi = new javax.swing.JLabel();
        t_instruksi = new javax.swing.JTextField();
        j_tanggalDisposisi = new javax.swing.JLabel();
        t_tanggalDisposisi = new javax.swing.JTextField();
        btn_disposisi = new javax.swing.JButton();
        b_kembali = new javax.swing.JButton();
        t_deadline = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1300, 640));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1300, 640));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setPreferredSize(new java.awt.Dimension(1300, 640));

        jPanel5.setBackground(new java.awt.Color(0, 102, 51));

        j_petunjukPimpinan.setBackground(new java.awt.Color(255, 255, 255));
        j_petunjukPimpinan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        j_petunjukPimpinan.setForeground(new java.awt.Color(255, 255, 255));
        j_petunjukPimpinan.setText("FORMULIR DISPOSISI");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(j_petunjukPimpinan)
                .addGap(569, 569, 569))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(j_petunjukPimpinan)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        j_tujuanDisposisi.setText("Tujuan Disposisi");

        t_tujuanDisposisi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Tujuan -" }));
        t_tujuanDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_tujuanDisposisiActionPerformed(evt);
            }
        });

        j_pesanDisposisi.setText("Pesan Disposisi");

        j_tindakLanjut.setText("Tindak Lanjuti");
        j_tindakLanjut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j_tindakLanjutActionPerformed(evt);
            }
        });

        j_untukDiketahui.setText("Untuk Diketahui");

        j_laporkan.setText("Laporkan");

        j_koordinasikan.setText("Koordinasikan");

        j_jawabTertulis.setText("Jawab Tertulis");

        j_hadiri.setText("Hadiri");
        j_hadiri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j_hadiriActionPerformed(evt);
            }
        });

        j_telaah.setText("Telaah");

        j_untukDiselesaikan.setText("Untuk Diselesaikan");

        j_dampingi.setText("Dampingi");

        j_jadwalkan.setText("Jadwalkan");

        j_siapkanBahan.setText("Siapkan Bahan");

        j_arsipkan.setText("Arsipkan");

        j_wakili.setText("Wakili");

        j_buatSambutan.setText("Buat Sambutan");

        j_bahanKoordinasi.setText("Bahan Koordinasi ");

        j_ACC.setText("ACC");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(j_tindakLanjut)
                    .addComponent(j_untukDiketahui)
                    .addComponent(j_laporkan)
                    .addComponent(j_koordinasikan)
                    .addComponent(j_jawabTertulis)
                    .addComponent(j_bahanKoordinasi))
                .addGap(51, 51, 51)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(j_hadiri)
                            .addComponent(j_telaah)
                            .addComponent(j_untukDiselesaikan)
                            .addComponent(j_dampingi)
                            .addComponent(j_jadwalkan))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(j_buatSambutan)
                            .addComponent(j_wakili)
                            .addComponent(j_arsipkan)
                            .addComponent(j_siapkanBahan)))
                    .addComponent(j_ACC))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_tindakLanjut)
                    .addComponent(j_hadiri)
                    .addComponent(j_siapkanBahan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_untukDiketahui)
                    .addComponent(j_telaah)
                    .addComponent(j_arsipkan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_laporkan)
                    .addComponent(j_untukDiselesaikan)
                    .addComponent(j_wakili))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_koordinasikan)
                    .addComponent(j_dampingi)
                    .addComponent(j_buatSambutan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_jawabTertulis)
                    .addComponent(j_jadwalkan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_bahanKoordinasi)
                    .addComponent(j_ACC))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        j_batasWaktu.setText("Batas Waktu");

        j_instruksi.setText("Instruksi/Pesan Tambahan");

        t_instruksi.setText(" ");

        j_tanggalDisposisi.setText("Tanggal Disposisi");

        t_tanggalDisposisi.setText(" ");
        t_tanggalDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_tanggalDisposisiActionPerformed(evt);
            }
        });

        btn_disposisi.setText("Disposisi");
        btn_disposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_disposisiActionPerformed(evt);
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
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_instruksi, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(j_instruksi)
                            .addComponent(t_deadline, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(j_pesanDisposisi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(j_tujuanDisposisi, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(j_batasWaktu))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(t_tujuanDisposisi, 0, 538, Short.MAX_VALUE)
                                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                        .addComponent(j_tanggalDisposisi)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(b_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_disposisi))
                    .addComponent(t_tanggalDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(158, 158, 158))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_tujuanDisposisi)
                    .addComponent(t_tujuanDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j_tanggalDisposisi)
                    .addComponent(t_tanggalDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j_pesanDisposisi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(j_batasWaktu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_deadline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_disposisi)
                            .addComponent(b_kembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(44, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(j_instruksi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_instruksi, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jScrollPane1.setViewportView(jPanel2);

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

    private void t_tujuanDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_tujuanDisposisiActionPerformed
    t_tujuanDisposisi.addActionListener((ActionEvent e) -> {
        Object selectedItem = t_tujuanDisposisi.getSelectedItem();
        if (selectedItem != null) {
            String selectedRole = selectedItem.toString(); // Nilai yang dipilih dalam ComboBox

            // Mendapatkan id_role berdasarkan role_name yang dipilih
            Integer id = roleMap.get(selectedRole);
            
            if (id != null) {
                // Jika id_role ditemukan, kita bisa menggunakannya untuk proses lebih lanjut
                int disposition_destination = id;
                
                // Contoh: Menyisipkan nilai disposition_destination ke dalam database
                String sql = "INSERT INTO mail_disposition (disposition_destination) VALUES (?)";
                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    st.setInt(1, disposition_destination);
                    st.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(tampilanDisposisi.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.err.println("Peran tidak ditemukan dalam peta.");
            }
        }
    });
    
    }//GEN-LAST:event_t_tujuanDisposisiActionPerformed

    private void j_tindakLanjutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j_tindakLanjutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_j_tindakLanjutActionPerformed

    private void t_tanggalDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_tanggalDisposisiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_tanggalDisposisiActionPerformed

    private void btn_disposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_disposisiActionPerformed
        int status_id =2;
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       String disposition_notes = t_instruksi.getText();
       String disposition_deadline = sdf.format(t_deadline.getDate()); 
       String disposition_date = t_tanggalDisposisi.getText();
       // Mengambil nilai yang dipilih dari ComboBox
        String disposition_destination = t_tujuanDisposisi.getSelectedItem().toString();

        // Mencari id_role yang sesuai dengan disposition_destination menggunakan roleMap
        Integer roleId = roleMap.get(disposition_destination);

        if (roleId == null) {
        // Jika roleId tidak ditemukan dalam roleMap, tampilkan pesan error
        JOptionPane.showMessageDialog(this, "Peran yang dipilih tidak valid.");
         return;  // Tidak melanjutkan proses jika roleId tidak ditemukan
        }
        
       StringBuilder selectedOptions = new StringBuilder();
       
       if (j_tindakLanjut.isSelected()){
           selectedOptions.append("Tindak Lanjuti,");
       }
       if (j_untukDiketahui.isSelected()){
           selectedOptions.append("Untuk DIketahui,");
       }
       if (j_laporkan.isSelected()){
           selectedOptions.append("Laporkan,");
       }
       if (j_koordinasikan.isSelected()){
           selectedOptions.append("Koordinasikan,");
       }
       if (j_jawabTertulis.isSelected()){
           selectedOptions.append("Jawab Tertulis,");
       }
       if (j_telaah.isSelected()){
           selectedOptions.append("Telaah,");
       }
       if (j_untukDiselesaikan.isSelected()){
           selectedOptions.append("Untuk Diselesaikan,");
       }
       if (j_dampingi.isSelected()){
           selectedOptions.append("Dampingi,");
       }
       if (j_siapkanBahan.isSelected()){
           selectedOptions.append("Siapkan Bahan,");
       }
       if (j_arsipkan.isSelected()){
           selectedOptions.append("Arsipkan,");
       }
       if (j_wakili.isSelected()){
           selectedOptions.append("Wakili,");
       }
       if (j_buatSambutan.isSelected()){
           selectedOptions.append("Buat Sambutan/Materi,");
       }
       if (j_hadiri.isSelected()){
           selectedOptions.append("Hadiri,");
       }
       if (j_bahanKoordinasi.isSelected()){
           selectedOptions.append("Bahan Koordinasi,");
       }
       if (j_ACC.isSelected()){
           selectedOptions.append("ACC,");
       }
       if (selectedOptions.length()>0){
           selectedOptions.setLength(selectedOptions.length()-1);
       }
       String disposition_instruction = selectedOptions.toString();
      
       role();
      
       
       String insertsql = "INSERT INTO mail_disposition (disposition_notes, disposition_deadline, disposition_date, disposition_instruction, disposition_destination, mail_received_id, status_id) VALUES (?,?,?,?,?,?,?)";
       try (PreparedStatement st = conn.prepareStatement(insertsql)) {
           st.setString(1, disposition_notes);
           st.setString(2, disposition_deadline);
           st.setString(3, disposition_date);
           st.setString(4, disposition_instruction);
           st.setInt(5, roleId);
           st.setString(6, mail_received_id);
           st.setInt(7, status_id);
            
           int rowInserted = st.executeUpdate();
           if(rowInserted>0){
               JOptionPane.showMessageDialog(this, "Surat Berhasil Didisposisi");}
       }catch (HeadlessException | SQLException e){
           Logger.getLogger(tampilanDisposisi.class.getName()).log(Level.SEVERE,null,e);
       }
       
       updateStatus(mail_received_id);
       
        detailSurat detail = new detailSurat(mail_received_id);
        AppContext.put("historymasuk_mailRcvId", mail_received_id);
        detail.updateData();
        Dashboard.card.revalidate();
        Dashboard.card.repaint();
        Dashboard.switchPanel("Detail Surat");
         
    }//GEN-LAST:event_btn_disposisiActionPerformed

    private void b_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_kembaliActionPerformed
        detailSurat detail = new detailSurat(mail_received_id);
        AppContext.put("historymasuk_mailRcvId", mail_received_id);
        detail.updateData();
        Dashboard.card.revalidate();
        Dashboard.card.repaint();
        Dashboard.switchPanel("Detail Surat");
    }//GEN-LAST:event_b_kembaliActionPerformed

    private void j_hadiriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j_hadiriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_j_hadiriActionPerformed

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
            java.util.logging.Logger.getLogger(tampilanDisposisi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tampilanDisposisi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tampilanDisposisi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tampilanDisposisi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {               
            }          
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_kembali;
    private javax.swing.JButton btn_disposisi;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox j_ACC;
    private javax.swing.JCheckBox j_arsipkan;
    private javax.swing.JCheckBox j_bahanKoordinasi;
    private javax.swing.JLabel j_batasWaktu;
    private javax.swing.JCheckBox j_buatSambutan;
    private javax.swing.JCheckBox j_dampingi;
    private javax.swing.JCheckBox j_hadiri;
    private javax.swing.JLabel j_instruksi;
    private javax.swing.JCheckBox j_jadwalkan;
    private javax.swing.JCheckBox j_jawabTertulis;
    private javax.swing.JCheckBox j_koordinasikan;
    private javax.swing.JCheckBox j_laporkan;
    private javax.swing.JLabel j_pesanDisposisi;
    private javax.swing.JLabel j_petunjukPimpinan;
    private javax.swing.JCheckBox j_siapkanBahan;
    private javax.swing.JLabel j_tanggalDisposisi;
    private javax.swing.JCheckBox j_telaah;
    private javax.swing.JCheckBox j_tindakLanjut;
    private javax.swing.JLabel j_tujuanDisposisi;
    private javax.swing.JCheckBox j_untukDiketahui;
    private javax.swing.JCheckBox j_untukDiselesaikan;
    private javax.swing.JCheckBox j_wakili;
    private com.toedter.calendar.JDateChooser t_deadline;
    private javax.swing.JTextField t_instruksi;
    private javax.swing.JTextField t_tanggalDisposisi;
    private javax.swing.JComboBox<String> t_tujuanDisposisi;
    // End of variables declaration//GEN-END:variables
}
