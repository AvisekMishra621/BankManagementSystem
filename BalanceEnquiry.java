package bank.management.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import javax.swing.*;
import java.util.*;
import java.math.BigDecimal;


class BalanceEnquiry extends JFrame implements ActionListener {

    JTextField t1, t2;
    JButton b1, b2, b3;
    JLabel l1, l2, l3;
    String pin;

    BalanceEnquiry(String pin) {
        setLayout(null);

        this.pin = pin;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 900, 900);
        add(l3);

        l1 = new JLabel();
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        b1 = new JButton("BACK");

        l1.setBounds(170, 300, 400, 30);
        l3.add(l1);

        b1.setBounds(355, 520, 150, 30);
        l3.add(b1);
        BigDecimal balance = BigDecimal.ZERO;
        Conn c1 = new Conn();
        try {
            ResultSet rs = c1.s.executeQuery("SELECT * FROM bank WHERE pin = '" + pin + "'");
            while (rs.next()) {
                BigDecimal amount = new BigDecimal(rs.getString("amount"));
                if (rs.getString("type").equals("Deposit")) {
                    balance = balance.add(amount);  // Add deposit
                } else {
                    balance = balance.subtract(amount);  // Subtract withdrawal
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        l1.setText("Your Current Account Balance is Rs " + balance);


        b1.addActionListener(this);

        setSize(900, 900);
        setUndecorated(true);
        setLocation(300, 0);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Transactions(pin).setVisible(true);
    }

    public static void main(String[] args) {
        new BalanceEnquiry("").setVisible(true);
    }
}

