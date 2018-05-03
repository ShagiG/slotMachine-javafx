package SlotMachine;

import javafx.scene.image.Image;

public interface ISymbol {

    //set the image associated with one of the symbols
    public void setImage(Image image);


    //return the image
    public Image getImage();


    //set the value of the symbol
    public void setValue(int v);


    //returns the value of the symbol
    public int getValue();

}
