package sim.kantordesa.modulPengiriman;

import java.awt.Container;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sim.kantordesa.config.AppContext;
import sim.kantordesa.config.User;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.dashboard.Dashboard;
import sim.kantordesa.modulPengiriman.registrasiNaskah;

public class detailSurat extends javax.swing.JFrame {

    Connection conn;
    String mail_received_id;
    int idRole = (int) AppContext.get("idRole");
    
    public detailSurat(String mail_received_id) {
        this.mail_received_id = mail_received_id;
        initComponents();
        conn = koneksi.getConnection();
        
        //System.out.println("ID Role: " + idRole);

        DefaultTableModel model = new DefaultTableModel();

        tbl_history.setModel(model);
 
        model.addColumn("Tanggal Proses");
        model.addColumn("Tanggal Penerimaan Surat");
        model.addColumn("Asal Proses");
        model.addColumn("Tujuan");
        model.addColumn("Pesan/Instruksi");
    }
    
    public void updateData() {
        String mailrcvid = (String) AppContext.get("historymasuk_mailRcvId");
        this.mail_received_id = mailrcvid != null ? mailrcvid : "";
        System.out.println("New ID: " + this.mail_received_id);
        loadTabel(this.mail_received_id);
        String disposition_id = getMailDispositionId(this.mail_received_id);
        if (idRole == 1 && disposition_id == null || disposition_id.isEmpty()){
            btn_penyelesaianDisposisi.setEnabled(true);
            btn_disposisi1.setEnabled(true);
            getDataMail(this.mail_received_id);
        } else if (idRole==1 && disposition_id !=null) {
            btn_penyelesaianDisposisi.setEnabled(true);
            btn_disposisi1.setEnabled(false);
            getDataDisposition(this.mail_received_id);
            hidePenyelesaianDisposisi(this.mail_received_id);
        }else if (idRole >= 2 && disposition_id == null || disposition_id.isEmpty()){
            btn_penyelesaianDisposisi.setEnabled(true);
            btn_disposisi1.setVisible(false);
            getDataMail(this.mail_received_id);
        }else if (idRole>=2 && disposition_id !=null){
            getDataDisposition(this.mail_received_id);
            btn_penyelesaianDisposisi.setEnabled(true);
            btn_disposisi1.setVisible(false);
            hidePenyelesaianDisposisi(this.mail_received_id);
        }
    }

    public Container getContentPanel() {
        return this.getContentPane();
    }

    private void loadTabel(String mail_received_id) {
        DefaultTableModel model = (DefaultTableModel) tbl_history.getModel();
        model.setRowCount(0);
        try{
        String sql = "Select  mr.mail_received_date, md.disposition_date, r1.role_name AS mail_destination, r2.role_name AS disposition_destination, "
                + "md.disposition_instruction, md.disposition_notes_staff "
                + "From mail_received mr "
                + "JOIN mail_disposition md ON md.mail_received_id = mr.mail_received_id "
                + "JOIN role r1 ON r1.id_role = mr.mail_destination "
                + "JOIN role r2 ON r2.id_role = md.disposition_destination "
                + "WHERE mr.mail_received_id = ?";
        try (java.sql.PreparedStatement st = conn.prepareStatement(sql);) {
            st.setString(1, mail_received_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                
                String disposition_notes_staff = rs.getString("disposition_notes_staff");
                //String disposition_instruction = rs.getString("disposition_instruction");
                //if (disposition_instruction == null || disposition_instruction.trim().isEmpty()) {
                   // disposition_instruction = rs.getString("disposition_notes_staff");
                    
                    Object[] row1 = {rs.getString("mail_received_date"), rs.getString("disposition_date"), rs.getString("mail_destination"),
                        rs.getString("disposition_destination"), rs.getString("disposition_instruction")};
                    model.addRow(row1);

                    if (disposition_notes_staff != null) {
                        Object[] row2 = {rs.getString("disposition_date"), rs.getString("mail_received_date"), rs.getString("disposition_destination"),
                            rs.getString("mail_destination"), rs.getString("disposition_notes_staff")};
                        model.addRow(row2);
                    }
                }

            }
        } catch (SQLException e) {
            Logger.getLogger(registrasiNaskah.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void getDataMail(String mail_received_id) {
        String sql = "SELECT mr.send_by, mr.sender_instance, mr.mail_number, mr.mail_type, mr.mail_secrecy, mr.mail_urgency, mr.mail_about, "
                + "mr.mail_content, mr.mail_received_date, mr.mail_date , mr.mail_received_id, status.status_name "
                + "FROM mail_received mr "
                + "JOIN status ON mr.status_id = status.status_id "
                + "WHERE mr.mail_received_id = ?";
        try {
            java.sql.PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, mail_received_id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                t_idSurat.setText(rs.getString("mail_received_id"));
                t_instansi.setText(rs.getString("sender_instance"));
                t_perihal.setText(rs.getString("mail_about"));
                t_noSurat.setText(rs.getString("mail_number"));
                t_diterima.setText(rs.getString("mail_received_date"));
                t_urgensi.setText(rs.getString("mail_urgency"));
                t_tipeSurat.setText(rs.getString("mail_type"));
                t_tglSurat.setText(rs.getString("mail_date"));
                t_statusSurat.setText(rs.getString("status_name"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getDataDisposition(String mail_received_id) {
        String sql = "SELECT mr.send_by, mr.sender_instance, mr.mail_number, mr.mail_type, mr.mail_secrecy, mr.mail_urgency, mr.mail_about, mr.mail_content, "
                + "mr.mail_received_date, mr.mail_date, mr.mail_received_id, md.disposition_deadline, "
                + "md.disposition_instruction, md.disposition_notes, md.disposition_date, md.disposition_id, status.status_name, role.role_name "
                + "FROM mail_received mr "
                + "JOIN mail_disposition md ON mr.mail_received_id = md.mail_received_id "
                + "JOIN status ON mr.status_id = status.status_id "
                + "JOIN role ON md.disposition_destination = role.id_role "
                + "WHERE mr.mail_received_id = ?";

        try {
            java.sql.PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, mail_received_id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                t_idSurat.setText(rs.getString("mail_received_id"));
                t_instansi.setText(rs.getString("sender_instance"));
                t_perihal.setText(rs.getString("mail_about"));
                t_noSurat.setText(rs.getString("mail_number"));
                t_diterima.setText(rs.getString("mail_received_date"));
                t_urgensi.setText(rs.getString("mail_urgency"));
                t_tipeSurat.setText(rs.getString("mail_type"));
                t_tglSurat.setText(rs.getString("mail_date"));
                t_tujuanDisposisi.setText(rs.getString("role_name"));
                t_instruksi.setText(rs.getString("disposition_instruction"));
                t_pesanDisposisi.setText(rs.getString("disposition_notes"));
                t_tanggalDisposisi.setText(rs.getString("disposition_date"));
                t_batasWaktu.setText(rs.getString("disposition_deadline"));
                t_idDisposisi.setText(rs.getString("disposition_id"));
                t_statusSurat.setText(rs.getString("status_name"));

            } else {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan");
            }

            //b_penyelesaian.setEnabled(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bukaFile(String mail_received_id) throws IOException {
        String sql = "SELECT path_file FROM mail_received WHERE mail_received_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, mail_received_id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                // Ambil BLOB dari database
                InputStream inputStream = rs.getBinaryStream("path_file");
                if (inputStream != null) {
                    // Tentukan lokasi file sementara
                    File tempFile = File.createTempFile("tempFile", ".pdf");
                    tempFile.deleteOnExit(); // Hapus file otomatis setelah aplikasi selesai

                    // Tulis data dari InputStream ke file sementara
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }

                    // Buka file menggunakan Desktop
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(tempFile);
                    } else {
                        JOptionPane.showMessageDialog(null, "Platform tidak mendukung fitur membuka file.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "File tidak ditemukan dalam database.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Data surat tidak ditemukan di database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saat mengakses database: " + ex.getMessage());
        }
    }

    public String getMailDispositionId(String mail_received_id) {
        String sql = "SELECT disposition_id FROM mail_disposition WHERE mail_received_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mail_received_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("disposition_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Jika tidak ditemukan, kembalikan null
    }

    public void hidePenyelesaianDisposisi(String mail_received_id) {
        String sql = "SELECT disposition_notes_staff FROM mail_disposition WHERE mail_received_id =?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mail_received_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dispositionNotesStaff = rs.getString("disposition_notes_staff");
                if (dispositionNotesStaff == null) {
                    // Jika nilai disposition_notes_staff adalah null, tombol di-enable
                    btn_penyelesaianDisposisi.setEnabled(true);
                } else {
                    // Jika nilai disposition_notes_staff tidak null, tombol di-disable
                    btn_penyelesaianDisposisi.setEnabled(false);
                }
            } else {
                // Jika tidak ada data, tombol di-enable
                btn_penyelesaianDisposisi.setEnabled(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        j_formDisposisi = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        j_formDisposisi1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        t_statusSurat = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        t_idDisposisi = new javax.swing.JTextField();
        j_tipeSurat = new javax.swing.JLabel();
        t_tipeSurat = new javax.swing.JTextField();
        j_urgensi = new javax.swing.JLabel();
        t_urgensi = new javax.swing.JTextField();
        j_diterima = new javax.swing.JLabel();
        t_diterima = new javax.swing.JTextField();
        j_formDisposisi2 = new javax.swing.JLabel();
        j_tipeSurat1 = new javax.swing.JLabel();
        t_idSurat = new javax.swing.JTextField();
        j_formDisposisi3 = new javax.swing.JLabel();
        j_instansi = new javax.swing.JLabel();
        t_tglSurat = new javax.swing.JTextField();
        t_perihal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        t_instansi = new javax.swing.JTextField();
        t_noSurat = new javax.swing.JTextField();
        www = new javax.swing.JLabel();
        b_download = new javax.swing.JButton();
        j_noSurat = new javax.swing.JLabel();
        j_perihal = new javax.swing.JLabel();
        j_tujuanDisposisi = new javax.swing.JLabel();
        t_tujuanDisposisi = new javax.swing.JTextField();
        j_pesanDisposisi = new javax.swing.JLabel();
        t_pesanDisposisi = new javax.swing.JTextField();
        j_tanggalDisposisi = new javax.swing.JLabel();
        t_tanggalDisposisi = new javax.swing.JTextField();
        j_batasWaktu = new javax.swing.JLabel();
        t_batasWaktu = new javax.swing.JTextField();
        j_instruksi = new javax.swing.JLabel();
        t_instruksi = new javax.swing.JTextField();
        j_formDisposisi4 = new javax.swing.JLabel();
        btn_kembali = new javax.swing.JButton();
        btn_penyelesaianDisposisi = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_history = new javax.swing.JTable();
        j_formDisposisi5 = new javax.swing.JLabel();
        btn_disposisi1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1290, 646));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1300, 640));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel6.setPreferredSize(new java.awt.Dimension(1290, 640));

        j_formDisposisi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        j_formDisposisi.setText("Detail Surat");

        jPanel7.setBackground(new java.awt.Color(0, 102, 51));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        j_formDisposisi1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        j_formDisposisi1.setText("Status Surat");

        jLabel1.setText("Status Surat : ");

        t_statusSurat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_statusSuratActionPerformed(evt);
            }
        });

        jLabel2.setText("ID Disposisi :");

        t_idDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_idDisposisiActionPerformed(evt);
            }
        });

        j_tipeSurat.setText("Tipe Surat");

        t_tipeSurat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_tipeSuratActionPerformed(evt);
            }
        });

        j_urgensi.setText("Urgensi");

        t_urgensi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_urgensiActionPerformed(evt);
            }
        });

        j_diterima.setText("Diterima");

        t_diterima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_diterimaActionPerformed(evt);
            }
        });

        j_formDisposisi2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        j_formDisposisi2.setText("DETAIL SURAT MASUK");

        j_tipeSurat1.setText("ID Surat");

        t_idSurat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_idSuratActionPerformed(evt);
            }
        });

        j_formDisposisi3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        j_formDisposisi3.setText("Nomor Surat");

        j_instansi.setText("Instansi");

        jLabel3.setText("File");

        t_noSurat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_noSuratActionPerformed(evt);
            }
        });

        www.setText("Tanggal Surat");

        b_download.setText("Download");
        b_download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_downloadActionPerformed(evt);
            }
        });

        j_noSurat.setText("No. Surat");

        j_perihal.setText("Perihal");

        j_tujuanDisposisi.setText("Tujuan Disposisi");

        t_tujuanDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_tujuanDisposisiActionPerformed(evt);
            }
        });

        j_pesanDisposisi.setText("Pesan Disposisi");

        j_tanggalDisposisi.setText("Tanggal Disposisi");

        t_tanggalDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_tanggalDisposisiActionPerformed(evt);
            }
        });

        j_batasWaktu.setText("Batas Waktu");

        j_instruksi.setText("Instruksi");

        j_formDisposisi4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        j_formDisposisi4.setText("Detail Disposisi");

        btn_kembali.setText("Kembali");
        btn_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kembaliActionPerformed(evt);
            }
        });

        btn_penyelesaianDisposisi.setText("Balas Surat");
        btn_penyelesaianDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_penyelesaianDisposisiActionPerformed(evt);
            }
        });

        tbl_history.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tbl_history);

        j_formDisposisi5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        j_formDisposisi5.setText("History Surat");

        btn_disposisi1.setText("Disposisi");
        btn_disposisi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_disposisi1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(j_formDisposisi2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_penyelesaianDisposisi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_disposisi1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(134, 134, 134))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(j_formDisposisi)
                                .addComponent(j_formDisposisi3)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                            .addComponent(j_diterima, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(t_diterima, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                            .addComponent(j_urgensi, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(t_urgensi))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addComponent(j_tipeSurat, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(12, 12, 12)
                                            .addComponent(t_tipeSurat)))
                                    .addGap(168, 168, 168)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(j_tipeSurat1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(t_idSurat, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(www)
                                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(j_noSurat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(j_instansi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(j_perihal, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(t_perihal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                                            .addComponent(t_instansi, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(t_noSurat, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(t_tglSurat)
                                            .addComponent(b_download, javax.swing.GroupLayout.Alignment.LEADING))))
                                .addGap(88, 88, 88)))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(j_tujuanDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j_pesanDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(t_tujuanDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(t_pesanDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(j_batasWaktu)
                                            .addComponent(j_instruksi))
                                        .addGap(34, 34, 34))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j_tanggalDisposisi)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(t_instruksi, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(300, Short.MAX_VALUE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(t_batasWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(t_tanggalDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel1))
                                        .addGap(26, 26, 26)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(t_statusSurat, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                                            .addComponent(t_idDisposisi)))
                                    .addComponent(j_formDisposisi4)
                                    .addComponent(j_formDisposisi1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(437, Short.MAX_VALUE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(j_formDisposisi5)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(j_formDisposisi2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_penyelesaianDisposisi)
                            .addComponent(btn_disposisi1)
                            .addComponent(btn_kembali))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(j_formDisposisi3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(j_tipeSurat1)
                            .addComponent(t_idSurat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(j_tipeSurat)
                            .addComponent(t_tipeSurat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(j_formDisposisi1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(t_statusSurat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(t_idDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(t_urgensi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(j_urgensi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(j_diterima)
                            .addComponent(t_diterima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addComponent(j_formDisposisi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(j_noSurat)
                            .addComponent(t_noSurat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(j_instansi)
                            .addComponent(t_instansi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(j_perihal)
                            .addComponent(t_perihal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(www)
                            .addComponent(t_tglSurat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_batasWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(j_batasWaktu))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(b_download)
                            .addComponent(jLabel3)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(j_instruksi))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(j_formDisposisi4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(t_tujuanDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(j_tujuanDisposisi))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(j_pesanDisposisi)
                            .addComponent(t_pesanDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(t_tanggalDisposisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(j_tanggalDisposisi))
                        .addGap(53, 53, 53)
                        .addComponent(t_instruksi, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(j_formDisposisi5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void t_tujuanDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_tujuanDisposisiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_tujuanDisposisiActionPerformed

    private void b_downloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_downloadActionPerformed
        String mail_received_id = t_idSurat.getText();

        if (mail_received_id != null) {
            try {
                bukaFile(mail_received_id);
            } catch (IOException ex) {
                Logger.getLogger(detailSurat.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Mail ID Tidak Ditemukan");
        }
    }//GEN-LAST:event_b_downloadActionPerformed

    private void t_idSuratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_idSuratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_idSuratActionPerformed

    private void t_diterimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_diterimaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_diterimaActionPerformed

    private void t_urgensiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_urgensiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_urgensiActionPerformed

    private void t_tipeSuratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_tipeSuratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_tipeSuratActionPerformed

    private void t_idDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_idDisposisiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_idDisposisiActionPerformed

    private void t_statusSuratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_statusSuratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_statusSuratActionPerformed

    private void t_tanggalDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_tanggalDisposisiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_tanggalDisposisiActionPerformed

    private void t_noSuratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_noSuratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_noSuratActionPerformed

    private void btn_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kembaliActionPerformed
        Dashboard.switchPanel("History Surat Masuk");
    }//GEN-LAST:event_btn_kembaliActionPerformed

    private void btn_penyelesaianDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_penyelesaianDisposisiActionPerformed
        String mail_received_id = t_idSurat.getText();
        replyStaff detail = new replyStaff(mail_received_id);
        Dashboard.card.add(detail.getContentPanel(), mail_received_id + "penyelesaian");
        Dashboard.switchPanel(mail_received_id + "penyelesaian");
    }//GEN-LAST:event_btn_penyelesaianDisposisiActionPerformed

    private void btn_disposisi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_disposisi1ActionPerformed
        String mail_received_id = t_idSurat.getText();
        tampilanDisposisi detail = new tampilanDisposisi(mail_received_id);
        Dashboard.card.add(detail.getContentPanel(), mail_received_id + "disposisi");
        Dashboard.switchPanel(mail_received_id + "disposisi");
    }//GEN-LAST:event_btn_disposisi1ActionPerformed

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
            java.util.logging.Logger.getLogger(detailSurat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(detailSurat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(detailSurat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(detailSurat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new detailSurat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_download;
    private javax.swing.JButton btn_disposisi1;
    private javax.swing.JButton btn_kembali;
    private javax.swing.JButton btn_penyelesaianDisposisi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel j_batasWaktu;
    private javax.swing.JLabel j_diterima;
    private javax.swing.JLabel j_formDisposisi;
    private javax.swing.JLabel j_formDisposisi1;
    private javax.swing.JLabel j_formDisposisi2;
    private javax.swing.JLabel j_formDisposisi3;
    private javax.swing.JLabel j_formDisposisi4;
    private javax.swing.JLabel j_formDisposisi5;
    private javax.swing.JLabel j_instansi;
    private javax.swing.JLabel j_instruksi;
    private javax.swing.JLabel j_noSurat;
    private javax.swing.JLabel j_perihal;
    private javax.swing.JLabel j_pesanDisposisi;
    private javax.swing.JLabel j_tanggalDisposisi;
    private javax.swing.JLabel j_tipeSurat;
    private javax.swing.JLabel j_tipeSurat1;
    private javax.swing.JLabel j_tujuanDisposisi;
    private javax.swing.JLabel j_urgensi;
    private javax.swing.JTextField t_batasWaktu;
    private javax.swing.JTextField t_diterima;
    private javax.swing.JTextField t_idDisposisi;
    private javax.swing.JTextField t_idSurat;
    private javax.swing.JTextField t_instansi;
    private javax.swing.JTextField t_instruksi;
    private javax.swing.JTextField t_noSurat;
    private javax.swing.JTextField t_perihal;
    private javax.swing.JTextField t_pesanDisposisi;
    private javax.swing.JTextField t_statusSurat;
    private javax.swing.JTextField t_tanggalDisposisi;
    private javax.swing.JTextField t_tglSurat;
    private javax.swing.JTextField t_tipeSurat;
    private javax.swing.JTextField t_tujuanDisposisi;
    private javax.swing.JTextField t_urgensi;
    private javax.swing.JTable tbl_history;
    private javax.swing.JLabel www;
    // End of variables declaration//GEN-END:variables
}