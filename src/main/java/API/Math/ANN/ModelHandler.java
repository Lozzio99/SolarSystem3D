package API.Math.ANN;

import API.Math.ADT.Matrix;

import java.io.*;

public final class ModelHandler {
    public static boolean export(String path, Matrix[] items) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(new File(path));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(items);
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            System.out.println("Model exported successfully");
        }
    }

    public static Matrix[] loadMatrix(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(path));
            ois = new ObjectInputStream(fis);
            ois.close();
            fis.close();
            return (Matrix[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            System.out.println("Model loaded successfully");
        }
    }
}
