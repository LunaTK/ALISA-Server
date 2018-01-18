import database.DBManager;
import java.util.Arrays;
import network.NetworkManager;

public class MainClass {

	public static void main(String[] args)
	{
            DBManager.getInstance().connectDB();
            NetworkManager.getInstance().startServer();
                
	}
}
