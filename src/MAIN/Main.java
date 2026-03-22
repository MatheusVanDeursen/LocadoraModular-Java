 package MAIN;

import CONFIG_DB.ConnMySQL;
import VIEW.Login;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args){
    	try(java.sql.Connection conn = ConnMySQL.conexao()){
    	    if(conn == null){
    	        throw new Exception("Conexão nula");
    	    }
    	}catch(Exception e){
    	    JOptionPane.showMessageDialog(null, "Falha crítica: Não foi possível conectar ao banco.", "Erro", JOptionPane.ERROR_MESSAGE);
    	    System.exit(1);
    	}
        try{
            Login frame = new Login();
            frame.setVisible(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
