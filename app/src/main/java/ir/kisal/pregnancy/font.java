package ir.kisal.pregnancy;
import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;


public class font extends Activity implements RadioGroup.OnCheckedChangeListener{
	 public static String font="tahoma.ttf";
	 RadioGroup rg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.font);
		rg=(RadioGroup)findViewById(R.id.radioGroup);
		rg.setOnCheckedChangeListener(this);
	
		//-----------------------------
	}
	
	
	protected String getFont()
	{
		return font;
	}


	public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
		
		case R.id.radio1:
			font="tahoma.ttf";

				finish();
			break;
		case R.id.radio2:
			font="BZar.ttf";
			finish();
			break;
		
		}
		
	}
}
