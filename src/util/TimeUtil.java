package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * <p>Title:TimeUtil</p>
 * <p>Description:根据固定格式转换时间，字符串转换为Date，或字符串转化为标准化字符串</P>
 * @author:jx
 * @date:2016年10月12日 上午10:02:22
 */


public class TimeUtil {


	public static String format(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(Long.parseLong(time));
	}

	public static String format(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time);
	}


	public static String blogFormat(String spanishTime){
		if(null == spanishTime){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

		String timeLong = null;
		try {
			timeLong = String.valueOf(sdf.parse(spanishTime).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format(timeLong);
	}

	public static String newsFormat(String spanishTime){
		if(null == spanishTime){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
		String timeLong = null;
		try {
			timeLong = String.valueOf(sdf.parse(spanishTime).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format(timeLong);
	}

	public static String forumFormat(String spanishTime){
		if(null == spanishTime){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy,hh:mm", Locale.ENGLISH);
		String timeLong = null;
		try {
			timeLong = String.valueOf(sdf.parse(spanishTime).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format(timeLong);
	}


	public static void main(String[] args) throws ParseException {
//		testBlog();
//		testForum();
		testNews();
	}

	public static void testBlog(){
		String time1 = "3 de mayo de 2014";
		System.out.println(blogFormat(time1));

	}
	public static void testForum(){
		String time1 = "04/10/2013,11:45";
		System.out.println(forumFormat(time1));

	}
	public static void testNews(){
		String time1 = "Feb 13, 2017 7:00 am";
		System.out.println(newsFormat(time1));

	}

}
