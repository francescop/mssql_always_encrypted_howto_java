import java.sql.*;
import java.security.SecureRandom;
import com.microsoft.sqlserver.jdbc.*;

public class GenerateEncryptedValue {
    public static void main(String[] args) {
        
        String connectionString  = "jdbc:sqlserver://localhost:1433;encrypt=true;user=sa;password=YourStrongPassword123!;columnEncryptionSetting=Enabled;keyStoreAuthentication=JavaKeyStorePassword;keyStoreLocation=./cmk-keystore.jks;keyStoreSecret=YourStrongPassword;trustServerCertificate=true";
        
        try (Connection connection = DriverManager.getConnection(connectionString)) {
            String cmkName = "CMK1";
            String cekName = "CEK1";
            
            // Generate a new CEK
            byte[] plainTextColumnEncryptionKey = new byte[32]; // 256-bit key
            new SecureRandom().nextBytes(plainTextColumnEncryptionKey);
            
            // Encrypt the CEK with the CMK
            SQLServerColumnEncryptionJavaKeyStoreProvider storeProvider = new SQLServerColumnEncryptionJavaKeyStoreProvider(
                "./cmk-keystore.jks",
                "YourStrongPassword".toCharArray()  // Convert String to char[]
            );
            byte[] encryptedColumnEncryptionKey = storeProvider.encryptColumnEncryptionKey(
                cmkName, 
                "RSA_OAEP", 
                plainTextColumnEncryptionKey
            );
            
            System.out.println("Encrypted CEK: " + bytesToHex(encryptedColumnEncryptionKey));
            
            String sql = String.format("INSERT INTO %s.%s (%s) VALUES (?)",
                "HR", "Employees", "FirstName");

        try (SQLServerConnection con = (SQLServerConnection) DriverManager.getConnection(connectionString)) {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setNString(1, "Johny");

            ps.executeUpdate();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}