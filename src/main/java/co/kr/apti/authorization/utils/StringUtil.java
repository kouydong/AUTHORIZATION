package co.kr.apti.authorization.utils;

public class StringUtil {


    /**
     * @author kouydong
     * @apiNote         문자열을 정수로 반환
     * @param intText   문자형태의 텍스트
     * @param defNumber null인 경우 반환값
     * @return int
     */
    public static int stringToInt(String intText, int defNumber) throws Exception {

        int returnNumber = 0;

        if(intText == null || intText.isEmpty()) {
            returnNumber = defNumber;
        }

        if(intText.chars().allMatch(Character::isDigit)) {
            returnNumber = Integer.parseInt(intText);
        }

        return returnNumber;
    }


    /**
     * @author Kouydong
     * @apiNote 특정 길의의 문자열에 공백값을 특정 문자로 왼쪽부터 치환해서 반환
     * @param word 원본 문자열
     * @param totalWidth 전체 길이
     * @param paddingChar 특정문자로 치환하기 위한 문자
     * @return  패딩처리된 문자열
     */
    public static String padLeft(String word, int totalWidth, char paddingChar) {
        String padWord = word;

        padWord = String.format("%" + totalWidth + "s", word).replace(' ', paddingChar);

        return padWord;
    }
    /**
     * @author Kouydong
     * @apiNote 특정 길의의 문자열에 공백값을 특정 문자로 오른쪽부터 치환해서 반환
     * @param word 원본 문자열
     * @param totalWidth 전체 길이
     * @param paddingChar 특정문자로 치환하기 위한 문자
     * @return  패딩처리된 문자열
     */
    public static String padRight(String word, int totalWidth, char paddingChar) {
        String padWord = word;

        padWord = String.format("%-" + totalWidth + "s", word).replace(' ', paddingChar);

        return padWord;
    }


}
