import ch.unibas.landolt.balduin.stemmatomat.src.mainApplication.StemmatomatMain;

/**
 * A Runner Class to run stemmat-o-mat Version 0.01.
 * 
 * @author Balduin Landolt
 *
 */
public class Stemmatomat_V0_01 {

    /**
     * Creates and runs an Instance of StemmatomatMain.
     * 
     * @param args ignored
     */
    public static void main(String[] args){
        System.out.println("Console Only: Program Launched.");

        StemmatomatMain app = new StemmatomatMain();
        app.run();
        

        System.out.println("Launcher Finished Successfully.");
    }

}
