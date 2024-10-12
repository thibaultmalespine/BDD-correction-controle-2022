import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.sql.*;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Lieu extends JFrame{
	
	private JLabel l1, l2;	
    private JTextField jt1;
    private JComboBox<String> jc1;
	private JButton b;
	
	public Lieu(){
		super("Ajout d'un lieu");
		this.setLayout(new BorderLayout());
		JPanel p1 = new JPanel(new GridLayout(2,1));
		JPanel p2 = new JPanel(new GridLayout(2,1));
        this.l1 = new JLabel("nom du lieu :");
		this.l2 = new JLabel("pays : ");
        this.jt1 = new JTextField();
		this.jc1 = new JComboBox<String>(getPays());
		this.b = new JButton("envoyer");
		
        p1.add(l1);
		p1.add(l2);
		p2.add(jt1);
		p2.add(jc1);
		this.add(p1,BorderLayout.WEST);
		this.add(p2,BorderLayout.CENTER);
		this.add(b,BorderLayout.SOUTH);
		b.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseClicked(java.awt.event.MouseEvent e) {
                gestionnaireClique();
            };
        });
		this.setSize(400,250);
		this.setVisible(true);
	}
	
    private void gestionnaireClique(){
        
		try
	    {
			if (jt1.getText().trim().equals("")) {
				new FenetreErreur("Renseigné le nom du Lieu !");
			}
			else {
				Connection connection = Bdd.connection;
				PreparedStatement query = connection.prepareStatement("INSERT INTO lieu VALUES(?, ?)");
				query.setString(1, jt1.getText());
				query.setString(2, (String) jc1.getSelectedItem());
				query.executeUpdate();
				Bdd.closeConnection();
				this.setVisible(false);
			}
		}
	    catch(SQLException e){
			if (e.getSQLState().equals("23505")) {
				new FenetreErreur("Ce Lieu existe déjà dans la base de données");
			} 
			else{
				System.out.println(e.getSQLState());
				System.out.println(e.getMessage());
			}
	    }
    }

    private Vector<String> getPays(){
        Vector<String> vector = new Vector<>();
        Connection connection = Bdd.connection;
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM pays");
            while (result.next()) {
                vector.add(result.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return vector;        
    }

	public static void main(String[] args) {
        Bdd.createConnection();
        Lieu f = new Lieu();
	}

}
