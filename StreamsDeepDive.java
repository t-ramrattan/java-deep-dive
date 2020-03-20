import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;
// https://jaxenter.com/java-performance-tutorial-how-fast-are-the-java-8-streams-118830.html
public class StreamsDeepDive {

  private static final Random random = new Random();

  private static Function<Integer[], Integer> findMaxLoop = (a) -> {
    System.out.print("int-array-for-loop: ");
    int m = Integer.MIN_VALUE;
    for (int i : a) {
      if (i > m) {
        m = i;
      }
    }
    return m;
  };

  private static Function<Integer[], Integer> seqStream = (a) -> {
    System.out.print("int-array-sequential-stream: ");
    return Arrays.stream(a).reduce(Integer.MIN_VALUE, Math::max);
  };

  private static Function<Integer[], Integer> parallelStream = (a) -> {
    System.out.print("int-array-parallel-stream: ");
    return Arrays.stream(a).reduce(Integer.MIN_VALUE, Math::max);
  };

  private static Function<List<Integer>, Integer> listForLoop = (a) -> {
    System.out.print("array-list-for-loop: ");
    int m = Integer.MIN_VALUE;
    for (int i : a) {
      if (i > m) {
        m = i;
      }
    }
    return m;
  };

  private static Function<List<Integer>, Integer> listSeqStream = (l) -> {
    System.out.print("array-list-seq-stream: ");
    return l.parallelStream().reduce(Integer.MIN_VALUE, Math::max);
  };

  private static Function<List<Integer>, Integer> listParallelStream = (l) -> {
    System.out.print("array-list-parallel-stream: ");
    return l.parallelStream().reduce(Integer.MIN_VALUE, Math::max);
  };




  public static void main(String[] args) {
    Map<String, Function<Integer[], Integer>> arryFunctions = new HashMap<>();
    Map<String, Function<List<Integer>, Integer>> listFunctions = new HashMap<>();
    
    arryFunctions.put("int-array-for-loop", findMaxLoop);
    arryFunctions.put("int-array-sequential-stream", seqStream);
    arryFunctions.put("int-array-parallel-stream", parallelStream);

    listFunctions.put("array-list-for-loop", listForLoop);
    listFunctions.put("array-list-seq-stream", listSeqStream);
    listFunctions.put("array-list-parallel-stream", listParallelStream);

    String input = "";
    Console console = System.console();
    while (!"q".equals(input)) {
      System.out.print("enter the sample size (q to quite): ");
      input = console.readLine();

      if (input.equals("q")) {
        continue;
      }

      int size = 0;
      String name;
      try {
        String[] s = input.split(",");
        name = s[0]; 
        size = Integer.parseInt(s[1]);
      } catch (NumberFormatException ex) {
        System.out.println("invalid input: " + input);
        continue;
      }
      random.setSeed(System.currentTimeMillis());
      Integer[] arr = new Integer[size];
      for (int i = 0; i < size; i++) {
        arr[i] = random.nextInt();
      }
      ArrayList<Integer> list = new ArrayList<>();
      Collections.addAll(list, arr);
      if (name.contains("list")) {
        runTest(list, listFunctions.get(name));        
      } else {
        runTest(arr, arryFunctions.get(name));
      }

      // runTest(arr, findMaxLoop);
      // runTest(arr, seqStream);
      // runTest(arr, parallelStream);

      // runTest(list, listForLoop);
      // runTest(list, listSeqStream);
      // runTest(list, listParallelStream);
    }

  }

  private static void runTest(Integer[] arr, Function<Integer[], Integer> method) {
    long start = System.nanoTime();
    method.apply(arr);
    long end = System.nanoTime();
    System.out.println(String.format("%.2f ms", (end - start) / 1000000f));
  }

  private static void runTest(List<Integer> list, Function<List<Integer>, Integer> method) {
    long start = System.nanoTime();
    method.apply(list);
    long end = System.nanoTime();
    System.out.println(String.format("%.2f ms", (end - start) / 1000000f));
  }

}
