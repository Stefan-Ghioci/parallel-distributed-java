package utils;

import java.util.List;

class DataUtils
{

    static String stringListToCsvString(List<String> numbers)
    {
        StringBuilder csvBuilder = new StringBuilder();

        for (String number : numbers)
        {
            csvBuilder.append(number);
            csvBuilder.append(",");
        }

        String line = csvBuilder.toString();
        line = line.substring(0, line.length() - 1);

        return line;
    }

}
