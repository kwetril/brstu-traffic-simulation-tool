package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.simulation.control.autonomous.WeightedSectionPart;

import java.util.*;

/**
 * Created by a.klimovich on 22.10.2016.
 *
 * This class should implement algorithm to find clique in graph with maximal weight of vertices.
 *
 * Possible implementation: Deniss Kumlander's article
 */
class WeightedGraph {
    private List<WeightedSectionPart> sectionParts;
    private boolean[][] graph;
    private double[] weights;

    WeightedGraph(List<WeightedSectionPart> sectionParts) {
        this.sectionParts = sectionParts;
        graph = new boolean[sectionParts.size()][sectionParts.size()];
        weights = new double[sectionParts.size()];
        for (int i = 0; i < sectionParts.size(); i++) {
            weights[i] = sectionParts.get(i).getWeight();
        }
        for (int i = 0; i < sectionParts.size(); i++) {
            for (int j = i + 1; j < sectionParts.size(); j++) {
                if (!sectionParts.get(i).hasConflicts(sectionParts.get(j))) {
                    graph[i][j] = true;
                    graph[j][i] = true;
                }
            }
        }
    }

    List<WeightedSectionPart> getOptimalSectionParts() {
        if (sectionParts.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        List<WeightedSectionPart> result = new ArrayList<>();
        if (sectionParts.size() == 1) {
            result.add(sectionParts.get(0));
            return result;
        }
        //current implementation returns maximum 6 directions
        boolean answerFound = false;
        boolean answerFound3 = false;
        boolean answerFound4 = false;
        boolean answerFound5 = false;
        boolean answerFound6 = false;
        int first = 0, second = 1;
        int third = 0, fourth = 0, fifth = 0, sixth = 0;
        double bestWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            for (int j = i + 1; j < weights.length; j++) {
                if (graph[i][j]) {
                    double currentWeight = weights[i] + weights[j];
                    if (currentWeight > bestWeight) {
                        bestWeight = currentWeight;
                        first = i;
                        second = j;
                        answerFound = true;
                        answerFound3 = answerFound4 = answerFound5 = answerFound6 = false;
                    }
                    for (int k = j + 1; k < weights.length; k++) {
                        if (graph[i][k] && graph[j][k]) {
                            double currentWeight3 = currentWeight + weights[k];
                            if (currentWeight3 > bestWeight) {
                                bestWeight = currentWeight3;
                                first = i;
                                second = j;
                                third = k;
                                answerFound3 = true;
                                answerFound4 = false;
                                answerFound5 = false;
                                answerFound6 = false;
                            }
                            for (int m = k + 1; m < weights.length; m++) {
                                if (graph[i][m] && graph[j][m] && graph[k][m]) {
                                    double currentWeight4 = currentWeight3 + weights[m];
                                    if (currentWeight4 > bestWeight) {
                                        bestWeight = currentWeight4;
                                        first = i;
                                        second = j;
                                        third = k;
                                        fourth = m;
                                        answerFound5 = false;
                                        answerFound6 = false;
                                        answerFound4 = true;
                                    }
                                    for (int n = m + 1; n < weights.length; n++) {
                                        if (graph[i][n] && graph[j][n] && graph[k][n] && graph[m][n]) {
                                            double currentWeight5 = currentWeight4 + weights[n];
                                            if (currentWeight5 > bestWeight) {
                                                bestWeight = currentWeight5;
                                                first = i;
                                                second = j;
                                                third = k;
                                                fourth = m;
                                                fifth = n;
                                                answerFound5 = true;
                                                answerFound6 = false;
                                            }
                                            for (int p = n + 1; p < weights.length; p++) {
                                                if (graph[i][p] && graph[j][p] && graph[k][p] && graph[m][p] && graph[n][p]) {
                                                    double currentWeight6 = currentWeight5 + weights[p];
                                                    if (currentWeight6 > bestWeight) {
                                                        bestWeight = currentWeight6;
                                                        first = i;
                                                        second = j;
                                                        third = k;
                                                        fourth = m;
                                                        fifth = n;
                                                        sixth = p;
                                                        answerFound6 = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (answerFound) {
            result.add(sectionParts.get(first));
            result.add(sectionParts.get(second));
            if (answerFound3) {
                result.add(sectionParts.get(third));
                if (answerFound4) {
                    result.add(sectionParts.get(fourth));
                    if (answerFound5) {
                        result.add(sectionParts.get(fifth));
                        if (answerFound6) {
                            result.add(sectionParts.get(sixth));
                        }
                    }
                }
            }
        }
        return result;
    }
}
