package tlt.th.co.toyotaleasing.modules.main;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Convert {

    //HashKey Generator
    public static String getProjectHashKey(Context context) {
        String hashKey = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(signature.toByteArray());
                hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", hashKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashKey;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    void hashBigInteger(String s){
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte b[] = hexStringToByteArray(s);
            sha.update(b,0,b.length);
            byte digest[] = sha.digest();
            BigInteger d = new BigInteger(1,digest);

            System.out.println("H "+d.toString(16));
        }catch (NoSuchAlgorithmException e){
            throw new UnsupportedOperationException(e);
        }
    }

}
