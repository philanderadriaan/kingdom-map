
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * <p>
 * You are presented with a map of a kingdom. Empty land on this map is depicted
 * as ‘.’ (without the quotes), and mountains are depicted by ‘#’. This kingdom
 * has factions whose armies are represented by lowercase letters in the map.
 * Two armies of the same letter belong to the same faction.
 * <p>
 * Armies can go up, down, left, and right, but cannot travel through mountains.
 * A region is an area enclosed by mountains. From this it can be deduced that
 * armies cannot travel between different regions. A region is said to be
 * controlled by a faction if it’s the only faction with an army in that region.
 * If there are at least two armies from different factions in a region, then
 * that region is said to be contested. Your task is to determine how many
 * regions each faction controls and how many contested regions there are.
 * <p>
 * Input:
 * <p>
 * The first line is the number of test cases T. Each test case will have two
 * numbers N and M, each on their own line given in that order. Following that
 * is N lines of M characters that represent the map.
 * <p>
 * Output:
 * <p>
 * For each test case, output one line of the form “Case C:” (without the
 * quotes), where C is the case number (starting from 1). On the following
 * lines, output the factions that appear in the grid in alphabetical order if
 * the faction controls at least one region and how many regions that letter
 * controls. Factions that don’t control any regions should not be in the
 * output. After this, print one last line of the form “contested K” where K is
 * the number of regions that contain at least two distinct letters.
 * <p>
 * See the sample output below for details.
 * <p>
 * Constraints:
 * <p>
 * 1 ≤ T ≤ 100 1 ≤ N ≤ 100 1 ≤ M ≤ 100
 *
 * @author Phil Adriaan <philander90@msn.com>
 */
public class Main
{

    private static final String INPUT_FILE_NAME = "input.in";

    private static final char EMPTY_LAND = '.';

    private static final char MOUNTAIN = '#';

    private static Map<Character, Integer> control_map;

    private static int contested = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        String[] input_array = readInput(INPUT_FILE_NAME);
        List<char[][]> case_list = getCases(input_array);
        for (int i = 0; i < case_list.size(); i++)
        {

            System.out.println();
            System.out.println("CASE " + (i + 1));
            System.out.println();

            for (char[] j : case_list.get(i))
            {
                System.out.println(Arrays.toString(j));
            }
            System.out.println();

            control_map = new TreeMap();
            Set<Entry<Character, Entry<Integer, Integer>>> starting_points = getStartingPoints(case_list.get(i));

            for (Entry j : control_map.entrySet())
            {
                System.out.println(j);
            }
            System.out.println();

            for (Entry<Character, Entry<Integer, Integer>> j : starting_points)
            {
                System.out.println(j.getKey() + " " + j.getValue().getKey() + ',' + j.getValue().getValue());
            }
            System.out.println();

            for (char[] j : case_list.get(i))
            {
                System.out.println(Arrays.toString(j));
            }
            System.out.println();
        }
    }

    private static Set<Entry<Character, Entry<Integer, Integer>>> getStartingPoints(char[][] grid)
    {
        Set<Entry<Character, Entry<Integer, Integer>>> starting_points = new HashSet();
        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                if (grid[i][j] != EMPTY_LAND && grid[i][j] != MOUNTAIN)
                {
                    starting_points.add(new SimpleEntry<Character, Entry<Integer, Integer>>(grid[i][j], new SimpleEntry<Integer, Integer>(i, j)));
                    control_map.put(grid[i][j], 0);
                }
            }
        }
        return starting_points;
    }

    private static List<char[][]> getCases(String[] input_array)
    {
        List<char[][]> case_list = new ArrayList<char[][]>();
        int i = 1;
        while (i < input_array.length)
        {
            int row = Integer.parseInt(input_array[i]);
            i++;
            int column = Integer.parseInt(input_array[i]);
            i++;
            char[][] grid = new char[row][column];
            for (int j = 0; j < row; j++)
            {
                grid[j] = input_array[i].toCharArray();
                i++;
            }
            case_list.add(grid);
        }

        return case_list;
    }

    /**
     * Inputs the file into raw data.
     *
     * @param file_name Name of input file.
     *
     * @return Raw string data.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static String[] readInput(String file_name) throws FileNotFoundException, IOException
    {
        BufferedReader buffered_reader = new BufferedReader(new FileReader(new File(file_name)));
        List<String> input_list = new ArrayList();
        String line = buffered_reader.readLine();
        while (line != null)
        {
            input_list.add(line);
            line = buffered_reader.readLine();
        }
        return input_list.toArray(new String[input_list.size()]);
    }

}
