package Task2;

public class BallThread extends Thread {
    private Ball b;
    private BallCanvas ballCanvas;
    private CatchedBallsCounter coughtBalls;

    public BallThread(Ball ball, CatchedBallsCounter coughtBalls){
        b = ball;
        this.coughtBalls = coughtBalls;
    }

    @Override
    public void run() {
        try {
            while (!b.InHole())
            {
                b.move();
                System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(2);
            }
            coughtBalls.increment();
            b.deleteBall();
        }
        catch(InterruptedException ex)
        {
            if(!ex.getMessage().equals("Sleep interrupted"))
                System.out.println(ex);
        }
    }
}

