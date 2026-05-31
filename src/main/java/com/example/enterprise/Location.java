package com.example.enterprise;

public class Location {

    private Long id;
    private String naam;
    private String adres;
    private int capaciteit;

    public Location() {}

    public Location(Long id, String naam, String adres, int capaciteit) {
        this.id = id;
        this.naam = naam;
        this.adres = adres;
        this.capaciteit = capaciteit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNaam() { return naam; }
    public void setNaam(String naam) { this.naam = naam; }

    public String getAdres() { return adres; }
    public void setAdres(String adres) { this.adres = adres; }

    public int getCapaciteit() { return capaciteit; }
    public void setCapaciteit(int capaciteit) { this.capaciteit = capaciteit; }
}