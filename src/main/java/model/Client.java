package model;

public class Client {
    private int id;
    private String nom;
    private String entreprise;
    private String contact;
    private String email;
    private String telephone;
    private String adresse;
    private String dateCreation;

    // Constructeurs
    public Client() {}

    public Client(String nom, String entreprise, String contact, String email) {
        this.nom = nom;
        this.entreprise = entreprise;
        this.contact = contact;
        this.email = email;
    }

    public Client(int id, String nom, String entreprise, String contact, String email, 
                  String telephone, String adresse, String dateCreation) {
        this.id = id;
        this.nom = nom;
        this.entreprise = entreprise;
        this.contact = contact;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEntreprise() { return entreprise; }
    public void setEntreprise(String entreprise) { this.entreprise = entreprise; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }

    @Override
    public String toString() {
        return nom + " - " + entreprise;
    }
} 