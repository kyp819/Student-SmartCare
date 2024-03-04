package CarCardView;


public class CarCardModel {
    int carImage;
    String ownerName;
    String carModel;
    String carNumber;

    public CarCardModel(String ownerName, String carModel, String carNumber, int carImage) {
        this.carImage = carImage;
        this.ownerName = ownerName;
        this.carModel = carModel;
        this.carNumber = carNumber;
    }

    public int getCarImage() {
        return carImage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarNumber() {
        return carNumber;
    }
}
