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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try{

            Date date= sdf.parse(sdf.format(dataValue));
            this.dtDataCorrente = date;

        }catch(Exception e){

        }

    }

    /**
     * Metodo que retorna o nome da estacao do ano
     * @return String Nome da estacao do ano
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

    /**
     * Metodo que retorna o nome da estacao do ano
     * @param data Data corrente
     * @return String Nome da estacao do ano
     */
    public String getTimeName(String data){
        String strTime;

        SimpleDateFormat dt=new SimpleDateFormat("HH:mm:ss");
        dt.setLenient(false);

        try
        {
            Date date= dt.parse(dt.format(dtDataCorrente));

            if(date.after(dt.parse("08:00:00")) && date.before(dt.parse("22:00:00"))){
                strTime = "Dia";
            }
            else strTime="Noite";
        }catch(Exception e){
            strTime="Horas Invalidas";
        }

        return strTime;
    }
}
