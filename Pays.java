import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Pays extends JFrame{
	
	private JLabel l1;	
    private JTextField jt1;
	private JButton b;
	
	public Pays(){
		super("Ajout d'un pays");
		this.setLayout(new BorderLayout());
		JPanel p1 = new JPanel(new GridLayout(1,1));
		JPanel p2 = new JPanel(new GridLayout(1,1));
		this.l1 = new JLabel("nom du pays : ");
		this.jt1 = new JTextField();
		this.b = new JButton("envoyer");
		p1.add(l1);
		p2.add(jt1);
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
				new FenetreErreur("Renseigné le nom du Pays !");
			}
			else {
				Connection connection = Bdd.connection;
				PreparedStatement query = connection.prepareStatement("INSERT INTO pays VALUES(?)");
				query.setString(1, jt1.getText());
				query.executeUpdate();
				Bdd.closeConnection();
				this.setVisible(false);
			}
		}
	    catch(SQLException e){
			if (e.getSQLState().equals("23505")) {
				new FenetreErreur("Ce Pays existe déjà dans la base de données");
			} 
			else{
				System.out.println(e.getSQLState());
				System.out.println(e.getMessage());
			}
	    }
    }

	public static void main(String[] args) {
		Bdd.createConnection();
		Pays f = new Pays();
	}

}
