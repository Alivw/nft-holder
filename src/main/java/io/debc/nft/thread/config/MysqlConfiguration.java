package io.debc.nft.thread.config;

import io.debc.nft.thread.utils.SysUtils;

import java.sql.*;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-13 11:08
 **/
public class MysqlConfiguration {
    public static final String host = SysUtils.getSystemEnv("mysqlHost", "192.168.31.192");
    public static final String password = SysUtils.getSystemEnv("mysqlPassword", "1804ceshi");
    public static final String user = SysUtils.getSystemEnv("mysqlUser", "root");
    public static final String database = SysUtils.getSystemEnv("mysqlDatabase", "erc20balance");
    public static final int port = Integer.parseInt(SysUtils.getSystemEnv("mysqlPort", "3306"));

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }

    public static void put(long blockNumber) {
        String sql = "insert into t_erc20_balance(id) values(?)";
        Connection conn = null;
        try {
             conn= getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, blockNumber);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static long getMaxId() {
        String sql = "select max(id) from t_erc20_balance";
        Connection conn = null;
        try {
            conn= getConn();
            Statement st =conn.createStatement();
            ResultSet resultSet = st.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return 0;
    }
}
