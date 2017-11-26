
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
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

    private static Set<Entry<Character, Entry<Integer, Integer>>> contested;

    private static Set<Entry<Character, Entry<Integer, Integer>>> starting_points;

    private static Set<Entry<Character, Entry<Integer, Integer>>> duplicate;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        String[] input_array = readInput(INPUT_FILE_NAME);
        List<char[][]> case_list = getCases(input_array);
        for (int i = 0; i < case_list.size(); i++)
        {
            System.out.println("Case " + (i + 1) + ":");

            control_map = new TreeMap<Character, Integer>();
            contested = new HashSet<Entry<Character, Entry<Integer, Integer>>>();
            starting_points = getStartingPoints(case_list.get(i));
            duplicate = new HashSet<Entry<Character, Entry<Integer, Integer>>>();

            for (Entry<Character, Entry<Integer, Integer>> j : starting_points)
            {
                if (!duplicate.contains(j) && !contested.contains(j))
                {
                    if (conquer(j.getKey(), case_list.get(i), j.getValue().getKey(), j.getValue().getValue(), true, j.getValue().getKey(), j.getValue().getValue()))
                    {
                        control_map.put(j.getKey(), control_map.get(j.getKey()) + 1);
                    }
                }
            }

            for (char j : control_map.keySet())
            {
                if (control_map.get(j) > 0)
                {
                    System.out.println(j + " " + control_map.get(j));
                }
            }
            //if (contested.size() > 0)
            //{
            System.out.println("contested " + contested.size());
            //}
        }

    }

    private static void checkDuplicate(char faction, int row, int column, int origin_row, int origin_column)
    {

        for (Entry<Character, Entry<Integer, Integer>> j : starting_points)
        {


            if (j.getKey().equals(faction)
                    && j.getValue().getKey().equals(row)
                    && j.getValue().getValue().equals(column)
                    && !(j.getValue().getKey().equals(origin_row)
                    && j.getValue().getValue().equals(origin_column)))
            {

                duplicate.add(new SimpleEntry(faction, new SimpleEntry(row, column)));
            }
        }
    }

    /**
     *
     * @param faction
     * @param grid
     * @param row
     * @param column
     * @param isStart
     * @return
     */
    private static boolean conquer(char faction, char[][] grid, int row, int column, boolean isStart, int origin_row, int origin_column)
    {

        checkDuplicate(faction, row, column, origin_row, origin_column);

        if (row < 0 || column < 0 || row >= grid.length || column >= grid[row].length)
        {
            return true;
        }
        else if (grid[row][column] != faction && grid[row][column] != EMPTY_LAND && grid[row][column] != MOUNTAIN)
        {
            contested.add(new SimpleEntry(grid[row][column], new SimpleEntry(row, column)));
            return false;
        }
        else if (grid[row][column] == EMPTY_LAND || isStart)
        {
            grid[row][column] = faction;
            return conquer(faction, grid, row, column + 1, false, origin_row, origin_column)
                    && conquer(faction, grid, row, column - 1, false, origin_row, origin_column)
                    && conquer(faction, grid, row + 1, column, false, origin_row, origin_column)
                    && conquer(faction, grid, row - 1, column, false, origin_row, origin_column);
        }
        else
        {
            return true;
        }
    }

    /**
     *
     * @param grid
     * @return
     */
    private static Set<Entry<Character, Entry<Integer, Integer>>> getStartingPoints(char[][] grid)
    {
        Set<Entry<Character, Entry<Integer, Integer>>> starting_points = new HashSet();
        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                if (grid[i][j] != EMPTY_LAND && grid[i][j] != MOUNTAIN)
                {
                    starting_points.add(new SimpleEntry(grid[i][j], new SimpleEntry(i, j)));
                    control_map.put(grid[i][j], 0);
                }
            }
        }
        return starting_points;
    }

    /**
     *
     * @param input_array
     * @return
     */
    private static List<char[][]> getCases(String[] input_array)
    {
        List<char[][]> case_list = new ArrayList();
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
