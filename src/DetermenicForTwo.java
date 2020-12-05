import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DetermenicForTwo extends Process {

    private static List<Double[]> matrixForFormula = new ArrayList<>(); //матрица в которой хранятся значения, используемые в формуле
    private static List<Integer> classesList = new ArrayList<>();

    private static List<Double[]> koefList = new ArrayList<>();
    private static List<Integer> simbolList = new ArrayList<>();

    DetermenicForTwo(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.lines().forEach(line -> {
                matrixForFormula.add(convertStringToIntMas(line));
                classesList.add(getClassByString(line));
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void study(){
        int streak = 0;
        boolean first = true;
        int currentLine = 0;
        int iter = 1;
        while (streak != classesList.size()) {
            if (first) {
                simbolList.add(1);
                koefList.add(matrixForFormula.get(currentLine));
                first = false;
                currentLine++;
            } else {
                double resultOfFormula = formula(matrixForFormula.get(currentLine));
                if (classesList.get(currentLine) == 1 && resultOfFormula < 0) {
                    simbolList.add(1);
                    koefList.add(matrixForFormula.get(currentLine));
                    streak = 0;
                } else if (classesList.get(currentLine) == 2 && resultOfFormula > 0) {
                    simbolList.add(0);
                    koefList.add(matrixForFormula.get(currentLine));
                    streak = 0;
                } else {
                    streak++;
                }
            }
            if (currentLine == classesList.size() - 1) {
                currentLine = 0;
            } else {
                currentLine++;
            }
            iter++;
        }
        System.out.println("Обучение завершено!!! Количество итераций: " + iter);
    }

    public void work(Double[] test){
        if (test.length != matrixForFormula.get(0).length){
            throw new IllegalArgumentException("Неправильная размерность входного массива");
        } else {
            if (formula(test) > 0) {
                System.out.println("Принадлежит первому классу");
            } else  {
                System.out.println("Принадлежит второму классу");
            }
        }
    }

    private static double formula(Double[] currentLine) {
        double result = 0; //подсчет
        for (int i = 0; i < koefList.size(); i++) { //идем по всем коэффициентам
            double resultForLine = 0;
            for (int j = 0; j < currentLine.length; j++) {
                resultForLine += Math.pow(currentLine[j] - koefList.get(i)[j], 2);
            }
            result = simbolList.get(i) == 1 ? result + Math.pow(Math.E, resultForLine * -1) : result - Math.pow(Math.E, resultForLine * -1);
        }
        return result;
    }

}
