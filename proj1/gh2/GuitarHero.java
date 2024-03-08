package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;

public class GuitarHero {

    public static void main(String[] args) {
        GuitarString[] guitar = new GuitarString[37];
        for (int i = 0; i < guitar.length; i++) {
            guitar[i] = new GuitarString(440 * Math.pow(2, (i - 24) / 12));
        }
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (keyboard.indexOf(key) == -1) {
                    continue;
                }
                guitar[keyboard.indexOf(key)].pluck();
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (GuitarString string : guitar) {
                sample += string.sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString string : guitar) {
                string.tic();
            }
        }
    }
}
