package com.example.keresoro.madass;



public class ListRestaurants {

    String Restname, Restaddress, Restcusine;
    int Restrating;
    Double latitude, longitude;

    public ListRestaurants (String RestnameIn, String RestaddressIn, String RestcusineIn, int RestratingIn, Double latitudeIn, Double longitudeIn) {

        Restname = RestnameIn;
        Restaddress = RestaddressIn;
        Restcusine = RestcusineIn;
        Restrating = RestratingIn;
        latitude = latitudeIn;
        longitude = longitudeIn;
    }

    public String getRestname() {return Restname;}
    public String getRestaddress() {return Restaddress;}
    public String getRestcusine() {return Restcusine;}
    public int getRestrating() {return Restrating;}
    public double getlatitude() {return latitude;}
    public double getlongitude() {return longitude;}

    @Override

    public String toString() {
        return Restname + "," + Restaddress + "," + Restcusine + "," + Restrating + "," + latitude + "," + longitude + ",";
    }
}
