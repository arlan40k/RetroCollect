package edu.uco.retrocollect.retrocollect;


public class Game {
    private String gameTitle;
    private String gameId;
    private double gameReleaseYear;
    private String gameReleaseDate;
    private String gamePublisher;
    private String gameStudio;
    private double gameRating;
    private String coverHash; //cover hash added -HASEEB

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

    public Game(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public Game(String gameTitle, String gameId) {
        this.gameTitle = gameTitle;
        this.gameId = gameId;
    }

    public Game(String gameTitle, String gameId, double gameReleaseYear) {
        this.gameTitle = gameTitle;
        this.gameId = gameId;
        this.gameReleaseYear = gameReleaseYear;
    }

    public Game(String gameTitle, String gameId, double gameReleaseYear, String gameReleaseDate) {
        this.gameTitle = gameTitle;
        this.gameId = gameId;
        this.gameReleaseYear = gameReleaseYear;
        this.gameReleaseDate = gameReleaseDate;
    }

    public Game(String gameTitle, String gameId, double gameReleaseYear, String gameReleaseDate, String gamePublisher) {
        this.gameTitle = gameTitle;
        this.gameId = gameId;
        this.gameReleaseYear = gameReleaseYear;
        this.gameReleaseDate = gameReleaseDate;
        this.gamePublisher = gamePublisher;
    }

    public Game(String gameTitle, String gameId, double gameReleaseYear, String gameReleaseDate, String gamePublisher, String gameStudio) {
        this.gameTitle = gameTitle;
        this.gameId = gameId;
        this.gameReleaseYear = gameReleaseYear;
        this.gameReleaseDate = gameReleaseDate;
        this.gamePublisher = gamePublisher;
        this.gameStudio = gameStudio;
    }

    public Game(String gameTitle, String gameId, double gameReleaseYear, String gameReleaseDate, String gamePublisher, String gameStudio, double gameRating) {
        this.gameTitle = gameTitle;
        this.gameId = gameId;
        this.gameReleaseYear = gameReleaseYear;
        this.gameReleaseDate = gameReleaseDate;
        this.gamePublisher = gamePublisher;
        this.gameStudio = gameStudio;
        this.gameRating = gameRating;

    }

    public Game(String gameTitle, String gameId, double gameReleaseYear, String gameReleaseDate,
                String gamePublisher, String gameStudio, double gameRating, String coverHash) {
        this.gameTitle = gameTitle;
        this.gameId = gameId;
        this.gameReleaseYear = gameReleaseYear;
        this.gameReleaseDate = gameReleaseDate;
        this.gamePublisher = gamePublisher;
        this.gameStudio = gameStudio;
        this.gameRating = gameRating;
        this.coverHash = coverHash;
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
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    public String getCoverHash() {return coverHash;} //added method - HASEEB

    public void setCoverHash(String coverHash) {this.coverHash = coverHash;}

}