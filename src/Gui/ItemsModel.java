package Gui;

import PhoneBoook.Item;
import PhoneBoook.ItemsManager;

import javax.swing.table.DefaultTableModel;

public class ItemsModel extends DefaultTableModel {

    private static String[] popisky = {"Jméno", "Adresa", "Telefon"};
    private ItemsManager itemsManager;

    public ItemsModel(ItemsManager itemsManager) {
        this.itemsManager = itemsManager;
    }

    @Override
    public String getColumnName(int column) {
        return popisky[column];
    }

    @Override
    public int getColumnCount() {
        return popisky.length;
    }

    @Override
    public int getRowCount() {
        if(this.itemsManager == null) return 0;

        return this.itemsManager.getCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Item k = this.itemsManager.getValueAt(rowIndex);

        switch (columnIndex) {
            case 0:
                return k.getName();
            case 1:
                return k.getAddress();
            case 2:
                return k.getPhone();
        }
        return null;
    }

    public void setItemsManager(ItemsManager itemsManager) {
        this.itemsManager = itemsManager;
    }

    public boolean isCellEditable(int row, int column){
        return false;
    }
}
