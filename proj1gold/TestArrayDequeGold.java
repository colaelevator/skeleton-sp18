import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> stu = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sol = new ArrayDequeSolution<>();
        for (int i = 0; i < 10; i++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                stu.addFirst(i);
                sol.addFirst(i);
            } else {
                stu.addLast(i);
                sol.addLast(i);
            }
        }

        for (int i = 0; i < 10; i++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.33) {
                assertEquals(stu.isEmpty(),sol.isEmpty());
            } else if (numberBetweenZeroAndOne <= 0.66) {
                assertEquals(stu.size(),sol.size());
            } else {
                assertEquals(stu.get(i),sol.get(i));
            }
        }

        for (int i = 0; i < 10; i++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne <= 0.5) {
                Integer x = stu.removeFirst();
                Integer y = sol.removeFirst();
                assertEquals("error!\nRandom num "+x+" is not equals to "+y+"!",x,y);
            } else {
                Integer x = stu.removeLast();
                Integer y = sol.removeLast();
                assertEquals("error!\nRandom num "+x+" is not equals to "+y+"!",x,y);
            }
        }

    }
}
