package PhoneBoook;

import Gui.ItemsModel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Collections;
import java.util.Vector;

public class ItemsManager {

    public static final String SEPARATOR = ",";

    private Vector<Item> items = new Vector<Item>();

    private String filePath;

    private ItemsModel model = null;

    public ItemsManager(String filePath) {
        this.filePath = filePath;
        this.readCsv();
    }

    public Vector<Item> readCsv() {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = SEPARATOR;

        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] item = line.split(cvsSplitBy);

                if (item.length == 3)
                    items.add(new Item(item[0], item[1], item[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return this.items;
    }

    public boolean flush() {

        try {
            FileWriter writer = new FileWriter(this.filePath);
            for (Item item : this.items) {

                writer.append(item.toString());
                writer.append("\n");

                writer.flush();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public ItemsModel getModel() {
        if (this.model == null)
            this.model = new ItemsModel(this);

        return this.model;
    }

    public ItemsManager add(Item item) {
        this.items.add(item);

        return this;
    }

    public ItemsManager remove(int index) {
        this.items.remove(index);

        return this;
    }

    public ItemsManager remove(Item index) {
        this.items.remove(index);

        return this;
    }

    public int getCount() {
        return this.items.size();
    }

    public Item getValueAt(int rowIndex) {
        return this.items.get(rowIndex);
    }
}
