

import java.io.FileNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Roster Loader Class
 */
public class RosterLoader implements IRosterLoader{

    // xml start node name
    private final String NODE_ROOT = "<root>";
    private final String NODE_ROW = "<row>";
    private final String NODE_NAME = "<name>";
    private final String NODE_JERSEY_NUMBER = "<jerseyNumber>";
    private final String NODE_YEAR = "<year>";

    // xml end node name
    private final String NODE_ROOT_E = "</root>";
    private final String NODE_ROW_E = "</row>";
    private final String NODE_NAME_E = "</name>";
    private final String NODE_JERSEY_NUMBER_E = "</jerseyNumber>";
    private final String NODE_YEAR_E = "</year>";

    /**
     * Parse the xml file and load the file content into the list
     * @param filepathToXML The path name of the xml file, which can be an absolute path or a relative path
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public List<Player> loadPlayers(String filepathToXML) throws FileNotFoundException {

        // Check if the file exists
        if (!Files.exists(Path.of(filepathToXML)))
            throw new FileNotFoundException();

        try {
            List<Player> playerList = new ArrayList<>();

            // read all file contents
            String strXml = Files.readString(Path.of(filepathToXML));

            // Get the content in the root of the xml file
            int rootStart = strXml.indexOf(NODE_ROOT, 0);
            int rootEnd = strXml.indexOf(NODE_ROOT_E, 0);

            // Determine whether the root start and end position is found, if it is -1, it means not found
            if ((rootStart == -1) || (rootEnd == -1)) {
                return playerList;
            }

            String rootXml = strXml.substring(rootStart + NODE_ROOT.length(), rootEnd);
            // Use row to split each group of data
            String[] rows = rootXml.split(NODE_ROW);

            // Loop to get each field, if the field acquisition fails, cancel this parsing
            for (String row : rows) {

                Player player = new Player();

                // Get the start and end position of the name
                int nameStart = row.indexOf(NODE_NAME);
                int nameEnd = row.indexOf(NODE_NAME_E);

                // Determine whether the start and end positions of the name are found
                if ((nameStart == -1) || (nameEnd == -1))
                    continue;

                // get the value of the field
                String name = row.substring(nameStart + NODE_NAME.length(), nameEnd);
                player.setName(name);

                // Get the start and end position of jerseyNumber
                int jerseyNumberStart = row.indexOf(NODE_JERSEY_NUMBER);
                int jerseyNumberEnd = row.indexOf(NODE_JERSEY_NUMBER_E);

                // Determine whether the start and end positions of jerseyNumber are found
                if ((jerseyNumberStart == -1) || (jerseyNumberEnd == -1))
                    continue;

                // get the value of the field
                String jerseyNumber = row.substring(jerseyNumberStart + NODE_JERSEY_NUMBER.length(), jerseyNumberEnd);
                player.setJerseyNumber(Integer.valueOf(jerseyNumber));

                // Get the start and end position of the year
                int yearStart = row.indexOf(NODE_YEAR);
                int yearEnd = row.indexOf(NODE_YEAR_E);

                // Determine whether the start and end positions of the year are found
                if ((yearStart == -1) || (yearEnd == -1))
                    continue;

                // get the value of the field
                String year = row.substring(yearStart + NODE_YEAR.length(), yearEnd);
                player.setYear(year);

                // add to List
                playerList.add(player);
            }

            return playerList;
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }
}

