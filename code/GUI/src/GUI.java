import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/*
    na pas inserten -- meteen inloggen
    na inloggen -- saldo, pinnen, snelkeuze 70eu, 

    altijd taal kunnen veranderen en kunnen afbreken


*/

public class GUI extends JFrame implements ActionListener {
    private final String Nederlands = "Nederlands";
    private final String Engels = "Engels";
    private String currentLanguage = Nederlands, currentUid;
    private Serial usedSerial;
    private SQLConnection SQLconnection;


    private int breedte = 600, hoogte = 800, aantalPoging =0, resterendePoging =2;
    private JButton[] knoppen;
    private int logoHoogte = 125, knopBreedte = 300, onderkantHoogte = 100;
    //final Font font = new Font("Arial", Font.BOLD, 20);

    private JPanel venster = new JPanel();
    private JPanel vensterLinks = new JPanel();
    private JPanel vensterRechts = new JPanel();
    private JPanel logo = new JPanel();
    private JPanel textPanel = new JPanel();
    private JPanel onderkant = new JPanel();
    private JLabel insertCardLabel = new JLabel();
    private JLabel logoLabel = new JLabel();
    private ImageIcon insertCardIcon = new ImageIcon("code/GUI/Images/insert card.png");
    private ImageIcon logoIcon = new ImageIcon("code/GUI/Images/logo_white_trans.png");

    private ArrayList<String> code = new ArrayList<String>();

    JLabel test1 = new JLabel("Nog 2 pogingen");
    JLabel test2 = new JLabel("Nog 1 poging");
    JLabel test3 = new JLabel("Nog 0 pogingen");
    JLabel text1 = new JLabel("");
    JLabel text2 = new JLabel("");
    JLabel text3 = new JLabel("");
    JLabel codeText = new JLabel("");

    public void startGUI(Serial s, SQLConnection c) {
        usedSerial = s;
        SQLconnection = c;
        knoppen = new JButton[8];
        setSize(breedte, hoogte);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setTitle("GUI");
        createGUI();
        setVisible(true);
    }

    private void createGUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);        
        getContentPane().setLayout(new BorderLayout(50, 50));

        //venster.setBackground(Color.red);
        venster.setFont(new Font("Calibri", Font.PLAIN, 50));
        venster.setAlignmentX(CENTER_ALIGNMENT);

        vensterLinks.setLayout(new GridLayout(4 , 1, 50, 50));
        vensterLinks.setPreferredSize(new Dimension(knopBreedte, MAXIMIZED_VERT));

        vensterRechts.setLayout(new GridLayout(4, 1, 50, 50));
        vensterRechts.setPreferredSize(new Dimension(knopBreedte, MAXIMIZED_VERT));

        logo.setPreferredSize(new Dimension(MAXIMIZED_HORIZ - knopBreedte * 2, logoHoogte));
        logo.setBackground(Color.black);

        textPanel.setLayout(new GridLayout(10, 1, venster.getWidth(), venster.getHeight()));
        venster.add(textPanel);

        onderkant.setPreferredSize(new Dimension(MAXIMIZED_HORIZ - knopBreedte * 2, onderkantHoogte));
        onderkant.setBackground(Color.black);

        insertCardLabel.setIcon(insertCardIcon);
        
        logoLabel.setIcon(logoIcon);

        for(int i = 0; i < 8; i++) {
            knoppen[i] = new JButton(String.valueOf(i));
            knoppen[i].setFont(new Font("Calibri", Font.BOLD, 20));
            knoppen[i].setBackground(Color.LIGHT_GRAY);
            if(i < 4) {
                vensterLinks.add(knoppen[i]);
            } else {
                vensterRechts.add(knoppen[i]);
            }
            knoppen[i].addActionListener(this);
        }

        hoofdscherm(Nederlands);

        venster.add(insertCardLabel);
        logo.add(logoLabel);

        getContentPane().add(vensterLinks, BorderLayout.WEST);
        getContentPane().add(vensterRechts, BorderLayout.EAST);
        getContentPane().add(venster, BorderLayout.CENTER);
        getContentPane().add(logo, BorderLayout.NORTH);
        getContentPane().add(onderkant, BorderLayout.SOUTH);

        test1.setFont((new Font("Calibri",1,20)));
        test1.setForeground(Color.red);
        venster.add(test1);	
        test2.setFont((new Font("Calibri",1,20)));
    	test2.setForeground(Color.red);
    	venster.add(test2);
    	test3.setFont((new Font("Calibri",1,20)));
    	test3.setForeground(Color.red);
    	venster.add(test3);
    	test3.setVisible(false);
    	test2.setVisible(false);
    	test1.setVisible(false);

        text1.setFont(new Font("Calibri", Font.PLAIN, 40));
        text2.setFont(new Font("Calibri", Font.PLAIN, 40));
        text3.setFont(new Font("Calibri", Font.PLAIN, 40));
        codeText.setFont(new Font("Calibri", Font.BOLD, 40));
        codeText.setVerticalTextPosition(SwingConstants.BOTTOM);
        textPanel.add(text1);
        textPanel.add(text2);
        textPanel.add(text3);
        textPanel.add(codeText);

    }

    public void hoofdscherm(String taal) {
        if(taal == Engels) {
            knoppen[0].setText("draw money");
            knoppen[3].setText("change language");
            knoppen[7].setText("close");
        } else if(taal == Nederlands) {
            knoppen[0].setText("pinnen");
            knoppen[3].setText("verander taal");
            knoppen[7].setText("afsluiten");
        }

        knoppen[0].setVisible(true);
        knoppen[1].setVisible(false);
        knoppen[2].setVisible(false);
        knoppen[3].setVisible(true);
        knoppen[4].setVisible(false);
        knoppen[5].setVisible(false);
        knoppen[6].setVisible(false);
        knoppen[7].setVisible(true);
    }

    public void inlogScherm(String taal) {
        for(int i = 0; i < knoppen.length; i++) {
            knoppen[i].setVisible(false);
        }
        knoppen[3].setVisible(true);
        insertCardLabel.setVisible(false);
        String[] details = SQLconnection.getDetails(currentUid);
        if(taal == Engels) {
            text1.setText("Iban: " + currentUid);
            text2.setText("Balance: " + details[0]);
            text3.setText("insert pin");
        } else if(taal == Nederlands) {
            text1.setText("Iban: " + currentUid);
            text2.setText("Saldo: " + details[0]);
            text3.setText("voer uw pincode in");
        }
        text3.setVisible(true);
    }

    public void pinnen(String taal) {
        text3.setVisible(false);
        if(taal == Engels){

            knoppen[1].setText("pin 10");
            knoppen[5].setText("pin 20");
            knoppen[7].setText("cancel");
        } else if(taal == Nederlands){
    
            knoppen[1].setText("pin 10");
            knoppen[5].setText("pin 20");
            knoppen[7].setText("afbreken");
        }
            
            knoppen[0].setVisible(false);
            knoppen[1].setVisible(true);
            knoppen[2].setVisible(false);
            knoppen[3].setVisible(false);
            knoppen[4].setVisible(false);
            knoppen[5].setVisible(true);
            knoppen[6].setVisible(false);
            knoppen[7].setVisible(true);

    }
    
    

    public void taal(String taal) {

        if(taal == Engels){

        knoppen[1].setText("Nederlands");
        knoppen[5].setText("English");
        knoppen[7].setText("cancel");

        knoppen[0].setVisible(false);
        knoppen[1].setVisible(true);
        knoppen[2].setVisible(false);
        knoppen[3].setVisible(false);
        knoppen[4].setVisible(false);
        knoppen[5].setVisible(true);
        knoppen[6].setVisible(false);
        knoppen[7].setVisible(true);

        } else if(taal == Nederlands){

        knoppen[1].setText("Nederlands");
        knoppen[5].setText("English");
        knoppen[7].setText("afbreken");

        knoppen[0].setVisible(false);
        knoppen[1].setVisible(true);
        knoppen[2].setVisible(false);
        knoppen[3].setVisible(false);
        knoppen[4].setVisible(false);
        knoppen[5].setVisible(true);
        knoppen[6].setVisible(false);
        knoppen[7].setVisible(true);
        }
    }

    private void kaartVerwijderdScherm(String taal) {
        for(int i = 0; i < knoppen.length; i++) {
            knoppen[i].setVisible(false);
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {

        } finally {
            hoofdscherm(taal);
        }
    }

    public void recievedKey(String data) {
        if(currentUid == null) {
            return;
        }

    	String maskedCode = "";
    	resterendePoging = 2 - aantalPoging;
        if(data.contains("D")) {
            if(checkCode()) {
                pinnen(Nederlands);
                code.clear();
                test3.setVisible(false);
            	test2.setVisible(false);
            	test1.setVisible(false);
            } else {
                System.out.println("Nog "+ resterendePoging+ " pogingen");
                if(resterendePoging == 2) {
                    test3.setVisible(false);
                    test2.setVisible(false);
                    test1.setVisible(true);
                    insertCardLabel.setVisible(false);
                }else if(resterendePoging == 1) {
                    test3.setVisible(false);
                    test2.setVisible(true);
                    test1.setVisible(false);
                    insertCardLabel.setVisible(false);
                }else if(resterendePoging <= 0) {
                    test2.setVisible(false);
                    test1.setVisible(false);
                    test3.setVisible(true);
                    insertCardLabel.setVisible(false);
                }
                code.clear();
            }
        } else if(data.contains("C")) {
            code.clear();
        } else if(code.size() < 6) {
            code.add(data);
        }

        for(int i = 0; i < code.size(); i++) {
            maskedCode += "*";
        }
        codeText.setText(maskedCode);
    }

    public boolean checkCode() {
        String checkCode = SQLconnection.getCode(currentUid);
        String newCode = String.join("", code);
        //System.out.println(newCode);
        if(newCode.equals(checkCode)) {
            System.out.println("code goedgekeurd");
            aantalPoging = 0;
            return true;
        } else {
            System.out.println("code foutgekeurd");
            aantalPoging++;
            return false;
        }
    }

    public void cardRemoved() {
        currentUid = null;
        kaartVerwijderdScherm(currentLanguage);
    }

    public void uidInUse(String uid) {
        currentUid = uid;
        inlogScherm(currentLanguage);
    }

    public void actionPerformed(ActionEvent e) {
        String text = e.getActionCommand();

        switch (text) {
            case "afsluiten":
                System.exit(0);
                break;
            case "close":
                System.exit(0);
                break;
            case "pinnen":
                pinnen(Nederlands);
                break;
            case "draw money":
                pinnen(Engels);
                break;
            case "verander taal":
                taal(Nederlands);
                break;
            case "change language":
                taal(Engels);
                break;
            case "English":
                currentLanguage = Engels;
                hoofdscherm(currentLanguage);
                break;
            case "Nederlands":
                currentLanguage = Nederlands;
                hoofdscherm(currentLanguage);
                break;
            case "afbreken":
                insertCardLabel.setVisible(true);
                hoofdscherm(Nederlands);
                break;
            case "cancel":
                hoofdscherm(Engels);
                break;
            default:
                System.out.println("weet niet wat te doen! " + text);
                break;
        }


        System.out.println("knop: " + text);
    }
}
