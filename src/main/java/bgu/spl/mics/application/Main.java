package bgu.spl.mics.application;

import bgu.spl.mics.application.services.*;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import com.google.gson.*;


/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
    public static CountDownLatch latch = new CountDownLatch(4);

    public static void main(String[] args) {
            Diary d=Diary.getInstance();
            Attack[] attacks;
            int durationR2D2;
            int durationLando;
            int numberOfEwoks;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                //Getting json file as input
                Reader reader = new FileReader(args[0]);
                jsonParse parser = gson.fromJson(reader, jsonParse.class);

                attacks = parser.getAttacks();
                durationR2D2 = parser.getR2D2();
                durationLando = parser.getLando();
                numberOfEwoks = parser.getEwoks();

                Ewoks.getInstacne().setEwoks(numberOfEwoks);
                //Creation of the microservices threads
                Thread R2D2Thread = new Thread(new R2D2Microservice(durationR2D2));
                Thread HanThread = new Thread(new HanSoloMicroservice());
                Thread C3POThread = new Thread(new C3POMicroservice());
                Thread LandoThread = new Thread(new LandoMicroservice(durationLando));
                Thread LeiaThread = new Thread(new LeiaMicroservice(attacks));
                //Start running them
                LeiaThread.start();
                C3POThread.start();
                HanThread.start();
                R2D2Thread.start();
                LandoThread.start();
                //Main thread is waiting for the microservices to stop
                LeiaThread.join();
                C3POThread.join();
                R2D2Thread.join();
                HanThread.join();
                LandoThread.join();
            } catch (Exception ignore) {
                System.out.println(ignore);
            }
            //Making json output of the diary
            try {
                Writer writer = new FileWriter(args[1]);
                gson.toJson(Diary.getInstance(), writer);
                writer.close();
            } catch (IOException ignore) {
            }



    }
}

