import database.DBManager;
import network.RequestManager;

public class MainClass {

    public static void main(String[] args)
    {
        System.out.println("Server Start");
        DBManager.getInstance().connectDB();
        RequestManager.getInstance().startServer(); 
    }
    
}