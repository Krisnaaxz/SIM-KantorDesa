package sim.kantordesa.validasi;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import sim.kantordesa.config.koneksi;
import java.awt.HeadlessException;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import sim.kantordesa.mailtemplate.MailData;
import sim.kantordesa.mailtemplate.edit_mailform;
import sim.kantordesa.mailtemplate.mailform;

/**
 *
 * @author krisna
 */
public class HistoryPage extends javax.swing.JFrame {

    private javax.swing.table.DefaultTableModel model;
    Connection c = koneksi.getConnection();

    public HistoryPage() {
        initComponents();

        model = new javax.swing.table.DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };

        tbHistory.setModel(model); //create table 
        tbHistory.setShowGrid(true);
        tbHistory.setGridColor(Color.lightGray);
        //create table column and column header
        model.addColumn("No.");
        model.addColumn("Nomor Surat");
        model.addColumn("Nama Pemohon");
        model.addColumn("Tanggal Pengajuan");
        model.addColumn("Perihal");
        model.addColumn("Status Validasi Sekdes");
        model.addColumn("Status Validasi Kades");
        model.addColumn("Comment");
        model.addColumn("Aksi");

        setTableAction(); //call function setTableAction()
        adjustColumnWidths(tbHistory); //call function adjustColumnWidths()

        // Set the preferred width for the "Comment" column 
        TableColumn commentColumn = tbHistory.getColumnModel().getColumn(7);
        commentColumn.setPreferredWidth(250);
        commentColumn.setMaxWidth(250);
        commentColumn.setMinWidth(250);

        // Set the preferred width for the "No." column 
        TableColumn noColumn = tbHistory.getColumnModel().getColumn(0);
        noColumn.setMaxWidth(50);
        noColumn.setMinWidth(50);

        // Set the custom cell renderer for the "Comment" column
        commentColumn.setCellRenderer(new TextAreaRenderer());
    }

    public JPanel getContentPanel() {
        return (JPanel) this.getContentPane();
    }

    public void setTableAction() { //function query get data from db
        model.getDataVector().removeAllElements();//remove value on table
        model.fireTableDataChanged();

        try {
            Statement s = c.createStatement();
            String sql = "select mail_id, mail_number, created_at, applicant_name, mail_comment, status_validation, status_lead, mail_comment, mail_type.type_name from mail_content inner join mail_type on mail_content.mail_type_id = mail_type.mail_type_id ORDER BY mail_id";
            ResultSet r = s.executeQuery(sql);
            int i = 1;
            while (r.next()) {

                model.addRow(new Object[]{//crate row on table and get data from db
                    i++,
                    r.getString("mail_number"),
                    r.getString("applicant_name"),
                    r.getString("created_at"),
                    r.getString("type_name"),
                    r.getInt("status_validation") == 0 ? "Reject" : r.getInt("status_validation") == 1 ? "Accept" : "Baru",
                    r.getInt("status_lead") == 0 ? "Reject" : r.getInt("status_lead") == 1 ? "Accept" : "Baru",
                    r.getString("mail_comment"),
                    r.getString("mail_id"),});

            }
            r.close();
            s.close();
        } catch (SQLException e) {
            System.out.println("Error, " + e);
        }
        //set style on status value 
        tbHistory.getColumn("Status Validasi Sekdes").setCellRenderer(new StatusCellRenderer());
        tbHistory.getColumn("Status Validasi Kades").setCellRenderer(new StatusCellRenderer());
        //rendering action button
        tbHistory.getColumn("Aksi").setCellRenderer(new ButtonPanelRenderer());
        tbHistory.getColumn("Aksi").setCellEditor(new ButtonPanelEditor(tbHistory));
    }

    // Custom cell renderer for text wrapping
    public class TextAreaRenderer extends JTextArea implements TableCellRenderer {

        private static final int FIXED_WIDTH = 200; // Fixed width in pixels

        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            setSize(FIXED_WIDTH, Short.MAX_VALUE);

            // Calculate the preferred height based on the fixed width
            int preferredHeight = getPreferredSize().height;

            // Set minimum height to avoid very small rows
            preferredHeight = Math.max(preferredHeight, 40);

            // Update the row height if necessary
            if (table.getRowHeight(row) != preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }

            //adding border
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray), // Border hitam
                    BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding
            ));

            // Handle selection highlighting
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            return this;
        }

        // Override getPreferredSize to maintain fixed width
        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = FIXED_WIDTH;
            return d;
        }
    }

    public static void adjustColumnWidths(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = getMaxPreferredWidth(table, column);
            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    private static int getMaxPreferredWidth(JTable table, int column) { //width adjusting with comparing max widht on header and value
        int maxWidth = 0;
        TableColumn tableColumn = table.getColumnModel().getColumn(column);

        // Get the width of the column header
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        Component comp = headerRenderer.getTableCellRendererComponent(table, tableColumn.getHeaderValue(), false, false, 0, column);
        maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);

        // Get the width of the column content
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
            comp = cellRenderer.getTableCellRendererComponent(table, table.getValueAt(row, column), false, false, row, column);
            maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
        }

        return maxWidth + 10; // Add some margin
    }

    class ButtonPanelRenderer extends ButtonPanel implements TableCellRenderer {

        public ButtonPanelRenderer() { //action button style
            setBackground(Color.white);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            //checking condition where accepted mail will rendering download button
            boolean statusValidation = "Accept".equals(table.getValueAt(row, 5));
            boolean statusLead = "Accept".equals(table.getValueAt(row, 6));
            Object mailComment = table.getValueAt(row, 7);
            boolean hasMailComment = (mailComment != null && !mailComment.toString().isEmpty() && statusValidation && statusLead);

            downloadButton.setVisible(hasMailComment);
            editButton.setVisible(!hasMailComment);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 1, 1, 0, Color.lightGray), // Border hitam
                    BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding
            ));

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }

    }

    class ButtonPanelEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        ButtonPanel panel;
        JTable table;

//        public ButtonPanelEditor(JButton editButton, JButton deleteButton, JButton downloadButton) {
        public ButtonPanelEditor(JTable table) { //constructor 
            this.table = table;
            panel = new ButtonPanel();

            panel.editButton.addActionListener(e -> handleEditButtonAction());
            panel.deleteButton.addActionListener(e -> handleDeleteButtonAction());
            panel.downloadButton.addActionListener(e -> handleDownloadButtonAction());

        }

        private void handleEditButtonAction() {
            System.out.println("Edit Button diklik");
            int row = table.getSelectedRow();
            if (row != -1) {
                String mailId = (String) table.getValueAt(row, 8);
                edit_mailform editForm = new edit_mailform(Integer.parseInt(mailId));
                editForm.setVisible(true);
            }
        }

        private void handleDeleteButtonAction() {
            System.out.println("Delete Button diklik");
            int row = table.getSelectedRow();

            //get mailID on selected row
            String mailId = (String) table.getValueAt(row, 8);
            //option panel to confirm delete
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Apakah anda yakin ingin menghapus pengajuan surat ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM mail_content WHERE mail_id = ?";
                try {
                    boolean hasil = koneksi.delete(query, mailId);
                    if (hasil) {
                        setTableAction();
                        JOptionPane.showMessageDialog(
                                null,
                                "Data Pengajuan Berhasil Dihapus");
                    }
                } catch (HeadlessException e) {
                    System.out.println("Error, " + e);
                }
            }
        }

        private void handleDownloadButtonAction() {
            System.out.println("Download Button diklik");
            mailform mf = new mailform();

            int row = table.getSelectedRow();
            String mailTypeString = (String) table.getValueAt(row, 4);
            String mailId = (String) table.getValueAt(row, 8);
            Integer mailTypeId = null;
            
            System.out.println(mailId);

            try {
                Connection conn = koneksi.getConnection();

                String getMailTypeIdQuery = "SELECT mail_type_id FROM `mail_type` where type_name = \"" + mailTypeString + "\";";
                Statement s = conn.createStatement();
                ResultSet rs1 = s.executeQuery(getMailTypeIdQuery);
                while (rs1.next()) {
                    mailTypeId = rs1.getInt("mail_type_id");
                }


                String getMailDataQuery = "SELECT c.applicant_name, c.mulai_berlaku, c.tgl_akhir, c.mail_number, c.keperluan, cr.tempat_tanggal_lahir, cr.usia, cr.warga_negara, cr.agama, cr.jenis_kelamin, cr.pekerjaan, cr.alamat, cr.no_ktp, cr.no_kk, cr.gol_darah, t.mail_type_id  FROM mail_content as c INNER JOIN civil_registry cr ON c.no_ktp = cr.no_ktp INNER JOIN mail_type t ON c.mail_type_id = t.mail_type_id WHERE mail_id = \"" + mailId + "\";";
                ResultSet rs2 = s.executeQuery(getMailDataQuery);

                while (rs2.next()) {
//                    System.out.println("hello world");
//                    System.out.println("nama pemohon : " + rs2.getString("applicant_name"));
                    MailData.put("nama", rs2.getString("applicant_name"));
                    MailData.put("ttl", rs2.getString("tempat_tanggal_lahir"));
                    MailData.put("umur", rs2.getString("usia"));
                    MailData.put("warga_negara", rs2.getString("warga_negara"));
                    MailData.put("agama", rs2.getString("agama"));
                    MailData.put("sex", rs2.getString("jenis_kelamin"));
                    MailData.put("pekerjaan", rs2.getString("pekerjaan"));
                    MailData.put("alamat", rs2.getString("alamat"));
                    MailData.put("no_ktp", rs2.getString("no_ktp"));
                    MailData.put("no_kk", rs2.getString("no_kk"));
                    MailData.put("keperluan", rs2.getString("keperluan"));
                    MailData.put("mulai_berlaku", rs2.getString("mulai_berlaku"));
                    MailData.put("tgl_akhir", rs2.getString("tgl_akhir"));
                    MailData.put("gol_darah", rs2.getString("gol_darah"));
                    MailData.put("mail_number", rs2.getString("mail_number"));
                }
                
                System.out.println(mailTypeId + " 2");

                mf.generatePDF(mailTypeId, conn, mailTypeString, true);
                
                rs1.close();
                rs2.close();
                s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override //make can't edit table value
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            boolean statusValidation = "Accept".equals(table.getValueAt(row, 5));
            boolean statusLead = "Accept".equals(table.getValueAt(row, 6));
            Object mailComment = table.getValueAt(row, 7);

            boolean hasMailComment = (mailComment != null && !mailComment.toString().isEmpty() && statusValidation && statusLead);
            panel.downloadButton.setVisible(hasMailComment);
            panel.editButton.setVisible(!hasMailComment);

            return panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    static class ButtonPanel extends javax.swing.JPanel {

        public javax.swing.JButton editButton;
        public javax.swing.JButton deleteButton;
        public javax.swing.JButton downloadButton;

        public ButtonPanel() {
            // Use GridBagLayout for better centering control
            setLayout(new GridBagLayout());
            // Use BoxLayout for vertical alignment
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            // Create inner panel for buttons with FlowLayout
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            buttonPanel.setOpaque(false);

            // Create buttons
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            downloadButton = new JButton("Download");

            // Add buttons to inner panel
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(downloadButton);

            // Add vertical glue for centering
            add(Box.createVerticalGlue());
            // Add button panel
            add(buttonPanel);
            // Add vertical glue for centering
            add(Box.createVerticalGlue());

        }
    }

    //filtering history 
    private void filter(String query) {
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        tbHistory.setRowSorter(tr);

        RowFilter<DefaultTableModel, Object> filter = null; //no filter

        if ("Diproses".equals(query)) {//filter by surat diproses
            filter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String statusSekdes = (String) entry.getValue(5);
                    String statusKades = (String) entry.getValue(6);

                    return ("Baru".equals(statusSekdes) || "Accept".equals(statusSekdes)) && "Baru".equals(statusKades);
                }
            };
        } else if ("Ditolak".equals(query)) {//filter by surat ditolak
            filter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String statusSekdes = (String) entry.getValue(5);
                    String statusKades = (String) entry.getValue(6);

                    return "Reject".equals(statusSekdes) || "Reject".equals(statusKades);
                }
            };
        } else if ("Selesai".equals(query)) {//filter by surat selesai
            filter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String statusSekdes = (String) entry.getValue(5);
                    String statusKades = (String) entry.getValue(6);
                    String mailComment = (String) entry.getValue(7);

                    return "Accept".equals(statusSekdes) && "Accept".equals(statusKades)
                            && (mailComment != null && !mailComment.isEmpty());
                }
            };
        } else if ("Semua".equals(query)) {
            tr.setRowFilter(null); // no filter
            return;
        }

        if (filter != null) {
            tr.setRowFilter(filter);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTb = new javax.swing.JPanel();
        panelScrollTb = new javax.swing.JScrollPane();
        tbHistory = new javax.swing.JTable();
        labelHistory = new javax.swing.JLabel();
        refresh = new javax.swing.JButton();
        filterBox = new javax.swing.JComboBox<>();
        filterLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1291, 634));

        panelTb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tbHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "No. Surat", "Tanggal Surat Masuk", "Status Validasi Sekdes", "Status Validasi Kades", "Perihal", "Aksi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbHistory.setRowHeight(30);
        tbHistory.setShowGrid(true);
        panelScrollTb.setViewportView(tbHistory);
        if (tbHistory.getColumnModel().getColumnCount() > 0) {
            tbHistory.getColumnModel().getColumn(0).setMinWidth(30);
            tbHistory.getColumnModel().getColumn(0).setMaxWidth(30);
            tbHistory.getColumnModel().getColumn(1).setMinWidth(120);
            tbHistory.getColumnModel().getColumn(1).setMaxWidth(130);
            tbHistory.getColumnModel().getColumn(2).setMinWidth(140);
            tbHistory.getColumnModel().getColumn(2).setMaxWidth(150);
            tbHistory.getColumnModel().getColumn(3).setMinWidth(140);
            tbHistory.getColumnModel().getColumn(3).setMaxWidth(150);
            tbHistory.getColumnModel().getColumn(4).setMinWidth(140);
            tbHistory.getColumnModel().getColumn(4).setMaxWidth(150);
            tbHistory.getColumnModel().getColumn(5).setMinWidth(120);
            tbHistory.getColumnModel().getColumn(5).setMaxWidth(130);
        }

        labelHistory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelHistory.setText("History Surat Keluar");

        refresh.setBackground(new java.awt.Color(19, 128, 97));
        refresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        refresh.setForeground(new java.awt.Color(255, 255, 255));
        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        filterBox.setBackground(new java.awt.Color(19, 128, 97));
        filterBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Diproses", "Ditolak", "Selesai" }));
        filterBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                filterBoxItemStateChanged(evt);
            }
        });
        filterBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterBoxActionPerformed(evt);
            }
        });

        filterLabel.setText("Filter:");

        javax.swing.GroupLayout panelTbLayout = new javax.swing.GroupLayout(panelTb);
        panelTb.setLayout(panelTbLayout);
        panelTbLayout.setHorizontalGroup(
            panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelScrollTb, javax.swing.GroupLayout.DEFAULT_SIZE, 1265, Short.MAX_VALUE)
                    .addGroup(panelTbLayout.createSequentialGroup()
                        .addComponent(labelHistory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(filterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelTbLayout.setVerticalGroup(
            panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelHistory)
                    .addComponent(filterBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelScrollTb, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(panelTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void filterBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterBoxActionPerformed

    private void filterBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterBoxItemStateChanged
        String query = filterBox.getSelectedItem().toString();

        filter(query);
    }//GEN-LAST:event_filterBoxItemStateChanged

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        // TODO add your handling code here:
        setTableAction();
    }//GEN-LAST:event_refreshActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HistoryPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> filterBox;
    private javax.swing.JLabel filterLabel;
    private javax.swing.JLabel labelHistory;
    private javax.swing.JScrollPane panelScrollTb;
    private javax.swing.JPanel panelTb;
    private javax.swing.JButton refresh;
    private javax.swing.JTable tbHistory;
    // End of variables declaration//GEN-END:variables
}
