package CONFIG_DB;

import java.sql.*;

public class ConnMySQL {
	public static Connection conexao() {
	    Connection conn = null;
	    String server = "localhost";
	    String port = "3306";
	    String database = "locadora_veiculos";
	    String user = "root";
	    String pass = "";
	    String driver = "com.mysql.cj.jdbc.Driver"; 
	    String url = "jdbc:mysql://" + server + ":" + port + "/" + database + "?useTimezone=true&serverTimezone=UTC";
	    //jdbc:mysql//localhost:3306/locadora_veiculos?useTimezone=true&serverTimezone=UTC

	    try{
	        Class.forName(driver);
	        conn = DriverManager.getConnection(url, user, pass);
	    }catch(ClassNotFoundException e){
	        System.err.println("Driver não encontrado!");
	        e.printStackTrace();
	    }catch(SQLException e){
	        System.err.println("Erro ao conectar no banco.");
	        e.printStackTrace();
	    }
	    return conn;
	}
}