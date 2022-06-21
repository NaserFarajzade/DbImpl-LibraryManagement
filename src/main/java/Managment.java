import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import project.ConnectionProvider;


public class Managment extends javax.swing.JFrame {

    public Managment() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("managment");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setText("Management");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("userID :");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, -1, -1));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 150, 152, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("accessinno :");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, -1, -1));
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 152, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("status :");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 210, -1, -1));

        jButton1.setText("modify");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 250, 73, -1));

        jButton2.setText("cancle");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 250, 73, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "reserve", "borrow", "return" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, 150, -1));

        String currentPath = null;
        try {
            currentPath = new java.io.File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jLabel4.setIcon(new javax.swing.ImageIcon(currentPath + "\\library project\\new.jpg")); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, -1));

        pack();
    }
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {}

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String userId = jTextField1.getText();
        String accessinno = jTextField2.getText();
        int statusId = jComboBox1.getSelectedIndex();
        //reserve = 0
        //borrow = 1
        //return = 2
        
        String userIdCheckQuery = "select * from users where userID = "+ userId;
        String accessinnoCheckQuery = "select * from books where accessinno = "+ accessinno;
        String updateRETURNQuery = "UPDATE borrows SET statusID = "+ statusId + " WHERE userID = " + userId + " AND accessinno = " + accessinno;
        String isBookAvailableQuery = "select * from borrows WHERE accessinno = " + accessinno + " AND borrows.statusID IN (0,1)";
        String addBORROWQuery = "insert into borrows values("+userId+","+accessinno+","+statusId+")";
        String wasBorroedOrReserved = "select * from borrows WHERE accessinno = " + accessinno + " AND borrows.statusID IN (0,1) AND userID = "+ userId;
        
        Connection con = ConnectionProvider.getCon();
        Statement statement;
        
       
        try {
            statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(userIdCheckQuery);
            if(resultSet.next()){
                resultSet.close();
                
                ResultSet resultSet2 = statement.executeQuery(accessinnoCheckQuery);
                if(resultSet2.next()){
                    resultSet2.close();
                    
                    if(statusId == 2){
                        //return book to library
                        ResultSet resultSet4 = statement.executeQuery(wasBorroedOrReserved);
                        if(resultSet4.next()){
                            resultSet4.close();
                            statement.executeUpdate(updateRETURNQuery);
                            JOptionPane.showMessageDialog(null , "suceccfully returned");
                            setVisible(false);
                            new Managment().setVisible(true);
                        }else{
                            JOptionPane.showMessageDialog(null , "you didn't borrowed or reserved this book");
                            setVisible(false);
                            new Managment().setVisible(true);
                        }
                        
                    }
                    else{
                        //borrow or reserve
                        ResultSet resultSet3 = statement.executeQuery(isBookAvailableQuery);
                        if(resultSet3.next()){
                            resultSet3.close();
                            //borrowed or reserved
                            JOptionPane.showMessageDialog(null , "the book was borroed or reserved");
                            setVisible(false);
                            new Managment().setVisible(true);
                        }else{
                            try{
                                statement.executeUpdate(addBORROWQuery);    
                            }catch(Exception e){
                                statement.executeUpdate(updateRETURNQuery);
                            }
                            
                            JOptionPane.showMessageDialog(null , "info successfully added");
                            setVisible(false);
                            new Managment().setVisible(true);
                        }
                    }
                    
                }
                else{
                    JOptionPane.showMessageDialog(null , "book is not in database");
                    setVisible(false);
                    new Managment().setVisible(true);
                }
                
            }
            else{
                JOptionPane.showMessageDialog(null , "user not found");
                setVisible(false);
                new Managment().setVisible(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Managment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
     dispose();
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {}

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Managment().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
}
