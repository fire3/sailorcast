package com.crixmod.sailorcast.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fire3 on 14-12-31.
 */
public class MD5 {
    private static String toHexString(byte[] paramArrayOfByte)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        int i = paramArrayOfByte.length;
        for (int j = 0; j < i; j++)
        {
            int k = paramArrayOfByte[j];
            if (k < 0)
                k += 256;
            if (k < 16)
                localStringBuilder.append("0");
            localStringBuilder.append(Integer.toHexString(k));
        }
        return localStringBuilder.toString();
    }

    public static String toMd5(String paramString)
    {
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.reset();
            localMessageDigest.update(paramString.getBytes("utf-8"));
            String str = toHexString(localMessageDigest.digest());
            return str;
        }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
        {
            throw new RuntimeException(localNoSuchAlgorithmException);
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
            localUnsupportedEncodingException.printStackTrace();
        }
        return "";
    }
}
