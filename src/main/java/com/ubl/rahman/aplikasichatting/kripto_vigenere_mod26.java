package com.ubl.rahman.aplikasichatting;

/**
 * Created by Rahman on 15-Jun-17.
 */

public class kripto_vigenere_mod26 {
    public static String BagianEnkripsi(String NomorKeluar){
        String Ctext = "";
        int k[] = {49, 52, 51, 50};
        int i;
        int d=0;

        for(i=0; i<NomorKeluar.length(); i++){
            if(d==4){
                d=0;
                Ctext = Ctext + (char) ((((NomorKeluar.charAt(i))+k[d])%26)+65);
                d++;
            }else{
                Ctext = Ctext + (char) ((((NomorKeluar.charAt(i))+k[d])%26)+65);
                d++;
            }
        }
        return Ctext;
    }

    public static String BagianDekripsi(String NomorMasuk){
        String Ptext = "";
        int PtextSementara;
        int k[] = {49, 52, 51, 50};
        int i;
        int d=0;

        for(i=0; i<NomorMasuk.length(); i++){
            if (d==4){
                d=0;
                PtextSementara = ((NomorMasuk.charAt(i))-65);
                if ((PtextSementara-k[d])<0){
                    PtextSementara = (PtextSementara-k[d])+98;
                }else  {
                    PtextSementara = (PtextSementara-k[d]);
                }

                Ptext = Ptext + (char) ((PtextSementara%26)+32);
                d++;
            }else {
                PtextSementara = ((NomorMasuk.charAt(i))-65);
                if ((PtextSementara-k[d])<0){
                    PtextSementara = (PtextSementara-k[d])+98;
                }else  {
                    PtextSementara = (PtextSementara-k[d]);
                }

                Ptext = Ptext + (char) ((PtextSementara%26)+32);
                d++;
            }
        }
        return Ptext;
    }
}
