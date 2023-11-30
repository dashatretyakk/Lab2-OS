// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.


import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

  // Головний метод для запуску алгоритму планування.
  public static Results run(int runtime, Vector<sProcess> processVector, Results result) {
    int comptime = 0;
    int currentProcess = findShortestRemainingTime(processVector);
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Summary-Processes";  // Файл для збереження результатів.

    result.schedulingType = "Batch (SJF preemptive)";
    result.schedulingName = "Shortest Remaining Time Next (SRTF)";
    try (PrintStream out = new PrintStream(new FileOutputStream(resultsFile))) {


      // Отримання інформації про перший процес.
      sProcess process = (sProcess) processVector.elementAt(currentProcess);
      out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
      
      // Головний цикл симуляції роботи планувальника.
      while (comptime < runtime) {
        
        // Перевірка завершення роботи поточного процесу.
        if (process.cpudone == process.cputime) {
          completed++;
          out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
          
          // Перевірка завершення всіх процесів.
          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return result;
          }
          
          // Вибір нового поточного процесу.
          currentProcess = findShortestRemainingTime(processVector);
          process = (sProcess) processVector.elementAt(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
        }      

        // Перевірка блокування на вводі/виводі.
        if (process.ioblocking == process.ionext) {
          out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
          process.numblocked++;
          process.ionext = 0;
          // Вибір нового поточного процесу.
          currentProcess = findShortestRemainingTime(processVector);
          process = (sProcess) processVector.elementAt(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
        }        

        // Імітація виконання процесу.
        process.cpudone++;       
        // Імітація вводу/виводу, якщо він є.
        if (process.ioblocking > 0) {
          process.ionext++;
        }
        comptime++;
      }
      out.close();
    } catch (IOException e) {}
    result.compuTime = comptime;
    return result;
  }

  // Метод для знаходження процесу з найкоротшим залишковим часом.
  private static int findShortestRemainingTime(Vector<sProcess> processVector) {
    int shortestIndex = -1;
    int shortestTime = Integer.MAX_VALUE;

    // Пошук процесу з найкоротшим залишковим часом.
    for (int i = 0; i < processVector.size(); i++) {
        // Отримання інформації про поточний процес.
        sProcess process = processVector.elementAt(i);
        // Розрахунок залишкового часу.
        int currentTimeLeft = process.cputime - process.cpudone;
        // Перевірка, чи це поточний найкоротший залишковий час.
        if ((0 < currentTimeLeft) && (currentTimeLeft < shortestTime)) {
            shortestTime = currentTimeLeft;
            shortestIndex = i;
        }
    }

    return shortestIndex;
  }

}

