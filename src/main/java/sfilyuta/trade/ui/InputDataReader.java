package sfilyuta.trade.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InputDataReader {

    public List<String> readFromConsole() {
        System.out.println("Enter orders (e.g. 'B 150 10.20')");
        List<String> data = new ArrayList<>();
        boolean stop = false;
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        while (!stop) {
            String line;
            try {
                line = console.readLine();
            } catch (IOException e) {
                throw new RuntimeException("Error reading data from console", e);
            }

            if (line.isEmpty()) {
                stop = true;
            } else {
                data.add(line);
            }
        };
        return data;
    }

}
