package BuySell;

import android.widget.Button;
import android.widget.TextView;

import java.time.temporal.Temporal;

public class BuySellModel {
    int bookImage;
    String bookName;
    String sellerName;
    String contactNumber;



    public BuySellModel(int bookImage, String bookName, String sellerName, String contactNumber) {
        this.bookImage = bookImage;
        this.bookName = bookName;
        this.sellerName = sellerName;
        this.contactNumber = contactNumber;


    }

    public int getBookImage() {
        return bookImage;
    }

    public String getBookName() {
        return bookName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
