package ua.kpi.comsys.IO8206;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView main_text;
    Integer numOdRate = 10;
    HashMap<String, String[]> map_task1 = new HashMap<>(); // словарь {"група": [студенты]}
    HashMap<String, HashMap<String, Integer[]>> map_task2 = new HashMap<>(); // словарь {"група": [{"студент": [оценки]}]}
    HashMap<String, HashMap<String, Integer>> map_task3 = new HashMap<>(); // словарь {"група": [{"студент": сумма-оценок}]}
    HashMap<String, Integer> map_task4 = new HashMap<>(); // словарь {"група": средняя-оценка-группы}
    HashMap<String, String[]> map_task5 = new HashMap<>(); // словарь {"група": [студенты-у-которых-больше-60]}
    Boolean task1End = false, task2End = false, task3End = false, task4End = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_text = findViewById(R.id.main_text);
        main_text.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onStart() {
        super.onStart();
        main_text.setText("Select task and push button");
    }

    public Integer[] generateRatings(){
        Random random = new Random();
        Integer[] result = new Integer[numOdRate];
        for (int i = 0; i < result.length; i++) {
            result[i] = random.nextInt(12 - 1) + 1;
        }
        return result;
    }


    public void task1(View view) {
        map_task1.clear();
        main_text.setText("Ok, you press");
        String studentsStr = "Дмитренко Олександр - ІП-84; Матвійчук Андрій - ІВ-83; " +
                "Лесик Сергій - ІО-82; Ткаченко Ярослав - ІВ-83; Аверкова Анастасія - ІО-83; " +
                "Соловйов Даніїл - ІО-83; Рахуба Вероніка - ІО-81; Кочерук Давид - ІВ-83; " +
                "Лихацька Юлія - ІВ-82; Головенець Руслан - ІВ-83; Ющенко Андрій - ІО-82; " +
                "Мінченко Володимир - ІП-83; Мартинюк Назар - ІО-82; Базова Лідія - ІВ-81; " +
                "Снігурець Олег - ІВ-81; Роман Олександр - ІО-82; Дудка Максим - ІО-81; " +
                "Кулініч Віталій - ІВ-81; Жуков Михайло - ІП-83; Грабко Михайло - ІВ-81; " +
                "Іванов Володимир - ІО-81; Востриков Нікіта - ІО-82; Бондаренко Максим - ІВ-83; " +
                "Скрипченко Володимир - ІВ-82; Кобук Назар - ІО-81; Дровнін Павло - ІВ-83; " +
                "Тарасенко Юлія - ІО-82; Дрозд Світлана - ІВ-81; Фещенко Кирил - ІО-82; " +
                "Крамар Віктор - ІО-83; Іванов Дмитро - ІВ-82";

        String[] splitedStudents = studentsStr.split("; ");
        String[] nameGr, studArray, tempArr;

        for (String student : splitedStudents) {
            nameGr = student.split(" - ");
            try {
                tempArr = map_task1.get(nameGr[1]);
                studArray = new String[tempArr.length+1];
                System.arraycopy(tempArr, 0, studArray, 0, tempArr.length); // скопировать временный массив в текущий
                map_task1.remove(nameGr[1]);
            }
            catch (Throwable t){
                studArray = new String[1];
            }
            studArray[studArray.length-1] = nameGr[0];
            map_task1.put(nameGr[1], studArray);
        }

        HashMap<String, String[]> map_copy = new HashMap<>(map_task1);
        for(String group : map_copy.keySet()){
            tempArr = map_task1.get(group);
            Arrays.sort(tempArr);
            map_task1.remove(group);
            map_task1.put(group, tempArr);

        }

        String result = "";
        for (Map.Entry<String, String[]> entry : map_task1.entrySet()) {
            result += (entry.getKey() + ":\n" + Arrays.toString(entry.getValue()) + "\n\n");
        }
        task1End = true;
        main_text.setText(result);
    }

    public void task2(View view) {
//        task1(view);
        HashMap<String, Integer[]> temporary = new HashMap<>(); // словарь {"студент": [оценки]}
        map_task2.clear();
        String result = "";

        if(task1End){
            for (Map.Entry<String, String[]> entry : map_task1.entrySet()) {
                temporary.clear();
                String tempStr = "";
                for (String student : entry.getValue()) {
                    temporary.put(student, generateRatings());
                    tempStr += student + temporary.get(student).toString() + "\n";
                }
                map_task2.put(entry.getKey(), new HashMap<>(temporary));
            }

            for (Map.Entry<String, HashMap<String, Integer[]>> entry : map_task2.entrySet()) {
                result += ("\n" + entry.getKey() + ":\n");
                for (Map.Entry<String, Integer[]> entry2 : entry.getValue().entrySet()) {
                    result += (entry2.getKey() + ": " + Arrays.toString(entry2.getValue()) + "\n");
                }
            }
            task2End = true;
        }
        else result = "Задание 1 не инициализировано.";
        main_text.setText(result);
    }

    public void task3(View view) {
//        task2(view);
        map_task3.clear();
        HashMap<String, Integer> temporary = new HashMap<>(); // словарь {"студент": сума оценок}
        int ratingsSum;
        String result = "";

        if(task2End){
            for (Map.Entry<String, HashMap<String, Integer[]>> entry : map_task2.entrySet()) {
            // entry.getKey() - группа
            temporary.clear();
            for (Map.Entry<String, Integer[]> entry2 : entry.getValue().entrySet()) {
                // entry2.getKey() - студент
                // entry2.getValue() - список оценок
                ratingsSum = 0;
                for(Integer i : entry2.getValue()){
                    ratingsSum += i;
                }
                temporary.put(entry2.getKey(), ratingsSum);
            }
            map_task3.put(entry.getKey(), new HashMap<>(temporary));
            }

            for (Map.Entry<String, HashMap<String, Integer>> entry : map_task3.entrySet()) {
                result += (entry.getKey() + ":\n");
                for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                    result += (entry2.getKey() + ": " + entry2.getValue() + "\n");
                }
                result += "\n";
            }
            task3End = true;
        }
        else result = "Задание 2 не инициализировано.";
        main_text.setText(result);
    }

    public void task4(View view) {
//        task3(view);
        map_task4.clear();
        // - ключ – назва групи
        // - значення – середня оцінка всіх студентів групи
        Integer sumRatings;
        String result = "";

        if(task3End) {
            for (Map.Entry<String, HashMap<String, Integer>> entry : map_task3.entrySet()) {
                sumRatings = 0;
                result += (entry.getKey() + ": ");
                for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                    sumRatings += entry2.getValue();
                }
                map_task4.put(entry.getKey(), (sumRatings / entry.getKey().length()) / numOdRate);
                result += (map_task4.get(entry.getKey()) + "\n");
            }
            task4End = true;
        }
        else result = "Задание 3 не инициализировано.";
        main_text.setText(result);
    }

    public void task5(View view) {
        map_task5.clear();
//        task3(view);
        // - ключ – назва групи
        // - значення – масив студентів, які мають >= 60 балів
        String result = "";
        String[] students;
        int counter1, counterTotal;

        if(task3End) {
            for (Map.Entry<String, HashMap<String, Integer>> entry : map_task3.entrySet()) {
                counter1 = counterTotal = 0;

                for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                    if (entry2.getValue()>60){
                        counter1 += 1;
                    }
                }

                students = new String[counter1];
                for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                    if (entry2.getValue()>60){
                        students[counterTotal] = entry2.getKey();
                        counterTotal += 1;
                    }
                }
                map_task5.put(entry.getKey(), students);
            }
            for (Map.Entry<String, String[]> entry3 : map_task5.entrySet()) {
                result += (entry3.getKey() + ": " + Arrays.toString(entry3.getValue()) + "\n");
            }
        }
        else result = "Задание 3 не инициализировано.";
        main_text.setText(result);
    }

    public void part2(View view){
        Intent intent = new Intent(MainActivity.this, TimeNV.class);
        startActivity(intent);
    }
}