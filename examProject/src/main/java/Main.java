import DAO.IUserDao;
import DAO.Impl.UserDao;
import Logic.MySystem;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        MySystem system = new MySystem();
        system.startSystem();
    }
}
