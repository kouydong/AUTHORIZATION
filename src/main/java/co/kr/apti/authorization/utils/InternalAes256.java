package co.kr.apti.authorization.utils;

import co.kr.apti.authorization.exception.DecryptionException;
import co.kr.apti.authorization.exception.EncryptionException;
import org.springframework.http.HttpStatus;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static co.kr.apti.authorization.constant.ErrorCode.DECRYPTION_EXCEPTION;
import static co.kr.apti.authorization.constant.ErrorCode.ENCRYPTION_EXCEPTION;

/**
 * @author Kouydong
 * @apiNote AES256암호화를 위한 Encryption 및 Decryption 처리를 진행
 */
public class InternalAes256 {
    public static String alg = "AES/CBC/PKCS5Padding";
    private static final String key = "U!8cQv+63Q*Yry$6F#%2+!1)!E37In%^";
    private static final String iv  = "abcdef1234567890"; // 16byte


    /**
     * @author Kouydong
     * @apiNote AES256 암호화 모듈
     * @param text 문자열
     * @return 암호화된문자열
     */
    public static String encrypt(String text) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));

            return Base64.getEncoder().encodeToString(encrypted);
        } catch(Exception e) {
                throw new EncryptionException(ENCRYPTION_EXCEPTION.getCode(), ENCRYPTION_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /**
     * @author Kouydong
     * @apiNote AES256 복호화 모듈
     * @param cipherText 암호화문자열
     * @return 복호화된문자열
     */
    public static String decrypt(String cipherText) throws DecryptionException {
        //─────────────────────────────────────────────────────────────────────────────────────────────
        try {
            //─────────────────────────────────────────────────────────────────────────────────────────────
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            //─────────────────────────────────────────────────────────────────────────────────────────────
            return new String(decrypted, "UTF-8");
            //─────────────────────────────────────────────────────────────────────────────────────────────
        } catch(Exception e) {
            //─────────────────────────────────────────────────────────────────────────────────────────────
            throw new DecryptionException(DECRYPTION_EXCEPTION.getCode(), DECRYPTION_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR);
            //─────────────────────────────────────────────────────────────────────────────────────────────
        }
        //─────────────────────────────────────────────────────────────────────────────────────────────
    }
    //─────────────────────────────────────────────────────────────────────────────────────────────
}