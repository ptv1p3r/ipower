package pt.ismat.ipower.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Pedro Roldan on 06-01-2017.
 * @version 0.0
 */
public class Dates {
    private Date dtDataCorrente;

    /**
     * Construtor
     * @param dataValue Data corrente
     */
    public Dates(Date dataValue) {
        this.dtDataCorrente = dataValue;
    }

    /**
     * Metodo que retorna o nome da estacao do ano
     * @return
     */
    public String getSeasonName(){
        String strSeason;

        SimpleDateFormat dt=new SimpleDateFormat("MM-dd");
        dt.setLenient(false);

        try
        {
            Date date= dt.parse(dt.format(dtDataCorrente));

            if(date.after(dt.parse("04-21")) && date.before(dt.parse("06-21"))){
                strSeason = "Primavera";
            }
            else if(date.after(dt.parse("06-20")) && (date.before(dt.parse("09-23"))))
            {
                strSeason = "Verão";
            }
            else if(date.after(dt.parse("09-22")) && date.before(dt.parse("12-22")))
            {
                strSeason="Outono";
            }
            else strSeason="Inverno";
        }catch(Exception e){
            strSeason="Data Invalida";
        }

        return strSeason;
    }
}
