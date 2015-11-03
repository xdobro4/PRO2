package Gui;

import PhoneBoook.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ItemDialog extends JDialog {
    private static ItemDialog _instance = null;

    private JTextField name, address, phone;

    private boolean confirm = false;

    private ItemDialog(JFrame owner) {
        super(owner, "Kontakt - detail", true);

        initGui();

        pack();
    }

    public static ItemDialog getInstance(JFrame owner) {
        if (_instance == null) {
            _instance = new ItemDialog(owner);
        }
        return _instance;
    }

    private void initGui() {
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);

        JLabel lbName = new JLabel("Jméno");
        lbName.setDisplayedMnemonic('J');
        add(lbName, c);
        //
        c.gridx = 1;
        JLabel lbAddress = new JLabel("Adresa");
        lbAddress.setDisplayedMnemonic('P');
        add(lbAddress, c);
        //
        name = new JTextField(15);
        lbName.setLabelFor(name);
        c.gridy = 1;
        c.gridx = 0;
        add(name, c);
        //
        address = new JTextField(15);
        lbAddress.setLabelFor(address);
        c.gridy = 1;
        c.gridx = 1;
        add(address, c);
        //
        JLabel lbPhone = new JLabel("Telefon");
        c.gridx = 0;
        c.gridy = 2;
        add(lbPhone, c);
        //
        phone = new JTextField(20);
        lbPhone.setLabelFor(phone);
        c.gridy = 3;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(phone, c);

        JButton btOK = new JButton("OK");
        btOK.setToolTipText("Odeslat lze také tlačítkem Enter");
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 4;
        add(btOK, c);

        JButton btStorno = new JButton("Storno");
        btStorno.setToolTipText("Odeslat lze také tlačítkem ESC");
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        add(btStorno, c);

        btOK.addActionListener(e -> {
            confirm = true;
            setVisible(false);
        });
        btStorno.addActionListener(e -> {
            confirm = false;
            setVisible(false);
        });

        ActionListener escListener = e -> {
            btStorno.doClick();
//            setVisible(false);
        };

        this.getRootPane().setDefaultButton(btOK);
        this.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),JComponent.WHEN_IN_FOCUSED_WINDOW);
    }


    public Item add() {
        Item item = null;
        emptyInputs();
        name.grabFocus();
        setVisible(true);
        if (confirm) {
            item = new Item(name.getText(), address.getText(), phone.getText());
        }
        return item;
    }

    private void emptyInputs() {
        phone.setText("");
        name.setText("");
        address.setText("");
    }

    public void edit(Item item) {
        name.setText(item.getName());
        address.setText(item.getAddress());
        phone.setText(item.getPhone());
        name.grabFocus();
        setVisible(true);
        if (confirm) {
            item.setName(name.getText());
            item.setAddress(address.getText());
            item.setPhone(phone.getText());
        }
    }

}

