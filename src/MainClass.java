import database.DBManager;
import network.NetworkManager;

public class MainClass {

    public static void main(String[] args)
    {
        System.out.println("Server Start");
        DBManager.getInstance().connectDB();
        NetworkManager.getInstance().startServer();
    }
}