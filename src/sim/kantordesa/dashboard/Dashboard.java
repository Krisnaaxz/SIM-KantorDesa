package sim.kantordesa.dashboard;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sim.kantordesa.config.User;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.master.Create_acc;
import sim.kantordesa.auth.login;
import sim.kantordesa.config.AppContext;
import sim.kantordesa.mailtemplate.mailform;
import sim.kantordesa.mailtemplate.templateselector;
import sim.kantordesa.master.Akses_role;
import sim.kantordesa.modulPengiriman.detailSurat;
import sim.kantordesa.modulPengiriman.historySuratMasuk;
import sim.kantordesa.modulPengiriman.registrasiNaskah;
import sim.kantordesa.modulPengiriman.suratMasukDisposisi;
import sim.kantordesa.validasi.HistoryPage;
import sim.kantordesa.validasi.PelaporanSuratPages;
import sim.kantordesa.validasi.ValidationPages;

public class Dashboard extends javax.swing.JFrame {

    private final Connection conn;
    private User currentUser;
    private Set<String> userAccess;
    private final int[][] buttonLocation;
    public static JPanel card;
    private static Map<String, Object> pageMap = new HashMap<>();

    public Dashboard(User currentUser, Set<String> userAccess) {
        this.currentUser = currentUser;
        this.userAccess = userAccess;

        FlatLightLaf.setup();

        initComponents();
        setExtendedState(MAXIMIZED_BOTH);

        card = this.Card;

        this.buttonLocation = new int[][]{
            {Beranda.getX(), Beranda.getY()},
            {FormSuratMasuk.getX(), FormSuratMasuk.getY()},
            {FormSuratKeluar.getX(), FormSuratKeluar.getY()},
            {HistorySuratMasuk.getX(), HistorySuratMasuk.getY()},
            {HistorySuratKeluar.getX(), HistorySuratKeluar.getY()},
            {Disposisi.getX(), Disposisi.getY()},
            {Validasi.getX(), Validasi.getY()},
            {Pelaporan.getX(), Pelaporan.getY()},
            {DaftarAkun.getX(), DaftarAkun.getY()},
            {AksesRole.getX(), AksesRole.getY()}
        };

        conn = koneksi.getConnection();

        NamaUser.setText(currentUser.getFullName());

        Role.setText(currentUser.getRole());
        AppContext.put("idRole", currentUser.getIdRole());
        
        String mailform_templateName = (String) AppContext.get("mailform_templateName");
        Integer mailform_mailTypeId = (Integer) AppContext.get("mailform_mailTypeId");
        String historymasuk_mailrcvid = (String) AppContext.get("historymasuk_mailRcvId");

        Dashboard.addPage(new Beranda(), "Beranda");
        Dashboard.addPage(new Akses_role(), "Akses Role");
        Dashboard.addPage(new Create_acc(), "Daftar Akun");
        Dashboard.addPage(new registrasiNaskah(), "Form Surat Masuk");
        Dashboard.addPage(new historySuratMasuk(), "History Surat Masuk");
        Dashboard.addPage(new HistoryPage(), "History Surat Keluar");
        Dashboard.addPage(new mailform(mailform_templateName != null ? mailform_templateName : "MAIL FORM", mailform_mailTypeId != null ? mailform_mailTypeId : 0), "Form Surat Keluar");
        Dashboard.addPage(new suratMasukDisposisi(), "Disposisi");
        Dashboard.addPage(new ValidationPages(currentUser), "Validasi");
        Dashboard.addPage(new PelaporanSuratPages(), "Pelaporan");
        Dashboard.addPage(new templateselector(), "Template Selector");
        Dashboard.addPage(new detailSurat(historymasuk_mailrcvid != null ? historymasuk_mailrcvid : ""), "Detail Surat");

        switchPanel("Beranda");

        Sidebar.removeAll();
        Sidebar.add(Beranda);
        Sidebar.add(FormSuratMasuk);
        Sidebar.add(FormSuratKeluar);
        Sidebar.add(HistorySuratMasuk);
        Sidebar.add(HistorySuratKeluar);
        Sidebar.add(Disposisi);
        Sidebar.add(Validasi);
        Sidebar.add(Pelaporan);
        Sidebar.add(DaftarAkun);
        Sidebar.add(AksesRole);
        Sidebar.add(Keluar);

        setSidebarVisibility(userAccess);

    }

    public static void switchPanel(String cardName) {
        CardLayout layout = (CardLayout) card.getLayout();
        card.revalidate();
        card.repaint();
        layout.show(card, cardName);
    }
    
    public static void addPage(Object page, String key) {
        if (page instanceof JPanel jPanel) {
            pageMap.put(key, page);
            card.add(jPanel, key);
        } else {
            try {
                JPanel contentPanel = (JPanel) page.getClass().getMethod("getContentPanel").invoke(page);
                pageMap.put(key, page);
                card.add(contentPanel, key);
            } catch (Exception e) {
                throw new IllegalArgumentException("Page must either be a JPanel or have a getContentPanel method.", e);
            }
        }
    }
    
    public static Object getPage(String key) {
        return pageMap.get(key);
    }

    private void setSidebarVisibility(Set<String> userAccess) {

        JLabel[] buttons = {
            Beranda,
            FormSuratMasuk,
            FormSuratKeluar,
            HistorySuratMasuk,
            HistorySuratKeluar,
            Disposisi,
            Validasi,
            Pelaporan,
            DaftarAkun,
            AksesRole,
            Keluar
        };

        int visibleIndex = 1;

        for (JLabel button : buttons) {
            String buttonLabel = button.getText();

            button.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                }
            });
            if (button == Beranda || button == Keluar) {
                continue;
            }

            if (userAccess.contains(buttonLabel)) {
                button.setLocation(button.getX(), buttonLocation[visibleIndex][1]);
                Sidebar.add(button);
                visibleIndex++;
            } else {
                Sidebar.remove(button);
            }
        }

        Sidebar.revalidate();
        Sidebar.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Sidebar = new javax.swing.JPanel();
        Beranda = new javax.swing.JLabel();
        FormSuratMasuk = new javax.swing.JLabel();
        FormSuratKeluar = new javax.swing.JLabel();
        HistorySuratMasuk = new javax.swing.JLabel();
        HistorySuratKeluar = new javax.swing.JLabel();
        Disposisi = new javax.swing.JLabel();
        Validasi = new javax.swing.JLabel();
        DaftarAkun = new javax.swing.JLabel();
        AksesRole = new javax.swing.JLabel();
        Keluar = new javax.swing.JLabel();
        Pelaporan = new javax.swing.JLabel();
        NamaDesa = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Header = new javax.swing.JPanel();
        NamaUser = new javax.swing.JLabel();
        Role = new javax.swing.JLabel();
        Card = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIM-Kantor Desa");

        Sidebar.setBackground(new java.awt.Color(255, 255, 255));
        Sidebar.setPreferredSize(new java.awt.Dimension(200, 640));

        Beranda.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Beranda.setForeground(new java.awt.Color(19, 128, 97));
        Beranda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/home.png"))); // NOI18N
        Beranda.setText("Beranda");
        Beranda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BerandaMouseClicked(evt);
            }
        });

        FormSuratMasuk.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FormSuratMasuk.setForeground(new java.awt.Color(19, 128, 97));
        FormSuratMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/form.png"))); // NOI18N
        FormSuratMasuk.setText("Form Surat Masuk");
        FormSuratMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FormSuratMasukMouseClicked(evt);
            }
        });

        FormSuratKeluar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FormSuratKeluar.setForeground(new java.awt.Color(19, 128, 97));
        FormSuratKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/form.png"))); // NOI18N
        FormSuratKeluar.setText("Form Surat Keluar");
        FormSuratKeluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FormSuratKeluarMouseClicked(evt);
            }
        });

        HistorySuratMasuk.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        HistorySuratMasuk.setForeground(new java.awt.Color(19, 128, 97));
        HistorySuratMasuk.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        HistorySuratMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/history.png"))); // NOI18N
        HistorySuratMasuk.setText("History Surat Masuk");
        HistorySuratMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HistorySuratMasukMouseClicked(evt);
            }
        });

        HistorySuratKeluar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        HistorySuratKeluar.setForeground(new java.awt.Color(19, 128, 97));
        HistorySuratKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/history.png"))); // NOI18N
        HistorySuratKeluar.setText("History Surat Keluar");
        HistorySuratKeluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HistorySuratKeluarMouseClicked(evt);
            }
        });

        Disposisi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Disposisi.setForeground(new java.awt.Color(19, 128, 97));
        Disposisi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/disposisi.png"))); // NOI18N
        Disposisi.setText("Disposisi");
        Disposisi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DisposisiMouseClicked(evt);
            }
        });

        Validasi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Validasi.setForeground(new java.awt.Color(19, 128, 97));
        Validasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/validasi.png"))); // NOI18N
        Validasi.setText("Validasi");
        Validasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ValidasiMouseClicked(evt);
            }
        });

        DaftarAkun.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        DaftarAkun.setForeground(new java.awt.Color(19, 128, 97));
        DaftarAkun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/register.png"))); // NOI18N
        DaftarAkun.setText("Daftar Akun");
        DaftarAkun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DaftarAkunMouseClicked(evt);
            }
        });

        AksesRole.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        AksesRole.setForeground(new java.awt.Color(19, 128, 97));
        AksesRole.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/access.png"))); // NOI18N
        AksesRole.setText("Akses Role");
        AksesRole.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AksesRoleMouseClicked(evt);
            }
        });

        Keluar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Keluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/log_out.png"))); // NOI18N
        Keluar.setText("Keluar");
        Keluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                KeluarMouseClicked(evt);
            }
        });

        Pelaporan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Pelaporan.setForeground(new java.awt.Color(19, 128, 97));
        Pelaporan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/pelaporan.png"))); // NOI18N
        Pelaporan.setText("Pelaporan");
        Pelaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PelaporanMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout SidebarLayout = new javax.swing.GroupLayout(Sidebar);
        Sidebar.setLayout(SidebarLayout);
        SidebarLayout.setHorizontalGroup(
            SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidebarLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Keluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SidebarLayout.createSequentialGroup()
                        .addGroup(SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Beranda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(FormSuratMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(HistorySuratMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(HistorySuratKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(FormSuratKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Disposisi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Validasi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(DaftarAkun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(AksesRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Pelaporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        SidebarLayout.setVerticalGroup(
            SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidebarLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(Beranda)
                .addGap(18, 18, 18)
                .addComponent(FormSuratMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(FormSuratKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(HistorySuratMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(HistorySuratKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(Disposisi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(Validasi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(Pelaporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(DaftarAkun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(AksesRole, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addGap(182, 182, 182)
                .addComponent(Keluar)
                .addGap(17, 17, 17))
        );

        Disposisi.getAccessibleContext().setAccessibleDescription("");

        NamaDesa.setBackground(new java.awt.Color(19, 128, 97));
        NamaDesa.setPreferredSize(new java.awt.Dimension(200, 60));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sistem Informasi Manajemen");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DESA TAMAN");

        javax.swing.GroupLayout NamaDesaLayout = new javax.swing.GroupLayout(NamaDesa);
        NamaDesa.setLayout(NamaDesaLayout);
        NamaDesaLayout.setHorizontalGroup(
            NamaDesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NamaDesaLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(NamaDesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        NamaDesaLayout.setVerticalGroup(
            NamaDesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NamaDesaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        Header.setBackground(new java.awt.Color(19, 128, 97));
        Header.setPreferredSize(new java.awt.Dimension(1300, 60));

        NamaUser.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NamaUser.setForeground(new java.awt.Color(255, 255, 255));
        NamaUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/user.png"))); // NOI18N
        NamaUser.setText("Nama Lengkap User");

        Role.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Role.setForeground(new java.awt.Color(255, 255, 255));
        Role.setText("Role");

        NamaUser.setText("");
        NamaUser.setText(currentUser.getFullName());

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeaderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(NamaUser, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Role, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NamaUser)
                    .addComponent(Role))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        Card.setBackground(new java.awt.Color(255, 255, 255));
        Card.setPreferredSize(new java.awt.Dimension(1300, 640));
        Card.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .addComponent(NamaDesa, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Card, javax.swing.GroupLayout.DEFAULT_SIZE, 1291, Short.MAX_VALUE)
                    .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, 1291, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NamaDesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addComponent(Card, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AksesRoleMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_AksesRoleMouseClicked
        switchPanel("Akses Role"); // Menampilkan panel Akses Role
    }// GEN-LAST:event_AksesRoleMouseClicked

    private void BerandaMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_BerandaMouseClicked
        switchPanel("Beranda"); // Menampilkan panel Beranda
    }// GEN-LAST:event_BerandaMouseClicked

    private void DaftarAkunMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_DaftarAkunMouseClicked
        switchPanel("Daftar Akun");
    }// GEN-LAST:event_DaftarAkunMouseClicked

    private void KeluarMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_KeluarMouseClicked
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            login loginFrame = new login();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }// GEN-LAST:event_KeluarMouseClicked

    private void FormSuratKeluarMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_FormSuratKeluarMouseClicked
        switchPanel("Template Selector");
    }// GEN-LAST:event_FormSuratKeluarMouseClicked

    private void FormSuratMasukMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_FormSuratMasukMouseClicked
        switchPanel("Form Surat Masuk");
    }// GEN-LAST:event_FormSuratMasukMouseClicked

    private void HistorySuratMasukMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_HistorySuratMasukMouseClicked
        switchPanel("History Surat Masuk");
    }// GEN-LAST:event_HistorySuratMasukMouseClicked

    private void HistorySuratKeluarMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_HistorySuratKeluarMouseClicked
        switchPanel("History Surat Keluar");
    }// GEN-LAST:event_HistorySuratKeluarMouseClicked

    private void DisposisiMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_DisposisiMouseClicked
        switchPanel("Disposisi");
    }// GEN-LAST:event_DisposisiMouseClicked

    private void ValidasiMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ValidasiMouseClicked
        switchPanel("Validasi");
    }// GEN-LAST:event_ValidasiMouseClicked

    private void PelaporanMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_PelaporanMouseClicked
        switchPanel("Pelaporan");
    }// GEN-LAST:event_PelaporanMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AksesRole;
    private javax.swing.JLabel Beranda;
    private javax.swing.JPanel Card;
    private javax.swing.JLabel DaftarAkun;
    private javax.swing.JLabel Disposisi;
    private javax.swing.JLabel FormSuratKeluar;
    private javax.swing.JLabel FormSuratMasuk;
    private javax.swing.JPanel Header;
    private javax.swing.JLabel HistorySuratKeluar;
    private javax.swing.JLabel HistorySuratMasuk;
    private javax.swing.JLabel Keluar;
    private javax.swing.JPanel NamaDesa;
    private javax.swing.JLabel NamaUser;
    private javax.swing.JLabel Pelaporan;
    private javax.swing.JLabel Role;
    private javax.swing.JPanel Sidebar;
    private javax.swing.JLabel Validasi;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
