package homework;

public class StartConfig {
	 public void boardInitValue() {//
	        for(int i=0; i<3; i++) {
	            for(int j=0; j<4; j++) {
	                Main.BOARD[i][j] = Main.nothing;
					Client.BOARD[i][j] = Client.nothing;
	            }
	        }
	    }
	 
	 public void boardSetup() {
	       
	        //ゲーム開始時のこまの配置
	        Main.BOARD[0][0] = Main.enemyGiraffe;
	        Main.BOARD[1][0] = Main.enemyLion;
	        Main.BOARD[2][0] = Main.enemyElephant;
	        Main.BOARD[1][1] = Main.enemyChick;
	        
	        Main.BOARD[1][2] = Main.chick;
	        Main.BOARD[0][3] = Main.elephant;
	        Main.BOARD[1][3] = Main.lion;
	        Main.BOARD[2][3] = Main.giraffe;

			 Client.BOARD[0][0] = Client.enemyGiraffe;
			 Client.BOARD[1][0] = Client.enemyLion;
			 Client.BOARD[2][0] = Client.enemyElephant;
			 Client.BOARD[1][1] = Client.enemyChick;

			 Client.BOARD[1][2] = Client.chick;
			 Client.BOARD[0][3] = Client.elephant;
			 Client.BOARD[1][3] = Client.lion;
			 Client.BOARD[2][3] = Client.giraffe;
	    }
}