/*
 * Copyright 2020 KeviTV.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.kevitv.obvilionNetwork;

import ru.kevitv.obvilionNetwork.utils.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    final private static String dbHost = Config.get("db_ip");
    final private static String dbName = Config.get("db_name");
    final private static String dbUser = Config.get("db_user");
    final private static String dbPass = Config.get("db_pass");
    final private static String dbPort = Config.get("db_port");

    public static Connection getConnection() {
        long time = System.currentTimeMillis();
        try {
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/"+dbName;
            Connection connection = DriverManager.getConnection(url, dbUser, dbPass);
            if (connection != null) {
                Config.dbPing = System.currentTimeMillis() - time;
                return connection;
            }

        } catch (SQLException ex) {
            System.out.println("DB An error occurred. Info: " + ex.getMessage());
        }
        return getConnection();
    }

    public static void createUpdateRequest(String updateRequest) {
        try {
            Connection connection = getConnection();
            int rs = connection.createStatement().executeUpdate(updateRequest);
            connection.close();
        } catch (SQLException e) {
            System.out.println("DB Error! Info: " + e.getMessage());
        }
    }

    public static String createQueryRequest(String queryRequest) {
        try {
            String result = "";
            Connection connection = getConnection();
            ResultSet rs = connection.createStatement().executeQuery(queryRequest);

            if(rs.next()) {
                result = rs.getString(1);
            }

            connection.close();
            return result;
        } catch (SQLException e) {
            System.out.println("DB Error! Info: " + e.getMessage());
            return "null";
        }
    }

    public static List<List<String>> createQueryRequestArray(String queryRequest, int count) {
        try {
            List<List<String>> result = new ArrayList<>();
            Connection connection = getConnection();
            ResultSet rs = connection.createStatement().executeQuery(queryRequest);

            while (rs.next()) {
                List<String> res = new ArrayList<>();
                for (int i = 1; i < count+1; i++) {
                    res.add(rs.getString(i));
                }
                result.add(res);
            }

            connection.close();
            return result;
        } catch (SQLException e) {
            System.out.println("DB Error! Info: " + e.getMessage());
            return null;
        }
    }

    public static int createQueryRequestInt(String queryRequest) {
        try {
            int result = 0;
            Connection connection = getConnection();
            ResultSet rs = connection.createStatement().executeQuery(queryRequest);

            if(rs.next()) {
                result = rs.getInt(1);
            }

            connection.close();
            return result;
        } catch (SQLException e) {
            System.out.println("DB Error! Info: " + e.getMessage());
            return 0;
        }
    }
}
