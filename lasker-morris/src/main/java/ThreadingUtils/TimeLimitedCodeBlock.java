package ThreadingUtils;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Allows one to call a thread with a given timeout. Implementation taken from:
 * https://stackoverflow.com/questions/5715235/java-set-timeout-on-a-certain-block-of-code
 * 
 * 
 */
public class TimeLimitedCodeBlock {

    /**
     * Calls a runnable object, with a given timeout.
     * @param runnable is the runnable object to run.
     * @param timeout is the timeout.
     * @param timeUnit is the unit of the timeout.
     * @throws Exception
     */
  public static void runWithTimeout(final Runnable runnable, long timeout, TimeUnit timeUnit) throws Exception {
    runWithTimeout(new Callable<Object>() {
      public Object call() throws Exception {
        runnable.run();
        return null;
      }
    }, timeout, timeUnit);
  }

  /**
   * Implements the actual runnable code block with timeout.
   */
  public static <T> T runWithTimeout(Callable<T> callable, long timeout, TimeUnit timeUnit) throws Exception {
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    final Future<T> future = executor.submit(callable);
    executor.shutdown();
    try {
      return future.get(timeout, timeUnit);
    }
    catch (TimeoutException e) {
      future.cancel(true);
      throw e;
    }
    catch (ExecutionException e) {
      Throwable t = e.getCause();
      if (t instanceof Error) {
        throw (Error) t;
      } else if (t instanceof Exception) {
        throw (Exception) t;
      } else {
        throw new IllegalStateException(t);
      }
    }
  }

}