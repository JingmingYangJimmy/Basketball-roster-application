import java.io.FileNotFoundException;
import java.util.List;
/**
 * Instances of this interface can be used to load roster data.
 */
public interface IRosterLoader {

    /**
     *This method loads the player's data on the roster.
     * @param filepathToXML
     * @return roster data with players' information
     * @throws FileNotFoundException
     */
    public List<Player> loadPlayers(String filepathToXML) throws FileNotFoundException;


}
