package homework;

import java.io.Serializable;
public class koma implements Serializable{
 private String name;
 private Vector2D vector[];//駒が動ける場所を定義
 private state state;
 private komaKinds KomaKinds;
 
 koma(state state, komaKinds KomaKinds)
 {
	 this.state = state;
	 if(KomaKinds == komaKinds.Lion) 
	 {
		 this.name = "ライオン";
		 this.vector =  new Vector2D[]{new Vector2D(0, 1), new Vector2D(1, 1), new Vector2D(1, 0)
					,new Vector2D(1, -1), new Vector2D(0, -1), new Vector2D(-1, -1)
					,new Vector2D(-1, 0), new Vector2D(-1, 1)};//ライオンは全方向動けるのでそのベクトルを保持してる
		 this.KomaKinds = KomaKinds;
	 }
	 else if(KomaKinds == komaKinds.Elephant) 
	 {
		 this.name = "ぞう";
		 this.vector = new Vector2D[] {new Vector2D(1, 1), new Vector2D(1, -1), new Vector2D(-1, -1), new Vector2D(-1,1)};
		 this.KomaKinds = KomaKinds;
	 }
	 else if(KomaKinds == komaKinds.Girafee) 
	 {
		 this.name = "きりん";
		 this.vector = new Vector2D[] {new Vector2D(0, 1), new Vector2D(1, 0), new Vector2D(0, -1), new Vector2D(-1,0)};
		 this.KomaKinds = KomaKinds;
	 }
	 else if(KomaKinds == komaKinds.Chick&&state==state.Enemy) 
	 {
		 this.name = "ひよこ";
		 this.vector = new Vector2D[] {new Vector2D(0,1)};
		 this.KomaKinds = KomaKinds;
	 }
	 else if(KomaKinds == komaKinds.Chick&&state==state.My) 
	 {
		 this.name = "ひよこ";
		 this.vector = new Vector2D[] {new Vector2D(0,-1)};
		 this.KomaKinds = KomaKinds;
	 }
	 else if(KomaKinds == komaKinds.Bird&&state==state.My) 
	 {
		 this.name = "鶏";
		 this.vector = new Vector2D[]{new Vector2D(0, 1), new Vector2D(1, 1), new Vector2D(1, 0)
					, new Vector2D(0, -1),new Vector2D(-1, 0), new Vector2D(-1, 1)};
		 this.KomaKinds = KomaKinds;
	 }
	 else if(KomaKinds == komaKinds.Bird&&state==state.Enemy) 
	 {
		 this.name = "鶏";
		 this.vector =  new Vector2D[]{new Vector2D(0, 1), new Vector2D(1, 0)
					,new Vector2D(1, -1), new Vector2D(0, -1), new Vector2D(-1, -1),new Vector2D(-1, 0)};
		 this.KomaKinds = KomaKinds;
	 }
	
 }
 
 koma(komaKinds KomaKinds)
 {
	 this.name = "nothing";
	 this.KomaKinds = KomaKinds;
 }
 
 public Vector2D[] getVector()
 {
	 return this.vector;
 }
 
 public String getName() 
 {
	 return this.name;
 }
 
 public komaKinds getkomaKinds() 
 {
	 return this.KomaKinds;
 }
 
 public state getState() 
 {
	 return this.state;
 }
 
 public void setState(state state) 
 {
	 this.state = state;
 }
 }
//駒の動物の種類

enum komaKinds
{
	Lion,
	Girafee,
	Chick,
	Elephant,
	Bird,
	Nothing
	
}
