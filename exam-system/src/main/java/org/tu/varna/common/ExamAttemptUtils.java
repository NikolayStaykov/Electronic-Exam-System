package org.tu.varna.common;

import org.tu.varna.entities.Question;

import java.util.*;

public class ExamAttemptUtils {
    public static ArrayList<ArrayList<Double>> findPointCombinations(Map<Double,List<Question>> mapOfQuestions, double sumOfPoints, int numberOfQuestions)
    {
        ArrayList<ArrayList<Double> > result = new ArrayList<>();
        ArrayList<Double> buffer = new ArrayList<>();

        ArrayList<Double> input = new ArrayList<>(mapOfQuestions.keySet());
        Collections.sort(input);

        findNumbers(result, input, sumOfPoints, 0, buffer);
        result.removeIf(variant -> {
            if(variant.size() != numberOfQuestions){
                //the current variation of questions does meet the required number of question
                return true;
            }
            Set<Double> uniquePoints = new HashSet<>(variant);
            for(double point : uniquePoints){
                if(variant.stream().filter(d -> d == point).count() > mapOfQuestions.get(point).size()){
                    //variation contains a question type of which we do not have enough of
                    return true;
                }
            }
            return false;
        });
        if(result.stream().anyMatch(ad -> ad.containsAll(input))){
            //if we have a variation that includes all question types it is better to use that than to use one that leaves some out
            result.removeIf(ad -> !ad.containsAll(input));
        }
        return result;
    }

    private static void findNumbers(ArrayList<ArrayList<Double>> result,
                ArrayList<Double> arr, double sumOfPoints, int index,
                ArrayList<Double> buffer)
    {

        if (sumOfPoints == 0) {

            result.add(new ArrayList<>(buffer));
            return;
        }

        for (int i = index; i < arr.size(); i++) {

            if ((sumOfPoints - arr.get(i)) >= 0) {

                buffer.add(arr.get(i));

                findNumbers(result, arr, sumOfPoints - arr.get(i), i,
                        buffer);

                buffer.remove(arr.get(i));
            }
        }
    }
}
