package DataAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pbstr on 11/15/2017.
 */

public class ServerUtil {

    public static <T> List<T> shuffle(List<T> listToShuffle) {
        List<T> shuffledList = new ArrayList<T>(listToShuffle.size()); //initialize it to have a max space as the deck has
        for(int i = 0; i < listToShuffle.size(); i++) {
            shuffledList.add(listToShuffle.get(i));
        }
        Random random = new Random();
        //start at the top, and go down, swapping with random elements in the array
        for(int i = shuffledList.size()-1; i > 0; i--) {
            int loc = random.nextInt(i); //limiting factor is i, so we will never swap with elements that we have already swapped
            //swap toRet[i] with toRet[loc]
            T temp = shuffledList.get(i);
            shuffledList.set(i, shuffledList.get(loc));
            shuffledList.set(loc, temp);
        }
        return shuffledList;

    }
}
