package HomeCardView;

public class HomeCardModel {
    int homeImage;
    String ownerName;
    String location;
    String contactNumber;

    public HomeCardModel(String ownerName, String location, String contactNumber, int homeImage) {
        this.homeImage = homeImage;
        this.ownerName = ownerName;
        this.location = location;
        this.contactNumber = contactNumber;
    }


    public int getHomeImage() {
        return homeImage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getLocation() {
        return location;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
