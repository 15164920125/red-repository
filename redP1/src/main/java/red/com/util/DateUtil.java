package red.com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class DateUtil {
	
	public static List<String> getDate(String startD,String endD) throws Exception{
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cale = Calendar.getInstance();
		cale.setTime(form.parse(startD));
		Calendar caleend = Calendar.getInstance();
		caleend.setTime(form.parse(endD));
		List<String> list = new ArrayList<>();
		list.add(endD);
		
		int j  = 0;
		while(form.parse(endD).after(cale.getTime())){
			cale.add(Calendar.DAY_OF_MONTH, 1);
			Date date = cale.getTime();
			SimpleDateFormat dateform = new SimpleDateFormat("EEEE");
			String string = dateform.format(date);
			if("星期五".equals(string)){
				Calendar instance = Calendar.getInstance();
				instance.setTime(date);
				SimpleDateFormat m = new SimpleDateFormat("yyyy-MM-dd");
				String format = m.format(date);
				j+=1;
				list.remove(0);
				list.add(format);
			}
		}
		System.out.println(JSON.toJSONString(list));
		return list;
	}
	
	public static void main(String[] args) throws Exception {
		DateUtil.getDate("2020-02-29", "2020-03-05");
	}

}
