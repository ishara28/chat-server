package utils;

public class ClientUtils {
    private static ClientUtils instance;

    private ClientUtils(){}

    public static ClientUtils getInstance(){
        if (instance == null){
            synchronized(ClientUtils.class){
                if (instance == null){
                    instance = new ClientUtils();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public boolean isValidIdentity(String identity){
        //todo:
        // check if identity is alphanumeric string starting with an upper or lower case character.
        // It must be at least 3 characters and no more than 16 characters long.
//        return /^[A-Za-z]{1}[A-Za-z0-9]{2,15}$/.test(identity);
        return true;
    }
}
