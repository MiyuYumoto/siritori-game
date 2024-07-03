package homework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Main {
	//ここで駒を生成(味方か敵か、どの動物か)を引数として渡す
	static koma lion = new koma(state.My, komaKinds.Lion);
	static koma giraffe = new koma(state.My, komaKinds.Girafee);
	static koma elephant = new koma(state.My, komaKinds.Elephant);
	static koma chick = new koma(state.My, komaKinds.Chick);
	static koma enemyLion = new koma(state.Enemy, komaKinds.Lion);
	static koma enemyGiraffe = new koma(state.Enemy, komaKinds.Girafee);
	static koma enemyElephant = new koma(state.Enemy, komaKinds.Elephant);
	static koma enemyChick = new koma(state.Enemy, komaKinds.Chick);
	static koma nothing = new koma(komaKinds.Nothing);
	// ゲームボードの定義（3x4のマス）
	static koma[][] BOARD = new koma[3][4];

	//入力値の情報を保持する
	private static int inputX;
	private static int inputY;
	static String x = "0";
	static String y = "0";
	static int afterX = 0;
	static int afterY = 0;
	static int beforeX = 0;
	static int beforeY = 0;
	static boolean isMoveHand;
	static int num;
	//ターンの確認
	static state turn = state.My;
	//次のターン
	static state nextTurn = state.Enemy;
	//これがfalseになるとゲーム終了
	static boolean isGame = true;
	//これら４つはボードを表示したり駒が置けるかの確認などの関数としての役割
	static StartConfig SC;
	static CheckInputValue CIV;
	static GameWindow GW;
	//相手からとった駒を保管するlist
	static List<koma> myList = new ArrayList<>();
	static List<koma> enemyList = new ArrayList<>();
	//文字を読み込む
	static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	// サーバーとクライアントの通信の設定
	private static final int PORT = 8880;
	static Socket client1Socket;
	static ObjectOutputStream outputStream;
	static ObjectInputStream inputStream;

	public static void main(String[] args) {
		try {
			setupServerAndClients();
			initializeGame();//ここで初期配置と初期描画
			GW = new GameWindow();//GUI
			GW.setVisible(true);
			executeGameLoop();//ゲームのメイン部分
			System.out.println("ゲーム終了");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setupServerAndClients() throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("homework.Server started. Waiting for clients...");

		client1Socket = serverSocket.accept();
		System.out.println("Clientと接続完了: " + client1Socket.getInetAddress());
		outputStream = new ObjectOutputStream(client1Socket.getOutputStream());
		inputStream = new ObjectInputStream(client1Socket.getInputStream());
	}

	private static void initializeGame() {
		//インスタンスの作成
		SC = new StartConfig();
		CIV = new CheckInputValue();
		System.out.println("動物将棋を開始します.");
		//ボードに初期値をセットする
		SC.boardInitValue();
		SC.boardSetup();
	}

	private static void executeGameLoop() {
		while (isGame) {
			sendStringToClient();
			movingKoma();//なければ自分のフィールドの駒を動かす
			switchTurn();//ターンを変える
			System.out.println("ターンが変わりました");


		}
	}


	private static void switchTurn() {    //ターンを変える
		state temp = turn;
		turn = nextTurn;
		nextTurn = temp;
	}


	static List<koma> getKomaList(state turn) {
		if (turn == state.My) {
			return myList;
		} else {
			return enemyList;
		}


	}

	static void removeList(state turn, int index) {
		if (turn == state.My) {
			myList.remove(index);
		} else {
			enemyList.remove(index);
		}
	}

	static void addList(state turn, koma koma) {
		koma.setState(turn);

		if (turn == state.My) {
			myList.add(koma);
		} else {
			enemyList.add(koma);

		}
	}

	static void waitKoma() {

	}

	static void movingKoma() {
		while (true) {
			while (true)//どの駒を動かすかを確認
			{
				//x座標を入力する処理
				System.out.println("動かしたい駒の位置をクリック");
				while (true) {
					x = GW.clickX;
					y = GW.clickY;
					if (GW.isSelected) {
						break;
					}
				}
				GW.isClicked = false;
				if (!GW.isSelectedHand) {
					if (CIV.isKomaCheck(x, y)) //動かすことのできる動物なのか
					{
						inputX = Integer.parseInt(x);
						inputY = Integer.parseInt(y);
						System.out.println("あなたが選んだ動物は" + BOARD[inputX][inputY].getName());
						break;
					} else {
						System.out.println("その値は無効です");
						GW.KomaCancel();
						GW.isSelected = false;
					}
				} else {
					inputX = Integer.parseInt(x);
					inputY = Integer.parseInt(y);
					if ((inputY == 4 && turn == state.My) || (inputY == 5 && turn == state.Enemy)) {
						System.out.println("手札から");
						break;
					} else {
						System.out.println("相手の駒です");
						GW.KomaCancel();
						GW.isSelected = false;
						GW.isSelectedHand = false;
					}
				}
			}
			//選んだ駒の動かしたい座標を指定する
			while (true) {
				//x座標を入力する処理
				System.out.println("動かしたい座標をクリック");
				while (true) {
					x = GW.clickX;
					y = GW.clickY;
					if (!GW.isSelected) {
						break;
					}
					if (GW.isClicked && GW.isSelected) {
						break;
					}
				}
				GW.isClicked = false;
				if (!GW.isSelected) {
					break;
				}
				if (!GW.isSelectedHand) {
					if (CIV.isIndexCheck(inputX, inputY, x, y)) //打ち込んだ座標に駒がおけるか
					{
						beforeX = inputX;
						beforeY = inputY;
						afterX = Integer.parseInt(x);
						afterY = Integer.parseInt(y);
						if (BOARD[afterX][afterY].getState() == nextTurn) //選んだところが相手の駒だったら
						{
							addList(turn, BOARD[afterX][afterY]);//自軍のlistに取った駒を入れる
							if (turn == state.My) {
								GW.KomaMoveHand(GW.HandNum1, 4, 0, GW.iOtherNum);
							} else {
								GW.KomaMoveHand(GW.HandNum2, 5, 1, GW.iOtherNum);
							}
						}
						//駒を入れ替える
						GW.KomaMove(afterX, afterY, GW.iNum, 5);
						BOARD[afterX][afterY] = BOARD[beforeX][beforeY];
						BOARD[beforeX][beforeY] = new koma(komaKinds.Nothing);
						break;
					} else {
						System.out.println("!!!!!!!!!!その値は無効です!!!!!!!!!!");
					}
				} else {
					if (CIV.isCheckList(x, y))//入力した値に駒を置くこととができるのか
					{
						beforeX = inputX;
						afterX = Integer.parseInt(x);
						afterY = Integer.parseInt(y);
						List<koma> koma = getKomaList(turn);//自軍のlistを取得
						BOARD[afterX][afterY] = koma.get(beforeX);//入力した座標に選択した駒を置く
						removeList(turn, beforeX);//それを自軍のlistから取り除く
						GW.KomaMove(afterX, afterY, GW.iNum, 2);
						if (turn == state.My) {
							GW.HandNum1 = beforeX;
							GW.isHand1[GW.HandNum1] = false;
						} else {
							GW.HandNum2 = beforeX;
							GW.isHand2[GW.HandNum2] = false;
						}
						break;
					}
				}
			}
			if (GW.isSelected) {
				setOutputString(GW.isSelectedHand,beforeX,beforeY,afterX, afterY, GW.iNum, GW.iOtherNum);
				GW.isSelectedHand = false;
				GW.isSelected = false;
				break;
			}
		}
	}

	static void movingKoma1(boolean isSelectedHand,int bx, int by ,int ax, int ay, int i,int ai) {

		//選んだ駒の動かしたい座標を指定する
		if (!isSelectedHand) {
			if (BOARD[ax][ay].getState() == nextTurn) //選んだところが相手の駒だったら
			{
				addList(turn, BOARD[ax][ay]);//自軍のlistに取った駒を入れる
				isMoveHand = true;
				if (turn == state.My) {
					GW.KomaMoveHand(GW.HandNum1, 4, 0, ai);
				} else {
					GW.KomaMoveHand(GW.HandNum2, 5, 1, ai);
				}
			}
			//駒を入れ替える
			GW.KomaMove(ax, ay, i, 5);
			BOARD[ax][ay] = BOARD[bx][by];
			BOARD[bx][by] = new koma(komaKinds.Nothing);
			//SB.homework.showBoard(new homework.toClientData(BOARD, myList, enemyList));

		} else {
			List<koma> koma = getKomaList(turn);//自軍のlistを取得
			BOARD[ax][ay] = koma.get(bx);//入力した座標に選択した駒を置く
			removeList(turn, bx);//それを自軍のlistから取り除く
			GW.KomaMove(ax, ay, i, 2);
			if (turn == state.My) {
				GW.HandNum1 = bx;
				GW.isHand1[GW.HandNum1] = false;
			} else {
				GW.HandNum2 = bx;
				GW.isHand2[GW.HandNum2] = false;
			}
			//SB.homework.showBoard(new homework.toClientData(BOARD,myList,enemyList));
		}
		isMoveHand = false;
	}


	public static void setOutputString(boolean isHand,int bx, int by, int ax, int ay, int i, int ai){
		String data;
		if(isHand){
			data = "0"+","+bx+","+by+","+ax+","+ay+","+i+","+ai;
		}else{
			data = "1"+","+bx+","+by+","+ax+","+ay+","+i+","+ai;
		}
		showBoardToClient(data);
	}


	// (ボード情報をクライアントに送信)
	public static void showBoardToClient(String data)
	{
		try
		{
			outputStream.writeObject(data);
			outputStream.flush();
			outputStream.reset();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// (クライアントに質問を送信し、回答を受け取る)
	public static void sendStringToClient()
	{
		try
		{
			String response = (String) inputStream.readObject();
			String[] array = response.split(",");
			int[] n = new int[7];
			for(int i = 0; i < n.length; i++){
				n[i] = Integer.parseInt(array[i]);
			}
			if(n[0]==1){
				movingKoma1(false,n[1],n[2],n[3],n[4],n[5],n[6]);
			}else{
				movingKoma1(true,n[1],n[2],n[3],n[4],n[5],n[6]);
			}

			switchTurn();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}

	}

}





enum state{//敵か味方を判別　例　今は敵のターンかとかこのの駒は敵のものかとか
	Enemy,
	My
	}
