package com.ubl.rahman.aplikasichatting;

/**
 * Created by Rahman on 4/24/2017.
 */

public class modelclass_isi_pesan {
    private String isiPesan;
    private String pengirimPesan;
    private String waktuDikirim;

    public modelclass_isi_pesan() {
    }

    public modelclass_isi_pesan(String isiPesan, String pengirimPesan, String waktuDikirim) {
        this.isiPesan = isiPesan;
        this.pengirimPesan = pengirimPesan;
        this.waktuDikirim = waktuDikirim;
    }



    public String getIsiPesan() {
        return isiPesan;
    }

    public void setIsiPesan(String isiPesan) {
        this.isiPesan = isiPesan;
    }

    public String getPengirimPesan() {
        return pengirimPesan;
    }

    public void setPengirimPesan(String pengirimPesan) {
        this.pengirimPesan = pengirimPesan;
    }

    public String getWaktuDikirim() {
        return waktuDikirim;
    }

    public void setWaktuDikirim(String waktuDikirim) {
        this.waktuDikirim = waktuDikirim;
    }

}
