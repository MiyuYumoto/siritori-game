package homework;

public class CheckInputValue {
   
	public boolean isKomaCheck(String x, String y) {
        //入力値が1~8ならtrue
        if(x.matches("[0-2]")&&y.matches("[0-3]")) {//選んだマスがボードの範囲内かどうか
        	int X = Integer.parseInt(x);
        	int Y =Integer.parseInt(y);
        	if(Main.BOARD[X][Y].getkomaKinds()!=komaKinds.Nothing&&isCanPutkoma(X,Y)&&Main.BOARD[X][Y].getState() == Main.turn) //選んだ場所がnothing以外
        	{
        		
        		return true;
        	}
            
        }

        //文字やそれ以外の数字を入力したらfalse
        
        return false;
    }

	public boolean isKomaCheck1(String x, String y) {
		//入力値が1~8ならtrue
		if(x.matches("[0-2]")&&y.matches("[0-3]")) {//選んだマスがボードの範囲内かどうか
			int X = Integer.parseInt(x);
			int Y =Integer.parseInt(y);
			if(Client.BOARD[X][Y].getkomaKinds()!=komaKinds.Nothing&&isCanPutkoma1(X,Y)&&Client.BOARD[X][Y].getState() == Client.turn) //選んだ場所がnothing以外
			{

				return true;
			}

		}

		//文字やそれ以外の数字を入力したらfalse

		return false;
	}
	
	public boolean isCanPutkoma(int x, int y) 
	{
		Vector2D[] vector = Main.BOARD[x][y].getVector();
		for(int i = 0;i < vector.length; i++) {
			int confirmX = x + vector[i].getX();
			int confirmY = y + vector[i].getY();
			if((confirmX >= 0 && confirmX <= 2) && (confirmY >= 0 && confirmY <= 3)) 
			{
				if(Main.BOARD[confirmX][confirmY].getState() != Main.turn)
				{
					return true;
				}
			}
		}
		
		return false;
		
	}

	public boolean isCanPutkoma1(int x, int y)
	{
		Vector2D[] vector = Client.BOARD[x][y].getVector();
		for(int i = 0;i < vector.length; i++) {
			int confirmX = x + vector[i].getX();
			int confirmY = y + vector[i].getY();
			if((confirmX >= 0 && confirmX <= 2) && (confirmY >= 0 && confirmY <= 3))
			{
				if(Client.BOARD[confirmX][confirmY].getState() != Client.turn)
				{
					return true;
				}
			}
		}

		return false;

	}
	
	//---------------------------------------------------------------------------------------------------------
	
	public boolean isIndexCheck(int beforeX ,int beforeY, String afterX, String afterY) 
	{
		 if(afterX.matches("[0-2]")&&afterY.matches("[0-3]")) {//打ち込んだ座標がボードの範囲内かどうか
	        	int X = Integer.parseInt(afterX);
	        	int Y =Integer.parseInt(afterY);
	        	Vector2D[] vector = Main.BOARD[beforeX][beforeY].getVector();//選んだ動物のベクトルを得る
	    		for(int i = 0;i < vector.length; i++) {
	    			int confirmX = beforeX + vector[i].getX();
	    			int confirmY = beforeY + vector[i].getY();
	    			if(confirmX==X&&confirmY==Y&&Main.BOARD[confirmX][confirmY].getState() != Main.turn) //選択した動物が指定した場所に動けるか
	    			{
	    				//ひよこが進化
	    				if(Main.BOARD[beforeX][beforeY].getkomaKinds()==komaKinds.Chick
	    						&&(Main.turn==state.Enemy&&Y==3)||(Main.turn==state.My&&Y==0))
	    				{
	    					Main.BOARD[beforeX][beforeY]=new koma(Main.turn,komaKinds.Bird);
	    				}
	    				//ゲームの勝利判定
	    				if((Main.BOARD[X][Y].getState() == Main.nextTurn//駒の置く場所に相手の駒があるなら
	    						&&Main.BOARD[X][Y].getkomaKinds() == komaKinds.Lion)//かつそれがライオンなら
	    						||(isLionLocation(X,Y)&&Main.BOARD[beforeX][beforeY].getkomaKinds()==komaKinds.Lion&&Main.turn==state.My&&Y==0)
	    						||(isLionLocation(X,Y)&&Main.BOARD[beforeX][beforeY].getkomaKinds()==komaKinds.Lion&&Main.turn==state.Enemy&&Y==3))
	    				{
	    					//勝利条件
	    					Main.isGame = false;
	    				}
	    					
	    				
	    				return true;
	    			}
	    			}
	    		}
		 return false;
	}

	public boolean isIndexCheck1(int beforeX ,int beforeY, String afterX, String afterY)
	{
		if(afterX.matches("[0-2]")&&afterY.matches("[0-3]")) {//打ち込んだ座標がボードの範囲内かどうか
			int X = Integer.parseInt(afterX);
			int Y =Integer.parseInt(afterY);
			Vector2D[] vector = Client.BOARD[beforeX][beforeY].getVector();//選んだ動物のベクトルを得る
			for(int i = 0;i < vector.length; i++) {
				int confirmX = beforeX + vector[i].getX();
				int confirmY = beforeY + vector[i].getY();
				if(confirmX==X&&confirmY==Y&&Client.BOARD[confirmX][confirmY].getState() != Client.turn) //選択した動物が指定した場所に動けるか
				{
					//ひよこが進化
					if(Client.BOARD[beforeX][beforeY].getkomaKinds()==komaKinds.Chick
							&&(Client.turn==state.Enemy&&Y==3)||(Client.turn==state.My&&Y==0))
					{
						Client.BOARD[beforeX][beforeY]=new koma(Client.turn,komaKinds.Bird);
					}
					//ゲームの勝利判定
					if((Client.BOARD[X][Y].getState() == Client.nextTurn//駒の置く場所に相手の駒があるなら
							&&Client.BOARD[X][Y].getkomaKinds() == komaKinds.Lion)//かつそれがライオンなら
							||(isLionLocation1(X,Y)&&Client.BOARD[beforeX][beforeY].getkomaKinds()==komaKinds.Lion&&Client.turn==state.My&&Y==0)
							||(isLionLocation1(X,Y)&&Client.BOARD[beforeX][beforeY].getkomaKinds()==komaKinds.Lion&&Client.turn==state.Enemy&&Y==3))
					{
						//勝利条件
						Client.isGame = false;
					}


					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isLionLocation(int X,int Y) 
	{
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 4; y++) {
				if(Main.BOARD[x][y].getState()==Main.nextTurn) //相手の動物の位置を特定
				{
					Vector2D[] vector = Main.BOARD[x][y].getVector();
					for(int i = 0; i < vector.length ; i++) 
					{
						if(X == vector[i].getX() + x&& Y == vector[i].getY() + y) 
						{
							return false;
						}
					}
				}
			}
		}
		return true;
		
	}

	public boolean isLionLocation1(int X,int Y)
	{
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 4; y++) {
				if(Client.BOARD[x][y].getState()==Client.nextTurn) //相手の動物の位置を特定
				{
					Vector2D[] vector = Client.BOARD[x][y].getVector();
					for(int i = 0; i < vector.length ; i++)
					{
						if(X == vector[i].getX() + x&& Y == vector[i].getY() + y)
						{
							return false;
						}
					}
				}
			}
		}
		return true;

	}
	
	public boolean isCheckList(String x, String y) 
	{
		 if(x.matches("[0-2]")&&y.matches("[0-3]")) {//選んだマスがボードの範囲内かどうか
	        	int X = Integer.parseInt(x);
	        	int Y =Integer.parseInt(y);
	        	if(Main.BOARD[X][Y].getkomaKinds()==komaKinds.Nothing) //選んだ場所がnothing以外
	        	{
	        		
	        		return true;
	        	}
	            
	        }
		
		return false;
	}


	public boolean isCheckList1(String x, String y)
	{
		if(x.matches("[0-2]")&&y.matches("[0-3]")) {//選んだマスがボードの範囲内かどうか
			int X = Integer.parseInt(x);
			int Y =Integer.parseInt(y);
			if(Client.BOARD[X][Y].getkomaKinds()==komaKinds.Nothing) //選んだ場所がnothing以外
			{

				return true;
			}

		}

		return false;
	}
}
