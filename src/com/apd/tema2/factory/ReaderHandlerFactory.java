package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {

        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                    // pentru a adauga o intersectie noua folosim:
                    // IntersectionFactory.newInstersection(handlerType, n, t, x);
                    // toata fisierele de input au maxim 3 variabile pe ultima linie asa ca si noi
                    // vom trimite catre hash-ul din intersection factory tot 3 parametrii
                }
            };
            case "simple_n_roundabout", "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] lineToParse = br.readLine().split(" ");
                    int n = Integer.parseInt(lineToParse[0]);
                    int t = Integer.parseInt(lineToParse[1]);
                    IntersectionFactory.newInstersection(handlerType, n, t, 0);
                }
            };
            case "simple_strict_x_car_roundabout", "simple_max_x_car_roundabout", "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] lineToParse = br.readLine().split(" ");
                    int n = Integer.parseInt(lineToParse[0]);
                    int t = Integer.parseInt(lineToParse[1]);
                    int x = Integer.parseInt(lineToParse[2]);
                    IntersectionFactory.newInstersection(handlerType, n, t, x);
                }
            };
            case "priority_intersection", "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                    IntersectionFactory.newInstersection(handlerType, 0, 0, 0);
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] lineToParse = br.readLine().split(" ");
                    int executeTime = Integer.parseInt(lineToParse[0]);
                    int maxPedestriansNo = Integer.parseInt(lineToParse[1]);
                    // la acest task nu avem nevoie de o intersectie, ci doar sa instantiem pietonii
                    Main.pedestrians = new Pedestrians(executeTime, maxPedestriansNo);
                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] lineToParse = br.readLine().split(" ");
                    int x = Integer.parseInt(lineToParse[0]);
                    IntersectionFactory.newInstersection(handlerType, 0, 0, x);
                }
            };
            default -> null;
        };
    }

}
