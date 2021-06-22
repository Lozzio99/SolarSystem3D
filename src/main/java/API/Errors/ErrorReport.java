package API.Errors;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Double.parseDouble;
import static API.Config.*;
import static phase3.Main.simulation;
import static API.Errors.ErrorPrint.*;

public class ErrorReport implements Runnable {

    public final static String[] solvers = new String[]{"E", "Rk", "Vs", "Vv", "M"};

    static {
        if (ERROR_EVALUATION) {
            parseOriginal();
        }
    }

    public final int MONTH_INDEX;
    /**
     * The constant monthIndex.
     */
    private final ErrorData state;
    private Writer fileWriter;
    private List<Vector3dInterface> absPosition, absVelocity;
    private List<Vector3dInterface> relPosition, relVelocity;

    public ErrorReport() {
        MONTH_INDEX = -1;
        state = null;
        absPosition = null;
        absVelocity = null;
    }


    /**
     * Instantiates a new Error report.
     *
     * @param state the state
     */
    @Contract(pure = true)
    public ErrorReport(final ErrorData state, int index) {
        //make a copy of the references
        this.state = state;
        MONTH_INDEX = index;
    }

    private static File extractDir(String file) {
        String path = "src\\main\\resources\\ErrorData";
        path += OUT_ERROR.equals(CSV) ? "\\CSV" : "\\TXT";
        path += "\\" + solvers[SOLVER] + "\\" + ((int) SS_STEP_SIZE);
        File dir = new File(path);
        if (dir.mkdirs() || dir.exists()) {
            return new File(path + "\\" + file);
        }
        return null;
    }

    /* Making csv from txt file  + creating error data array */
    public static void parseOriginal() {
        FileReader fr;
        try {
            File csvFile = new File("src/main/resources/ErrorData/ORIGINAL_MONTHS.csv");
            fr = new FileReader(csvFile);
            Scanner scan = new Scanner(fr);
            int monthIndex = 0;
            scan.nextLine();
            while (scan.hasNextLine()) {
                List<Vector3dInterface> pos = new ArrayList<>(11), vel = new ArrayList<>(11);
                for (int i = 0; i < 11; i++) {
                    String[] line = scan.nextLine().split(",");
                    pos.add(parseVector(line, true));
                    vel.add(parseVector(line, false));
                }
                ORIGINAL_DATA[monthIndex] = new ErrorData().setData(pos, vel);
                monthIndex++;
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Contract("_, _ -> new")
    private static @NotNull Vector3dInterface parseVector(final String @NotNull [] vectorVel, boolean position) {
        String x, y, z;
        x = vectorVel[position ? 2 : 5];
        y = vectorVel[position ? 3 : 6];
        z = vectorVel[position ? 4 : 7];
        return new Vector3D(
                parseDouble(x.substring(1, x.length() - 1)),
                parseDouble(y.substring(1, y.length() - 1)),
                parseDouble(z.substring(1, z.length() - 1)));
    }

    /**
     * Start.
     */
    public void start() {
        if (MONTH_INDEX < 13 && MONTH_INDEX >= 0)
            new Thread(this, "Error Log").start();
    }

    @Override
    public void run() {
        //ASSUMING [ NO ] ROCKET HERE
        absPosition = new ArrayList<>(11);
        absVelocity = new ArrayList<>(11);
        relPosition = new ArrayList<>(11);
        relVelocity = new ArrayList<>(11);

        if (OUT_ERROR == CONSOLE) {
            System.out.println("********************        SOE     **************************");
            System.out.println("MONTH " + MONTH_INDEX);
        }

        if (state != null) {
            for (int i = 0; i < state.getPositions().size(); i++) {
                absPosition.add(state.getPositions().get(i)
                        .absSub(ORIGINAL_DATA[MONTH_INDEX].getPositions().get(i)));
                absVelocity.add(state.getVelocities().get(i)
                        .absSub(ORIGINAL_DATA[MONTH_INDEX].getVelocities().get(i)));
                relPosition.add(absPosition.get(i)
                        .div(ORIGINAL_DATA[MONTH_INDEX].getPositions().get(i)));
                relVelocity.add(absVelocity.get(i)
                        .div(ORIGINAL_DATA[MONTH_INDEX].getVelocities().get(i)));
                if (OUT_ERROR == CONSOLE) {
                    System.out.println("PLANET " + simulation.getSystem().getCelestialBodies().get(i).toString() + "~~~~~~~~");
                    //System.out.println("ORIGINAL PV : " + ORIGINAL_DATA[monthIndex].getPositions().get(i));
                    //System.out.println("SIMULATED PV: " + state.getPositions().get(i));
                    System.out.println("ABS ERROR POSIT : " + absPosition.get(i));
                    System.out.println("ABS ERROR VEL   : " + absVelocity.get(i));
                    //System.out.println("ORIGINAL VV : " + ORIGINAL_DATA[monthIndex].getVelocities().get(i));
                    //System.out.println("SIMULATED VV:" + state.getVelocities().get(i));
                    System.out.println("REL ERROR POS : " + relPosition.get(i));
                    System.out.println("REL ERROR VEL : " + relVelocity.get(i));
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                }
            }
        }


        if (OUT_ERROR == CONSOLE) {
            System.out.println("********************        EOE     **************************");
        } else if (OUT_ERROR == TXT) writeFileTxt();
        else if (OUT_ERROR == CSV) writeFileCsv();

    }

    public void writeFileTxt() {
        try {
            init();
            if (DEBUG) System.out.println("Writing on txt file");
            fileWriter.write("********************************        SOE     **************************************\n");
            fileWriter.write("MONTH " + MONTH_INDEX + "\n");
            if (state != null) {
                for (int i = 0; i < state.getPositions().size(); i++) {
                    fileWriter.write("PLANET " + simulation.getSystem().getCelestialBodies().get(i).toString() + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                    //fileWriter.get().write("ABS ERROR POSIT : " + absPosition.get(i) + "\n");
                    //fileWriter.get().write("ABS ERROR VEL   : " + absVelocity.get(i)+ "\n");
                    fileWriter.write("REL ERROR POS : " + relPosition.get(i) + "\n");
                    fileWriter.write("REL ERROR VEL : " + relVelocity.get(i) + "\n");
                }
            }
            //fileWriter.get().write("\n" + "AVERAGE ERROR POSIT : " + averageErrorPosition + "\n");
            //fileWriter.get().write("AVERAGE ERROR VEL   : " + averageErrorVelocity + "\n");
            fileWriter.write("********************************        EOE     **************************************\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFileCsv() {
        try {
            init();
            if (DEBUG) System.out.println("Writing on csv file");
            if (state != null) {
                for (int i = 0; i < state.getPositions().size(); i++) {
                    ((ErrorCSVWriter) fileWriter).addErrorData(
                            solvers[SOLVER],
                            MONTH_INDEX,
                            simulation.getSystem().getCelestialBodies().get(i).toString(),
                            relPosition.get(i),
                            relVelocity.get(i)
                    );
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String[] removeSpaces(String[] vec) {
        String[] t = new String[3];
        int k = 0;
        for (String s : vec) {
            if (!s.equals("") && !s.equals(" ") && !s.equals("  ")) {
                t[k] = s;
                k++;
            }
        }
        return t;
    }

    private void init() throws IOException {
        this.fileWriter = OUT_ERROR.equals(TXT) ?
                new FileWriter(Objects.requireNonNull(extractDir(SIMULATION_CLOCK.monthString((MONTH_INDEX + 3) % 12) + ".txt"))) :
                new ErrorCSVWriter(extractDir(SIMULATION_CLOCK.monthString((MONTH_INDEX + 3) % 12) + ".csv"));

    }
}
