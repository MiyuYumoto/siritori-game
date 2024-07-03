package homework;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {
    // ServerのIPアドレスとport番号を定義
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8880;

    // showBoardのインスタンスを保持する変数
    static koma lion = new koma(state.My, komaKinds.Lion);
    static koma giraffe = new koma( state.My, komaKinds.Girafee);
    static koma elephant = new koma(state.My, komaKinds.Elephant);
    static koma chick = new koma(state.My, komaKinds.Chick);
    static koma enemyLion = new koma(state.Enemy, komaKinds.Lion);
    static koma enemyGiraffe = new koma(state.Enemy,komaKinds.Girafee);
    static koma enemyElephant = new koma(state.Enemy,komaKinds.Elephant);
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
    static GameWindow GW1;
    //
    //相手からとった駒を保管するlist
    static List<koma> myList = new ArrayList<>();
    static List<koma> enemyList = new ArrayList<>();
    //文字を読み込む
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    // サーバーとクライアントの通信の設定
    private static final int PORT = 8880;
    static ObjectOutputStream outputStream;
    static ObjectInputStream inputStream;

    public static void main(String[] args) {
        try
        {
            setupServerAndClients();
            GW1 = new GameWindow();//GUI
            GW1.setVisible(true);
            initializeGame();//ここで初期配置と初期描画
            executeGameLoop();//ゲームのメイン部分
            System.out.println("ゲーム終了");
        }
        catch (IOException e)
        {
            // エラーが発生した場合はスタックトレースを出力
            e.printStackTrace();
        }
    }

    private static void setupServerAndClients() throws IOException
    {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        System.out.println("Connected to server.");
        // サーバーへの出力ストリームとサーバーからの入力ストリームを生成
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    private static void initializeGame()
    {
        //インスタンスの作成
        SC = new StartConfig();
        CIV = new CheckInputValue();
        System.out.println("動物将棋を開始します.");
        //ボードに初期値をセットする
        SC.boardInitValue();
        SC.boardSetup();
        //ボードを表示する
        //SB.homework.showBoard(new homework.toClientData(BOARD,myList,enemyList));
    }

    private static void executeGameLoop()
    {
        while (isGame) {
            movingKoma();//なければ自分のフィールドの駒を動かす
            switchTurn();//ターンを変える
            System.out.println("ターンが変わりました");
            sendStringToClient();
        }
    }

    private static void executePlayerChoice()
    {
        try
        {
            while (true) {
                System.out.println("持ち駒をフィールドに置くかフィールドの駒を動かすか\n" +
                        "持ち込まをフィールドに置く場合: 1\n" +
                        "フィールドの駒を動かす場合: 2");
                if (reader.readLine().matches("[1-2]")) {
                    break;
                }
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void switchTurn() {	//ターンを変える
        state temp = turn;
        turn = nextTurn;
        nextTurn = temp;
    }


    static List<koma> getKomaList(state turn)
    {
        if(turn == state.My)
        {
            return myList;
        }

        else
        {
            return enemyList;
        }


    }

    static void removeList(state turn, int index)
    {
        if(turn == state.My)
        {
            myList.remove(index);
        }

        else
        {
            enemyList.remove(index);
        }
    }

    static void addList(state turn, koma koma)
    {
        koma.setState(turn);

        if(turn == state.My)
        {
            myList.add(koma);
        }
        else
        {
            enemyList.add(koma);

        }
    }
    static void movingKoma()
    {
        while(true) {
            while (true)//どの駒を動かすかを確認
            {
                //x座標を入力する処理
                System.out.println("動かしたい駒の位置をクリック");
                while (true) {
                    x = GW1.clickX;
                    y = GW1.clickY;
                    if (GW1.isSelected) {
                        break;
                    }
                }
                GW1.isClicked = false;
                if(!GW1.isSelectedHand) {
                    if (CIV.isKomaCheck1(x, y)) //動かすことのできる動物なのか
                    {
                        inputX = Integer.parseInt(x);
                        inputY = Integer.parseInt(y);
                        //System.out.println("あなたが選んだ動物は" + BOARD[inputX][inputY].getName());
                        break;
                    } else {
                        System.out.println("その値は無効です");
                        GW1.KomaCancel();
                        GW1.isSelected = false;
                    }
                }else{
                    inputX = Integer.parseInt(x);
                    inputY = Integer.parseInt(y);
                    if((inputY == 4 && turn == state.My) || (inputY == 5 && turn == state.Enemy)) {
                        System.out.println("手札から");
                        break;
                    }else{
                        System.out.println("相手の駒です");
                        GW1.KomaCancel();
                        GW1.isSelected = false;
                        GW1.isSelectedHand = false;
                    }
                }
            }
            //選んだ駒の動かしたい座標を指定する
            while (true) {
                //x座標を入力する処理
                System.out.println("動かしたい座標をクリック");
                while (true) {
                    x = GW1.clickX;
                    y = GW1.clickY;
                    if(!GW1.isSelected){
                        break;
                    }
                    if (GW1.isClicked && GW1.isSelected) {
                        break;
                    }
                }
                GW1.isClicked = false;
                if(!GW1.isSelected){
                    break;
                }
                if(!GW1.isSelectedHand) {
                    if (CIV.isIndexCheck1(inputX, inputY, x, y)) //打ち込んだ座標に駒がおけるか
                    {
                        beforeX = inputX;
                        beforeY = inputY;
                        afterX = Integer.parseInt(x);
                        afterY = Integer.parseInt(y);
                        if (BOARD[afterX][afterY].getState() == nextTurn) //選んだところが相手の駒だったら
                        {
                            addList(turn, BOARD[afterX][afterY]);//自軍のlistに取った駒を入れる
                            isMoveHand = true;
                            if (turn == state.My) {
                                GW1.KomaMoveHand(GW1.HandNum1, 4, 0, GW1.iOtherNum);
                                //相手のクライアントに情報を送る
                            } else {
                                GW1.KomaMoveHand(GW1.HandNum2, 5, 1, GW1.iOtherNum);
                            }
                        }
                        //駒を入れ替える
                        GW1.KomaMove(afterX, afterY, GW1.iNum, 5);
                        BOARD[afterX][afterY] = BOARD[beforeX][beforeY];
                        BOARD[beforeX][beforeY] = new koma(komaKinds.Nothing);
                        //SB.homework.showBoard(new homework.toClientData(BOARD, myList, enemyList));
                        break;
                    } else {
                        System.out.println("!!!!!!!!!!その値は無効です!!!!!!!!!!");
                    }
                }else{
                    if(CIV.isCheckList1(x,y))//入力した値に駒を置くこととができるのか
                    {
                        beforeX = inputX;
                        afterX = Integer.parseInt(x);
                        afterY = Integer.parseInt(y);
                        List<koma> koma = getKomaList(turn);//自軍のlistを取得
                        BOARD[afterX][afterY] = koma.get(beforeX);//入力した座標に選択した駒を置く
                        removeList(turn, beforeX);//それを自軍のlistから取り除く
                        GW1.KomaMove(afterX, afterY, GW1.iNum, 2);
                        if(turn == state.My){
                            GW1.HandNum1 = beforeX;
                            GW1.isHand1[GW1.HandNum1] = false;
                        }else{
                            GW1.HandNum2 = beforeX;
                            GW1.isHand2[GW1.HandNum2] = false;
                        }
                        //SB.homework.showBoard(new homework.toClientData(BOARD,myList,enemyList));
                        break;
                    }
                }
            }
            if(GW1.isSelected){
                setOutputString(GW1.isSelectedHand,beforeX,beforeY,afterX, afterY, GW1.iNum, GW1.iOtherNum);
                GW1.isSelectedHand = false;
                GW1.isSelected = false;
                isMoveHand = false;
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
                        GW1.KomaMoveHand(GW1.HandNum1, 4, 0, ai);
                    } else {
                        GW1.KomaMoveHand(GW1.HandNum2, 5, 1, ai);
                    }
                }
                //駒を入れ替える
                GW1.KomaMove(ax, ay, i, 5);
                BOARD[ax][ay] = BOARD[bx][by];
                BOARD[bx][by] = new koma(komaKinds.Nothing);
                //SB.homework.showBoard(new homework.toClientData(BOARD, myList, enemyList));

        } else {
                List<koma> koma = getKomaList(turn);//自軍のlistを取得
                BOARD[ax][ay] = koma.get(bx);//入力した座標に選択した駒を置く
                removeList(turn, bx);//それを自軍のlistから取り除く
                GW1.KomaMove(ax, ay, i, 2);
                if (turn == state.My) {
                    GW1.HandNum1 = bx;
                    GW1.isHand1[GW1.HandNum1] = false;
                } else {
                    GW1.HandNum2 = bx;
                    GW1.isHand2[GW1.HandNum2] = false;
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
