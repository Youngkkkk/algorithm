import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 加权轮询算法
 *
 * @author km_yang
 * @create 2021/7/30 11:04
 */
public class WeightedRoundRobin {

    /**
     * 上次选择的服务器索引下标
     */
    private int currentIndex = -1;

    /**
     * 当前调度的权值
     */
    private int currentWeight = 0;

    /**
     * 最大权重
     */
    private int maxWeight;

    /**
     * 权重最大公约数
     */
    private int gcdWeight;

    /**
     * 服务器列表
     */
    private List<Server> serverList = new ArrayList<>();

    public WeightedRoundRobin() {
        serverList.add(new Server("127.0.0.1", 8));
        serverList.add(new Server("127.0.0.2", 6));
        serverList.add(new Server("127.0.0.3", 4));
        serverList.add(new Server("127.0.0.4", 2));
        maxWeight = serverList.stream().max(Comparator.comparing(server -> server.getWeight())).get().getWeight();
        gcdWeight = getGCDServerWeight();
    }

    /**
     * 算法流程：
     * 在服务器数组S中，首先计算所有服务器权重的最大值max(S)，以及所有服务器权重的最大公约数gcd(S)。
     * currentIndex表示上次请求选择的服务器的索引，初始值为-1；current_weight表示当前调度的权值，初始值为0
     * 当请求到来时，从currentIndex + 1开始轮询服务器数组S，找到其中权重大于等于current_weight的第一个服务器，用于处理该请求。
     * 在轮询服务器数组时，如果到达了数组末尾，则重新从头开始搜索，并且增加current_weight的值：current_weight += gcd(S)。
     * 如果current_weight大于等于max(S)，则将其重置为0。
     * 注意边界值判断
     */
    public synchronized Server getServer() {
        while (true) {
            currentIndex = (currentIndex + 1) % serverList.size();
            if (currentIndex == 0) {
                currentWeight = currentWeight >= maxWeight ? gcdWeight : currentWeight + gcdWeight;
            }
            if (serverList.get(currentIndex).getWeight() >= currentWeight) {
                return serverList.get(currentIndex);
            }
        }
    }

    public int gcd(int a, int b) {
        int max = Math.max(a, b);
        int min = Math.min(a, b);
        int gcd = 1;
        for (int i = 2; i <= min; i++) {
            if (min % i == 0 && max % i == 0) {
                gcd = i;
            }
        }
        return gcd;
    }

    public int getGCDServerWeight() {
        int result = 1;
        for (int i = 0; i < serverList.size() - 1; i++) {
            result = Math.max(result, gcd(serverList.get(i).getWeight(), serverList.get(i + 1).getWeight()));
        }
        return result;
    }

    public static void main(String[] args) {
        WeightedRoundRobin weightedRoundRobin = new WeightedRoundRobin();
        /*for (int i = 0; i < 20; i++) {
            Server server = weightedRoundRobin.getServer();
            System.out.println(String.format("ip:%s, weight:%s", server.getIp(), server.getWeight()));
        }*/
        
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                Server server = weightedRoundRobin.getServer();
                System.out.println(String.format("ip:%s, weight:%s", server.getIp(), server.getWeight()));
            }).start();

        }

    }

    class Server {
        private String ip;
        private int weight;

        public Server(String ip, int weight) {
            this.ip = ip;
            this.weight = weight;
        }

        public String getIp() {
            return ip;
        }

        public int getWeight() {
            return weight;
        }
    }

}
