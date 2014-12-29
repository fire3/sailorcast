package com.decapi;

/**
 * Created by fire3 on 14-12-29.
 */
public class Decryptions {
    static
    {
        System.loadLibrary("algms");
    }

    private String DESDec(String paramString1, int paramInt, String paramString2)
    {
        return nativeDESDec(paramString1, paramInt, paramString2);
    }

    private String DESEnc(String paramString1, String paramString2)
    {
        return nativeDESEnc(paramString1, paramString2);
    }

    private static native String nativeAESDec(byte[] paramArrayOfByte, int paramInt, String paramString);

    private static native byte[] nativeAESEnc(String paramString1, String paramString2);

    private static native String nativeDESDec(String paramString1, int paramInt, String paramString2);

    private static native String nativeDESEnc(String paramString1, String paramString2);

    public String AESDec(String paramString1, int paramInt, String paramString2)
    {
        byte[] arrayOfByte = new byte[paramString1.length()];
        char[] arrayOfChar = paramString1.toCharArray();
        for (int i = 0; i < arrayOfByte.length; i++)
            arrayOfByte[i] = ((byte)arrayOfChar[i]);
        return nativeAESDec(arrayOfByte, paramInt, paramString2);
    }

    public String AESEnc(String paramString1, String paramString2)
    {
        byte[] arrayOfByte = nativeAESEnc(paramString1, paramString2);
        char[] arrayOfChar = new char[arrayOfByte.length];
        for (int i = 0; i < arrayOfByte.length; i++)
            arrayOfChar[i] = ((char)arrayOfByte[i]);
        return String.valueOf(arrayOfChar);
    }
}
