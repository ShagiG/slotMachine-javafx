package SlotMachine;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//class which represents a single reel in the slot machine

public class Reel extends Thread {


    private ImageView reelImage;
    private int symValue;

    //assigning the paths for the symbol images
    Symbol seven = new Symbol(new Image("/images/redseven.png"), 7);
    Symbol bell = new Symbol(new Image("/images/bell.png"), 6);
    Symbol watermelon = new Symbol(new Image("images/watermelon.png"), 5);
    Symbol plum = new Symbol(new Image("/images/plum.png"), 4);
    Symbol lemon = new Symbol(new Image("/images/lemon.png"), 3);
    Symbol cherry = new Symbol(new Image("/images/cherry.png"), 2);

    //array of the symbols representing a reel
    private ArrayList<Symbol> symbols = new ArrayList<>();


    public Reel(ImageView reelImage) {
        this.reelImage = reelImage;
    }

    // swapping the arrays
    public ArrayList<Symbol> spin() {
        symbols.add(seven);
        symbols.add(bell);
        symbols.add(watermelon);
        symbols.add(plum);
        symbols.add(lemon);
        symbols.add(cherry);


        //shuffling the arraylist
        Collections.shuffle(symbols);

        //returning the shuffled array
        return symbols;

    }


    public int getSymValue() {

        return symValue;
    }

    @Override
    public void run() {
        /*
         *@return slotreels array
         */
        spin();

        while (true) {
            for (int i = 0; i < symbols.size(); i++) {
                synchronized (Reel.class) {
                    reelImage.setImage(symbols.get(i).getImage());
                    symValue = symbols.get(i).getValue();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
