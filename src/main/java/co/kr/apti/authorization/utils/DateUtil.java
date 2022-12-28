package co.kr.apti.authorization.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * @author kouydong
 * @apiNote Relating the Data handling utility
 */

public class DateUtil {

    /**
     * @author kouydong
     * @param textDate simply date format 'yyyyMMdd'
     * @param addDay which you want to add(e.g : 1, 2, 3 and so forth)
     * @return String textDate(yyyyMMdd) which is added from 'addDay param'
     * @implNote adding the day which you want to
     */
    public static String addDay(String textDate, int addDay) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date =  dateFormat.parse(textDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, addDay);

        return dateFormat.format(cal.getTime());
    }


    /**
     * @author kouydong
     * @param textDate simply date format 'yyyyMMdd'
     * @param addDay which you want to add(e.g : 1, 2, 3 and so forth)
     * @return int textDate(yyyyMMdd) which is added from 'addDay param'
     * @implNote adding the day which you want to
     */
    public static String addDay(int textDate, int addDay) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date =  dateFormat.parse(String.valueOf(textDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, addDay);

        return dateFormat.format(cal.getTime());
    }


    /**
     * @author kouydong
     * @apiNote 현재 일자를 데이터 포맷에 맞추어 반환(포맷이 존재하지 않는 경우 yyyyMMdd 형태로 반환)
     * @param dateFormat*
     * @return String textDate
     * @throws ParseException 일자 포맷이 적합하지 않은 경우 예외 처리
     */
    public static String getNowDateByDateFormat(Optional<String> dateFormat) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.orElse("yyyyMMdd"));

        Date nowDate = new Date();

        return simpleDateFormat.format(nowDate);
    }


    /**
     * @author kouydong
     * @apiNote 현재 일자를 데이터 포맷에 맞추어 반환(포맷이 존재하지 않는 경우 yyyyMMdd 형태로 반환)
     * @param dateFormat(기본값 : yyyyMMdd)
     * @return long textDate
     * @throws ParseException 일자 포맷이 적합하지 않은 경우 예외 처리
     */
    public static long getNowDateByDateFormat(String dateFormat) throws ParseException {

        if(dateFormat.isBlank()) {
            dateFormat = "yyyyMMdd";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        Date nowDate = new Date();

        return Long.parseLong(simpleDateFormat.format(nowDate));
    }


    /**
     * @author kouydong
     * @apiNote LocalDateTime을 원하는 형식으로 변경하여 문자열 반환
     * @param localDateTime
     * @param dateFormat
     * @return String formatting textDate
     */
    public static String getDateTimeByDateFormat(LocalDateTime localDateTime, String dateFormat) throws ParseException {

        return localDateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }

}
