import database.DBManager;
import java.util.Arrays;
import network.NetworkManager;

public class MainClass {

    public static void main(String[] args)
    {
        System.out.println("Start");
        DBManager.getInstance().connectDB();
        NetworkManager.getInstance().startServer();
    }
}
