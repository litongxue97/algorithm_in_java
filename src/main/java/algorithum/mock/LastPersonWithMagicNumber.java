package algorithum.mock;

/**
 * @author : liqinzuo
 * @date : 2023/11/8
 * Description :
 */
import java.util.LinkedList;
import java.util.Queue;

public class LastPersonWithMagicNumber {
    public int findLastPerson(int[] magicNumbers) {
        int n = magicNumbers.length;
        Queue<Integer> remainingPeople = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            remainingPeople.add(i);
        }

        int currentIndex = 0;
        while (remainingPeople.size() > 1) {
            int magicNumber = magicNumbers[currentIndex];
            for (int i = 0; i < magicNumber; i++) { // 从0开始报数
                int nextPerson = remainingPeople.poll();
                remainingPeople.offer(nextPerson);
            }
            int personOut = remainingPeople.poll();
            System.out.println("出局编号" + personOut);
            System.out.println();
            currentIndex = personOut + 1; // 更新下一个报数的人，从0开始
        }

        return remainingPeople.poll();
    }

    public static void main(String[] args) {
        LastPersonWithMagicNumber solver = new LastPersonWithMagicNumber();
        int[] magicNumbers = {2, 1, 3, 4, 5}; // 假设每个人的魔法数字
        int lastPerson = solver.findLastPerson(magicNumbers);
        System.out.println("最后留下的人的编号是：" + lastPerson);
    }
}

