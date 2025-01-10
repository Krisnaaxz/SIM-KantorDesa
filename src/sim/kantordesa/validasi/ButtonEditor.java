/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sim.kantordesa.validasi;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import sim.kantordesa.config.User;

/**
 *
 * @author Razik
 */
public class ButtonEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {
    private javax.swing.JButton button;
    private String label;
    private boolean isPushed;
    private javax.swing.JTable table;
    private User currentUser;
    
    public ButtonEditor(javax.swing.JTable table, User currentUser) {
        this.table = table;
        this.currentUser = currentUser;
        button = new javax.swing.JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListenerImpl());
    }
    
    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return label;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "Periksa" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
    
    private class ActionListenerImpl implements ActionListener {

        public ActionListenerImpl() {
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            fireEditingStopped();
            int row = table.getSelectedRow();
            Object value = table.getModel().getValueAt(row, 9);
            PopUpValidasiSekdes.main(new String[] { value.toString(), String.valueOf(currentUser.getIdRole()) });
        }
    }
    
}
