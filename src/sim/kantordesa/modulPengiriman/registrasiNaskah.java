package sim.kantordesa.modulPengiriman;

import java.awt.Container;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.dashboard.Dashboard;

public class registrasiNaskah extends javax.swing.JFrame {

    Connection conn;
    private File selectedFile;

    public registrasiNaskah() {
        conn = koneksi.getConnection();
        initComponents();
        Date();
    }

    public void Date() {
        DateTimeFormatter dates = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        t_tglSuratDiterima.setText(dates.format(now));
        t_tglSuratDiterima.setEditable(false);
    }

    public Container getContentPanel() {
        return this.getContentPane();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        j_registrasiNaskah = new javax.swing.JLabel();
        b_kembali = new javax.swing.JButton();
        j_identitasSurat = new javax.swing.JLabel();
        j_namaPengirim = new javax.swing.JLabel();
        t_namaPengirim = new javax.swing.JTextField();
        j_jabatanPengirim = new javax.swing.JLabel();
        t_jabatanPengirim = new javax.swing.JTextField();
        j_instansiPengirim = new javax.swing.JLabel();
        t_asalInstansi = new javax.swing.JTextField();
        j_detailIsiSurat = new javax.swing.JLabel();
        j_tipeSurat = new javax.swing.JLabel();
        t_jenisSurat = new javax.swing.JComboBox<>();
        j_nomorSurat = new javax.swing.JLabel();
        t_nomorSurat = new javax.swing.JTextField();
        j_tanggalSurat = new javax.swing.JLabel();
        j_sifatSurat = new javax.swing.JLabel();
        t_sifatSurat = new javax.swing.JComboBox<>();
        j_urgensiSurat = new javax.swing.JLabel();
        t_urgensi = new javax.swing.JComboBox<>();
        j_tanggalSuratDiterima = new javax.swing.JLabel();
        t_tglSuratDiterima = new javax.swing.JTextField();
        j_halSurat = new javax.swing.JLabel();
        t_hal = new javax.swing.JTextField();
        j_isiRingkasSurat = new javax.swing.JLabel();
        t_isiRingkasSurat = new javax.swing.JTextField();
        b_kirim = new javax.swing.JButton();
        b_tambahFile = new javax.swing.JButton();
        t_pathFile = new javax.swing.JTextField();
        j_unggahFileSurat = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        t_tglSurat = new com.toedter.calendar.JDateChooser();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(32767, 32767));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1300, 640));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 640));

        j_registrasiNaskah.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        j_registrasiNaskah.setText("Registrasi Naskah");

        b_kembali.setText("Kembali");
        b_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_kembaliActionPerformed(evt);
            }
        });

        j_identitasSurat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        j_identitasSurat.setText("IDENTITAS SURAT");

        j_namaPengirim.setText("Nama Pengirim");

        t_namaPengirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_namaPengirimActionPerformed(evt);
            }
        });

        j_jabatanPengirim.setText("Jabatan Pengirim");

        j_instansiPengirim.setText("Instansi Pengirim");

        j_detailIsiSurat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        j_detailIsiSurat.setText("Detail Isi Surat");

        j_tipeSurat.setText("Tipe Surat");

        t_jenisSurat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih - ", "Surat Undangan", "Surat Pemberitahuan", "Surat Edaran", "Surat Keputusan", "Surat Panggilan", "Surat Permohonan", "Surat Pengantar" }));
        t_jenisSurat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_jenisSuratActionPerformed(evt);
            }
        });

        j_nomorSurat.setText("Nomor Surat");

        j_tanggalSurat.setText("Tanggal Surat");

        j_sifatSurat.setText("Sifat Surat");

        t_sifatSurat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Pilih-", "Rahasia", "Biasa" }));

        j_urgensiSurat.setText("Urgensi Surat");

        t_urgensi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Pilih-", "Segera", "Sangat Segera", "Normal", " " }));
        t_urgensi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_urgensiActionPerformed(evt);
            }
        });

        j_tanggalSuratDiterima.setText("Tanggal Surat Diterima");

        j_halSurat.setText("Hal Surat");

        j_isiRingkasSurat.setText("Isi Ringkas Surat");

        b_kirim.setText("Kirim");
        b_kirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_kirimActionPerformed(evt);
            }
        });

        b_tambahFile.setText("Tambah File");
        b_tambahFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_tambahFileActionPerformed(evt);
            }
        });

        t_pathFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_pathFileActionPerformed(evt);
            }
        });

        j_unggahFileSurat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        j_unggahFileSurat.setText("Unggah File Surat");

        jPanel8.setBackground(new java.awt.Color(0, 102, 51));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(j_registrasiNaskah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(220, 220, 220))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(t_tglSurat, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                        .addGap(428, 428, 428)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(j_halSurat)
                            .addComponent(t_hal, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(j_isiRingkasSurat)
                            .addComponent(t_isiRingkasSurat, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(j_detailIsiSurat)
                            .addComponent(j_identitasSurat)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(j_instansiPengirim)
                                .addComponent(j_namaPengirim)
                                .addComponent(t_namaPengirim)
                                .addComponent(j_jabatanPengirim)
                                .addComponent(t_jabatanPengirim)
                                .addComponent(t_asalInstansi, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(j_unggahFileSurat)
                            .addComponent(j_tipeSurat)
                            .addComponent(t_jenisSurat, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(j_nomorSurat)
                            .addComponent(j_tanggalSurat)
                            .addComponent(t_nomorSurat, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(j_sifatSurat)
                            .addComponent(j_urgensiSurat)
                            .addComponent(j_tanggalSuratDiterima)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(t_tglSuratDiterima, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(t_urgensi, javax.swing.GroupLayout.Alignment.LEADING, 0, 138, Short.MAX_VALUE)
                                .addComponent(t_sifatSurat, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(t_pathFile, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b_tambahFile)
                                .addGap(102, 102, 102)
                                .addComponent(b_kirim)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 101, Short.MAX_VALUE))
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_kembali)
                    .addComponent(j_registrasiNaskah))
                .addGap(15, 15, 15)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(j_identitasSurat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(j_namaPengirim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_namaPengirim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(j_jabatanPengirim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_jabatanPengirim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(j_instansiPengirim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_asalInstansi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(j_detailIsiSurat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_tipeSurat)
                    .addComponent(j_halSurat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(t_jenisSurat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(j_nomorSurat))
                    .addComponent(t_hal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_nomorSurat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(j_tanggalSurat)
                        .addGap(10, 10, 10)
                        .addComponent(t_tglSurat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(j_sifatSurat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_sifatSurat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(j_urgensiSurat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_urgensi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(j_isiRingkasSurat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_isiRingkasSurat, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(j_tanggalSuratDiterima)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_tglSuratDiterima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(j_unggahFileSurat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_pathFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_tambahFile)
                    .addComponent(b_kirim))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_kembaliActionPerformed
        Dashboard.switchPanel("History Surat Masuk");
    }//GEN-LAST:event_b_kembaliActionPerformed

    private void b_kirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_kirimActionPerformed
        String destDir = "sim.kantordesa.modulPengirimanFiles";
        File destFolder = new File(destDir);
        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }
        File destFile = new File(destFolder, selectedFile.getName());
        int status_id = 1;

        String send_by = t_namaPengirim.getText();
        String sender_position = t_jabatanPengirim.getText();
        String sender_instance = t_asalInstansi.getText();
        String mail_number = t_nomorSurat.getText();
        String mail_type = t_jenisSurat.getSelectedItem().toString();
        String mail_received_date = t_tglSuratDiterima.getText();
        String mail_secrecy = t_sifatSurat.getSelectedItem().toString();
        String mail_urgency = t_urgensi.getSelectedItem().toString();
        String mail_about = t_hal.getText();
        String mail_content = t_isiRingkasSurat.getText();
        String path_file = destFile.getAbsolutePath();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String mail_date = sdf.format(t_tglSurat.getDate());

        try {
            // Salin file ke direktori tujuan
            Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Validasi koneksi database
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Koneksi ke database belum dibuat. Harap periksa konfigurasi koneksi.");
                return;
            }

            String sql = "INSERT INTO mail_received (send_by, sender_position, sender_instance, mail_number, mail_type, mail_secrecy, mail_urgency, mail_received_date, mail_about, mail_content, mail_date, status_id, path_file) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, send_by);
                st.setString(2, sender_position);
                st.setString(3, sender_instance);
                st.setString(4, mail_number);
                st.setString(5, mail_type);
                st.setString(6, mail_secrecy);
                st.setString(7, mail_urgency);
                st.setString(8, mail_received_date);
                st.setString(9, mail_about);
                st.setString(10, mail_content);
                st.setString(11, mail_date);
                st.setInt(12, status_id);

                InputStream inputStream = new FileInputStream(destFile);
                st.setBinaryStream(13, inputStream, (int) destFile.length());

                int rowInserted = st.executeUpdate();
                if (rowInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Surat Berhasil Ditambahkan dan Dikirim Ke Akun Kepala Desa");
                    historySuratMasuk detail = new historySuratMasuk();
                    detail.setVisible(true);
                    resetForm();
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(registrasiNaskah.class.getName()).log(Level.SEVERE, "Kesalahan saat menyimpan data ke database", e);
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + e.getMessage());
        } catch (IOException e) {
            Logger.getLogger(registrasiNaskah.class.getName()).log(Level.SEVERE, "Kesalahan I/O saat mengakses file", e);
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengakses file: " + e.getMessage());
        }

    }//GEN-LAST:event_b_kirimActionPerformed

    private void b_tambahFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_tambahFileActionPerformed
        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
        jfc.setFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile(); // Simpan file yang dipilih ke variabel global
            String fileName = selectedFile.getName();

            // Validasi jika file bukan PDF
            if (!fileName.toLowerCase().endsWith(".pdf")) {
                JOptionPane.showMessageDialog(null, "Hanya file PDF yang diperbolehkan.");
                return; // Menghentikan proses jika bukan file PDF
            }

            String dir = selectedFile.getAbsolutePath(); // Dapatkan path file
            t_pathFile.setText(dir); // Tampilkan path file di text field
            JOptionPane.showMessageDialog(null, "File berhasil dipilih: " + selectedFile.getName());
        } else {
            JOptionPane.showMessageDialog(null, "Tidak ada file yang dipilih.");
        }
    }//GEN-LAST:event_b_tambahFileActionPerformed

    private void t_urgensiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_urgensiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_urgensiActionPerformed

    private void t_namaPengirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_namaPengirimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_namaPengirimActionPerformed

    private void t_jenisSuratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_jenisSuratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_jenisSuratActionPerformed

    private void t_pathFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_pathFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_pathFileActionPerformed
    private void resetForm() {
        t_namaPengirim.setText("");
        t_jabatanPengirim.setText("");
        t_asalInstansi.setText("");
        t_nomorSurat.setText("");
        t_jenisSurat.setSelectedItem("-Pilih");
        t_sifatSurat.setSelectedItem("-Pilih-");
        t_urgensi.setSelectedItem("-Pilih-");
        t_hal.setText("");
        t_isiRingkasSurat.setText("");
        t_pathFile.setText("");
    }

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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new registrasiNaskah().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_kembali;
    private javax.swing.JButton b_kirim;
    private javax.swing.JButton b_tambahFile;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel j_detailIsiSurat;
    private javax.swing.JLabel j_halSurat;
    private javax.swing.JLabel j_identitasSurat;
    private javax.swing.JLabel j_instansiPengirim;
    private javax.swing.JLabel j_isiRingkasSurat;
    private javax.swing.JLabel j_jabatanPengirim;
    private javax.swing.JLabel j_namaPengirim;
    private javax.swing.JLabel j_nomorSurat;
    private javax.swing.JLabel j_registrasiNaskah;
    private javax.swing.JLabel j_sifatSurat;
    private javax.swing.JLabel j_tanggalSurat;
    private javax.swing.JLabel j_tanggalSuratDiterima;
    private javax.swing.JLabel j_tipeSurat;
    private javax.swing.JLabel j_unggahFileSurat;
    private javax.swing.JLabel j_urgensiSurat;
    private javax.swing.JTextField t_asalInstansi;
    private javax.swing.JTextField t_hal;
    private javax.swing.JTextField t_isiRingkasSurat;
    private javax.swing.JTextField t_jabatanPengirim;
    private javax.swing.JComboBox<String> t_jenisSurat;
    private javax.swing.JTextField t_namaPengirim;
    private javax.swing.JTextField t_nomorSurat;
    private javax.swing.JTextField t_pathFile;
    private javax.swing.JComboBox<String> t_sifatSurat;
    private com.toedter.calendar.JDateChooser t_tglSurat;
    private javax.swing.JTextField t_tglSuratDiterima;
    private javax.swing.JComboBox<String> t_urgensi;
    // End of variables declaration//GEN-END:variables
}
