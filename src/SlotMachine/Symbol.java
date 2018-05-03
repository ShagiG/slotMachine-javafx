package SlotMachine;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//this class implement the methods in the ISymbol
public class Symbol implements ISymbol {


    //declaring the private variables for the class

    // stores the path of symbol image
    private Image image;

    //value specific for the symbol
    private int value;


    //constructor for the type symbol
    public Symbol(Image image, int value) {
        this.image = image;
        this.value = value;
    }


    //overrided image to set the image associated with one of the symbols
    @Override
    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public void setValue(int v) {
        value = v;

    }

    @Override
    public int getValue() {
        return this.value;
    }
}


