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

public class Participation extends JFrame{
	
	private JLabel l1, l2, l3, l4;	
    private JTextField resultat;
    private JComboBox<String> athlete, specialite, lieu;
	private JButton b;
	
	public Participation(){
		super("Ajout d'une participation");
		this.setLayout(new BorderLayout());
		JPanel p1 = new JPanel(new GridLayout(4,1));
		JPanel p2 = new JPanel(new GridLayout(4,1));
        this.l1 = new JLabel("selectionné l'athlete :");
        this.l2 = new JLabel("selectionné la spécialité :");
		this.l3 = new JLabel("selectionné le lieu : ");
		this.l4 = new JLabel("résultat (numéro de la place) : ");

        this.athlete = new JComboBox<String>(new Vector<String>(getAthlete()));
        this.specialite = new JComboBox<String>(new Vector<String>(getSpecialite()));
		this.lieu = new JComboBox<String>(new Vector<String>(getLieu()));
		this.resultat = new JTextField();
		this.b = new JButton("envoyer");
		
        p1.add(l1);
		p1.add(l2);
		p1.add(l3);
		p1.add(l4);

		p2.add(athlete);
		p2.add(specialite);
		p2.add(lieu);
		p2.add(resultat);
		
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
            if (resultat.getText().trim().equals("")) {
                new FenetreErreur("Renseigné la place de l'athlete !");
			}
			else {
                int place_de_athlete = Integer.parseInt(resultat.getText());
                int no_athelte = getNumeroAthlete((String) athlete.getSelectedItem());

                Connection connection = Bdd.connection;
				PreparedStatement query = connection.prepareStatement("INSERT INTO participation VALUES(?,?,?,?)");
				query.setInt(1, no_athelte);
				query.setString(2, (String) specialite.getSelectedItem());
				query.setString(3, (String) lieu.getSelectedItem());
				query.setInt(4,  place_de_athlete);
				query.executeUpdate();
				Bdd.closeConnection();
				this.setVisible(false);
			}
		}
	    catch(SQLException e){
			if (e.getSQLState().equals("23505")) {
				new FenetreErreur("Cette participation existe déjà dans la base de données");
			} 
			else{
				System.out.println(e.getSQLState());
				System.out.println(e.getMessage());
			}
	    }
        catch(NumberFormatException e){
				new FenetreErreur("Le résultat(numéro de la place) doit être un entier !");
        }
    }

    private Vector<String> getAthlete(){
        Vector<String> vector = new Vector<>();
        Connection connection = Bdd.connection;
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT nomathlete, prenomathlete FROM athlete");
            while (result.next()) {
                vector.add(result.getString(1)+" "+result.getString(2));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return vector;        
    }

    private Vector<String> getSpecialite(){
        Vector<String> vector = new Vector<>();
        Connection connection = Bdd.connection;
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM specialite");
            while (result.next()) {
                vector.add(result.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return vector;        
    }

    private Vector<String> getLieu(){
        Vector<String> vector = new Vector<>();
        Connection connection = Bdd.connection;
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT nomlieu FROM lieu");
            while (result.next()) {
                vector.add(result.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return vector;        
    }

    private int getNumeroAthlete(String nom_prenom_athlete){
        int place_de_athlete = 0;
        String nom = nom_prenom_athlete.split(" ")[0];
        String prenom = nom_prenom_athlete.split(" ")[1];

        Connection connection = Bdd.connection;
        try {
            PreparedStatement query = connection.prepareStatement("SELECT noathlete FROM athlete WHERE nomathlete = ? AND prenomathlete = ?");
            query.setString(1, nom);
            query.setString(2, prenom);
            ResultSet result = query.executeQuery();
            result.next();
            place_de_athlete = result.getInt(1);
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return place_de_athlete;
    }

	public static void main(String[] args) {
		Bdd.createConnection();
        Participation f = new Participation();
        
	}

}
