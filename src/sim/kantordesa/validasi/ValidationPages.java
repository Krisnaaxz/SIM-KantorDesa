package sim.kantordesa.validasi;

import com.formdev.flatlaf.FlatLightLaf;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JPanel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import sim.kantordesa.config.User;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.dashboard.Dashboard;

/**
 *
 * @author rika
 */
public class ValidationPages extends javax.swing.JFrame {

    Object[] tableContent = new Object[10];
    private final javax.swing.table.DefaultTableModel model;
    private User currentUser;

    /**
     * Creates new form sekdes
     * @param currentUser
     */
    
    public ValidationPages(User currentUser) {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        
        this.currentUser = currentUser;

        model = new javax.swing.table.DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        jTable1.setModel(model);

        // "No", "Status", "Diterima tgl.", "Nama Pemohon", "Perihal", "Val. Sekdes",
        // "Val. Kades", "Aksi"
        model.addColumn("No.");
        model.addColumn("Nomor Surat");
        model.addColumn("Nama Pemohon");
        model.addColumn("Tanggal Pengajuan");
        model.addColumn("Perihal");
        model.addColumn("Status Validasi Sekdes");
        model.addColumn("Status Validasi Kades");
        model.addColumn("Aksi");
        model.addColumn("mail_comment");
        model.addColumn("mail_id");

        loadData();

        TableColumnModel columnModel = jTable1.getColumnModel();
        TableColumn mailCommentCol = columnModel.getColumn(jTable1.convertColumnIndexToView(8));
        TableColumn mailIdCol = columnModel.getColumn(jTable1.convertColumnIndexToView(9));

        columnModel.removeColumn(mailCommentCol);
        columnModel.removeColumn(mailIdCol);

    }

    public JPanel getContentPanel() {
        return (JPanel) this.getContentPane();
    }

    public void loadData() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            Connection c = koneksi.getConnection();
            Statement s = c.createStatement();
            String sql = "select mail_id, mail_number, applicant_name, created_at, status_validation, status_lead, mail_comment, mail_type.type_name from mail_content inner join mail_type on mail_content.mail_type_id = mail_type.mail_type_id ORDER BY mail_id";
            ResultSet r = s.executeQuery(sql);
            int i = 1;
            while (r.next()) {
                tableContent[0] = i++;
                tableContent[1] = r.getString("mail_number");
                tableContent[2] = r.getString("applicant_name");
                tableContent[3] = r.getString("created_at");
                tableContent[4] = r.getString("type_name");
                tableContent[5] = r.getInt("status_validation") == 0 ? "Reject" : r.getInt("status_validation") == 1 ? "Accept" : "Baru";
                tableContent[6] = r.getInt("status_lead") == 0 ? "Reject" : r.getInt("status_lead") == 1 ? "Accept" : "Baru";
                tableContent[7] = "Periksa";
                tableContent[8] = r.getString("mail_comment");
                tableContent[9] = r.getString("mail_id");
                
                if (currentUser.getIdRole() == 1 && (r.getInt("status_validation") == 1 && r.getInt("status_lead") == 2)) {
                    model.addRow(tableContent);
                } else if (currentUser.getIdRole() == 2 && (r.getInt("status_validation") == 2 && r.getInt("status_lead") == 2)) {
                    model.addRow(tableContent);
                } else if (currentUser.getIdRole() == 0) {
                    model.addRow(tableContent);
                }
                
                
 
            }
            r.close();
            s.close();
            jTable1.getColumn("Status Validasi Sekdes").setCellRenderer(new StatusCellRenderer());
            jTable1.getColumn("Status Validasi Kades").setCellRenderer(new StatusCellRenderer());
            jTable1.getColumn("Aksi").setCellRenderer(new ButtonRenderer());
            jTable1.getColumn("Aksi").setCellEditor(new ButtonEditor(jTable1, currentUser));

        } catch (SQLException e) {
            System.out.println("Error, " + e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        historybtn = new javax.swing.JButton();
        refresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1291, 634));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Status", "Diterima tgl.", "Nama Pemohon", "Perihal", "Val. Sekdes", "Val. Kades", "Aksi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setRowHeight(30);
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Validasi Surat Masuk");

        historybtn.setBackground(new java.awt.Color(19, 128, 97));
        historybtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        historybtn.setForeground(new java.awt.Color(255, 255, 255));
        historybtn.setText("History");
        historybtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybtnActionPerformed(evt);
            }
        });

        refresh.setBackground(new java.awt.Color(19, 128, 97));
        refresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        refresh.setForeground(new java.awt.Color(255, 255, 255));
        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 972, Short.MAX_VALUE)
                .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(historybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1016, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(historybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(586, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(45, 45, 45)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshActionPerformed
        // TODO add your handling code here:
        loadData();
    }// GEN-LAST:event_refreshActionPerformed

    private void historybtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_historybtnActionPerformed
        // TODO add your handling code here:
        Dashboard.switchPanel("History Surat Keluar");
    }// GEN-LAST:event_historybtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ValidationPages().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton historybtn;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton refresh;
    // End of variables declaration//GEN-END:variables
}
