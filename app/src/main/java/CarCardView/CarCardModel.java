package CarCardView;


public class CarCardModel {




    String carImage;
    String ownerName;
    String route;

    String carModel;

    public CarCardModel() {
    }


    public CarCardModel(String ownerName, String carModel, String route, String carImage) {
        this.carImage = carImage;
        this.ownerName = ownerName;
        this.carModel = carModel;
        this.route = route;
    }



    public String getCarImage() {
        return carImage;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public String getRoute() {
        return route;
    }


    public String getCarModel() {
        return carModel;
    }



    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public void setRoute(String route) {
        this.route = route;
    }


    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setCarImage(String carImage) {this.carImage = carImage;
    }
}
