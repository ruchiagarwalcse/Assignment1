package voting;

import org.springframework.security.crypto.codec.Base64;

public class BasicAuth{

    //Checking Basic Authentication
    public static Boolean isAuthorized(String authorization){
        if (authorization != null && authorization.startsWith("Basic") ){

            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.decode(base64Credentials.getBytes()));
            String[] values = credentials.split(":", 2);
            if(values[0].equals("foo") && values[1].equals("bar")){
                  return true;
            }
        }
        return false;
    }
}