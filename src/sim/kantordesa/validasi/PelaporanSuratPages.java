package sim.kantordesa.validasi;

import com.formdev.flatlaf.FlatLightLaf;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.BorderLayout;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
//import javax.swing.text.Document;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author krisna
 */
public class PelaporanSuratPages extends javax.swing.JFrame { //kelas membuat antarmuka GUI

    private javax.swing.table.DefaultTableModel model; //Model untuk JTable
    Connection c = koneksi.getConnection(); //koneksi ke database

    public PelaporanSuratPages() { //konstraktor
        initComponents(); //GUI
//        showLineChart(dataset); //line chart
        

        model = new javax.swing.table.DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        tbHistory.setModel(model);

        model.addColumn("No."); //menambahkan kolom baru
        model.addColumn("Tipe Surat");
        model.addColumn("Semua");
        model.addColumn("Diterima");
        model.addColumn("Diproses");
        model.addColumn("Ditolak");

//        TableColumnModel columnModel = tbHistory.getColumnModel(); //mengambil model kolom dari jtabel1
//        TableColumn statusKades = columnModel.getColumn(tbHistory.convertColumnIndexToView(6)); //mengambil data dari kolom dengan index ke 8 (mail comment)
//        TableColumn statusSekdes = columnModel.getColumn(tbHistory.convertColumnIndexToView(7));
//        
//        columnModel.removeColumn(statusKades); //tidak ditampilkan untuk pengguna di hlmn validasi
//        columnModel.removeColumn(statusSekdes); //tidak ditampilkan untuk pengguna di hlmn validasi
        // Tambahkan dropdown untuk filter
//        cbFilterSurat = new JComboBox<>(new String[]{"Semua", "Diproses", "Ditolak", "Selesai"});
//        cbFilterSurat.addActionListener(e -> filter(cbFilterSurat.getSelectedItem().toString()));
        setTableAction(); //memanggil method
        adjustColumnWidths(tbHistory);
        
        createPieChart("Semua"); //pie chart
        showLineChart("Semua");

    }

    private JFreeChart showLineChart(String query) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); //membuat dataset untuk line chart

        try {
            Statement s = c.createStatement(); //membuat objek statement untuk menjalankan oerintah di database dengan koneksi c
            String sql = "SELECT mail_id, DATE_FORMAT(created_at, '%Y-%m') AS bulan, c.status_validation, c.status_lead, COUNT(*) AS jumlah_surat FROM mail_content AS c";
            
            String sqlDiproses = sql + " WHERE (c.status_validation = 1 AND c.status_lead = 2) OR (c.status_validation = 2 AND c.status_lead = 2) GROUP BY DATE_FORMAT(created_at, '%Y-%m') ORDER BY bulan ";
            String sqlDitolak = sql + " WHERE (c.status_validation = 0 AND c.status_lead = 0) "
                    + "OR (c.status_validation = 0 AND c.status_lead = 2) "
                    + "OR (c.status_validation = 1 AND c.status_lead = 0) "
                    + "GROUP BY DATE_FORMAT(created_at, '%Y-%m') ORDER BY bulan;";
            String sqlDiterima = sql + " WHERE c.status_validation = 1 AND c.status_lead = 1 GROUP BY DATE_FORMAT(created_at, '%Y-%m') ORDER BY bulan; ";
            
            String selectedSql;

            if ("Diproses".equals(query)) {
                selectedSql = sqlDiproses;
            } else if ("Ditolak".equals(query)) {
                selectedSql = sqlDitolak;
            } else if ("Diterima".equals(query)) {
                selectedSql = sqlDiterima;
            } else {
                selectedSql = sql + " GROUP BY DATE_FORMAT(created_at, '%Y-%m') ORDER BY bulan;";
            }
//       
//            if ("Diproses".equals(query)) {
//                sql += " WHERE (c.status_validation = 1 AND c.status_lead = 2) OR (c.status_validation = 2 AND c.status_lead = 2)";
//            } else if ("Ditolak".equals(query)) {
//                sql += " WHERE (c.status_validation = 0 AND c.status_lead = 0) "
//                        + "OR (c.status_validation = 0 AND c.status_lead = 2) "
//                        + "OR (c.status_validation = 1 AND c.status_lead = 0) ";
//            } else if ("Diterima".equals(query)) {
//                sql += " WHERE c.status_validation = 1 AND c.status_lead = 1 ";
//            }
            ResultSet r = s.executeQuery(selectedSql); //menjalankan query dan menyimpannya di resultset

            while (r.next()) { //mengambil hasil query sql per baris 
                String bulan = r.getString("bulan"); //mengambil data bulan
                int jumlahSurat = r.getInt("jumlah_surat"); //mengambil data jumlah surat
                System.out.println(jumlahSurat + " " + bulan + " " + r.getString("mail_id") );
                dataset.addValue(jumlahSurat, "Jumlah Surat", bulan); //nilai, label bari, label kolom
            }

            r.close();
            s.close();
        } catch (SQLException e) {
            System.out.println("Error, " + e); //menampilkan pesan eror
        }

        System.out.println("Dataset size: " + dataset.getRowCount()); //menampilkan jumlah baris data yang ada di dataset
        JFreeChart lineChart = ChartFactory.createLineChart( //membuat line chart
                "Surat Masuk per Bulan", //judul
                "Bulan", //judul sumbu x
                "Jumlah Surat", //judul sumbu y
                dataset //dataset yang ditampilkan pada line chart
        );

        CategoryPlot plot = lineChart.getCategoryPlot(); //mengatur tampilan grafik
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(); //mendapatkan sumbu Y 
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); //mengubah sumbu Y menjadi integer atau bilangan bulat

        ChartPanel chartPanel = new ChartPanel(lineChart); //membuat gui panel tempat line chart akan ditampilkan
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600)); //dimensi panel 
        chartPanel1.removeAll(); //menghapus semua komponen yang ada pada panel tempat line chart akan ditambahkan
        chartPanel1.setLayout(new BorderLayout()); //mengaturnya menjadi border layout
        chartPanel1.add(chartPanel, BorderLayout.CENTER); //menambahkan chart panel ke chart panel 1 dengan border layout centered
        chartPanel1.revalidate(); //memastikan tata letak diperbarui 
        chartPanel1.repaint(); //mengambar ulang agar yg terbaru

        return lineChart; //mengembalikan line chart
    }

    private JFreeChart createPieChart(String query) {
        DefaultPieDataset dataset = new DefaultPieDataset(); //membat dataset untuk pie chart
//        ResultSet r;
        Integer rowCount = tbHistory.getRowCount();
        TableModel datamodel = tbHistory.getModel();

        // Mengambil data dari database
//        try {
//            Statement s = c.createStatement();
//            String sql = "SELECT t.type_name, COUNT(*) AS jumlah_surat FROM mail_content c JOIN mail_type t ON c.mail_type_id = t.mail_type_id GROUP BY t.type_name";
//
//            String sqlDiproses = sql + " WHERE (c.status_validation = 1 AND c.status_lead = 2) OR (c.status_validation = 2 AND c.status_lead = 2)";
//            String sqlDitolak = sql + " WHERE (c.status_validation = 0 AND c.status_lead = 0) "
//                    + "OR (c.status_validation = 0 AND c.status_lead = 2) "
//                    + "OR (c.status_validation = 1 AND c.status_lead = 0) ";
//            String sqlDiterima = sql + " WHERE c.status_validation = 1 AND c.status_lead = 1 ";
            // Menyesuaikan query berdasarkan filter
//            if ("Diproses".equals(query)) {
//                sql += " WHERE (c.status_validation = 1 AND c.status_lead = 2) OR (c.status_validation = 2 AND c.status_lead = 2)";
//            } else if ("Ditolak".equals(query)) {
//                sql += " WHERE (c.status_validation = 0 AND c.status_lead = 0) "
//                        + "OR (c.status_validation = 0 AND c.status_lead = 2) "
//                        + "OR (c.status_validation = 1 AND c.status_lead = 0) ";
//            } else if ("Diterima".equals(query)) {
//                sql += " WHERE c.status_validation = 1 AND c.status_lead = 1 ";
//            }

            if ("Diproses".equals(query)) {
                dataset.clear();
//                r=s.executeQuery(sqlDiproses + "GROUP BY t.type_name");
                for (int i = 0; i < rowCount; i++) {
                    String category = (String) datamodel.getValueAt(i, 1);
                    dataset.setValue(category, (Integer) datamodel.getValueAt(i, 4));
                }
            } else if ("Ditolak".equals(query)) {
                dataset.clear();
//                r=s.executeQuery(sqlDitolak + "GROUP BY t.type_name");
                for (int i = 0; i < rowCount; i++) {
                    String category = (String) datamodel.getValueAt(i, 1);
                    dataset.setValue(category, (Integer) datamodel.getValueAt(i, 5));
                }
            } else if ("Diterima".equals(query)) {
                dataset.clear();
//                r=s.executeQuery(sqlDiterima + "GROUP BY t.type_name");
                for (int i = 0; i < rowCount; i++) {
                    String category = (String) datamodel.getValueAt(i, 1);
                    dataset.setValue(category, (Integer) datamodel.getValueAt(i, 3));
                }
            } else {
                dataset.clear();
//                r=s.executeQuery(sql + ";");
                for (int i=0; i < rowCount; i++) {
                   String category = (String) datamodel.getValueAt(i, 1);
                   dataset.setValue(category, (Integer) datamodel.getValueAt(i, 2));
                }
            }

//            sql += "GROUP BY t.type_name";
//            ResultSet rSemua = s.executeQuery(sql);
//            while (r.next()) {
//                String category = r.getString("type_name"); //mengambil tipe surat
//                int jumlahSurat = rSemua.getInt("jumlah_surat"); //mengambil jumlah surat 
//                dataset.setValue(category, jumlahSurat); //menambahkan kategori dan jumlah surat ke dataset
//            }
            System.out.println(dataset.getValue(0));
//            r.close();
//            s.close();
//        } catch (SQLException e) {
//            System.out.println("Error: " + e.getMessage());
//        }

        System.out.println(dataset.getItemCount());

        // Membuat Pie Chart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Jumlah Surat Masuk per Kategori", // Judul chart
                dataset, // Data untuk chart
                true, // Legend
                true, // Tooltip
                false // URLs
        );

        // Membungkus chart dalam ChartPanel
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel2.setPreferredSize(new java.awt.Dimension(400, 400));
        chartPanel2.setLayout(new BorderLayout());
        chartPanel2.removeAll();
        chartPanel2.add(chartPanel, BorderLayout.CENTER);
        chartPanel2.revalidate();
        chartPanel2.repaint();

        return pieChart;
    }

    public void setTableAction() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            Statement s = c.createStatement();
            String sql = "SELECT mt.type_name, "
                    + "COUNT(mc.mail_id) AS Semua, "
                    + "SUM(CASE WHEN mc.status_validation = 1 AND mc.status_lead = 1 THEN 1 ELSE 0 END) AS Diterima, "
                    + "SUM(CASE WHEN mc.status_validation = 1 AND mc.status_lead = 2 OR mc.status_validation = 2 AND mc.status_lead = 2 THEN 1 ELSE 0 END) AS Diproses, "
                    + "SUM(CASE WHEN mc.status_validation = 0 AND mc.status_lead = 0 OR mc.status_validation = 0 AND mc.status_lead = 2 OR mc.status_validation = 1 AND mc.status_lead = 0 THEN 1 ELSE 0 END) AS Ditolak "
                    + "FROM mail_content mc "
                    + "INNER JOIN mail_type mt ON mc.mail_type_id = mt.mail_type_id "
                    + "GROUP BY mt.type_name "
                    + "ORDER BY mt.type_name ASC;";
            ResultSet r = s.executeQuery(sql);
            int i = 1;
            while (r.next()) {
                model.addRow(new Object[]{
                    i++,
                    r.getString("type_name"),
                    r.getInt("Semua"),
                    r.getInt("Diterima"),
                    r.getInt("Diproses"),
                    r.getInt("Ditolak")
                });

            }
            r.close();
            s.close();
        } catch (SQLException e) {
            System.out.println("Error, " + e);
        }
        tbHistory.getColumnModel().getColumn(0).setHeaderValue("No");
        tbHistory.getColumnModel().getColumn(1).setHeaderValue("Tipe Surat");
        tbHistory.getColumnModel().getColumn(2).setHeaderValue("Semua");
        tbHistory.getColumnModel().getColumn(3).setHeaderValue("Diterima");
        tbHistory.getColumnModel().getColumn(4).setHeaderValue("Diproses");
        tbHistory.getColumnModel().getColumn(5).setHeaderValue("Ditolak");

    }

    public static void adjustColumnWidths(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = getMaxPreferredWidth(table, column);
            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    private static int getMaxPreferredWidth(JTable table, int column) {
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

        return maxWidth + 10;
    }

    // Metode filter
    private void filter(String query) {
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        tbHistory.setRowSorter(tr);

        RowFilter<DefaultTableModel, Object> filter = null;
//        List<String> filteredData = new ArrayList<>(); // Menyimpan data yang difilter untuk chart

        if ("Diproses".equals(query)) {
            filter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String diproses = (String) entry.getValue(4);
                    return "Diproses".equalsIgnoreCase(diproses);
                }
            };
        } else if ("Ditolak".equals(query)) {
            filter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String ditolak = (String) entry.getValue(5);
                    return "Ditolak".equalsIgnoreCase(ditolak);
                }
            };
        } else if ("Diterima".equals(query)) {
            filter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String diterima = (String) entry.getValue(3);
                    return "Diterima".equalsIgnoreCase(diterima);
                }
            };
        } else if ("Semua".equals(query)) {
            tr.setRowFilter(null); // Tidak ada filter
            createPieChart("Semua"); // Perbarui pie chart dengan semua data
            return;
        }
        if (filter != null) {
            tr.setRowFilter(filter);
            createPieChart(query); // Perbarui pie chart sesuai filter
        }
    }

//    private void updateCharts(List<String> filteredData) {
//        if (filteredData == null) {
//            filteredData = new ArrayList<>();
//            for (int i = 0; i < model.getRowCount(); i++) {
//                filteredData.add((String) model.getValueAt(i, 1));
//            }
//        }
//        
//        
//    }
    public void addJTableToPDF(JTable table, Document document) {
        try {
            // Membuat tabel PDF dengan jumlah kolom sesuai JTable
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);

            // Menambahkan header dari JTable
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                pdfTable.addCell(cell);
            }

            // Menambahkan isi tabel dari JTable
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    pdfTable.addCell(value != null ? value.toString() : "");
                }
            }

            // Menambahkan tabel ke dokumen PDF
            document.add(pdfTable);
        } catch (DocumentException e) {
            System.out.println("Error saat menambahkan JTable ke PDF: " + e.getMessage());
        }
    }

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        setTableAction();
    }

    private void exportChartAsImage(JFreeChart chart, File file) {
        try {
            ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan grafik: " + e.getMessage());
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
        chartPanel1 = new javax.swing.JPanel();
        chartPanel2 = new javax.swing.JPanel();
        UnduhLaporan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        filterBox = new javax.swing.JComboBox<>();

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
        labelHistory.setText("Pelaporan Surat Masuk");

        refresh.setBackground(new java.awt.Color(19, 128, 97));
        refresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        refresh.setForeground(new java.awt.Color(255, 255, 255));
        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        chartPanel1.setLayout(new java.awt.BorderLayout());

        chartPanel2.setLayout(new java.awt.BorderLayout());

        UnduhLaporan.setBackground(new java.awt.Color(19, 128, 97));
        UnduhLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        UnduhLaporan.setForeground(new java.awt.Color(255, 255, 255));
        UnduhLaporan.setText("Unduh");
        UnduhLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnduhLaporanActionPerformed(evt);
            }
        });

        jLabel1.setText("Filter : ");

        filterBox.setBackground(new java.awt.Color(19, 128, 97));
        filterBox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        filterBox.setForeground(new java.awt.Color(255, 255, 255));
        filterBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Diproses", "Ditolak", "Diterima" }));
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

        javax.swing.GroupLayout panelTbLayout = new javax.swing.GroupLayout(panelTb);
        panelTb.setLayout(panelTbLayout);
        panelTbLayout.setHorizontalGroup(
            panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTbLayout.createSequentialGroup()
                        .addComponent(labelHistory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(UnduhLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(panelTbLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(chartPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                        .addComponent(chartPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTbLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelScrollTb, javax.swing.GroupLayout.PREFERRED_SIZE, 878, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(205, 205, 205))
        );
        panelTbLayout.setVerticalGroup(
            panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelHistory)
                    .addComponent(UnduhLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(filterBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chartPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(chartPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addComponent(panelScrollTb, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void UnduhLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnduhLaporanActionPerformed
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Laporan Sebagai");
            fileChooser.setSelectedFile(new File("LaporanSurat.pdf"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                Font tittleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph title = new Paragraph("Laporan Surat Masuk", tittleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));

                // Ekspor Line Chart sebagai gambar
                File lineChartFile = new File("line_chart_temp.png");
                String selectedFilterLine = filterBox.getSelectedItem().toString();
                exportChartAsImage(showLineChart(selectedFilterLine), lineChartFile);

                File pieChartFile = new File("pie_chart_temp.png");
                String selectedFilter = filterBox.getSelectedItem().toString();
                exportChartAsImage(createPieChart(selectedFilter), pieChartFile);

                // Tambahkan grafik Line Chart ke PDF dengan ukuran disesuaikan
                Image lineChartImage = Image.getInstance(lineChartFile.getAbsolutePath());
                lineChartImage.scaleToFit(675, 475);
                lineChartImage.setAlignment(Element.ALIGN_CENTER);
                document.add(lineChartImage);

                document.newPage(); // Jarak antar grafik

                // Tambahkan grafik Pie Chart ke PDF dengan ukuran disesuaikan
                Image pieChartImage = Image.getInstance(pieChartFile.getAbsolutePath());
                pieChartImage.scaleToFit(700, 500);
                pieChartImage.setAlignment(Element.ALIGN_CENTER);
                document.add(pieChartImage);

                document.newPage(); // Jarak antar elemen

                // Menyisipkan tabel dari JTable ke PDF
                addJTableToPDF(tbHistory, document);

                document.close();

                // Hapus file sementara
                lineChartFile.delete();
                pieChartFile.delete();

                JOptionPane.showMessageDialog(this, "Laporan berhasil disimpan di " + filePath);

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat membuat laporan: " + e.getMessage());
        }// TODO add your handling code here:
    }//GEN-LAST:event_UnduhLaporanActionPerformed

    private void filterBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterBoxActionPerformed

    private void filterBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterBoxItemStateChanged
        // TODO add your handlin        
        String query = filterBox.getSelectedItem().toString();

//        filter(query);
        setTableAction();
        createPieChart(query);
        showLineChart(query);
    }//GEN-LAST:event_filterBoxItemStateChanged
//    private void initComponents() {
//        chartContainer = new javax.swing.JPanel();
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//        setTitle("Line Chart");
//        
//        chartContainer.setLayout(new java.awt.BorderLayout());
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
//        getContentPane().setLayout(layout);
//        layout.setHorizontalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addComponent(chartContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        layout.setVerticalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addComponent(chartContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        
//        pack();
//    }

//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(() -> {
//            new PelaporanSuratPages().setVisible(true);
//        });
//    }
//    
    private javax.swing.JPanel chartContainer;

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        ValidationPages.main(null);
        dispose();
    }// GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PelaporanSuratPages().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton UnduhLaporan;
    private javax.swing.JPanel chartPanel1;
    private javax.swing.JPanel chartPanel2;
    private javax.swing.JComboBox<String> filterBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelHistory;
    private javax.swing.JScrollPane panelScrollTb;
    private javax.swing.JPanel panelTb;
    private javax.swing.JButton refresh;
    private javax.swing.JTable tbHistory;
    // End of variables declaration//GEN-END:variables
}
