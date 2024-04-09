package pl.akozioro.rcm;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

public class MockedTask extends Task {

    public MockedTask(String host, int port, FirebaseHandler firebaseHandler, JTextArea logTextArea) {
        super(host, port, firebaseHandler, logTextArea);
    }

    @Override
    public void run() {
        Random rand = new Random();
        int lap = 1;
        long start2 = System.currentTimeMillis();
        long start3 = System.currentTimeMillis();
        long start4 = System.currentTimeMillis();
        int time = rand.nextInt(3001) + 16000;
        String timeLine = "$L" + convertTime(String.valueOf(time)) + " - $L19.758$ - $L18.177$ - $U59.166$ - $L17.213$ - $L18.134$ - $U36.510$ - $L19.336$ - $U1:06.359$ - $L17.812$ - $L17.886$ - $L17.804$ - $L18.515$ - $L18.105$ - $L19.087$ - $L17.701$ - $U42.231$ - $U33.670$ - $L21.494$ - $L19.681$";
        logTextArea.append("Mocked task is working ..." + "\n");
        int clockCurrent = 0;
        int clockTotal = 60000;
        int clockRemaining = clockTotal;
        while (true) {
            if (stop) {
                return;
            }
            try {
                if (isInterrupted()) {
                    return;
                }
                if (System.currentTimeMillis() - start3 > 10000) {
                    lap++;
                    start3 = System.currentTimeMillis();
                    time = rand.nextInt(3001) + 16000;
                    timeLine = "$L" + convertTime(String.valueOf(time)) + "$ - " + timeLine;
                }
                if (System.currentTimeMillis() - start2 > 3000) {
                    String line = "��1:#@MessageId=7439|Command=GRID_UPDATE|A1=7|AA1=15461355|B1=1|C1=1|D1=Hert Paweł|E1=|F1=�|G1="
                            + lap + "|H1=" + time + "|I1=3429062|J1=0|K1=16495|L1=20312|M1=743|N1=9247658|O1=|P1=|Q1=18|R1=0.000|S1=0.000|T1=19|U1=14711112|V1=1807|VA1=91.10%|W1=0|X1=n/sa|Y1=n/sa|Z1=20.60|DA1=110|DB1=Hert|DC1=PaweB|DD1=30|KA1=11|FA1=0|FB1=0|HA1=" + timeLine + "|KB1=0|EA1=|EB1=|EC1=|ED1=|EE1=|EF1=|EG1=|EH1=|EI1=|EJ1=|CA1=|A2=8|AA2=15461355|B2=2|C2=3|D2=Szkutnik Kamil|E2=L|F2=�|G2=80|H2=18307|I2=2526183|J2=0|K2=17174|L2=26373|M2=690|N2=8701741|O2=RC Sport Planeta|P2=|Q2=0|R2=-13|S2=-13|T2=100|U2=14708593|V2=2735|VA2=89.62%|W2=0|X2=n/sa|Y2=n/sa|Z2=19.66|DA2=26|DB2=Szkutnik|DC2=Kamil|DD2=12|KA2=61|FA2=0|FB2=0|HA2=$L18.307$ - $L19.544$ - $L18.309$ - $L17.867$ - $L17.865$ - $L17.842$ - $U29.215$ - $L20.338$ - $L17.610$ - $L17.634$ - $L18.218$ - $U47.406$ - $U51.349$ - $L17.764$ - $L17.221$ - $L18.931$ - $L17.974$ - $L17.694$ - $L17.519$ - $L17.174$ - $L17.307$ - $L17.588$ - $L17.914$ - $L17.939$ - $L18.079$ - $U2:07.862$|KB2=0|EA2=|EB2=|EC2=|ED2=|EE2=|EF2=|EG2=|EH2=|EI2=|EJ2=|CA2=|A3=5|AA3=15461355|B3=3|C3=7|D3=Norbert ER|E3=M|F3=�|G3=59|H3=19016|I3=3345016|J3=0|K3=18347|L3=26721|M3=618|N3=5595666|O3=|P3=|Q3=0|R3=-34|S3=-21|T3=100|U3=14706682|V3=1135|VA3=95.75%|W3=0|X3=n/sa|Y3=n/sa|Z3=18.93|DA3=7|DB3=Norbert|DC3=ER|DD3=12|KA3=40|FA3=0|FB3=0|HA3=$L19.016$ - $L20.533$ - $L20.038$ - $L21.242$ - $U32.617$ - $L19.465$ - $L23.001$ - $L23.267$ - $L19.363$ - $L18.668$ - 28.476 - $L22.054$ - $L19.049$ - $L19.360$ - 27.372 - $L19.042$ - $L19.166$ - $U48.358$ - $L18.624$ - $L18.347$ - $U51.025$ - $L21.586$ - $L24.277$ - $L18.747$ - $L19.039$ - $L19.048$|KB3=0|EA3=|EB3=|EC3=|ED3=|EE3=|EF3=|EG3=|EH3=|EI3=|EJ3=|CA3=|A4=8|AA4=15461355|B4=4|C4=4|D4=Czekalski RadosBaw|E4=L|F4=�|G4=36|H4=41996|I4=2066405|J4=0|K4=17163|L4=27853|M4=481|N4=5726330|O4=RC Sport Planeta|P4=|Q4=0|R4=-57|S4=-23|T4=100|U4=14701918|V4=9588|VA4=65.57%|W4=0|X4=n/sa|Y4=n/sa|Z4=8.57|DA4=27|DB4=Czekalski|DC4=RadosBaw|DD4=12|KA4=31|FA4=0|FB4=0|HA4=" + timeLine + "|KB4=0|EA4=|EB4=|EC4=|ED4=|EE4=|EF4=|EG4=|EH4=|EI4=|EJ4=|CA4=|A5=8|AA5=15461355|B5=5|C5=2|D5=Koziorowicz Artur|E5=L|F5=�|G5=26|H5=18906|I5=958168|J5=0|K5=18521|L5=36753|M5=395|N5=34|O5=|P5=|Q5=16|R5=-67|S5=-10|T5=100|U5=14711029|V5=7926|VA5=78.43%|W5=0|X5=n/sa|Y5=n/sa|Z5=19.04|DA5=93|DB5=Koziorowicz|DC5=Artur|DD5=30|KA5=13|FA5=0|FB5=0|HA5=$L18.906$ - $L19.070$ - $L19.783$ - $U1:04.574$ - $L19.608$ - $L18.528$ - $L19.118$ - $L20.293$ - $L20.239$ - $U44.917$ - $L19.488$ - 35.130 - $L20.257$ - $L18.521$ - $L19.377$ - $L19.472$ - $L27.264$ - $U1:28.988$ - $L20.454$ - 34.994 - $U1:00.168$ - $U58.781$ - $U52.556$ - $U1:45.102$ - $U54.192$ - $U55.802$|KB5=0|EA5=|EB5=|EC5=|ED5=|EE5=|EF5=|EG5=|EH5=|EI5=|EJ5=|CA5=|A6=8|AA6=15461355|B6=6|C6=6|D6=Wrzalik Marcin|E6=L|F6=�|G6=23|H6=24451|I6=2059747|J6=0|K6=17267|L6=21009|M6=604|N6=6507300|O6=RC Sport Planeta|P6=|Q6=61|R6=-70|S6=-3|T6=100|U6=14714752|V6=10117|VA6=51.84%|W6=0|X6=n/sa|Y6=n/sa|Z6=14.72|DA6=34|DB6=Wrzalik|DC6=Marcin|DD6=44|KA6=17|FA6=0|FB6=0|HA6=$U24.451$ - $L17.863$ - $L17.756$ - $L17.656$ - $L17.719$ - $L17.430$ - $L17.267$ - $L17.697$ - 21.939 - $L17.539$ - $L17.518$ - $L18.632$ - $L17.994$ - $L17.752$ - 21.021 - 19.230 - $L18.442$ - $U1:06.356$ - 19.971 - $L17.301$ - $U24.828$ - $L17.964$ - $L18.896$|KB6=0|EA6=|EB6=|EC6=|ED6=|EE6=|EF6=|EG6=|EH6=|EI6=|EJ6=|CA6=|A7=8|AA7=15461355|B7=7|C7=5|D7=Szczepan Jakub|E7=L|F7=�|G7=19|H7=19758|I7=1989302|J7=0|K7=17213|L7=26244|M7=414|N7=4656490|O7=|P7=|Q7=0|R7=-74|S7=-4|T7=100|U7=14708643|V7=14793|VA7=43.63%|W7=0|X7=n/sa|Y7=n/sa|Z7=18.22|DA7=28|DB7=Szczepan|DC7=Jakub|DD7=12|KA7=16|FA7=0|FB7=0|HA7=|KB7=0|EA7=|EB7=|EC7=|ED7=|EE7=|EF7=|EG7=|EH7=|EI7=|EJ7=|CA7=|"
                            + "A8=8|AA8=15461355|B8=8|C8=5|D8=Malkovic John|E8=L|F8=�|G8=19|H8=19758|I8=1989302|J8=0|K8=17213|L8=26244|M8=414|N8=4656490|O8=|P8=|Q8=0|R8=-74|S8=-4|T8=100|U8=14708643|V8=14793|VA8=43.63%|W8=0|X8=n/sa|Y8=n/sa|Z8=18.22|DA8=28|DB8=Szczepan|DC8=Jakub|DD8=12|KA8=16|FA8=0|FB8=0|HA8=$L19.758$ - $L18.177$ - $U59.166$ - $L17.213$ - $L18.134$ - $U36.510$ - $L19.336$ - $U1:06.359$ - $L17.812$ - $L17.886$ - $L17.804$ - $L18.515$ - $L18.105$ - $L19.087$ - $L17.701$ - $U42.231$ - $U33.670$ - $L21.494$ - $L19.681$|KB8=0|EA8=|EB8=|EC8=|ED8=|EE8=|EF8=|EG8=|EH8=|EI8=|EJ8=|CA8=|"
                            + "A9=8|AA9=15461355|B9=9|C9=5|D9=Nowaczek Jan|E9=L|F9=�|G9=19|H9=19758|I9=1989302|J9=0|K9=17213|L9=26244|M9=414|N9=4656490|O9=|P9=|Q9=0|R9=-74|S9=-4|T9=100|U9=14708643|V9=14793|VA9=43.63%|W9=0|X9=n/sa|Y9=n/sa|Z9=18.22|DA9=28|DB9=Szczepan|DC9=Jakub|DD9=12|KA9=16|FA9=0|FB9=0|HA9=$L19.758$ - $L18.177$ - $U59.166$ - $L17.213$ - $L18.134$ - $U36.510$ - $L19.336$ - $U1:06.359$ - $L17.812$ - $L17.886$ - $L17.804$ - $L18.515$ - $L18.105$ - $L19.087$ - $L17.701$ - $U42.231$ - $U33.670$ - $L21.494$ - $L19.681$|KB9=0|EA9=|EB9=|EC9=|ED9=|EE9=|EF9=|EG9=|EH9=|EI9=|EJ9=|CA9=|RecordCount=9|";
                    start2 = System.currentTimeMillis();
                    try {
                        handleLine(line);
                    } catch (IOException e) {
                        logTextArea.append("Exception: " + e.getMessage() + "\n");
                        throw new RuntimeException(e);
                    }
                }
                if (System.currentTimeMillis() - start4 > 1000) {
                    String line = "��1:#@MessageId=7439|Command=CLOCK_UPDATE|A=" + clockTotal + "|B=" + clockRemaining-- + "|C=" + clockCurrent++ + "|";
                    start4 = System.currentTimeMillis();
                    try {
                        handleLine(line);
                    } catch (IOException e) {
                        logTextArea.append("Exception: " + e.getMessage() + "\n");
                        throw new RuntimeException(e);
                    }
                }
                sleep(1);
            } catch (InterruptedException e) {
                logTextArea.append("Stopped." + "\n");
                return;
            }
        }
    }

    private static String convertTime(String time) {
        long timestamp = Optional.ofNullable(time).filter(s -> s != null && s.matches("\\d+")).map(Long::parseLong).orElse(0l);
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC)
                .plusNanos((timestamp % 1000) * 1000000);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss.SSS");
        return dateTime.format(formatter).replaceAll("^00:", "");
    }
}