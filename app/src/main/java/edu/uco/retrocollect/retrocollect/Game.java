package edu.uco.retrocollect.retrocollect;


public class Game {
    private String gameTitle;
    private double gameReleaseYear;
    private String gameReleaseDate;
    private String gamePublisher;
    private String gameStudio;
    private double gameRating;

    //Added by Adam Bilby
    //Static Enumerations of Various API Features
    static enum ESRBRatings
    {
        ERROR, RP, EC, E, E10, T, M, AO
    }
    static enum PegiRatings
    {
        ERROR, Three, Seven, Twelve, Sixteen, Eighteen
    }
    static enum region
    {
        Error, EU, NA, AU, NZ, JP, CH, AS, WW
    }
    //Overloaded Constructor
    public Game(String title){
        this.gameTitle = title;
    }

    //Overloaded Constructor
    public Game(String title, Double releaseYear, String releaseDate){
        this.gameTitle = title;
        this.gameReleaseYear = releaseYear;
        this.gameReleaseDate = releaseDate;

    }

    //Overloaded Constructor
    public Game(String title, Double releaseYear, String releaseDate, String publisher, String studio){
        this.gameTitle = title;
        this.gameReleaseYear = releaseYear;
        this.gameReleaseDate = releaseDate;
        this.gamePublisher = publisher;
        this.gameStudio = studio;
    }

    //Overloaded Constructor
    public Game(String title, Double releaseYear, String releaseDate, String publisher, String studio, double rating){
        this.gameTitle = title;
        this.gameReleaseYear = releaseYear;
        this.gameReleaseDate = releaseDate;
        this.gamePublisher = publisher;
        this.gameStudio = studio;
        this.gameRating = rating;
    }



    @Override
    public String toString() {
        return gameTitle;
    }

    public String getTitle() {
        return gameTitle;
    }

    public void setTitle(String title) {
        this.gameTitle = title;
    }

    public double getReleaseYear() {
        return gameReleaseYear;
    }

    public void setReleaseYear(double releaseYear) {
        this.gameReleaseYear = releaseYear;
    }

    public String getReleaseDate() {
        return gameReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.gameReleaseDate = releaseDate;
    }

    public String getPublisher() {
        return gamePublisher;
    }

    public void setPublisher(String publisher) {
        this.gamePublisher = publisher;
    }

    public String getStudio() {
        return gameStudio;
    }

    public void setStudio(String studio) {
        this.gameStudio = studio;
    }

    public double getRating() {
        return gameRating;
    }

    public void setRating(double rating) {
        this.gameRating = rating;
    }

}