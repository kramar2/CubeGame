package com.example.panklocek;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent.OnFinished;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PanKlocekMain extends Activity {

	public DataCarier f;
	
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_klocek_main);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        f=new DataCarier(9);

      //  f.fileWrite("kvuvlvlvlvlvlvlvlvlxkv0.0v0.0v0.0v0.0v0.0v0.0v0.0v0.0v0.0", "game_data1");
        
        
    //    f.ReadSettings();//"kvuvlvlvlvlvlvlvlvlxkv0.0v0.0v0.0v0.0v0.0v0.0v0.0v0.0v0.0";
        
      //  Toast.makeText(this, "odczyt: "+ f.lvl_unlocks.toString() +" ++ "+f.lvl_times.toString() , Toast.LENGTH_SHORT).show();
        
    //    f.ReadStamp();
    }
    
    @Override
    protected void onStop() {
    	
    	f.WriteSettings();
    	super.onStop();
    }

    @Override
    protected void onStart() {
    	f.ReadSettings();
    	f.ReadStamp();
    	super.onStart();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    
    
    public void setLevelsAvability(){
    	findViewById(R.id.b1).setEnabled(f.lvl_unlocks.get(0));
    	findViewById(R.id.b2).setEnabled(f.lvl_unlocks.get(1));
    	findViewById(R.id.b3).setEnabled(f.lvl_unlocks.get(2));
    	findViewById(R.id.b4).setEnabled(f.lvl_unlocks.get(3));
    	findViewById(R.id.b5).setEnabled(f.lvl_unlocks.get(4));
    	findViewById(R.id.b6).setEnabled(f.lvl_unlocks.get(5));
    	findViewById(R.id.b7).setEnabled(f.lvl_unlocks.get(6));
    	findViewById(R.id.b8).setEnabled(f.lvl_unlocks.get(7));
    	findViewById(R.id.b9).setEnabled(f.lvl_unlocks.get(8));
    }
    
    
    public void startFunc(View vx){
    	setContentView(R.layout.activity_pan_klocek_start);
    	setLevelsAvability();
        
     //   b1=(Button)findViewById(R.id.b4);
     //   b1.setEnabled(false);
      //  f.fileWrite(""); 
     //   Toast.makeText(this, "zapis "  , Toast.LENGTH_LONG).show();

    }
  
    public void Instrukcja(View vx){
    	setContentView(R.layout.activity_pan_klocek_ins);
    	TextView tv=(TextView)findViewById(R.id.textView2);
    	
    	String ts="";
    	int i=0;
    	while(i<f.lvls){
    		ts=ts+"Poziom "+(i+1)+" :: Najlepszy czas: "+f.lvl_times.get(i)+" \n";
    		i++;
    	}
    	tv.setText(ts);
    }
    
    
    
    public void Ustawienia(View vx){
    	setContentView(R.layout.activity_pan_klocek_autor);

    }
    public void executeCode(View vx){
    	EditText et =(EditText)findViewById(R.id.editText1);
    	if(et.getText().toString().compareTo("reset")==0){
    		int i=0;
    		f.lvl_unlocks.set(i, true);
    		i++;
    		while(i<f.lvls){
    			f.lvl_unlocks.set(i, false);
    			i++;
    		}
    		Toast.makeText(this, "kod zaakceptowany!" , Toast.LENGTH_SHORT).show();
    		et.setText("");
    	}else if(et.getText().toString().compareTo("bt7")==0){
    		int i=0;
    		while(i<f.lvls){
    			f.lvl_unlocks.set(i, true);
    			i++;
    		}
    		Toast.makeText(this, "kod zaakceptowany!" , Toast.LENGTH_SHORT).show();
    		et.setText("");
    	}else if(et.getText().toString().compareTo("resettimes")==0){
    		int i=0;
    		while(i<f.lvls){
    			f.lvl_times.set(i,0.0f);
    			i++;
    		}
    		Toast.makeText(this, "kod zaakceptowany!" , Toast.LENGTH_SHORT).show();
    		et.setText("");
    	}else{
    		et.setText("");
    	}
    }
    
    
    
    public void quit(View vx){
    	finish();
    }
    
    public void back(View vx){
    	setContentView(R.layout.activity_pan_klocek_main);
    }
    
    
    
    
   
    
    
    public void load_level(int i){
    	
    	String[] maps= { "tmx/mission01.tmx",
    			         "tmx/mxtest.tmx",
    			         "tmx/mission03.tmx",
    			         "tmx/mission04.tmx",
    			         "tmx/mission05.tmx",
    			         "tmx/mission06.tmx",
    			         "tmx/mission07.tmx",
    			         "tmx/mission08.tmx",
    			         "tmx/mission09.tmx",
    	};
    	
    	PanKlocekGame.missionName=maps[i];
    	this.startActivity(new Intent(this, PanKlocekGame.class));
    }
    
    public void lvl1(View vx){
     	load_level(0);
    }
    public void lvl2(View vx){
     	load_level(1);
    }
    public void lvl3(View vx){
     	load_level(2);
    }
    public void lvl4(View vx){
     	load_level(3);
    }
    public void lvl5(View vx){
     	load_level(4);
    }
    public void lvl6(View vx){
     	load_level(5);
    }
    public void lvl7(View vx){
     	load_level(6);
    }
    public void lvl8(View vx){
     	load_level(7);
    }
    public void lvl9(View vx){
     	load_level(8);
    }

    
    public class DataCarier {
    	
    	
    	public int lvls=9;
    	public List<Boolean> lvl_unlocks=new ArrayList<Boolean>();
    	public List<Float> lvl_times=new ArrayList<Float>();

    	public DataCarier(int map_count){
    		lvls=map_count;
    		int i=0;
    		while(i<lvls){
    			lvl_unlocks.add(true);
    			lvl_times.add(0.0f);
    			i++;
    		}
    	}
    	
    	public void WriteSettings(){
    			String s="";
    			String su="";
    			String st="";
        		int i=0;
        		while(i<lvls){
        			if(lvl_unlocks.get(i)==true){
        				su=su+"vu";
        			}else{
        				su=su+"vl";
        			}
        			st=st+"v"+lvl_times.get(i).toString();
        			i++;
        		}
        		s="k"+su+"xk"+st;
        		fileWrite(s,"game_data1");
    	}
    	
    	public void ReadSettings(){
    		String s=fileRead("game_data1");
    		String unl=s.split("x")[0];
    		String tim=s.split("x")[1];
    		
    		String[] unls=unl.split("v");
    		String[] tims=tim.split("v");
    		int i=1;
    		while(i<=lvls){
    			if(unls[i].compareTo("u")==0){
    				lvl_unlocks.set(i-1, true);
    			}else {
    				lvl_unlocks.set(i-1, false);
    			}
    			lvl_times.set(i-1, Float.parseFloat(tims[i]));
    			
        	//	Toast.makeText(PanKlocekMain.this, ""+i+"st:"+unls[i-1]+":ts"+"st:"+tims[i-1]+":ts"  , Toast.LENGTH_LONG).show();
        		i++;
    		}
    		
    		
    	}	
    	
        public void fileWrite(String data,String FILENAME){
        	//String FILENAME = "game_data1";
        	String string = data;

        	FileOutputStream fos;
    		try {
    			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
    	    	fos.write(string.getBytes());
    	    	fos.close();
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}

        }

        
        public String fileRead(String FILENAME){
        	//String FILENAME = "game_data1";
        	String string = "";
        	FileInputStream fis;
    		try {
    			try {
    			fis = openFileInput(FILENAME);
    			int content;
    			while ((content = fis.read()) != -1) {
    				string=string+(char)content;
    			}
    			fis.close();
    			}catch(FileNotFoundException e){
    				if(FILENAME.compareTo("game_data1")==0){
	    				string="kvuvlvlvlvlvlvlvlvlxkv0.0v0.0v0.0v0.0v0.0v0.0v0.0v0.0v0.0";
	    				fileWrite(string,FILENAME);
	    				return string;
    				}else if(FILENAME.compareTo("stamp")==0){
	    				string="null";
	    				fileWrite(string,FILENAME);
	    				return string;
    				}
    			}
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}
    		return string;

        }
        
        
        public void NullStamp(){
        	fileWrite("null","stamp");
        }
        
        public void ReadStamp(){
        	String s=fileRead("stamp");
        	if(s.compareTo("null")==0){
        		// nope
        	}else{
        		int mn=Integer.parseInt(s.split("_")[0]);
        		float mt=Float.parseFloat(s.split("_")[1]);
        		
        		if(lvl_times.get(mn-1)<0.1f || lvl_times.get(mn-1)>mt){
        			lvl_times.set(mn-1,mt);
        		}
        		if(mn<lvls){
        			lvl_unlocks.set(mn, true);
        		}
        		WriteSettings();
        		
        		Toast.makeText(PanKlocekMain.this, "Poziom "+mn+" uko�czony!\nCzas: "+mt  , Toast.LENGTH_LONG).show();
        		
        		
        		NullStamp();
        	}
        	
        }
        
    }
    
    
    
    
    
    
}
