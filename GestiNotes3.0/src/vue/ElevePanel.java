package vue;

/**
 * Crée 2016-10-31,09:38:32
 *
 * @author Raphael Duchaine
 */
import Réutilisable.UtileFrame;
import modele.Evaluation;
import modele.Etablissement;
import modele.Eleve;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public abstract class ElevePanel extends GestiNotesPanel {
    //variables
    JProgressBar pBar;
    
    String texteBouton="(\"Verbe\") un eleve";
    //Méthodes
    //Constructeur
    public ElevePanel(UtileFrame fenetre,JProgressBar p_pBar) {
        super(fenetre);
        this.pBar = p_pBar;
        pBar.setStringPainted(true);
        pBar.setString(pBar.getValue() + "/10");
        GridLayout gl = new GridLayout(10, 1, 0, 25);	//Cree GridLayout
        simplePanel.setLayout(gl);
        addChamp("Nom","Nom de famille de l'élève");
        addEspace();
        addChamp("Prenom","Prénom de l'élève");
        addEspace();
        addChamp("Date (JJ-MM-AAAA)","<html>Date de naissance de l'élève<p>" +"Écrire en chiffre sous ce format : JJ-MM-AAAA"+"</p>(les tirets inclus)</html>");
        addEspace();
        addChamp("Code Permanent","<html>Code Permanent de l'élève<p>" +"Est sous ce format :"+"</p>"+"Première lettre du Nom puis du prénom puis l'année de naissance</html>");
        addEspace();
        getLastChamp().setEditable(false);
        //Appel de la méthode créant les champs pour chaque notes selon NBR_NOTES de Etablissement
        addChampsNote(true);
        addBouton(texteBouton);
        //Un bouton "Clear"
        addBouton("Vider les champs");
        addEspace();
        addLabel("Eleve(s) dans le groupe: ");       
        simplePanel.add(pBar);
        updatePbar();
        
    }

    public ElevePanel() {
        this(null,null);
    }
    //Autres Méthodes
    
    @Override
    public void actionPerformed(ActionEvent event) {  // Methode recoit evenement

        if (((JButton) event.getSource()).getText() == texteBouton) {
            bouton0Presse();
            revalidate();
        }
        //Un bouton qui vide tous les champs
        if (((JButton) event.getSource()).getText() == "Vider les champs") {
            viderChamps();
        }

    }

    public void updatePbar() throws HeadlessException {
        try {
            pBar.setValue(Etablissement.getLastGroupe().getTabEleve().size());
            pBar.setString(pBar.getValue() + "/10");
        } catch (Exception e) {
            if (!e.getMessage().equals("-1"))
                messageErreur(e);
            else{
                pBar.setValue(0);
                Etablissement.addGroupe();
            }
        }
    }

    abstract void bouton0Presse();
    
     void afficherEleve(){
            String codePermanent = JOptionPane.showInputDialog(null,
                    "Entrer le code permanent de l'eleve:",
                    "Afficher un Eleve", JOptionPane.QUESTION_MESSAGE);
            afficherEleve(codePermanent);
    }
    
    boolean afficherEleve(String codePermanent) {
        Eleve eleve;
        try {
            eleve = Etablissement.searchEleve(codePermanent);
            if (eleve.equals(null)) {
                throw new NullPointerException("Code incorrect");
            }
            setChamp(0, eleve.getNom());
            setChamp(1, eleve.getPrenom());
            setChamp(2, eleve.getDateNaissance());
            setChamp(3, codePermanent);
            for (int i = 0; i < Etablissement.NBR_NOTES; i++) {
                setChamp(i + 4, eleve.getNote(i));
            }
            return true;
        } catch (NullPointerException e) {
            messageErreur(new Exception("Eleve introuvable"));
            return false;
        }
        catch (Exception e) {
            messageErreur(e);
            return false;
        }
    }
    void notification(String texte) {
        JOptionPane.showMessageDialog(null, texte, "Operation Effectué", JOptionPane.INFORMATION_MESSAGE);
    }
}

    
