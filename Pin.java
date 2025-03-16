package bank.management.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Pin extends JFrame implements ActionListener {

    JPasswordField t1, t2;
    JButton b1, b2;
    JLabel l1, l2, l3;
    String pin;

    Pin(String pin) {
        this.pin = pin;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l4 = new JLabel(i3);
        l4.setBounds(0, 0, 900, 900);
        add(l4);

        l1 = new JLabel("CHANGE YOUR PIN");
        l1.setFont(new Font("System", Font.BOLD, 16));
        l1.setForeground(Color.WHITE);

        l2 = new JLabel("New PIN:");
        l2.setFont(new Font("System", Font.BOLD, 16));
        l2.setForeground(Color.WHITE);

        l3 = new JLabel("Re-Enter New PIN:");
        l3.setFont(new Font("System", Font.BOLD, 16));
        l3.setForeground(Color.WHITE);

        t1 = new JPasswordField();
        t1.setFont(new Font("Raleway", Font.BOLD, 25));

        t2 = new JPasswordField();
        t2.setFont(new Font("Raleway", Font.BOLD, 25));

        b1 = new JButton("CHANGE");
        b2 = new JButton("BACK");

        b1.addActionListener(this);
        b2.addActionListener(this);

        setLayout(null);

        l1.setBounds(250, 280, 500, 35);
        l4.add(l1);

        l2.setBounds(165, 320, 180, 25);
        l4.add(l2);

        l3.setBounds(165, 360, 180, 25);
        l4.add(l3);

        t1.setBounds(330, 320, 180, 25);
        l4.add(t1);

        t2.setBounds(330, 360, 180, 25);
        l4.add(t2);

        b1.setBounds(355, 485, 150, 35);
        l4.add(b1);

        b2.setBounds(355, 520, 150, 35);
        l4.add(b2);

        setSize(900, 900);
        setLocation(300, 0);
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == b1) {
            try {
                String npin = new String(t1.getPassword());
                String rpin = new String(t2.getPassword());

                if (npin.isEmpty() || rpin.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "PIN fields cannot be empty");
                    return;
                }

                if (!npin.equals(rpin)) {
                    JOptionPane.showMessageDialog(null, "Entered PINs do not match");
                    return;
                }

                Conn c1 = new Conn();
                Connection con = c1.getConnection();

                con.setAutoCommit(false); // Start transaction

                // 1. Update `login` table FIRST (to avoid foreign key issue)
                String updateLogin = "UPDATE login SET pin = ? WHERE pin = ?";
                try (PreparedStatement stmt = con.prepareStatement(updateLogin)) {
                    stmt.setString(1, rpin);
                    stmt.setString(2, pin);
                    stmt.executeUpdate();
                }

                // 2. Update `bank` table NEXT
                String updateBank = "UPDATE bank SET pin = ? WHERE pin = ?";
                try (PreparedStatement stmt = con.prepareStatement(updateBank)) {
                    stmt.setString(1, rpin);
                    stmt.setString(2, pin);
                    stmt.executeUpdate();
                }

                // 3. Update `signup3` table LAST
                String updateSignup3 = "UPDATE signup3 SET pin = ? WHERE pin = ?";
                try (PreparedStatement stmt = con.prepareStatement(updateSignup3)) {
                    stmt.setString(1, rpin);
                    stmt.setString(2, pin);
                    stmt.executeUpdate();
                }

                con.commit(); // Commit the transaction
                JOptionPane.showMessageDialog(null, "PIN changed successfully");

                setVisible(false);
                new Transactions(rpin).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating PIN");
            }
        } else if (ae.getSource() == b2) {
            new Transactions(pin).setVisible(true);
            setVisible(false);
        }
    }



    public static void main(String[] args) {
        new Pin("").setVisible(true);
    }
}
