package barqsoft.footballscores;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{
    public static final int SERIE_A = 401;
    public static final int PREMIER_LEGAUE = 398;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 399;
    public static final int BUNDESLIGA = 394;
    public static final int BUNDESLIGA2 = 395;
    public static String getLeague(int league_num)
    {
        switch (league_num)
        {
            case SERIE_A : return "Seria A";
            case PREMIER_LEGAUE : return "Premier League";
            case CHAMPIONS_LEAGUE : return "UEFA Champions League";
            case PRIMERA_DIVISION : return "Primera Division";
            case BUNDESLIGA : return "Bundesliga";
            case BUNDESLIGA2 : return "Bundesliga 2";
            default: return "Not known League Please report";
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return "Group Stages, Matchday : 6";
            }
            else if(match_day == 7 || match_day == 8)
            {
                return "First Knockout round";
            }
            else if(match_day == 9 || match_day == 10)
            {
                return "QuarterFinal";
            }
            else if(match_day == 11 || match_day == 12)
            {
                return "SemiFinal";
            }
            else
            {
                return "Final";
            }
        }
        else
        {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            case "Real Madrid CF": return R.drawable.rmadrid;
            case "FC Barcelona" : return R.drawable.barcelona;
            case "RCD Espanyol" : return R.drawable.espanyol;
            case "Málaga CF": return R.drawable.malaga;
            case "Real Sociedad de Fútbol" : return R.drawable.rsociedad;
            case "Sevilla FC" : return R.drawable.sevilla;
            case "Valencia CF" : return R.drawable.valencia;
            case "UD Las Palmas" : return R.drawable.udlaspalmas;
            case "RC Celta de Vigo" : return R.drawable.celta_de_vigo;
            case "RC Deportivo La Coruna" : return R.drawable.deportivo_de_la_coruna;
            case "Levante UD" : return R.drawable.levante;
            case "Sporting Gijón" : return R.drawable.sporting_gijon;
            case "Real Valladolid" : return R.drawable.valladolid;
            case "CA Osasuna" : return R.drawable.osasuna;
            case "Real Betis" : return R.drawable.betis;
            case "Club Atlético de Madrid" : return R.drawable.atletico_de_madrid;
            case "Granada CF" : return R.drawable.granada;
            case "Athletic Club" : return R.drawable.athletico_de_bilbao;
            case "Villarreal CF" : return R.drawable.villarreal;
            case "SD Eibar" : return R.drawable.eibar;
            case "Watford FC" : return R.drawable.watford;
            case "Bor. Mönchengladbach" : return R.drawable.bor_monchen;
            case "Hannover 96" : return R.drawable.hannover_96;
            case "VfB Stuttgart" : return R.drawable.vfb_stuttgart;
            case "FC Augsburg" : return R.drawable.augsburg;
            case "Eintracht Frankfurt" : return R.drawable.eintracht_frankfurt;
            case "Bayer Leverkusen" : return R.drawable.bayern_leverkusen;
            case "1. FC Köln" : return R.drawable.koln;
            case "1. FSV Mainz 05" : return R.drawable.mainz_05;
            case "VfL Wolfsburg" : return R.drawable.wlfsbufg;
            case "Werder Bremen" : return R.drawable.werder_bremen;
            case "Southampton FC" : return R.drawable.southampton_fc;
            case "Newcastle United FC" : return R.drawable.newcastle;
            case "Leicester City FC" : return R.drawable.leicester;
            case "Chelsea FC" : return R.drawable.chelsea;
            case "Norwich City FC" : return R.drawable.norwich;
            case "West Bromwich Albion FC" : return R.drawable.west_bromwich;
            case "Arsenal FC" : return R.drawable.arsenal;
            case "Swansea City FC" : return R.drawable.swansea_city_afc;
            case "AFC Bournemouth" : return R.drawable.bournemouth;
            case "Aston Villa FC" : return R.drawable.aston_villa;
            case "FC Bayern München" : return R.drawable.bayer_munchen;
            case "Hertha BSC" : return R.drawable.hertha;
            case "TSG 1899 Hoffenheim" : return R.drawable.tsg;
            case "FC Ingolstadt 04" : return R.drawable.fc_ingolstadt;
            case "Crystal Palace FC" : return R.drawable.crystal_palace_fc;
            case "Hamburger SV" : return R.drawable.hamburger;
            case "Manchester City FC" : return R.drawable.manchester_city;
            case "Torino FC" : return R.drawable.torino;
            case "Bologna FC" : return R.drawable.bologna;
            case "AC Milan" : return R.drawable.ac_milan;
            case "UC Sampdoria" : return R.drawable.sampdoria;
            case "SV Darmstadt 98" : return R.drawable.darmastadt;
            case "Getafe CF" : return R.drawable.getafe;
            case "AC Chievo Verona" : return R.drawable.ac_chievo;
            case "Udinese Calcio" : return R.drawable.udinese;
            case "Frosinone Calcio" : return R.drawable.frosinone;
            case "Hellas Verona FC" : return R.drawable.hellas_verona;
            case "AS Roma" : return R.drawable.as_roma;
            case "Genoa CFC" : return R.drawable.genoa;
            case "Carpi FC" : return R.drawable.carpi;
            case "Atalanta BC" : return R.drawable.atalanta;
            case "Borussia Dortmund" : return R.drawable.borusia_dortmund;
            case "Liverpool FC" : return R.drawable.liverpool;
            case "Empoli FC" : return R.drawable.empoli;
            case "SS Lazio" : return R.drawable.ss_lazio;
            case "Rayo Vallecano de Madrid" : return R.drawable.rayo_vallecano;
            case "US Cittá di Palermo" : return R.drawable.palermo;
            case "Juventus Turin" : return R.drawable.juventus;
            case "FC Schalke 04" : return R.drawable.shalke;
            case "US Sassuolo Calcio" : return R.drawable.sassuolo;
            case "ACF Fiorentina" : return R.drawable.fiorentina;
            case "SSC Napoli" : return R.drawable.napoli;
            case "FC Internazionale Milano" : return R.drawable.milano;
            default: return R.drawable.no_icon;
        }
    }

    public static boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("barqsoft.footballscores.service.FetchDataService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }
}
