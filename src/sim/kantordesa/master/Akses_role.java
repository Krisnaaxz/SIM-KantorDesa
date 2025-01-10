package sim.kantordesa.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import sim.kantordesa.config.koneksi;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class Akses_role extends javax.swing.JFrame {

    private final Connection conn;
    private int roleId;

    public Akses_role() {
        initComponents();
        conn = koneksi.getConnection();
        getAllData();
        getData();
        role();
        tampilan();
        hideAkses();
        nonAktifButton();
        getContentPanel();
        roleId = -1;
        initializeToggleButtons(roleId);
        addToggleButtonListeners();
    }

    public JPanel getContentPanel() {
        return (JPanel) this.getContentPane();
    }

    private void getAllData() {
        DefaultTableModel model = (DefaultTableModel) tabel_data.getModel();
        model.setRowCount(0);

        try {
            String sql = "SELECT role.id_role, role.role_name, access.access_name "
                    + "FROM role "
                    + "INNER JOIN role_access ON role.id_role = role_access.role_id "
                    + "INNER JOIN access ON role_access.access_id = access.access_id";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_role"),
                    rs.getString("role_name"),
                    rs.getString("access_name")
                };
                model.addRow(row);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void getData() {
        DefaultTableModel model = (DefaultTableModel) tabel_data.getModel();
        model.setRowCount(0);

        try {
            String selectedRole = cmb_role.getSelectedItem() != null ? cmb_role.getSelectedItem().toString() : "";
            if (roleMap.containsKey(selectedRole)) {
                int roleId = roleMap.get(selectedRole);

                String sql = "SELECT role.id_role, role.role_name, access.access_name "
                        + "FROM role "
                        + "INNER JOIN role_access ON role.id_role = role_access.role_id "
                        + "INNER JOIN access ON role_access.access_id = access.access_id "
                        + "WHERE role.id_role = ?";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1, roleId);
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("id_role"),
                        rs.getString("role_name"),
                        rs.getString("access_name")
                    };
                    model.addRow(row);
                }

                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private final Map<String, Integer> roleMap = new HashMap<>();

    private int getSelectedRoleId() {
        String selectedRole = cmb_role.getSelectedItem() != null ? cmb_role.getSelectedItem().toString() : "";
        if (roleMap.containsKey(selectedRole)) {
            return roleMap.get(selectedRole);
        }
        return -1; // Role ID tidak valid
    }

    public void role() {
        try {
            String sql = "SELECT * FROM role";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            getData();
            getAllData();

            // Tambahkan item "Pilih Role" pertama di combobox
            cmb_role.addItem("Pilih Role");

            while (rs.next()) {
                String roleName = rs.getString("role_name");
                int roleId = rs.getInt("id_role");
                roleMap.put(roleName, roleId);
                cmb_role.addItem(roleName);
            }

            cmb_role.addItem("Baru"); // Tambahkan item "Baru"

            // ActionListener untuk combobox cmb_role
            cmb_role.addActionListener((ActionEvent e) -> {
                String selectedRole = cmb_role.getSelectedItem() != null ? cmb_role.getSelectedItem().toString() : "";

                if (selectedRole.equals("Baru")) {
                    // Menampilkan komponen untuk input role baru
                    getAllData();
                    lbl_roleBaru.setVisible(true);
                    txt_roleBaru.setVisible(true);
                    txt_id.setText("");
                    btn_tambahRole.setVisible(true);

                    // Set roleId ke -1 untuk role baru
                    roleId = -1;

                    // Sembunyikan semua checkbox
                    hideAkses();

                    // Nonaktifkan tombol aksi lainnya
                    nonAktifButton();

                } else if (roleMap.containsKey(selectedRole)) {
                    // Reset label input role baru
                    getData();
                    lbl_roleBaru.setVisible(false);
                    txt_roleBaru.setVisible(false);
                    btn_tambahRole.setVisible(false);

                    // Dapatkan ID role dari roleMap
                    roleId = roleMap.get(selectedRole); // Perbarui roleId dengan ID role yang dipilih
                    txt_id.setText(String.valueOf(roleId)); // Tampilkan ID role

                    // Muat akses untuk role yang dipilih
                    loadRoleAccess(roleId);

                    // Inisialisasi toggle berdasarkan role yang dipilih
                    initializeToggleButtons(roleId);

                    // Tampilkan semua checkbox
                    showAkses();

                    // Aktifkan tombol aksi
                    aktifButton();

                } else {
                    // Jika "Pilih Role" dipilih atau kondisi lainnya
                    getAllData();
                    txt_id.setText(""); // Kosongkan ID
                    roleId = -1; // Reset roleId
                    hideAkses();
                    nonAktifButton();
                }
            });

        } catch (SQLException e) {
            Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private final Map<String, Boolean> accessStatusMap = new HashMap<>();

    private void initializeToggleButtons(int roleId) {
        try {
            // Ambil data akses dari database
            String sql = "SELECT access.access_name FROM role_access "
                    + "INNER JOIN access ON role_access.access_id = access.access_id "
                    + "WHERE role_access.role_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();

            accessStatusMap.clear();

            while (rs.next()) {
                String accessName = rs.getString("access_name");
                accessStatusMap.put(accessName, true); // Set akses aktif
            }

            // Inisialisasi toggle button berdasarkan data
            configureToggleButton(tog_SuratMasuk, lbl_SuratMasuk, "Form Surat Masuk", true);
            configureToggleButton(tog_SuratKeluar, lbl_SuratKeluar, "Form Surat Keluar", true);
            configureToggleButton(tog_HistorySuratMasuk, lbl_HistorySuratMasuk, "History Surat Masuk", true);
            configureToggleButton(tog_HistorySuratKeluar, lbl_HistorySuratKeluar, "History Surat Keluar", true);
            configureToggleButton(tog_Disposisi, lbl_Disposisi, "Disposisi", true);
            configureToggleButton(tog_Validasi, lbl_Validasi, "Validasi", true);
            configureToggleButton(tog_Pelaporan, lbl_Pelaporan, "Pelaporan", true);
            configureToggleButton(tog_DaftarAkun, lbl_DaftarAkun, "Daftar Akun", true);
            configureToggleButton(tog_AksesRole, lbl_AksesRole, "Akses Role", true);

            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void configureToggleButton(JToggleButton toggleButton, JLabel statusLabel, String accessName, boolean isInitializing) {
        if (isInitializing) {
            // Inisialisasi status toggle berdasarkan data di accessStatusMap
            boolean isOn = accessStatusMap.getOrDefault(accessName, false);
            toggleButton.setSelected(isOn);
            statusLabel.setText(isOn ? "ON" : "OFF");
            toggleButton.setText(isOn ? "OFF" : "ON");
        } else {
            // Tambahkan listener aksi pada toggle
            toggleButton.addActionListener(evt -> {
                boolean isSelected = toggleButton.isSelected();
                accessStatusMap.put(accessName, isSelected); // Simpan status sementara
                statusLabel.setText(isSelected ? "ON" : "OFF"); // Perbarui label status
                toggleButton.setText(isSelected ? "OFF" : "ON"); // Perbarui teks tombol
            });
        }
    }

    private void addToggleButtonListeners() {
        configureToggleButton(tog_SuratMasuk, lbl_SuratMasuk, "Form Surat Masuk", false);
        configureToggleButton(tog_SuratKeluar, lbl_SuratKeluar, "Form Surat Keluar", false);
        configureToggleButton(tog_HistorySuratMasuk, lbl_HistorySuratMasuk, "History Surat Masuk", false);
        configureToggleButton(tog_HistorySuratKeluar, lbl_HistorySuratKeluar, "History Surat Keluar", false);
        configureToggleButton(tog_Disposisi, lbl_Disposisi, "Disposisi", false);
        configureToggleButton(tog_Validasi, lbl_Validasi, "Validasi", false);
        configureToggleButton(tog_Pelaporan, lbl_Pelaporan, "Pelaporan", false);
        configureToggleButton(tog_DaftarAkun, lbl_DaftarAkun, "Daftar Akun", false);
        configureToggleButton(tog_AksesRole, lbl_AksesRole, "Akses Role", false);
    }

    private boolean isAccessAlreadyAssigned(int roleId, int accessId) throws SQLException {
        String sqlCheckAccess = "SELECT COUNT(*) AS count FROM role_access WHERE role_id = ? AND access_id = ?";
        PreparedStatement psCheck = conn.prepareStatement(sqlCheckAccess);
        psCheck.setInt(1, roleId);
        psCheck.setInt(2, accessId);
        ResultSet rs = psCheck.executeQuery();

        if (rs.next()) {
            return rs.getInt("count") > 0; // Jika COUNT > 0, artinya sudah ada
        }

        return false; // Tidak ditemukan
    }

    private int getExistingAccessId(String accessName) throws SQLException {
        String sqlGetAccessId = "SELECT access_id FROM access WHERE access_name = ?";
        PreparedStatement ps = conn.prepareStatement(sqlGetAccessId);
        ps.setString(1, accessName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("access_id");
        } else {
            JOptionPane.showMessageDialog(this, "Akses '" + accessName + "' tidak ditemukan di database.");
            return -1; // Jika tidak ditemukan, kembalikan nilai invalid
        }
    }

    private void loadRoleAccess(int roleId) {
        try {
            String sql = "SELECT access.access_name "
                    + "FROM role_access "
                    + "INNER JOIN access ON role_access.access_id = access.access_id "
                    + "WHERE role_access.role_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, roleId);
            ResultSet rs = st.executeQuery();

            // Loop melalui hasil query dan atur toggle button yang sesuai
            while (rs.next()) {
                String accessName = rs.getString("access_name");

                // Cocokkan access_name dengan toggle button
                if (accessName.equals("Form Surat Masuk")) {
                    tog_SuratMasuk.setSelected(true);
                    lbl_SuratMasuk.setText("ON");
                }
                if (accessName.equals("Form Surat Keluar")) {
                    tog_SuratKeluar.setSelected(true);
                    lbl_SuratKeluar.setText("ON");
                }
                if (accessName.equals("History Surat Masuk")) {
                    tog_HistorySuratMasuk.setSelected(true);
                    lbl_HistorySuratMasuk.setText("ON");
                }
                if (accessName.equals("History Surat Keluar")) {
                    tog_HistorySuratKeluar.setSelected(true);
                    lbl_HistorySuratKeluar.setText("ON");
                }
                if (accessName.equals("Disposisi")) {
                    tog_Disposisi.setSelected(true);
                    lbl_Disposisi.setText("ON");
                }
                if (accessName.equals("Validasi")) {
                    tog_Validasi.setSelected(true);
                    lbl_Validasi.setText("ON");
                }
                if (accessName.equals("Pelaporan")) {
                    tog_Pelaporan.setSelected(true);
                    lbl_Pelaporan.setText("ON");
                }
                if (accessName.equals("Daftar Akun")) {
                    tog_DaftarAkun.setSelected(true);
                    lbl_DaftarAkun.setText("ON");
                }
                if (accessName.equals("Akses Role")) {
                    tog_AksesRole.setSelected(true);
                    lbl_AksesRole.setText("ON");
                }
            }

            rs.close();
            st.close();

        } catch (SQLException e) {
            Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Set<String> getExistingAccessNames(int roleId) {
        Set<String> existingAccess = new HashSet<>();
        try {
            String sql = "SELECT access.access_name "
                    + "FROM role_access "
                    + "INNER JOIN access ON role_access.access_id = access.access_id "
                    + "WHERE role_access.role_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, roleId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                existingAccess.add(rs.getString("access_name"));
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, e);
        }
        return existingAccess;
    }

    private void hideAkses() {
        lbl_akses.setVisible(false);
        FormSuratMasuk.setVisible(false);
        tog_SuratMasuk.setVisible(false);
        Status1.setVisible(false);
        lbl_SuratMasuk.setVisible(false);
        FormSuratKeluar.setVisible(false);
        tog_SuratKeluar.setVisible(false);
        Status2.setVisible(false);
        lbl_SuratKeluar.setVisible(false);
        HistorySuratMasuk.setVisible(false);
        tog_HistorySuratMasuk.setVisible(false);
        Status3.setVisible(false);
        lbl_HistorySuratMasuk.setVisible(false);
        HistorySuratKeluar.setVisible(false);
        tog_HistorySuratKeluar.setVisible(false);
        Status4.setVisible(false);
        lbl_HistorySuratKeluar.setVisible(false);
        Disposisi.setVisible(false);
        tog_Disposisi.setVisible(false);
        Status5.setVisible(false);
        lbl_Disposisi.setVisible(false);
        Validasi.setVisible(false);
        tog_Validasi.setVisible(false);
        Status6.setVisible(false);
        lbl_Validasi.setVisible(false);
        Pelaporan.setVisible(false);
        tog_Pelaporan.setVisible(false);
        Status9.setVisible(false);
        lbl_Pelaporan.setVisible(false);
        DaftarAkun.setVisible(false);
        tog_DaftarAkun.setVisible(false);
        Status7.setVisible(false);
        lbl_DaftarAkun.setVisible(false);
        AksesRole.setVisible(false);
        tog_AksesRole.setVisible(false);
        Status8.setVisible(false);
        lbl_AksesRole.setVisible(false);
    }

    private void showAkses() {
        lbl_akses.setVisible(true);
        FormSuratMasuk.setVisible(true);
        tog_SuratMasuk.setVisible(true);
        Status1.setVisible(true);
        lbl_SuratMasuk.setVisible(true);
        FormSuratKeluar.setVisible(true);
        tog_SuratKeluar.setVisible(true);
        Status2.setVisible(true);
        lbl_SuratKeluar.setVisible(true);
        HistorySuratMasuk.setVisible(true);
        tog_HistorySuratMasuk.setVisible(true);
        Status3.setVisible(true);
        lbl_HistorySuratMasuk.setVisible(true);
        HistorySuratKeluar.setVisible(true);
        tog_HistorySuratKeluar.setVisible(true);
        Status4.setVisible(true);
        lbl_HistorySuratKeluar.setVisible(true);
        Disposisi.setVisible(true);
        tog_Disposisi.setVisible(true);
        Status5.setVisible(true);
        lbl_Disposisi.setVisible(true);
        Validasi.setVisible(true);
        tog_Validasi.setVisible(true);
        Status6.setVisible(true);
        lbl_Validasi.setVisible(true);
        Pelaporan.setVisible(true);
        tog_Pelaporan.setVisible(true);
        Status9.setVisible(true);
        lbl_Pelaporan.setVisible(true);
        DaftarAkun.setVisible(true);
        tog_DaftarAkun.setVisible(true);
        Status7.setVisible(true);
        lbl_DaftarAkun.setVisible(true);
        AksesRole.setVisible(true);
        tog_AksesRole.setVisible(true);
        Status8.setVisible(true);
        lbl_AksesRole.setVisible(true);

    }

    private void nonAktifButton() {
        btn_tambah.setVisible(false);
        btn_hapus.setVisible(false);
    }

    private void aktifButton() {
        btn_tambah.setVisible(true);
        btn_hapus.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        User_access = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbl_akses = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_data = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        btn_tambah = new javax.swing.JButton();
        txt_cari = new javax.swing.JTextField();
        btn_cari = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        cmb_role = new javax.swing.JComboBox<>();
        lbl_roleBaru = new javax.swing.JLabel();
        txt_roleBaru = new javax.swing.JTextField();
        btn_tambahRole = new javax.swing.JButton();
        tog_SuratMasuk = new javax.swing.JToggleButton();
        FormSuratMasuk = new javax.swing.JLabel();
        Status1 = new javax.swing.JLabel();
        lbl_SuratMasuk = new javax.swing.JLabel();
        FormSuratKeluar = new javax.swing.JLabel();
        tog_SuratKeluar = new javax.swing.JToggleButton();
        Status2 = new javax.swing.JLabel();
        lbl_SuratKeluar = new javax.swing.JLabel();
        HistorySuratMasuk = new javax.swing.JLabel();
        tog_HistorySuratMasuk = new javax.swing.JToggleButton();
        Status3 = new javax.swing.JLabel();
        lbl_HistorySuratMasuk = new javax.swing.JLabel();
        HistorySuratKeluar = new javax.swing.JLabel();
        tog_HistorySuratKeluar = new javax.swing.JToggleButton();
        Status4 = new javax.swing.JLabel();
        lbl_HistorySuratKeluar = new javax.swing.JLabel();
        lbl_Disposisi = new javax.swing.JLabel();
        Disposisi = new javax.swing.JLabel();
        Status5 = new javax.swing.JLabel();
        tog_Disposisi = new javax.swing.JToggleButton();
        lbl_Validasi = new javax.swing.JLabel();
        Validasi = new javax.swing.JLabel();
        tog_Validasi = new javax.swing.JToggleButton();
        Status6 = new javax.swing.JLabel();
        DaftarAkun = new javax.swing.JLabel();
        lbl_DaftarAkun = new javax.swing.JLabel();
        Status7 = new javax.swing.JLabel();
        tog_DaftarAkun = new javax.swing.JToggleButton();
        Status8 = new javax.swing.JLabel();
        lbl_AksesRole = new javax.swing.JLabel();
        tog_AksesRole = new javax.swing.JToggleButton();
        AksesRole = new javax.swing.JLabel();
        Pelaporan = new javax.swing.JLabel();
        tog_Pelaporan = new javax.swing.JToggleButton();
        Status9 = new javax.swing.JLabel();
        lbl_Pelaporan = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(32767, 32767));

        User_access.setBackground(new java.awt.Color(255, 255, 255));
        User_access.setPreferredSize(new java.awt.Dimension(1300, 640));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setText("Akses Role");

        jLabel2.setText("Role");

        lbl_akses.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_akses.setText("Nama Akses");

        tabel_data.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tabel_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID Role", "Role", "Akses"
            }
        ));
        jScrollPane1.setViewportView(tabel_data);

        jLabel4.setText("ID");

        txt_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idActionPerformed(evt);
            }
        });

        btn_tambah.setText("Simpan");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_cari.setText("Cari");
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });

        btn_hapus.setText("Hapus");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        cmb_role.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_roleActionPerformed(evt);
            }
        });

        lbl_roleBaru.setText("Role Baru");

        btn_tambahRole.setText("Tambah Role");
        btn_tambahRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahRoleActionPerformed(evt);
            }
        });

        tog_SuratMasuk.setText("ON");

        FormSuratMasuk.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        FormSuratMasuk.setText("Form Surat Masuk");

        Status1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status1.setText("STATUS :");

        lbl_SuratMasuk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_SuratMasuk.setText("ON");

        FormSuratKeluar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        FormSuratKeluar.setText("Form Surat Keluar");
        FormSuratKeluar.setPreferredSize(new java.awt.Dimension(102, 16));

        tog_SuratKeluar.setText("ON");

        Status2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status2.setText("STATUS :");

        lbl_SuratKeluar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_SuratKeluar.setText("ON");

        HistorySuratMasuk.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        HistorySuratMasuk.setText("History Surat Masuk");
        HistorySuratMasuk.setPreferredSize(new java.awt.Dimension(102, 16));

        tog_HistorySuratMasuk.setText("ON");

        Status3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status3.setText("STATUS :");

        lbl_HistorySuratMasuk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_HistorySuratMasuk.setText("ON");

        HistorySuratKeluar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        HistorySuratKeluar.setText("History Surat Keluar");
        HistorySuratKeluar.setPreferredSize(new java.awt.Dimension(102, 16));

        tog_HistorySuratKeluar.setText("ON");

        Status4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status4.setText("STATUS :");

        lbl_HistorySuratKeluar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_HistorySuratKeluar.setText("ON");

        lbl_Disposisi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Disposisi.setText("ON");

        Disposisi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Disposisi.setText("Disposisi");
        Disposisi.setPreferredSize(new java.awt.Dimension(102, 16));

        Status5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status5.setText("STATUS :");

        tog_Disposisi.setText("ON");

        lbl_Validasi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Validasi.setText("ON");

        Validasi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Validasi.setText("Validasi");
        Validasi.setPreferredSize(new java.awt.Dimension(102, 16));

        tog_Validasi.setText("ON");

        Status6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status6.setText("STATUS :");

        DaftarAkun.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        DaftarAkun.setText("Daftar Akun");
        DaftarAkun.setPreferredSize(new java.awt.Dimension(102, 16));

        lbl_DaftarAkun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DaftarAkun.setText("ON");

        Status7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status7.setText("STATUS :");

        tog_DaftarAkun.setText("ON");

        Status8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status8.setText("STATUS :");

        lbl_AksesRole.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_AksesRole.setText("ON");

        tog_AksesRole.setText("ON");

        AksesRole.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AksesRole.setText("Akses Role");
        AksesRole.setPreferredSize(new java.awt.Dimension(102, 16));

        Pelaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Pelaporan.setText("Pelaporan");
        Pelaporan.setPreferredSize(new java.awt.Dimension(102, 16));

        tog_Pelaporan.setText("ON");
        tog_Pelaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tog_PelaporanActionPerformed(evt);
            }
        });

        Status9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status9.setText("STATUS :");

        lbl_Pelaporan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Pelaporan.setText("ON");

        javax.swing.GroupLayout User_accessLayout = new javax.swing.GroupLayout(User_access);
        User_access.setLayout(User_accessLayout);
        User_accessLayout.setHorizontalGroup(
            User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(User_accessLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(User_accessLayout.createSequentialGroup()
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btn_tambah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tog_SuratKeluar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tog_HistorySuratMasuk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tog_HistorySuratKeluar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tog_SuratMasuk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_hapus))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, User_accessLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(Status1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(Status2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(Status3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(Status4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, 0)
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_SuratMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_SuratKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_HistorySuratMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_HistorySuratKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(101, 101, 101)
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addComponent(tog_AksesRole, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(Status8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_AksesRole, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addComponent(tog_DaftarAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(Status7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(lbl_DaftarAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addComponent(tog_Disposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(Status5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(lbl_Disposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addComponent(tog_Validasi, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(Status6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(lbl_Validasi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(AksesRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DaftarAkun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Validasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Disposisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(User_accessLayout.createSequentialGroup()
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmb_role, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(User_accessLayout.createSequentialGroup()
                        .addComponent(lbl_roleBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_roleBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addComponent(btn_tambahRole, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_akses, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FormSuratMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FormSuratKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HistorySuratMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HistorySuratKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(User_accessLayout.createSequentialGroup()
                        .addComponent(tog_Pelaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Status9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lbl_Pelaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Pelaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(User_accessLayout.createSequentialGroup()
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(188, Short.MAX_VALUE))
        );
        User_accessLayout.setVerticalGroup(
            User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(User_accessLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(User_accessLayout.createSequentialGroup()
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmb_role, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_roleBaru)
                            .addComponent(txt_roleBaru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_tambahRole))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_akses)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(User_accessLayout.createSequentialGroup()
                                        .addComponent(FormSuratMasuk)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(tog_SuratMasuk)
                                            .addComponent(Status1)
                                            .addComponent(lbl_SuratMasuk)))
                                    .addGroup(User_accessLayout.createSequentialGroup()
                                        .addComponent(Disposisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(tog_Disposisi)
                                            .addComponent(Status5)
                                            .addComponent(lbl_Disposisi))))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(FormSuratKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tog_SuratKeluar)
                                    .addComponent(Status2)
                                    .addComponent(lbl_SuratKeluar)))
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addComponent(Validasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tog_Validasi)
                                    .addComponent(Status6)
                                    .addComponent(lbl_Validasi))))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addComponent(HistorySuratMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tog_HistorySuratMasuk)
                                    .addComponent(Status3)
                                    .addComponent(lbl_HistorySuratMasuk)))
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addComponent(DaftarAkun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tog_DaftarAkun)
                                    .addComponent(Status7)
                                    .addComponent(lbl_DaftarAkun))))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addComponent(HistorySuratKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tog_HistorySuratKeluar)
                                    .addComponent(Status4)
                                    .addComponent(lbl_HistorySuratKeluar))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(Pelaporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tog_Pelaporan)
                                    .addComponent(Status9)
                                    .addComponent(lbl_Pelaporan))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btn_tambah)
                                    .addComponent(btn_hapus)))
                            .addGroup(User_accessLayout.createSequentialGroup()
                                .addComponent(AksesRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(User_accessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tog_AksesRole)
                                    .addComponent(Status8)
                                    .addComponent(lbl_AksesRole)))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(User_access, javax.swing.GroupLayout.DEFAULT_SIZE, 1291, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(User_access, javax.swing.GroupLayout.PREFERRED_SIZE, 634, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahRoleActionPerformed
        String roleBaru = txt_roleBaru.getText().trim();

        if (!roleBaru.isEmpty()) {
            try {
                // Tambahkan role baru ke database
                String sqlInsert = "INSERT INTO role (role_name) VALUES (?)";
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, roleBaru);
                ps.executeUpdate();

                // Ambil ID role baru yang baru ditambahkan
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int roleId = generatedKeys.getInt(1); // ID role baru

                    // Menambahkan role ke roleMap dan combobox
                    roleMap.put(roleBaru, roleId);
                    cmb_role.addItem(roleBaru);
                    cmb_role.setSelectedItem(roleBaru);
                    txt_id.setText(String.valueOf(roleId));

                    // Sembunyikan input role baru dan tampilkan checkbox
                    lbl_roleBaru.setVisible(false);
                    txt_roleBaru.setVisible(false);
                    btn_tambahRole.setVisible(false);

                    // Tampilkan checkbox untuk akses yang relevan
                    showAkses();

                    // Tampilkan tombol aksi untuk melanjutkan pengaturan akses
                    aktifButton();

                    JOptionPane.showMessageDialog(this, "Role baru berhasil ditambahkan dengan ID: " + roleId);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan role baru.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menambahkan role baru: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nama role baru tidak boleh kosong.");
        }
    }//GEN-LAST:event_btn_tambahRoleActionPerformed

    private void cmb_roleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_roleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_roleActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        String selectedRole = cmb_role.getSelectedItem() != null ? cmb_role.getSelectedItem().toString() : "";

        if (cmb_role.getSelectedItem() == null || selectedRole.isEmpty() || selectedRole.equals("Pilih Role") || selectedRole.equals("Baru")) {
            JOptionPane.showMessageDialog(this, "Pilih role yang valid untuk dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus role \"" + selectedRole + "\" beserta semua aksesnya?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Dapatkan ID role dari roleMap
                Integer roleId = roleMap.get(selectedRole);
                if (roleId == null) {
                    JOptionPane.showMessageDialog(this, "Role tidak ditemukan. Mungkin role sudah dihapus atau terjadi kesalahan sinkronisasi.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Logger.getLogger(Akses_role.class.getName()).log(Level.INFO, "Menghapus role {0} dengan ID {1}", new Object[]{selectedRole, roleId});

                conn.setAutoCommit(false); // Mulai transaksi

                // Hapus semua akses terkait role dari tabel role_access
                String deleteAccessSql = "DELETE FROM role_access WHERE role_id = ?";
                PreparedStatement deleteAccessStmt = conn.prepareStatement(deleteAccessSql);
                deleteAccessStmt.setInt(1, roleId);
                deleteAccessStmt.executeUpdate();
                deleteAccessStmt.close();

                // Hapus role dari tabel role
                String deleteRoleSql = "DELETE FROM role WHERE id_role = ?";
                PreparedStatement deleteRoleStmt = conn.prepareStatement(deleteRoleSql);
                deleteRoleStmt.setInt(1, roleId);
                deleteRoleStmt.executeUpdate();
                deleteRoleStmt.close();

                conn.commit(); // Komit transaksi
                JOptionPane.showMessageDialog(this, "Role \"" + selectedRole + "\" dan semua akses terkait berhasil dihapus.");

                // Perbarui GUI
                roleMap.remove(selectedRole);
                cmb_role.removeItem(selectedRole);
                txt_id.setText("");
                lbl_roleBaru.setVisible(false);
                txt_roleBaru.setVisible(false);
                btn_tambahRole.setVisible(false);
                getData();
            } catch (SQLException e) {
                try {
                    conn.rollback(); // Batalkan transaksi jika terjadi error
                } catch (SQLException rollbackEx) {
                    Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, rollbackEx);
                }
                Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus role: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.setAutoCommit(true); // Kembali ke mode auto-commit
                } catch (SQLException ex) {
                    Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        String keyword = txt_cari.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan kata kunci untuk pencarian.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tabel_data.getModel();
        model.setRowCount(0); // Hapus data lama di tabel

        try {
            String sql = "SELECT role.id_role, role.role_name, access.access_name "
                    + "FROM role "
                    + "LEFT JOIN role_access ON role.id_role = role_access.role_id "
                    + "LEFT JOIN access ON role_access.access_id = access.access_id "
                    + "WHERE role.role_name LIKE ? OR access.access_name LIKE ?";

            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%"); // Gunakan wildcard untuk pencarian
            st.setString(2, "%" + keyword + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_role"),
                    rs.getString("role_name"),
                    rs.getString("access_name")
                };
                model.addRow(row);
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ditemukan hasil pencarian untuk: " + keyword, "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat melakukan pencarian: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_cariActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        String selectedRole = cmb_role.getSelectedItem() != null ? cmb_role.getSelectedItem().toString() : "";

        try {
            if (selectedRole.equals("Pilih Role") || selectedRole.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih role yang valid untuk ditambahkan aksesnya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (selectedRole.equals("Baru")) {
                // Tampilkan input untuk role baru
                lbl_roleBaru.setVisible(true);
                txt_roleBaru.setVisible(true);
                txt_id.setText(""); // Kosongkan ID karena role baru belum ada
                btn_tambahRole.setVisible(true);

                nonAktifButton();
            } else {
                Integer roleId = roleMap.get(selectedRole);
                if (roleId != null) {
                    for (Map.Entry<String, Boolean> entry : accessStatusMap.entrySet()) {
                        String accessName = entry.getKey();
                        boolean isEnabled = entry.getValue();
                        int accessId = getExistingAccessId(accessName);

                        if (isEnabled) {
                            if (!isAccessAlreadyAssigned(roleId, accessId)) {
                                String insertSql = "INSERT INTO role_access (role_id, access_id) VALUES (?, ?)";
                                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                                insertStmt.setInt(1, roleId);
                                insertStmt.setInt(2, accessId);
                                insertStmt.executeUpdate();
                                insertStmt.close();
                            }
                        } else {
                            if (isAccessAlreadyAssigned(roleId, accessId)) {
                                String deleteSql = "DELETE FROM role_access WHERE role_id = ? AND access_id = ?";
                                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                                deleteStmt.setInt(1, roleId);
                                deleteStmt.setInt(2, accessId);
                                deleteStmt.executeUpdate();
                                deleteStmt.close();
                            }
                        }
                    }

                    JOptionPane.showMessageDialog(this, "Akses berhasil diperbarui untuk role: " + selectedRole);
                    loadRoleAccess(roleId); // Muat ulang akses untuk role
                    getData(); // Perbarui tabel data
                } else {
                    JOptionPane.showMessageDialog(this, "Role tidak ditemukan di database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            Logger.getLogger(Akses_role.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_tambahActionPerformed

    private void txt_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idActionPerformed

    private void tog_PelaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tog_PelaporanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tog_PelaporanActionPerformed

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
            java.util.logging.Logger.getLogger(Akses_role.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Akses_role.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Akses_role.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Akses_role.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Akses_role().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AksesRole;
    private javax.swing.JLabel DaftarAkun;
    private javax.swing.JLabel Disposisi;
    private javax.swing.JLabel FormSuratKeluar;
    private javax.swing.JLabel FormSuratMasuk;
    private javax.swing.JLabel HistorySuratKeluar;
    private javax.swing.JLabel HistorySuratMasuk;
    private javax.swing.JLabel Pelaporan;
    private javax.swing.JLabel Status1;
    private javax.swing.JLabel Status2;
    private javax.swing.JLabel Status3;
    private javax.swing.JLabel Status4;
    private javax.swing.JLabel Status5;
    private javax.swing.JLabel Status6;
    private javax.swing.JLabel Status7;
    private javax.swing.JLabel Status8;
    private javax.swing.JLabel Status9;
    private javax.swing.JPanel User_access;
    private javax.swing.JLabel Validasi;
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_tambahRole;
    private javax.swing.JComboBox<String> cmb_role;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_AksesRole;
    private javax.swing.JLabel lbl_DaftarAkun;
    private javax.swing.JLabel lbl_Disposisi;
    private javax.swing.JLabel lbl_HistorySuratKeluar;
    private javax.swing.JLabel lbl_HistorySuratMasuk;
    private javax.swing.JLabel lbl_Pelaporan;
    private javax.swing.JLabel lbl_SuratKeluar;
    private javax.swing.JLabel lbl_SuratMasuk;
    private javax.swing.JLabel lbl_Validasi;
    private javax.swing.JLabel lbl_akses;
    private javax.swing.JLabel lbl_roleBaru;
    private javax.swing.JTable tabel_data;
    private javax.swing.JToggleButton tog_AksesRole;
    private javax.swing.JToggleButton tog_DaftarAkun;
    private javax.swing.JToggleButton tog_Disposisi;
    private javax.swing.JToggleButton tog_HistorySuratKeluar;
    private javax.swing.JToggleButton tog_HistorySuratMasuk;
    private javax.swing.JToggleButton tog_Pelaporan;
    private javax.swing.JToggleButton tog_SuratKeluar;
    private javax.swing.JToggleButton tog_SuratMasuk;
    private javax.swing.JToggleButton tog_Validasi;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_roleBaru;
    // End of variables declaration//GEN-END:variables

    private void tampilan() {
        lbl_roleBaru.setVisible(false);
        txt_roleBaru.setVisible(false);
        btn_tambahRole.setVisible(false);
    }

}
