package sim.kantordesa.config;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Base64;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class koneksi {

    static Connection conn;

    public static Connection getConnection() {
        Properties properties = new Properties();
        File configFile = new File("src/sim/kantordesa/config/config.properties");
        if (conn == null) {
            try (InputStream input = new FileInputStream(configFile.getAbsolutePath())) {
                properties.load(input);
                try {
                    String url = properties.getProperty("db.url") + properties.getProperty("db.name");
                    String creds = new String((byte[]) Base64.getDecoder().decode(properties.getProperty("db.credentials")));
                    DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                    conn = (Connection) DriverManager.getConnection(url, creds, creds);
                } catch (SQLException e) {
                    Logger.getLogger(koneksi.class.getName()).log(Level.SEVERE, "Connection failed!", e);

                }
            } catch (IOException e) {
                Logger.getLogger(koneksi.class.getName()).log(Level.SEVERE, "Cannot read config file!", e);
            }
        }
        return conn;
    }

    public static boolean delete(String query, Object... params) {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(koneksi.class.getName()).log(Level.SEVERE, "Gagal melakukan delete", e);
            return false;
        }
    }

}
