package vue;

/**
 * Crée 2016-10-31,09:38:32
 *
 * @author Raphael Duchaine
 */
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
import javax.swing.JTextField;

public class ElevePanel extends UtilePanel {

    private UtilePanel gestionPanel;

    //variables
    final int NBR_NOTES = 4;
    UtileFrame fenetre;

    //Méthodes
    //Constructeur
    public ElevePanel(UtileFrame fenetre, UtilePanel gestionPanel) {
        super(fenetre);
        this.gestionPanel = gestionPanel;
        GridLayout gl = new GridLayout(13, 6, 0, 25);	//Cree GridLayout
        simplePanel.setLayout(gl);
        addChamp("Nom");
        addChamp("Prenom");
        addChamp("Date (JJ-MM-AAAA)");
        addChamp("Code Permanent");
        getLastChamp().setEditable(false);
        for (int i = 0; i < NBR_NOTES; i++) {
            addChamp("Note" + (i + 1));
            //if((i%2)!=0)addEspace();
        }
        addBouton("Enregistrer un eleve");
        addBouton("Afficher un eleve");
        addBouton("Modifier un eleve");
        //Un bouton "Clear"
        addBouton("Vider les champs");
    }

    public ElevePanel() {
        this(null, null);
    }
    //Get-Set
    //toString
    //Autres Méthodes

    @Override
    public void actionPerformed(ActionEvent event) {  // Methode recoit evenement

        if (((JButton) event.getSource()).getText() == "Enregistrer un eleve") {
            addEleve();
            revalidate();
        }
        if (((JButton) event.getSource()).getText() == "Afficher un eleve") {
            afficherEleve();
        }
        if (((JButton) event.getSource()).getText() == "Modifier un eleve") {
            modifierEleve();
            revalidate();
        }
        //Un bouton qui vide tous les champs
        if (((JButton) event.getSource()).getText() == "Vider les champs") {
            viderChamps();
        }

    }

    private void addEleve() {
        try {
            //Traitement de champ vide
            for (int i = 0; i < 2; i++) {
                if (champs.get(i).getText().equals("")) {
                    throw new Exception("Donnée introuvable");
                }
            }
            if (champs.get(2).getText().length() != 10) {
                throw new Exception("Format incorrect \n format requis: \"JJ-MM-AAAA\"");
            }
            Eleve eleve = new Eleve(champs.get(0).getText(), champs.get(1).getText(), champs.get(2).getText());
            Etablissement.addEleve(eleve);
            afficherEleve(eleve.codePermanent());
            notification("Enregistrement effectue");
            //Afficher l'élève créé
            //Enregistrer des notes
        } catch (Exception e) {
            messageErreur(e);
        }
        refreshComboBoxes();
    }

    private void afficherEleve() {
        String codePermanent = JOptionPane.showInputDialog(null,
                "Entrer le code permanent de l'eleve:",
                "Afficher un Eleve", JOptionPane.QUESTION_MESSAGE);
        if (afficherEleve(codePermanent)) {
            notification("Affichage effectue");
        }
    }

    private boolean afficherEleve(String codePermanent) {
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
            for (int i = 0; i < NBR_NOTES; i++) {
                setChamp(i + 4, eleve.getNote(i));
            }
            return true;
        } catch (NullPointerException e) {
            messageErreur(new Exception("Eleve introuvable"));
            return false;
        } catch (Exception e) {
            messageErreur(e);
            return false;
        }
    }

    private void modifierEleve() {
        Eleve eleve;
        String codePermanent;
        if (getChamp(3).getText().equals("")) {
            afficherEleve();
            return;
        } else {
            codePermanent = getChamp(3).getText();
        }
        try {
            eleve = Etablissement.searchEleve(codePermanent);
            if (eleve.equals(null)) {
                throw new NullPointerException("Code incorrect");
            }
            eleve.setNom(getChamp(0).getText());
            eleve.setPrenom(getChamp(1).getText());
            eleve.setDateNaissance(getChamp(2).getText());
            for (int i = 0; i < NBR_NOTES; i++) {
                eleve.setNote(i,Float.parseFloat(getChamp(i + 4).getText()));
            }
            notification("Modification effectue");

        } catch (NullPointerException e) {
            messageErreur(e);
        }

    }

    private void notification(String texte) {
        JOptionPane.showMessageDialog(null, texte, "Operation Effectué", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viderChamps() {
        for (JTextField champ : champs) {
            champ.setText("");
        }
    }

    public void refreshComboBoxes() {
        gestionPanel.refreshComboBoxes(Etablissement.getTabGroupe());
    }
}