import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Athlete extends JFrame{
	
	private JLabel l1, l2, l3, l4;	
    private JTextField nomAthlete, prenomAthlete;
    private JComboBox<String> categorie, pays;
	private JButton b;
	
	public Athlete(){
		super("Ajout d'un athlete");
		this.setLayout(new BorderLayout());
		JPanel p1 = new JPanel(new GridLayout(4,1));
		JPanel p2 = new JPanel(new GridLayout(4,1));
        this.l1 = new JLabel("nom de l'athlete :");
        this.l2 = new JLabel("prenom de l'athlete :");
		this.l3 = new JLabel("categorie : ");
		this.l4 = new JLabel("pays : ");

        this.nomAthlete = new JTextField();
        this.prenomAthlete = new JTextField();
		this.categorie = new JComboBox<String>(new Vector<String>(Arrays.asList("","HOMME","DAME")));
		this.pays = new JComboBox<String>(getPays());
		this.b = new JButton("envoyer");
		
        p1.add(l1);
		p1.add(l2);
		p1.add(l3);
		p1.add(l4);

		p2.add(nomAthlete);
		p2.add(prenomAthlete);
		p2.add(categorie);
		p2.add(pays);
		
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
			if (nomAthlete.getText().trim().equals("")) {
				new FenetreErreur("Renseigné le nom de l'athlete !");
			}
			else if(prenomAthlete.getText().trim().equals("")) {
				new FenetreErreur("Renseigné le prenom de l'athlete !");
			}
			else {
				Connection connection = Bdd.connection;
				PreparedStatement query = connection.prepareStatement("INSERT INTO athlete VALUES(nextval('cleathlete'),?,?,?,?)");
				query.setString(1, nomAthlete.getText());
				query.setString(2, prenomAthlete.getText());
				query.setString(3, (String) categorie.getSelectedItem());
				query.setString(4, (String) pays.getSelectedItem());
				query.executeUpdate();
				Bdd.closeConnection();
				this.setVisible(false);
			}
		}
	    catch(SQLException e){
			if (e.getSQLState().equals("23505")) {
				new FenetreErreur("Cet athlete existe déjà dans la base de données");
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
        Athlete f = new Athlete();
	}

}
