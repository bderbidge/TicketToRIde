package Communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Queue;

import Command.Command;
import Command.ICommand;

/**
 * Created by emilyprigmore on 9/30/17.
 */

public class EncoderDecoder {

    public EncoderDecoder() {
    }

    /*
        This method takes in a generic object and serializes its contents to a String.
     */
    public static Object decode(Object o, String j) {
        o = new Gson().fromJson(j, o.getClass());
        return o;
    }

    /*
        This method takes in a String and a generic object and then deserializes the String into
        the given object. The caller of this method should give an instance of a more specific
        object to deserialize the object to.
    */
    public static String encode(Object o, String j) {
        j = new Gson().toJson(o, o.getClass());
        return j;
    }

    public static CommandList decodeQueue(CommandList  list, String j){
        CommandList list1 = (CommandList) new Gson().fromJson(j, list.getClass());
        return list1;
    }

    public static String encodeList(List<Command> o, String j) {
        j = new Gson().toJson(o, o.getClass());
        return j;
    }

}
