package com.ubl.rahman.aplikasichatting;

/**
 * Created by Rahman on 5/27/2017.
 */

public class kripto_vigenere {
    public static String BagianEnkripsi(String PesanKeluar){
        String Ctext = "";
        int k[] = {82, 52, 104, 97, 53, 49, 65};
        int i;
        int d=0;

        for(i=0; i< PesanKeluar.length(); i++){

            if(d==7){
                d=0;
                Ctext = Ctext+(char) (((PesanKeluar.charAt(i)) + k[d])%96);
                d++;
            }else{
                Ctext = Ctext+(char) (((PesanKeluar.charAt(i)) + k[d])%96);
                d++;
            }
        }
        return Ctext;
    }

    public static String BagianDekripsi(String PesanMasuk){
        String Ptext = "";
        int PtextSementara;
        int k[] = {82, 52, 104, 97, 53, 49, 65};
        int i;
        int d=0;

        for(i=0; i<PesanMasuk.length(); i++){
            if(d==7){
                d=0;
                if((PesanMasuk.charAt(i)-k[d])<0 ){
                    PtextSementara = (((PesanMasuk.charAt(i))-k[d])+96);
                }else{
                    PtextSementara = ((PesanMasuk.charAt(i))-k[d]);
                }

                if((PtextSementara %96)<32){
                    PtextSementara = (((PtextSementara %96)-32)+128);
                }else {
                    PtextSementara = (PtextSementara%96);
                }

                Ptext = Ptext+(char) PtextSementara;
                d++;

            }else{
                if((PesanMasuk.charAt(i)-k[d])<0 ){
                    PtextSementara = (((PesanMasuk.charAt(i))-k[d])+96);
                }else{
                    PtextSementara = ((PesanMasuk.charAt(i))-k[d]);
                }

                if((PtextSementara %96)<32){
                    PtextSementara = (((PtextSementara %96)-32)+128);
                }else {
                    PtextSementara = (PtextSementara%96);
                }
                Ptext = Ptext+(char) PtextSementara;
                d++;
            }
        }
        return Ptext;
    }
}
