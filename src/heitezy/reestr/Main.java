package heitezy.reestr;

import java.io.IOException;

class Main {

    public static void main (String [] args) throws IOException {
        boolean silence = false;
        if (args.length != 0) {
            if (args[0].equals("-s")) {
            silence = true;
            }
        }
        new mainUI(silence);
    }
}
