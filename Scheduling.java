// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

// Цей файл містить головну функцію main() для симуляції планування.
// Init() ініціалізує більшість змінних, зчитуючи їх із вказаного файлу.
// SchedulingAlgorithm.Run() викликається з main() для запуску симуляції.
// Summary-Results - це файл, де записуються результати виконання,
// а Summary-Processes - де записуються результати планування процесів.

// Створено Alexander Reeder, 2001 січень 06

import java.io.*;
import java.util.*;


public class Scheduling {

  // Оголошення статичних змінних класу.
  private static int processnum = 5;
  private static int run_time_average = 1000;
  private static int run_time_stddev = 100;
  private static int block_time_average = 100;
  private static int block_time_stddev = 10;
  private static int runtime = 1000;
  private static Vector<sProcess> processVector = new Vector<sProcess>();
  private static Results result = new Results("null","null",0);
  private static String resultsFile = "Summary-Results";  // Файл для збереження результатів симуляції.

  // Ініціалізація даних з файлу.
  private static void Init(String file) {
    File f = new File(file);
    String line;


    try (BufferedReader in = new BufferedReader(new FileReader(f))) {

      while ((line = in.readLine()) != null) {
        if (line.startsWith("numprocess")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          processnum = Common.s2i(st.nextToken());
        }
        // Зчитується середній проміжок часу, протягом якого процес виконується перед блокуванням для введення або виведення.
        if (line.startsWith("run_time_average")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          run_time_average = Common.s2i(st.nextToken());
        }
        // Зчитуєтсья кількість стандартних відхилень від середнього часу виконання процесу перед блокуванням для введення або виведення.
        if (line.startsWith("run_time_stddev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          run_time_stddev = Common.s2i(st.nextToken());
        }
        // Зчитуєтсья середній час у мілісекундах, протягом якого процес залишається заблокованим для введення або виведення.
        if (line.startsWith("block_time_average")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          block_time_average = Common.s2i(st.nextToken());
        }
        // Зчитуєтсья кількість стандартних відхилень від середнього часу, коли процес залишається заблокованим для введення або виведення.
        if (line.startsWith("block_time_stddev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          block_time_stddev = Common.s2i(st.nextToken());
        }

        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.s2i(st.nextToken());
        }
      }
      in.close();
    } catch (IOException e) { /* Handle exceptions */ }
  }



  public static void main(String[] args) {
    int cputime = 0;
    int ioblocking = 0;
    double X = 0.0;

    // Перевірка коректності вхідних параметрів.
    if (args.length != 1) {
      System.out.println("Usage: 'java Scheduling <INIT FILE>'");
      System.exit(-1);
    }
    // Перевірка наявності та читання файлу ініціалізації.
    File f = new File(args[0]);
    if (!(f.exists())) {
      System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
      System.exit(-1);
    }  
    if (!(f.canRead())) {
      System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
      System.exit(-1);
    }
    System.out.println("Working...");
    // Ініціалізація системи планування.
    Init(args[0]);
    // Додавання процесів
    for (int i = 0; i < processnum; i++) {
      X = Common.R1();

      while (X == -1.0) {
        X = Common.R1();
      }

      ioblocking = (int)(X * block_time_stddev) + block_time_average;

      X = X * (run_time_stddev + block_time_stddev);

      cputime = (int)X + run_time_average + block_time_average;
      // Додавання процесу у список процесів
      processVector.addElement(new sProcess(cputime, ioblocking, 0, 0, 0));

    }
    // Запуск алгоритму планування.
    result = SchedulingAlgorithm.run(runtime, processVector, result);
    // Запис результатів в файл.
    try (PrintStream out = new PrintStream(new FileOutputStream(resultsFile))) {
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Run Time Average: " + run_time_average);
      out.println("Run Time Standard Deviation: " + run_time_stddev);
      out.println("Block Time Average: " + block_time_average);
      out.println("Block Time Standard Deviation: " + block_time_stddev);
      out.println("Process #\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked");
      for (int i = 0; i < processVector.size(); i++) {
        sProcess process = (sProcess) processVector.elementAt(i);
        out.print(Integer.toString(i));
        if (i < 100) { out.print("\t\t"); } else { out.print("\t"); }
        out.print(Integer.toString(process.cputime));
        if (process.cputime < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(Integer.toString(process.ioblocking));
        if (process.ioblocking < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(Integer.toString(process.cpudone));
        if (process.cpudone < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.println(process.numblocked + " times");
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
  System.out.println("Completed.");
  }
}

