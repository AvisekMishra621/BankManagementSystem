package bank.management.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Withdrawl extends JFrame implements ActionListener {
    JTextField t1;
    JButton b1, b2;
    JLabel l1, l2;
    String pin;

    Withdrawl(String pin) {
        this.pin = pin;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 900, 900);
        add(l3);

        l1 = new JLabel("MAXIMUM WITHDRAWAL IS RS.10,000");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        l2 = new JLabel("PLEASE ENTER YOUR AMOUNT");
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("System", Font.BOLD, 16));

        t1 = new JTextField();
        t1.setFont(new Font("Raleway", Font.BOLD, 25));

        b1 = new JButton("WITHDRAW");
        b2 = new JButton("BACK");

        setLayout(null);

        l1.setBounds(190,300,700,35);
        l3.add(l1);

        l2.setBounds(190, 400, 400, 20);
        l3.add(l2);

        t1.setBounds(190, 430, 300, 30);
        l3.add(t1);

        b1.setBounds(355,485,150,30);
        l3.add(b1);

        b2.setBounds(355,520,150,30);
        l3.add(b2);

        b1.addActionListener(this);
        b2.addActionListener(this);

        setSize(900, 900);
        setLocation(500, 0);
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String amount = t1.getText();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(date);

            if (ae.getSource() == b1) {
                if (amount.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter the Amount you want to Withdraw");
                } else {
                    BigDecimal withdrawalAmount = new BigDecimal(amount); // Convert input to BigDecimal

                    Conn c1 = new Conn();
                    ResultSet rs = c1.s.executeQuery("SELECT * FROM bank WHERE pin = '" + pin + "'");
                    BigDecimal balance = BigDecimal.ZERO;

                    while (rs.next()) {
                        BigDecimal transactionAmount = new BigDecimal(rs.getString("amount"));
                        if (rs.getString("type").equals("Deposit")) {
                            balance = balance.add(transactionAmount);
                        } else {
                            balance = balance.subtract(transactionAmount);
                        }
                    }

                    // **FIXED COMPARISON**
                    if (balance.compareTo(withdrawalAmount) < 0) {
                        JOptionPane.showMessageDialog(null, "Insufficient Balance");
                        return;
                    }

                    Connection con = c1.getConnection();  // Get the Connection object

                    String query = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement pst = con.prepareStatement(query);  // Use Connection, not Statement
                    pst.setString(1, pin);
                    pst.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Store date as TIMESTAMP
                    pst.setString(3, "Withdraw");
                    pst.setBigDecimal(4, new BigDecimal(amount)); // Convert amount to BigDecimal

                    pst.executeUpdate();
                    pst.close();
                    con.close(); // Close resources


                    JOptionPane.showMessageDialog(null, "Rs. " + withdrawalAmount + " Debited Successfully");

                    setVisible(false);
                    new Transactions(pin).setVisible(true);
                }
            } else if (ae.getSource() == b2) {
                setVisible(false);
                new Transactions(pin).setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e);
        }
    }

    public static void main(String[] args) {
        new Withdrawl("").setVisible(true);
    }
}
