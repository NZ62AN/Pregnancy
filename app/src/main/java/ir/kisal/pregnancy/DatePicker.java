package ir.kisal.pregnancy;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class DatePicker extends LinearLayout {

	private int startYear = 1279;
	private int endYear = 1632;

	private View myPickerView;

	private Button month_plus;
	private EditText month_display;
	private Button month_minus;

	private Button date_plus;
	private EditText date_display;
	private Button date_minus;

	private Button year_plus;
	private EditText year_display;
	private Button year_minus;
	
	private TextView text;

	private Calendar cal;
	private CalendarTool calIR;

	public DatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context mContext) {
		LayoutInflater inflator = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myPickerView = inflator.inflate(R.layout.datepicker, null);
		this.addView(myPickerView);

		initializeReference();
	}

	private void initializeReference() {

		month_plus = (Button) myPickerView.findViewById(R.id.month_plus);
		month_plus.setOnClickListener(month_plus_listener);
		month_display = (EditText) myPickerView
				.findViewById(R.id.month_display);
		month_minus = (Button) myPickerView.findViewById(R.id.month_minus);
		month_minus.setOnClickListener(month_minus_listener);

		date_plus = (Button) myPickerView.findViewById(R.id.date_plus);
		date_plus.setOnClickListener(date_plus_listener);
		date_display = (EditText) myPickerView.findViewById(R.id.date_display);
		date_display.addTextChangedListener(date_watcher);
		date_minus = (Button) myPickerView.findViewById(R.id.date_minus);
		date_minus.setOnClickListener(date_minus_listener);

		year_plus = (Button) myPickerView.findViewById(R.id.year_plus);
		year_plus.setOnClickListener(year_plus_listener);
		year_display = (EditText) myPickerView.findViewById(R.id.year_display);
		year_display.setOnFocusChangeListener(mLostFocusYear);
		year_display.addTextChangedListener(year_watcher);
		year_minus = (Button) myPickerView.findViewById(R.id.year_minus);
		year_minus.setOnClickListener(year_minus_listener);

		text= (TextView) myPickerView.findViewById(R.id.textView1);

		initData();
		initFilterNumericDigit();

	}

	private void initData() {
		cal = Calendar.getInstance();
		calIR=new CalendarTool(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
		text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));
		
		month_display.setText(months[calIR.getIranianMonth()-1]);
		date_display.setText(String.valueOf(calIR.getIranianDay()));
		year_display.setText(String.valueOf(calIR.getIranianYear()));
	}
	
	private void initFilterNumericDigit() {

		try {
			date_display.setFilters(new InputFilter[] { new InputFilterMinMax(
					1,31) });

			InputFilter[] filterArray_year = new InputFilter[1];
			filterArray_year[0] = new InputFilter.LengthFilter(4);
			year_display.setFilters(filterArray_year);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeFilter() {
		try {

			date_display.setFilters(new InputFilter[] { new InputFilterMinMax(
					1, 31) });
		} catch (Exception e) {
			date_display.setText(""+ String.valueOf(calIR.getIranianDay()));
			calIR.setGregorianDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));

			e.printStackTrace();
		}
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) throws Exception {
		if (startYear < 1632 && startYear > 1279) {
			this.startYear = startYear;
			swapStartEndYear();
		} else {
			throw new NumberFormatException(
					"StartYear should be in the range of 1279 to 1632");
		}
	}

	public void reset() {
		initData();
	}

	public int getEndYear() {
		return endYear;
	}

	public void setDateChangedListener(DateWatcher listener) {
		this.mDateWatcher = listener;
	}

	public void removeDateChangedListener() {
		this.mDateWatcher = null;
	}

	public void setEndYear(int endYear) throws Exception {
		if (endYear < 1632 && endYear > 1279) {
			this.endYear = endYear;
			swapStartEndYear();
		} else {
			throw new NumberFormatException(
					"endYear should be in the range of 1279 to 1632");
		}
	}

	String[] months = {  "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان",
			"آذر", "دی", "بهمن" ,"اسفند"};

	OnClickListener month_plus_listener = new OnClickListener() {

		public void onClick(View v) {

			try {
				calIR.setIranianDate(calIR.getIranianYear(), calIR.getIranianMonth()+1, calIR.getIranianDay());
				
				text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));
				
				month_display.setText(months[calIR.getIranianMonth()-1]);
				year_display.setText(String.valueOf(calIR.getIranianYear()));
				date_display.setText(String.valueOf(calIR.getIranianDay()));
				
				changeFilter();
				sendToListener();
			} catch (Exception e) {
				Log.e("", e.toString());
			}
		}
	};
	OnClickListener month_minus_listener = new OnClickListener() {

		public void onClick(View v) {
			try {
               calIR.setIranianDate(calIR.getIranianYear(), calIR.getIranianMonth()-1, calIR.getIranianDay());
				
				text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));
				
				month_display.setText(months[calIR.getIranianMonth()-1]);
				year_display.setText(String.valueOf(calIR.getIranianYear()));
				date_display.setText(String.valueOf(calIR.getIranianDay()));

				changeFilter();
				sendToListener();
			} catch (Exception e) {
				Log.e("", e.toString());
			}
		}
	};

	OnClickListener date_plus_listener = new OnClickListener() {

		public void onClick(View v) {

			try {
				date_display.requestFocus();
				
				calIR.setIranianDate(calIR.getIranianYear(), calIR.getIranianMonth(), calIR.getIranianDay()+1);
				text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));
				
				month_display.setText(months[calIR.getIranianMonth()-1]);
				year_display.setText(String.valueOf(calIR.getIranianYear()));
				date_display.setText(String.valueOf(calIR.getIranianDay()));
				
				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}

		
	};
	OnClickListener date_minus_listener = new OnClickListener() {

		public void onClick(View v) {

			try {
				date_display.requestFocus();
				
				calIR.setIranianDate(calIR.getIranianYear(), calIR.getIranianMonth(), calIR.getIranianDay()-1);
				text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));
				
				month_display.setText(months[calIR.getIranianMonth()-1]);
				year_display.setText(String.valueOf(calIR.getIranianYear()));
				date_display.setText(String.valueOf(calIR.getIranianDay()));
				

				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		
	};
	OnClickListener year_plus_listener = new OnClickListener() {

		public void onClick(View v) {
			try {
				year_display.requestFocus();

				if (calIR.getIranianYear() >= endYear) {
					calIR.setIranianDate(startYear, calIR.getIranianMonth(), calIR.getIranianDay());
					

				} else {
					calIR.setIranianDate(calIR.getIranianYear()+1, calIR.getIranianMonth(), calIR.getIranianDay());
				}
				text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));
				
				month_display.setText(months[calIR.getIranianMonth()-1]);
				date_display.setText(String.valueOf(calIR.getIranianDay()));
				year_display.setText(String.valueOf(calIR.getIranianYear()));


				changeFilter();
				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};
	OnClickListener year_minus_listener = new OnClickListener() {

		public void onClick(View v) {
			try {
				year_display.requestFocus();

				if (calIR.getIranianYear() <= startYear) {
					calIR.setIranianDate(endYear, calIR.getIranianMonth(), calIR.getIranianDay());

				} else {
					calIR.setIranianDate(calIR.getIranianYear()-1, calIR.getIranianMonth(), calIR.getIranianDay());
				}

				text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));
				
				month_display.setText(months[calIR.getIranianMonth()-1]);
				date_display.setText(String.valueOf(calIR.getIranianDay()));
				year_display.setText(String.valueOf(calIR.getIranianYear()));


				changeFilter();
				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		
	};

	OnFocusChangeListener mLostFocusYear = new OnFocusChangeListener() {

		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {

				year_display.setText(String.valueOf(calIR.getIranianYear()));
				text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));


			}
		}

		
	};

	class InputFilterMinMax implements InputFilter {

		private int min, max;

		public InputFilterMinMax(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public InputFilterMinMax(String min, String max) {
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}

		

		private boolean isInRange(int a, int b, int c) {
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}

		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			try {
				int input = Integer.parseInt(dest.toString()
						+ source.toString());
				if (isInRange(min, max, input)) {
					return null;
				}
			} catch (NumberFormatException nfe) {
			}
			return "";
		}
	}

	TextWatcher date_watcher = new TextWatcher() {

	

		public void afterTextChanged(Editable s) {
			try {
				if (s.toString().length() > 0) {
					// Log.e("", "afterTextChanged : " + s.toString());
					
					calIR.setIranianDate(calIR.getIranianYear(), calIR.getIranianMonth(),Integer.parseInt(s.toString()));
					month_display.setText(months[calIR.getIranianMonth()-1]);
					
				   
					text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));

					sendToListener();
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		
		
	};

	TextWatcher year_watcher = new TextWatcher() {


		public void afterTextChanged(Editable s) {
			try {
				if (s.toString().length() == 4) {
					int year = Integer.parseInt(s.toString());

					if (year > endYear) {
						calIR.setIranianDate(endYear , calIR.getIranianMonth(), calIR.getIranianDay());
					} else if (year < startYear) {
						calIR.setIranianDate(startYear, calIR.getIranianMonth(), calIR.getIranianDay());
					} else {
						calIR.setIranianDate(year, calIR.getIranianMonth(), calIR.getIranianDay());
					}
				}

				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

	};

	private void sendToListener() {

		if (mDateWatcher != null) {
			mDateWatcher.onDateChanged(cal);
		}

	}

	DateWatcher mDateWatcher = null;

	public interface DateWatcher {
		void onDateChanged(Calendar c);
	}

	private void swapStartEndYear() {
		if (this.startYear > this.endYear) {
			int temp = endYear;
			endYear = startYear;
			startYear = temp;
		}

		calIR.setIranianDate(endYear, calIR.getIranianMonth(), calIR.getIranianDay());
		initDisplay();
	}

	private void initDisplay() {
		text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));
		month_display.setText(months[calIR.getIranianMonth()-1]);
		date_display.setText(String.valueOf(calIR.getIranianDay()));
		year_display.setText(String.valueOf(calIR.getIranianYear()));

	}

	public int getGregorianYear() {
		// TODO Auto-generated method stub
	    return	calIR.getGregorianYear();		
	}

	public int getGregorianMonth() {
		// TODO Auto-generated method stub
		 return	calIR.getGregorianMonth();
	}

	public int getGregorianDayOfMonth() {
		// TODO Auto-generated method stub
		 return	calIR.getGregorianDay();
	}
	
	public int getPersionYear() {
		// TODO Auto-generated method stub
	    return	calIR.getIranianYear();	
	}

	public int getPersianMonth() {
		// TODO Auto-generated method stub
		 return	calIR.getIranianMonth();
	}

	public int getPersianDayOfMonth() {
		// TODO Auto-generated method stub
		 return	calIR.getIranianDay();
	}
	
	public void setTextLable(String str){
		text.setText(str);
	}
	 
	public void setDate(int y,int m, int d){
		
		try {
			
			
			calIR.setIranianDate(y, m, d);
			text.setText(String.valueOf(calIR.getGregorianDate())+ "     "+ String.valueOf(calIR.getIranianDate()));
			
			month_display.setText(months[calIR.getIranianMonth()-1]);
			year_display.setText(String.valueOf(calIR.getIranianYear()));
			date_display.setText(String.valueOf(calIR.getIranianDay()));
			
			sendToListener();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
