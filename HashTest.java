import java.security.MessageDigest;

public class HashTest {
    public static void main(String[] args) throws Exception {
        String input = "admin123";
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println("Java Hash: " + sb.toString());
        
        // Expected from SQL
        String expected = "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9";
        System.out.println("SQL  Hash: " + expected);
        
        System.out.println("Match: " + sb.toString().equals(expected));
    }
}
