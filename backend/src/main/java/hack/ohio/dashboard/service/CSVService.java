package hack.ohio.dashboard.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CSVService {

	static Map<String, HashMap<String, Double>> mDaily = new HashMap<String, HashMap<String, Double>>();

	public void initialize() throws IOException, ParseException {
		File config = new File("C:\\Users\\elena\\Desktop\\Work Stuff\\IntelliJ\\ParseData\\HackathonConfig.csv");
		File daily = new File("C:\\Users\\elena\\Desktop\\Work Stuff\\IntelliJ\\ParseData\\HackathonDataDaily.csv");

		BufferedReader in1 = new BufferedReader(new FileReader(config));
		BufferedReader in2 = new BufferedReader(new FileReader(daily));

		Map<String, HashMap<String, String>> mConfig = new HashMap<String, HashMap<String, String>>();

		// calendar is date, string is building name
		Map<Map.Entry<String, String>, HashMap<String, Double>> mBuilding = new HashMap<Map.Entry<String, String>, HashMap<String, Double>>();

		String read;
		while ((read = in1.readLine()) != null) {
			String cvsSplitBy = ",";
			String[] details = read.split(cvsSplitBy);

			HashMap<String, String> m = new HashMap<String, String>();

			for (int i = 0; i < details.length; i++) {
				details[i] = details[i].substring(1, details[i].length() - 1);
			}

			m.put("units", details[3]);
			m.put("resource", details[4]);
			m.put("building", details[5]);
			m.put("area", details[6]);

			mConfig.put(details[1], m);
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		while ((read = in2.readLine()) != null) {
			String cvsSplitBy = ",";
			String[] details = read.split(cvsSplitBy);

			HashMap<String, Double> m1 = new HashMap<String, Double>();

			Date date = df.parse(details[3].substring(0, details[3].indexOf('T')));
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			// System.out.println( mConfig.get(details[0]).get("building"));
			Map.Entry<String, String> p = new AbstractMap.SimpleEntry<String, String>(df.format(date),
					mConfig.get(details[0]).get("building"));

			// hot water, cold water, steam, electricity
			double[] arrR = getResourcePower(mConfig, details);

			c.add(Calendar.DAY_OF_YEAR, -1);
			Map.Entry<String, String> p2 = new AbstractMap.SimpleEntry<String, String>(df.format(c.getTime()),
					mConfig.get(details[0]).get("building"));
			c.add(Calendar.DAY_OF_YEAR, 1);

			double change;
			double change_hot;
			double change_cool;
			double change_steam;
			double change_elec;

			// meters are totalizers
			if (!mBuilding.containsKey(p2)) {
				m1.put("Change", 0.0);
				change = 0.0;

				m1.put("Change for hot", 0.0);
				change_hot = 0.0;
				m1.put("Change for cold", 0.0);
				change_cool = 0.0;
				m1.put("Change for steam", 0.0);
				change_steam = 0.0;
				m1.put("Change for electricity", 0.0);
				change_elec = 0.0;

				m1.put("Base for hot", arrR[0]);
				m1.put("Base for cold", arrR[1]);
				m1.put("Base for steam", arrR[2]);
				m1.put("Base for electricity", arrR[3]);
			} else {
				// m1.put("Change", Math.abs(Double.valueOf(details[1]) -
				// mBuilding.get(p2).get("Total Energy")));
				// change = Math.abs(Double.valueOf(details[1]) - mBuilding.get(p2).get("Total
				// Energy"));

				m1.put("Change for hot", Math.abs(arrR[0] - mBuilding.get(p2).get("Base for hot")));
				change_hot = Math.abs(arrR[0] - mBuilding.get(p2).get("Base for hot"));
				m1.put("Change for cold", Math.abs(arrR[1] - mBuilding.get(p2).get("Base for cold")));
				change_cool = Math.abs(arrR[1] - mBuilding.get(p2).get("Base for cold"));
				m1.put("Change for steam", Math.abs(arrR[2] - mBuilding.get(p2).get("Base for steam")));
				change_steam = Math.abs(arrR[2] - mBuilding.get(p2).get("Base for steam"));
				m1.put("Change for electricity", Math.abs(arrR[3] - mBuilding.get(p2).get("Base for electricity")));
				change_elec = Math.abs(arrR[3] - mBuilding.get(p2).get("Base for electricity"));

				change = change_hot + change_cool + change_steam + change_elec + mBuilding.get(p2).get("Change");
				m1.put("Change", change);

				if (!mBuilding.containsKey(p)) {
					m1.put("Base for hot", arrR[0]);
					m1.put("Base for cold", arrR[1]);
					m1.put("Base for steam", arrR[2]);
					m1.put("Base for electricity", arrR[3]);
				} else {
					m1.put("Base for hot", mBuilding.get(p).get("Base for hot"));
					m1.put("Base for cold", mBuilding.get(p).get("Base for cold"));
					m1.put("Base for steam", mBuilding.get(p).get("Base for steam"));
					m1.put("Base for electricity", mBuilding.get(p).get("Base for electricity"));

					if (arrR[0] != 0) {
						m1.put("Base for hot", arrR[0]);
					} else if (arrR[1] != 0) {
						m1.put("Base for cold", arrR[1]);
					} else if (arrR[2] != 0) {
						m1.put("Base for steam", arrR[2]);
					} else if (arrR[3] != 0) {
						m1.put("Base for electricity", arrR[3]);
					}
				}

			}

			m1.put("Total Energy", Double.valueOf(details[1]));
			mBuilding.put(p, m1);

			HashMap<String, Double> m2 = new HashMap<String, Double>();

			if (!mDaily.containsKey(df.format(date))) {
				m2.put("Total Change", change_hot + change_cool + change_steam + change_elec);
				m2.put("Change for hot", change_hot);
				m2.put("Change for cool", change_cool);
				m2.put("Change for steam", change_steam);
				m2.put("Change for electricity", change_elec);
			} else {
				change_hot += mDaily.get(df.format(date)).get("Change for hot");
				change_cool += mDaily.get(df.format(date)).get("Change for cool");
				change_steam += mDaily.get(df.format(date)).get("Change for steam");
				change_elec += mDaily.get(df.format(date)).get("Change for electricity");

				m2.put("Total Change", change_hot + change_cool + change_steam + change_elec);
				m2.put("Change for hot", change_hot);
				m2.put("Change for cool", change_cool);
				m2.put("Change for steam", change_steam);
				m2.put("Change for electricity", change_elec);
			}

			mDaily.put(df.format(date), m2);

		}

		in1.close();
		in2.close();
    }

    public static double convertToKWH(String meterId, String value, Map<String, HashMap<String, String>> mConfig)
    {
        final double KWH_TO_BTU = 0.000293071;

        double d = Double.valueOf(value);
        if (mConfig.get(meterId).get("units").equals("KBTU/HR"))
        {
            d *= (1/KWH_TO_BTU);
        }
        return d;
    }

    public static double[] getResourcePower(Map<String, HashMap<String, String>> mConfig, String[] details)
    {
        double[] arrR = {0, 0, 0, 0};
        if (mConfig.get(details[0]).get("resource").contains("Heating"))
        {
            arrR[0] = convertToKWH(details[0], details[1], mConfig);
        }
        else if (mConfig.get(details[0]).get("resource").contains("Chilled"))
        {
            arrR[1] = convertToKWH(details[0], details[1], mConfig);
        }
        else if (mConfig.get(details[0]).get("resource").contains("Steam"))
        {
            arrR[2] = convertToKWH(details[0], details[1], mConfig);
        }
        else if (mConfig.get(details[0]).get("resource").contains("Electricity"))
        {
            arrR[3] = convertToKWH(details[0], details[1], mConfig);
        }

        return arrR;
    }

	// change, change in heat, change in cool, change in steam, change in elec
    public static double[] getPowersDay(String start) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse(start);

		double[] powers = new double[5];
		powers[0] = mDaily.get(df.format(date)).get("Total Change");
		powers[1] = mDaily.get(df.format(date)).get("Change for hot");
		powers[2] = mDaily.get(df.format(date)).get("Change for cool");
		powers[3] = mDaily.get(df.format(date)).get("Change for steam");
		powers[4] = mDaily.get(df.format(date)).get("Change for electricity");

		return powers;
	}

	// change, change in heat, change in cool, change in steam, change in elec
	public static double[] getPowersRange(String start, String end) throws Exception {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		cal1.setTime(dateFormat.parse(start));
		cal2.setTime(dateFormat.parse(end));

		double[] powers = new double[5];

		for (Map.Entry<String, HashMap<String, Double>> m : mDaily.entrySet()) {
			Calendar cal3 = Calendar.getInstance();
			cal3.setTime(dateFormat.parse(m.getKey()));

			if (isWithinDateRange(cal1, cal2, cal3)) {
				powers[0] += m.getValue().get("Total Change");
				powers[1] += m.getValue().get("Change for hot");
				powers[2] += m.getValue().get("Change for cool");
				powers[3] += m.getValue().get("Change for steam");
				powers[4] += m.getValue().get("Change for electricity");
			}
		}

		return powers;

	}

	public static boolean isWithinDateRange(Calendar cal1, Calendar cal2, Calendar cal3) {
		boolean sameDay1 = cal1.get(Calendar.DAY_OF_YEAR) == cal3.get(Calendar.DAY_OF_YEAR)
				&& cal1.get(Calendar.YEAR) == cal3.get(Calendar.YEAR);

		boolean sameDay2 = cal2.get(Calendar.DAY_OF_YEAR) == cal3.get(Calendar.DAY_OF_YEAR)
				&& cal2.get(Calendar.YEAR) == cal3.get(Calendar.YEAR);

		boolean isWithinRange = cal2.after(cal3) && cal1.before(cal3);

		return (sameDay1 || sameDay2 || isWithinRange);
	}

	public static double[] getPastWeekPower(String start) throws Exception
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse(start);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -7);

		Date date2 = cal.getTime();

		double allPower[] = new double[8];

		for (int i = 0; i < 8; i++)
		{
			double power[] = getPowersDay(df.format(date2));
			allPower[i] = power[0];

			cal.add(Calendar.DAY_OF_YEAR, 1);
			date2 = cal.getTime();
		}

        return allPower;
	}

	public static String[] getPastWeekDate(String start) throws Exception
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse(start);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -7);

		Date date2 = cal.getTime();

		String[] allDates = new String[8];

		for (int i = 0; i < 8; i++)
		{
			allDates[i] = df.format(date2);

			cal.add(Calendar.DAY_OF_YEAR, 1);
			date2 = cal.getTime();
		}

        return allDates;
	}

	public static double getPastMonthSum(String start) throws Exception
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse(start);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -31);

		Date date2 = cal.getTime();

		double sum = 0.0;

		for (int i = 0; i < 32; i++)
		{
			sum += getPowersDay(df.format(date2))[0];

			cal.add(Calendar.DAY_OF_YEAR, 1);
			date2 = cal.getTime();
		}

        return sum;
	}

}

