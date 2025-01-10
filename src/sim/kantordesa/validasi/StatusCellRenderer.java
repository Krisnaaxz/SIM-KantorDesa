/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sim.kantordesa.validasi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Razik
 */
public class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            String status = value.toString();
            if (null == status) {
                cell.setForeground(Color.BLACK);
            } else switch (status) {
                case "Reject" -> cell.setForeground(Color.RED);
                case "Accept" -> cell.setForeground(Color.GREEN);
                default -> cell.setForeground(Color.BLUE);
            }
        }

        return cell;
    }
}
