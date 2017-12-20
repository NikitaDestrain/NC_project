package client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {

    /**
     * Encodes user's password using MD5 algorithm
     * @param password user's password
     * @return byte array with encoded input string
     * @throws NoSuchAlgorithmException
     */

    public byte[] encode(String password) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password.getBytes());

        return md5.digest();
    }
}
