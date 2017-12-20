package server.controller;

public class PasswordHexConverter {

    /**
     * Converts incoming byte array to hex string
     * @param digest incoming byte array
     * @return string with hex from byte array that can be compared with existing passwords in server's map
     */

    public String convertToHex(byte[] digest) {
        StringBuffer sb = new StringBuffer();
        String hex;
        for (byte data : digest) {
            hex = Integer.toHexString(0xFF & data);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
