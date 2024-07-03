package homework;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
public class GameWindow extends JFrame{
    JLabel backPanel;//背景
    JLabel[] pane = new JLabel[18];//盤面
    //駒
    //0:象 1:ライオン 2:きりん 3:ひよこ 4:敵ひよこ 5:敵きりん 6:敵ライオン 7:敵象
    JLabel[] koma  = new JLabel[8];
    ImageIcon[] komaImg = new ImageIcon[8];//画像

    boolean[] isKoma = new boolean[8];//特定の駒が選択されているか
    boolean[] isMoving = new boolean[8];//特定の駒が動いている最中か
    boolean isClicked = false;//クリックされたか
    boolean isSelected = false;//駒のどれかが選択されているか
    boolean isSelectedHand = false;//手駒が選択されているか
    //手駒スロットのどこが空いているか
    boolean[] isHand1 = new boolean[3];
    boolean[] isHand2 = new boolean[3];
    //駒の座標
    int komaX[] = new int[8];
    int komaY[] = new int[8];
    //駒のGUI座標
    int[] x = new int[8];
    int[] y = new int[8];
    //クリックした座標
    String clickX = "0";
    String clickY = "0";
    int iNum = 0;
    int iOtherNum = 0;
    int HandNum1 = 0;
    int HandNum2 = 0;

    GameWindow(){
        setTitle("display");
        setSize(600,850);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        backPanel = new JLabel();
        backPanel.setBounds(0,0,600,850);
        backPanel.setVerticalAlignment(JLabel.TOP);
        add(backPanel);
        backPanel.setOpaque(true);
        //backPanel.setBackground(Color.pink);


        for(int i = 0; i < komaImg.length; i++){
            komaImg[i] = new ImageIcon(i+".png");
        }

        for(int i = 0; i < koma.length; i++){
            if(i<3){
                komaX[i] = i;
                komaY[i] = 3;
            }else if(i==3){
                komaX[i] = 1;
                komaY[i] = 2;
            }else if(i==4){
                komaX[i] = 1;
                komaY[i] = 1;
            }else{
                komaX[i] = i-5;
                komaY[i] = 0;
            }
            x[i] = 140+komaX[i]*110;
            y[i] = 195+komaY[i]*110;

            koma[i] = new JLabel();
            koma[i].setBounds(x[i], y[i],100,100);
            koma[i].setBorder(new LineBorder(Color.red,2,true));
            koma[i].setOpaque(true);
            koma[i].setIcon(komaImg[i]);
            backPanel.add(koma[i]);
            backPanel.setComponentZOrder(koma[i], 0);

            isKoma[i] = false;
            isMoving[i] = false;
        }

        int num = 0;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 3; j++) {
                pane[num] = new JLabel();
                pane[num].setBounds(135+j*110,190+i*110,110,110);
                pane[num].setBorder(new LineBorder(Color.black, 1, true));
                pane[num].addMouseListener(new LabelCheck(j,i));
                backPanel.add(pane[num]);
                num++;
            }
        }
        for(int i = 0; i < 6; i++){
            pane[i+12] = new JLabel();
            if(i < 3){
                pane[i+12].setBounds(10+i*90, 710,90,90);
                pane[i+12].addMouseListener(new LabelCheck(i,4));
                isHand1[i] = false;
            }else{
                pane[i+12].setBounds(580-(i-2)*90, 10,90,90);
                pane[i+12].addMouseListener(new LabelCheck(i-3,5));
                isHand2[i-3] = false;
            }
            pane[i+12].setBorder(new LineBorder(Color.black, 1, true));
            backPanel.add(pane[i+12]);
        }
    }

    //駒を動かすメソッド
    public void KomaMove(int dx, int dy, int i, int time){
        if(!isMoving[i]) {
            Thread th = new Move(dx, dy, i, time);
            th.start();
            komaX[i] = dx;
            komaY[i] = dy;
        }
    }
    public void KomaMoveHand(int dx, int dy, int state, int i){
        Thread th = new MoveHand(state ,i);
        th.start();
        komaX[i] = dx;
        komaY[i] = dy;
    }
    //mainに渡す変数の設定
    public void ClickXY(int x, int y){
        clickX = Integer.toString(x);
        clickY = Integer.toString(y);
    }
    public void Inumber(int i){
        iNum = i;
    }
    public void Iother(int i){
        iOtherNum = i;
    }
    public void KomaCancel(){
        for(int i = 0; i < koma.length; i++) {
            koma[i].setBorder(new LineBorder(Color.red, 2, true));
            isKoma[i] = false;
        }
    }
    public void RotateIcon(int i, int imgI){
        koma[i].setIcon(komaImg[imgI]);
    }
    //クリックした時
    class LabelCheck extends MouseInputAdapter{
        int num1;
        int num2;
        LabelCheck(int n1,int n2){
            num1 = n1;
            num2 = n2;
        }
        public void mousePressed(MouseEvent e) {
            ClickXY(num1, num2);
            isClicked = true;
            for(int i = 0; i < koma.length; i++){
                if(num1 == komaX[i] && num2 == komaY[i] && num2 < 4){
                    if(!isKoma[i] && !isSelected) {
                        koma[i].setBorder(new LineBorder(Color.cyan, 2, true));
                        isKoma[i] = true;
                        isSelected = true;
                    }else if(!isKoma[i] && isSelected){
                        Iother(i);
                    }else{
                        koma[i].setBorder(new LineBorder(Color.red,2,true));
                        isKoma[i] = false;
                        isSelected = false;
                    }
                }else if(num1 == komaX[i] && num2 == komaY[i] && num2 >= 4){
                    if(!isKoma[i] && !isSelected) {
                        koma[i].setBorder(new LineBorder(Color.cyan, 2, true));
                        isKoma[i] = true;
                        isSelectedHand = true;
                        isSelected = true;
                    }else if(!isKoma[i] && isSelected){
                        Iother(i);
                    }else{
                        koma[i].setBorder(new LineBorder(Color.red,2,true));
                        isKoma[i] = false;
                        isSelectedHand = false;
                        isSelected = false;
                    }
                }else {
                    if (isKoma[i]) {
                        Inumber(i);
                    }
                }
            }
        }
    }

    class Move extends Thread{
        int dx;
        int dy;
        int i;
        int time;
        Move(int dx, int dy, int i, int time){
            this.dx = 140+dx*110;
            this.dy = 195+dy*110;
            this.i = i;
            this.time = time;
            isMoving[i] = true;
            backPanel.setComponentZOrder(koma[i], 0);
            koma[i].revalidate();
        }
        public void run(){
            while(isMoving[i]){
                koma[i].setBounds(x[i], y[i],100,100);
                if(y[i]<dy&&x[i]==dx){
                    y[i]++;
                }else if(y[i]>dy&&x[i]==dx){
                    y[i]--;
                }else if(y[i]==dy&&x[i]<dx){
                    x[i]++;
                }else if(y[i]==dy&&x[i]>dx){
                    x[i]--;
                }else if(y[i]<dy&&x[i]<dx){
                    y[i]++;
                    x[i]++;
                }else if(y[i]>dy&&x[i]<dx){
                    y[i]--;
                    x[i]++;
                }else if(y[i]<dy&&x[i]>dx){
                    y[i]++;
                    x[i]--;
                }else if(y[i]>dy&&x[i]>dx){
                    y[i]--;
                    x[i]--;
                }else{
                    koma[i].setBorder(new LineBorder(Color.red,2,true));
                    isKoma[i] = false;
                    isMoving[i] = false;
                    isSelected = false;
                }
                try{
                    Thread.sleep(time);
                }catch(Exception e){

                }

            }
        }
    }
    class MoveHand extends Thread{
        int dx;
        int dy;
        int i;
        MoveHand(int state, int i){
            if(state==0){
                dx = 15+HandNum1*90;
                dy = 715;
                if (isHand1[HandNum1]) {
                    while(true) {
                        HandNum1++;
                        if (HandNum1 > 2) {
                            HandNum1 = 0;
                        }
                        if(isHand1[HandNum1]){
                            isHand1[HandNum1] = true;
                            HandNum1++;
                            if (HandNum1 > 2) {
                                HandNum1 = 0;
                            }
                            break;
                        }
                    }
                }else{
                    isHand1[HandNum1] = true;
                    HandNum1++;
                    if (HandNum2 > 2) {
                        HandNum2 = 0;
                    }
                }
            }else{
                dx = 585-(HandNum2+1)*90;
                dy = 15;
                if (isHand2[HandNum2]) {
                    while(true) {
                        HandNum2++;
                        if (HandNum2 > 2) {
                            HandNum2 = 0;
                        }
                        if(isHand2[HandNum2]){
                            isHand2[HandNum2] = true;
                            HandNum2++;
                            if (HandNum2 > 2) {
                                HandNum2 = 0;
                            }
                            break;
                        }
                    }
                }else{
                    isHand2[HandNum2] = true;
                    HandNum2++;
                    if (HandNum2 > 2) {
                        HandNum2 = 0;
                    }
                }
            }
            this.i = i;
        }
        public void run(){
            while(true){
                if(isMoving[i]){
                    continue;
                }
                koma[i].setBounds(x[i], y[i],80,80);
                if(y[i]<dy&&x[i]==dx){
                    y[i]++;
                }else if(y[i]>dy&&x[i]==dx){
                    y[i]--;
                }else if(y[i]==dy&&x[i]<dx){
                    x[i]++;
                }else if(y[i]==dy&&x[i]>dx){
                    x[i]--;
                }else if(y[i]<dy&&x[i]<dx){
                    y[i]++;
                    x[i]++;
                }else if(y[i]>dy&&x[i]<dx){
                    y[i]--;
                    x[i]++;
                }else if(y[i]<dy&&x[i]>dx){
                    y[i]++;
                    x[i]--;
                }else if(y[i]>dy&&x[i]>dx){
                    y[i]--;
                    x[i]--;
                }else {
                    RotateIcon(i, 7-i);
                    break;
                }
                try{
                    Thread.sleep(2);
                }catch(Exception e){

                }
            }
        }
    }
}
