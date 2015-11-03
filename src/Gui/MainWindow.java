package Gui;

import PhoneBoook.Item;
import PhoneBoook.ItemsManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

public class MainWindow extends JFrame {

    private ItemsManager itemsManager;
    private JTable jTable;

    public static final String SEARCH_STRING = "Vyhledat";
    private JTextField search = new JTextField(SEARCH_STRING, 15);
    private TableRowSorter<TableModel> rowSorter;

    // construct
    public MainWindow() {
        JFileChooser fc = setupFileChooser();

        File file = null;
//        File file = new File("/Users/xdobro4/Documents/projekty/PRO2/items.csv");
        Integer i = 0;
        do {
            Integer returnVal = fc.showOpenDialog(MainWindow.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
            } else {
                showMessage("Nejprve je třeba vybrat CSV soubor!", "Varování", JOptionPane.WARNING_MESSAGE);
            }

        } while (++i < 2 && file == null); // pokud uzivatel nevybere soubor 2x program konci
//
        if (file == null) {
            showMessage("Nebyl zvolen žádný soubor!", "Varování", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
            return;
        }

        this.itemsManager = new ItemsManager(file.getAbsoluteFile().toString());

        setTitle("Evidence kontaktů");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // items
        this.add(createPanel(), BorderLayout.CENTER);
        this.add(createToolbar(), BorderLayout.NORTH);
        this.add(createSearchbar(), BorderLayout.SOUTH);

        this.pack();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });
    }

    private JPanel createSearchbar() {
        JPanel panel = new JPanel();

        this.search.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleSearch();
            }

        });
        this.search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (search.getText().equals(SEARCH_STRING)) {
                    search.setText("");
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (search.getText().equals("")) {
                    clearSearch();
                }
            }
        });

        this.rowSorter = new TableRowSorter<>(jTable.getModel());
        this.jTable.setRowSorter(this.rowSorter);

        panel.add(search);

        return panel;
    }

    private JToolBar createToolbar() {
        String vers = System.getProperty("os.name").toLowerCase();
        boolean isMac = vers.contains("mac");

        JToolBar tb = new JToolBar();

        JButton btDelete = new JButton("Smazat");
        this.addToolTip(btDelete, isMac ? "CMD+MINUS nebo CMD+D" : "CTRL+MINUS nebo CTRL+D");
        btDelete.addActionListener(e -> {
            int index = jTable.getSelectedRow();

            if (index != -1) {
                itemsManager.remove(jTable.convertRowIndexToModel(index));
                itemsManager.getModel().fireTableDataChanged();
            }

        });

        JButton btAdd = new JButton("Pridej");
        this.addToolTip(btAdd, isMac ? "CMD+PLUS nebo CMD+A" : "CTRL+PLUS nebo CTRL+A");
        btAdd.addActionListener(e -> {
            Item k = ItemDialog.getInstance(MainWindow.this).add();
            if (k != null) {
                itemsManager.add(k);
                itemsManager.getModel().fireTableDataChanged();
            }
        });

        JButton btSave = new JButton("Uložit");
        this.addToolTip(btSave, isMac ? "CMD+S" : "CTRL+S");
        btSave.addActionListener(e -> {
            if (itemsManager.flush()) {
                showMessage("Uloženo", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessage("Chyba při ukládní", "Varování", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btEdit = new JButton("Upravit");
        btEdit.addActionListener(e -> edit());

        ActionListener saveAction = e -> btSave.doClick();
        ActionListener addAction = e -> btAdd.doClick();
        ActionListener deleteAction = e -> btDelete.doClick();

        // hot keys
        if (isMac) {
            // os x
            this.getRootPane().registerKeyboardAction(saveAction, KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), JComponent.WHEN_IN_FOCUSED_WINDOW);
            this.getRootPane().registerKeyboardAction(addAction, KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), JComponent.WHEN_IN_FOCUSED_WINDOW);
            this.getRootPane().registerKeyboardAction(addAction, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), JComponent.WHEN_IN_FOCUSED_WINDOW);
            this.getRootPane().registerKeyboardAction(deleteAction, KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), JComponent.WHEN_IN_FOCUSED_WINDOW);
            this.getRootPane().registerKeyboardAction(deleteAction, KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), JComponent.WHEN_IN_FOCUSED_WINDOW);
        } else {
            // windows + linux
            this.getRootPane().registerKeyboardAction(saveAction, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
            this.getRootPane().registerKeyboardAction(addAction, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
            this.getRootPane().registerKeyboardAction(addAction, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
            this.getRootPane().registerKeyboardAction(deleteAction, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
            this.getRootPane().registerKeyboardAction(deleteAction, KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }


        tb.add(btSave);
        tb.add(btAdd);
        tb.add(btEdit);
        tb.add(btDelete);

        return tb;
    }

    private void addToolTip(JButton btn, String s) {
        btn.setToolTipText(String.format("Hotkey: %s", s));
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        ItemsModel model = this.itemsManager.getModel();
        this.jTable = new JTable(model);
        this.jTable.removeEditor();
        this.jTable.setAutoCreateRowSorter(true);
        this.jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    edit();
                }
            }
        });

        panel.add(new JScrollPane(jTable), BorderLayout.CENTER);
        return panel;
    }

    private void edit() {
        int index = jTable.getSelectedRow();
        if (index != -1) {
            Item item = this.itemsManager.getValueAt(jTable.convertRowIndexToModel(index));
            ItemDialog.getInstance(MainWindow.this).edit(item);
            this.itemsManager.getModel().fireTableDataChanged();
        }
    }


    private void handleSearch() {
        String text = search.getText();
        if (text.trim().length() == 0) {
            this.rowSorter.setRowFilter(null);
        } else {
            this.rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void clearSearch() {
        this.search.setText(SEARCH_STRING);
    }

    // UTILS
    private JFileChooser setupFileChooser() {
        JFileChooser fc = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV soubor", "csv");
        fc.setFileFilter(filter);

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setCurrentDirectory(workingDirectory);

        return fc;
    }

    private void showMessage(String message, String title, int type) {
        final JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, title, type);
    }

    // RUN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }

}

