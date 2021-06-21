package API.Errors;

import API.Math.ADT.Vector3dInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static API.Config.DEBUG;

/**
 * This class takes care of exporting the calculated errors to a .csv file.
 * The folder must be present in the project.
 */
public class ErrorCSVWriter extends PrintWriter {

    /**
     * Private constructor of a singleton.
     */
    public ErrorCSVWriter(File path) throws FileNotFoundException {
        super(path);
        // Build file path (according to solver and step size)
        if (DEBUG) System.out.println("Old .csv file deleted!");
        // Create instances to write into file
        writeHeader();
    }

    public void writeHeader() {
        try {
            this.write("SOLVER;MONTH;PLANET;RELATIVE P. ERROR;RELATIVE V. ERROR;R.P.E. NORM;R.V.E. NORM\n");
            this.write("");
            this.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a set of error data to the CSV file.
     *
     * @param month
     * @param planet
     * @param relErrorPos
     * @param relErrorVel
     */
    public void addErrorData(String solver, int month, String planet, Vector3dInterface relErrorPos, Vector3dInterface relErrorVel) {
        if (DEBUG) System.out.println("Log data to CSV: " + month + " " + planet);
        this.write(solver + ";" + month + ";" + planet + ";" + relErrorPos + ";" + relErrorVel + ";" + relErrorPos.norm() + ";" + relErrorVel.norm() + "\n");
        this.flush();
    }


    /**
     * Closes the file.
     */
    @Override
    public void close() {
        if (DEBUG) System.out.println("Closing file writer");
        super.close();
    }
}